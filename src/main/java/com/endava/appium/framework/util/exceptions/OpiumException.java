package com.endava.appium.framework.util.exceptions;

public class OpiumException extends CustomRuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OpiumException(String message) {
		super(message);
	}
	
	public OpiumException(Exception e) {
		super(e.getMessage());
	}

}
