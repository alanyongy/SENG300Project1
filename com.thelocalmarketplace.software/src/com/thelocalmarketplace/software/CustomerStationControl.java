// Class that contains actions that can be performed by a customer station
// Authors:
// Jaden Taylor (30113034)
// TODO: Add your names and if you are the last person to do so delete this comment
package com.thelocalmarketplace.software;

import com.thelocalmarketplace.hardware.*;
import com.tdc.*;
import com.tdc.coin.*;

public class CustomerStationControl {
	
	private SelfCheckoutStation station;
	
	private PayCoin payCoinController;
	private Order order;
	
	public CustomerStationControl(SelfCheckoutStation customerStationControl) {
		this.station = customerStationControl;
		
		//have to initialize weight dicrepency and add item controllers too (same way)
		
		
		//initialize PayCoin control
		payCoinController = new PayCoin(this);
		customerStationControl.coinValidator.attach(payCoinController);
		customerStationControl.coinStorage.attach(payCoinController);
	}
	
	public SelfCheckoutStation getSelfCheckoutStation() {
		return station;
	}
	
	public void startSession() {
		order = new Order(this);
	}
	
	public void pay() {
		payCoinController.pay(order);
	}
	
	//displays message to attendant
	public void notifyAttendant(String message) {
		System.out.println("Attendant: " + message);
	}
	
	//displays message to customer
	public void notifyCustomer(String message) {
		System.out.println("Customer: " + message);
	}
	
	public Order getOrder() {
		return order;
	}
	
	/**need methods
	 * method that calls notifyCustomer to scan next item
	 * method that adds BarcodedProduct to order -  probably calls add method from order 
	 */
}
