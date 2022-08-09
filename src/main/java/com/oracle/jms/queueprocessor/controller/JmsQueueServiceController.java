package com.oracle.jms.queueprocessor.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.jms.queueprocessor.dto.MessageRequestDto;
import com.oracle.jms.queueprocessor.dto.MessageResponseDto;
import com.oracle.jms.queueprocessor.service.JMSQueuePosterService;

@RestController
@RequestMapping("/service")
@CrossOrigin(origins = { "*" }, allowCredentials = "false", allowedHeaders = { "*" }, maxAge = 60 * 30, methods = {
		RequestMethod.GET, RequestMethod.POST })

public class JmsQueueServiceController {

	@Autowired
	private JMSQueuePosterService jmsQueuePosterService;

	@PostMapping("/v1/postMessage")
	public ResponseEntity<MessageResponseDto> postMessagetoQueue(@Valid @RequestBody MessageRequestDto messageRequestDto) {
		return this.jmsQueuePosterService.postMessageToQueue(messageRequestDto);
	};

	/*
	 * @GetMapping("/getEODConfigurations") public ResponseEntity<HashMap<String,
	 * String>> getAllEodAppConfig() { return
	 * this.eodMonitorAppApi.getAllEodConfiguration(); };
	 * 
	 * 
	 * @GetMapping("/getEodErrorLog") public ResponseEntity<List<EodErrorLogModel>>
	 * getEodErrorLogs() { return this.eodMonitorAppApi.getEodErrorLogs(); };
	 */
}
