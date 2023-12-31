/**
 * Implements the BarcodeScannerListener to allow the control software to react to various events
 * involving the barcode scanner.
 * 
 * @author Jaden Taylor (30113034)
 * @author Alan Yong (30105707)
 * @author Kear Sang Heng (30087289)
 * @author Daniel Yakimenka (10185055)
 * @author Anandita Mahika (30097559)
 */
package com.thelocalmarketplace.software;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.IBarcodeScanner;

/**Listens for events thrown by a scanner and takes appropriate actions.
 * @author Alan Yong (30105707)
 */
public class BarcodeScanListener implements BarcodeScannerListener{
	CustomerStationControl customerStationControl;
	
	BarcodeScanListener(CustomerStationControl c){
		customerStationControl = c;
	}

	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		// Not used in current implementation.
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		// Not used in current implementation.
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// Not used in current implementation.
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		// Not used in current implementation.
	}

	/**
	 * Triggered whenever an item is scanned with the barcode scanner. Entry point for interaction
	 * between the hardware and the software.
	 */
	@Override
	public void aBarcodeHasBeenScanned(IBarcodeScanner barcodeScanner, Barcode barcode) {
		// Silently ignores scan if a session has not been started
		if (!customerStationControl.getSessionStarted()) {
			return;
		}
		
		customerStationControl.getOrder().add(barcode);
	}
}