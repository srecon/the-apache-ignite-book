package com.blu.imdg.example5;

import com.blu.imdg.common.HttpAuditClient;
import com.blu.imdg.common.JSEvaluate;
import com.blu.imdg.common.XsdValidator;
import org.apache.http.client.HttpClient;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.compute.ComputeJobContext;
import org.apache.ignite.compute.ComputeJobSibling;
import org.apache.ignite.compute.ComputeTaskSession;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.JobContextResource;
import org.apache.ignite.resources.TaskSessionResource;
import com.blu.imdg.example3.ValidateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentMap;

/**
 * Job adapter with session support for distributed validation.
 */
public class ForkJoinWithSessionJobAdapter extends ComputeJobAdapter {
    private static final Logger log = LoggerFactory.getLogger(ForkJoinWithSessionJobAdapter.class);

    @TaskSessionResource
    private ComputeTaskSession session;

    @JobContextResource
    private ComputeJobContext jobCtx;

    @IgniteInstanceResource
    private Ignite ignite;

    private final ValidateMessage msg;

    public ForkJoinWithSessionJobAdapter(ValidateMessage msg) {
        this.msg = msg;
    }

    @Override
    public Boolean execute() throws IgniteException {
        try {
            var validateXsdResult = XsdValidator.validate(msg.msg(), msg.xsd());
            session.setAttribute(jobCtx.getJobId(), validateXsdResult);
            log.info("Job id={} validated", jobCtx.getJobId());

            if (!validateXsdResult) {
                log.warn("XSD validation failed for message {}, returning false", msg.id());
                return sendResultAndReturn(false);
            }

            for (ComputeJobSibling sibling : session.getJobSiblings()) {
                var siblingStep1Result = session.waitForAttribute(sibling.getJobId(), 0);
                if (!siblingStep1Result) {
                    log.info("One sibling returned false for message {}", msg.id());
                    return sendResultAndReturn(false);
                }
            }

            var validateByJs = JSEvaluate.evaluateJs(msg.msg(), msg.js());
            return sendResultAndReturn(validateByJs);

        } catch (Exception err) {
            log.error("Job execution error for message {}", msg.id(), err);
            throw new IgniteException(err);
        }
    }

    @NotNull
    private Boolean sendResultAndReturn(Boolean result) throws URISyntaxException, IOException {
        ConcurrentMap<Object, Object> nodeLocalMap = ignite.cluster().nodeLocalMap();
        try (var client = HttpAuditClient.createHttpClient(nodeLocalMap)) {
            return HttpAuditClient.sendResult(client, result, msg.id());
        }
    }
}
