package edu.mum.coffee.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import edu.mum.coffee.domain.Order;
import edu.mum.coffee.domain.Person;

@Service
public class OrderService  {

	private static String uri = "http://localhost:8090/orders/";
	RestTemplate template= new RestTemplate();
	
	public OrderService() {
		
	}

	
	@SuppressWarnings({ "unchecked", "unused" })
	public List<Order> getAll() {
		List<Order> result = template.getForObject(uri, List.class);
		return fixedList(result);
	}
	
	protected List<Order> fixedList(List result){
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

	private List<Order> getResultOneByOne(List firstResult) {
		try {
			List<Order> newResult = new ArrayList<Order>();

			for(Object item : firstResult) {
				Object key = ((LinkedHashMap)item).get("id");
				newResult.add(get((int)key));
			}
			
			return newResult;
			
		} catch (Exception e) {
			return null;
		}
	}

	public Order get(long id) {
		return template.getForObject(uri + String.valueOf(id), Order.class);
	}

	public Order add(Order obj) {
		return template.postForObject(uri, obj, Order.class);
	}

	public Order edit(long id, Order obj) {
		template.put(uri + String.valueOf(id), obj);
		return obj;
	}

	public void delete(long id) {
		template.delete(uri + String.valueOf(id));
	}
	
	
	
	public List<Order> findByPerson(Person p) {
		String url = uri + "getByPerson/" + Long.toString(p.getId());
		return fixedList(template.getForObject(url, List.class));
	}
}