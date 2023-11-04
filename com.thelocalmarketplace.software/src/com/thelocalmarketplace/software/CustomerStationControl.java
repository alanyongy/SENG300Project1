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

	public Order order;
	public Boolean blocked = false;
	private PayCoin payCoinController;
	
	//Used for testing as signaling UI will not be implemented in this iteration
	public boolean attendantNotified = false;
	public boolean customerNotified = false;
	
	public CustomerStationControl(SelfCheckoutStation customerStationControl) {
		this.station = customerStationControl;
		
		//have to initialize weight dicrepency and add item controllers too (same way)
		// DY: I made a constructor to initialize a ScaleListener object. Also registered it to the scale thats part of the station. Implement:
		ScaleListener scaleListener = new ScaleListener(this);
		customerStationControl.baggingArea.register(scaleListener);
		// DY: end
		
		//initialize PayCoin control
		payCoinController = new PayCoin(this);
		customerStationControl.coinValidator.attach(payCoinController);
		customerStationControl.coinStorage.attach(payCoinController);
		
		//register listener to station's barcode scanner
		BarcodeScanListener barcodeScanListener = new BarcodeScanListener(this);
		customerStationControl.scanner.register(barcodeScanListener);
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
		attendantNotified = true;
	}
	
	//displays message to customer
	public void notifyCustomer(String message) {
		System.out.println("Customer: " + message);
		customerNotified = true;
	}
	
	public Order getOrder() {
		return order;
	}
	
	/**need methods
	 * method that calls notifyCustomer to scan next item
	 * method that adds BarcodedProduct to order -  probably calls add method from order 
	 */

	
	public void block() {
		blocked = true;
	}
	
	public void unblock() {
		blocked = false;
	}
	
	public Boolean isBlocked() {
		return blocked;
	}
}



