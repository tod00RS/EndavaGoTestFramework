package com.endava.appium.framework.reporting;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.ITestContext;

import com.endava.appium.framework.util.CustomLogger;
import com.endava.appium.framework.util.exceptions.ExtentReportException;
import com.relevantcodes.extentreports.ExtentReports;

/**
 *	SINGLETON CLASS FOR CREATING THE REPORT AND GETTING THE INSTANCE DURING RUNTIME
 */
public class ExtentManager {
	private static ExtentReports extent;
	private static ITestContext context;
	private static final Logger LOG = CustomLogger.INSTANCE.getLogger(ExtentManager.class);

	private ExtentManager() {

	}
	
	public static synchronized void resetManager() {
		extent = null;
	}

	public static synchronized ExtentReports getInstance() {
		if (extent == null) {
			File outputDirectory = new File(context.getOutputDirectory());
			File resultDirectory = new File(outputDirectory.getParentFile(), "ExtentReport " + context.getSuite().getName());
			extent = new ExtentReports(resultDirectory + File.separator + "Report " + context.getSuite().getName() + ".html", true);
			if (null != System.getProperty("x")) {
				// if extentX is enabled, then connect to the mongodb to record
				// the report
				extent.x();
			}
			extent.loadConfig(getCustomConfigFile());
			LOG.info("Extent Report directory: " + resultDirectory, true);
		}

		return extent;
	}

	private static File getCustomConfigFile() {
		InputStream is = ExtentManager.class.getResourceAsStream("/extentReportConfig.xml");
		Assert.assertNotNull(is);
		File configFile = null;
		try {

			configFile = File.createTempFile("extentReportConfig", ".xml");
			Assert.assertNotNull(configFile);
			FileUtils.copyInputStreamToFile(is, configFile);
		} catch (IOException e) {
			throw new ExtentReportException(e);
		}
		return configFile;
	}

	public static void setOutputDirectory(ITestContext context) {
		ExtentManager.context = context;
	}
}