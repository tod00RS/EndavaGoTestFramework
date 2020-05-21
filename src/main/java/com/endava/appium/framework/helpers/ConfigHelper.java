package com.endava.appium.framework.helpers;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;

import com.endava.appium.framework.util.AppiumServerCapabilities;
import com.endava.appium.framework.util.CustomLogger;
import com.endava.appium.framework.util.OpiumCapabilities;
import com.endava.appium.framework.util.OsUtils;
import com.endava.appium.framework.util.PlatformConstants;
import com.endava.appium.framework.util.TestdroidCapabilities;
import com.endava.appium.framework.util.exceptions.ConfigFileNotFoundException;

/**
 * HELPER CLASS THAT DOES THE INITIAL SETUP OF THE TEST RUN(s):
 * 	- READS CONFIGURATION FILE(s)
 * 	- SETS UP THE CAPABILITY OBJECTS THAT WILL BE USED TO CREATE SERVERS/DRIVERS
 */
public class ConfigHelper {
	
	private static final Logger LOG = CustomLogger.INSTANCE.getLogger(ConfigHelper.class);

	private AppiumServerCapabilities appiumServerCapabilities;
	private DesiredCapabilities desiredCapabilities;
	private OpiumCapabilities opiumCapabilities;
	private TestdroidCapabilities testdroidCapabilities;

	private static final String JSON_PARSE_ERROR_MESSAGE = "Error parsing config file. Please make sure that it has a proper json format";

	private String configFileLocation = "";

	/*
	 * INSTANTIATION
	 */

	private static final ThreadLocal<ConfigHelper> localConfigHelper = new ThreadLocal<ConfigHelper>() {
		@Override
		protected ConfigHelper initialValue() {
			return new ConfigHelper();
		}
	};

	/*
	 * METHODS
	 */

	private static ConfigHelper getThreadInstance() {
		return localConfigHelper.get();
	}

	public static ConfigHelper getInstance() {
		return getThreadInstance();
	}

	public DesiredCapabilities getDesiredCapabilities() {
		return this.desiredCapabilities;
	}

	public AppiumServerCapabilities getAppiumServerCapabilities() {
		return this.appiumServerCapabilities;
	}

	public OpiumCapabilities getOpiumCapabilities() {
		return this.opiumCapabilities;
	}

	public TestdroidCapabilities getTestdroidCapabilities() {
		return this.testdroidCapabilities;
	}

	public String getConfigFileLocation() {
		return configFileLocation;
	}

	public Object getCapability(String capabilityName) {
		return this.desiredCapabilities.getCapability(capabilityName);
	}

	public URL getHubUrl() {
		return getAppiumServerCapabilities().getHubUrl();
	}

	public void setCapability(String capabilityName, String value) {
		this.desiredCapabilities.setCapability(capabilityName, value);
	}

	/*
	 * CONFIG DATA LOADERS
	 */

	public static synchronized void loadConfigData(String fileName) {
		if ((System.getProperty(PlatformConstants.CLOUD) != null) && (System.getProperty("serverside") != null)) {
			// server side cloud runs do not need config files
			// config is done by the cloud
			LOG.info("Serverside cloud run detected. Skipping config file load");
		} else {
			loadConfigFromJsonFile(fileName, getThreadInstance());
		}

	}

	public static synchronized void loadConfigFromJsonFile(String fileName, ConfigHelper configHelper) {
		File configFile = FileUtils.getFile(System.getProperty("user.dir") + OsUtils.getOsSepparator() + fileName);
		if (!configFile.isFile()) {
			throw new ConfigFileNotFoundException("Config file " + configFile.getAbsolutePath() + " does not exist");
		} else {
			configHelper.configFileLocation = configFile.getAbsolutePath();
			LOG.info("Loading config data from config file: " + fileName + " at location " + configFile.getAbsolutePath());
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject = (JSONObject) parser.parse(new FileReader(configFile.getAbsoluteFile()));

				JSONObject appiumServerCapabilities = (JSONObject) jsonObject.get("appiumServerCapabilities");
				configHelper.appiumServerCapabilities = new AppiumServerCapabilities(appiumServerCapabilities);
				LOG.info(configHelper.appiumServerCapabilities.toString());

				JSONObject desiredCapabilities = (JSONObject) jsonObject.get("desiredCapabilities");
				configHelper.desiredCapabilities = createDesiredCapabilities(desiredCapabilities);
				LOG.info(configHelper.desiredCapabilities.toString());

				if (null != System.getProperty("opium")) {
					JSONObject opiumCapabilities = (JSONObject) jsonObject.get("opiumCapabilities");
					configHelper.opiumCapabilities = new OpiumCapabilities(opiumCapabilities);
					LOG.info(configHelper.opiumCapabilities.toString());
				}

				if (null != System.getProperty(PlatformConstants.CLOUD)) {
					JSONObject testdroidCapabilities = (JSONObject) jsonObject.get("testdroidCapabilities");
					configHelper.testdroidCapabilities = new TestdroidCapabilities(testdroidCapabilities);
					LOG.info(configHelper.testdroidCapabilities.toString());
					updateDesiredCapabilities(configHelper.getDesiredCapabilities(), testdroidCapabilities);
					LOG.info(configHelper.desiredCapabilities.toString());
				}

			} catch (Exception e) {
				LOG.error(JSON_PARSE_ERROR_MESSAGE);
				LOG.error(e.getMessage(), e);
			}
		}
	}

	private static DesiredCapabilities createDesiredCapabilities(JSONObject json) {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		Set<?> jsonCustomCapabilities = json.entrySet();
		for (Object o : jsonCustomCapabilities) {
			String[] jsonObjects = o.toString().split("=");
			capabilities.setCapability(jsonObjects[0], jsonObjects[1]);
		}
		return capabilities;
	}

	private static DesiredCapabilities updateDesiredCapabilities(DesiredCapabilities capabilities, JSONObject json) {
		Set<?> jsonCustomCapabilities = json.entrySet();
		for (Object o : jsonCustomCapabilities) {
			String[] jsonObjects = o.toString().split("=");
			capabilities.setCapability(jsonObjects[0], jsonObjects[1]);
		}
		return capabilities;
	}

}
