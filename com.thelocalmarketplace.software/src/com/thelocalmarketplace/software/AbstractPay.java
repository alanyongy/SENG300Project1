/**
 * Abstract class to provide base functionality for different pay options.
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

import com.tdc.*;
import com.tdc.coin.*;

//abstract class for all coin payment methods

public abstract class AbstractPay {
	protected CustomerStationControl customerStationControl;
	
	protected BigDecimal amountDue;
	
	public AbstractPay(CustomerStationControl customerStationControl) {
		this.customerStationControl = customerStationControl;
	}
	
	/**
	 * processes the payment for the customer's order
	 * @param order the list of items the customer is paying for
	 * @return remaining balance to fully pay the order, 0 if order has been fully paid
	 */
	public abstract BigDecimal pay(Order order);
	
	public abstract void processPayment(CoinValidator validator, BigDecimal num);
	
	public abstract void fullPaid();
	
}
