package com.thelocalmarketplace.software;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;

/**
 * Represents various types of products that are added to the order list.
 * Mass refers to the expected weight of the product if available, 
 * otherwise the recorded weight in the case of priced-by-weight items such as PLUProducts.
 * @author Alan
 */
public class SessionItem extends Item {
	protected SessionItem(Mass mass) {
		super(mass);
	}
}
