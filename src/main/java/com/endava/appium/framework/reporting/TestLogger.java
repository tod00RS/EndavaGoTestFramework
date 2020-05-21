package com.endava.appium.framework.reporting;

import org.testng.SkipException;

import com.endava.appium.framework.util.BaseTest;
import com.endava.appium.framework.util.Screenshot;
import com.relevantcodes.extentreports.LogStatus;

/**
 * LOGGER CLASS FOR HTML REPORT
 * LOG:
 * 	- MESSAGES
 * 	- STATUS
 * 	- SCREENSHOTS
 * ASSIGN:
 * 	- AUTHOR
 * 	- CATEGORIES
 * SKIP TESTS
 * INSERT HTML CONTENT (LABELS FOR EXAMPLE)
 */
public class TestLogger extends BaseTest {
	public static void log(final String message) {
		ExtentTestManager.getTest().log(LogStatus.INFO, message + "<br>");
	}

	public static void pass(final String message) {
		ExtentTestManager.getTest().log(LogStatus.PASS, message + "<br>");
	}

	public static void fail(final String message) {
		ExtentTestManager.getTest().log(LogStatus.FAIL, message + "<br>");
	}

	public static void warn(final String message) {
		ExtentTestManager.getTest().log(LogStatus.WARNING, message + "<br>");
	}

	public static void fatal(final String message) {
		ExtentTestManager.getTest().log(LogStatus.FATAL, message + "<br>");
	}

	public static void error(final String message) {
		ExtentTestManager.getTest().log(LogStatus.ERROR, message + "<br>");
	}

	public static void skipTest(String reason) {
		ExtentTestManager.getTest().log(LogStatus.SKIP, reason + "<br>");
		throw new SkipException(reason);
	}

	public static void assignCategory(String... category) {
		ExtentTestManager.getTest().assignCategory(category);
	}

	public static void assignAuthor(String author) {
		ExtentTestManager.getTest().assignAuthor(author);
	}

	public static void attachScreenshot(Screenshot screenshot) {
		String image = ExtentTestManager.getTest().addScreenCapture(screenshot.getLocation());
		ExtentTestManager.getTest().log(LogStatus.INFO, "Screenshot" + image);
	}

	public static void attachScreenshot(Screenshot screenshot, String description) {
		String image = ExtentTestManager.getTest().addScreenCapture(screenshot.getLocation());
		ExtentTestManager.getTest().log(LogStatus.INFO, description + image);
	}

	public static void addSystemInfo(String info, String value) {
		ExtentManager.getInstance().addSystemInfo(info, value);
	}

	public static void demoLabels() {
		String labelMessage = "Labels can be created using: " + "<pre>"
				+ "&lt;span class='success label'&gt;Success&lt;/span&gt; <br />"
				+ "&lt;span class='fail label'&gt;Fail&lt;/span&gt; <br />"
				+ "&lt;span class='warning label'&gt;Warning&lt;/span&gt; <br />"
				+ "&lt;span class='info label'&gt;Info&lt;/span&gt; <br />"
				+ "&lt;span class='skip label'&gt;Skip&lt;/span&gt;" + "</pre>";
		ExtentTestManager.getTest().log(LogStatus.PASS, labelMessage);
		labelMessage = "<span class='success label'>Success</span> " + "<span class='fail label'>Fail</span> "
				+ "<span class='warning label'>Warning</span> " + "<span class='info label'>Info</span> "
				+ "<span class='skip label'>Skip</span>";

		ExtentTestManager.getTest().log(LogStatus.PASS, labelMessage);
	}

}
