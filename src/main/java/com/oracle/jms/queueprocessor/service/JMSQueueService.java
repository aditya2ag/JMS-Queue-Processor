/* JMSQueuePoster is responsible for sending message to JMS queue set up in Weblogic server
 
 * Author : Aditya A G
  
 * */
package com.oracle.jms.queueprocessor.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.oracle.jms.queueprocessor.dto.JmsMessageResponseDto;
import com.oracle.jms.queueprocessor.dto.MessagePullReqHeaderDto;
import com.oracle.jms.queueprocessor.dto.MessageRequestDto;
import com.oracle.jms.queueprocessor.dto.MessageResponseDto;
import com.oracle.jms.queueprocessor.pull.JMSQueuePuller;
import com.oracle.jms.queueprocessor.push.JMSQueuePoster;

@Service
public class JMSQueueService {

	private static final Logger LOGGER = LoggerFactory.getLogger(JMSQueueService.class);

	private void debug(String msg) {
		LOGGER.debug(this.getClass().getName() + "-->" + msg);
		System.out.println(this.getClass().getName() + "-->" + msg);
	}

	@Autowired
	JMSQueuePoster jmsQueuePoster;
	@Autowired
	JMSQueuePuller jmsQueuePuller;

	public ResponseEntity<MessageResponseDto> postMessageToQueue(MessageRequestDto messageReq) {
		return this.jmsQueuePoster.postMessageToQueue(messageReq);
	}

	public ResponseEntity<List<JmsMessageResponseDto>> pullMessageFromQueue(MessagePullReqHeaderDto messagePullReqHeaderDto) {
		return this.jmsQueuePuller.pullMessageFromQueue(messagePullReqHeaderDto);
	}
}
