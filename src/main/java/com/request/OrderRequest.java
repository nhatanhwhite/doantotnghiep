package com.request;

import java.util.List;

import com.dto.ShoppingCartDTO;

public class OrderRequest {
	private String customerName;
	private String customerEmail;
	private String customerPhone;
	private String address;
	private List<ShoppingCartDTO> orderDetail;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<ShoppingCartDTO> getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(List<ShoppingCartDTO> orderDetail) {
		this.orderDetail = orderDetail;
	}

}
