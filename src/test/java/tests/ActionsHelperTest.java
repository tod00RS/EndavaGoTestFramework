package tests;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.endava.appium.framework.helpers.ActionsHelper;
import com.endava.appium.framework.helpers.DriverHelper;
import com.endava.appium.framework.util.BaseTest;

public class ActionsHelperTest extends BaseTest {
	
    @Test
    public void testRotation() throws InterruptedException {
        log.info(DriverHelper.getDriver().manage().window().getSize().height + " / "
                + DriverHelper.getDriver().manage().window().getSize().width);
        ActionsHelper.INSTANCE.rotateToLandscape();
        log.info(DriverHelper.getDriver().manage().window().getSize().height + " / "
                + DriverHelper.getDriver().manage().window().getSize().width);
        Assert.assertTrue(ActionsHelper.INSTANCE.isLandscapeMode());
        ActionsHelper.INSTANCE.rotateToPortrait();
        log.info(DriverHelper.getDriver().manage().window().getSize().height + " / "
                + DriverHelper.getDriver().manage().window().getSize().width);
        Assert.assertTrue(ActionsHelper.INSTANCE.isPortraitMode());
    }

    @Test
    public void testNewtorkConnectionStatus() {
        
        ActionsHelper actionsHelper = ActionsHelper.INSTANCE;
        actionsHelper.turnOffWifiWithAdb();
        actionsHelper.turnOnWifiWithAdb();
        actionsHelper.turnOnAirplaneModeWithAdb();
        actionsHelper.turnOffAirplaneModeWithAdb();
        actionsHelper.turnOnAirplaneMode();
        actionsHelper.turnOffAirplaneMode();
    }
    
    @Test
    public void unistallApp() {
        DriverHelper.uninstallApp("de.bertelsmann.epub");
    }

    @AfterClass
    public void destroyConn() {
        DriverHelper.resetApplication();
        DriverHelper.closeApplication();
    }

}
