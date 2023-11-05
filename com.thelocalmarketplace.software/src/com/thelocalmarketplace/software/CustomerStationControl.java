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
		
		order = new Order(this);
	}
	
	public void startSession() {
		order = new Order(this);
		sessionStarted = true;
		unblock();
	}
	
	public void pay() {
		payCoinController.pay(order);
	}
	
	public void notifyAttendant(String message, String code) {
		lastNotification = ("Attendant: " + message);
		//System.out.println("Attendant: " + message);
		attendantNotified = code;
	}
	
	public void notifyCustomer(String message, String code) {
		lastNotification = ("Customer: " + message);
		//System.out.println("Customer: " + message);
		customerNotified = code;
	}
	
	public Order getOrder() {
		return order;
	}
	
	/**need methods
	 * method that calls notifyCustomer to scan next item
	 * method that adds BarcodedProduct to order -  probably calls add method from order 
	 */
	
	public String getCustomerNotified() {
	    return customerNotified;
	}
	
	public String getAttendantNotified() {
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

	public Boolean getSessionStarted() {
		return sessionStarted;
	}
}



