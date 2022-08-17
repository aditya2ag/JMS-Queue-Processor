/* JMSQueuePoster is responsible for sending message to JMS queue set up in Weblogic server
 
 * Author : Aditya A G
  
 * */
package com.oracle.jms.queueprocessor.push;

import java.io.IOException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.oracle.jms.queueprocessor.dto.MessageRequestDto;
import com.oracle.jms.queueprocessor.dto.MessageResponseDto;

@Component
public class JMSQueuePoster {

	private static final Logger LOGGER = LoggerFactory.getLogger(JMSQueuePoster.class);

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
	private QueueSender queueSender;
	private Queue queue;

	private TextMessage textMessage;

	public JMSQueuePoster(String jMS_SERVER_URL, String jMS_CONN_FACTORY, String jMS_QUEUE) {
		super();
		JMS_SERVER_URL = jMS_SERVER_URL;
		JMS_CONN_FACTORY = jMS_CONN_FACTORY;
		JMS_QUEUE = jMS_QUEUE;
	}

	public JMSQueuePoster() {
		super();
	}

	private void init(Context context, String queueName) throws NamingException, JMSException {
		debug("JMS factory:" + this.JMS_CONN_FACTORY);
		queueConnectionFactory = (QueueConnectionFactory) context.lookup(this.JMS_CONN_FACTORY);
		queueConnection = queueConnectionFactory.createQueueConnection();
		queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		queue = (Queue) context.lookup(queueName);
		queueSender = queueSession.createSender(queue);
		textMessage = queueSession.createTextMessage();

		queueConnection.start();
		debug("Queue init done");
	}

	private void pushMessage(String msg) throws JMSException {
		textMessage.setText(msg);
		queueSender.send(textMessage);
	}

	private void close() throws JMSException {
		queueSender.close();
		queueSession.close();
		queueConnection.close();
		debug("Closing of all connections done");
	}

	private void pushToJmsQ(JMSQueuePoster jmsQueuePoster, String msg) throws IOException, JMSException {
		debug("Posting message to JMS queue");
		jmsQueuePoster.pushMessage(msg);
		debug("Message pushed successfully!");
	}

	private InitialContext getInitialContext() throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, this.JNDI_FACTORY);
		env.put(Context.PROVIDER_URL, this.JMS_SERVER_URL);
	//	Context.SECURITY_PROTOCOL
		return new InitialContext(env);
	}

	public ResponseEntity<MessageResponseDto> postMessageToQueue(MessageRequestDto messageReq) {
		MessageResponseDto response = new MessageResponseDto();
		response.setResponse("Message posted to queue->" + messageReq.getJmsQueue() + " successfully");
		/*
		 * String WL_SERVER_URL =
		 * "t3s://".concat(messageReq.getJmsServerIP()).concat(":")
		 * .concat(messageReq.getJsmServerPort());
		 */
		
		String WL_SERVER_URL = messageReq.getJmsServerIP().concat(":")
				.concat(messageReq.getJsmServerPort());
		
		
		JMSQueuePoster jmsQueuePoster = new JMSQueuePoster(WL_SERVER_URL, messageReq.getJmsConnectionFactory(),
				messageReq.getJmsQueue());

		InitialContext initialContext;
		this.JMS_SERVER_URL = jmsQueuePoster.JMS_SERVER_URL;
		debug("JMS weblogic URL:" + jmsQueuePoster.JMS_SERVER_URL);
		try {
			initialContext = getInitialContext();
			debug("InitialContext init done");
			debug("Connection Factory , Queue name:" + messageReq.getJmsQueue() + ","
					+ jmsQueuePoster.JMS_CONN_FACTORY);
			jmsQueuePoster.init(initialContext, jmsQueuePoster.JMS_QUEUE);
			jmsQueuePoster.pushToJmsQ(jmsQueuePoster, messageReq.getMessage());
			jmsQueuePoster.close();
		} catch (Exception e) {
			e.printStackTrace();
			debug("Inside exception:" + e.getMessage());
			response.setResponse(e.getMessage());
			return new ResponseEntity<MessageResponseDto>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<MessageResponseDto>(response, HttpStatus.OK);
	}

}
