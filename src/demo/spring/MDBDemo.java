package demo.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class MDBDemo {
	
	public static void main(String[] args) {
		try {
			ApplicationContext ctx = new FileSystemXmlApplicationContext("./mdb-config.xml");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
