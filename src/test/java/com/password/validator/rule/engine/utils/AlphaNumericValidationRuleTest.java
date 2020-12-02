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

import com.password.validator.rule.engine.utility.AlphaNumericValidationRule;
import com.password.validator.rule.engine.utility.PasswordEngineResponse;
import com.password.validator.rule.engine.utility.ValidationRule;

@RunWith(MockitoJUnitRunner.class)
public class AlphaNumericValidationRuleTest {

    private static final String VALIDATION_ERROR_MESSAGE = "password must consist of a mixture of lowercase letters and numerical digits only, with at least one of each";
    private static final String regex = "[\\p{Lower}]+[\\p{Digit}]+|[\\p{Digit}]+[\\p{Lower}]+";
    private static final String ALPHANUMERIC_VALIDATION = "alphanumeric.validation";

    @Mock
    private MessageSource messageSource;

    private ValidationRule validationRule;

    @Before
    public void setUp() throws Exception{
        validationRule = new AlphaNumericValidationRule(regex, messageSource);

        when(messageSource.getMessage(eq(ALPHANUMERIC_VALIDATION),any(Object[].class),anyObject())).thenReturn(VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate(){
        PasswordEngineResponse response = validationRule.validate("test1234");
        Assert.assertEquals(response.isSuccess(), true);
        Assert.assertEquals(response.getErrorMessage(), null);
    }

    @Test
    public void testValidate_upperCase(){
        PasswordEngineResponse response = validationRule.validate("TEST");
        Assert.assertEquals(response.isSuccess(), false);
        Assert.assertEquals(response.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate_digits(){
        PasswordEngineResponse response = validationRule.validate("123");
        Assert.assertEquals(response.isSuccess(), false);
        Assert.assertEquals(response.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate_chars(){
        PasswordEngineResponse response = validationRule.validate("test");
        Assert.assertEquals(response.isSuccess(), false);
        Assert.assertEquals(response.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate_specialChars(){
        PasswordEngineResponse response = validationRule.validate("test123$!@#%");
        Assert.assertEquals(response.isSuccess(), false);
        Assert.assertEquals(response.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate_upperLowerCase(){
        PasswordEngineResponse response = validationRule.validate("teST");
        Assert.assertEquals(response.isSuccess(), false);
        Assert.assertEquals(response.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate_upperLowerCase_Digits(){
        PasswordEngineResponse response = validationRule.validate("teST123");
        Assert.assertEquals(response.isSuccess(), false);
        Assert.assertEquals(response.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void testValidate_upperCase_Digits(){
        PasswordEngineResponse response = validationRule.validate("TEST123");
        Assert.assertEquals(response.isSuccess(), false);
        Assert.assertEquals(response.getErrorMessage(), VALIDATION_ERROR_MESSAGE);
    }
}
