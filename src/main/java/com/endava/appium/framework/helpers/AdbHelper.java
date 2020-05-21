package com.endava.appium.framework.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

import com.endava.appium.framework.util.CapabilitiesConstants;
import com.endava.appium.framework.util.CustomLogger;
import com.endava.appium.framework.util.PlatformConstants;
import com.endava.appium.framework.util.exceptions.AdbException;

/**
 * HELPER CLASS TO INTERACT WITH THE ADB INSTANCE RUNNING ON THE MACHINE
 * ONLY WORKS ON ANDROID RUNS
 * DOES NOT WORK FOR REMOTE EXECTION / CLOUD RUNS
 */

public enum AdbHelper {
	
	INSTANCE;
	/*
	 * GLOBAL VARS for REMOTE ADB COMMAND EXECUTION BOTH VARIABLES HAVE TO BE
	 * SET IN ORDER TO WORK
	 */

	private static final String REMOTE_ADB_HOST = System.getProperty("remoteAdbHost");
	private static final String REMOTE_ADB_PORT = System.getProperty("remoteAdbPort");
	private static final String SHELL = "shell";
	private static final String CLOUD = "cloud";
	private static final String APPIUM_SETTINGS_APP = "io.appium.settings/.Settings";
	private static final String ADB_IN_CLOUD_NOT_SUPPORTED_ERROR = "ADB IS NOT SUPPORTED IN CLOUD/OPIUM RUNS";
	private static final String START = "start";
	private static final String INPUT = "input";

	private static final Logger LOG = CustomLogger.INSTANCE.getLogger(AdbHelper.class);

	/*
	 * ADB_COMMAND_LOCATION has to be set in order to know where to run adb
	 * commands from
	 */
	private static final String ADB_COMMAND_LOCATION = getAdbCommandLocation();   //it's ok
	/*
	 * DEFAULT ADB command used as a prefix for all adb commands
	 */
	private static final List<String> DEFAULT_ADB_COMMAND = getDefaultAdbCommand();
	/*
	 * DEVICE_UDID has to be set in order to execute adb commands to the device
	 * on which tests are running
	 */

	/*
	 * WARNING: UDID MUST BE PRESENT in config file
	 */
	private static boolean packageFound;
	/*
	 * list containing the current command response. Each time a command is run,
	 * the list is reset
	 */
	private static LinkedHashSet<String> currentCommandResponse = new LinkedHashSet<>();

	/**
	 * 
	 * @param pathToApk
	 *            : full path to apk file, including .apk file extension
	 */
	public void installApp(String pathToApk) {
		File f = new File(pathToApk);
		// verify if the source folder exists
		if (!f.isFile()) {
			throw new AdbException(pathToApk + " file not found");
		}
		executeDeviceAdbCommand("install", pathToApk);
	}

	/**
	 * sends the given text to the device using adb shell command
	 * 
	 * @param text
	 */
	public void sendTextToDevice(String text) {
		executeDeviceAdbCommand(SHELL, INPUT, "text", text);
	}

	/**
	 * Returns the installed status of the apk identified by the packageName
	 * 
	 * @param packageName
	 * @return
	 */
	public boolean isAppInstalled(String packageName) {
		boolean isInstalled = false;
		LOG.info("Starting to look for application " + packageName);
		executeDeviceAdbCommand(SHELL, "pm", "list", "packages", packageName);
		if (packageFound) {
			isInstalled = true;
		}
		LOG.info("Lookup finished. Application " + packageName + " found status is " + isInstalled);
		return isInstalled;
	}

	/**
	 * get the activity details of the given activity
	 * 
	 * @param activity
	 * @return
	 */
	public Set<String> getActivityDetails(String activity) {
		LOG.info("Getting details of activity that contains the keyword " + activity);
		executeDeviceAdbCommand(SHELL, "\"dumpsys", "activity", "|", "grep", activity + "\"");
		return currentCommandResponse;
	}

	/**
	 * get details of activity android.intent.action.VIEW
	 * 
	 * @return
	 */
	public Set<String> getViewActivityDetails() {
		return getActivityDetails("android.intent.action.VIEW");
	}

	/**
	 * force stops the app identified by appPackage
	 * 
	 * @param appPackage
	 */
	public void forceStopApplication(String appPackage) {
		LOG.info("Triggering force stop command for application with package name " + appPackage);
		executeDeviceAdbCommand(SHELL, "am", "force-stop", appPackage);
		if (isAppClosed(appPackage)) {
			LOG.info("Successfully force stopped application with package name " + appPackage);
		} else {
			LOG.warn("UNABLE to force stop application with package name " + appPackage);
		}
	}

	/**
	 * Verifies if the application identified by appPackage is closed. It
	 * ignores the occurrence of the app in the Recents activities
	 * 
	 * @param appPackage
	 * @return
	 */
	public boolean isAppClosed(String appPackage) {
		LOG.info(String.format("Checking if application with package %s is closed", appPackage));
		executeDeviceAdbCommand(SHELL, "\"ps", "|", "grep", appPackage, "\"");
		boolean isAppKilled = true;
		for (String s : currentCommandResponse) {
			if (s.contains(appPackage)) {
				isAppKilled = false;
			}
		}
		LOG.info(String.format("Application with package %s closed status is %s", appPackage, isAppKilled));
		return isAppKilled;
	}

	/**
	 * Pushes all files from sourceFolder (from machine DISK) to
	 * destinationFolder (on device sdcard)
	 * 
	 * @param sourceFolder
	 *            the folder of items that will be pushed to device
	 * @param destinationFolder
	 *            the destination folder that will be created on
	 *            /sdcard/Downloads/
	 */
	public void pushFilesToDevice(String sourceFolder, String destinationFolder) {
		File f = new File(sourceFolder);
		// verify if the source folder exists
		if (!f.isDirectory()) {
			throw new AdbException(sourceFolder + " is not a valid folder");

		}
		adbPushContent(sourceFolder, destinationFolder);

	}

	/**
	 * Pushes a single file located by sourceFilePath (from machine DISK) to
	 * destinationFolder (on device sdcard)
	 * 
	 * @param sourceFilePath
	 *            the path for the file that is required to be pushed to the
	 *            device
	 * @param destinationFolder
	 *            the destination folder that will be created on
	 *            /sdcard/Downloads/
	 */
	public void pushFileToDevice(String sourceFilePath, String destinationFolder) {
		File f = new File(sourceFilePath);
		if (!f.isFile()) {
			throw new AdbException(sourceFilePath + " is not a valid file");
		}
		adbPushContent(sourceFilePath, destinationFolder);
	}

	/**
	 * deletes the file at the given path from the device
	 * 
	 * @param pathToFile
	 */

	public void deleteFileFromDevice(String pathToFile) {
		LOG.info("Trying to remove file at " + pathToFile);
		executeDeviceAdbCommand(SHELL, "rm", pathToFile);
		LOG.info("Removed file at " + pathToFile);
	}

	/**
	 * deletes the folder at pathToFolder and all it's contents
	 * 
	 * @param pathToFolder
	 */
	public void deleteFolderFromDevice(String pathToFolder) {
		executeDeviceAdbCommand(SHELL, "rm", "-rf", pathToFolder);
		LOG.info("Removed folder " + pathToFolder);
	}

	/**
	 * sends the current focused app to background by pressing the home button
	 */
	public void sendAppToBackground() {
		executeDeviceAdbCommand(SHELL, INPUT, "keyevent", "3");
	}

	/**
	 * brings to foreground back the app controlled by the activityName from
	 * packageName
	 * 
	 * @param packageName
	 * @param activityName
	 */
	public void bringAppInForeground(String packageName, String activityName) {
		executeDeviceAdbCommand(SHELL, "am", START, packageName + "/" + activityName);
	}

	/**
	 * forces a restart of the application. No resets are done
	 * 
	 * @param packageName
	 * @param activityName
	 */
	public void restartApp(String packageName, String activityName) {
		executeDeviceAdbCommand(SHELL, "am", START, "-n", packageName + "/" + activityName);
	}

	/**
	 * navigates to previous screen by using the android back functionality
	 */
	public void navigateBack() {
		executeDeviceAdbCommand(SHELL, INPUT, "keyevent", "4");
	}

	/**
	 * enables/disables airplane mode
	 * 
	 * @param enable
	 */
	protected void setAirplaneMode(boolean enable) {
		if (enable) {
			executeDeviceAdbCommand(SHELL, "settings", "put", "global", "airplane_mode_on", "1");
			executeDeviceAdbCommand(SHELL, "am", "broadcast", "-a", "android.intent.action.AIRPLANE_MODE", "--ez",
					"state", "true");

		} else {
			executeDeviceAdbCommand(SHELL, "settings", "put", "global", "airplane_mode_on", "0");
			executeDeviceAdbCommand(SHELL, "am", "broadcast", "-a", "android.intent.action.AIRPLANE_MODE", "--ez",
					"state", "false");
		}

	}

	/**
	 * enables/disables wifi for device using the .Settings app from Appium
	 * 
	 * @param enable
	 */
	protected void setWifiModeWithAppium(boolean enable) {
		if (enable) {
			executeDeviceAdbCommand(SHELL, "am", START, "-n", APPIUM_SETTINGS_APP, "-e", "wifi", "on");
		} else {
			executeDeviceAdbCommand(SHELL, "am", START, "-n", APPIUM_SETTINGS_APP, "-e", "wifi", "off");
		}
	}

	/**
	 * enables/disables data connection using the .Settings app from Appium
	 * 
	 * @param enable
	 */
	protected void setDataModeWithAppium(boolean enable) {
		if (enable) {
			LOG.info("set data on");
			executeDeviceAdbCommand(SHELL, "am", START, "-n", APPIUM_SETTINGS_APP, "-e", "data", "on");
		} else {
			LOG.info("set data off");
			executeDeviceAdbCommand(SHELL, "am", START, "-n", APPIUM_SETTINGS_APP, "-e", "data", "off");
		}
	}

	/**
	 * only works on rooted devices
	 * 
	 * @param enable
	 */
	protected void setDataMode(boolean enable) {
		if (enable) {
			executeDeviceAdbCommand(SHELL, "svc", "data", "enable");
		} else {
			executeDeviceAdbCommand(SHELL, "svc", "data", "disable");
		}
	}

	/**
	 * only works on rooted devices
	 * 
	 * @param enable
	 */
	protected void setWifiMode(boolean enable) {
		if (enable) {
			executeDeviceAdbCommand(SHELL, "svc", "wifi", "enable");
		} else {
			executeDeviceAdbCommand(SHELL, "svc", "wifi", "disable");
		}
	}

	/**
	 * push content from sourcePath to device destinationFolder
	 * 
	 * @param sourcePath
	 * @param destinationFolder
	 */
	private void adbPushContent(String sourcePath, String destinationFolder) {
		adbMkdir(destinationFolder);
		String devicePath = "/sdcard/" + destinationFolder + "/";
		executeDeviceAdbCommand("push", sourcePath, devicePath);
	}

	private void adbMkdir(String folderName) {
		String devicePath = "/sdcard/" + folderName;
		executeDeviceAdbCommand(SHELL, "mkdir", devicePath);
	}

	/**
	 * runs the given cmd line instruction
	 * 
	 * @param processBuilder
	 */
	private static void runProcess(ProcessBuilder processBuilder) {
		if (!ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_PLATFORM_NAME)
				.equals(PlatformConstants.PLATFORM_ANDROID)) {
			throw new AdbException("ADB COMMANDS CAN ONLY BE RUN ON ANDROID PLATFORM");
		}
		Process process = null;
		try {
			// start the process
			LOG.info("Running command " + processBuilder.command());
			String message;
			processBuilder.redirectErrorStream(true);
			process = processBuilder.start();
			if (currentCommandResponse != null) {
				currentCommandResponse.clear();
			}

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			LinkedHashSet<String> outputMessages = new LinkedHashSet<>();
			while ((message = bufferedReader.readLine()) != null) {

				if (!message.isEmpty()) {
					outputMessages.add(message);
					LOG.info("Command output " + message);
				}
			}
			currentCommandResponse = outputMessages;
			// parse the output
			parseOutput(outputMessages);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		try {
			// wait for the process to finish
			if (null != process) {
				process.waitFor();
			} else {
				throw new AdbException("Cannot wait for process. Process may be null.");
			}
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
			Thread.currentThread().interrupt();
		}
	}

	private static void parseOutput(LinkedHashSet<String> output) {
		for (String s : output) {
			LOG.info("Parsing output " + s);
			// fail if errors or unexpected failures are seen in the reponse
			if (s.contains("error") && (!s.contains("returns"))) {
				throw new AdbException(s);
			}

			if (s.contains("failed") && (!s.contains("File exists"))) {
				throw new AdbException(s);
			}

			if (s.contains("exists")) {
				LOG.warn("Skipping folder creation. Reason : " + s);
			}
			// if adb packages command was run, set the packageFound
			// variable
			if (s.matches("package:(.*?)")) {
				LOG.info(s + " found");
				packageFound = true;
			}

		}
	}

	/**
	 * checks that ANDROID_HOME is set, and then goes to
	 * %ANDROID_HOME%/platform-tools/adb to use the adb program
	 * 
	 * @return
	 */
	private static String getAdbCommandLocation() {
		String adbLocation = "";
		if ((null == System.getProperty(CLOUD)) && (null == System.getProperty("opium"))) {
			if (ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_PLATFORM_NAME)
					.equals(PlatformConstants.PLATFORM_ANDROID)) {

				LOG.info("Trying to set ADB command");
				LOG.info(
						"If the tests are run remotely (ex: from Jenkins), please set the maven parameter -DadbLocation "
								+ "to point explicitly to the path of the adb from the machine that will run the tests");
				LOG.info("Example: <PATH_TO_ANDROID_HOME>/platform-tools/adb");
				// if the tests are run from Jenkins (or other location) then
				// the adb location cannot be the system location
				// set the adbCommandLocation to the path of the machine that
				// runs the test
				String remoteAdbLocation = System.getProperty("adbLocation");
				if (remoteAdbLocation != null) {
					LOG.info("REMOTE RUN. SETTING ADB LOCATION TO EXPLICIT LOCATION " + remoteAdbLocation);
					adbLocation = remoteAdbLocation;
				} else {
					LOG.info("LOCAL RUN. SETTING ADB LOCATION TO IMPLICIT SYSTEM LOCATION");
					verifyAndroidHomeSystemVar();
					adbLocation = System.getenv("ANDROID_HOME") + "/platform-tools/adb";			//it's ok
				}
				LOG.info("ADB command set successfully to: " + adbLocation);
				// build the desired paths
			} else {
				LOG.info("No need to set ADB for ios runs");
			}
		} else {
			LOG.warn(ADB_IN_CLOUD_NOT_SUPPORTED_ERROR);
		}
		return adbLocation;
	}

	private static void verifyAndroidHomeSystemVar() {
		// verify that ANDROID_HOME is set and/or Eclipse was started from
		// terminal on MAC
		String adbHome = System.getenv("ANDROID_HOME");
		if (adbHome == null) {
			throw new AdbException("Please set ANDROID_HOME system var and/or start IDE from Terminal if on MAC");
		}
	}

	/**
	 * executes an adb command on the device UDID
	 * 
	 * @param commandParams
	 */
	private static void executeDeviceAdbCommand(String... commandParams) {
		if (System.getProperty(CLOUD) == null) {
			List<String> adbInstruction = new ArrayList<>(DEFAULT_ADB_COMMAND);
			adbInstruction.add("-s");
			adbInstruction.add((String) ConfigHelper.getInstance().getCapability("udid"));
			executeCommand(adbInstruction, commandParams);
		} else {
			throw new AdbException(ADB_IN_CLOUD_NOT_SUPPORTED_ERROR);
		}

	}

	/**
	 * executes ANY generic command prompt instruction
	 * 
	 * @param cmdInstruction
	 * @param commandParams
	 */
	private static void executeCommand(List<String> cmdInstruction, String... commandParams) {
		for (String s : commandParams) {
			cmdInstruction.add(s);
		}
		ProcessBuilder processBuilder = new ProcessBuilder(cmdInstruction);
		runProcess(processBuilder);
	}

	/**
	 * builds the default adb command first get the adb location then, if
	 * necessary, add remote info
	 * 
	 * @return
	 */
	private static List<String> getDefaultAdbCommand() {
		List<String> defaultAdbCommand = new ArrayList<>();
		defaultAdbCommand.add(ADB_COMMAND_LOCATION);
		if (REMOTE_ADB_HOST != null) {
			LOG.info("Adding remote adb host information to adb command");
			addRemoteInfo(defaultAdbCommand);
			LOG.info("Remote adb host is: " + REMOTE_ADB_HOST + " . Remote adb port is: " + REMOTE_ADB_PORT);
		}
		return defaultAdbCommand;
	}

	/**
	 * add remote information to the adb command
	 * 
	 * @param adbInstruction
	 */
	private static void addRemoteInfo(List<String> adbInstruction) {
		if (REMOTE_ADB_PORT != null) {
			adbInstruction.add("-H");
			adbInstruction.add(REMOTE_ADB_HOST);
			adbInstruction.add("-P");
			adbInstruction.add(REMOTE_ADB_PORT);
		} else {
			throw new AdbException("Please provide both remoteAdbHost and remoteAdbPort parameters");
		}
	}

}
