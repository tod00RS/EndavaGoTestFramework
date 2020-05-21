package com.endava.appium.framework.screens;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.testng.Assert;

public class HomeScreen extends BaseScreen{

    private By sideNavigationMenu = MobileBy.id("net.zedge.android:id/menu_fragment");
    private By continueWithAds = MobileBy.AndroidUIAutomator("new UiSelector().text(\"NO, CONTINUE WITH ADS\")");


    public boolean verifyNoAdvertisementNotificationIsDisplayed(){
        return (actionsHelper.isElementPresent(continueWithAds));
    }

    public HomeScreen clickOnContinueWithAdsButton(){
        if (actionsHelper.isElementPresent(continueWithAds)) actionsHelper.clickOn(continueWithAds);
        return new HomeScreen();
    }

    public HomeScreen verifySideNavigationMenuIsDisplayed(){
        Assert.assertTrue(actionsHelper.isElementPresent(sideNavigationMenu));
        return this;
    }




}
