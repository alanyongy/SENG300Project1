package com.thelocalmarketplace.software.test;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.thelocalmarketplace.hardware.SelfCheckoutStation;
import com.thelocalmarketplace.software.CustomerStationControl;
import com.thelocalmarketplace.software.SessionItem;

import java.math.BigDecimal;

import org.junit.Assert;
import powerutility.PowerGrid;

public class WeightDiscrepancyTests {
	private SelfCheckoutStation station;
	private CustomerStationControl control;
	
	@Before
	public void init() {
		// initialize station and turn on required components
		station = new SelfCheckoutStation();
		station.plugIn(PowerGrid.instance());
		station.baggingArea.turnOn();
		station.scanner.turnOn();
		
		// Initialize control and start the session
		control = new CustomerStationControl(station);
		control.startSession();
		
		// Initialize database
		ExampleItems.updateDatabase();
	}
	/**
	 * Creates a weight discrepancy and ensures that the attendant is notified. Currently uses
	 * a variable in control for testing due to lack signaling UI implementation.
	 */
	@Test
	public void discrepancyAttendantNotified() {
		createDiscrepancy();
		Assert.assertEquals(control.attendantNotified, control.notifyDiscrepancyCode);
	}
	
	/**
	 * Creates a weight discrepancy and ensures that the customer is notified. Currently uses
	 * a variable in control for testing due to lack signaling UI implementation.
	 */
	@Test
	public void discrepancyCustomerNotified() {
		createDiscrepancy();
		Assert.assertEquals(control.customerNotified, control.notifyDiscrepancyCode);
	}
	
	/**
	 * Creates a weight discrepancy and ensures that the station is unable to add items.
	 */
	@Test
	public void discrepancyAddItem() {
		createDiscrepancy();
		station.scanner.scan(ExampleItems.AppleJuice.barcodedItem);
		Assert.assertTrue(control.getOrder().getItems().size()==1);
	}
	
	/**
	 * Creates a weight discrepancy and ensures that the station is unable to initiate payment, 
	 * thus not prompting the customer to insert payment.
	 */
	@Test
	public void discrepancyPay() {
		createDiscrepancy();
		control.pay();
		Assert.assertFalse(control.customerNotified == control.notifyInsertPaymentCode);
	}
	
	private void createDiscrepancy() {
		station.scanner.scan(ExampleItems.PotatoChips.barcodedItem);
		station.baggingArea.addAnItem(ExampleItems.PotatoChips.barcodedItem);
		station.baggingArea.removeAnItem(ExampleItems.PotatoChips.barcodedItem);
	}
}
