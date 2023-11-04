/**
 * Contains tests to ensure an item is being added to the order correctly.
 * 
 * @author Jaden Taylor (30113034)
 * @author Alan Yong (30105707)
 * @author Kear Sang Heng (30087289)
 * @author Daniel Yakimenka (10185055)
 * @author Anandita Mahika (30097559)
 */
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
		
		// Initialize control
		control = new CustomerStationControl(station);
		
		// Initialize database
		ExampleItems.updateDatabase();
	}
	
	/**
	 * Checks if an item is added to the order after it has been scanned, ensures the weight updates correctly
	 */
	@Test
	public void itemAddedWhenScanned() {
		control.startSession();
		ArrayList<SessionItem> items = control.getOrder().getItems();
		
		// Add an item and check if the size increases
		assertEquals(0, items.size());
		station.scanner.scan(ExampleItems.AppleJuice.barcodedItem);
		assertEquals(1, items.size());
		
		// Check if the expected mass of the added item matches the item that was added
		assertTrue(items.get(0).getMass().equals(new Mass(ExampleItems.AppleJuice.expectedWeightGrams)));
		assertFalse(items.get(0).getMass().equals(new Mass(ExampleItems.PotatoChips.expectedWeightGrams)));
	}
	
	/**
	 * Makes sure the system blocks when an item is added but not placed on the scale.
	 * Ensures if an item is scanned while the system is blocked it will not be added to the order.
	 */
	@Test
	public void systemBlockedWhenAdded() {
		control.startSession();
		ArrayList<SessionItem> items = control.getOrder().getItems();
		
		// Scan an item
		assertFalse(control.isBlocked());
		station.scanner.scan(ExampleItems.PeanutButter.barcodedItem);
		assertEquals(1, items.size());
		assertTrue(control.isBlocked());
		// Scan  an item after the system blocked, should be no items added.
		station.scanner.scan(ExampleItems.PotatoChips.barcodedItem);
		assertEquals(1, items.size());
		
	}
	
	/**
	 * Makes sure an item being scanned updates the total price by the price of the item.
	 */
	@Test
	public void totalPriceUpdatedCorrectly() {
		control.startSession();
		BigDecimal expectedTotal = new BigDecimal(0);
		assertEquals(expectedTotal, control.getOrder().getTotal());
		// Scan an item and check if the price is updated
		station.scanner.scan(ExampleItems.AppleJuice.barcodedItem);
		expectedTotal = expectedTotal.add(ExampleItems.AppleJuice.bdPrice);
		assertEquals(expectedTotal, control.getOrder().getTotal());
	}
	
	/**
	 * Makes sure an item being scanned updates the total unpaid price by the price of the item
	 */
	@Test
	public void totalUnpaidPriceUpdatedCorrectly() {
		control.startSession();
		BigDecimal expectedUnpaid = new BigDecimal(0);
		assertEquals(expectedUnpaid, control.getOrder().getTotalUnpaid());
		// Scan an item and check if the price is updated
		station.scanner.scan(ExampleItems.AppleJuice.barcodedItem);
		expectedUnpaid = expectedUnpaid.add(ExampleItems.AppleJuice.bdPrice);
		assertEquals(expectedUnpaid, control.getOrder().getTotalUnpaid());
	}
	
	/**
	 * Make sure the system unblocks when an item is placed on the bagging area.
	 */
	@Test
	public void systemUnblocked() {
		control.startSession();
		// Scan an item
		assertFalse(control.isBlocked());
		station.scanner.scan(ExampleItems.AppleJuice.barcodedItem);
		assertTrue(control.isBlocked());
		// Place an item on the scale
		station.baggingArea.addAnItem(ExampleItems.AppleJuice.barcodedItem);
		assertFalse(control.isBlocked());
	}
	
	/**
	 * Makes sure the customer is notified that they must place their item in the bagging area.
	 * Currently uses a variable in CustomerStationControl for testing due to lack signaling UI implementation.
	 */
	@Test
	public void notifiesCustomerToBagItem() {
		control.startSession();
		assertEquals(control.getCustomerNotified(), "");
		station.scanner.scan(ExampleItems.AppleJuice.barcodedItem);
		assertEquals(control.getCustomerNotified(), control.notifyPlaceItemInBaggingAreaCode);
	}
	
	/**
	 * Makes sure an item is ignored when it is scanned and the session is not started
	 */
	@Test
	public void itemAddedWhileSessionIsNotStarted() {
		// Scan item before starting session, should be no items in the order list
		ArrayList<SessionItem> items = control.getOrder().getItems();
		station.scanner.scan(ExampleItems.AppleJuice.barcodedItem);
		assertEquals(0, items.size());
		// Start session and scan an item again, should now have 1 item in the order list
		control.startSession();
		items = control.getOrder().getItems();
		station.scanner.scan(ExampleItems.AppleJuice.barcodedItem);
		assertEquals(1, items.size());
		
	}
}
