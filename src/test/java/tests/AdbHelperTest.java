package tests;

import org.slf4j.Logger;
import org.testng.annotations.Test;

import com.endava.appium.framework.helpers.AdbHelper;
import com.endava.appium.framework.helpers.ConfigHelper;
import com.endava.appium.framework.util.CapabilitiesConstants;
import com.endava.appium.framework.util.CustomLogger;

public class AdbHelperTest {

    private static final Logger LOG = CustomLogger.INSTANCE.getLogger(AdbHelper.class);

    @Test
    public void pushFiles() {
        ConfigHelper.getInstance().setCapability(CapabilitiesConstants.CAPAB_PLATFORM_NAME, "Android");
        AdbHelper.INSTANCE.pushFilesToDevice(System.getProperty("user.dir") + "/src/test/resources/transfers/",
                "folder2folder");
        AdbHelper.INSTANCE.pushFileToDevice(System.getProperty("user.dir")
                + "/src/test/resources/transfers/TEST_TRANSFER.txt", "singleFileTransfer");

    }

    @Test
    public void testAdbHelperIos() {
        ConfigHelper.getInstance().setCapability(CapabilitiesConstants.CAPAB_PLATFORM_NAME, "iOS");
        try {
            AdbHelper.INSTANCE.pushFilesToDevice(System.getProperty("user.dir") + "/src/test/resources/transfers/",
                    "folder2folder");

        } catch (AssertionError e) {
            LOG.error(e.getMessage());
        }

        try {
            AdbHelper.INSTANCE.pushFileToDevice(System.getProperty("user.dir")
                    + "/src/test/resources/transfers/TEST_TRANSFER.txt", "singleFileTransfer");
        } catch (AssertionError e) {
            LOG.error(e.getMessage());
        }
    }

    @Test
    public void testAdbHelperUnknown() {
        ConfigHelper.getInstance().setCapability(CapabilitiesConstants.CAPAB_PLATFORM_NAME, "afsafafaf");
        try {
            AdbHelper.INSTANCE.pushFilesToDevice(System.getProperty("user.dir") + "/src/test/resources/transfers/",
                    "folder2folder");

        } catch (AssertionError e) {
            LOG.error(e.getMessage());
        }

        try {
            AdbHelper.INSTANCE.pushFileToDevice(System.getProperty("user.dir")
                    + "/src/test/resources/transfers/TEST_TRANSFER.txt", "singleFileTransfer");
        } catch (AssertionError e) {
            LOG.error(e.getMessage());

        }
    }

    @Test
    public void testActivityReader() {
        for (String s : AdbHelper.INSTANCE.getActivityDetails("bmp")) {
            LOG.info(s);
        }

    }
    
    @Test
    public void testApplicationClosed() {
        LOG.info(String.valueOf(AdbHelper.INSTANCE.isAppClosed("de.telekom.bmp")));
    }
    
    
    @Test
    public void testApplicationInstalled() {
        //LOG.info(AdbHelper.INSTANCE.isAppInstalled("com.android.chrome"));
    }

}
