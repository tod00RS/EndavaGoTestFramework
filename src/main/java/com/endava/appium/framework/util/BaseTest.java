package com.endava.appium.framework.util;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.MDC;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import com.endava.appium.framework.helpers.ConfigHelper;
import com.endava.appium.framework.helpers.DriverHelper;
import com.endava.appium.framework.helpers.ScreenshotHelper;
import com.endava.appium.framework.reporting.ExtentManager;
import com.endava.appium.framework.reporting.ExtentTestManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

/**
 * BASE TEST CLASS THAT HANDLES: - TEST RUNS - HTML REPORTING (Controls when the
 * HTML reporter starts, when reporting starts for each test) - STARTS / STOPS
 * APPLICATION BEFORE/AFTER EACH TEST - HANDLES INITIAL SETUP / CLEANUP - TAKES
 * ACTIONS DEPENDING ON TEST RESULT (Pass/Fail/Skip) ALL TESTS THAT WANT TO USE
 * THE FRAMEWORK CAPABILITIES SHOULD EXTEND THIS CLASS
 */
@Listeners({ BaseTestListener.class, TestFilterListener.class })
public class BaseTest extends Assert {

	protected final Logger log = CustomLogger.INSTANCE.getLogger(getClass());
	public static final String TEST_NAME = "testname";
	private String callingTestClassName;
	private static ExtentReports extent;
	protected String configFile;
	private static ThreadLocal<Boolean> initializationStatus = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return false;
		}
	};

	public static ThreadLocal<Boolean> getInitializationStatus() {  //ADDED, ADDED, ADDED
		return initializationStatus;
	}

	public String getCallingTestClassName() {  //ADDED, ADDED, ADDED
		return callingTestClassName;
	}

	public BaseTest() {
		this.callingTestClassName = getClass().getSimpleName();
	}

	@BeforeSuite(alwaysRun = true)
	public void extentSetup(ITestContext context) {
		ExtentManager.setOutputDirectory(context);
		extent = ExtentManager.getInstance();
	}

	@BeforeMethod(alwaysRun = true)
	public void startExtent(Method method) {
		ExtentTestManager.startTest(method.getName());
		ExtentTestManager.getTest().assignCategory(
				(String) ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_DEVICE_NAME));
	}

	@AfterMethod(alwaysRun = true)
	public void afterEachTestMethod(ITestResult result) {

		ExtentTestManager.getTest().getTest().setStartedTime(getTime(result.getStartMillis()));
		ExtentTestManager.getTest().getTest().setEndedTime(getTime(result.getEndMillis()));

		for (String group : result.getMethod().getGroups()) {
			ExtentTestManager.getTest().assignCategory(group);
		}

		if (result.getStatus() == 1) {
			ExtentTestManager.getTest().log(LogStatus.PASS, "Test Finished");
		} else if (result.getStatus() == 2) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, result.getThrowable());
			String image = ExtentTestManager.getTest()
					.addScreenCapture(ScreenshotHelper.INSTANCE.takeScreenshot().getLocation());
			ExtentTestManager.getTest().log(LogStatus.FAIL, "FailureImage", image);
			String logFolder = System.getProperty("user.dir") + "/logs/";
			ExtentTestManager.getTest().log(LogStatus.FAIL, "<a href='" + logFolder
					+ "' target='_blank'><span class='label info'>Go to logs folder</span></a>");
		} else if (result.getStatus() == 3) {
			if (null != result.getThrowable()) {
				ExtentTestManager.getTest().log(LogStatus.SKIP,
						"Test Skipped due to " + result.getThrowable().getMessage());
			} else {
				ExtentTestManager.getTest().log(LogStatus.SKIP,
						"Test Skipped due to failing/skipping in before hooks of tests");
			}
		}

		ExtentTestManager.endTest();
		extent.flush();
	}

	@AfterSuite(alwaysRun = true)
	public void generateReport() {
		if (null != extent) {
			extent.close();
			// reset initializationStatus in case of multiple suite runs from
			// the
			// same VM instance
			initializationStatus.set(false);
			// reset the static extent to null so it gets created by next suite
			// runner (in case of multiple suite runs from the same VM instance)
			ExtentManager.resetManager();
		}
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	@BeforeClass(alwaysRun = true)
	public void baseTestSetup() {
		MDC.put(TEST_NAME, callingTestClassName + "-Thread-id-" + Thread.currentThread().getId());
		if (!initializationStatus.get()) {
			// first time call for init NO NEED to call launch application
			log.info("First test class from suite, using brand new driver with session id: "
					+ DriverHelper.getDriver().getSessionId());
			initializationStatus.set(true);
		} else {
			// all future calls from same run will close&launch after each test
			// reset of application data is done in @AfterClass hook
			log.info("Using existing driver with Session Id:" + DriverHelper.getDriver().getSessionId());
			DriverHelper.closeApplication();
			DriverHelper.launchApplication();

		}

	}

	@AfterClass(alwaysRun = true)
	public void baseTestCleanup() {
		if (initializationStatus.get()) {
			if (null == System.getProperty("noReset")) {
				DriverHelper.resetApplication();
			}
			MDC.remove(TEST_NAME);
			Map<String, String> deviceInfo = new HashMap<>();
			deviceInfo.put(
					"Configuration "
							+ ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_DEVICE_NAME),
					"<a href='" + ConfigHelper.getInstance().getConfigFileLocation()
							+ "' target='_blank'><span class='label info'>Go to configuration file</span></a>");
			ExtentManager.getInstance().addSystemInfo(deviceInfo);
			DriverHelper.captureLogcat(callingTestClassName + "-Thread-id-" + Thread.currentThread().getId());
		} else {
			log.warn("TESTS NOT EVEN STARTED, NO CLEANUP NECESSARY");
		}
	}

}
