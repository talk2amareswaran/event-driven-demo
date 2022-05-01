package com.educative.notificationservice.model;

public class Shipping {

	private OrderPackage orderPackage;
	private String shipping_id;
	private String shipping_date;
	private String shipping_by;

	public OrderPackage getOrderPackage() {
		return orderPackage;
	}

	public void setOrderPackage(OrderPackage orderPackage) {
		this.orderPackage = orderPackage;
	}

	public String getShipping_id() {
		return shipping_id;
	}

	public void setShipping_id(String shipping_id) {
		this.shipping_id = shipping_id;
	}

	public String getShipping_date() {
		return shipping_date;
	}

	public void setShipping_date(String shipping_date) {
		this.shipping_date = shipping_date;
	}

	public String getShipping_by() {
		return shipping_by;
	}

	public void setShipping_by(String shipping_by) {
		this.shipping_by = shipping_by;
	}

}
