package com.thelocalmarketplace.software;

import java.math.BigDecimal;
import java.util.Currency;

import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.coin.CoinSlot;
import com.tdc.coin.CoinSlotObserver;
import com.tdc.coin.CoinStorageUnit;
import com.tdc.coin.CoinStorageUnitObserver;
import com.tdc.coin.CoinValidator;
import com.tdc.coin.CoinValidatorObserver;

public class PayCoin extends AbstractPay implements CoinValidatorObserver, CoinStorageUnitObserver, CoinSlotObserver {

	public PayCoin(CustomerStationControl customerStationControl) {
		super(customerStationControl);
	}

	@Override
	public void enabled(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disabled(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnedOn(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnedOff(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinInserted(CoinSlot slot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinsFull(CoinStorageUnit unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinAdded(CoinStorageUnit unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinsLoaded(CoinStorageUnit unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinsUnloaded(CoinStorageUnit unit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validCoinDetected(CoinValidator validator, BigDecimal value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void invalidCoinDetected(CoinValidator validator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BigDecimal pay(Order order) {
		BigDecimal totalUnpaid = order.getTotalUnpaid();
	    
	    if (totalUnpaid.compareTo(BigDecimal.ZERO) > 0) {
	        updateAmountDue(order);
	        customerStationControl.notifyCustomer("Please insert your payment");
	    } else {
	        float absChange = Math.abs(totalUnpaid.floatValue());
	        customerStationControl.notifyCustomer("Your change: %.2f".formatted(absChange));
	    }
	    
	    return totalUnpaid;
	}
	
	private void updateAmountDue(Order order) {
		// TODO Auto-generated method stub
		
	}
		

	@Override
	public void processPayment(CoinValidator validator, Currency currency, int num) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endPay() {
		// TODO Auto-generated method stub
		
	}
	
	

}
