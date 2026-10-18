package com.blu.imdg.example9;

import com.blu.imdg.common.HttpAuditClient;
import com.blu.imdg.example9.exception.LogServiceException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Log service implementation that sends audit results via HTTP.
 */
public class LogServiceImpl implements LogService, Service {
    private static final Logger log = LoggerFactory.getLogger(LogServiceImpl.class);

    private CloseableHttpClient auditClient;

    @Override
    public void logOperation(String operationCode, Boolean result) throws LogServiceException {
        try {
            HttpAuditClient.sendResult(auditClient, result, operationCode);
            log.debug("Logged operation={} result={}", operationCode, result);
        } catch (Exception err) {
            log.error("Failed to log operation: {}", operationCode, err);
            throw new LogServiceException(err);
        }
    }

    @Override
    public void cancel(ServiceContext serviceContext) {
        log.info("Cancelling LogServiceImpl");
        try {
            auditClient.close();
        } catch (IOException e) {
            log.error("Error closing audit client in LogServiceImpl", e);
        }
    }

    @Override
    public void init(ServiceContext serviceContext) throws Exception {
        log.info("Initializing LogServiceImpl");
        auditClient = (CloseableHttpClient) HttpAuditClient.createHttpClient();
    }

    @Override
    public void execute(ServiceContext serviceContext) throws Exception {

    }
}
