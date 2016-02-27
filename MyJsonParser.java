package eCANDY;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class MyJsonParser {

	private static ArrayList<Product> products = new ArrayList<>();

	public MyJsonParser() {
	}

	public static ArrayList<Product> getProducts() {
		return products;
	}

	private static InputStream getInputFromServer(String urlString) throws IOException {
		URL url = new URL(urlString);
		return url.openStream();
	}

	public static List<Product> parseJson(String url) {

		try {

			JsonReader jsonReader = new JsonReader(new InputStreamReader(getInputFromServer(url)));
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = jsonParser.parse(jsonReader).getAsJsonObject();
			JsonArray jsonArray = jsonObject.getAsJsonArray("products");
			for (JsonElement elem : jsonArray) {
				try {
					double price = NumberFormat.getInstance()
							.parse(elem.getAsJsonObject().get("price").getAsString().trim()).doubleValue();
					String name = elem.getAsJsonObject().get("name").getAsString().trim();
					String id = elem.getAsJsonObject().get("id").getAsString().trim();
					String currency = elem.getAsJsonObject().get("currency").getAsString().trim();
					LocalDate bestBefore = LocalDate
							.parse(elem.getAsJsonObject().get("bestBefore").getAsString().trim());
					Product product = new Product(id, name, price, currency, bestBefore);
					products.add(product);

				} catch (ParseException | DateTimeParseException e) {
				}
			}
		} catch (IOException e1) {}
		return products;
	}
	
	public static void main(String[] args){}
}