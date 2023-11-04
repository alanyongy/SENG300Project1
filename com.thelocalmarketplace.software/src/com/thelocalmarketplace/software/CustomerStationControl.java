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
	
	
	public String notifyDiscrepancyCode =  "discrepancy";
	public String notifyInsertPaymentCode =  "insertPayment";
	public String notifyPlaceItemInBaggingAreaCode =  "placeItemInBaggingArea";
	public String notifyOtherCode =  "other";
	/**Used for testing as signaling UI will not be implemented in this iteration.
	 *Possible values: discrepancy, insertPayment, placeItemInBaggingArea 
	 */
	public String attendantNotified = "";
	/**Used for testing as signaling UI will not be implemented in this iteration.
	 *Possible values: discrepancy, insertPayment, placeItemInBaggingArea 
	 */
	public String customerNotified = "";

	
	
	private Boolean customerNotified;
	private Boolean attendantNotified;
	private String lastNotification;
	
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
	
	public void notifyAttendant(String message, String code) {
		System.out.println("Attendant: " + message);
		attendantNotified = code;
	}
	
	public void notifyCustomer(String message, String code) {
		System.out.println("Customer: " + message);
		customerNotified = code;
	}
	
	public Order getOrder() {
		return order;
	}
	
	/**need methods
	 * method that calls notifyCustomer to scan next item
	 * method that adds BarcodedProduct to order -  probably calls add method from order 
	 */
	
	public Boolean getCustomerNotified() {
	    return customerNotified;
	}
	
	public Boolean getAttendantNotified() {
	    return attendantNotified;
	}
	
	public String getLastNotification() {
		return lastNotification;
	}
	
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



