package demo.broker;

import org.apache.activemq.broker.BrokerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author Administrator
 *
 */
public class SpringBroker {
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ApplicationContext ctx = new FileSystemXmlApplicationContext("./broker-config.xml");
			BrokerService brokerService = (BrokerService)ctx.getBean("broker");
			brokerService.start();
			
			System.out.println("press any key to stop the broker...");
			System.out.println();
			System.in.read();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
