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
	
	public void addCoinsPaid(int value) {
		totalUnpaid = totalUnpaid.min(new BigDecimal(value));
	}
	
	/**
	 * methods needed:
	 * call methods for Total, items, 
	 * IMPORTANT: add method - adds item to items, adds price of each item
	 * to item total and total unpaid, keeps track of weight
	 */

}
