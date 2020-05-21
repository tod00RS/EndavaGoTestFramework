package com.endava.appium.framework.screens;

import com.endava.appium.framework.helpers.DriverHelper;
import com.endava.appium.framework.objects.EndavaGoUiObjects;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;

public class OwnerHomeScreen extends BaseScreen {

    private static final String APP_PACKAGE = "com.endava.rsbgd.app.endavago";


    private EndavaGoUiObjects uiObject = new EndavaGoUiObjects();


    public void searchForSpot(int orderNumberOfTheDay) {
        actionsHelper.clickOn(uiObject.ownerSearchReleaseButton(orderNumberOfTheDay));
        waitHelper.waitFor(5000);
    }

    public boolean isSearchReleaseButtonPresent(int orderNumberOfTheDay){
        try {
            log.info("Checking if Search/Release parking spot button is present...");
//            waitHelper.waitForElementVisibility(uiObject.ownerSearchReleaseButton(orderNumberOfTheDay));
            return actionsHelper.isElementPresent(uiObject.ownerSearchReleaseButton(orderNumberOfTheDay));
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't find Search/Release parking spot button, element absent or blocked.");
        }
    }

    public boolean isParkingSpotAvailableToRelease(int orderNumberOfTheDay) {
        try {
            log.info("Checking if Parking spot with order number of the day: "+ orderNumberOfTheDay + ", is available for release...");
            return actionsHelper.getTextOfElement(uiObject.ownerSearchReleaseButton(orderNumberOfTheDay)).equalsIgnoreCase("Release");
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't find Search/Release parking spot button, element absent or blocked.");
        }
    }

    public String getTextFromOwnerParkingSpot(int orderNumberOfTheDay) {
        try {
            log.info("Getting text from owner Parking spot...");
            return actionsHelper.getTextOfElement(uiObject.ownerSearchReleaseButton(orderNumberOfTheDay));
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't find Search/Release parking spot button, element absent or blocked.");
        }
    }

    public void waitForTextFromOwnerParkingSpot(int orderNumberOfTheDay, String textToBeWaited) {
        try {
            log.info("Waiting: " + textToBeWaited + " text from owner Parking spot to appear...");
            waitHelper.waitForElementToContainText(uiObject.ownerSearchReleaseButton(orderNumberOfTheDay), textToBeWaited);
        } catch (Exception e) {
//            throw new AssertionError("Can't find " + textToBeWaited + ", element absent or blocked.");
        }
    }

    public void clickOnReleaseOrSearchParkingSpot(int orderNumberOfTheDay) {
        try {
            log.info("Releasing as Owner parking spot...");
            actionsHelper.clickOn(uiObject.ownerSearchReleaseButton(orderNumberOfTheDay));
            if(actionsHelper.isElementPresent(uiObject.confirmationMessageText())) actionsHelper.clickOn(uiObject.releaseConfirmationButton());
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't find Search/Release parking spot button, element absent or blocked.");
        }
    }

    public String getOwnerParkingSpotStatusInfo(int orderNumberOfTheDay){
        try {
            log.info("Getting status of owner parking spot...");
            return actionsHelper.getTextOfElement(uiObject.ownerParkingSpotStatusInfo(orderNumberOfTheDay));
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't get parking spot status info button, element absent or blocked.");
        }
    }

    public void logOut() {
        try {
            log.info("Logging out from Owner profile...");
            actionsHelper.clickOn(uiObject.ownerProfileButton());
            actionsHelper.clickOn(uiObject.signOutFromProfileScreenButton());
            actionsHelper.pressBackspace();
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't logout from Owner profile.");
        }
    }

    public void swipeForScreenDown(int startFromX, int startFromY, int endAtX, int endAtY, int duration){
        actionsHelper.swipe(startFromX, startFromY, endAtX, endAtY, duration);
        waitHelper.waitFor(5000);
    }

    public void swipeFromElementToElement(int ordinalNumberOfTheFromDay, int ordinalNumberOfTheToDay){
        WebElement fromElement =  DriverHelper.getDriver().findElement(MobileBy.xpath("(//android.widget.Button[@resource-id='" + APP_PACKAGE + ":id/pso_button'])[" + ordinalNumberOfTheFromDay + "]"));
        WebElement toElement =  DriverHelper.getDriver().findElement(MobileBy.xpath("(//android.widget.Button[@resource-id='" + APP_PACKAGE + ":id/pso_button'])[" + ordinalNumberOfTheToDay + "]"));
        actionsHelper.swipe(fromElement, toElement, 2000);
    }


}
