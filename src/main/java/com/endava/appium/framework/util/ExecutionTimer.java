package com.endava.appium.framework.util;

import java.math.BigDecimal;

/**
 * Custom start/stop timer class
 */
public class ExecutionTimer {
	
	private long start;
	private long end;

	private static final ThreadLocal<ExecutionTimer> execTimer = new ThreadLocal<ExecutionTimer>(){
		@Override
		protected ExecutionTimer initialValue() {
			return new ExecutionTimer();
		}
	};
	
	private ExecutionTimer(){
		
	}

	public static void start() {
		execTimer.get().reset();
		execTimer.get().start = System.currentTimeMillis();
	}

	public static void stop() {
		execTimer.get().end = System.currentTimeMillis();
	}

	public static String duration() {
		BigDecimal x = new BigDecimal(1000);
		BigDecimal num1 = new BigDecimal(execTimer.get().end);
		BigDecimal num2 = new BigDecimal(execTimer.get().start);
		return num1.subtract(num2).divide(x).toString();
	}

	public double durationInSeconds() {
		return (execTimer.get().end - execTimer.get().start) / 1000;
	}

	public static String exactDuration() {
		BigDecimal x = new BigDecimal(1000);
		BigDecimal num1 = new BigDecimal(execTimer.get().end);
		BigDecimal num2 = new BigDecimal(execTimer.get().start);
		return num1.subtract(num2).divide(x).toString();
	}

	public void reset() {
		execTimer.get().start = 0;
		execTimer.get().end = 0;
	}

}
