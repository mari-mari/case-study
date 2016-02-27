package eCANDY;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyContentHandler extends DefaultHandler implements ErrorHandler{

	private Product product;
	private ArrayList<Product> productList = new ArrayList<>();
	private String content;

	public ArrayList<Product> getProductList() {
		return productList;
	}

	public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes attrs)
			throws SAXException {
		if (qualifiedName.equalsIgnoreCase("Item"))
			product = new Product();
		if (qualifiedName.equalsIgnoreCase(("Product")) && product == null)
			product = new Product();
		loopOverAttributes(attrs);
	}

	private void loopOverAttributes(Attributes attrs) {
		for (int i = 0; i < attrs.getLength(); i++) {
			switch (attrs.getQName(i).toLowerCase()) {
			case "name":
				product.setName(attrs.getValue(i));
				break;
			case "price":
				try {
					product.setPrice(NumberFormat.getInstance().parse(attrs.getValue(i)).doubleValue());
				} catch (ParseException e) {
					// System.err.println(e.getMessage());
				}
				break;
			case "currency":
				product.setCurrency(attrs.getValue(i));
				break;
			case "bestbefore":
			case "date":
				product.setBestBefore(attrs.getValue(i));
				break;
			default:
				break;
			}
		}

	}
	
	public void endElement(String namespaceURI, String localName, String qualifiedName) throws SAXException {
		content = content.trim();
		switch (qualifiedName.toLowerCase()) {
		case "product": {
			if(product!=null)
			productList.add(product);
			product = null;
		}
			break;
		case "id":product.setId(content);
		case "name":
			product.setName(content);
			break;
		case "currency":
			product.setCurrency(content);
			break;
		case "price":
			try {
				product.setPrice(NumberFormat.getInstance().parse(content).doubleValue());
			} catch (ParseException e) {
				
			}
			break;
		default:
			break;
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		content = new String(ch, start, length);
	}	
}
