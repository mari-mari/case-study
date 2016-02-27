package eCANDY;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class MyXmlParser {
	static private ArrayList<Product> productList = new ArrayList<>();
	static MyContentHandler contentHandler;

	private static InputStream getInputFromServer(String urlString) throws IOException {
		URL url = new URL(urlString);
		return url.openStream();
	}

	public static ArrayList<Product> parseXml(String url) {

		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			XMLReader xmlReader = saxParser.getXMLReader();

			try {
				xmlReader.setFeature("http://apache.org/xml/features/continue-after-fatal-error", true);
			} catch (SAXException e) {
				System.out.println("error in setting up parser feature");
			}
			contentHandler = new MyContentHandler();
			xmlReader.setContentHandler(contentHandler);
			xmlReader.setErrorHandler(new MyErrorHandler());
			xmlReader.parse(new InputSource(getInputFromServer(url)));
			productList = contentHandler.getProductList();

		} catch (Throwable e) {
			productList = contentHandler.getProductList();
			// System.out.println("Error -- " +e.getMessage());
		}
		return productList;
	}

	public static void main(String[] args) {}
}
