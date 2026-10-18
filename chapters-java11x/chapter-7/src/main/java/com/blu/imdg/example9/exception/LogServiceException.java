package com.blu.imdg.example9.exception;

/**
 * Runtime exception thrown when the log service encounters an error.
 */
public class LogServiceException extends RuntimeException {
    public LogServiceException(Throwable cause) {
        super(cause);
    }
}
