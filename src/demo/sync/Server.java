package demo.sync;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Server implements MessageListener {
	
	private String brokerUrl = "tcp://127.0.0.1:61616";
	private String requestQueue = "demo.requests";
	
	private Connection connection;
	private Session session;
	private MessageProducer producer;
	private MessageConsumer consumer;
	
	public void start() throws Exception {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		
		connection = connectionFactory.createConnection();
		connection.start();
		
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue(requestQueue);
		
		consumer = session.createConsumer(destination);
		consumer.setMessageListener(this);
	}
	
	public void stop() throws Exception {
		producer.close();
		consumer.close();
		session.close();
		connection.close();
	}

	@Override
	public void onMessage(Message message) {
		try {			
			if(message instanceof TextMessage) {
				// get request
				TextMessage txtMsg = (TextMessage)message;
				String txt = txtMsg.getText();
				
				String result = handleRequest(txt);
				TextMessage response = session.createTextMessage();
				response.setText(result);
				
				// 设置请求对应ID
				response.setJMSCorrelationID(message.getJMSCorrelationID());
				
				// 向指定临时响应通道发送响应信息
				producer = session.createProducer(null);
				producer.send(message.getJMSReplyTo(), response);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String handleRequest(String request) {
		System.out.println("SERVER [" + new Date() + "] receive message:" + request);
		String response = "reply for [" + request + "]";
		return response;
	}
	
	public static void main(String[] args) {
		try {
			Server server = new Server();
			server.start();
			
			System.out.println("press any key to exit...");
			System.in.read();
			
			server.stop();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
