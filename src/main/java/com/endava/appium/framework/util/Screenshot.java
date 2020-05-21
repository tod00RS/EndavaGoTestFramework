package com.endava.appium.framework.util;

import java.io.File;

import com.endava.appium.framework.util.exceptions.ScreenshotException;

/**
 * Custom ...
 */
public class Screenshot {

	private String name;
	private double size;
	private String location;

	public Screenshot() {
		this.name = "defaultScreenshotName";
	}

	public Screenshot(String location) {
		String loc = location;

		if (OsUtils.isWindows()) {
			loc = loc.replace("/", "\\");
		}
		if (OsUtils.isMac()) {
			loc = loc.replace("\\", "/");
		}
		this.location = loc;
		File f = new File(loc);
		if (!f.exists()) {
			CustomLogger.INSTANCE.getLogger(Screenshot.class)
					.error("Failed to create screenshot object. File " + loc + " not found");
			throw new ScreenshotException("Failed to create screenshot object. File " + loc + " not found");
		}
		this.name = f.getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Screenshot [name=" + name + ", size=" + size + ", location=" + location + "]";
	}
}
