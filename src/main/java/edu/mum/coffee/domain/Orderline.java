package edu.mum.coffee.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Orderline {

	private int id;
	private int quantity;

	private Product product;

	private Order order;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@JsonIgnore
	public Order getOrder() {
		return order;
	}

	@JsonIgnore
	public void setOrder(Order order) {
		this.order = order;
	}

	public double getSubtotal() {
		return quantity * product.getPrice();
	}

	public double getPrice() {
		return product.getPrice();
	}

}
