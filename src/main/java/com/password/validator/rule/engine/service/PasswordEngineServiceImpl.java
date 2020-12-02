package com.password.validator.rule.engine.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.password.validator.rule.engine.exception.PasswordEngineException;
import com.password.validator.rule.engine.utility.PasswordEngineResponse;
import com.password.validator.rule.engine.utility.ValidationRule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PasswordEngineServiceImpl implements PasswordEngineService {

    List<ValidationRule> configuredValidationRules = new ArrayList<>();

    private static final String INVALID_RULE_MESSAGE = "configured password validation rule {} is not available in system.";

    /**
     * @param passwordValidationRules - All available rules
     * @param configuredRules         - Rules defined in configuration file
     */
    @Autowired
    public PasswordEngineServiceImpl(Map<String, ValidationRule> passwordValidationRules,
                               @Value("#{'${password.validation.rules:}'.split(',')}") List<String> configuredRules) {
        configuredRules.stream()
                .filter(configuredRule -> !StringUtils.isEmpty(configuredRule))
                .filter(configuredRule -> isValidRule(configuredRule, passwordValidationRules))
                .map(passwordValidationRules::get)
                .forEach(configuredValidationRules::add);
        if (CollectionUtils.isEmpty(configuredValidationRules)) {
            log.info("There are no configured rules. So, validating with all available rules.");
            configuredValidationRules.addAll(passwordValidationRules.values());
        }
    }

    private boolean isValidRule(String configuredRule, Map<String, ValidationRule> passwordValidationRules) {
        if (passwordValidationRules.get(configuredRule) == null) {
            String invalidRuleMessage = MessageFormat.format("configured password validation rule {0} is not available in system.", configuredRule);
            log.error(invalidRuleMessage);
            throw new PasswordEngineException(invalidRuleMessage);
        }
        return true;
    }

    @Override
    public List<PasswordEngineResponse> validate(String password) {
        if (StringUtils.isEmpty(password)) {
            log.warn("Password cannot be null or empty.");
            throw new IllegalArgumentException("password cannot be empty or null");
        }
        return configuredValidationRules.stream().map(passwordValidationRule -> passwordValidationRule.validate(password)).collect(Collectors.toList());
    }


}
