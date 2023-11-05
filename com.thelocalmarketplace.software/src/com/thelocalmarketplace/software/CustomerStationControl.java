/**
 * Control structure for actions that can be performed by a customer station.
 * 
 * @author Jaden Taylor (30113034)
 * @author Alan Yong (30105707)
 * @author Kear Sang Heng (30087289)
 * @author Daniel Yakimenka (10185055)
 * @author Anandita Mahika (30097559)
 */
package com.thelocalmarketplace.software;

import com.thelocalmarketplace.hardware.*;

public class CustomerStationControl {
	private SelfCheckoutStation station;

	private Order order;
	private Boolean blocked = true;
	private Boolean sessionStarted = false;
	private PayCoin payCoinController;
	
	public String notifyDiscrepancyCode =  "discrepancy";
	public String notifyInsertPaymentCode =  "insertPayment";
	public String notifyPlaceItemInBaggingAreaCode =  "placeItemInBaggingArea";
	public String notifyOtherCode =  "other";
	public String notifyInvalidCoinCode = "invalidCoin";
	/**Used for testing as signaling UI will not be implemented in this iteration.
	 *Possible values: discrepancy, insertPayment, placeItemInBaggingArea 
	 */
	private String attendantNotified = "";
	/**Used for testing as signaling UI will not be implemented in this iteration.
	 *Possible values: discrepancy, insertPayment, placeItemInBaggingArea 
	 */
	private String customerNotified = "";
	private String lastNotification;
	
	public CustomerStationControl(SelfCheckoutStation customerStationControl) {
		this.station = customerStationControl;
		
		// Initialize scale listener
		ScaleListener scaleListener = new ScaleListener(this);
		customerStationControl.baggingArea.register(scaleListener);
		
		// Initialize PayCoin control
		payCoinController = new PayCoin(this);
		customerStationControl.coinValidator.attach(payCoinController);
		customerStationControl.coinStorage.attach(payCoinController);
		
		// Register listener to station's barcode scanner
		BarcodeScanListener barcodeScanListener = new BarcodeScanListener(this);
		customerStationControl.scanner.register(barcodeScanListener);
		
		// Create a new blank order
		order = new Order(this);
	}
	
	/**
	 * Starts a session, unblocking and allowing customer interaction.
	 */
	public void startSession() {
		order = new Order(this);
		sessionStarted = true;
		unblock();
	}
	
	/**
	 * Triggers customer payment action with the payCoinController
	 */
	public void pay() {
		payCoinController.pay(order);
	}
	
	/**
	 * Notifies the attendant to a message
	 * @param message
	 * 				String: message to send to the attendant
	 * @param code
	 * 				String: The code corresponding to the type of message
	 */
	public void notifyAttendant(String message, String code) {
		lastNotification = ("Attendant: " + message);
		//System.out.println("Attendant: " + message);
		attendantNotified = code;
	}
	
	/**
	 * Notifies the customer to a message
	 * @param message
	 * 				String: message to send to the customer
	 * @param code
	 * 				String: The code corresponding to the type of message
	 */
	public void notifyCustomer(String message, String code) {
		lastNotification = ("Customer: " + message);
		//System.out.println("Customer: " + message);
		customerNotified = code;
	}
	
	/**
	 * @return Order: the current order
	 */
	public Order getOrder() {
		return order;
	}
	
	/**
	 * @return String: customer notified code
	 */
	public String getCustomerNotified() {
	    return customerNotified;
	}
	
	/**
	 * @return String: attendant notified code
	 */
	public String getAttendantNotified() {
	    return attendantNotified;
	}
	
	/**
	 * @return String: code of the previous notification
	 */
	public String getLastNotification() {
		return lastNotification;
	}
	
	/**
	 * Blocks the system from customer interaction
	 */
	public void block() {
		blocked = true;
	}
	
	/**
	 * Unblocks the system, allowing customers to interact
	 */
	public void unblock() {
		blocked = false;
	}
	
	/**
	 * @return Boolean: true if the system is blocked, false otherwise
	 */
	public Boolean isBlocked() {
		return blocked;
	}

	/**
	 * @return Boolean: flag to see if the session is currently active
	 */
	public Boolean getSessionStarted() {
		return sessionStarted;
	}
}



