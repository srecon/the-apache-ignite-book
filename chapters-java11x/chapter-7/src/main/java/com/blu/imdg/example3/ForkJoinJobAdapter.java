package com.blu.imdg.example3;

import org.apache.ignite.IgniteException;
import org.apache.ignite.compute.ComputeJobAdapter;
import com.blu.imdg.common.JSEvaluate;
import com.blu.imdg.common.XsdValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Job that validates a message against XSD and evaluates a JS script.
 */
public class ForkJoinJobAdapter extends ComputeJobAdapter {
    private static final Logger log = LoggerFactory.getLogger(ForkJoinJobAdapter.class);

    private final ValidateMessage msg;

    public ForkJoinJobAdapter(ValidateMessage msg) {
        this.msg = msg;
    }

    @Override
    public Boolean execute() throws IgniteException {
        var validateXsdResult = XsdValidator.validate(msg.msg(), msg.xsd());
        var validateByJs = JSEvaluate.evaluateJs(msg.msg(), msg.js());

        log.info("msg={} validateXsdResult={} validateByJs={}", msg.id(), validateXsdResult, validateByJs);

        return validateXsdResult && validateByJs;
    }
}
