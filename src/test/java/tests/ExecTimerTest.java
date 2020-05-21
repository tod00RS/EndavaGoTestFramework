package tests;

import org.slf4j.Logger;
import org.testng.annotations.Test;

import com.endava.appium.framework.helpers.WaitHelper;
import com.endava.appium.framework.util.CustomLogger;
import com.endava.appium.framework.util.ExecutionTimer;

public class ExecTimerTest {
	
	private Logger LOG = CustomLogger.INSTANCE.getLogger(ExecTimerTest.class);
	
	@Test
	public void display() {
		ExecutionTimer.start();
		WaitHelper.INSTANCE.waitFor(2454);
		ExecutionTimer.stop();
		LOG.info(String.valueOf(ExecutionTimer.exactDuration()));
	}

}
