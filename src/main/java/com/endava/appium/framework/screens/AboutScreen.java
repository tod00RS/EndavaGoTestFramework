package com.endava.appium.framework.screens;

import com.endava.appium.framework.objects.EndavaGoUiObjects;

import java.util.NoSuchElementException;

public class AboutScreen extends BaseScreen {

    EndavaGoUiObjects uiObject = new EndavaGoUiObjects();

    public boolean isAboutScreenManualPresent(){
        try {
            log.info("Checking if About Screen is opened...");
            waitHelper.waitForElementVisibility(uiObject.aboutScreenManual());
            return actionsHelper.isElementPresent(uiObject.aboutScreenManual());
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't find About Screen manual, element absent or blocked.");
        }
    }

    public ProfileScreen clickBackFromProfileButton (){
        try {
            log.info("Clicking on Back from About Screen...");
            actionsHelper.clickOn(uiObject.backFromProfileScreenButton());
            return new ProfileScreen();
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't click on Back from About Screen, element absent or blocked.");
        }
    }

    public AboutScreen clickOnManualInfoButton() {
        try {
            log.info("Clicking on Manual Info button from profile.");
            actionsHelper.clickOn(uiObject.manualInfoButton());
            return new AboutScreen();
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't click on Manual Info button, element absent or blocked.");
        }
    }

    public String getToolbarTitleText() {
        try {
            log.info("Getting text from toolbar title...");
            return actionsHelper.getTextOfElement(uiObject.toolbarTitleField());
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't find toolbar title, element absent or blocked.");
        }
    }

    public SeekerHomeScreen clickBackFromManualScreenButton (){
        try {
            log.info("Clicking on Back from Manual Screen...");
            actionsHelper.clickOn(uiObject.backOneScreenFromManualButton());
            return new SeekerHomeScreen();
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't click on Back from About Screen, element absent or blocked.");
        }
    }

    public AboutScreen clickOnContactInfoButton() {
        try {
            log.info("Clicking on Contact Info button from profile.");
            actionsHelper.clickOn(uiObject.contactInfoButton());
            return new AboutScreen();
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't click on Contact Info button, element absent or blocked.");
        }
    }

    public AboutScreen clickOnAboutInfoButton() {
        try {
            log.info("Clicking on About Info button from profile.");
            actionsHelper.clickOn(uiObject.aboutInfoButton());
            return new AboutScreen();
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't click on About Info button, element absent or blocked.");
        }
    }

}
