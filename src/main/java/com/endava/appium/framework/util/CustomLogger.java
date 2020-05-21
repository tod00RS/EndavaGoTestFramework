package com.endava.appium.framework.util;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * Custom class used for changing default logging params
 */
public enum CustomLogger {
	INSTANCE;

	private Logger log;
	String consoleLogLevel = System.getProperty("consoleLogLevel");
	String fileLogLevel = System.getProperty("fileLogLevel");

	public Logger getLogger(Class<?> clazz) {
		// if -DconsoleLogLevel AND/OR -DfileLogLevel are not set => DEFAULT
		// values should be INFO/DEBUG
		if (null == consoleLogLevel) {
			System.setProperty("consoleLogLevel", "INFO");
		}

		if (null == fileLogLevel) {
			System.setProperty("fileLogLevel", "DEBUG");
		}
		log = (Logger) LoggerFactory.getLogger(clazz);
		return log;
	}
}
