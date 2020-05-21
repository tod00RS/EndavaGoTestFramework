package com.endava.appium.framework.screens;

import com.endava.appium.framework.objects.EndavaGoUiObjects;

import java.util.NoSuchElementException;

public class WelcomeScreen extends BaseScreen {

    public EndavaGoUiObjects uiObject = new EndavaGoUiObjects();

    public AccountSelectorScreen signInWithMicrosoft(){
        try {
            log.info("Clicking on Sign in with Microsoft.");
            actionsHelper.clickOn(uiObject.signInWithMicrosoftBtn());
            return new AccountSelectorScreen();
        } catch (
                NoSuchElementException e) {
            throw new AssertionError("Can't click on Sign in with Microsoft, element absent or blocked.");
        }
    }

    public OwnerHomeScreen loginOwnerSamsungA50(){
        actionsHelper.clickOn(uiObject.signInWithMicrosoftBtn());
        waitHelper.waitForElementToContainText(uiObject.urlTextFieldBuild1Dot0Dot1(), "login");
        actionsHelper.clickAtCoords(810, 550);
        waitHelper.waitFor(10000);
        if(actionsHelper.isElementPresent(uiObject.appErrorMessage())) actionsHelper.clickOn(uiObject.closeAppBtn());
        adbHelper.bringAppInForeground("com.endava.rsbgd.app.endavago","com.endava.rsbgd.app.endavago.MainActivity");
//        waitHelper.waitFor(5000);
        waitHelper.waitForElementVisibility(uiObject.ownerProfileButton());
        return new OwnerHomeScreen();
    }

    public OwnerHomeScreen loginOwnerAccount(String email) {
        try {
            actionsHelper.clickOn(uiObject.signInWithMicrosoftBtn());
            waitHelper.waitForElementToContainText(uiObject.urlTextFieldBuild1Dot0(), "login");
            log.info("Choosing Owner Account...");
            actionsHelper.clickOn(uiObject.pickAccBtn(email));
            waitHelper.waitFor(10000);
            if(actionsHelper.isElementPresent(uiObject.appErrorMessage())) actionsHelper.clickOn(uiObject.closeAppBtn());
            adbHelper.bringAppInForeground("com.endava.rsbgd.app.endavago","com.endava.rsbgd.app.endavago.MainActivity");
//            waitHelper.waitFor(5000);
            waitHelper.waitForElementVisibility(uiObject.ownerProfileButton());
            return new OwnerHomeScreen();
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't choose account, element absent or blocked.");
        }
    }

    public OwnerHomeScreen loginOwnerAccountBuild101(String email) {
        try {
            actionsHelper.clickOn(uiObject.signInWithMicrosoftBtn());
            waitHelper.waitForElementToContainText(uiObject.urlTextFieldBuild1Dot0Dot1(), "login");
            log.info("Choosing Owner Account From Build 1.0.1...");
            actionsHelper.clickOn(uiObject.pickAccBtn(email));
            waitHelper.waitFor(10000);
            if(actionsHelper.isElementPresent(uiObject.appErrorMessage())) actionsHelper.clickOn(uiObject.closeAppBtn());
            adbHelper.bringAppInForeground("com.endava.rsbgd.app.endavago","com.endava.rsbgd.app.endavago.MainActivity");
            waitHelper.waitFor(2000);
            waitHelper.waitForElementVisibility(uiObject.ownerProfileButton());
            return new OwnerHomeScreen();
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't choose account, element absent or blocked.");
        }
    }

}
