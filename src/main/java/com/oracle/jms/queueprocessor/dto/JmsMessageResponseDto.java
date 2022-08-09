package com.oracle.jms.queueprocessor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JmsMessageResponseDto {

	@JsonProperty("message")
	private String message;
	
}
