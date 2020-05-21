package tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import com.endava.appium.framework.helpers.ConfigHelper;

public class BaseOpiumTest {
	
	@BeforeClass
	@Parameters("configFile")
	public void setupOpium(String configFile) {
		ConfigHelper.loadConfigData(configFile);
	}

}
