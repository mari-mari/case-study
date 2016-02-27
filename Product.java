package eCANDY;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.trivago.currency_client.CurrencyClientApplication;

@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

	private String id = UUID.randomUUID().toString();
	private String name;
	private double price;
	private String currency;

	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	@XmlElement(name = "bestBefore")
	private LocalDate bestBefore = LocalDate.now();

	public Product() {
	};

	public Product(String id, String name, double price, String currency, LocalDate bestBefore) {
		super();
		if (id != null)
			if (!id.equals("") || id.length() == 36)
				this.id = id;

		this.name = name;
		this.price = price;
		this.currency = currency;
		if (currency == null || currency.equals("") || currency.length() > 3)
			this.currency = "EUR";
		this.bestBefore = bestBefore;
		if (bestBefore == null || bestBefore.equals(""))
			this.bestBefore = LocalDate.now();
	}

	public void setBestBefore(String bestBefore) {
		if (bestBefore != null)
			if (!bestBefore.equals("")) {
				List<String> formats = Arrays.asList("yyyy-MM-dd", "dd-MM-yyyy", "dd.MM.yyyy");
				for (String f : formats) {
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern(f);
					try {
						this.bestBefore = LocalDate.parse(bestBefore, dtf);
					} catch (Exception e) {}
				}
			}
	}

	public LocalDate getBestBefore() {
		return bestBefore;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
		if (currency == null || currency.equals("") || currency.length() > 3)
			this.currency = "EUR";
	}

	public String getCurrency() {
		return currency;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPrice() {
		return price;
	}

	public void setId(String id) {
		this.id = id;
		if (id == null || id.equals("") || id.length() < 36)
			this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return "Product: " + "name: " + name + " price: " + price + " cur: " + currency + " date: " + bestBefore
				+ " id: " + id;
	}

	public void changeCurrency() {
		try {
			if (this.currency != "EUR") {
				NumberFormat formatter = new DecimalFormat("#0.00");
				this.price = (double) CurrencyClientApplication.getConvertedPrice((float) this.price, this.currency,
						"EUR");
				this.price = Double.valueOf(formatter.format(this.price));
				this.currency = "EUR";
			}
		} catch (Exception e) {
			//System.out.println(e.getMessage());
		}
	}

	public boolean isValid() {
		if (this.name == null || this.name.equals(""))
			return false;
		if (this.price == 0.0)
			return false;

		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		Product object = (Product) obj;
		return this.name.equals(object.name);
	}
}
