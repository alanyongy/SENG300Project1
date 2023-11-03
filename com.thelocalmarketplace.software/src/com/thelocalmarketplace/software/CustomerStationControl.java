// Class that contains actions that can be performed by a customer station
// Authors:
// Jaden Taylor (30113034)
// TODO: Add your names and if you are the last person to do so delete this comment
package com.thelocalmarketplace.software;

public class CustomerStationControl {
	public boolean blocked = false;
	
	public void block() {
		blocked = true;
	}
	
	public void unblock() {
		blocked = false;
	}
}