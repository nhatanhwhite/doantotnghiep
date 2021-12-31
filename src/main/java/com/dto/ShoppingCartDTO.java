package com.dto;

import com.entity.Product;

public class ShoppingCartDTO {
	private String productId;
	private int quantity;
	private Product productDTO;
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Product getProductDTO() {
		return productDTO;
	}
	public void setProductDTO(Product productDTO) {
		this.productDTO = productDTO;
	}
	
	
}
