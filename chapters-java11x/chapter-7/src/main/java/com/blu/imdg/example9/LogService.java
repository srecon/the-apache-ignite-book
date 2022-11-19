package com.blu.imdg.example9;

import com.blu.imdg.example9.exception.LogServiceException;

/**
 *
 */
public interface LogService {
    String NAME = "logService";

    void logOperation(String operationCode, Boolean result) throws LogServiceException;
}
