package com.oracle.jms.queueprocessor.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Component
@Data
public class MessageResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String response;

}
