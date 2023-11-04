package com.thelocalmarketplace.software;

import java.util.ResourceBundle.Control;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Mass.MassDifference;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;
/**
 * Implementation of ElectronicScaleListener. 
 * Checks if the change in mass on the scale should result in a weight discrepancy event and trigger the event to occur when appropriate.
 * If a WeightDiscrepancy event is active but expected mass matches the actual mass after a mass change event, the Weight Discrepancy event is stopped. 
 */
public class ScaleListener implements ElectronicScaleListener {
	/**
	 * CustomerStationControl object which sends order data and can be blocked if a Weight Discrepancy occurs
	 */
	protected CustomerStationControl Controller; 
	
	/**
	 * Expected Mass on the scale based on items
	 */
	protected Mass expectedMass;
	/**
	 * Actual Mass on scale announced by the scale
	 */
	protected Mass actualMass;
	/**
	 * Scale sensitivity limit, differences in mass below this limit cannot be detected
	 */
	protected Mass sensLimit;
	/**
	 * Difference between actual and expected mass on scale
	 */
	protected Mass delta;
	/**
	 * WeightDiscrepancy object to be enabled and disabled by listener
	 */
	protected WeightDiscrepancy Discrepancy; 
	
	/**
	 * Method called by scale when mass on it changes. Checks if the mass difference (delta) between expected and actual is higher than sensitivity.
	 * If it is, triggers a new WeightDiscrepancyEvent to occur. Does nothing if it is already active.
	 * If it isn't, stops an active WeightDiscrepancyEvent or does nothing if one is not active. 
	 */
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass){

		actualMass = mass;
		sensLimit = scale.getSensitivityLimit();
		expectedMass = Controller.order.getExpectedMass();
		delta = actualMass.difference(expectedMass).abs();

		// Address Scenario: New weight added to the scale that causes expected mass to differ from actual mass
		if (delta.compareTo(sensLimit) == 1 && Discrepancy.checkStatus() == false) {
			Discrepancy.WeightDiscrepancyEvent(Controller);}
		// Address Scenario: New weight added causes the expected mass to match the actual mass, resolving an existing discrepancy block
		else if (delta.compareTo(sensLimit) != 1 && Discrepancy.checkStatus() == true) {
			Discrepancy.Unblock(Controller);
		} 
		// Address Scenario: New weight added causes the expected mass to match the actual mass, resolving block in controller caused by add in order class
		else if (delta.compareTo(sensLimit) != 1 && Controller.blocked == true) {
			Controller.unblock();
		}
		// If all these scenarios are false that means that either: Discrepancy is active and the latest mass change did not make
		// expected weight match actual weight. OR Discrepancy is inactive,the added weight caused the expected mass to match actual mass, and the
		// controller is not blocked. (I dont think this is ever true)
	}

	/**
	 * Simple Constructor for ScaleListener, allows a CustomerStationControl object to which ScaleListener blocks to be specified.
	 * @param CSC
	 * 				CustomerStationControl object to interact with
	 */				
	public ScaleListener(CustomerStationControl CSC){
		Controller = CSC;
		Discrepancy = new WeightDiscrepancy();
	}
	
	public void theMasOnTheScaleHasExceededItsLimit(IElectronicScale scale) {}
	
	@Override
	public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale) {
		// TODO Auto-generated method stub
	}
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}
}
