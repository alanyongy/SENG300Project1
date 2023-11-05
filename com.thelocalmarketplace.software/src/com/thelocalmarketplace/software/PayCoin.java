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

import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.coin.CoinSlot;
import com.tdc.coin.CoinSlotObserver;
import com.tdc.coin.CoinStorageUnit;
import com.tdc.coin.CoinStorageUnitObserver;
import com.tdc.coin.CoinValidator;
import com.tdc.coin.CoinValidatorObserver;

/**
 * PayCoin controls all coin payments. PayCoin object is instantiated by CustomerStationControl class, 
 * where it waits for a valid coin to be detected by CoinValidatorObserver, which is then processed as 
 * payment for the customer's order. The coin payment is then processed, and added to storage by 
 * CoinStorageUnitObserver, which indicates successful order payment. 
 * 
 * <p>
 * Implements {@link com.tdc.coin.CoinValidatorObserver CoinValidObserver},
 * and {@link com.tdc.coin.CoinStorageUnitObserver}
 * </p>
 * 
 */

public class PayCoin extends AbstractPay implements CoinValidatorObserver, CoinStorageUnitObserver, CoinSlotObserver {

	/**
	 * creates PayCoin controller in CustomerStationControl class
	 * @param customerStationControl the station to which the controller is implemented
	 */
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
		// not used in this iteration
		
	}

	/**
	 * called when there is no empty space in coin storage. This means inserted coins cannot be processed 
	 * for payment and notifies the attendant that the storage is full
	 * 
	 * @param unit CoinStorageUnit device that stores coins for the station
	 */
	@Override
	public void coinsFull(CoinStorageUnit unit) {
		coinAdded(unit);
		customerStationControl.notifyAttendant("Coin Storage Full", customerStationControl.notifyOtherCode);

	}

	/**
	 * Called when valid coins inserted for payment end up in storage (if there is space in storage),
	 * which then can record the amount due/paid for the order. 
	 * 
	 * @param unit CoinStorageUnit device that stores coins for the station
	 */
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

	/**
	 * Called when valid coin is inserted into coin slot of station and starts the payment process
	 * 
	 * @param validator CoinValidator device that detects valid coins
	 * @param value value of the coins inserted for payment
	 */
	@Override
	public void validCoinDetected(CoinValidator validator, BigDecimal value) {
		processPayment(validator, value);
		
	}

	/**
	 * Called when invalid coins (not supported for payment) are inserted into coin slot, which cannot 
	 * start the payment process, so customer is notified to insert valid coins.
	 * 
	 * @param validator CoinValidator device that can detect invalid coins
	 */
	@Override
	public void invalidCoinDetected(CoinValidator validator) {
		customerStationControl.notifyCustomer("Please insert a valid coin", customerStationControl.notifyInvalidCoinCode);
		
	}

	/**
	 * Begins payment process by updating the remaining balance for the order if there is no weight discrepancy 
	 * in the bagging area. Also sends the customer a message about how much is still due. 
	 */
	@Override
	public BigDecimal pay(Order order) {
		BigDecimal totalUnpaid = order.getTotalUnpaid();
	    
	    if (totalUnpaid.compareTo(BigDecimal.ZERO) > 0 && !customerStationControl.isBlocked()) {
	        updateRemainingBalance(order);
	        customerStationControl.notifyCustomer("Please insert your payment", customerStationControl.notifyInsertPaymentCode);
	    } 
	    return totalUnpaid;
	}
	
	/**
	 * notifies the customer of the unpaid amount for the order
	 * @param order	the customer's order for which they are paying
	 */
	public void updateRemainingBalance(Order order) {
		BigDecimal totalUnpaid = order.getTotalUnpaid();
		customerStationControl = order.getCustomerStationControl();
		customerStationControl.notifyCustomer(String.format("Amount due: %.2f", totalUnpaid), customerStationControl.notifyOtherCode);
	}
	
	/**
	 * Processes amountDue and notifies the customer of the amount still unpaid for the order
	 * @param order the customer's order for which payment is being processed
	 */
	public void updatePayment(Order order) {
		amountDue = BigDecimal.ZERO;
		order.addCoinsPaid(amountDue);
		BigDecimal totalUnpaid = order.getTotalUnpaid();
		
		if (totalUnpaid.compareTo(BigDecimal.ZERO) > 0) {
	        updateRemainingBalance(order);
	        customerStationControl.notifyCustomer("Please insert your payment", customerStationControl.notifyInsertPaymentCode);
	    }
		
		if (totalUnpaid.compareTo(BigDecimal.ZERO) == 0) {
			fullPaid();
		} 
//		else if (totalUnpaid.compareTo(BigDecimal.ZERO) < 0) {
//			//where change would be implemented
//		}
		
	}
		

	/**
	 * After detecting the inserted coins, the updating payment process will be called to set the 
	 * amountDue for the order. The paid amount is then subtracted from the order total when the 
	 * coin reaches storage and calls coinsFull/coinsAdded.
	 * 
	 * @param validator CoinValidator device that validates coins for payment
	 * @param num value of the coins inserted by customer for payment
	*/
	@Override
	public void processPayment(CoinValidator validator, BigDecimal num) {
		amountDue = num;
	}

	/*
	 * Called once the total order has been paid for and sets amountDue back to 0
	 */
	@Override
	public void fullPaid() {
		amountDue = BigDecimal.ZERO;
		
		
	}

	@Override
	public void coinInserted(CoinSlot slot) {
		// not used in this iteration	
	}
	
	/**
	 * 
	 * @return amount due for the order
	 */
	public BigDecimal getAmountDue() {
		return amountDue; 
	}
	
	

}
