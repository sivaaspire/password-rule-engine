package com.password.validator.rule.engine.utility;

/**
 * Implement this interface to provide a new validation rule
 */
public interface ValidationRule {
    /**
     * This method validates input string
     *
     * @param str - string to be validated
     * @return RuleResult if the validation fails
     */
    PasswordEngineResponse validate(String str);
}
