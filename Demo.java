package eCANDY;

import java.util.*;

import javax.xml.bind.JAXBException;

public class Demo {
	public static void main(String[] args) {
		
		String[] urls = { "http://localhost:8080/products/json", "http://localhost:8080/products/xml?source=1",
				"http://localhost:8080/products/xml?source=2", "http://localhost:8080/products/xml?source=3" };
		Catalog c = new Catalog();
		System.out.println("************* Parsing is started! *************");
		System.out.println("***************** WAIT !!!************************");
		c.getParsedProducts(urls);
		System.out.println("************* Parsing is finished! ************");
		System.out.println("***********Enter the path for output file******");
		System.out.println("For example, \"/home/mari/workspace/output.xml\":\n");
		Scanner sc = new Scanner(System.in);
		String path = sc.nextLine().trim();

		try {
			c.marshaling(path);
		} catch (JAXBException e) {}
		System.out.println("*****************Done!*************************");

	}

}
