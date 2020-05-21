package tests;

import com.endava.appium.framework.screens.*;
import com.endava.appium.framework.util.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class EndavaGoSeekerProfileScreenTest extends BaseTest {

    private SeekerHomeScreen seekerHomeScreen;
    private ProfileScreen profileScreen;
    private AboutScreen aboutScreen;

    @Parameters({"email"})
    @Test (priority = 0)
    public void openAboutScreen(String email){
        AccountSelectorScreen accountSelectorScreen = new WelcomeScreen().signInWithMicrosoft();
        seekerHomeScreen = accountSelectorScreen.chooseSeekerAccount(email);
        profileScreen = seekerHomeScreen.clickOnUserProfile();
        aboutScreen = profileScreen.clickOnAboutButton();
        Assert.assertTrue(aboutScreen.isAboutScreenManualPresent(), "About Screen manual is NOT present! About Screen has not been reached!");
        profileScreen = aboutScreen.clickBackFromProfileButton();
        profileScreen.signOutFromProfileScreen();
    }

    @Parameters({"email"})
    @Test (priority = 1)
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
