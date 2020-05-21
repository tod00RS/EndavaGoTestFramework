package com.endava.appium.framework.util;

/**
 * ENUM that holds all the supported Mobile Connection Types
 */
public enum MobileConnectionType {

	WIFI("wifi"), DATA("data"), AIRPLANE_MODE("airplane");

	private final String conn;

	private MobileConnectionType(final String conn) {
		this.conn = conn;
	}

	public String getValue() {
		return conn;
	}

}
