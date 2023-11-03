package com.thelocalmarketplace.software;

public class WeightDiscrepancy {
	
	private boolean status = false;
	

	
	private static void SignalCustomer() {
		
	}
	
	private static void SignalAttendant() {
		
	}
	public void Unblock(CustomerStationControl Controller) {
		Controller.unblock();
	}
	
	public void WeightDiscrepancyEvent(CustomerStationControl Controller) {
		status = true;
		
		Controller.block();
		SignalCustomer();
		SignalAttendant();
		
		
	}
	
	public boolean checkStatus() {
		return(status);
	}
	
	
}
