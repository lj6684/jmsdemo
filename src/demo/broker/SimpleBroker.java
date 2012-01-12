package demo.broker;

import org.apache.activemq.broker.BrokerService;

public class SimpleBroker {
	
	public static void main(String[] args) {
		try {
			BrokerService broker = new BrokerService();
			broker.setBrokerName("myBroker");
			broker.setDataDirectory("./data");
			
			broker.addConnector("tcp://172.16.13.11:61616");
			broker.addConnector("tcp://127.0.0.1:61616");
			broker.start();
			
			System.out.println("press any key to stop the broker...");
			System.out.println();
			System.in.read();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
