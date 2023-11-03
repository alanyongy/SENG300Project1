package com.thelocalmarketplace.software;

import com.thelocalmarketplace.hardware.*;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scanner.Barcode;
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
	
	public void add(Barcode barcode) {
		
		if(preconditionsMet(barcode)) {
			customerStationControl.block();
			
			//Fetching item from database based on scanned barcode
			BarcodedProduct item = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
			
			//weight and expected weight is done through querying the items in the order list
						
			//Adding item to session order
			items.add(item);
			total = total.add(new BigDecimal(item.getPrice()));
			totalUnpaid = totalUnpaid.add(new BigDecimal(item.getPrice()));
			
			//Signaling to customer to place scanned item into bagging area
				//unimplemented due to lack of user interface
			
			//the customer station is unblocked following a non-discrepancy creating weight change from the scale listener
		}
	}
	
	
	public boolean preconditionsMet(Barcode barcode) {
		if(customerStationControl.blocked) return true;
		return false;
	}
	
	public boolean preconditionsMet(PriceLookUpCode plu) {
		//preconditions for adding an item by PLU.
		//implemented only to show code structure.	
		return false;
	}

}
