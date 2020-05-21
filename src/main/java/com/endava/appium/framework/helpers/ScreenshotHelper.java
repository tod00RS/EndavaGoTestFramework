package com.endava.appium.framework.helpers;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.testng.Assert;

import com.endava.appium.framework.util.CustomLogger;
import com.endava.appium.framework.util.OsUtils;
import com.endava.appium.framework.util.Screenshot;
import com.endava.appium.framework.util.exceptions.ScreenshotException;

/**
 * HELPER CLASS FOR TAKING / SAVING SCREENSHOTS DURING TEST RUN CAN ALSO HANDLE
 * SCREENSHOT COMPARISON
 */
public enum ScreenshotHelper {

	INSTANCE;

	private static final BigDecimal DEFAULT_PERCENTAGE = BigDecimal.valueOf(99.5);
	private static final Logger LOG = CustomLogger.INSTANCE.getLogger(ScreenshotHelper.class);
	private static final String USER_DIR = "user.dir";

	/**
	 * Take a screenshot of the current screen of the application
	 * 
	 * @return Screenshot object saved as a temp file, with .png extension
	 */

	public Screenshot takeScreenshot() {
		File srcFile = ((TakesScreenshot) DriverHelper.getDriver()).getScreenshotAs(OutputType.FILE);
		File destFile = null;
		try {
			destFile = File.createTempFile("temp" + System.currentTimeMillis(), ".png");
			FileUtils.copyFile(srcFile, destFile);
		} catch (IOException e) {
			throw new ScreenshotException(e);
		}
		Screenshot ss = new Screenshot();
		if (null != destFile) {
			ss.setName(destFile.getName());
			ss.setLocation(destFile.getAbsolutePath());
			ss.setSize(destFile.length());
			LOG.info("Screenshot " + ss.getLocation() + " captured");
		} else {
			throw new ScreenshotException("Failed to create temp file");
		}
		return ss;
	}

	/**
	 * Takes screenshot of a given WebElement
	 * 
	 * @param element
	 * @return
	 */

	public Screenshot takeScreenshot(WebElement element) {
		File srcFile = ((TakesScreenshot) DriverHelper.getDriver()).getScreenshotAs(OutputType.FILE);
		File destFile = null;
		cropElementFromScreenshot(srcFile, element);
		try {
			destFile = File.createTempFile("temp" + System.currentTimeMillis(), ".png");
			FileUtils.copyFile(srcFile, destFile);
		} catch (IOException e) {
			throw new ScreenshotException(e);
		}

		Screenshot ss = new Screenshot();
		if (null != destFile) {
			ss.setName(destFile.getName());
			ss.setLocation(destFile.getAbsolutePath());
			ss.setSize(destFile.length());
			LOG.info("Screenshot " + ss.getName() + " captured");
		} else {
			throw new ScreenshotException("Failed to create a temp file");
		}
		return ss;
	}

	/**
	 * Saves a screenshot of a given WebElement
	 * 
	 * @param element
	 * @throws IOException
	 */
	public void saveScreenshot(WebElement element) {
		saveScreenshot(element, String.valueOf(System.currentTimeMillis()));
	}

	/**
	 * Saves a screenshot of a given WebElement with custom Name
	 * 
	 * @param element
	 * @param name
	 */
	public void saveScreenshot(WebElement element, String name) {
		File srcFile = ((TakesScreenshot) DriverHelper.getDriver()).getScreenshotAs(OutputType.FILE);
		File destFile = null;
		cropElementFromScreenshot(srcFile, element);
		try {
			destFile = new File(System.getProperty(USER_DIR) + OsUtils.getOsSepparator() + "screenshots",
					name + ".png");
			FileUtils.copyFile(srcFile, destFile);
			LOG.info("Saved screenshot " + name);
		} catch (IOException e) {
			throw new ScreenshotException(e);
		}
	}

	/**
	 * Takes a screenshot, and saves it with a random name to
	 * {user.dir}/screenshots
	 */
	public void saveScreenshot() {
		File srcFile = ((TakesScreenshot) DriverHelper.getDriver()).getScreenshotAs(OutputType.FILE);
		File destFile = null;
		try {
			destFile = new File(System.getProperty(USER_DIR) + OsUtils.getOsSepparator() + "screenshots",
					System.currentTimeMillis() + ".png");
			FileUtils.copyFile(srcFile, destFile);
		} catch (IOException e) {
			throw new ScreenshotException(e);
		}
	}

	/**
	 * Takes a screenshot, and saves it with given name to {user.dir}/{location}
	 * 
	 * @param location
	 * @param name
	 */
	public void saveScreenshot(String location, String name) {
		File srcFile = ((TakesScreenshot) DriverHelper.getDriver()).getScreenshotAs(OutputType.FILE);
		File destFile = null;
		try {
			destFile = new File(System.getProperty(USER_DIR) + OsUtils.getOsSepparator() + location, name + ".png");
			FileUtils.copyFile(srcFile, destFile);
		} catch (IOException e) {
			throw new ScreenshotException(e);
		}
	}

	/**
	 * <p>
	 * Compare two screenshots pixel by pixel and determine if they are equal
	 * based on a DEFAULT_PERCENTAGE of equality
	 * </p>
	 * 
	 * <p>
	 * Q: WHY is percentage of equality required?
	 * </p>
	 * <p>
	 * A: Screenshots from device also include the device status bar, thus, two
	 * pictures taken at different timestamps, of the same identical screen will
	 * never be 100% equal
	 * </p>
	 * 
	 * @param screenshot1
	 *            path to the expected screenshot
	 * @param screenshot2
	 *            path to the actual screenshot taken by the test
	 */

	public void compareScreenshots(Screenshot screenshot1, Screenshot screenshot2) {
		compareScreenshots(screenshot1, screenshot2, DEFAULT_PERCENTAGE);
	}

	/**
	 * <p>
	 * Compare two screenshots pixel by pixel and determine if they are equal
	 * based on a given percentage of equality
	 * </p>
	 * 
	 * <p>
	 * Q: WHY is percentage of equality required?
	 * </p>
	 * <p>
	 * A: Screenshots from device also include the device status bar, thus, two
	 * pictures taken at different timestamps, of the same identical screen will
	 * never be 100% equal
	 * </p>
	 * 
	 * @param screenshot1
	 *            path to the expected screenshot
	 * @param screenshot2
	 *            path to the actual screenshot taken by the test
	 * @param percentageOfEquality
	 *            desired percent of equality between screenshots
	 */

	public void compareScreenshots(Screenshot screenshot1, Screenshot screenshot2, BigDecimal percentageOfEquality) {
		Assert.assertTrue(areScreenshotsEqual(screenshot1, screenshot2, percentageOfEquality),
				"Screenshot comparison failed because percentage of equality was not met");
	}

	/**
	 * Decides if two screenshots are 100% equal
	 * 
	 * @param screenshot1
	 * @param screenshot2
	 * @return
	 */
	public boolean areScreenshotsEqual(Screenshot screenshot1, Screenshot screenshot2) {
		return areScreenshotsEqual(screenshot1, screenshot2, new BigDecimal(100));
	}

	/**
	 * Decides if two screenshots are equal base on percentageOfEquality
	 * 
	 * @param screenshot1
	 * @param screenshot2
	 * @param percentageOfEquality
	 * @return
	 */
	public boolean areScreenshotsEqual(Screenshot screenshot1, Screenshot screenshot2,
			BigDecimal percentageOfEquality) {

		LOG.info("Comparing screenshot " + screenshot1.getName() + " with screenshot " + screenshot2.getName());
		boolean result = false;
		/*
		 * get exact filepath to screenshots
		 */
		String file1 = screenshot1.getLocation();
		String file2 = screenshot2.getLocation();

		/*
		 * get images for each screenshot
		 */
		Image image1 = Toolkit.getDefaultToolkit().getImage(file1);
		Image image2 = Toolkit.getDefaultToolkit().getImage(file2);

		try {

			/*
			 * create arrays of int containing pixels for each image using
			 * PixelGrabber
			 */
			PixelGrabber grab1 = new PixelGrabber(image1, 0, 0, -1, -1, false);
			PixelGrabber grab2 = new PixelGrabber(image2, 0, 0, -1, -1, false);

			int[] data1 = new int[-1];

			if (grab1.grabPixels()) {
				data1 = (int[]) grab1.getPixels();
			}

			int[] data2 = new int[-1];

			if (grab2.grabPixels()) {
				data2 = (int[]) grab2.getPixels();
			}
			if (data1.length != data2.length) {
				throw new ScreenshotException("Cannot compare screenshots that do not have the same resolution.");
			}
			/*
			 * count the pixels at same coords that are identical
			 */
			double ok = 0;
			for (int i = 0; i < data1.length; i++) {
				if (data1[i] == data2[i]) {
					ok++;
				}
			}
			/*
			 * calculate the percentage of equality between the two screenshots
			 */
			BigDecimal percent = BigDecimal.valueOf((ok / data1.length) * 100);
			/*
			 * set the scale to 10 decimals, just in case
			 */
			BigDecimal scaledPercentageOfEquality = percentageOfEquality.setScale(10, RoundingMode.HALF_DOWN);
			percent = percent.setScale(10, RoundingMode.HALF_DOWN);
			LOG.info("Pixels are " + percent + " equal.");
			LOG.info("Comparing equality percentages. INTENDED: " + scaledPercentageOfEquality + " vs ACTUAL: "
					+ percent);
			/*
			 * if the percentage of equality > THRESHOLD, then screenshots are
			 * ok
			 */
			result = percentageOfEquality.compareTo(percent) <= 0;

		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e);
			Thread.currentThread().interrupt();
		}
		return result;
	}

	/**
	 * Captures a screenshot of the screen when the failure occurs
	 * 
	 * @param fileName
	 */
	public void captureFailureScreenshot(String fileName) {
		captureFailureScreenshot(fileName, "target");
	}

	/**
	 * Captures a screenshot of the screen when the failure occurs and stores it
	 * to <b>destinationFolder</b>
	 * 
	 * @param fileName
	 * @param destinationFolder
	 */
	public void captureFailureScreenshot(String fileName, String destinationFolder) {
		if (System.getProperty("noss") == null) {
			// switch to native context -> screenshots cannot be capture while
			// on webviews
			ActionsHelper.INSTANCE.switchToNativeContext();
			FileOutputStream out = null;
			byte[] screenshot = null;
			try {
				screenshot = ((TakesScreenshot) DriverHelper.getDriver()).getScreenshotAs(OutputType.BYTES);
			} catch (NullPointerException e) {
				LOG.error("Driver was not started, skipping failure screenshot capturing");
				LOG.error(e.getMessage());
				throw e;
			}
			DateFormat dateFormat = new SimpleDateFormat("-yyyy-MM-dd HH.mm.ss");
			Date date = new Date();
			String formatedFileName = fileName + dateFormat.format(date);
			// Ensure directory is created
			new File(destinationFolder).mkdirs();
			try {
				out = new FileOutputStream(destinationFolder + OsUtils.getOsSepparator() + formatedFileName + ".png");
			} catch (FileNotFoundException e) {
				LOG.error(e.getMessage(), e);
			}
			if (null != out) {
				try {
					out.write(screenshot);
				} catch (WebDriverException | IOException e) {
					LOG.error(e.getMessage(), e);
				}
				try {
					out.close();
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				} finally {
					try {
						out.close();
					} catch (Exception e) {
						LOG.error(e.getMessage(), e);

					}
					LOG.info("Failure screenshot can be found at : " + destinationFolder + OsUtils.getOsSepparator()
							+ fileName + ".png");

				}
			} else {
				throw new ScreenshotException("Failed to capture failure screenshot");
			}
		} else {
			LOG.warn("SCREENSHOTS ARE DISABLED. PLEASE ENABLE BY REMOVING THE -Dnoss MAVEN PARAM FROM EXECUTION");
		}

	}

	private void cropElementFromScreenshot(File srcFile, WebElement element) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(srcFile);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		if (null != img) {
			LOG.info("initial img size:" + img.getHeight() + "x" + img.getWidth());
			int height = element.getSize().height;
			int width = element.getSize().width;
			Rectangle rect = new Rectangle(width, height);
			Point p = element.getLocation();

			BufferedImage dest;
			if (ActionsHelper.INSTANCE.isPortraitMode()) {
				LOG.info("trying to crop an image of size: " + rect.getHeight() + "x" + rect.getWidth());
				dest = img.getSubimage(p.getX(), p.getY(), rect.width, rect.height);
				LOG.info("resulting img size:" + dest.getHeight() + "x" + dest.getWidth());
			} else {
				LOG.info("trying to crop an image of size: " + rect.getWidth() + "x" + rect.getHeight());
				dest = img.getSubimage(p.getY(), p.getX(), rect.height, rect.width);
				LOG.info("resulting img size:" + dest.getHeight() + "x" + dest.getWidth());
			}

			try {
				ImageIO.write(dest, "png", srcFile);
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		} else {
			throw new ScreenshotException("Failed to crop image");
		}
	}
}
