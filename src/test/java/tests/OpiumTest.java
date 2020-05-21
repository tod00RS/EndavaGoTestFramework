package tests;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.endava.appium.framework.helpers.OpiumHelper;
import com.endava.appium.framework.util.CustomLogger;
import com.endava.appium.framework.util.SearchCriteria;

public class OpiumTest extends BaseOpiumTest {

	private static final Logger LOG = CustomLogger.INSTANCE.getLogger(OpiumTest.class);
	private JSONObject serverStartedOn = null;
	private String deviceModel = null;
	private String deviceSdk = "1[8-9]|2[0-3]";
	private String deviceAndroid = "4.4.*";

	@Test(enabled = true)
	public void searchDeviceOkOneCriteria() throws ClientProtocolException, IOException {
		SearchCriteria sc = new SearchCriteria.SearchCriteriaBuilder().withModel(deviceModel).build();
		OpiumHelper.INSTANCE.queryForDevice(sc);
	}

	@Test(enabled = true)
	public void searchDeviceOkMultipleCriteria() throws ClientProtocolException, IOException {
		SearchCriteria sc = new SearchCriteria.SearchCriteriaBuilder().withModel(deviceModel)
				.withAndroidVersion(deviceAndroid).withSdkVersion(deviceSdk).build();
		Assert.assertNotNull(OpiumHelper.INSTANCE.queryForDevice(sc));
	}

	@Test(enabled = true)
	public void searchDeviceNotOkOneCriteria() throws ClientProtocolException, IOException {
		SearchCriteria sc = new SearchCriteria.SearchCriteriaBuilder().withModel("aa").build();
		Assert.assertNull(OpiumHelper.INSTANCE.queryForDevice(sc));
	}

	@Test(enabled = true)
	public void searchDeviceNotOkMultipleCriteria() throws ClientProtocolException, IOException {
		SearchCriteria sc = new SearchCriteria.SearchCriteriaBuilder().withModel("bb")
				.withAndroidVersion(deviceAndroid).withSdkVersion(deviceSdk).build();
		Assert.assertNull(OpiumHelper.INSTANCE.queryForDevice(sc));
	}

	@Test(enabled = true)
	public void startServerOk() throws ClientProtocolException, IOException {
		SearchCriteria sc = new SearchCriteria.SearchCriteriaBuilder().withModel(deviceModel).build();
		JSONObject targetDevice = OpiumHelper.INSTANCE.queryForDevice(sc);
		serverStartedOn = targetDevice;
		OpiumHelper.INSTANCE.startAppiumServerForDevice(targetDevice);
	}

	@Test(enabled = true, dependsOnMethods = { "startServerOk" })
	public void stopServerOk() throws ClientProtocolException, IOException {
		if (null != serverStartedOn) {
			OpiumHelper.INSTANCE.stopAppiumServerForDevice(serverStartedOn);
		}

	}
	
	@Test(enabled = true, expectedExceptions = JSONException.class)
	public void startServerBadRequest() throws ClientProtocolException, IOException {
		SearchCriteria sc = new SearchCriteria.SearchCriteriaBuilder().withModel(deviceModel).build();
		JSONObject targetDevice = OpiumHelper.INSTANCE.queryForDevice(sc);
		targetDevice.remove("ip");
		OpiumHelper.INSTANCE.startAppiumServerForDevice(targetDevice);
	}

	@AfterMethod
	public void afterEachTest(ITestResult result) {
		LOG.info("FINISHED TEST " + result.getName());
	}

}
