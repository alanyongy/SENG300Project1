package com.thelocalmarketplace.software.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.jjjwelectronics.Mass;
import com.thelocalmarketplace.hardware.SelfCheckoutStation;
import com.thelocalmarketplace.software.CustomerStationControl;
import com.thelocalmarketplace.software.SessionItem;

import powerutility.PowerGrid;

public class AddItemTests {
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
	
	
	@Test
	public void itemAddedWhenScanned() {
		ArrayList<SessionItem> items = control.order.getItems();
		
		// Add an item and check if the size increases
		assertEquals(0, items.size());
		station.scanner.scan(ExampleItems.AppleJuice.barcodedItem);
		assertEquals(1, items.size());
		
		// Check if the expected mass of the added item matches the item that was added
		assertTrue(items.get(0).getMass().equals(new Mass(ExampleItems.AppleJuice.expectedWeightGrams)));
		assertFalse(items.get(0).getMass().equals(new Mass(ExampleItems.PotatoChips.expectedWeightGrams)));
	}
	
	@Test
	public void systemBlockedWhenAdded() {
		assertFalse(control.blocked);
		station.scanner.scan(ExampleItems.PeanutButter.barcodedItem);
		assertTrue(control.blocked);
	}
	
	@Test
	public void totalPriceUpdatedCorrectly() {
		BigDecimal expectedTotal = new BigDecimal(0);
		assertEquals(expectedTotal, control.order.getTotal());
		station.scanner.scan(ExampleItems.AppleJuice.barcodedItem);
		expectedTotal = expectedTotal.add(ExampleItems.AppleJuice.bdPrice);
		assertEquals(expectedTotal, control.order.getTotal());
	}
	
	@Test
	public void totalUnpaidPriceUpdatedCorrectly() {
		BigDecimal expectedUnpaid = new BigDecimal(0);
		assertEquals(expectedUnpaid, control.order.getTotalUnpaid());
		station.scanner.scan(ExampleItems.AppleJuice.barcodedItem);
		expectedUnpaid = expectedUnpaid.add(ExampleItems.AppleJuice.bdPrice);
		assertEquals(expectedUnpaid, control.order.getTotalUnpaid());
	}
	
	@Test
	public void systemUnblocked() {
		assertFalse(control.blocked);
		station.scanner.scan(ExampleItems.AppleJuice.barcodedItem);
		assertTrue(control.blocked);
		station.baggingArea.addAnItem(ExampleItems.AppleJuice.barcodedItem);
		assertFalse(control.blocked);
		
	}
}
