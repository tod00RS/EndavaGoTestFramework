package com.endava.appium.framework.helpers;

import org.slf4j.Logger;

import com.endava.appium.framework.util.CustomLogger;

/**
 * HELPER CLASS THAT HOLDS THE INSTANCES OF THE OTHER HELPER CLASSES
 * USED AS PARENT CLASS FOR THE BASE SCREEN TO GET EASY USE OF THE HELPERS IN SCREENS
 */

public class BaseHelper {

	//BaseHelper class is extended to BaseScreen class, which is then extended to other Screen classes.
	//As a result, instances of Helper classes can be used in all Screen classes, without the need to declare that same instances in those Screen classes
	protected VerifyHelper verifyHelper = VerifyHelper.INSTANCE;
	protected ActionsHelper actionsHelper = ActionsHelper.INSTANCE;
	protected WaitHelper waitHelper = WaitHelper.INSTANCE;
	protected ScreenshotHelper ssHelper = ScreenshotHelper.INSTANCE;
	protected AdbHelper adbHelper = AdbHelper.INSTANCE;

	protected final Logger log = CustomLogger.INSTANCE.getLogger(getClass());
}
