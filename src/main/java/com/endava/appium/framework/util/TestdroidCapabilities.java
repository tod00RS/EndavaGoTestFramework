package com.endava.appium.framework.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;
import org.slf4j.Logger;

import com.endava.appium.framework.util.exceptions.TestdroidCapabilitiesException;

/**
 * CUSTOM CABABILITIES CLASS THAT HOLDS ALL THE DATA NEEDED TO RUN TESTS IN
 * TESTDROID CLOUD DATA IS READ BY CONFIGHELPER FROM A JSON CONFIGURATION FILE
 */
public class TestdroidCapabilities {

	private static final Logger LOG = CustomLogger.INSTANCE.getLogger(TestdroidCapabilities.class);

	private String testdroidHubUrl;
	private String testdroidUsername;
	private String testdroidPassword;
	private String testdroidTarget;
	private String testdroidProject;
	private String testdroidTestrun;
	private String testdroidDevice;
	private String testdroidApp;
	private String testdroidJunitWaitTime;

	public TestdroidCapabilities(JSONObject json) {
		this.testdroidHubUrl = (String) json.get(CapabilitiesConstants.TESTDROID_HUBURL);
		this.testdroidUsername = (String) json.get(CapabilitiesConstants.TESTDROID_USERNAME);
		this.testdroidPassword = (String) json.get(CapabilitiesConstants.TESTDROID_PASSWORD);
		this.testdroidTarget = (String) json.get(CapabilitiesConstants.TESTDROID_TARGET);
		this.testdroidProject = (String) json.get(CapabilitiesConstants.TESTDROID_PROJECT);

		this.testdroidDevice = (String) json.get(CapabilitiesConstants.TESTDROID_DEVICE);
		this.testdroidApp = (String) json.get(CapabilitiesConstants.TESTDROID_APP);
		this.testdroidJunitWaitTime = (String) json.get(CapabilitiesConstants.TESTDROID_JUNIT_WAITTIME);

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		this.testdroidTestrun = (String) json.get(CapabilitiesConstants.TESTDROID_TESTRUN) + dateFormat.format(date);

		verifyMandatoryDataIsPresent();
	}

	public String getTestdroidHubUrl() {
		return testdroidHubUrl;
	}

	public void setTestdroidHubUrl(String testdroidHubUrl) {
		this.testdroidHubUrl = testdroidHubUrl;
	}

	public String getTestdroidUsername() {
		return testdroidUsername;
	}

	public void setTestdroidUsername(String testdroidUsername) {
		this.testdroidUsername = testdroidUsername;
	}

	public String getTestdroidPassword() {
		return testdroidPassword;
	}

	public void setTestdroidPassword(String testdroidPassword) {
		this.testdroidPassword = testdroidPassword;
	}

	public String getTestdroidTarget() {
		return testdroidTarget;
	}

	public void setTestdroidTarget(String testdroidTarget) {
		this.testdroidTarget = testdroidTarget;
	}

	public String getTestdroidProject() {
		return testdroidProject;
	}

	public void setTestdroidProject(String testdroidProject) {
		this.testdroidProject = testdroidProject;
	}

	public String getTestdroidTestrun() {
		return testdroidTestrun;
	}

	public void setTestdroidTestrun(String testdroidTestrun) {
		this.testdroidTestrun = testdroidTestrun;
	}

	public String getTestdroidDevice() {
		return testdroidDevice;
	}

	public void setTestdroidDevice(String testdroidDevice) {
		this.testdroidDevice = testdroidDevice;
	}

	public String getTestdroidApp() {
		return testdroidApp;
	}

	public void setTestdroidApp(String testdroidApp) {
		this.testdroidApp = testdroidApp;
	}

	public String getTestdroidJunitWaitTime() {
		return testdroidJunitWaitTime;
	}

	public void setTestdroidJunitWaitTime(String testdroidJunitWaitTime) {
		this.testdroidJunitWaitTime = testdroidJunitWaitTime;
	}

	private void verifyMandatoryDataIsPresent() {
		if (CommonUtils.notNull(this.testdroidHubUrl, this.testdroidUsername, this.testdroidPassword, this.testdroidApp,
				this.testdroidDevice, this.testdroidProject, this.testdroidTarget, this.testdroidTestrun)) {
			LOG.info("Mandatory TESTDROID CAPABILTIES data verification PASSED");
		} else {
			throw new TestdroidCapabilitiesException(
					"TESTDROID: HUBURL, USERNAME, PASSWORD, APP, DEVICE, PROJECT, TARGET, TESTRUN ARE MANDATORY FOR TESTDROID TO START");
		}
	}

	@Override
	public String toString() {
		return "TestdroidCapabilities [testdroidHubUrl=" + testdroidHubUrl + ", testdroidUsername=" + testdroidUsername
				+ ", testdroidPassword=" + testdroidPassword + ", testdroidTarget=" + testdroidTarget
				+ ", testdroidProject=" + testdroidProject + ", testdroidTestrun=" + testdroidTestrun
				+ ", testdroidDevice=" + testdroidDevice + ", testdroidApp=" + testdroidApp
				+ ", testdroidJunitWaitTime=" + testdroidJunitWaitTime + "]";
	}

}
