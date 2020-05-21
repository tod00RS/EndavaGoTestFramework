//package tests;
//
//
//import com.endava.appium.framework.helpers.DriverHelper;
//import com.endava.appium.framework.screens.OwnerHomeScreen;
//import com.endava.appium.framework.screens.WelcomeScreen;
//import com.endava.appium.framework.util.BaseTest;
//import org.jboss.netty.handler.timeout.ReadTimeoutException;
//import org.testng.Assert;
//import org.testng.ITestContext;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Parameters;
//import org.testng.annotations.Test;
//
//import java.util.concurrent.TimeoutException;
//
//
//public class EndavaGoOwnerHomeScreenBuild101Test extends BaseTest {
//
//    private OwnerHomeScreen ownerHomeScreen;
//    private int orderNumberOfTheDayForParkingSpot;
//
//    @Parameters({"email"})
//    @Test (invocationCount = 2, priority = 6)
//    public void reachOwnerHomeScreen(String email) {
//        orderNumberOfTheDayForParkingSpot = 1;
//        ownerHomeScreen = new WelcomeScreen().loginOwnerSamsungA50();
////        ownerHomeScreen = new WelcomeScreen().loginOwnerAccountBuild101(email);
//        Assert.assertTrue(ownerHomeScreen.isSearchReleaseButtonPresent(orderNumberOfTheDayForParkingSpot), "Search/Release button is not present, Owner Home screen is not present!");
//        ownerHomeScreen.logOut();
//    }
//
//    @Parameters({"email"})
//    @Test(priority = 1)
//    public void releaseOwnerParkingSpotToday(String email) {
//        orderNumberOfTheDayForParkingSpot = 1;
//        ownerHomeScreen = new WelcomeScreen().loginOwnerSamsungA50();  //Samsung A50
////        ownerHomeScreen = new WelcomeScreen().loginOwnerAccountBuild101(email);
//        if (ownerHomeScreen.isParkingSpotAvailableToRelease(orderNumberOfTheDayForParkingSpot)) {
//            ownerHomeScreen.clickOnReleaseSpot(orderNumberOfTheDayForParkingSpot);
//        }
//        Assert.assertEquals(ownerHomeScreen.getTextFromOwnerParkingSpot(orderNumberOfTheDayForParkingSpot), "Search", "Owner parking spot is NOT released!");
//        ownerHomeScreen.logOut();
//    }
//
//    @Parameters({"email"})
//    @Test (priority = 2)
//    public void searchOrUndoReleaseOwnerParkingSpotToday(String email){
//        int orderNumberOfTheDayForParkingSpot = 1;
//        ownerHomeScreen = new WelcomeScreen().loginOwnerSamsungA50();  //Samsung A50
////        ownerHomeScreen = new WelcomeScreen().loginOwnerAccountBuild101(email);
//        if ( !ownerHomeScreen.isParkingSpotAvailableToRelease(orderNumberOfTheDayForParkingSpot)) {
//            ownerHomeScreen.clickOnReleaseSpot(orderNumberOfTheDayForParkingSpot);
//        }
//        try {
//            ownerHomeScreen.waitForTextFromOwnerParkingSpot(orderNumberOfTheDayForParkingSpot, "Release");
//        } catch (ReadTimeoutException e){
//            e.printStackTrace();
//        }
//        Assert.assertEquals(ownerHomeScreen.getOwnerParkingSpotStatusInfo(orderNumberOfTheDayForParkingSpot), "", "Owner parking spot is NOT available for release!");
//        ownerHomeScreen.logOut();
//    }
//
//    @DataProvider(name = "OrderNumberOfTheDayForParkingSpot")
//    public Object[][] getDataFromOrderNumberOfTheDayForParkingSpotDataProvider(ITestContext context) {
//        String emailParameterFromXML = context.getCurrentXmlTest().getParameter("email");
//        return new Object[][]
//                {
//                        { emailParameterFromXML, 3 },
//                        { emailParameterFromXML, 5},
//                };
//    }
//
//    @Parameters({"email"})
//    @Test(priority = 3)
//    public void releaseOwnerParkingSpotsForMultipleDaysAtOnce(String email) {
//        int daysToReleaseParkingSpot[] = {2, 3, 5};
//        ownerHomeScreen = new WelcomeScreen().loginOwnerSamsungA50();  //Samsung A50
////        ownerHomeScreen = new WelcomeScreen().loginOwnerAccountBuild101(email);
//        for (int i = 0; i < daysToReleaseParkingSpot.length; i++) {
//            if (ownerHomeScreen.isParkingSpotAvailableToRelease(daysToReleaseParkingSpot[i])) {
//                ownerHomeScreen.clickOnReleaseSpot(daysToReleaseParkingSpot[i]);
//                Assert.assertTrue(ownerHomeScreen.getTextFromOwnerParkingSpot(daysToReleaseParkingSpot[i]).equalsIgnoreCase("Search") ||
//                                ownerHomeScreen.getTextFromOwnerParkingSpot(daysToReleaseParkingSpot[i]).equalsIgnoreCase("Undo"),
//                        "Owner parking spot for day order number: " + daysToReleaseParkingSpot[i] + " is NOT released!");
//            }
//        }
//        ownerHomeScreen.logOut();
//    }
//
////    @Parameters({"email"})  // @Parameters and @DataProvider can NOT be combined in the same test!
//    @Test(priority = 4, dataProvider = "OrderNumberOfTheDayForParkingSpot")
//    public void releaseOwnerParkingSpotForOneParticularDay(String email, int orderNumberOfTheDayForParkingSpot) {
//        ownerHomeScreen = new WelcomeScreen().loginOwnerSamsungA50();  //Samsung A50
////        ownerHomeScreen = new WelcomeScreen().loginOwnerAccountBuild101(email);
//        if (ownerHomeScreen.isParkingSpotAvailableToRelease(orderNumberOfTheDayForParkingSpot)) {
//            ownerHomeScreen.clickOnReleaseSpot(orderNumberOfTheDayForParkingSpot);
//        }
//        Assert.assertTrue(ownerHomeScreen.getTextFromOwnerParkingSpot(orderNumberOfTheDayForParkingSpot).equalsIgnoreCase("Search")  ||
//                ownerHomeScreen.getTextFromOwnerParkingSpot(orderNumberOfTheDayForParkingSpot).equalsIgnoreCase("Undo"), "Owner parking spot is NOT released!");
//        ownerHomeScreen.logOut();
//    }
//
//    @Parameters({"email"})
//    @Test(priority = 5)
//    public void searchOrUndoReleaseOwnerParkingSpotsForMultipleDaysAtOnce(String email) {
//        int daysToSearchOrUndoReleaseParkingSpots[] = {2, 3, 4, 5, 6};
//        ownerHomeScreen = new WelcomeScreen().loginOwnerSamsungA50();  //Samsung A50
////        ownerHomeScreen = new WelcomeScreen().loginOwnerAccountBuild101(email);
//        for (int i = 0; i < daysToSearchOrUndoReleaseParkingSpots.length; i++) {
//            if ( !ownerHomeScreen.isParkingSpotAvailableToRelease(daysToSearchOrUndoReleaseParkingSpots[i])) {
//                ownerHomeScreen.clickOnReleaseSpot(daysToSearchOrUndoReleaseParkingSpots[i]);
//                ownerHomeScreen.waitForTextFromOwnerParkingSpot(daysToSearchOrUndoReleaseParkingSpots[i], "Release");
//                Assert.assertTrue(ownerHomeScreen.getTextFromOwnerParkingSpot(daysToSearchOrUndoReleaseParkingSpots[i]).equalsIgnoreCase("Release") ,
//                        "Owner parking spot for day order number: " + daysToSearchOrUndoReleaseParkingSpots[i] + " IS released!");
//            }
//        }
//        ownerHomeScreen.logOut();
//    }
//
//    @AfterMethod
//    public void resetApplication(){
//        if (null == System.getProperty("noReset")) {
//            DriverHelper.resetApplication();
//        }
//    }
//
//
//}
