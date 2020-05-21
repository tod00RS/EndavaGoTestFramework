package com.endava.appium.framework.util;

/**
 *	Custom class used to store more than just DEFAULT attributes of a mobile web elements
 */
public class CustomWebElement {

	private int leftX;
	private int rightX;
	private int upperY;
	private int lowerY;
	private int middleX;
	private int middleY;

	public CustomWebElement(int leftX, int rightX, int upperY, int lowerY) {
		this.leftX = leftX;
		this.rightX = rightX;
		this.upperY = upperY;
		this.lowerY = lowerY;
		this.middleX = (rightX + leftX) / 2;
		this.middleY = (lowerY + upperY) / 2;
	}

	@Override
	public String toString() {
		return "CustomWebElement [leftX=" + leftX + ", rightX=" + rightX + ", upperY=" + upperY + ", lowerY=" + lowerY
				+ ", middleX=" + middleX + ", middleY=" + middleY + "]";
	}

	public int getLeftX() {
		return leftX;
	}

	public int getRightX() {
		return rightX;
	}

	public int getUpperY() {
		return upperY;
	}

	public int getLowerY() {
		return lowerY;
	}

	public int getMiddleX() {
		return middleX;
	}

	public int getMiddleY() {
		return middleY;
	}

}
