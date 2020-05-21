package com.endava.appium.framework.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.testng.Assert;

import com.endava.appium.framework.util.CapabilitiesConstants;
import com.endava.appium.framework.util.CustomLogger;
import com.endava.appium.framework.util.CustomWebElement;
import com.endava.appium.framework.util.ExecutionTimer;
import com.endava.appium.framework.util.exceptions.ElementNotFoundException;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.NoSuchContextException;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.Connection;
import io.appium.java_client.ios.IOSDriver;

/**
 * HELPER CLASS FOR INTERACTING WITH THE DEVICE: - PERFORM ACTIONS (Clicks, Send
 * text ...) - APPLICATION MANAGEMENT (Minimize, maximize, change
 * orientation...) - DEVICE NETWORK STATUS INTERACTION
 */

public enum ActionsHelper {

	INSTANCE;
	private static final Logger LOG = CustomLogger.INSTANCE.getLogger(ActionsHelper.class);
	private WaitHelper waitHelper = WaitHelper.INSTANCE;
	private By androidNotificationsDrawer = By.id("com.android.systemui:id/notification_panel");
	private static final String NATIVE_APP = "NATIVE_APP";
	private static final String UI_SELECTOR_TEXT = "new UiSelector().text(\"";
	private static final String TO = " TO: ";
	private static final String DATA_TURNED_ON_MSG = "Data connection turned ON successfully";
	private static final String DATA_TURNED_OFF_MSG = "Data connection turned OFF successfully";

	private AdbHelper adbHelper = AdbHelper.INSTANCE;

	public void clickOn(By by) {
		waitHelper.waitForElementVisibility(by);
		getDriver().findElement(by).click();
		LOG.info("Clicked on element at " + by);
	}

	public void clickOn(WebElement elem) {
		waitHelper.waitForElementVisibility(elem);
		elem.click();
		LOG.info("Clicked element " + elem);
	}

	public void pressAndHold(By by) {
		WebElement elem = getElement(by);
		TouchAction touchAction = new TouchAction(getDriver());
		touchAction.press(elem).perform();
		LOG.info("Press and hold on " + by);
	}

	public void pressAndHold(int x, int y) {
		TouchAction touchAction = new TouchAction(getDriver());
		touchAction.press(x, y).perform();
	}

	public void longPress(By by) {
		WebElement elem = getElement(by);
		longPress(elem);
	}

	public void longPress(WebElement elem) {
		LOG.info("Longpressing on element " + elem);
		TouchAction touchAction = new TouchAction(getDriver());
		touchAction.longPress(elem).waitAction(100).perform();
	}

	public void release() {
		TouchAction touchAction = new TouchAction(getDriver());
		touchAction.press(10, 10).release();
		getDriver().performTouchAction(touchAction);
		LOG.info("Performed release action");
	}

	public void clickOnWithoutWait(WebElement elem) {
		elem.click();
		LOG.info("Clicked element " + elem + "without performing wait");
	}

	public void typeText(By by, String text) {
		clearThe(by);
		getDriver().findElement(by).sendKeys(text);
		LOG.info("Sent " + text + " to element at " + by);
	}

	public void typeText(WebElement elem, String text) {
		LOG.info(String.format("Sending %s to element %s", text, elem));
		clearThe(elem);
		elem.sendKeys(text);
		LOG.info(String.format("Sent text %s to element %s", text, elem));
	}

	public void typeTextWithAdb(By by, String text) {
		clearTheWithAdb(by);
		String trimmedText = text.replace(" ", "%s");
		LOG.info("Sending " + trimmedText + " to currently focused element");
		adbHelper.sendTextToDevice(trimmedText);
	}

	public void typeTextWithoutWait(WebElement elem, String text) {
		LOG.info("Sending " + text + " to element " + elem + " without wait");
		elem.sendKeys(text);
		LOG.info("Sent " + text + " to element " + elem);
	}

	public void clearThe(By by) {
		waitHelper.waitForElementVisibility(by);
		getDriver().findElement(by).clear();
		LOG.info("Cleared text content at " + by);
	}

	public void clearThe(WebElement elem) {
		waitHelper.waitForElementVisibility(elem);
		elem.clear();
		LOG.info("Cleared text content for element " + elem);
	}

	public void clearTheWithAdb(By by) {
		LOG.info("Trying to clear element " + by);
		clickOn(by);
		waitHelper.waitFor(1000);
		// select all text in textfield using CTRL+A
		selectAllText();
		waitHelper.waitFor(1000);
		// send backspace
		pressBackspace();
		LOG.info("Cleared text content at " + by);

	}

	public void sendKeyEvent(int keyEvent) {
		getAndroidDriver().pressKeyCode(keyEvent);
		LOG.info("Sent keyevent " + keyEvent);
	}

	public void sendKeyEvent(int keyEvent, int metastate) {
		getAndroidDriver().pressKeyCode(keyEvent, metastate);
		LOG.info(String.format("Sent keyevent %d with metastate %d", keyEvent, metastate));
	}

	public void hideKeyboard() {
		getDriver().hideKeyboard();
		LOG.info("Keyboard was hidden");
	}

	/**
	 * Generate xpath based on new value.
	 * 
	 * @param by
	 * @param token
	 * @param newValue
	 * @return
	 */
	public By replaceParamInBy(By by, String token, String newValue) {
		By newBy = null;
		if (by instanceof ByXPath) {
			newBy = By.xpath(by.toString().substring(10).replace(token, newValue));
		}
		if (by instanceof ById) {
			newBy = By.id(by.toString().substring(7).replace(token, newValue));
		}
		LOG.info(String.format("Replaced given by. OLD= %s NEW= %s", by, newBy));
		return newBy;
	}

	public void selectOptionFromDropdown(By by, String option) {
		waitHelper.waitForElementVisibility(by);
		Select select = new Select(getDriver().findElement(by));
		select.selectByValue(option);
		LOG.info(String.format("Selected option %s from select element at %s", option, by));
	}

	public Set<String> getContextHandles() {
		return getDriver().getContextHandles();
	}

	public void listAllAvailableContexts() {
		LOG.info("Available contexts are:");
		for (String s : getContextHandles()) {
			LOG.info(s);
		}
	}

	public String getCurrentContext() {
		return getDriver().getContext();
	}

	public void switchToContext(String context) {
		LOG.info("Trying to switch to WEBVIEW: " + context);
		if (getDriver().getContext().equalsIgnoreCase(context)) {
			LOG.info("Already on context " + context);
		} else {
			getDriver().getContextHandles();
			try {
				getDriver().context(context);
				LOG.info("Successfully switched to WEBVIEW: " + context);
			} catch (NoSuchContextException e) {
				LOG.error("Could not switch to context " + context);
				throw e;
			}
		}

	}

	public void switchToFirstAvailableWebview() {
		LOG.info("Trying to switch to first non NATIVE_APP view");
		boolean foundNonNativeContext = false;
		for (String s : getContextHandles()) {
			if ((!foundNonNativeContext) && !NATIVE_APP.equals(s)) {
				foundNonNativeContext = true;
				LOG.info("Found non native context: " + s);
				switchToContext(s);
			}
		}
		if (!foundNonNativeContext) {
			LOG.info("ONLY NATIVE_APP CONTEXT AVAILABLE");
		}
	}

	public void switchToNativeContext() {
		LOG.info("Trying to switch to NATIVE_APP");
		if (getDriver().getContext().equalsIgnoreCase(NATIVE_APP)) {
			LOG.info("Already on NATIVE_APP");
		} else {
			getDriver().getContextHandles();
			try {
				getDriver().context(NATIVE_APP);
				LOG.info("Successfully switched to NATIVE_APP");
			} catch (NoSuchContextException e) {
				LOG.error("Could not switch to NATIVE_APP");
				throw e;
			}
		}

	}

	/**
	 * Sets the 'value' attribute of a WebElem to the desired content
	 * 
	 * @param javascript
	 *            Js to find the wanted element. Eg:
	 *            document.findElementById('id')
	 * @param text
	 *            the desired text that you want the WebElement attribute
	 *            'value' to be set to
	 */
	public void setValueToElementWithJs(String javascript, String text) {
		((JavascriptExecutor) getDriver()).executeScript(javascript + ".setAttribute('value','" + text + "')");
	}

	/**
	 * Sets the 'value' attribute of a WebElem found by elementId to the desired
	 * content
	 * 
	 * @param elementId
	 * @param text
	 */
	public void setValueToElement(String elementId, String text) {
		((JavascriptExecutor) getDriver())
				.executeScript("document.getElementById('" + elementId + "').setAttribute('value','" + text + "')");
	}

	public List<WebElement> getElements(By by) {
		// wait is NOT needed
		// cannot wait for the first element in an unknown list
		// simply return the list of all elements that have the given By
		return getDriver().findElements(by);
	}

//	public void waitForSpecificTextToAppearInTheField(){
//		LOG.info("Waiting for specific text in the field to appear " + by);
//		wait.until(ExpectedConditions.textToBePresentInElement(webElement, “textToFind”));
//		waitHelper.waitForElementToContainText();
//		waitHelper.
//	}

	public String getTextOfElement(By by) {
		LOG.info("Getting text from element locate at " + by);
		waitHelper.waitForElementVisibility(by);
		return getDriver().findElement(by).getText();
	}

	public List<String> getTextOfElements(By by) {
		LOG.info("Getting text from all elements that have matching " + by);
		List<String> elementsText = new ArrayList<>();
		List<WebElement> list = getElements(by);
		for (WebElement w : list) {
			elementsText.add(getTextOfElement(w));
		}
		return elementsText;
	}

	public String getAttributeOfElement(By by, String attribute) {
		WebElement elem = getElement(by);
		return getAttributeOfElement(elem, attribute);
	}

	public String getAttributeOfElement(WebElement elem, String attribute) {
		LOG.info(String.format("Getting attribute %s from element %s", attribute, elem));
		return elem.getAttribute(attribute);
	}

	public String getTextOfElement(WebElement elem) {
		LOG.info("Getting text from element " + elem);
		waitHelper.waitForElementVisibility(elem);
		return elem.getText();
	}

	public String getResourceIdOfElement(By by) {
		LOG.info("Getting resource-id of element located at " + by);
		waitHelper.waitForElementVisibility(by);
		return getDriver().findElement(by).getAttribute("resourceId");
	}

	public String getResourceIdOfElement(WebElement elem) {
		LOG.info("Getting resource-id of element " + elem);
		waitHelper.waitForElementVisibility(elem);
		return elem.getAttribute("resourceId");
	}

	public String getTextOfElementWithoutWait(WebElement elem) {
		LOG.info("Getting text from element " + elem + " without wait");
		waitHelper.waitForElementVisibility(elem);
		return elem.getText();
	}

	public MobileElement getMobileElem(By by) {
		LOG.info("Retrieving mobile element located at " + by);
		waitHelper.waitForElementVisibility(by);
		return (MobileElement) getDriver().findElement(by);
	}

	public WebElement getElement(By by) {
		LOG.info("Retrieving element located at " + by);
		waitHelper.waitForElementVisibility(by);
		return getDriver().findElement(by);
	}

	public WebElement getChildOfElement(WebElement parent, By by) {
		LOG.info("Looking for elements identified by " + by + " within the children of " + parent);
		return parent.findElement(by);
	}

	public WebElement getElementWithUiAutomatorText(String text) {
		LOG.info("Looking for element with exact text: " + text);
		int numberOfTries = 0;
		LOG.info("Getting list");
		List<WebElement> list = getElementsWithUiAutomatorText(text);
		while (list.size() != 1) {
			if (list.size() > 1) {
				throw new ElementNotFoundException("More than one element with given identifier was found. Please refine your search criteria.");
			} else {
				if (numberOfTries <= 3) {
					LOG.info("Element not found yet");
					waitHelper.simpleWait();
					numberOfTries++;
					list = getElementsWithUiAutomatorText(text);
				} else {
					throw new ElementNotFoundException("Element not found after 3 attempts");
				}
			}

		}
		return getAndroidDriver().findElementByAndroidUIAutomator(UI_SELECTOR_TEXT + text + "\")");
	}

	public List<WebElement> getElementsWithUiAutomatorText(String text) {
		return getAndroidDriver().findElementsByAndroidUIAutomator(UI_SELECTOR_TEXT + text + "\")");
	}

	public WebElement getWelementWithUiAutomatorTextContains(String text) {
		LOG.info("Looking for element that contains the text: " + text);
		return getAndroidDriver().findElementByAndroidUIAutomator("new UiSelector().textContains(\"" + text + "\")");
	}

	/**
	 * Swipe from given start coordinates to given end coordinates.
	 * 
	 * @param startFromX
	 * @param startFromY
	 * @param endAtX
	 * @param endAtY
	 * @param duration
	 */
	public void swipe(int startFromX, int startFromY, int endAtX, int endAtY, int duration) {
		LOG.info("Swipe action FROM: " + startFromX + " " + startFromY + TO + endAtX + " " + endAtY);
		TouchAction touchAction = new TouchAction(getDriver());
		// appium converts press-wait-moveto-release to a swipe action
		touchAction.press(startFromX, startFromY).waitAction(duration).moveTo(endAtX, endAtY).release();
		touchAction.perform();
	}

	/**
	 * Swipe from given start coordinates to given end coordinates.
	 * 
	 * @param startFromX
	 * @param startFromY
	 * @param endAtX
	 * @param endAtY
	 * @param duration
	 */
	public void swipeAndHold(int startFromX, int startFromY, int endAtX, int endAtY, int duration) {
		LOG.info("Swipe and hold action FROM: " + startFromX + " " + startFromY + TO + endAtX + " " + endAtY);
		TouchAction touchAction = new TouchAction(getDriver());
		// appium converts press-wait-moveto-release to a swipe action
		touchAction.press(startFromX, startFromY).waitAction(duration).moveTo(endAtX - startFromX, endAtY - startFromY);
		touchAction.perform();

	}

	/**
	 * Swipe from one element to another. Perform swipe from center of from
	 * element to center of to element.
	 * 
	 * @param from
	 * @param to
	 * @param duration
	 */
	public void swipe(WebElement from, WebElement to, int duration) {
		TouchAction touchAction = new TouchAction(getDriver());
		int startFromX;
		int startFromY;
		int endAtX;
		int endAtY;

		// compute middle x
		startFromX = from.getLocation().getX() + from.getSize().getWidth() / 2;
		// compute middle y
		startFromY = from.getLocation().getY() + from.getSize().getHeight() / 2;
		// compute middle x
		endAtX = to.getLocation().getX() + to.getSize().getWidth() / 2;
		// compute middle y
		endAtY = to.getLocation().getY() + to.getSize().getHeight() / 2;

		// appium converts press-wait-move-to-release to a swipe action
		touchAction.press(startFromX, startFromY).waitAction(duration).moveTo(endAtX, endAtY).release();
		touchAction.perform();
		LOG.info("Swipe action FROM: " + startFromX + " " + startFromY + TO + endAtX + " " + endAtY);
	}

	public void tapAtCoords(int x, int y) {
		LOG.info("Tap action at coords " + x + " " + y);
		getDriver().tap(1, x, y, 200);
	}

	public void clickAtCoords(int x, int y) {
		LOG.info("CLICK at coordinates " + x + " " + y);
//		waitHelper.waitForElementToContainText(uiObject.elementName, waitedText );  //in case we need to wait, we can
//		wait for the part of text of some other element to appear, and then click at given coordinates. In that case
//		we have to add 2 more parameters to the clickAtCoords method: By uiObject.elementName  &   String waitedText !!!!
		TouchAction action= new TouchAction(getDriver());
		action.press(x, y).release().perform();
	}

	public void rotateToLandscape() {
		LOG.info("Rotating screen to landscape mode");
		ExecutionTimer.start();
		getDriver().rotate(ScreenOrientation.LANDSCAPE);
		ExecutionTimer.stop();
		LOG.info("Rotate command duration " + ExecutionTimer.duration());
	}

	public void rotateToPortrait() {
		LOG.info("Rotating screen to portrait mode");
		ExecutionTimer.start();
		getDriver().rotate(ScreenOrientation.PORTRAIT);
		ExecutionTimer.stop();
		LOG.info("Rotate command duration " + ExecutionTimer.duration() + " ms");
	}

	public boolean isElementPresent(By by) {
		boolean isPresent = false;
		if (!getElements(by).isEmpty()) {
			isPresent = true;
		} else {
			LOG.warn("Element " + by + " was not present");
		}
		return isPresent;
	}

	public boolean isElementChecked(By by) {
		WebElement elem = getElement(by);
		return isElementChecked(elem);
	}

	public boolean isElementChecked(WebElement elem) {
		boolean result = Boolean.parseBoolean(elem.getAttribute("checked"));
		LOG.info("Checked if web element " + elem + " is checked. Result= " + result);
		return result;
	}

	public boolean isElementEnabled(By by) {
		WebElement elem = getElement(by);
		return isElementEnabled(elem);
	}

	public boolean isElementEnabled(WebElement elem) {
		boolean result = Boolean.parseBoolean(elem.getAttribute("enabled"));
		LOG.info("Checked if web element " + elem + " is enabled. Result= " + result);
		return result;
	}

	public boolean isLandscapeMode() {
		LOG.info("Verifying if screen in in LANDSCAPE MODE");
		return getDriver().getOrientation().equals(ScreenOrientation.LANDSCAPE);
	}

	public boolean isPortraitMode() {
		LOG.info("Verifying if screen in in PORTRAIT MODE");
		return getDriver().getOrientation().equals(ScreenOrientation.PORTRAIT);
	}

	public CustomWebElement getCustomWebElem(By by) {
		WebElement elem = getElement(by);
		int leftX = elem.getLocation().getX();
		int rightX = leftX + elem.getSize().getWidth();
		int upperY = elem.getLocation().getY();
		int lowerY = upperY + elem.getSize().getHeight();
		CustomWebElement customWebElement = new CustomWebElement(leftX, rightX, upperY, lowerY);
		LOG.info("Created " + customWebElement.toString() + " from element found at " + by);
		return customWebElement;
	}

	public CustomWebElement getCustomWebElement(WebElement elem) {
		int leftX = elem.getLocation().getX();
		int rightX = leftX + elem.getSize().getWidth();
		int upperY = elem.getLocation().getY();
		int lowerY = upperY + elem.getSize().getHeight();
		CustomWebElement customWebElement = new CustomWebElement(leftX, rightX, upperY, lowerY);
		LOG.info("Created " + customWebElement.toString() + " from element " + elem);
		return customWebElement;
	}

	public void minimizeApplication() {
		LOG.info("Sending app to background");
		ExecutionTimer.start();
		sendKeyEvent(3);
		ExecutionTimer.stop();
		LOG.info("Send app to background executed in " + ExecutionTimer.duration() + " ms");
	}

	public void maximizeApplication(String packageName, String activityName) {
		LOG.info("Bringing app in foreground");
		ExecutionTimer.start();
		adbHelper.bringAppInForeground(packageName, activityName);
		ExecutionTimer.stop();
		LOG.info("Bring app to foreground executed in " + ExecutionTimer.duration() + " ms");
	}

	/**
	 * nagivate back using Android back button
	 */
	public void navigateBack() {
		LOG.info("Pressing back button");
		sendKeyEvent(4);
	}

	public void openNotificationDrawer() {
		LOG.info("Opening notifications drawer");
		if (isElementPresent(androidNotificationsDrawer)) {
			LOG.warn("Notifications drawer already opened");
		} else {
			getAndroidDriver().openNotifications();
		}
		LOG.info("Opened notifications drawer");
	}

	public void closeNotificationDrawer() {
		LOG.info("Closing notifications drawer");
		if (isElementPresent(androidNotificationsDrawer)) {
			navigateBack();
			LOG.info("Closed notifications drawer");
		} else {
			LOG.warn("Could not close notifications drawer. Reason: Notifications drawer not open");
		}
	}

	public void pressEnter() {
		LOG.info("Pressing ENTER key");
		sendKeyEvent(66);
	}

	public void pressBackspace() {
		LOG.info("Pressing BACKSPACE");
		sendKeyEvent(67);
	}

	public void selectAllText() {
		LOG.info("Pressing CTRL+A");
		sendKeyEvent(29, 28672);
	}

	public void copyText() {
		LOG.info("Pressing CTRL+C");
		sendKeyEvent(31, 28672);
	}

	public void pasteText() {
		LOG.info("Pressing CTRL+V");
		sendKeyEvent(50, 28672);
	}

	/**
	 * Scrolls and returns a WebElement that contains the given text
	 * 
	 * @param text
	 * @return
	 */
	public MobileElement scrollToElementWithText(String text) {
		LOG.info("Starting to look for element that contains the text " + text);
		ExecutionTimer.start();
		String uiScrollables = uiScrollable("new UiSelector().textContains(\"" + text + "\")");
		return getElementUsingUiAutomatorSelector(uiScrollables);
	}

	/**
	 * Scrolls and returns a WebElement that has the exact the given text
	 * 
	 * @param text
	 * @return
	 */
	public MobileElement scrollToElementWithExactText(String text) {
		LOG.info("Starting to look for element with the exact text " + text);
		String uiScrollables = uiScrollable(UI_SELECTOR_TEXT + text + "\")");
		return getElementUsingUiAutomatorSelector(uiScrollables);
	}

	public void refreshApplicationPage() {
		LOG.info("Refreshing the app");
		getDriver().runAppInBackground(1);
	}

	/**
	 * Unfortunately, at the moment Appium does not support the Selenium network
	 * connection API for iOS.
	 */
	public void turnOnAirplaneModeWithAdb() {
		LOG.info("Turning on airplane mode using ADB");
		adbHelper.setAirplaneMode(true);
		LOG.info("Airplane mode turned ON successfully");
	}

	public void turnOffAirplaneModeWithAdb() {
		LOG.info("Turning off airplane mode using ADB");
		adbHelper.setAirplaneMode(false);
		LOG.info("Airplane mode turned OFF successfully");
	}

	public void turnOnAirplaneMode() {
		LOG.info("Turning on airplane mode using Appium");
		getAndroidDriver().setConnection(Connection.AIRPLANE);
		Assert.assertEquals(Connection.AIRPLANE, getAndroidDriver().getConnection());
		LOG.info("Airplane mode turned ON successfully");
	}

	public void turnOffAirplaneMode() {
		LOG.info("Turning off airplane mode using Appium");
		getAndroidDriver().setConnection(Connection.ALL);
		Assert.assertEquals(Connection.ALL, getAndroidDriver().getConnection());
		LOG.info("Airplane mode turned OFF successfully");
	}

	public void turnOffWifiWithAdb() {
		LOG.info("Turning off wifi connection using ADB");
		adbHelper.setWifiModeWithAppium(false);
		LOG.info("Wifi connection turned OFF successfully");
	}

	public void turnOnWifiWithAdb() {
		LOG.info("Turning on wifi connection using ADB");
		adbHelper.setWifiModeWithAppium(true);
		LOG.info("Wifi connection turned ON successfully");
	}

	public void turnOnWifi() {
		LOG.info("Turning on wifi connection using Appium");
		getAndroidDriver().setConnection(Connection.WIFI);
		Assert.assertEquals(Connection.WIFI, getAndroidDriver().getConnection());
		LOG.info("Wifi connection turned ON successfully");
	}

	public void turnOnDataWithAdb() {
		LOG.info("Turning on data connection using ADB");
		adbHelper.setDataModeWithAppium(true);
		LOG.info(DATA_TURNED_ON_MSG);
	}

	public void turnOffDataWithAdb() {
		LOG.info("Turning off data connection using ADB");
		adbHelper.setDataModeWithAppium(false);
		LOG.info(DATA_TURNED_OFF_MSG);
	}

	public void turnOnData() {
		LOG.info("Turning on data connection using Appium");
		getAndroidDriver().setConnection(Connection.DATA);
		Assert.assertEquals(Connection.DATA, getAndroidDriver().getConnection());
		LOG.info(DATA_TURNED_ON_MSG);
	}

	public void turnOffAllConnection() {
		LOG.info("Turning off ALL connection using Appium");
		getAndroidDriver().setConnection(Connection.NONE);
		Assert.assertEquals(Connection.NONE, getAndroidDriver().getConnection());
		LOG.info("ALL connections turned OFF successfully");
	}

	public void turnOnAllConnection() {
		LOG.info("Turning on ALL connection using Appium");
		getAndroidDriver().setConnection(Connection.ALL);
		Assert.assertEquals(Connection.ALL, getAndroidDriver().getConnection());
		LOG.info("ALL conncetions turned ON successfully");
	}

	public void restartApplication() {
		adbHelper.restartApp((String) ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_APP_PACKAGE),
				(String) ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_APP_ACTIVITY));
	}

	static String uiScrollable(String uiSelector) {
		return "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(" + uiSelector
				+ ".instance(0));";
	}

	MobileElement getElementUsingUiAutomatorSelector(String uiAutomatorSelector) {
		MobileElement result = null;
		ExecutionTimer.start();
		try {
			result = (MobileElement) getAndroidDriver().findElementByAndroidUIAutomator(uiAutomatorSelector);
		} catch (NoSuchElementException e) {
			LOG.error(e.getLocalizedMessage());
			ExecutionTimer.stop();
			LOG.error("Could not find element after " + ExecutionTimer.duration() + " ms");
			throw e;
		}
		ExecutionTimer.stop();
		LOG.info("Found element after " + ExecutionTimer.duration() + " ms");
		return result;
	}

	AppiumDriver<WebElement> getDriver() {
		return DriverHelper.getDriver();
	}

	AndroidDriver<WebElement> getAndroidDriver() {
		return DriverHelper.getAndroidDriver();
	}

	IOSDriver<WebElement> getIosDriver() {
		return DriverHelper.getIosDriver();
	}
}
