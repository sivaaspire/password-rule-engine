package com.password.validator.rule.engine.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.password.validator.rule.engine.service.PasswordEngineServiceImpl;
import com.password.validator.rule.engine.utility.PasswordEngineResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Handles Rest requests related to password validation
 */
@RestController
@RequestMapping(path = "/validate")
@Api(value = "Password Rule Engine", description = "As a process of Password rule engine service, system needs to validate based on setup configured.")
public class PasswordEngineController {

	@Autowired
    private PasswordEngineServiceImpl passwordEngineService;

    /**
     * This method validates password using the rules configured in system
     * @param password - password to be validated
     * @return  represents the entire HTTP response contains each rule result
     *          Status Code 200 - Ok.
     */
	@ApiOperation(value = "To validate the password using rule engine", response = Iterable.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully password validated"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
    @PostMapping(value = "/password")
    public ResponseEntity<List<PasswordEngineResponse>> validate(@RequestBody String password) {
        List<PasswordEngineResponse> ruleResults = passwordEngineService.validate(password);
        return new ResponseEntity<List<PasswordEngineResponse>>(ruleResults, HttpStatus.OK);
    }


}
