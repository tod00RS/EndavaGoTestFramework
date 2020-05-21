package com.endava.appium.framework.helpers;

import static org.testng.Assert.*;
import io.appium.java_client.AppiumDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;

import com.endava.appium.framework.util.CustomLogger;

/**
 * HELPER CLASS THAT IS USED TO VERIFY/ASSERT TEST REQUIREMENTS
 * THIS CLASS IS TOTALLY OPTIONAL, CAN BE USED IN CASE YOU WANT FASTER/EASIER TO READ IN LOGS ASSERTS
 */
public enum VerifyHelper {

	INSTANCE;

	private static final Logger LOG = CustomLogger.INSTANCE.getLogger(VerifyHelper.class);
	private ActionsHelper actionsHelper = ActionsHelper.INSTANCE;
	private static final String VERIFY = "Verifying if element ";
	private static final String ELEMENT = "Element ";

	public void isElementPresent(By by) {
		LOG.info(VERIFY + by + " is present");
		boolean result = (getDriver().findElements(by).size() > 0) ? true : false;
		assertTrue(result, ELEMENT + by + " was not present");
	}

	public void isElementNotPresent(By by) {
		LOG.info(VERIFY + by + " is not present");
		boolean result = (getDriver().findElements(by).size() == 0) ? true : false;
		assertTrue(result, ELEMENT + by + " was present");
	}

	public void isElementDisplayed(By by) {
		LOG.info(VERIFY + by + " is displayed");
		Boolean result = null;
		try {
			result = actionsHelper.getElement(by).isDisplayed();
		} catch (TimeoutException e) {
			LOG.error(e.getMessage(), e);
			result = false;
		}
		assertTrue(result, ELEMENT + by + " was not displayed");
	}

	public void isElementNotDisplayed(By by) {
		LOG.info(VERIFY + by + " is not displayed");
		boolean result = actionsHelper.isElementPresent(by);
		// if an element is NOT PRESENT => it is also not displayed
		if (result) {
			result = getDriver().findElement(by).isDisplayed();
		}
		assertTrue(!result, ELEMENT + by + " was displayed");
	}

	public void isElementEnabled(By by) {
		LOG.info(VERIFY + by + " is enabled");
		Boolean result = null;
		try {
			result = actionsHelper.getElement(by).isEnabled();
		} catch (TimeoutException e) {
			LOG.error(e.getMessage(), e);
			result = false;
		}
		assertTrue(result, ELEMENT + by + " was not enabled");
	}

	public void isElementNotEnabled(By by) {
		LOG.info(VERIFY + by + " is not enabled");
		boolean result = actionsHelper.isElementPresent(by);
		// if an element is NOT PRESENT => it is also not enabled
		if (result) {
			result = getDriver().findElement(by).isEnabled();
		}
		assertTrue(!result, ELEMENT + by + " was enabled");
	}

	public void isElementSelected(By by) {
		LOG.info(VERIFY + by + " is selected");
		Boolean result = null;
		try {
			result = actionsHelper.getElement(by).isSelected();
		} catch (TimeoutException e) {
			LOG.error(e.getMessage(), e);
			result = false;
		}
		assertTrue(result, ELEMENT + by + " was not selected");
	}

	public void isElementNotSelected(By by) {
		LOG.info(VERIFY + by + " is not selected");
		boolean result = actionsHelper.isElementPresent(by);
		// if an element is NOT PRESENT => it is also not selected
		if (result) {
			result = getDriver().findElement(by).isSelected();
		}
		assertTrue(!result, ELEMENT + by + " was selected");
	}

	private AppiumDriver<WebElement> getDriver() {
		return DriverHelper.getDriver();
	}

}
