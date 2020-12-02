package com.password.validator.rule.engine.service;

import java.util.List;

import com.password.validator.rule.engine.utility.PasswordEngineResponse;

public interface PasswordEngineService {
	/**
	 * This method validates password using the rules configured in system
	 *
	 * @param password
	 *            - password to be validated
	 * @return collection of applied rule result
	 */
	List<PasswordEngineResponse> validate(String password);

}
