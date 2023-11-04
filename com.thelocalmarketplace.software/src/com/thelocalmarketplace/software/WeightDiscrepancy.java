package com.thelocalmarketplace.software;

/**
 * WeightDiscrepnancy class initialized by the ScaleListner class when a MassChange is announced by the scale. By default the status of the event is set to false. 
 * The status is changed by the ScaleListener class based on if a weight discrepancy is currently occurring. When weight discrepancy occurs, the customer is blocked
 * from performing further action such as paying or adding more items. 
 */

public class WeightDiscrepancy {
	
	private boolean status = false;
	
	/**
	 * Method called by ScaleListener object.
	 * Re-enables customer action. 	
	 * @param Controller 
	 * 			The CustomerStationControl object to be unblocked
	 */
	public void Unblock(CustomerStationControl Controller) {
		status = false;
		Controller.unblock();
	}
	/**
	 * Method called by ScaleListener object. Blocks customer from paying or adding more items.
	 * Announces to the customer and the Attendant that a weight discrepancy has been detected
	 * @param Controller
	 * 			The CustomerStationControl object to be blocked
	 */			
	public void WeightDiscrepancyEvent(CustomerStationControl Controller) {
		status = true;
		
		Controller.block();		
		Controller.notifyCustomer("Weight discrepancy detected. %n Please adjust items in bagging area before paying or adding new items %n");
		Controller.notifyAttendant("Weight Discrepancy at Customer Station");
		
		
	}
	/**
	 * Checks the status of this object so that repeated WeightDIscrepancyEvents are not triggered by the ScaleListener
	 * @return
	 * 			Boolean of the status is returned.
	 */
	public boolean checkStatus() {
		return(status);
	}
	
	
}
