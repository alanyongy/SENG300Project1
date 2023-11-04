/**
 * Supports the specific payment method of paying with coins.
 * 
 * @author Jaden Taylor (30113034)
 * @author Alan Yong (30105707)
 * @author Kear Sang Heng (30087289)
 * @author Daniel Yakimenka (10185055)
 * @author Anandita Mahika (30097559)
 */
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
		// not used in this iteration
		
	}

	@Override
	public void disabled(IComponent<? extends IComponentObserver> component) {
		// not used in this iteration
		
	}

	@Override
	public void turnedOn(IComponent<? extends IComponentObserver> component) {
		// not used in this iteration
		
	}

	@Override
	public void turnedOff(IComponent<? extends IComponentObserver> component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void coinsFull(CoinStorageUnit unit) {
		coinAdded(unit);
		customerStationControl.notifyAttendant("Coin Storage Full", customerStationControl.notifyOtherCode);

	}

	@Override
	public void coinAdded(CoinStorageUnit unit) {
		Order order = customerStationControl.getOrder();
		updatePayment(order);
		
	}

	@Override
	public void coinsLoaded(CoinStorageUnit unit) {
		// not used in this iteration
		
	}

	@Override
	public void coinsUnloaded(CoinStorageUnit unit) {
		// not used in this iteration
		
	}

	@Override
	public void validCoinDetected(CoinValidator validator, BigDecimal value) {
		processPayment(validator, value);
		
	}

	@Override
	public void invalidCoinDetected(CoinValidator validator) {
		customerStationControl.notifyCustomer("Please insert a valid coin", customerStationControl.notifyOtherCode);
		
	}

	@Override
	public BigDecimal pay(Order order) {
		BigDecimal totalUnpaid = order.getTotalUnpaid();
	    
	    if (totalUnpaid.compareTo(BigDecimal.ZERO) > 0 && !customerStationControl.isBlocked()) {
	        updateRemainingBalance(order);
	        customerStationControl.notifyCustomer("Please insert your payment", customerStationControl.notifyInsertPaymentCode);
	    } 
	    return totalUnpaid;
	}
	
	private void updateRemainingBalance(Order order) {
		BigDecimal totalUnpaid = order.getTotalUnpaid();
		customerStationControl = order.getCustomerStationControl();
		customerStationControl.notifyCustomer(String.format("Amount due: %.2f", totalUnpaid), customerStationControl.notifyOtherCode);
	}
	
	public void updatePayment(Order order) {
		order.addCoinsPaid(amountDue);
		amountDue = BigDecimal.ZERO;
		BigDecimal totalUnpaid = order.getTotalUnpaid();
		
		if (totalUnpaid.compareTo(BigDecimal.ZERO) > 0) {
	        updateRemainingBalance(order);
	        customerStationControl.notifyCustomer("Please insert your payment", customerStationControl.notifyInsertPaymentCode);
	    }
		
		System.out.println(totalUnpaid);
		
		if (totalUnpaid.compareTo(BigDecimal.ZERO) == 0) {
			fullPaid();
		} else if (totalUnpaid.compareTo(BigDecimal.ZERO) < 0) {
			fullPaid(); //where change would be implemented
		}
		
	}
		

	@Override
	public void processPayment(CoinValidator validator, BigDecimal num) {
		amountDue = num;
	}

	@Override
	public void fullPaid() {
		amountDue = BigDecimal.ZERO;
		
		
	}

	@Override
	public void coinInserted(CoinSlot slot) {
		// TODO Auto-generated method stub
		
	}
	
	public BigDecimal getAmountDue() {
		return amountDue;
	}
	
	

}
