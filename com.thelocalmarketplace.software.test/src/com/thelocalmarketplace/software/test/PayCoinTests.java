package com.thelocalmarketplace.software.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import com.thelocalmarketplace.software.PayCoin;
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
	// Fields to hold components for testing
	private CustomerStationControl control;
	private SelfCheckoutStation station;
	private PayCoin payCoin;
	private CoinStorageUnit coinStorageStub;
	private BigDecimal dollar = new BigDecimal(1.00);
	private List<BigDecimal> denominations;
	private Coin testCoin;
	private Currency usd = Currency.getInstance("USD");
	private Order order;
	
	
	//Setup to initialize common components before testing
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
				
				// Initialize Coin Storage Unit with size 2
				coinStorageStub = new CoinStorageUnit(2);
				coinStorageStub.connect(PowerGrid.instance());
				
				
				testCoin = new Coin(usd, dollar);
				
				payCoin = new PayCoin(control);
				
				denominations = new ArrayList<>();
				denominations.add(dollar);
				
				
				order = new Order(control);
	}
	
	// Testing behavior when coins are added to a full storage
	@Test
	public void addingCoinToFullStorage() throws DisabledException, CashOverloadException{
		
		coinStorageStub.activate();
		coinStorageStub.enable();
		
		coinStorageStub.receive(testCoin);
		coinStorageStub.receive(testCoin);
		
		payCoin.coinsFull(coinStorageStub);
	
		assertTrue(control.getAttendantNotified());
		
	}

	// Test behavior when a valid coin is detected
	@Test
	public void validCoinDetectedTest() {
		BigDecimal expectedAmountDue = dollar;
		payCoin.validCoinDetected(new CoinValidator (usd, denominations), dollar);
		assertEquals(expectedAmountDue, payCoin.getAmountDue());
	}
	
	
	// Test behavior when a invalid coin is detected
	@Test
	public void invalidCoinDetectedTest() {
		Boolean customerNotified = control.getCustomerNotified();
		assertNull(control.getCustomerNotified());
		payCoin.invalidCoinDetected(new CoinValidator (usd, denominations));
		customerNotified = control.getCustomerNotified();
		assertNotNull(customerNotified);
		assertTrue(control.getCustomerNotified());
	}
	
	
	// Testing pay method
	@Test
	public void testPayMethod() {
		BigDecimal expectedTotalUnpaid = new BigDecimal("10.00");
		
		order.setTotalUnpaid(expectedTotalUnpaid);
		BigDecimal returnedTotalUnpaid = payCoin.pay(order);
		
		assertEquals(expectedTotalUnpaid, returnedTotalUnpaid);
		
	}

	// Test updating the remain balance when there is a positive unpaid amount
	@Test
	public void testUpdateRemainingBalanceWithPositiveAmount() {
		order.setTotalUnpaid(new BigDecimal("5.00"));
		payCoin.updateRemainingBalance(order);
		assertEquals("Customer: Amount due: 5.00", control.getLastNotification());
	}
	
	// Test updating the remain balance when there zero unpaid amount
	@Test
	public void testUpdateRemainingBalanceWithZeroAmount() {
		order.setTotalUnpaid(BigDecimal.ZERO);
		payCoin.updateRemainingBalance(order);
		assertEquals("Customer: Amount due: 0.00", control.getLastNotification());
	}
	
	// Test updating the remain balance when there is a negative unpaid amount
	@Test
	public void testUpdateRemainingBalanceWithNegativeAmount() {
		order.setTotalUnpaid(new BigDecimal("-5.00"));
		payCoin.updateRemainingBalance(order);
		assertEquals("Customer: Amount due: -5.00", control.getLastNotification());
	}
	
	// Test update payment on order when not paid
	@Test
	public void testUpdatePaymentOnUnpaidOrder() {
		order.setTotalUnpaid(new BigDecimal("5.00"));
		payCoin.updatePayment(order);
		assertEquals("Customer: Please insert your payment", control.getLastNotification());
	}
	// Test update payment on order when paid
	@Test
	public void testUpdatePaymentOnFullyPaidOrder() {
		order.setTotalUnpaid(BigDecimal.ZERO);
		payCoin.updatePayment(order);
		assertEquals(BigDecimal.ZERO, payCoin.getAmountDue());
	}
	// Test update payment on order when over paid
	@Test
	public void testUpdatePaymentOnOverPaidOrder() {
		order.setTotalUnpaid(BigDecimal.ZERO);
		payCoin.updatePayment(order);
		assertEquals(BigDecimal.ZERO, payCoin.getAmountDue());
	}
	
	// Test behavior when payment is processed
	@Test
	public void testProcessPayment() {
		BigDecimal paymentAmount = new BigDecimal("15.00");
		assertNull(payCoin.getAmountDue());
		
		payCoin.processPayment(null, paymentAmount);
		
		assertEquals(paymentAmount, payCoin.getAmountDue());
		
	}
	
	// Test the behavior when and order is fully paid
	@Test
	public void testFullPaid() {
		BigDecimal paymentAmount = new BigDecimal("15.00");
		payCoin.processPayment(null, paymentAmount);
		assertEquals(paymentAmount, payCoin.getAmountDue());
		
		payCoin.fullPaid();
		assertEquals(BigDecimal.ZERO, payCoin.getAmountDue());
	}
	
	
}
