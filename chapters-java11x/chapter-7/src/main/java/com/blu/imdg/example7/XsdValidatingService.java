package com.blu.imdg.example7;

import com.blu.imdg.example3.ValidateMessage;

/**
 * Service interface for XSD validation, deployable as an Ignite service.
 */
public interface XsdValidatingService {
    boolean isOk(ValidateMessage msg);
}
