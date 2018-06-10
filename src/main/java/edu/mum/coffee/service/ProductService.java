package edu.mum.coffee.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import edu.mum.coffee.domain.Product;

@Service
public class ProductService {
	private static String uri = "http://localhost:8090/products/";

	RestTemplate template = new RestTemplate();
	
	@SuppressWarnings({ "unchecked", "unused" })
	public List<Product> getAll() {
		List<Product> result = template.getForObject(uri, List.class);
		return fixedList(result);
	}
	
	protected List<Product> fixedList(List result){
		if (result.size() == 0) {
			return result;
		}

		String typeName = result.get(0).getClass().getName();
		if (typeName.toUpperCase().contains("LINKEDHASHMAP")) {
			return getResultOneByOne(result);
		} else {
			return result;
		}
	}

	private List<Product> getResultOneByOne(List firstResult) {
		try {
			List<Product> newResult = new ArrayList<Product>();

			for(Object item : firstResult) {
				Object key = ((LinkedHashMap)item).get("id");
				newResult.add(get((int)key));
			}
			
			return newResult;
			
		} catch (Exception e) {
			return null;
		}
	}

	public Product get(int id) {
		return template.getForObject(uri +String.valueOf(id), Product.class);
	}

	public Product add(Product obj) {
		return template.postForObject(uri, obj, Product.class);
	}

	public Product edit(int id, Product obj) {
		template.put(uri + String.valueOf(id), obj);
		return obj;
	}

	public void delete(int id) {
		template.delete(uri + String.valueOf(id));
	}
}
