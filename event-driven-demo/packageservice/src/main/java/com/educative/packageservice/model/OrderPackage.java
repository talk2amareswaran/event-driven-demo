package com.educative.packageservice.model;

public class OrderPackage {

	private Order order;
	private String package_id;
	private String package_date;
	private String package_by;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getPackage_id() {
		return package_id;
	}

	public void setPackage_id(String package_id) {
		this.package_id = package_id;
	}

	public String getPackage_date() {
		return package_date;
	}

	public void setPackage_date(String package_date) {
		this.package_date = package_date;
	}

	public String getPackage_by() {
		return package_by;
	}

	public void setPackage_by(String package_by) {
		this.package_by = package_by;
	}

	

	

}
