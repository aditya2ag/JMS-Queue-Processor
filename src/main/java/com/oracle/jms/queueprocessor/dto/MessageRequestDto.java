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
public class MessageRequestDto implements Serializable {
	private static final long serialVersionUID = 1L;
	// "t3://100.76.135.191:7003";

	@ApiModelProperty(required = true, value = "jmsServerIP is mandatory.")
	@NotNull(message = "Weblogic Server IP/Hostname cannot be empty or null")
	@Size(message="jmsServerIP length must be more than 0" ,min = 1)
	@JsonProperty("jmsServerIP")
	private String jmsServerIP;

	@ApiModelProperty(required = true, value = "jsmServerPort is mandatory.")
	@NotNull(message = "JMS Server Port cannot be empty or null")
	@Size(message="jsmServerPort length must be 4" ,min = 4,max=4)
	@JsonProperty("jsmServerPort")
	private String jsmServerPort;

	// "com.infinity.weblogic12c.test.cf";
	@ApiModelProperty(required = true, value = "jmsConnectionFactory is mandatory.")
	@NotNull(message = "JMS connection factory cannot be empty or null")
	@Size(message="jmsConnectionFactory length must be more than 0" ,min = 1)
	@JsonProperty("jmsConnectionFactory")
	private String jmsConnectionFactory;

	@ApiModelProperty(required = true, value = "jmsQueue is mandatory.")
	@NotNull(message = "JMS queue cannot be empty or null")
	@Size(message="jmsQueue length must be more than 0" ,min = 1)
	@JsonProperty("jmsQueue")
	private String jmsQueue;
	// "com.infinity.weblogic12c.test.dq";

	@ApiModelProperty(required = true, value = "message is mandatory.")
	@NotNull(message = "Message cannot be empty or null")
	@Size(message="message length must be more than 0" ,min = 1)
	@JsonProperty("message")
	private String message;

}
