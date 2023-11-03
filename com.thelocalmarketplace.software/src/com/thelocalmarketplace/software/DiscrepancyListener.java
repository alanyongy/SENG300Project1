package com.thelocalmarketplace.software;

import com.jjjwelectronics.Mass;

public class DiscrepancyListener {
	protected Mass expectedMass;
	
	public void updateExpectedMass(Mass mass) {
		expectedMass = mass;
	}
}
