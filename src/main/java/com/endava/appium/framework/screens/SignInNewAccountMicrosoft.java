package com.endava.appium.framework.screens;

import com.endava.appium.framework.objects.EndavaGoUiObjects;

public class SignInNewAccountMicrosoft extends BaseScreen {

    private EndavaGoUiObjects uiObject = new EndavaGoUiObjects();

    public String getEmailAddress(String emailAddress) {
        return actionsHelper.getTextOfElement(uiObject.usernameFieldEndavaFederationSignInPage(emailAddress.toLowerCase()));
    }

}
