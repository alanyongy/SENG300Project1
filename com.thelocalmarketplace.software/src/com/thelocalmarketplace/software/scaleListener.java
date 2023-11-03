package com.thelocalmarketplace.software;

import java.util.ArrayList;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Mass.MassDifference;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;
import com.thelocalmarketplace.hardware.BarcodedProduct;

public class scaleListener implements ElectronicScaleListener {
	
	protected CustomerStationControl Controller; 
	
	protected Mass expectedMass; // Must be updated based on current cart
	protected Mass actualMass;
	protected Mass sensLimit;
	protected MassDifference delta;
	protected WeightDiscrepancy Discrepancy; 
	protected Mass Sum;
	
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass){
		actualMass = mass;
		sensLimit = scale.getSensitivityLimit();
		expectedMass = getExpectedMass();
		delta = actualMass.difference(expectedMass);
		
		if (delta.compareTo(sensLimit) == 1 && Discrepancy.checkStatus() == false) {
			Discrepancy.WeightDiscrepancyEvent(Controller);}
		
		else if (delta.compareTo(sensLimit) != 1 && Discrepancy.checkStatus() == true) {
			Discrepancy.Unblock(Controller);
		}
				
		
		
	}
	
	public int getStatus() {
		return(delta.compareTo(sensLimit));	
	}
	
	public Mass getExpectedMass() {
		// needs items in order to have a mass so that the expected mass can be calculated
		// PLACEHOLDER BARCODED PRODUCT ARRAY, SHOULD NOT BE FINAL
		ArrayList<BarcodedProduct> items = new ArrayList<BarcodedProduct>();
		items = Order.getItems;
		for (BarcodedProduct i : items) {
			Mass iMass = new Mass(i.getExpectedWeight());
			
			
			Sum = Sum.sum(iMass);	
		}
		return (Sum);
		
	}
	
	
	public scaleListener(CustomerStationControl CSC){
		Controller = CSC;
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