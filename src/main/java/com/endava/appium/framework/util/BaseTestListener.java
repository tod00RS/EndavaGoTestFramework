package com.endava.appium.framework.util;

import org.slf4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.SkipException;

import com.endava.appium.framework.helpers.ConfigHelper;
import com.endava.appium.framework.helpers.DriverHelper;

/**
 * Generic test listener used for: - CLEANUP - LOGGING MESSAGES RELATED TO TEST
 * SUITE
 */
public final class BaseTestListener implements ITestListener {

	private static final Logger LOG = CustomLogger.INSTANCE.getLogger(BaseTestListener.class);
	public static final String TEST_NAME = "testname";
	private static final String CONFIG_FILE = "configFile";

	@Override
	public void onFinish(ITestContext testContext) {

		LOG.info("FINISHED TEST RUN " + testContext.getName());
		String totalTestsRun = String.valueOf(testContext.getPassedTests().size() + testContext.getFailedTests().size()
				+ testContext.getSkippedTests().size());
		LOG.info("Number of tests executed was: " + totalTestsRun);
		DriverHelper.killDriver();
		DriverHelper.stopAppiumServer();
		DriverHelper.stopOpium();
		DriverHelper.killXCUITestLeftOvers();

	}

	@Override
	public void onStart(ITestContext testContext) {
		Thread.currentThread().setName(testContext.getName());
		LOG.info("STARTING RUN " + testContext.getName());
		String configFile;
		if (null != System.getProperty(CONFIG_FILE)) {
			LOG.info("-DconfigFile parameter detected. Overriding ANY configFile param present in test suite");
			configFile = System.getProperty(CONFIG_FILE);
		} else {
			LOG.info("Reading configFile param from testng suite");
			configFile = testContext.getCurrentXmlTest().getParameter(CONFIG_FILE);
		}
		LOG.info("Initializing driver connection from config file: " + configFile);
		ConfigHelper.loadConfigData(configFile);
		DriverHelper.initConnection();
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult testResult) {
		// add details here
	}

	@Override
	public void onTestFailure(ITestResult testResult) {
		LOG.warn(String.format("Test Failed. Class Name: %s Test Method Name: %s()",
				testResult.getTestClass().getName(), testResult.getMethod().getMethodName()));

	}

	@Override
	public void onTestSkipped(ITestResult testResult) {
		LOG.warn(String.format("Test Skipped. Class Name: %s Test Method Name: %s()",
				testResult.getTestClass().getName(), testResult.getMethod().getMethodName()));

	}

	@Override
	public void onTestStart(ITestResult testResult) {
		Ignore skipAnnotation = testResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Ignore.class);
		// if @Test has @Ignore annotation, skip the test
		if (null != skipAnnotation) {
			throw new SkipException(skipAnnotation.reason());
		} else {
			LOG.info("STARTING TEST " + testResult.getName());
		}

	}

	@Override
	public void onTestSuccess(ITestResult testResult) {
		LOG.info("Test " + testResult.getName() + " PASSED");
	}

}
