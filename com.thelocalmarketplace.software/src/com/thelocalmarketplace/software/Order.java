package com.thelocalmarketplace.software;

import com.thelocalmarketplace.hardware.*;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scanner.Barcode;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Order {

	private CustomerStationControl customerStationControl;
	private ArrayList<SessionItem> items;
	
	private BigDecimal total; //total price
	private BigDecimal totalUnpaid; //total UNPAID price
	//probably need some weight trackers too
	
	public Order(CustomerStationControl customerStationControl) {
		this.customerStationControl = customerStationControl;
		items = new ArrayList<SessionItem>();
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
	
	public ArrayList<SessionItem> getItems() {
		return items;
	}
	
	public BigDecimal getTotal() {
		return total;
	}
	
	/**
	 * Adds an product with a barcode to the order list as a new item with 
	 * it's mass assigned to the expected weight of the product.
	 * @param barcode
	 */
	public void add(Barcode barcode) {
		if(preconditionsMet(barcode)) {
			customerStationControl.block();
			
			//Fetching item from database based on scanned barcode
			BarcodedProduct itemProduct = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
			SessionItem item = new SessionItem(new Mass(itemProduct.getExpectedWeight()));
			
			//weight and expected weight is done through querying the items in the order list
						
			//Adding item to session order
			items.add(item);
			total = total.add(new BigDecimal(itemProduct.getPrice()));
			totalUnpaid = totalUnpaid.add(new BigDecimal(itemProduct.getPrice()));
			
			customerStationControl.notifyCustomer("Place the scanned item in the bagging area");
			
			//the customer station is unblocked following a non-discrepancy creating weight change from the scale listener
		}
	}
	
	/**
	 * Adds an item to the order list by PLU code.
	 * Only used to show code structure in overloading the add method 
	 * to support the various ways of adding an item to the order list.
	 * @param plu 
	 */
	public void add(PriceLookUpCode plu) {}
	
	
	/**Checks if the preconditions are met for adding a barcode product to the order list.
	 * @param barcode
	 * @return
	 */
	public boolean preconditionsMet(Barcode barcode) {
		if(customerStationControl.blocked) return true;
		return false;
	}
	
	
	/**Checks if the preconditions are met for adding a PLU product to the order list.
	 * Only used to show code structure in overloading the preconditionsMet method
	 * to support differing preconditions for the various ways of adding an item to the order list.
	 * @param plu
	 * @return
	 */
	public boolean preconditionsMet(PriceLookUpCode plu) {return false;}

}
