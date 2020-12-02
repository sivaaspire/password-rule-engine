package com.password.validator.rule.engine.utility;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validation rule related to character sequence
 */
@Component
public class CharacterSequenceValiationRule extends AbstractValidationRule {

    

    Pattern passwordPattern;

    @Autowired
    public CharacterSequenceValiationRule(@Value("${charactersequence.validation.regex}") String regex, MessageSource messageSource) {
        super(PasswordEngineUtils.CHAR_RULE_NAME, messageSource, PasswordEngineUtils.CHAR_MESSAGE_ID, null);
        passwordPattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
    }

    /**
     * This method checks whether provided string contains any sequence of characters immediately followed by the same sequence
     *
     * @param str - string to be validated
     * @return false if the input string contain any sequence of characters immediately followed by the same sequence
     */
    @Override
    public boolean doValidate(String str) {
        Matcher matcher = passwordPattern.matcher(str);
        return !matcher.find();
    }
}
