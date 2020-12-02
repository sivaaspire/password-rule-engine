package com.password.validator.rule.engine.exception;

/**
 * Exception thrown when password validation fails.
 */
public class PasswordEngineException extends RuntimeException {

	private static final long serialVersionUID = -8980875820201468385L;

	public PasswordEngineException(String message) {
        super(message);
    }
}
