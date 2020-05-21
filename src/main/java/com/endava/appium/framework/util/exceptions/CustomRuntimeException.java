package com.endava.appium.framework.util.exceptions;

import org.slf4j.Logger;

import com.endava.appium.framework.util.CustomLogger;

public class CustomRuntimeException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	protected final Logger log = CustomLogger.INSTANCE.getLogger(getClass());
	
	public CustomRuntimeException(String message) {
		super(message);
		log.error(message,this);
	}

}
