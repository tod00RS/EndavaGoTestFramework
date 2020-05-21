package com.endava.appium.framework.util.exceptions;

public class ScreenshotException extends CustomRuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ScreenshotException(String message) {
		super(message);
	}
	
	public ScreenshotException(Exception e) {
		super(e.getMessage());
	}

}
