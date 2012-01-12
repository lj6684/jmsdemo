package demo.spring;

import javax.jms.Destination;
import javax.jms.Message;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

public class HelloWorldReceiver {
	
	private JmsTemplate jmsTemplate;
	private Destination destination;
	
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}
	
	public void receiveMessage() {
		System.out.println("will wait " + jmsTemplate.getReceiveTimeout() + " seconds for message");
		Message message = jmsTemplate.receive(destination);
		
		System.out.println("Received message:" + message);
	}

	public static void main(String[] args) {
		try {
			ApplicationContext ctx = new FileSystemXmlApplicationContext("./queue-config.xml");
			HelloWorldReceiver receiver = (HelloWorldReceiver)ctx.getBean("receiver");
			receiver.receiveMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
