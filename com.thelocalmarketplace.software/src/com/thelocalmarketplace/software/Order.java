package com.thelocalmarketplace.software;

import com.thelocalmarketplace.hardware.*;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import ca.ucalgary.seng300.simulation.SimulationException;

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
	
	
	public boolean preconditionsMet(Barcode barcode) {
		if(customerStationControl.blocked) return true;
		return false;
	}
	
	public boolean preconditionsMet(PriceLookUpCode plu) {
		//preconditions for adding an item by PLU.
		//implemented only to show code structure.	
		return false;
	}
	
	/**
	 * Gets the expected mass of the order
	 **/
	public Mass getExpectedMass() {
		Mass Sum = new Mass(0);
		for (SessionItem i : items) {
			Mass iMass = i.getMass();
			
			Sum = Sum.sum(iMass);	
		}
		return (Sum);
	}

}
