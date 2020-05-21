package tests;

import com.endava.appium.framework.screens.HomeScreen;
import com.endava.appium.framework.util.BaseTest;
import org.testng.annotations.Test;

public class DemoTest extends BaseTest {

    @Test
    public void reachHomePage(){
            HomeScreen homeScreen = new HomeScreen().clickOnContinueWithAdsButton();
            homeScreen.verifySideNavigationMenuIsDisplayed();
        }



}
