package tests;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.endava.appium.framework.helpers.ConfigHelper;

public class JsonTest {
	
	@Test
	@Parameters("configFile")
	public void readTest(String configFile) {
		ConfigHelper.loadConfigFromJsonFile(configFile, ConfigHelper.getInstance());
	}

}
