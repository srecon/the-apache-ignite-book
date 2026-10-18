package com.blu.imdg.example9.exception;

/**
 * Runtime exception thrown when an account is not found in the cache.
 */
public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
