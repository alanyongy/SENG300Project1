package com.thelocalmarketplace.software.test;

import java.math.BigDecimal;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.external.ProductDatabases;


// Contains example items that can be used for testing
public class ExampleItems {
	public static void updateDatabase() {
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(AppleJuice.barcode, AppleJuice.barcodedProduct);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(PotatoChips.barcode, PotatoChips.barcodedProduct);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(PeanutButter.barcode, PeanutButter.barcodedProduct);
	}
	
	public static class AppleJuice {
		static Numeral[] barcodeDigits = {Numeral.one, Numeral.five, Numeral.seven, Numeral.three};
		static double actualWeightGrams = 225;
		static Mass actualMass = new Mass(actualWeightGrams);
		static double expectedWeightGrams = 225;
		static String desc = "Box of apple juice.";
		static long price = 3;
		static BigDecimal bdPrice = new BigDecimal((double)price);
		static Barcode barcode = new Barcode(barcodeDigits);
		static BarcodedItem barcodedItem = new BarcodedItem(barcode, actualMass);
		static BarcodedProduct barcodedProduct = new BarcodedProduct(barcode, desc, price, expectedWeightGrams);
	}
	
	public static class PotatoChips {
		static Numeral[] barcodeDigits = {Numeral.seven, Numeral.three, Numeral.two, Numeral.nine};
		static double actualWeightGrams = 37;
		static Mass actualMass = new Mass(actualWeightGrams);
		static double expectedWeightGrams = 37;
		static String desc = "Large bag of potatoChips.";
		static long price = 4;
		static BigDecimal bdPrice = new BigDecimal((double)price);
		static Barcode barcode = new Barcode(barcodeDigits);
		static BarcodedItem barcodedItem = new BarcodedItem(barcode, actualMass);
		static BarcodedProduct barcodedProduct = new BarcodedProduct(barcode, desc, price, expectedWeightGrams);
	}
	
	public static class PeanutButter {
		static Numeral[] barcodeDigits = {Numeral.two, Numeral.two, Numeral.two, Numeral.six};
		static double actualWeightGrams = 289;
		static Mass actualMass = new Mass(actualWeightGrams);
		static double expectedWeightGrams = 289;
		static String desc = "Jar of peanut butter.";
		static long price = 6;
		static BigDecimal bdPrice = new BigDecimal((double)price);
		static Barcode barcode = new Barcode(barcodeDigits);
		static BarcodedItem barcodedItem = new BarcodedItem(barcode, actualMass);
		static BarcodedProduct barcodedProduct = new BarcodedProduct(barcode, desc, price, expectedWeightGrams);
	}
}
