package com.endava.appium.framework.util;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Returns the actual thread id for logging purposes
 * Logger framework does not have access to thread id (only thread name is available)
 */
public class ThreadIdConverter extends ClassicConverter {

	@Override
	public String convert(ILoggingEvent event) {
		return "Thread-id=" + Thread.currentThread().getId();
	}

}
