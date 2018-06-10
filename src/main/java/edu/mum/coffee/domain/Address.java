package edu.mum.coffee.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Address {

	private int id;
	private String city;
	private String state;
	private String country;
	private String zipcode;

	@NotNull
	@NotBlank
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@NotNull
	@NotBlank
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@NotNull
	@NotBlank
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@NotNull
	@NotBlank
	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

}
