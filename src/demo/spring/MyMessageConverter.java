package demo.spring;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

public class MyMessageConverter implements MessageConverter {

	@Override
	public Object fromMessage(Message message) throws JMSException,
			MessageConversionException {
		if(message instanceof MapMessage) {
			System.out.println("convert MapMessage to Person Object");
			
			MapMessage mapMsg = (MapMessage)message;
			String name = mapMsg.getString("name");
			int age = mapMsg.getInt("age");
			
			Person p = new Person();
			p.setName(name);
			p.setAge(age);
			
			return p;
		}
		
		return null;
	}

	@Override
	public Message toMessage(Object obj, Session session) throws JMSException,
			MessageConversionException {
		if(obj instanceof Person) {
			Person p = (Person)obj;
			System.out.println("convert Person Object to MapMessage");
			MapMessage mapMsg = session.createMapMessage();
			mapMsg.setString("name", p.getName());
			mapMsg.setInt("age", p.getAge());
			
			return mapMsg;
		}
		return null;
	}

}
