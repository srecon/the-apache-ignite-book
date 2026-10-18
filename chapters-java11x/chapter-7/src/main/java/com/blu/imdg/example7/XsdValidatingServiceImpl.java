package com.blu.imdg.example7;

import com.blu.imdg.common.HttpAuditClient;
import com.blu.imdg.common.XsdValidator;
import com.blu.imdg.example3.ValidateMessage;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;

/**
 * XSD validation service implementation deployed on Ignite nodes.
 */
public class XsdValidatingServiceImpl implements XsdValidatingService, Service {
    private static final Logger log = LoggerFactory.getLogger(XsdValidatingServiceImpl.class);

    private CloseableHttpClient auditClient;

    @Override
    public boolean isOk(ValidateMessage msg) {
        var validateXsdResult = XsdValidator.validate(msg.msg(), msg.xsd());
        sendResult(msg, validateXsdResult);
        return validateXsdResult;
    }

    private void sendResult(ValidateMessage msg, Boolean validateXsdResult) {
        try {
            HttpAuditClient.sendResult(auditClient, validateXsdResult, msg.id());
        } catch (Exception err) {
            log.error("Failed to send audit result for message {}", msg.id(), err);
        }
    }

    //service methods

    @Override
    public void cancel(ServiceContext serviceContext) {
        log.info("Cancelling XSD validation service");
        try {
            auditClient.close();
        } catch (IOException e) {
            log.error("Error closing audit client", e);
        }
    }

    @Override
    public void init(ServiceContext serviceContext) throws Exception {
        log.info("Initializing XSD validation service");
        auditClient = (CloseableHttpClient) HttpAuditClient.createHttpClient();
    }

    @Override
    public void execute(ServiceContext serviceContext) throws Exception {
    }
}
