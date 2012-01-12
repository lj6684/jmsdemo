package demo.sync;

import java.util.Date;
import java.util.UUID;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Client implements MessageListener {
	
	private String brokerUrl = "tcp://127.0.0.1:61616";
	private String requestQueue = "demo.requests";
	
	private Connection connection;
	private Session session;
	private MessageProducer producer;
	private MessageConsumer consumer;
	private Destination tempDest;
	
	public void start() throws Exception {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		
		connection = connectionFactory.createConnection();
		connection.start();
		
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue(requestQueue);
		
		producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		tempDest = session.createTemporaryQueue();
		consumer = session.createConsumer(tempDest);
		consumer.setMessageListener(this);
	}
	
	public void stop() throws Exception {
		producer.close();
		consumer.close();
		session.close();
		connection.close();
	}
	
	public void sendRequest(String request) throws Exception {
		System.out.println("CLINET [" + new Date() + "] sending request:" + request);
		TextMessage txtMsg = session.createTextMessage();
		txtMsg.setText(request);
		
		txtMsg.setJMSReplyTo(tempDest);
		String correlationID = UUID.randomUUID().toString();
		txtMsg.setJMSCorrelationID(correlationID);
		this.producer.send(txtMsg);
	} 
	
	public void handleResponse(String response) throws Exception {
		System.out.println("CLIENT [" + new Date() + "] receive response:" + response);
	}
	
	@Override
	public void onMessage(Message message) {
		try {
			if(message instanceof TextMessage) {
				TextMessage txtMsg = (TextMessage)message;
				String responseStr = txtMsg.getText();
				handleResponse(responseStr);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			Client client = new Client();
			client.start();
			
			for(int i = 0; i < 10; i++) {
				client.sendRequest("REQUEST-" + i);
			}
			Thread.sleep(3000);
			client.stop();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
