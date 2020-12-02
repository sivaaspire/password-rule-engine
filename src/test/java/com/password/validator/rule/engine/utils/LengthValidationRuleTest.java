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

import com.password.validator.rule.engine.utility.LengthValidationRule;
import com.password.validator.rule.engine.utility.PasswordEngineResponse;
import com.password.validator.rule.engine.utility.ValidationRule;

@RunWith(MockitoJUnitRunner.class)
public class LengthValidationRuleTest {

    private static final String VALIDATION_ERROR_MESSAGE = "password length must be between 5 and 12 characters";
    private static final String LENGTH_VALIDATION = "length.validation";

    private ValidationRule validationRule;

    @Mock
    private MessageSource messageSource;

    @Before
    public void setUp() throws Exception{
        int PASSWORD_MIN_LENGTH = 5;
        int PASSWORD_MAX_LENGTH = 12;
        validationRule = new LengthValidationRule(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH, messageSource);

        when(messageSource.getMessage(eq(LENGTH_VALIDATION),any(Object[].class),anyObject())).thenReturn(VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate(){
        PasswordEngineResponse response = validationRule.validate("test1234");
        Assert.assertEquals(response.isSuccess(), true);
        Assert.assertEquals(response.getErrorMessage(), null);
    }

    @Test
    public void testValidate_5chars(){
        PasswordEngineResponse response = validationRule.validate("test5");
        Assert.assertEquals(response.isSuccess(), true);
        Assert.assertEquals(response.getErrorMessage(), null);
    }

    @Test
    public void testValidate_lessThan5chars(){
        PasswordEngineResponse response = validationRule.validate("testtest123");
        Assert.assertEquals(response.isSuccess(), true);
        Assert.assertEquals(response.getErrorMessage(), null);
    }

    @Test
    public void testValidate_12chars(){
        PasswordEngineResponse response = validationRule.validate("test12345678");
        Assert.assertEquals(response.isSuccess(), true);
        Assert.assertEquals(response.getErrorMessage(), null);
    }

    @Test
    public void testValidate_greaterThan12chars(){
        PasswordEngineResponse response = validationRule.validate("testing123456");
        Assert.assertEquals(response.isSuccess(), false);
        Assert.assertEquals(response.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }
}
