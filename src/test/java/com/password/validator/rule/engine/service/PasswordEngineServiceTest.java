package com.password.validator.rule.engine.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import com.password.validator.rule.engine.exception.PasswordEngineException;
import com.password.validator.rule.engine.utility.AlphaNumericValidationRule;
import com.password.validator.rule.engine.utility.CharacterSequenceValiationRule;
import com.password.validator.rule.engine.utility.LengthValidationRule;
import com.password.validator.rule.engine.utility.PasswordEngineResponse;
import com.password.validator.rule.engine.utility.ValidationRule;

@RunWith(MockitoJUnitRunner.class)
public class PasswordEngineServiceTest {

    private static final int PASSWORD_MIN_LENGTH = 5;
    private static final int PASSWORD_MAX_LENGTH = 12;

    private static final String alphaNumericRegex = "[\\p{Lower}]+[\\p{Digit}]+|[\\p{Digit}]+[\\p{Lower}]+";
    private static final String charSequenceRegex = "(\\p{Alnum}{2,})\\1";

    @Mock
    private MessageSource messageSource;

    private ValidationRule alphaNumericValidationRule;

    private ValidationRule passwordLengthValidationRule;

    private ValidationRule characterSequenceValidationRule;

    private PasswordEngineService passwordEngineService;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() throws Exception{
        alphaNumericValidationRule = spy(new AlphaNumericValidationRule(alphaNumericRegex, messageSource));
        passwordLengthValidationRule = spy(new LengthValidationRule(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH, messageSource));
        characterSequenceValidationRule = spy(new CharacterSequenceValiationRule(charSequenceRegex, messageSource));

        when(messageSource.getMessage(anyString(),any(Object[].class),anyObject())).thenReturn("Test Message");
    }

    @Test
    public void testValidate() {
        Map<String, ValidationRule> passwordValidationRules = getPasswordValidationRules();
        passwordEngineService = new PasswordEngineServiceImpl(passwordValidationRules, Collections.emptyList());

        List<PasswordEngineResponse> PasswordEngineResponses = passwordEngineService.validate("test1234");
        verify(alphaNumericValidationRule, times(1)).validate("test1234");
        verify(passwordLengthValidationRule, times(1)).validate("test1234");
        verify(characterSequenceValidationRule, times(1)).validate("test1234");
        PasswordEngineResponses.forEach(PasswordEngineResponse -> {
                    Assert.assertEquals(PasswordEngineResponse.isSuccess(), true);
                    Assert.assertEquals(PasswordEngineResponse.getErrorMessage(), null);
                }
        );
    }

    @Test
    public void testValidate_null() {
        Map<String, ValidationRule> passwordValidationRules = getPasswordValidationRules();
        passwordEngineService = new PasswordEngineServiceImpl(passwordValidationRules, Collections.emptyList());

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("password cannot be empty or null");

        passwordEngineService.validate(null);
        verifyZeroInteractions(alphaNumericValidationRule,
                passwordLengthValidationRule,
                characterSequenceValidationRule);
    }

    @Test
    public void testValidate_multipleRules_oneRuleFailed() {
        Map<String, ValidationRule> passwordValidationRules = getPasswordValidationRules();
        List<String> configuredRules = Arrays.asList("LENGTH", "ALPHA_NUMERIC");

        passwordEngineService = new PasswordEngineServiceImpl(passwordValidationRules, configuredRules);

        List<PasswordEngineResponse> PasswordEngineResponses = passwordEngineService.validate("testing");
        verify(passwordLengthValidationRule, times(1)).validate("testing");
        verify(alphaNumericValidationRule, times(1)).validate("testing");
        verifyZeroInteractions(characterSequenceValidationRule);
        PasswordEngineResponses.forEach(PasswordEngineResponse -> {
                    if ("ALPHA_NUMERIC".equals(PasswordEngineResponse.getName())) {
                        Assert.assertEquals(PasswordEngineResponse.isSuccess(), false);
                        Assert.assertNotEquals(PasswordEngineResponse.getErrorMessage(), null);
                    } else {
                        Assert.assertEquals(PasswordEngineResponse.isSuccess(), true);
                        Assert.assertEquals(PasswordEngineResponse.getErrorMessage(), null);
                    }
                }
        );
    }

    @Test
    public void testValidate_multipleRules_allRulesFailed() {
        Map<String, ValidationRule> passwordValidationRules = getPasswordValidationRules();
        List<String> configuredRules = Arrays.asList("CHAR_SEQUENCE", "ALPHA_NUMERIC");

        passwordEngineService = new PasswordEngineServiceImpl(passwordValidationRules, configuredRules);

        List<PasswordEngineResponse> PasswordEngineResponses = passwordEngineService.validate("testtest");
        verify(characterSequenceValidationRule, times(1)).validate("testtest");
        verify(alphaNumericValidationRule, times(1)).validate("testtest");
        verifyZeroInteractions(passwordLengthValidationRule);
        PasswordEngineResponses.forEach(PasswordEngineResponse -> {
                    Assert.assertEquals(PasswordEngineResponse.isSuccess(), false);
                    Assert.assertNotEquals(PasswordEngineResponse.getErrorMessage(), null);
                }
        );
    }

    @Test
    public void testValidate_invalidRuleName() {
        expectedEx.expect(PasswordEngineException.class);
        expectedEx.expectMessage("configured password validation rule INVALID is not available in system.");

        Map<String, ValidationRule> passwordValidationRules = getPasswordValidationRules();
        List<String> configuredRules = Arrays.asList("INVALID");

        passwordEngineService = new PasswordEngineServiceImpl(passwordValidationRules, configuredRules);

        passwordEngineService.validate("test1234");
        verifyZeroInteractions(passwordLengthValidationRule,
                alphaNumericValidationRule,
                characterSequenceValidationRule);
    }

    private Map<String, ValidationRule> getPasswordValidationRules() {
        Map<String, ValidationRule> passwordValidationRules = new HashMap<>();
        passwordValidationRules.put("ALPHA_NUMERIC", alphaNumericValidationRule);
        passwordValidationRules.put("LENGTH", passwordLengthValidationRule);
        passwordValidationRules.put("CHAR_SEQUENCE", characterSequenceValidationRule);
        return passwordValidationRules;
    }


}
