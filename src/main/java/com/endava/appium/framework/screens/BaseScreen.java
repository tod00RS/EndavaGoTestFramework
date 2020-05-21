package com.endava.appium.framework.screens;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.support.PageFactory;

import com.endava.appium.framework.helpers.BaseHelper;
import com.endava.appium.framework.helpers.ConfigHelper;
import com.endava.appium.framework.helpers.DriverHelper;
import com.endava.appium.framework.helpers.WaitHelper;
import com.endava.appium.framework.util.CapabilitiesConstants;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class BaseScreen extends BaseHelper {

	protected String packageName; // needed in case testing branding for android
									// apps that have different packageName

	public BaseScreen() {
		this.packageName = (String) ConfigHelper.getInstance().getCapability(CapabilitiesConstants.CAPAB_APP_PACKAGE);
		PageFactory.initElements(
				new AppiumFieldDecorator(DriverHelper.getDriver(), WaitHelper.DEFAULT_TIMEOUT, TimeUnit.SECONDS), this);
	}

	/**
	 * Overwrites the implicit constructor in order to instantiate screen with
	 * different package name than config package name
	 * 
	 * @param packageName
	 */
	public BaseScreen(String packageName) {
		this.packageName = packageName;
	}

}
