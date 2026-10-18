package com.blu.imdg.example4;

import com.blu.imdg.common.HttpAuditClient;
import org.apache.http.client.HttpClient;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.resources.IgniteInstanceResource;
import com.blu.imdg.common.JSEvaluate;
import com.blu.imdg.common.XsdValidator;
import com.blu.imdg.example3.ValidateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.ConcurrentMap;

/**
 * Extended job adapter that sends audit results via HTTP.
 */
public class ForkJoinJobAdapterExt extends ComputeJobAdapter {
    private static final Logger log = LoggerFactory.getLogger(ForkJoinJobAdapterExt.class);

    @IgniteInstanceResource
    private Ignite ignite;

    private final ValidateMessage msg;

    public ForkJoinJobAdapterExt(ValidateMessage msg) {
        this.msg = msg;
    }

    @Override
    public Boolean execute() throws IgniteException {
        try {
            var validateXsdResult = XsdValidator.validate(msg.msg(), msg.xsd());
            var validateByJs = JSEvaluate.evaluateJs(msg.msg(), msg.js());

            var result = validateXsdResult && validateByJs;

            ConcurrentMap<Object, Object> nodeLocalMap = ignite.cluster().nodeLocalMap();

            try (var client = HttpAuditClient.createHttpClient(nodeLocalMap)) {
                return HttpAuditClient.sendResult(client, result, msg.id());
            }

        } catch (Exception err) {
            log.error("Job execution failed for message {}", msg.id(), err);
            throw new IgniteException(err);
        }
    }

}
