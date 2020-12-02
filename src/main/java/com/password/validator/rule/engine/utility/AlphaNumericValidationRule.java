package com.password.validator.rule.engine.utility;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validation rule related to letters and digits in password
 */
@Component
public class AlphaNumericValidationRule extends AbstractValidationRule {

    private Pattern passwordPattern;

    @Autowired
    public AlphaNumericValidationRule(@Value("${alphanumeric.validation.regex}") String regex, MessageSource messageSource) {
        super(PasswordEngineUtils.ALPHA_NUMERIC_RULE_NAME, messageSource, PasswordEngineUtils.ALPHA_NUMERIC_MESSAGE_ID, null);
        passwordPattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
    }

    /**
     * This method checks whether provided string consist of a mixture of lowercase letters and numerical digits only, with at least one of each
     *
     * @param str - string to be validated
     * @return false if the input string does not contain consist of a mixture of lowercase letters and numerical digits or at least one of each
     */
    @Override
    public boolean doValidate(String str) {
        Matcher matcher = passwordPattern.matcher(str);
        return matcher.matches();
    }
}
