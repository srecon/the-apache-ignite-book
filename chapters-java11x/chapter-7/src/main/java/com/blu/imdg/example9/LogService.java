package com.blu.imdg.example9;

import com.blu.imdg.example9.exception.LogServiceException;

/**
 * Service interface for logging operations.
 */
public interface LogService {
    String NAME = "logService";

    void logOperation(String operationCode, Boolean result) throws LogServiceException;
}
