/* JMSQueuePoster is responsible for sending message to JMS queue set up in Weblogic server
 
 * Author : Aditya A G
  
 * */
package com.oracle.jms.queueprocessor.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.oracle.jms.queueprocessor.dto.MessageRequestDto;
import com.oracle.jms.queueprocessor.dto.MessageResponseDto;
import com.oracle.jms.queueprocessor.push.JMSQueuePoster;

@Service
public class JMSQueuePosterService {

	private static final Logger LOGGER = LoggerFactory.getLogger(JMSQueuePosterService.class);

	private void debug(String msg) {
		LOGGER.debug(this.getClass().getName() + "-->" + msg);
		System.out.println(this.getClass().getName() + "-->" + msg);
	}

	@Autowired
	JMSQueuePoster jmsQueuePoster;

	public ResponseEntity<MessageResponseDto> postMessageToQueue(MessageRequestDto messageReq) {
		return this.jmsQueuePoster.postMessageToQueue(messageReq);
	}

}
