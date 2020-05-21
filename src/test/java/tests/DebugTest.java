package tests;

import java.io.IOException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.endava.appium.framework.util.BaseTest;
import com.endava.appium.framework.util.Ignore;

public class DebugTest extends BaseTest {

    // private String homeBtn = "Home";
    // private String notificationBtn = "Notifications";
    // private String feedbackBtn = "Feedback";
    // private String settingsBtn = "Settings";
    //
    // private String titleEndavaPeopleId= "ENDAVA PEOPLE";
    // private String titleCaseStudiesId = "CASE STUDIES";
    // private String titleResourcesId = "RESOURCES";
    // private String titleNewsId = "NEWS";
    // private String menuBtnId = "button hamburger menu";
    // private String backBtnId = "button back";
    //
    // private String newsHeaderTitleTxtId = "News";
    // private String newsListId = "Empty list"; //rows 1 to 4 of 15
    //
    // private String menuEndavaId = "Endava People";
    // private String menuAboutUsId = "About Us";
    // private String subMenuAboutTheCompanyId = "About The Company";
    // private String subMenuTheProjectId = "The Project";
    //
    // private String nameTxtField = "//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[1]/UIATextField[1]";
    // private String emailTxtField = "//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[2]/UIATextField[1]";
    // private String companyTxtField = "//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[3]/UIATextField[1]";
    // private String feedbackTxtField = "//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[4]/UIATextView[1]";
    // private String sendFeedbackBtnId = "Send feedback";
    //
    // String alertSuccessTxtId = "Success";
    // String alertThankYouTxtId = "Thank You!";
    // String alertOKBtnId = "OK";
    //
    // private String notificationsSwitchId = "NOTIFICATIONS";

    @BeforeClass
    public void setup() {
        // DriverHelper.initConnection();
    }

    @Ignore
    @Test
    public void debug() throws InterruptedException, IOException {
        /*
         * CustomAppiumDriver customAppiumDriver = new
         * CustomAppiumDriver.DriverBuilder().withUrl("http://127.0.0.1:4723/wd/hub") .withPlatformVersion("8.1.3")
         * .withPlatformName("iOS") .withDeviceName("Fabiana's iPhone") .withApp("") .build();
         * DriverHelper.initConnection(customAppiumDriver);
         */
        // startAppplication();
        // Assert.fail();
        /*
         * AppiumDriver driver = DriverHelper.getDriver();
         * 
         * //swipe to news /* WebElement start = driver.findElement(By.id(titleCaseStudiesId)); WebElement end =
         * driver.findElement(By.id(titleEndavaPeopleId)); ActionsHelper.INSTANCE.swipe(start,end,650); WebElement start2 =
         * driver.findElement(By.id(titleResourcesId)); ActionsHelper.INSTANCE.swipe(start2, start, 650);
         * 
         * ActionsHelper.INSTANCE.clickOn(By.id(titleNewsId));
         * 
         * Assert.assertEquals("Header title is not correct","News",
         * ActionsHelper.INSTANCE.getTextOfElement(By.id(newsHeaderTitleTxtId)));
         * ActionsHelper.INSTANCE.clickOn(By.id(backBtnId));
         * 
         * ActionsHelper.INSTANCE.clickOn(By.id(menuBtnId));
         * Assert.assertTrue(ActionsHelper.INSTANCE.isElementPresent(By.id(menuEndavaId)));
         * ActionsHelper.INSTANCE.clickOn(By.id(menuAboutUsId));
         * Assert.assertTrue(ActionsHelper.INSTANCE.isElementPresent(By.id(subMenuTheProjectId)));
         * Assert.assertTrue(ActionsHelper.INSTANCE.isElementPresent(By.id(subMenuAboutTheCompanyId)));
         * 
         * ActionsHelper.INSTANCE.clickOn(By.id(menuBtnId));
         * Assert.assertTrue(ActionsHelper.INSTANCE.isElementPresent(By.id(titleNewsId)));
         */

        /*
         * ActionsHelper.INSTANCE.clickOn(By.id(feedbackBtn));
         * ActionsHelper.INSTANCE.typeText(By.xpath(nameTxtField),"Demo");
         * ActionsHelper.INSTANCE.typeText(By.xpath(emailTxtField), "demo@endava.com");
         * ActionsHelper.INSTANCE.typeText(By.xpath(companyTxtField), "Endava");
         * ActionsHelper.INSTANCE.typeText(By.xpath(feedbackTxtField),"Test feedback here."); driver.hideKeyboard();
         * ActionsHelper.INSTANCE.clickOn(By.id(sendFeedbackBtnId));
         * 
         * Assert.assertTrue(ActionsHelper.INSTANCE.isElementPresent(By.id(alertSuccessTxtId)));
         * Assert.assertTrue(ActionsHelper.INSTANCE.isElementPresent(By.id(alertThankYouTxtId)));
         * Assert.assertTrue(ActionsHelper.INSTANCE.isElementPresent(By.id(alertOKBtnId)));
         * ActionsHelper.INSTANCE.clickOn(By.id(alertOKBtnId));
         */

        /*
         * ActionsHelper.INSTANCE.clickOn(By.id(settingsBtn)); WebElement elem =
         * driver.findElements(By.className("UIASwitch")).get(0); Assert.assertEquals("1", elem.getAttribute("value"));
         * ActionsHelper.INSTANCE.clickOn(elem); //ActionsHelper.INSTANCE.clickOn(By.className("UISwitch"));
         * Assert.assertEquals("0", elem.getAttribute("value"));
         * 
         * //DriverHelper.closeApplication(); //DriverHelper.killDriver();
         */

    }

}
