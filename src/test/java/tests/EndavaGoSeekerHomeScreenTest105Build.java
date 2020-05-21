package tests;

import com.endava.appium.framework.helpers.WaitHelper;
import com.endava.appium.framework.screens.AccountSelectorScreen;
import com.endava.appium.framework.screens.ProfileScreen;
import com.endava.appium.framework.screens.SeekerHomeScreen;
import com.endava.appium.framework.screens.WelcomeScreen;
import com.endava.appium.framework.util.BaseTest;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class EndavaGoSeekerHomeScreenTest105Build extends BaseTest {

    //    public WelcomeScreen welcomeScreen;
//    public AccountSelectorScreen accountSelectorScreen;
    private SeekerHomeScreen seekerHomeScreen;
    private ProfileScreen profileScreen;
    private WaitHelper waitHelper = WaitHelper.INSTANCE;


    @Parameters({"email"})
    @Test (priority = 0)
    public void openSeekerHomePage(String email) {
        AccountSelectorScreen accountSelectorScreen = new WelcomeScreen().signInWithMicrosoft();
        seekerHomeScreen = accountSelectorScreen.chooseSeekerAccount(email);
        Assert.assertTrue(seekerHomeScreen.isSearchButtonPresent(), "Search button is NOT present! Seeker Home Page has not been reached!");
    }

    @Parameters({"email"})
    @Test(priority = 1)
    public void searchForParkingSpotSeekerHomePage(String email) {
        AccountSelectorScreen accountSelectorScreen = new WelcomeScreen().signInWithMicrosoft();
        seekerHomeScreen = accountSelectorScreen.chooseSeekerAccount(email);
        if ( !seekerHomeScreen.getTextOfElementSeekerSearchButton().equalsIgnoreCase("SEARCH")) {   //Varijanta gde se vidi bug sa SEARCH-om i sledeci red koji je menja
//          if ( !seekerHomeScreen.isSeekerReleaseParkingSpotButtonPresent()) {
            Assert.assertEquals(seekerHomeScreen.getTextOfElementSeekerSearchStatusTopMessage(), "Your parking spot for today", "Parking spot is NOT taken by seeker!");
//        } else if (seekerHomeScreen.getTextOfElementSeekerSearchStatusBottomMessage().equalsIgnoreCase("Tap to find parking spot")) {    //Varijanta gde se vidi bug sa SEARCH-om i sledeci 1 red koji je menja
          } else  {
            seekerHomeScreen.clickOnSearchParkingSpotButton();
        }
////        seekerHomeScreen.isSnackBarNotificationPresent();  //old version
        Assert.assertEquals(seekerHomeScreen.getTextOfElementSeekerSearchStatusTopMessage(), "Success!", "Parking spot is NOT taken by seeker!"); //Varijanta gde se vidi bug sa SEARCH-om i sledeci 1 red koji je menja
//        Assert.assertTrue(seekerHomeScreen.isSeekerReleaseParkingSpotButtonPresent(), "Parking spot is NOT taken by seeker!");
    }

    @Parameters({"email"})
    @Test (priority = 2)
    public void hasSeekerTakenParkingSpotOnSeekerHomePage(String email) {
        AccountSelectorScreen accountSelectorScreen = new WelcomeScreen().signInWithMicrosoft();
        seekerHomeScreen = accountSelectorScreen.chooseSeekerAccount(email);
//        if (seekerHomeScreen.isSeekerSearchStatusTopMessagePresent())
//            Assert.assertEquals(seekerHomeScreen.getTextOfElementSeekerSearchStatusTopMessage(), "Your parking spot for today", "Parking spot is NOT taken by seeker!");
        Assert.assertNotEquals(seekerHomeScreen.getTextOfElementSeekerSearchButton(), "SEARCH","Parking spot is NOT taken by seeker!");
    }

//    @Parameters({"email"})   //Varijanta gde se vidi bug sa SEARCH-om
//    @Test (priority = 3)
//    public void releaseOfParkingSpotSeekerHomePage(String email) {
//        if (null == System.getProperty("noReset")) {
//            DriverHelper.resetApplication();
//        }
//        AccountSelectorScreen accountSelectorScreen = new WelcomeScreen().signInWithMicrosoft();
//        seekerHomeScreen = accountSelectorScreen.chooseSeekerAccount(email);
//        if (seekerHomeScreen.isSeekerSearchStatusTopMessagePresent() &&
//                seekerHomeScreen.getTextOfElementSeekerSearchStatusTopMessage().equalsIgnoreCase("Your parking spot for today")) {
//            seekerHomeScreen.clickReleaseParkingSeekerHomeScreen("yes");  //Confirmations are: ok, yes, true!
//        }
//        Assert.assertEquals(seekerHomeScreen.getTextOfElementSeekerSearchButton(), "SEARCH", "Parking spot is NOT released by seeker!");
//    }

    @Parameters({"email"})
    @Test (priority = 3)
    public void releaseOfParkingSpotSeekerHomePage(String email) {
//        if (null == System.getProperty("noReset")) {
//            DriverHelper.resetApplication();
//        }
        AccountSelectorScreen accountSelectorScreen = new WelcomeScreen().signInWithMicrosoft();
        seekerHomeScreen = accountSelectorScreen.chooseSeekerAccount(email);
        if (seekerHomeScreen.isSeekerReleaseParkingSpotButtonPresent()) {
            seekerHomeScreen.clickReleaseParkingSeekerHomeScreen("yes");  //Confirmations are: ok, yes, true!
        }
        Assert.assertEquals(seekerHomeScreen.getTextOfElementSeekerSearchButton(), "SEARCH", "Parking spot is NOT released by seeker!");
    }


    @AfterMethod
    public void logOutFromSeekerUserProfile() {
        profileScreen = seekerHomeScreen.clickOnUserProfile();
        profileScreen.signOutFromProfileScreen();
    }


}
