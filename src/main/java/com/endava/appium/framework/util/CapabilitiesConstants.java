package com.endava.appium.framework.util;

/**
 * CUSTOM CABABILITIES CLASS THAT HOLDS ALL THE DATA NEEDED TO START THE APPIUM DRIVER
 * DATA IS READ BY CONFIGHELPER FROM A JSON CONFIGURATION FILE
 */

public class CapabilitiesConstants {

	/*
	 * SERVER DATA
	 */

	public static final String APPIUM_SERVER_URL = "hubUrl";
	public static final String APPIUM_SERVER_IP = "ip";
	public static final String APPIUM_SERVER_PORT = "port";
	public static final String APPIUM_SERVER_BOOTSTRAP_PORT = "bootstrapPort";
	public static final String APPIUM_SERVER_CHROMEDRIVER_PORT = "chromedriverPort";
	public static final String APPIUM_SERVER_LOG_LEVEL = "logLevel";
	public static final String APPIUM_SERVER_SESSION_OVERRIDE = "sessionOverride";
	public static final String APPIUM_SERVER_IOS_WEBKIT_DEBUG_PROXY_PORT = "iosWebkitDebugProxyPort";

	/*
	 * DRIVER DATA
	 */

	public static final String CAPAB_APP_ACTIVITY = "appActivity";
	public static final String CAPAB_APP_PACKAGE = "appPackage";
	public static final String CAPAB_PLATFORM_NAME = "platformName";
	public static final String CAPAB_PLATFORM_VERSION = "platformVersion";
	public static final String CAPAB_DEVICE_NAME = "deviceName";
	public static final String CAPAB_COMMAND_TIMEOUT = "newCommandTimeout";
	public static final String CAPAB_UDID = "udid";
	public static final String CAPAB_APP = "app";
	public static final String CAPAB_AUTO_LAUNCH = "autoLaunch";
	public static final String CAPAB_BROWSER_NAME = "browserName";
	public static final String CAPAB_BUNDLE_ID = "bundleId";
	public static final String CAPAB_WDA_LOCAL_PORT = "wdaLocalPort";

	/*
	 * OPIUM REMOTE DATA
	 */

	public static final String OPIUM_SERVER_ADDRESS = "address";
	public static final String OPIUM_SEARCH_CRITERIA = "opiumSearchCriteria";

	/*
	 * TESTDROID DATA
	 */
	public static final String TESTDROID_HUBURL = "testdroid_hubUrl";
	public static final String TESTDROID_USERNAME = "testdroid_username";
	public static final String TESTDROID_PASSWORD = "testdroid_password";
	public static final String TESTDROID_TARGET = "testdroid_target";
	public static final String TESTDROID_PROJECT = "testdroid_project";
	public static final String TESTDROID_TESTRUN = "testdroid_testrun";
	public static final String TESTDROID_DEVICE = "testdroid_device";
	public static final String TESTDROID_APP = "testdroid_app";
	public static final String TESTDROID_JUNIT_WAITTIME = "testdroid_junitWaitTime";

	private CapabilitiesConstants() {

	}

}
