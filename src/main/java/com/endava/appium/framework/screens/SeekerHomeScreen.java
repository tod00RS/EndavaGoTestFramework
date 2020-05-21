package com.endava.appium.framework.screens;

import com.endava.appium.framework.objects.EndavaGoUiObjects;
import org.openqa.selenium.TimeoutException;

import java.util.NoSuchElementException;

public class SeekerHomeScreen extends BaseScreen {

    public EndavaGoUiObjects uiObject = new EndavaGoUiObjects();
//    public static final Logger LOG = CustomLogger.INSTANCE.getLogger(SeekerHomeScreen.class);

    public ProfileScreen clickOnUserProfile() {
        try {
            log.info("Clicking on User Profile.");
            actionsHelper.clickOn(uiObject.seekerProfileButton());
            return new ProfileScreen();
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't click on User Profile, element absent or blocked.");
        }
    }

    public SeekerHomeScreen clickOnSearchParkingSpotButton(){
        try {
            log.info("Clicking on Search parking spot.");
            actionsHelper.clickOn(uiObject.searchButton());
            waitHelper.waitForElementVisibility(uiObject.currentSeekerParkingSpotStatusText());
            return this;
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't click on Search parking spot, element absent or blocked.");
        }
    }

    public boolean isSearchButtonPresent(){
        try {
            log.info("Checking if Search parking spot button is present...");
            waitHelper.waitForElementVisibility(uiObject.searchButton());
            return actionsHelper.isElementPresent(uiObject.searchButton());
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't find Search parking spot, element absent or blocked.");
        }
    }

    public boolean isSnackBarNotificationPresent() {
        try {
            log.info("Checking if Snack Bar Notification is present...");
            waitHelper.waitForElementVisibility(uiObject.snackBarNotification());
            return actionsHelper.isElementPresent(uiObject.snackBarNotification());
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't find Snack Bar Notification, element absent or blocked.");
        }
    }

    public boolean isReleaseParkingSeekerButtonPresent(){
        try {
            log.info("Checking if Release parking spot by Seeker button is present...");
            return actionsHelper.isElementPresent(uiObject.releaseParkingSpotSeekerButton());
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't find Search parking spot, element absent or blocked.");
        }
    }

    public String getCurrentSeekerParkingSpotStatusText() {
        try {
            log.info("Getting current parking spot text...");
            return actionsHelper.getTextOfElement(uiObject.currentSeekerParkingSpotStatusText());
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't find current parking spot text, element absent or blocked.");
        }
    }

    public void clickReleaseParkingSeekerHomeScreen(String confirmation){
        try {
            log.info("Releasing parking spot from Seeker Home Page...");
            actionsHelper.clickOn(uiObject.releaseParkingSpotSeekerButton());
            if (confirmation.equalsIgnoreCase("ok") || confirmation.equalsIgnoreCase("yes") || confirmation.equalsIgnoreCase("true")) {
                if(actionsHelper.isElementPresent(uiObject.confirmationMessageText())) actionsHelper.clickOn(uiObject.releaseConfirmationButton());
            } else
            if(actionsHelper.isElementPresent(uiObject.confirmationMessageText())) actionsHelper.clickOn(uiObject.seekerCancelReleaseButton());
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't click Release parking spot, element absent or blocked.");
        }
    }

    public void clickReleaseParkingSeekerHomeScreen(){
        try {
            log.info("Releasing parking spot from Seeker Home Page...");
            actionsHelper.clickOn(uiObject.releaseParkingSpotSeekerButton());
            if(actionsHelper.isElementPresent(uiObject.confirmationMessageText())) actionsHelper.clickOn(uiObject.releaseConfirmationButton());
        }catch (NoSuchElementException e) {
            throw new AssertionError("Can't click Release parking spot, element absent or blocked.");
        }
    }

    public String getTextOfElementSeekerSearchStatusBottomMessage() {
        try {
            log.info("Getting current status text in Bottom text message...");
            return actionsHelper.getTextOfElement(uiObject.seekerTapToFindSpotBottomTextMessage());
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't find current status text in Bottom text message, element absent or blocked.");
        }
    }

    public String getTextOfElementSeekerSearchButton() {
        try {
            log.info("Getting current status text in Search button...");
            return actionsHelper.getTextOfElement(uiObject.searchButton());
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't find current status text in Search button, element absent or blocked.");
        }
    }

    public String getTextOfElementSeekerSearchStatusTopMessage() {
        try {
            log.info("Getting current status text in Top text message...");
            return actionsHelper.getTextOfElement(uiObject.seekerYourParkingSpotForTodayTopTextMessage());
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't find current status text in Top text message, element absent or blocked.");
        }
    }

    public boolean isSeekerReleaseParkingSpotButtonPresent(){
        try {
            log.info("Checking if Seeker release button is present...");
            waitHelper.waitForElementVisibility(uiObject.releaseParkingSpotSeekerButton());
            return actionsHelper.isElementPresent(uiObject.releaseParkingSpotSeekerButton());
        } catch (NoSuchElementException | TimeoutException e) {
            throw new AssertionError("Can't find Seeker release button, element absent or blocked.");
        }
    }

    public AboutScreen clickOnMoreInfoButton() {
        try {
            log.info("Clicking on About button from profile.");
            actionsHelper.clickOn(uiObject.moreInfoButton());
            return new AboutScreen();
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't click on About button, element absent or blocked.");
        }
    }

}



