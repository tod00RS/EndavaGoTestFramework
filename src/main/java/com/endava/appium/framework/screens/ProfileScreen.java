package com.endava.appium.framework.screens;

import com.endava.appium.framework.objects.EndavaGoUiObjects;

import java.util.NoSuchElementException;

public class ProfileScreen extends BaseScreen {

    public EndavaGoUiObjects uiObject = new EndavaGoUiObjects();

    public WelcomeScreen signOutFromProfileScreen() {
        try {
            log.info("Clicking on Sign out from profile.");
            actionsHelper.clickOn(uiObject.signOutFromProfileScreenButton());
            return new WelcomeScreen();
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't click on Sign out from profile, element absent or blocked.");
        }
    }

    public void clickBackFromProfileButton (){
        try {
            log.info("Clicking on Back from profile.");
            actionsHelper.clickOn(uiObject.backFromProfileScreenButton());
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't click on Back from profile button, element absent or blocked.");
        }
    }

    public AboutScreen clickOnAboutButton() {
        try {
            log.info("Clicking on About button from profile.");
            actionsHelper.clickOn(uiObject.infoButton());
            return new AboutScreen();
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't click on About button, element absent or blocked.");
        }
    }

    public boolean isProfileImagePresent(){
        try {
            log.info("Checking if Profile Image is present...");
            waitHelper.waitForElementVisibility(uiObject.profileImage());
            return actionsHelper.isElementPresent(uiObject.profileImage());
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't find Profile Image, element absent or blocked.");
        }
    }

    public boolean isProfileUserNameFieldPresent(){
        try {
            log.info("Checking if Profile Username is present...");
            waitHelper.waitForElementVisibility(uiObject.profileUserNameField());
            return actionsHelper.isElementPresent(uiObject.profileUserNameField());
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't find Profile Username, element absent or blocked.");
        }
    }

    public boolean isProfileEmailPresent(){
        try {
            log.info("Checking if Profile Email is present...");
            waitHelper.waitForElementVisibility(uiObject.profileEmailField());
            return actionsHelper.isElementPresent(uiObject.profileEmailField());
        }catch (NoSuchElementException e){
            throw new AssertionError("Can't find Profile Email, element absent or blocked.");
        }
    }


}



