package com.endava.appium.framework.util;

/**
 *	Custom class that is used to treat cross platform support
 */
public final class OsUtils {
	private static String os = null;
	private static String sepparator = null;
	private static String nodeExecutable = null;

	private OsUtils() {

	}

	public static String getOsName() {
		if (os == null) {
			os = System.getProperty("os.name");
		}
		return os;
	}

	public static boolean isWindows() {
		return getOsName().startsWith("Windows");
	}

	public static boolean isMac() {
		return getOsName().startsWith("Mac");
	}

	public static String getOsSepparator() {
		if (isWindows()) {
			sepparator = "\\";
		}
		if (isMac()) {
			sepparator = "/";
		}
		return sepparator;
	}

	public static String getNodeExecutable() {
		if (isWindows()) {
			nodeExecutable = "node.exe";
		}
		if (isMac()) {
			nodeExecutable = "node";
		}
		return nodeExecutable;
	}
}
