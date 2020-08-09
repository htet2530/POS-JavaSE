package com.jdc.entities;

import java.time.LocalDate;
import java.time.LocalTime;

public class SalesDetails {
	private int id;
	private int unitPrice;
	private int quantity;
	private int tax;
	
	private int itemId;
	private int saleId;
	
	private int subTotal;
	
	//TODO:
	private LocalDate saleDate;
	private LocalTime saleTime;
	private String itemName;
	private String category;
	
	public LocalDate getSaleDate() {
		return saleDate;
	}
	public void setSaleDate(LocalDate saleDate) {
		this.saleDate = saleDate;
	}
	public LocalTime getSaleTime() {
		return saleTime;
	}
	public void setSaleTime(LocalTime saleTime) {
		this.saleTime = saleTime;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setSubTotal(int subTotal) {
		this.subTotal = subTotal;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getSaleId() {
		return saleId;
	}
	public void setSaleId(int saleId) {
		this.saleId = saleId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getTax() {
		return tax;
	}
	public void setTax(int tax) {
		this.tax = tax;
	}
	public int getSubTotal() {
		return unitPrice*quantity;
	}
	public int getTotal() {
		return getSubTotal()+getTax();
	}
}
