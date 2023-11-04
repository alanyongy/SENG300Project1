/**
 * Contains information and actions which can be performed by the current order.
 * 
 * @author Jaden Taylor (30113034)
 * @author Alan Yong (30105707)
 * @author Kear Sang Heng (30087289)
 * @author Daniel Yakimenka (10185055)
 * @author Anandita Mahika (30097559)
 */
package com.thelocalmarketplace.software;

import com.thelocalmarketplace.hardware.*;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scanner.Barcode;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Order {

	private CustomerStationControl customerStationControl;
	private ArrayList<SessionItem> items;
	
	private BigDecimal total; //total price of order
	private BigDecimal totalUnpaid; //total UNPAID price of order

	public Order(CustomerStationControl customerStationControl) {
		this.customerStationControl = customerStationControl;
		items = new ArrayList<SessionItem>();
		total = new BigDecimal(0);
		totalUnpaid = new BigDecimal(0);
	}

	/**
	 * 
	 * @return the total unpaid amount of the order
	 */
	public BigDecimal getTotalUnpaid() {
		return totalUnpaid;
	}
	
	/**
	 * Subtracts the amount paid by the customer from the total unpaid amount for the order
	 * @param value the value of the coins paid by the customer
	 */
	public void addCoinsPaid(BigDecimal value) {
		totalUnpaid = totalUnpaid.subtract(value);
	}
	
	/**
	 * 
	 * @return the control class for the customer station that checks for payment, adds items,
	 * and weight discrepancy of the items in the bagging area
	 */
	public CustomerStationControl getCustomerStationControl() {
		return customerStationControl;
	}
	
	/**
	 * 
	 * @return the list of items in the order
	 */
	public ArrayList<SessionItem> getItems() {
		return items;
	}
	
	/**
	 * 
	 * @return the calculated total price of the order 
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * Adds an product with a barcode to the order list as a new item with 
	 * it's mass assigned to the expected weight of the product.
	 * @param barcode the barcode of the added item
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
			
			customerStationControl.notifyCustomer("Place the scanned item in the bagging area", 
					customerStationControl.notifyPlaceItemInBaggingAreaCode);
			
			//the customer station is unblocked following a non-discrepancy creating weight change from the scale listener
		}
	}
	
	/**
	 * Adds an item to the order list by PLU code.
	 * Only used to show code structure in overloading the add method 
	 * to support the various ways of adding an item to the order list.
	 * @param plu the plu of the item adding to order
	 */
	public void add(PriceLookUpCode plu) {}
	
	
	/**Checks if the preconditions are met for adding a barcode product to the order list.
	 * @param barcode
	 * @return boolean that tracks whether adding items and payment is blocked at the station
	 */
	public boolean preconditionsMet(Barcode barcode) {
		return !customerStationControl.isBlocked();
	}
	
	/**Checks if the preconditions are met for adding a PLU product to the order list.
	 * Only used to show code structure in overloading the preconditionsMet method
	 * to support differing preconditions for the various ways of adding an item to the order list.
	 * @param plu the plus of the item adding to oder
	 * @return the expected mass of the order
	 */
	public boolean preconditionsMet(PriceLookUpCode plu) {return false;}
	
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

	
	/**
	 * Sets total unpaid variable.
	 * Used solely for testing due to limited scope of implementation of payment process in this iteration
	 */
	public void setTotalUnpaid(BigDecimal n) {
		totalUnpaid = n;
	}
}
