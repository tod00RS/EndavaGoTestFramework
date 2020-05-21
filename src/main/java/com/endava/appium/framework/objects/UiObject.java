package com.endava.appium.framework.objects;

import com.endava.appium.framework.util.CustomLogger;
import org.slf4j.Logger;

public class UiObject {

    private String locator;
    private static final Logger LOG = CustomLogger.INSTANCE.getLogger(UiObject.class);

    UiObject(String locator){
        this.locator = locator;
        LOG.debug("Created new UiObject: " + this.locator);
    }


}
