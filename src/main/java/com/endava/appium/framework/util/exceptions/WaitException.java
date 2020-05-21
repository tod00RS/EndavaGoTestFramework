package com.endava.appium.framework.util.exceptions;

public class WaitException extends CustomRuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WaitException(String message) {
		super(message);
	}
	
	public WaitException(Exception e) {
		super(e.getMessage());
	}
	
	

}
