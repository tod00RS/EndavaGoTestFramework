package tests;

import com.endava.appium.framework.screens.*;
import com.endava.appium.framework.util.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class EndavaGoSeekerProfileScreenTest105Build extends BaseTest {

    private SeekerHomeScreen seekerHomeScreen;
    private ProfileScreen profileScreen;
    private AboutScreen aboutScreen;

    private static final String MANUAL_INFO = "MANUAL INFO";
    private static final String CONTACT = "CONTACT";
    private static final String ABOUT = "ABOUT";

    @Parameters({"email"})
    @Test (priority = 0)
    public void openAboutScreen(String email){
        AccountSelectorScreen accountSelectorScreen = new WelcomeScreen().signInWithMicrosoft();
        seekerHomeScreen = accountSelectorScreen.chooseSeekerAccount(email);
//        profileScreen = seekerHomeScreen.clickOnUserProfile();
//        aboutScreen = profileScreen.clickOnAboutButton();
        aboutScreen = seekerHomeScreen.clickOnMoreInfoButton();
        aboutScreen.clickOnManualInfoButton();
        Assert.assertEquals(MANUAL_INFO, aboutScreen.getToolbarTitleText(), "Manual Info Screen is NOT present! Manual Info Screen has not been reached!");
        aboutScreen.clickBackFromManualScreenButton();
        aboutScreen.clickOnContactInfoButton();
        Assert.assertEquals(CONTACT, aboutScreen.getToolbarTitleText(), "Contact Info Screen is NOT present! Contact Info Screen has not been reached!");
        aboutScreen.clickBackFromManualScreenButton();
        aboutScreen.clickOnAboutInfoButton();
        Assert.assertEquals(ABOUT, aboutScreen.getToolbarTitleText(), "About Info Screen is NOT present! About Info Screen has not been reached!");
        aboutScreen.clickBackFromManualScreenButton();
        seekerHomeScreen = aboutScreen.clickBackFromManualScreenButton();

//        Assert.assertTrue(aboutScreen.isAboutScreenManualPresent(), "About Screen manual is NOT present! About Screen has not been reached!");
        profileScreen = seekerHomeScreen.clickOnUserProfile();
        profileScreen.signOutFromProfileScreen();
    }

    @Parameters({"email"})
    @Test (priority = 1, invocationCount = 3)
    public void openProfileScreen(String email){
        AccountSelectorScreen accountSelectorScreen = new WelcomeScreen().signInWithMicrosoft();
        seekerHomeScreen = accountSelectorScreen.chooseSeekerAccount(email);
        profileScreen = seekerHomeScreen.clickOnUserProfile();
        Assert.assertTrue(profileScreen.isProfileImagePresent(), "Profile Image is NOT present! Profile Screen has not been reached!");
        Assert.assertTrue(profileScreen.isProfileUserNameFieldPresent(), "Profile Username is NOT present! Profile Screen has not been reached!");
        Assert.assertTrue(profileScreen.isProfileEmailPresent(), "Profile Email is NOT present! Profile Screen has not been reached!");
        profileScreen.signOutFromProfileScreen();
    }




}
