package com.endava.appium.framework.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;

import com.endava.appium.framework.helpers.ConfigHelper;

/**
 *	Class that holds all the AppiumServer object attributes
 */
public class CustomAppiumServer {

	private String port;
	private String bp;
	private String chromedriverPort;
	private String logfilePath;
	private String ip;
	private String logLevel = "error:debug";
	private String iosWebkitDebugProxyPort;

	private static final Logger LOG = CustomLogger.INSTANCE.getLogger(CustomAppiumServer.class);

	public CustomAppiumServer() {
		LOG.info("Creating instance of appium server object");
		port = ConfigHelper.getInstance().getAppiumServerCapabilities().getPort();
		if (ConfigHelper.getInstance().getAppiumServerCapabilities().getBootstrapPort() == null) {
			LOG.info("No bootstrap port detected in cfg file. Using default bootstrap port value 4825");
			bp = "4825";
		} else {
			bp = ConfigHelper.getInstance().getAppiumServerCapabilities().getBootstrapPort();
		}
		if (ConfigHelper.getInstance().getAppiumServerCapabilities().getChromeDriverPort() == null) {
			LOG.info("No chromedriverPort detected in cfg file. Using default chromedriverPort value 4925");
			chromedriverPort = "4925";
		} else {
			chromedriverPort = ConfigHelper.getInstance().getAppiumServerCapabilities().getChromeDriverPort();
		}
		if (null == ConfigHelper.getInstance().getAppiumServerCapabilities().getIosWebkitDebugProxyPort()) {
			LOG.warn("iosWebkitDebugProxyPort not found in cfg file, falling back to 27753");
			iosWebkitDebugProxyPort = "27753";
		} else {
			iosWebkitDebugProxyPort = ConfigHelper.getInstance().getAppiumServerCapabilities().getIosWebkitDebugProxyPort();
		}
		LOG.info("No logfilePath detected in cfg file. Using default logfilePath value current directory");
		String sepparator = OsUtils.getOsSepparator();
		DateFormat dateFormat = new SimpleDateFormat(
				"yyyy" + sepparator + "MM" + sepparator + "dd" + sepparator + "HH.mm.ss");
		Date date = new Date();
		String folder = System.getProperty("user.dir") + sepparator + "logs" + sepparator + "appiumLogs" + sepparator;
		logfilePath = folder + String.valueOf(dateFormat.format(date) + Thread.currentThread().getId() + ".txt");
		LOG.info("Appium logs can be found at " + logfilePath);
		if (null != ConfigHelper.getInstance().getAppiumServerCapabilities().getLogLevel()) {
			logLevel = ConfigHelper.getInstance().getAppiumServerCapabilities().getLogLevel();
		}
		ip = ConfigHelper.getInstance().getAppiumServerCapabilities().getIp();
		LOG.info(toString());
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getBootstrapPort() {
		return this.bp;
	}

	public void setBootstrapPort(String bootstrapPort) {
		this.bp = bootstrapPort;
	}

	public String getChromedriverPort() {
		return chromedriverPort;
	}

	public void setChromedriverPort(String chromedriverPort) {
		this.chromedriverPort = chromedriverPort;
	}

	public String getLogfilePath() {
		return logfilePath;
	}

	public void setLogfilePath(String logfilePath) {
		this.logfilePath = logfilePath;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLogLevel() {
		return logLevel;
	}
	
	public String getIosWebkitDebugProxyPort() {
		return iosWebkitDebugProxyPort;
	}
	
	public void setIosWebkitDebugProxyPort(String port) {
		this.iosWebkitDebugProxyPort = port;
	}

	@Override
	public String toString() {
		return "CustomAppiumServer [port=" + port + ", bp=" + bp + ", chromedriverPort=" + chromedriverPort
				+ ", logfilePath=" + logfilePath + ", ip=" + ip + ", logLevel=" + logLevel
				+ ", iosWebkitDebugProxyPort=" + iosWebkitDebugProxyPort + "]";
	}


}
