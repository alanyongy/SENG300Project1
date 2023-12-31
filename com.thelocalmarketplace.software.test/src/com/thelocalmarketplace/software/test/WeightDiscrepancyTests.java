/**
 * Contains tests to ensure the system is correctly reacting to weight discrepancies.
 * 
 * @author Jaden Taylor (30113034)
 * @author Alan Yong (30105707)
 * @author Kear Sang Heng (30087289)
 * @author Daniel Yakimenka (10185055)
 * @author Anandita Mahika (30097559)
 */
package com.thelocalmarketplace.software.test;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.SelfCheckoutStation;
import com.thelocalmarketplace.software.CustomerStationControl;

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
		PowerGrid.engageUninterruptiblePowerSource();
		station.baggingArea.turnOn();
		station.scanner.turnOn();
		
		// Initialize control and start the session
		control = new CustomerStationControl(station);
		control.startSession();
		
		// Initialize database
		ExampleItems.updateDatabase();
	}
	
	/**
	 * After fixing a discrepancy by adding the correct item, the station is unblocked.
	 */
	@Test
	public void correctDiscrepancyByAddingItem() {
		createDiscrepancyByRemoving(ExampleItems.PotatoChips.barcodedItem);
		station.baggingArea.addAnItem(ExampleItems.PotatoChips.barcodedItem);
		Assert.assertTrue(!control.isBlocked());
	}
	
	/**
	 * After fixing a discrepancy by removing incorrect item, the station is unblocked.
	 */
	@Test
	public void correctDiscrepancyByRemovingItem() {
		station.scanner.scan(ExampleItems.PotatoChips.barcodedItem);
		station.baggingArea.addAnItem(ExampleItems.PotatoChips.barcodedItem);
		station.baggingArea.addAnItem(ExampleItems.AppleJuice.barcodedItem);
		station.baggingArea.removeAnItem(ExampleItems.AppleJuice.barcodedItem);
		Assert.assertTrue(!control.isBlocked());
	}
	
	
	/**
	 * The station is blocked after adding the wrong item to the bagging area.
	 */
	@Test
	public void addWrongItem() {
		station.scanner.scan(ExampleItems.PotatoChips.barcodedItem);
		station.baggingArea.addAnItem(ExampleItems.AppleJuice.barcodedItem);
		Assert.assertTrue(control.isBlocked());
	}
	
	/**
	 * The station is blocked after removing the correctly added item from the bagging area.
	 */
	@Test
	public void removeCorrectlyAddedItem() {
		station.scanner.scan(ExampleItems.PotatoChips.barcodedItem);
		station.baggingArea.addAnItem(ExampleItems.PotatoChips.barcodedItem);
		station.baggingArea.removeAnItem(ExampleItems.PotatoChips.barcodedItem);
		Assert.assertTrue(control.isBlocked());
	}
	
	/**
	 * The station is blocked after adding an additional item after correctly adding an item to the bagging area.
	 */
	@Test
	public void addAdditionalItem() {
		station.scanner.scan(ExampleItems.PotatoChips.barcodedItem);
		station.baggingArea.addAnItem(ExampleItems.PotatoChips.barcodedItem);
		station.baggingArea.addAnItem(ExampleItems.AppleJuice.barcodedItem);
		Assert.assertTrue(control.isBlocked());
	}
	
	/**
	 * The attendant is notified after a weight discrepancy occurs. Currently uses
	 * a variable in control for testing due to lack signaling UI implementation.
	 */
	@Test
	public void discrepancyAttendantNotified() {
		createDiscrepancyByRemoving(ExampleItems.PotatoChips.barcodedItem);
		Assert.assertEquals(control.getAttendantNotified(), control.notifyDiscrepancyCode);
	}
	
	/**
	 * The customer is notified after a weight discrepancy occurs. Currently uses
	 * a variable in control for testing due to lack signaling UI implementation.
	 */
	@Test
	public void discrepancyCustomerNotified() {
		createDiscrepancyByRemoving(ExampleItems.PotatoChips.barcodedItem);
		Assert.assertEquals(control.getCustomerNotified(), control.notifyDiscrepancyCode);
	}
	
	/**
	 * The station is unable to add items to the order after a discrepancy occurs.
	 */
	@Test
	public void discrepancyAddItem() {
		createDiscrepancyByRemoving(ExampleItems.PotatoChips.barcodedItem);
		station.scanner.scan(ExampleItems.AppleJuice.barcodedItem);
		Assert.assertTrue(control.getOrder().getItems().size()==1);
	}
	
	/**
	 * The station is unable to pay for the order after a discrepancy occurs.
	 * This condition is checked by the lack of prompts to the customer to insert payment 
	 * after attempting to start the payment process.
	 */
	@Test
	public void discrepancyPay() {
		createDiscrepancyByRemoving(ExampleItems.PotatoChips.barcodedItem);
		control.pay();
		Assert.assertFalse(control.getCustomerNotified() == control.notifyInsertPaymentCode);
	}
	
	/**
	 * Creates a weight discrepancy by removing a correctly added item from the bagging area.
	 */
	private void createDiscrepancyByRemoving(BarcodedItem item) {
		station.scanner.scan(item);
		station.baggingArea.addAnItem(item);
		station.baggingArea.removeAnItem(item);
	}
}
