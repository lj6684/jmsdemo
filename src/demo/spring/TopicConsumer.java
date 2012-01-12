package demo.spring;

import javax.jms.Destination;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

public class TopicConsumer {
	
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
	
	public void consumeMessage() {
		while(true) {
			System.out.println("waiting");
			Person p = (Person)jmsTemplate.receiveAndConvert(destination);
			System.out.println("received:" + p);
		}
	}
	
	public static void main(String[] args) {
		try {
			ApplicationContext ctx = new FileSystemXmlApplicationContext("./topic-config.xml");
			
			TopicConsumer consumer = (TopicConsumer)ctx.getBean("consumer");
			consumer.consumeMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
