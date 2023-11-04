package com.thelocalmarketplace.software.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import com.thelocalmarketplace.software.PayCoin;
import com.thelocalmarketplace.software.AbstractPay;
import com.thelocalmarketplace.software.CustomerStationControl;
import com.thelocalmarketplace.hardware.SelfCheckoutStation;
import com.thelocalmarketplace.software.Order;
import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.coin.Coin;
import com.tdc.coin.CoinStorageUnit;
import com.tdc.coin.CoinValidator;

import powerutility.PowerGrid;

public class PayCoinTests {
	private CustomerStationControl control;
	private SelfCheckoutStation station;
	private PayCoin payCoin;
	private CoinStorageUnit coinStorageStub;
	private BigDecimal dollar = new BigDecimal(1.00);
	private List<BigDecimal> denominations;
	private Coin testCoin;
	private Currency usd = Currency.getInstance("USD");
	private Order order;
	
	
	@Before
	public void setup(){
		// initialize station and turn on required components
				station = new SelfCheckoutStation();
				station.plugIn(PowerGrid.instance());
				station.baggingArea.turnOn();
				station.scanner.turnOn();
				
				// Initialize control and start the session
				control = new CustomerStationControl(station);
				control.startSession();
				
				// Initialize Coin Storage Unit
				coinStorageStub = new CoinStorageUnit(2);
				coinStorageStub.connect(PowerGrid.instance());
				
				
				testCoin = new Coin(usd, dollar);
				
				payCoin = new PayCoin(control);
				
				denominations = new ArrayList<>();
				denominations.add(dollar);
				
				
				order = new Order(control);
				order.setTotalUnpaid(new BigDecimal(5));
				// Initialize database
		//		ExampleItems.updateDatabase();
		
	}
	
	@Test
	public void addingCoinToFullStorage() throws DisabledException, CashOverloadException{
		
		coinStorageStub.activate();
		coinStorageStub.enable();
		
		coinStorageStub.receive(testCoin);
		coinStorageStub.receive(testCoin);
		
		payCoin.coinsFull(coinStorageStub);
	
		assertTrue(control.getAttendantNotified());
		
	}

	@Test
	public void validCoinDetectedTest() {
		BigDecimal expectedAmountDue = dollar;
		payCoin.validCoinDetected(new CoinValidator (usd, denominations), dollar);
		assertEquals(expectedAmountDue, payCoin.getAmountDue());
	}
	
	
	
	@Test
	public void invalidCoinDetectedTest() {
		Boolean customerNotified = control.getCustomerNotified();
		assertNull(control.getCustomerNotified());
		payCoin.invalidCoinDetected(new CoinValidator (usd, denominations));
		customerNotified = control.getCustomerNotified();
		assertNotNull(customerNotified);
		assertTrue(control.getCustomerNotified());
	}
	

	
	

}
