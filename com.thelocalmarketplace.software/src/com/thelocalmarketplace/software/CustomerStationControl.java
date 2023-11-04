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
	public Order order;
	public Boolean blocked = false;
	public ScaleListener scaleListener; //TODO needs to be set
	
	public CustomerStationControl(SelfCheckoutStation customerStationControl) {
		this.station = customerStationControl;
		
		//have to initialize weight dicrepency and add item controllers too (same way)
		// DY: I made a constructor to initialize a ScaleListener object. Implement below
		
		
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

	
	public void block() {
		blocked = true;
	}
	
	public void unblock() {
		blocked = false;
	}
}



