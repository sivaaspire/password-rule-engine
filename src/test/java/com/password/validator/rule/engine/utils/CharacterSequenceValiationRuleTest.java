package com.password.validator.rule.engine.utils;


import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import com.password.validator.rule.engine.utility.CharacterSequenceValiationRule;
import com.password.validator.rule.engine.utility.PasswordEngineResponse;
import com.password.validator.rule.engine.utility.ValidationRule;

@RunWith(MockitoJUnitRunner.class)
public class CharacterSequenceValiationRuleTest {

    private static final String VALIDATION_ERROR_MESSAGE = "password must not contain any sequence of characters immediately followed by the same sequence";
    private static final String regex = "(\\p{Alnum}{2,})\\1";
    private static final String CHAR_SEQ_VALIDATION = "charactersequence.validation";

    private ValidationRule validationRule;

    @Mock
    private MessageSource messageSource;

    @Before
    public void setUp() throws Exception{
        validationRule = new CharacterSequenceValiationRule(regex, messageSource);

        when(messageSource.getMessage(eq(CHAR_SEQ_VALIDATION),any(Object[].class),anyObject())).thenReturn(VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate(){
        PasswordEngineResponse response = validationRule.validate("test1234");
        Assert.assertEquals(response.isSuccess(), true);
        Assert.assertEquals(response.getErrorMessage(), null);
    }

    @Test
    public void testValidate_ImmediateCharSeq(){
        PasswordEngineResponse response = validationRule.validate("testtest123");
        Assert.assertEquals(response.isSuccess(), false);
        Assert.assertEquals(response.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate_NotImmediateCharSeq(){
        PasswordEngineResponse response = validationRule.validate("test123test");
        Assert.assertEquals(response.isSuccess(), true);
        Assert.assertEquals(response.getErrorMessage(), null);
    }
}
