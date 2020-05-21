package com.endava.appium.framework.util;

public class CommonUtils {

	private CommonUtils() {

	}

	static Boolean notNull(Object... args) {
		for (Object arg : args) {
			if (arg == null)
				return false;
		}
		return true;
	}

}
