package com.oracle.jms.queueprocessor.controller;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.jms.queueprocessor.dto.JmsMessageResponseDto;
import com.oracle.jms.queueprocessor.dto.MessagePullReqHeaderDto;
import com.oracle.jms.queueprocessor.dto.MessageRequestDto;
import com.oracle.jms.queueprocessor.dto.MessageResponseDto;
import com.oracle.jms.queueprocessor.service.JMSQueueService;

@RestController
@RequestMapping("/service")
@CrossOrigin(origins = { "*" }, allowCredentials = "false", allowedHeaders = { "*" }, maxAge = 60 * 30, methods = {
		RequestMethod.GET, RequestMethod.POST })

public class JmsQueueServiceController {

	@Autowired
	private JMSQueueService jmsQueueService;

	@PostMapping("/v1/postMessage")
	public ResponseEntity<MessageResponseDto> postMessagetoQueue(
			@Valid @RequestBody MessageRequestDto messageRequestDto) {
		return this.jmsQueueService.postMessageToQueue(messageRequestDto);
	};

	@GetMapping("/v1/pullMessages")
	public ResponseEntity<List<JmsMessageResponseDto>> pullMessageFromQueue(@RequestHeader String jmsServerIP,
			@RequestHeader String jsmServerPort, @RequestHeader String jmsConnectionFactory,
			@RequestHeader String jmsQueue) {

		MessagePullReqHeaderDto messagePullReqHeaderDto = new MessagePullReqHeaderDto();
		messagePullReqHeaderDto.setJmsServerIP(jmsServerIP);
		messagePullReqHeaderDto.setJsmServerPort(jsmServerPort);
		messagePullReqHeaderDto.setJmsConnectionFactory(jmsConnectionFactory);
		messagePullReqHeaderDto.setJmsQueue(jmsQueue);

		return this.jmsQueueService.pullMessageFromQueue(messagePullReqHeaderDto);
	};

}
