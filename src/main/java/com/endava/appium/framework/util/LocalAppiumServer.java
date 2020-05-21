package com.endava.appium.framework.util;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;

import com.endava.appium.framework.helpers.ConfigHelper;
import com.endava.appium.framework.util.exceptions.AppiumServerFileNotFoundException;
import com.endava.appium.framework.util.exceptions.AppiumServerNotStartedException;
import com.endava.appium.framework.util.exceptions.EnvironmentVariableException;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.AndroidServerFlag;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.appium.java_client.service.local.flags.IOSServerFlag;

public abstract class LocalAppiumServer {

	private static final Logger LOG = CustomLogger.INSTANCE.getLogger(LocalAppiumServer.class);
//	private static final File nodeExecutable = new File(
//			System.getenv("NODE_HOME") + OsUtils.getOsSepparator() + OsUtils.getNodeExecutable());
	private static final File nodeExecutable = new File(
		"C:/nodejs/node.exe");
	private static final File appiumJs = new File(System.getenv("APPIUM_HOME") + OsUtils.getOsSepparator() + "build"
			+ OsUtils.getOsSepparator() + "lib" + OsUtils.getOsSepparator() + "main.js");
//private static final File appiumJs = new File("C:/Users/jovan.penic/AppData/Roaming/npm/node_modules/appium/build/lib/main.js");

	private LocalAppiumServer() {

	}

	public static AppiumDriverLocalService startLocalAppiumServer() {
		verifyEnvVars();
		AppiumDriverLocalService server;
		CustomAppiumServer customServer = new CustomAppiumServer();
		LOG.info("Detected request to start appium server from code");
		server = buildService(customServer);
		if (null != server) {
			LOG.info("-------------STARTING APPIUM SERVER------------");
			ExecutionTimer.start();
			server.start();
			ExecutionTimer.stop();
			LOG.info(String.format("-------------APPIUM SERVER STARTED after %s seconds------------",
					ExecutionTimer.exactDuration()));
			LOG.info(String.format(
					"Appium server running for device with UDID=%s at %s using bootstrap port=%s and chromedriverport=%s",
					ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_UDID),
					ConfigHelper.getInstance().getHubUrl(), customServer.getBootstrapPort(),
					customServer.getChromedriverPort()));
		} else {
			try {
				throw new AppiumServerNotStartedException("Appium server not started. Server object was null");
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				throw e;
			}
		}

		return server;
	}

	private static AppiumDriverLocalService buildService(CustomAppiumServer customServer) {
		LOG.info("-------------SETTING UP APPIUM SERVER PARAMETERS------------");
		File serverLogs = new File(customServer.getLogfilePath());
		serverLogs.getParentFile().mkdirs();
		boolean serverLogFileCreated = false;
		try {
			serverLogFileCreated = serverLogs.createNewFile();
		} catch (IOException ioex) {
			LOG.error(ioex.getMessage(), ioex);
		}
		if (!serverLogFileCreated) {
			LOG.error("Fail to create appium server logfile");
		}
		/*
		 * check that the necessary system files are present and usable
		 */
		if (!nodeExecutable.isFile()) {
			throw new AppiumServerFileNotFoundException("Node executable not found");
		}
		LOG.info("Using NODE file located at " + nodeExecutable.getAbsolutePath());
		if (!appiumJs.isFile()) {
			throw new AppiumServerFileNotFoundException("Appium javascript main.js not found");
		}
		LOG.info("Using APPIUM js file located at " + appiumJs.getAbsolutePath());

		return AppiumDriverLocalService.buildService(new AppiumServiceBuilder().usingDriverExecutable(nodeExecutable)
				.withAppiumJS(appiumJs).withLogFile(serverLogs).usingPort(Integer.parseInt(customServer.getPort()))
				.withArgument(AndroidServerFlag.CHROME_DRIVER_PORT, customServer.getChromedriverPort())
				.withArgument(AndroidServerFlag.BOOTSTRAP_PORT_NUMBER, customServer.getBootstrapPort())
				.withArgument(GeneralServerFlag.LOG_LEVEL, customServer.getLogLevel())
				.withIPAddress(customServer.getIp()).withArgument(GeneralServerFlag.SESSION_OVERRIDE)
				.withArgument(IOSServerFlag.WEBKIT_DEBUG_PROXY_PORT, customServer.getIosWebkitDebugProxyPort())
				.withCapabilities(ConfigHelper.getInstance().getDesiredCapabilities()));
	}

	private static void verifyEnvVars() {
		verifyEnvVar("NODE_HOME");
		verifyEnvVar("APPIUM_HOME");
	}

	private static void verifyEnvVar(String varName) {
		LOG.info("Checking if env variable " + varName + " is set");
		if (null == System.getenv(varName) || "".equals(System.getenv(varName))) {
			throw new EnvironmentVariableException(varName + " IS NOT SET AS AN ENVIRONMENT VARIABLE");
		}
		LOG.info("Env var " + varName + " OK");
	}

}
