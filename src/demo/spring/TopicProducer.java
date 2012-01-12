package demo.spring;

import javax.jms.Destination;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

public class TopicProducer {
	
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
	
	public void publishMessage() {
		try {
			for(int i = 0; i < 3; i++) {
				Person p = new Person();
				p.setName("Test" + i);
				p.setAge(i);
				System.out.println("Sending Message:" + i);
				jmsTemplate.convertAndSend(destination, p);
				Thread.sleep(1000);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			ApplicationContext ctx = new FileSystemXmlApplicationContext("./topic-config.xml");
			
			TopicProducer producer = (TopicProducer)ctx.getBean("producer");
			producer.publishMessage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
