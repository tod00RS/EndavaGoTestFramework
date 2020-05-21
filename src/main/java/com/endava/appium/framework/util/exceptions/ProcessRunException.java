package com.endava.appium.framework.util.exceptions;

public class ProcessRunException extends CustomRuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProcessRunException(String message) {
		super(message);
	}
	
	public ProcessRunException(Exception e) {
		super(e.getMessage());
	}

}
