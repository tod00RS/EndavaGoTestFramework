package tests;

import com.endava.appium.framework.screens.AccountSelectorScreen;
import com.endava.appium.framework.screens.ProfileScreen;
import com.endava.appium.framework.screens.SeekerHomeScreen;
import com.endava.appium.framework.screens.WelcomeScreen;
import com.endava.appium.framework.util.BaseTest;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class EndavaGoSeekerHomeScreenTest extends BaseTest {

//    public WelcomeScreen welcomeScreen;
//    public AccountSelectorScreen accountSelectorScreen;
    private SeekerHomeScreen seekerHomeScreen;
    private ProfileScreen profileScreen;

    @Parameters({"email"})
    @Test (priority = 0)
    public void openSeekerHomePage(String email) {
        AccountSelectorScreen accountSelectorScreen = new WelcomeScreen().signInWithMicrosoft();
        seekerHomeScreen = accountSelectorScreen.chooseSeekerAccount(email);
        Assert.assertTrue(seekerHomeScreen.isSearchButtonPresent(), "Search button is NOT present! Seeker Home Page has not been reached!");
    }

    @Parameters({"email"})
    @Test(priority = 3)
    public void searchForParkingSpotSeekerHomePage(String email) {
        AccountSelectorScreen accountSelectorScreen = new WelcomeScreen().signInWithMicrosoft();
        seekerHomeScreen = accountSelectorScreen.chooseSeekerAccount(email);
        if (seekerHomeScreen.getCurrentSeekerParkingSpotStatusText().equalsIgnoreCase("Your temporary parking spot for today")) {
            Assert.assertEquals(seekerHomeScreen.getCurrentSeekerParkingSpotStatusText(), "Your temporary parking spot for today", "Parking spot is NOT taken by seeker!");
//        } else if (!seekerHomeScreen.isSnackBarNotificationPresent()){  //nece se videti snack bar notification, izmeni uslov
        } else if (seekerHomeScreen.getCurrentSeekerParkingSpotStatusText().equalsIgnoreCase("You have no parking spot today")) {
            seekerHomeScreen.clickOnSearchParkingSpotButton();
        }
        Assert.assertEquals(seekerHomeScreen.getCurrentSeekerParkingSpotStatusText(), "You have no parking spot today", "Parking spot IS taken by seeker!");
    }

    @Parameters({"email"})
    @Test (priority = 2)
    public void hasSeekerTakenParkingSpotOnSeekerHomePage(String email) {
        AccountSelectorScreen accountSelectorScreen = new WelcomeScreen().signInWithMicrosoft();
        seekerHomeScreen = accountSelectorScreen.chooseSeekerAccount(email);
        Assert.assertEquals(seekerHomeScreen.getCurrentSeekerParkingSpotStatusText(),"Your temporary parking spot for today", "Parking spot is NOT taken by seeker!");
    }

    @Parameters({"email"})
    @Test (priority = 1)
    public void releaseOfParkingSpotSeekerHomePage(String email) {
        AccountSelectorScreen accountSelectorScreen = new WelcomeScreen().signInWithMicrosoft();
        seekerHomeScreen = accountSelectorScreen.chooseSeekerAccount(email);
        if (seekerHomeScreen.getCurrentSeekerParkingSpotStatusText().equalsIgnoreCase("Your temporary parking spot for today")) {
            seekerHomeScreen.clickReleaseParkingSeekerHomeScreen("yes");  //Confirmations are: ok, yes, true!
        }
        Assert.assertEquals(seekerHomeScreen.getCurrentSeekerParkingSpotStatusText(), "You have no parking spot today", "Parking spot is NOT released by seeker!");
    }

    @AfterMethod
    public void logOutFromSeekerUserProfile() {
        profileScreen = seekerHomeScreen.clickOnUserProfile();
        profileScreen.signOutFromProfileScreen();
    }


}
