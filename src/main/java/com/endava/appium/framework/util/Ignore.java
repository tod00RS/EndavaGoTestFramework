package com.endava.appium.framework.util;

import java.lang.annotation.Retention;
/**
 *	Custom annotation to skip TestNG tests just like @Ignore from JUnit
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Ignore {
	
	String reason() default "No skip reason given";

}
