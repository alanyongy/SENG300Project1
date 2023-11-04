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

public abstract class AbstractPay {
	protected CustomerStationControl customerStationControl;
	
	protected BigDecimal amountDue;
	
	public AbstractPay(CustomerStationControl customerStationControl) {
		this.customerStationControl = customerStationControl;
	}
	
	public abstract BigDecimal pay(Order order);
	
	public abstract void processPayment(CoinValidator validator, BigDecimal num);
	
	public abstract void fullPaid();
	
	
	//need method that updates how much is due to customer
}
