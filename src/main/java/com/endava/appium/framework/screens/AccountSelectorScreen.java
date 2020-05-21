package com.endava.appium.framework.screens;

import com.endava.appium.framework.helpers.DriverHelper;
import com.endava.appium.framework.objects.EndavaGoUiObjects;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;

public class AccountSelectorScreen extends BaseScreen {

    public EndavaGoUiObjects uiObject = new EndavaGoUiObjects();
//    public ActionsHelper actionsHelper = ActionsHelper.INSTANCE;
//    private static final Logger LOG = CustomLogger.INSTANCE.getLogger(AccountSelectorScreen.class);

    public SeekerHomeScreen chooseSeekerAccount(String email) {
        try {
//            LOG.info("Choosing Seeker Account.");
            log.info("Choosing Seeker Account.");
            actionsHelper.clickOn(uiObject.pickAccBtn(email));
            return new SeekerHomeScreen();
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't choose account, element absent or blocked.");
        }
    }

    public AccountSelectorScreen clickOpenSignOutFromAccountMenuButton(){
        try {
            log.info("Clicking on Sign Out Menu.");
            actionsHelper.clickOn(uiObject.openSignOutMenuButton());
            return this;
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't click on Sign Out Menu, element absent or blocked.");
        }
    }

    public void signOutFromAccount(){
        try {
            clickOpenSignOutFromAccountMenuButton();
            log.info("Clicking on Sign Out from account.");
            actionsHelper.clickOn(uiObject.signOutAccountButton());
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't click on Sign Out from account, element absent or blocked.");
        }
    }

    public void signOutFromAccountAndForget(){
        try {
            clickOpenSignOutFromAccountMenuButton();
            log.info("Clicking on Sign Out from account and forget.");
            actionsHelper.clickOn(uiObject.signOutAndForgetAccountButton());
        } catch (NoSuchElementException e) {
            throw new AssertionError("Can't click on Sign Out from account and forget, element absent or blocked.");
        }
    }


    public boolean isUserAccAvailableInList(String emailAddress) {
        waitHelper.waitForElementVisibility(uiObject.useAnotherAccBtn());
        List<WebElement> listOfAccUsers = DriverHelper.getDriver().findElements(MobileBy.AndroidUIAutomator("new UiSelector().text(\"Sign in with " + emailAddress + " work or school account.\")"));
        boolean contains = false;
        int numOfAccUsers = listOfAccUsers.size();
        for (int i = 0; i < numOfAccUsers; i++) {
            contains = listOfAccUsers.get(i).getText().contains(emailAddress);
        }
        return contains;
    }

    public SignInNewAccountMicrosoft addNewAccount(String emailAddress) {
        actionsHelper.clickOn(uiObject.useAnotherAccBtn());
        log.info("Clicking on Use another account button.");
        actionsHelper.typeText(uiObject.inputEmailFieldMicrosoftSignInPage(), emailAddress);
        log.info("Typing email address in email input field.");
        actionsHelper.clickOn(uiObject.nextBtnMicrosoftSignInPage());
        log.info("Clicking on Next button.");
        return new SignInNewAccountMicrosoft();
    }

    public AccountSelectorScreen openAccOptionTab(String emailAddress) {
        actionsHelper.clickOn(uiObject.accOptionTab(emailAddress));
        return this;
    }

    public boolean isAccTabOpened() {
        return actionsHelper.isElementPresent(uiObject.accOptionSignOutTab());
    }


    public void pressBackButton(){
        actionsHelper.navigateBack();
    }


//    public OwnerHomeScreen pickOwnerAccount(){
//
//    }


}
