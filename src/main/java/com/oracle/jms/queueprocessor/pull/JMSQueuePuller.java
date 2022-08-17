/* JMSQueuePoster is responsible for sending message to JMS queue set up in Weblogic server
 
 * Author : Aditya A G
  
 * */
package com.oracle.jms.queueprocessor.pull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.oracle.jms.queueprocessor.dto.JmsMessageResponseDto;
import com.oracle.jms.queueprocessor.dto.MessagePullReqHeaderDto;


@Component
public class JMSQueuePuller {

	private static final Logger LOGGER = LoggerFactory.getLogger(JMSQueuePuller.class);

	private void debug(String msg) {
		LOGGER.debug(this.getClass().getName() + "-->" + msg);
		System.out.println(this.getClass().getName() + "-->" + msg);
	}

	private String JMS_SERVER_URL;
	private final static String JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";
	// JMS context factory name.
	private String JMS_CONN_FACTORY;
	private String JMS_QUEUE;

	private QueueConnectionFactory queueConnectionFactory;
	private QueueConnection queueConnection;
	private QueueSession queueSession;
	private QueueBrowser queueBrowser;
	private Queue queue;

	public JMSQueuePuller(String jMS_SERVER_URL, String jMS_CONN_FACTORY, String jMS_QUEUE) {
		super();
		JMS_SERVER_URL = jMS_SERVER_URL;
		JMS_CONN_FACTORY = jMS_CONN_FACTORY;
		JMS_QUEUE = jMS_QUEUE;
	}

	public JMSQueuePuller() {
		super();
	}

	private void init(Context context, String queueName) throws NamingException, JMSException {
		debug("JMS factory:" + this.JMS_CONN_FACTORY);
		queueConnectionFactory = (QueueConnectionFactory) context.lookup(this.JMS_CONN_FACTORY);
		queueConnection = queueConnectionFactory.createQueueConnection();
		queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		queue = (Queue) context.lookup(queueName);
		queueBrowser = queueSession.createBrowser(queue);
		queueConnection.start();
		debug("Queue init done");
	}

	private void close() throws JMSException {
		queueBrowser.close();
		queueSession.close();
		queueConnection.close();
		debug("Closing of all connections done");
	}

	private List<JmsMessageResponseDto> pullFromJmsQ(JMSQueuePuller jmsQueuePuller) throws JMSException {
		List<JmsMessageResponseDto> responseList = new ArrayList<JmsMessageResponseDto>();
		Enumeration jmsBrowser;
		jmsBrowser = jmsQueuePuller.queueBrowser.getEnumeration();
		while (jmsBrowser.hasMoreElements()) {
			TextMessage message = (TextMessage) jmsBrowser.nextElement();
			// System.out.println("Message->" + message.getText());
			JmsMessageResponseDto jmsMessage = new JmsMessageResponseDto();
			jmsMessage.setMessage(message.getText());
			responseList.add(jmsMessage);
		}
		debug("Message pulled successfully!");
		return responseList;
	}

	private InitialContext getInitialContext() throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, this.JNDI_FACTORY);
		env.put(Context.PROVIDER_URL, this.JMS_SERVER_URL);
		return new InitialContext(env);
	}

	public ResponseEntity<List<JmsMessageResponseDto>> pullMessageFromQueue(MessagePullReqHeaderDto reqHeaderDto) {
		List<JmsMessageResponseDto> response = new ArrayList<JmsMessageResponseDto>();
		/*
		 * String WL_SERVER_URL =
		 * "t3://".concat(reqHeaderDto.getJmsServerIP()).concat(":")
		 * .concat(reqHeaderDto.getJsmServerPort());
		 */

		String WL_SERVER_URL = reqHeaderDto.getJmsServerIP().concat(":")
				.concat(reqHeaderDto.getJsmServerPort());
		
		JMSQueuePuller jmsQueuePuller = new JMSQueuePuller(WL_SERVER_URL, reqHeaderDto.getJmsConnectionFactory(),
				reqHeaderDto.getJmsQueue());

		InitialContext initialContext;
		this.JMS_SERVER_URL = jmsQueuePuller.JMS_SERVER_URL;
		debug("JMS weblogic URL:" + jmsQueuePuller.JMS_SERVER_URL);
		try {
			initialContext = getInitialContext();
			debug("InitialContext init done");
			debug("Connection Factory , Queue name:" + reqHeaderDto.getJmsQueue() + ","
					+ jmsQueuePuller.JMS_CONN_FACTORY);
			jmsQueuePuller.init(initialContext, jmsQueuePuller.JMS_QUEUE);
			response = jmsQueuePuller.pullFromJmsQ(jmsQueuePuller);
			jmsQueuePuller.close();
		} catch (Exception e) {
			e.printStackTrace();
			debug("Inside exception:" + e.getMessage());
			JmsMessageResponseDto jmsError = new JmsMessageResponseDto();
			jmsError.setMessage(e.getMessage());
			response.add(jmsError);
			return new ResponseEntity<List<JmsMessageResponseDto>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<JmsMessageResponseDto>>(response, HttpStatus.OK);
	}

}
