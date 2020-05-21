package com.endava.appium.framework.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

/**
 * Listener for filtering out test classes / methods based on user preference
 */
public class TestFilterListener implements IMethodInterceptor {

	private static final Logger LOG = CustomLogger.INSTANCE.getLogger(TestFilterListener.class);

	public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
		/*
		 * Read params from xml suite
		 */
		String testsToBeIgnored = context.getCurrentXmlTest().getParameter("ignoreMethods");
		String classesToBeIgnored = context.getCurrentXmlTest().getParameter("ignoreClasses");

		List<String> listOfTestsToBeIgnored = new ArrayList<>();
		List<String> listOfClassesToBeIgnored = new ArrayList<>();

		/*
		 * split the params and build the list of methods/classes to filter out
		 */
		if (null != classesToBeIgnored) {
			listOfClassesToBeIgnored = Arrays.asList(classesToBeIgnored.split(","));
			if (listOfClassesToBeIgnored.isEmpty()) {
				LOG.info("DETECTED request to ignore some classes from the suite");
			}
		}

		if (null != testsToBeIgnored) {
			listOfTestsToBeIgnored = Arrays.asList(testsToBeIgnored.split(","));
			if (listOfTestsToBeIgnored.isEmpty()) {
				LOG.info("DETECTED request to ignore some tests from the suite");
			}
		}

		/*
		 * this result will be the actual suite that will be executed by testng
		 */
		List<IMethodInstance> result = new ArrayList<>();

		/*
		 * list of all classes that are coming in before filtering
		 * 
		 * instead of doing logic for each test method, we use this list to
		 * avoid extra loops
		 * 
		 * since we can ONLY loop through test methods, we need to extract class
		 * names for each method
		 * 
		 * once we have the class test name for the method, add it to the list,
		 * and NEVER do class level checks for filtering in case we already
		 * filtered out a whole class
		 */
		List<String> testClasses = new ArrayList<>();

		/*
		 * loop through methods
		 */
		for (IMethodInstance method : methods) {
			String methodClassName = method.getMethod().getInstance().getClass().getSimpleName();
			// in case the test class is to be ignored
			if (listOfClassesToBeIgnored.contains(methodClassName)) {
				// if we HAVENT already worked through this class, add it to our
				// internal list so we dont have to duplicate log messages
				if (!testClasses.contains(methodClassName)) {
					testClasses.add(methodClassName);
					LOG.info("FOUND MATCHING CLASS IN LIST OF TEST CLASSES TO BE IGNORED");
					LOG.info("ALL METHODS IN CLASS <<" + methodClassName + ">> WILL BE IGNORED IN THIS RUN");

				}
			} else {
				// in case the class is NOT to be ignored we make sure that the
				// method is NOT to be ignored
				// and we add it to the result list
				if (!listOfTestsToBeIgnored.contains(method.getMethod().getMethodName())) {
					result.add(method);
				} else {
					LOG.info("FOUND MATCHING TEST IN LIST OF TESTS TO BE IGNORED");
					LOG.info("TEST METHOD <" + method.getMethod().getMethodName() + "> WILL NOT RUN");
				}
			}
		}

		return result;

	}
}