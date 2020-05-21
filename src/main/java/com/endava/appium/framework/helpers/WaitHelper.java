package com.endava.appium.framework.helpers;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;

import com.endava.appium.framework.util.CustomLogger;
import com.endava.appium.framework.util.ExecutionTimer;
import com.endava.appium.framework.util.exceptions.WaitException;

import io.appium.java_client.AppiumDriver;

/**
 * HELPER CLASS CONTAINING DYNAMIC WAITS
 * MAINLY USED BY THE ACTIONSHELPER CLASS AS A PRE-STEP WHEN DOING ACTIONS
 * CAN BE USED AS A WAIT MECHANISM IN TESTS
 */
public enum WaitHelper {

	INSTANCE;

	// set default timeout baesd on -Dtimeout param
	// if -Dtimeout not set => default timeout = 30 seconds
	public static final int DEFAULT_TIMEOUT = (System.getProperty("timeout") == null) ? 30
			: Integer.parseInt(System.getProperty("timeout"));
	private static final Logger LOG = CustomLogger.INSTANCE.getLogger(WaitHelper.class);

	/**
	 * Default wait method, called by all other waits
	 * Can be used to implement new custom wait methods in your project
	 * 
	 * @param condition
	 * @param timeout
	 */
	public void waitForElement(ExpectedCondition<?> condition, int timeout) {
		try {
			newWait(DriverHelper.getDriver(), timeout).until(condition);
		} catch (TimeoutException e) {
			LOG.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Waits the given timeout for the given by to be visible
	 * 
	 * @param by
	 * @param timeout
	 */

	public void waitForElementVisibility(By by, int timeout) {
		LOG.info(String.format("Waiting for element at by %s with timeout=%d", by, timeout));
		waitForElement(ExpectedConditions.visibilityOfElementLocated(by), timeout);

	}

	public void waitForElementVisibility(WebElement elem, int timeout) {
		LOG.info(String.format("Waiting for element %s with timeout=%d", elem, timeout));
		waitForElement(ExpectedConditions.visibilityOf(elem), timeout);

	}
	
	/**
	 * Waits the given timeout for the given by to be clickable
	 * @param by
	 * @param timeout
	 */
	public void waitForElementToBeClickable(By by, int timeout) {
		LOG.info(String.format("Waiting for element at by %s with timeout=%d to be clickable", by, timeout));
		waitForElement(ExpectedConditions.elementToBeClickable(by), timeout);
	}
	
	public void waitForElementToBeClickable(WebElement elem, int timeout) {
		LOG.info(String.format("Waiting for element %s with timeout=%d to be clickable", elem, timeout));
		waitForElement(ExpectedConditions.elementToBeClickable(elem), timeout);
	}

	/**
	 * Waits the given timeout for the given by to be invisible
	 * 
	 * @param by
	 * @param timeout
	 */
	public void waitForElementInvisibility(By by, int timeout) {
		LOG.info("Waiting for invisiblity of " + by);
		waitForElement(ExpectedConditions.invisibilityOfElementLocated(by), timeout);
	}

	/*
	 * DEFAULT WAITS SECTION, ONLY USE DEFAULT_TIMEOUT
	 */

	/**
	 * Waits for both javascript and jQuerry to completely load on the page
	 */
	public void waitForJavascriptAndJQuerryToLoad() {
		LOG.info("Waiting for webpage to be completely loaded");
		try {
			waitForJavascriptToLoad();
			waitForJQuerryToLoad();
		} catch (TimeoutException e) {
			LOG.error("Timed out while waiting for webpage to load. Javascript/jQuerry did not reach a COMPLETE load state.");
			throw new WaitException(e);
		}
		LOG.info("Finished loading webpage");
	}

	/**
	 * Waits for document.readyState() to evaluate to "complete"
	 */
	public void waitForJavascriptToLoad() {
		LOG.info("waiting for Javascript to load");
		newWait(getDriver(), DEFAULT_TIMEOUT).until(javascriptFinishedLoading());
		LOG.info("Javascript finsihed loading");
	}
	
	/**
	 * Waits for jQuerry.active() to evaluate to "0"
	 */
	public void waitForJQuerryToLoad() {
		LOG.info("waiting for jQuerry to load");
		newWait(getDriver(), DEFAULT_TIMEOUT).until(jQuerryFinishedLoading());
		LOG.info("jQuerry finished loading");
	}

	public void waitForElementVisibility(By by) {
		ExecutionTimer.start();
		waitForElementVisibility(by, DEFAULT_TIMEOUT);
		ExecutionTimer.stop();
		LOG.info(String.format("Found element at %s after %s ms", by, ExecutionTimer.duration()));
	}

	public void waitForElementVisibility(WebElement elem) {
		LOG.info("Waiting for element " + elem);
		ExecutionTimer.start();
		waitForElement(ExpectedConditions.visibilityOf(elem), DEFAULT_TIMEOUT);
		ExecutionTimer.stop();
		LOG.info(String.format("Found element %s after %s ms", elem, ExecutionTimer.duration()));
	}

	public void waitForElementInvisibility(By by) {
		ExecutionTimer.start();
		waitForElementInvisibility(by, DEFAULT_TIMEOUT);
		ExecutionTimer.stop();
		LOG.info(String.format("Element at by %s no longer visible after %s ms", by, ExecutionTimer.duration()));
	}

	public void waitForElementToDisappear(By by) {
		waitForElementToDisappear(by, DEFAULT_TIMEOUT);
	}

	public void waitForElementToDisappear(By by, int timeOut) {
		LOG.info(String.format("Waiting for element at by %s to disappear with timeout=%d seconds", by, timeOut));
		ExecutionTimer.start();
		waitForElement(elementCountIs(by, 0), timeOut);
		ExecutionTimer.stop();
		LOG.info(String.format("Element at by %s dissappeared after %s ms", by, ExecutionTimer.duration()));

	}

	public void waitForElementToDisappear(WebElement elem) {
		LOG.info(String.format("Waiting for element %s to disappear", elem));
		ExecutionTimer.start();
		waitForElement(elementIsNotDisplayed(elem), DEFAULT_TIMEOUT);
		ExecutionTimer.stop();
		LOG.info(String.format("Element %s disappeared after %s ms", elem, ExecutionTimer.duration()));

	}

	public void waitForChildElementNotPresent(WebElement elem, By by) {
		LOG.info(String.format("Waiting for child with By %s of element %s to not be present", by, elem));
		ExecutionTimer.start();
		waitForElement(childElementCount(by, elem, 0), DEFAULT_TIMEOUT);
		ExecutionTimer.stop();
		LOG.info(String.format("Child with By %s of element %s dissappeared after %s seconds", by, elem,
				ExecutionTimer.duration()));
	}

	public void waitForElementWithText(By by, String text) {
		LOG.info(String.format("Waiting for element at %s with text: %s and with timeout=%d seconds", by, text,
				DEFAULT_TIMEOUT));
		waitForElement(elementHasText(by, text), DEFAULT_TIMEOUT);
		LOG.info(String.format("Found element with text %s using %s", text, by));
	}

	public void waitForElementToContainText(By by, String text) {
		LOG.info(String.format("Waiting for element at %s with text: %s and with timeout=%d seconds", by, text,
				DEFAULT_TIMEOUT));
		waitForElement(elementContainsText(by, text), DEFAULT_TIMEOUT);
		LOG.info(String.format("Found element with text %s using %s", text, by));
	}

	public void simpleWait() {
		waitFor(500);
	}

	public void waitFor(int intervalMs) {
		try {
			LOG.info(String.format("Waiting for %d ms", intervalMs));
			Thread.sleep(intervalMs);
		} catch (InterruptedException e) {
			LOG.error(e.getLocalizedMessage(), e);
			Thread.currentThread().interrupt();
		}
	}

	/*
	 * HELPER METHOD SECTIONS
	 */

	private Wait<WebDriver> newWait(WebDriver driver, int timeout) {
		return new FluentWait<WebDriver>(driver).withTimeout(timeout, TimeUnit.SECONDS)
				.pollingEvery(500, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);
	}

	/**
	 * Expected condition that evaluates to true if the number of elements on
	 * the page == count
	 * 
	 * @param sel
	 * @param count
	 * @return
	 */

	private ExpectedCondition<Boolean> elementCountIs(final By sel, final int count) {
		return new ExpectedCondition<Boolean>() {
			public Boolean apply(final WebDriver driver) {
				return driver.findElements(sel).size() == count;
			}
		};
	}

	private ExpectedCondition<Boolean> childElementCount(final By by, final WebElement el, final int count) {
		return new ExpectedCondition<Boolean>() {
			public Boolean apply(final WebDriver driver) {
				return el.findElements(by).size() == count;
			}
		};
	}

	/**
	 * Expected condition that evaluates to true if the given element has the
	 * given text
	 * 
	 * @param sel
	 * @param text
	 * @return
	 */

	private ExpectedCondition<Boolean> elementHasText(final By sel, final String text) {
		return new ExpectedCondition<Boolean>() {
			public Boolean apply(final WebDriver driver) {
				return driver.findElement(sel).getText().trim().equals(text);
			}
		};
	}

	/**
	 * Expected condition that evaluates to true if the given element contains
	 * the given text
	 * 
	 * @param sel
	 * @param text
	 * @return
	 */

	private ExpectedCondition<Boolean> elementContainsText(final By sel, final String text) {
		return new ExpectedCondition<Boolean>() {
			public Boolean apply(final WebDriver driver) {
				return driver.findElement(sel).getText().trim().contains(text);
			}
		};
	}

	/**
	 * Expected condition that evaluates to true if the give element is not
	 * displayed
	 * 
	 * @param elem
	 * @return
	 */
	private ExpectedCondition<Boolean> elementIsNotDisplayed(final WebElement elem) {
		return new ExpectedCondition<Boolean>() {
			public Boolean apply(final WebDriver driver) {
				return !elem.isDisplayed();
			}
		};
	}
	
	private ExpectedCondition<Boolean> javascriptFinishedLoading() {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return "complete".equals(
						((JavascriptExecutor) getDriver()).executeScript("return document.readyState").toString());
			}
		};
	}

	private ExpectedCondition<Boolean> jQuerryFinishedLoading() {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					return (Long) ((JavascriptExecutor) getDriver()).executeScript("return jQuery.active") == 0;
				} catch (WebDriverException e) {
					LOG.warn(e.getMessage(), e);
					return true;
				}
			}
		};
	}

	private AppiumDriver<WebElement> getDriver() {
		return DriverHelper.getDriver();
	}
}
