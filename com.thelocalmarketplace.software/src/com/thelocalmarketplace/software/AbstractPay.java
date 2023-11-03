package com.thelocalmarketplace.software;

import com.thelocalmarketplace.hardware.*;

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
