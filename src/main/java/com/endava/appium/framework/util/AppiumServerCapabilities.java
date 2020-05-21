package com.endava.appium.framework.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.slf4j.Logger;

import com.endava.appium.framework.helpers.ConfigHelper;
import com.endava.appium.framework.util.exceptions.AppiumServerCapabilitiesException;

/**
 * CUSTOM CABABILITIES CLASS THAT HOLDS ALL THE DATA NEEDED TO START THE APPIUM SERVER
 * DATA IS READ BY CONFIGHELPER FROM A JSON CONFIGURATION FILE
 */
public class AppiumServerCapabilities {

	private static final Logger LOG = CustomLogger.INSTANCE.getLogger(AppiumServerCapabilities.class);

	private String ip;
	private String port;
	private String bootstrapPort;
	private String chromeDriverPort;
	private String logLevel;
	private String sessionOverride;
	private String hubUrl;
	private String iosWebkitDebugProxyPort;

	public AppiumServerCapabilities(JSONObject json) {
		this.ip = (String) json.get(CapabilitiesConstants.APPIUM_SERVER_IP);
		this.port = (String) json.get(CapabilitiesConstants.APPIUM_SERVER_PORT);
		this.bootstrapPort = (String) json.get(CapabilitiesConstants.APPIUM_SERVER_BOOTSTRAP_PORT);
		this.chromeDriverPort = (String) json.get(CapabilitiesConstants.APPIUM_SERVER_CHROMEDRIVER_PORT);
		this.logLevel = (String) json.get(CapabilitiesConstants.APPIUM_SERVER_LOG_LEVEL);
		this.sessionOverride = (String) json.get(CapabilitiesConstants.APPIUM_SERVER_SESSION_OVERRIDE);
		this.iosWebkitDebugProxyPort = (String) json.get(CapabilitiesConstants.APPIUM_SERVER_IOS_WEBKIT_DEBUG_PROXY_PORT);
		verifyMandatoryDataIsPresent();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getBootstrapPort() {
		return bootstrapPort;
	}

	public void setBootstrapPort(String bootstrapPort) {
		this.bootstrapPort = bootstrapPort;
	}

	public String getChromeDriverPort() {
		return chromeDriverPort;
	}

	public void setChromeDriverPort(String chromeDriverPort) {
		this.chromeDriverPort = chromeDriverPort;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getSessionOverride() {
		return sessionOverride;
	}

	public void setSessionOverride(String sessionOverride) {
		this.sessionOverride = sessionOverride;
	}
	
	public String getIosWebkitDebugProxyPort() {
		return iosWebkitDebugProxyPort;
	}
	
	public void setIosWebkitDebugProxyPort(String port){
		this.iosWebkitDebugProxyPort = port;
	}

	public URL getHubUrl() {
		URL url = null;
		if (null == System.getProperty(PlatformConstants.CLOUD)) {
			hubUrl = "http://" + this.getIp() + ":" + this.getPort() + "/wd/hub";
		} else {
			LOG.info("Cloud run. Setting hubUrl manually from config file");
			hubUrl = ConfigHelper.getInstance().getTestdroidCapabilities().getTestdroidHubUrl();
		}
		try {
			url = new URL(hubUrl);
		} catch (MalformedURLException e) {
			LOG.error(e.getMessage());
		}
		return url;
	}
	
	private void verifyMandatoryDataIsPresent() {
		if (CommonUtils.notNull(this.ip, this.port, this.bootstrapPort)) {
			LOG.info("Mandatory APPIUM SERVER CAPABILTIES data verification PASSED");
		} else {
			throw new AppiumServerCapabilitiesException("IP, PORT AND BOOTSTRAPPORT ARE MANDATORY FOR APPIUM SERVER TO START");
		}
	}

	@Override
	public String toString() {
		return "AppiumServerCapabilities [ip=" + ip + ", port=" + port + ", bootstrapPort=" + bootstrapPort
				+ ", chromeDriverPort=" + chromeDriverPort + ", logLevel=" + logLevel + ", sessionOverride="
				+ sessionOverride + "]";
	}

}
