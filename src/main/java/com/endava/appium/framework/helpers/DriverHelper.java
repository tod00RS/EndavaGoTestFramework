package com.endava.appium.framework.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntry;
import org.slf4j.Logger;
import org.slf4j.MDC;

import com.endava.appium.framework.util.CapabilitiesConstants;
import com.endava.appium.framework.util.CustomLogger;
import com.endava.appium.framework.util.LocalAppiumServer;
import com.endava.appium.framework.util.OsUtils;
import com.endava.appium.framework.util.PlatformConstants;
import com.endava.appium.framework.util.SearchCriteria;
import com.endava.appium.framework.util.exceptions.OpiumException;
import com.endava.appium.framework.util.exceptions.ProcessRunException;
import com.endava.appium.framework.util.exceptions.RunParametersConflictException;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;

/**
 * HELPER CLASS THAT HANDLES THE APPIUM SERVER/DRIVER CONNECTION:
 *  - START/STOP APPIUM SERVER
 *  - START/STOP APPIUM DRIVER
 *  - SIGNALS OPIUMHELPER TO START/STOP OPIUM DRIVER (FOR REMOTE EXECUTION)
 *  - START/STOP APPLICATION
 *  - GET THE SINGLETON INSTANCE OF THE DRIVER THAT CONTROLS THE RUN
 */

/**
 * INIT > START SERVER (OPTIONAL) > CREATE DRIVER INSTANCE
 */

public class DriverHelper {
	private static final Logger LOG = CustomLogger.INSTANCE.getLogger(DriverHelper.class);
	private static final String LOGCAT_NOT_ENABLED_ERROR = "Logcat logging is not enabled. To enable please use maven param -Dlogcat";
	private static final String LOGCAT = "logcat";

	/**
	 * appium server initialization
	 */
	private static final ThreadLocal<AppiumDriverLocalService> localServer = new ThreadLocal<AppiumDriverLocalService>() {
		@Override
		protected AppiumDriverLocalService initialValue() {
			return LocalAppiumServer.startLocalAppiumServer();
		}
	};

	private static final ThreadLocal<IOSDriver<WebElement>> localIOSDriver = new ThreadLocal<IOSDriver<WebElement>>() {
		@Override
		protected IOSDriver<WebElement> initialValue() {
			return new IOSDriver<>(ConfigHelper.getInstance().getHubUrl(),
					ConfigHelper.getInstance().getDesiredCapabilities());
		}
	};

	private static final ThreadLocal<AndroidDriver<WebElement>> localAndroidDriver = new ThreadLocal<AndroidDriver<WebElement>>() {
		@Override
		protected AndroidDriver<WebElement> initialValue() {
			return new AndroidDriver<>(ConfigHelper.getInstance().getHubUrl(),
					ConfigHelper.getInstance().getDesiredCapabilities());
		}
	};

	private DriverHelper() {

	}

	/*
	 * INITIALIZATION PHASE
	 */

	/**
	 * 
	 * @param configFile
	 *            - mandatory way to start tests maven params / no config
	 *            removed - useless cloud runs should function in the exact same
	 *            way
	 */

	public static synchronized void initConnection() {
		// start appium server from code
		String startServerFromCode = System.getProperty("server");
		String startOnOpium = System.getProperty("opium");

		if ((null != startOnOpium) && (null != startServerFromCode)) {
			throw new RunParametersConflictException("EITHER -Dopium or -Dserver CAN BE USED, NOT BOTH");
		}
		// start local server
		if (null != startServerFromCode) {
			if ((System.getProperty("cloud") != null) && (System.getProperty("serverside") == null)) {
				throw new RunParametersConflictException("NON SERVER-SIDE CLOUD RUNS SHOULD NOT START SERVER FROM CODE. PLEASE REMOVE THE -Dserver PARAM/nIN CASE YOU WISH TO RUN SERVERSIDE ON CLOUD, PLEASE SET THE -Dcloud and -Dserverside PARAMS");
			} else {
				startAppiumServer();
			}
		}

		// start remote opium server

		if (null != startOnOpium) {
			if (null != ConfigHelper.getInstance().getOpiumCapabilities().getOpiumSearchCriteria()) {
				LOG.info("opiumSearchCriteria detected. Starting opium server with CUSTOM SEARCH CRITERIA");
				startOpium(ConfigHelper.getInstance().getOpiumCapabilities().getOpiumSearchCriteria());
			} else {
				LOG.info("No opiumSearchCriteria detected. Starting opium server with DEFAULT SEARCH CRITERIA");
				startOpium();
			}
		}

		// init
		DriverHelper.createNewDriver();

	}

	/**
	 * Create a new driver instances based on given URL and Capabilities Driver
	 * type determined by platform name inside config helper
	 * 
	 * @param url
	 * @param capabilities
	 */
	private static synchronized void createNewDriver() {
		// for testdroid cloud may need to create their own instances of driver
		try {
			if (ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_PLATFORM_NAME)
					.equals(PlatformConstants.PLATFORM_ANDROID)) {
				getAndroidDriver();
				// running get() command for logcat in order to reset all
				// previous data
				if (System.getProperty(LOGCAT) != null) {
					LOG.info("Logcat enabled. Clearing old logs");
					getAndroidDriver().manage().logs().get(LOGCAT);
				} else {
					LOG.warn(LOGCAT_NOT_ENABLED_ERROR);
					LOG.trace(LOGCAT_NOT_ENABLED_ERROR);
				}
			} else if (ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_PLATFORM_NAME)
					.equals(PlatformConstants.PLATFORM_IOS)) {
				getIosDriver();
			}
			LOG.info("Created new driver with session id: " + getDriver().getSessionId());

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

	}

	/*
	 * APPIUM SERVER
	 */

	/**
	 * @return Thread instance of Appium Server
	 */
	private static AppiumDriverLocalService getServerInstance() {
		return localServer.get();
	}

	private static synchronized void startAppiumServer() {
		if (null == System.getProperty("cloud") && (null == System.getProperty("remote"))) {
			killAllAppiumProcesses();
			killXCUITestLeftOvers();
		} else {
			LOG.info("Killing node process is not necessary for cloud or remote runs");
		}
		getServerInstance();
	}

	public static void stopAppiumServer() {
		if (System.getProperty("server") != null) {
			LOG.info("-------------STOPPING APPIUM SERVER------------" + getServerInstance().getUrl());
			getServerInstance().stop();
			LOG.info("-------------APPIUM SERVER STOPPED------------");
			killAllAppiumProcesses();
		}
	}

	private static synchronized void startOpium() {
		String androidVersion = (String) ConfigHelper.getInstance().getDesiredCapabilities()
				.getCapability(CapabilitiesConstants.CAPAB_PLATFORM_VERSION);
		SearchCriteria sc = new SearchCriteria.SearchCriteriaBuilder().withAndroidVersion(androidVersion).build();

		startOpium(sc);

	}

	private static synchronized void startOpium(SearchCriteria sc) {
		try {
			JSONObject opiumTarget = OpiumHelper.INSTANCE.queryForDevice(sc);
			ConfigHelper.getInstance().getAppiumServerCapabilities().setIp(opiumTarget.getString("ip"));
			OpiumHelper.INSTANCE.startAppiumServerForDevice(opiumTarget);

		} catch (IOException e) {
			throw new OpiumException(e);
		}

	}

	public static synchronized void stopOpium() {
		if (null != getDriver() && (null != System.getProperty("opium"))) {
			try {
				OpiumHelper.INSTANCE.stopAppiumServerForDevice();
			} catch (IOException e) {
				LOG.error(e.getLocalizedMessage(), e);
			}
		}
	}

	/**
	 * Kill node process to avoid port is in use error
	 */
	private static synchronized void killAllAppiumProcesses() {
		String port = ConfigHelper.getInstance().getAppiumServerCapabilities().getPort();
		LOG.info("Killing any leftover open appium process at port " + port);
		ProcessBuilder pb = getProcessBuilderForProcess(port);
		ArrayList<String> pids = getPidsInProcessBuilderForProcessesThatContain(pb, "node");
		killProcesses(pids);
	}

	public static synchronized void killXCUITestLeftOvers() {
		if (PlatformConstants.PLATFORM_IOS
				.equals(ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_PLATFORM_NAME))) {
			killIproxy();
			killXcodeBuild();
		}
	}

	private static synchronized void killIproxy() {
		LOG.info("Killing any leftover open iproxy processes");
		String wdaLocalPort = (String) ConfigHelper.getInstance().getDesiredCapabilities()
				.getCapability(CapabilitiesConstants.CAPAB_WDA_LOCAL_PORT);
		if (null == wdaLocalPort || "".equals(wdaLocalPort)) {
			LOG.warn("wdaLocalPort was not set in the configFile. Falling back to default port value of 8100");
			wdaLocalPort = "8100";
		}
		ProcessBuilder pb = getProcessBuilderForProcess(wdaLocalPort);
		ArrayList<String> pids = getPidsInProcessBuilderForProcessesThatContain(pb, "iproxy", wdaLocalPort);
		killProcesses(pids);
	}

	private static synchronized void killXcodeBuild() {
		LOG.info("Killing any leftover open xcodebuild processes");
		ProcessBuilder pb = getProcessBuilderForProcess("xcodebuild");
		ArrayList<String> pids = getPidsInProcessBuilderForProcessesThatContain(pb, "xcodebuild", (String) ConfigHelper
				.getInstance().getDesiredCapabilities().getCapability(CapabilitiesConstants.CAPAB_UDID));
		killProcesses(pids);
	}

	private static ProcessBuilder getProcessBuilderForProcess(String processIdentifier) {
		// init the processbuilder
		ProcessBuilder pb = new ProcessBuilder("echo hello world");
		LOG.info("Getting process builder for process containing: " + processIdentifier);
		if (OsUtils.isMac()) {
			// search for all processes running that contain processIdentifier
			pb = new ProcessBuilder("/bin/sh", "-c", "ps -e | grep " + processIdentifier + " | grep -v grep");
		}
		if (OsUtils.isWindows()) {
			// search for all processes running that contain processIdentifier
			pb = new ProcessBuilder("cmd.exe", "/c", "netstat -a -n -o | findstr " + processIdentifier);
		}
		return pb;
	}

	private static ArrayList<String> getPidsInProcessBuilderForProcessesThatContain(ProcessBuilder pb,
			String... searchTerms) {
		ArrayList<String> pids = new ArrayList<>();
		Process p;
		String message;
		try {
			p = pb.start();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((message = bufferedReader.readLine()) != null) {
				// filter only the lines that are linked to searchTerms
				// processes
				String pid;
				if (message.isEmpty()) {
					break;
				} else {
					pid = message.trim().substring(message.trim().lastIndexOf(' '), message.trim().length()).trim();
				}
				if (OsUtils.isMac() && (areAllTermsPresent(message, searchTerms))) {
					// add the pids to the list for future murdering
					LOG.info("Killing process " + message);
					pids.add(message.trim().substring(0, message.trim().indexOf(' ')));
				}
				if (OsUtils.isWindows() && (!message.contains("Java")) && !"0".equals(pid)) {

					LOG.info("Killing process " + message);
					// add the pids to the list for future murdering
					pids.add(pid);
				}
			}
			p.waitFor();
		} catch (Exception e) {
			throw new ProcessRunException(e);
		}
		return pids;
	}

	private static Boolean areAllTermsPresent(String message, String... searchTerms) {
		boolean allSearchTermsFound = true;
		for (String s : searchTerms) {
			if (!message.contains(s)) {
				allSearchTermsFound = false;
			}
		}
		return allSearchTermsFound;
	}

	private static void killProcesses(ArrayList<String> pids) {
		// init the processbuilder
		ProcessBuilder pb = new ProcessBuilder("echo hello world");
		Process p;
		for (String pid : pids) {
			// loop through all the pids and murder each one
			if (OsUtils.isMac()) {
				pb = new ProcessBuilder("/bin/sh", "-c", "kill -9 " + pid);
			}
			if (OsUtils.isWindows()) {
				pb = new ProcessBuilder("cmd.exe", "/c", "taskkill /F /PID " + pid);
			}
			try {
				p = pb.start();
				p.waitFor();
				LOG.info("Killed process " + pid);
			} catch (Exception e) {
				throw new ProcessRunException(e);
			}
		}

		if (!pids.isEmpty()) {
			LOG.info("Successfully killed " + pids.size() + " processes");
		} else {
			LOG.info("Nothing to kill.");
		}
	}

	/*
	 * DRIVER INSTANCES
	 */

	public static AppiumDriver<WebElement> getDriver() {
		if (ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_PLATFORM_NAME)
				.equals(PlatformConstants.PLATFORM_ANDROID)) {
			return (AppiumDriver<WebElement>) localAndroidDriver.get();
		} else if (ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_PLATFORM_NAME)
				.equals(PlatformConstants.PLATFORM_IOS)) {
			return (AppiumDriver<WebElement>) localIOSDriver.get();
		} else
			return null;
	}

	public static AndroidDriver<WebElement> getAndroidDriver() {
		return localAndroidDriver.get();
	}

	public static IOSDriver<WebElement> getIosDriver() {
		return localIOSDriver.get();
	}

	/*
	 * DRIVER METHODS
	 */

	public static void startActivity(String appPackage, String appActivity) {
		localAndroidDriver.get().startActivity(appPackage, appActivity);
	}

	public static void launchApplication() {
		if (PlatformConstants.PLATFORM_IOS
				.equals(ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_PLATFORM_NAME))) {
			LOG.info("Launching application "
					+ ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_BUNDLE_ID));
		}
		if (PlatformConstants.PLATFORM_ANDROID
				.equals(ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_PLATFORM_NAME))) {
			LOG.info("Launching application "
					+ ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_APP_ACTIVITY));
		}
		getDriver().launchApp();
	}

	public static void closeApplication() {
		if (null != getDriver()) {

			if (System.getProperty("debug") != null) {
				LOG.warn(
						"Debugging run detected. Application will not close in order to have app set at last screen in test.");
			} else {
				LOG.info("Closing application " + getTestApplicationName());
				getDriver().closeApp();
			}

			LOG.info("Successfully closed application");
		} else {
			LOG.error("Cannot close app, driver has not been initialized");
		}
	}

	public static void killDriver() {
		if (null != getDriver()) {
			LOG.info("Killing driver with SessionId: " + getDriver().getSessionId());
			getDriver().quit();
		}
	}

	public static void resetApplication() {
		if (null != getDriver()) {

			LOG.info("Resetting activity " + getTestApplicationName());
			getDriver().resetApp();
			LOG.info("Successfully reset " + getTestApplicationName());
		} else {
			LOG.error("Cannot reset app, driver has not been initialized");
		}
	}

	private static String getTestApplicationName() {
		String testApplicationName;
		if (!"".equals(getDriver().getCapabilities().getCapability(CapabilitiesConstants.CAPAB_BROWSER_NAME))
				&& (null != getDriver().getCapabilities().getCapability(CapabilitiesConstants.CAPAB_BROWSER_NAME))) {
			testApplicationName = (String) getDriver().getCapabilities()
					.getCapability(CapabilitiesConstants.CAPAB_BROWSER_NAME);
		} else {
			if (ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_PLATFORM_NAME)
					.equals(PlatformConstants.PLATFORM_ANDROID)) {
				testApplicationName = (String) getDriver().getCapabilities()
						.getCapability(CapabilitiesConstants.CAPAB_APP_ACTIVITY);
			} else {
				testApplicationName = (String) getDriver().getCapabilities()
						.getCapability(CapabilitiesConstants.CAPAB_BUNDLE_ID);
			}
		}
		return testApplicationName;
	}

	public static void uninstallApp(String appPackageName) {
		LOG.info("Trying to uninstall " + appPackageName);
		if (getDriver().isAppInstalled(appPackageName)) {
			getDriver().removeApp(appPackageName);
			LOG.info("Successfully uninstalled " + appPackageName);
		} else {
			LOG.info(appPackageName + " was not found. Skipping uninstall");
		}

	}

	public static void captureLogcat(String testClassName) {
		// get logcat data and dump it at DEBUG level for the proper appender to
		// catch it
		// logcat can only be fetched from native view
		if (ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_PLATFORM_NAME)
				.equals(PlatformConstants.PLATFORM_ANDROID)) {
			if (System.getProperty(LOGCAT) != null) {
				MDC.put("testname", testClassName + ".logcat");
				LOG.info("Capturing logcat logs");
				localAndroidDriver.get().context("NATIVE_APP");
				List<LogEntry> logEntries = localAndroidDriver.get().manage().logs().get(LOGCAT).getAll();
				for (LogEntry l : logEntries) {
					LOG.debug(l.getMessage());
				}
				LOG.info("Logcat logs captured");
				MDC.remove("testname");
			} else {
				LOG.warn(LOGCAT_NOT_ENABLED_ERROR);
				LOG.debug(LOGCAT_NOT_ENABLED_ERROR);
			}
		}

	}
}