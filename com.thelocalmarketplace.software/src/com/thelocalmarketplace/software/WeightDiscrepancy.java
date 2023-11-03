package com.thelocalmarketplace.software;

public class WeightDiscrepancy {
	
	boolean status = false;
	private static void BlockCustomer() {
		// Needs implementation for blocking pay with coin and add item barcode
	}
	
	private static void SignalCustomer() {
		
	}
	
	private static void SignalAttendant() {
		
	}
	public void Unblock() {
		
	}
	
	public void WeightDiscrepancyEvent(DiscrepancyListener listener) {
		status = true;
		BlockCustomer();
		SignalCustomer();
		SignalAttendant();
		
		
	}
	
	public boolean checkStatus() {
		return(status);
	}
	
	
}
