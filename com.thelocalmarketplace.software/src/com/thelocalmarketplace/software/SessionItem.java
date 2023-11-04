/**
 * Basic item to hold the expected weight of items added to an order for ease of access
 * when comparing the actual weight to the expected weight.
 * 
 * @author Jaden Taylor (30113034)
 * @author Alan Yong (30105707)
 * @author Kear Sang Heng (30087289)
 * @author Daniel Yakimenka (10185055)
 * @author Anandita Mahika (30097559)
 */
package com.thelocalmarketplace.software;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;

/**
 * Represents various types of products that are added to the order list.
 * Mass refers to the expected weight of the product if available, 
 * otherwise the recorded weight in the case of priced-by-weight items such as PLUProducts.
 * @author Alan Yong (30105707)
 */
public class SessionItem extends Item {
	protected SessionItem(Mass mass) {
		super(mass);
	}
}
