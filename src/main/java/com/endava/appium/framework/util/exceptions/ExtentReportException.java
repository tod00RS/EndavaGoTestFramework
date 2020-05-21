package com.endava.appium.framework.util.exceptions;

public class ExtentReportException extends CustomRuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExtentReportException(String message) {
		super(message);
	}
	
	public ExtentReportException(Exception e) {
		super(e.getMessage());
	}

}
