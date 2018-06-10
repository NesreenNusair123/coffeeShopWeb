package edu.mum.coffee.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import edu.mum.coffee.domain.Person;
import edu.mum.coffee.domain.Product;

@Service
public class PersonService{
	private static String uri = "http://localhost:8090/people/";

	RestTemplate template = new RestTemplate();
	
	@SuppressWarnings({ "unchecked", "unused" })
	public List<Person> getAll() {
		List<Person> result = template.getForObject(uri, List.class);
		return fixedList(result);
	}
	
	protected List<Person> fixedList(List result){
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

	private List<Person> getResultOneByOne(List firstResult) {
		try {
			List<Person> newResult = new ArrayList<Person>();

			for(Object item : firstResult) {
				Object key = ((LinkedHashMap)item).get("id");
				newResult.add(get((int)key));
			}
			
			return newResult;
			
		} catch (Exception e) {
			return null;
		}
	}

	public Person get(long id) {
		return template.getForObject(uri +String.valueOf(id), Person.class);
	}

	public Person edit(long id, Person obj) {
		template.put(uri + String.valueOf(id), obj);
		return obj;
	}

	public void delete(long id) {
		template.delete(uri + String.valueOf(id));
	}

	public Person login(String email, String password) {
		String url = uri + "login/";
		Person p = new Person();
		p.setEmail(email);
		p.setPassword(password);

		return template.postForObject(url, p, Person.class);
	}

	public Person getByEmail(String email) {
		try {
			String url = uri + "getByEmail";
			return template.postForObject(url, email, Person.class);
		} catch (Exception e) {
			return null;
		}
	}

	public Person add(Person obj) {
		if (obj.isAdmin()) {
			return template.postForObject(uri + "admin", obj, Person.class);
		} else {
			return template.postForObject(uri, obj, Person.class);
		}
	}
}
