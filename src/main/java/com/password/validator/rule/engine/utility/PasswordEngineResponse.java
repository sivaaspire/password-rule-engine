package com.password.validator.rule.engine.utility;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PasswordEngineResponse {

	@ApiModelProperty(notes = "Denotes the Rule Name")
	private String name;
	
	@ApiModelProperty(notes = "Describe the whether password was validated or not")
	private boolean success;
	
	@ApiModelProperty(notes = "Describe the error message of password rule engine")
	private String errorMessage;

}
