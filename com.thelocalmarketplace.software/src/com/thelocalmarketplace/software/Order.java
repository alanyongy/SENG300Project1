package com.thelocalmarketplace.software;

import com.thelocalmarketplace.hardware.*;
import com.tdc.*;
import com.tdc.coin.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

public class Order {

	private CustomerStationControl customerStationControl;
	private ArrayList<Product> items;
	
	private BigDecimal total; //total price
	private BigDecimal totalUnpaid; //total UNPAID price
	//probably need some weight trackers too
	
	public Order(CustomerStationControl customerStationControl) {
		this.customerStationControl = customerStationControl;
		items = new ArrayList<Product>();
		total = new BigDecimal(0);
		totalUnpaid = new BigDecimal(0);
	}

	public BigDecimal getTotalUnpaid() {
		return totalUnpaid;
	}
	
	public void addCoinsPaid(BigDecimal value) {
		totalUnpaid = totalUnpaid.min(value);
	}
	
	public CustomerStationControl getCustomerStationControl() {
		return customerStationControl;
	}
	
	public ArrayList<Product> getItems() {
		return items;
	}
	
	public BigDecimal getTotal() {
		return total;
	}
	
	public void add(BarcodedProduct item) {
		items.add(item);
		total = total.add(new BigDecimal(item.getPrice()));
		totalUnpaid = totalUnpaid.add(new BigDecimal(item.getPrice()));
	}
	

}
