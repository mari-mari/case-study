package eCANDY;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Catalog {
	@XmlElement(name = "product")
	private List<Product> products;

	public List<Product> getProduct() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<Product> getParsedProducts(String... urls) {
		products = MyJsonParser.parseJson(urls[0]);
		// System.out.println("After JSON=" + products.size());
		products.addAll(MyXmlParser.parseXml(urls[1]));
		products.addAll(MyXmlParser.parseXml(urls[2]));
		products.addAll(MyXmlParser.parseXml(urls[3]));
		// System.out.println("After XML = " + products.size());
		validateAndConvert();
		return products;
	}

	public void validateAndConvert() {
		products.removeIf(x -> x.isValid() == false);
		products.removeIf(x -> x.getPrice() == 0.0);
		products.forEach(x -> x.changeCurrency());
		Collections.sort(products, (x, y) -> x.getName().compareTo(y.getName()));
		deleteDuplicates();
	}

	private void deleteDuplicates() {
		List<Product> uniqueProduct = new ArrayList<>();
		for (Product p : products) {
			if (!uniqueProduct.contains(p)) {
				uniqueProduct.add(p);
			} else {
				if (uniqueProduct.removeIf(x -> x.equals(p) && x.getBestBefore().compareTo(p.getBestBefore()) < 0))
					uniqueProduct.add(p);
			}
		}
		products = uniqueProduct;
	}

	public void marshaling(String path) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Catalog.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		//jaxbMarshaller.marshal(this, System.out);
		jaxbMarshaller.marshal(this, new File(path));
	}

	public static void main(String[] args) throws JAXBException {}
}
