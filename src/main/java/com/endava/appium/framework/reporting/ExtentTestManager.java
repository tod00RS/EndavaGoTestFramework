package com.endava.appium.framework.reporting;

import java.util.HashMap;
import java.util.Map;

import com.relevantcodes.extentreports.ExtentTest;

/**
 * REPORTING CLASS THAT SIGNALS EXTENT WHEN TO START/STOP A TEST AND TO GRAB THE CURRENT TEST INSTANCE
 */
public class ExtentTestManager {

	static Map<Integer, ExtentTest> extentTestMap = new HashMap<>();

	private ExtentTestManager() {

	}

	public static synchronized ExtentTest getTest() {
		return extentTestMap.get((int) (Thread.currentThread().getId()));
	}

	public static synchronized void endTest() {
		ExtentManager.getInstance().endTest(extentTestMap.get((int) (Thread.currentThread().getId())));
	}

	public static synchronized ExtentTest startTest(String testName) {
		return startTest(testName, "");
	}

	public static synchronized ExtentTest startTest(String testName, String desc) {
		ExtentTest test = ExtentManager.getInstance().startTest(testName, desc);
		extentTestMap.put((int) (Thread.currentThread().getId()), test);

		return test;
	}

}
