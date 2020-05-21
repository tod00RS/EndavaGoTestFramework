package com.endava.appium.framework.helpers;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import com.endava.appium.framework.util.CapabilitiesConstants;
import com.endava.appium.framework.util.CustomLogger;
import com.endava.appium.framework.util.HttpDeleteWithBody;
import com.endava.appium.framework.util.SearchCriteria;
import com.endava.appium.framework.util.exceptions.OpiumException;

/**
 * HELPER CLASS THAT HANDLES REMOTE EXECUTION:
 *  - START/STOP OPIUM SERVER (REMOTE START APPIUM ON A NETWORKED MACHINE, VIA REST CALLS)
 *  - QUERRIES THE NETWORK (VIA OPIUM) FOR A DEVICE MATHCING THE SEARCH CRITERIA
 *  - SEARCH CRITERIA is a JSON of the device that the user wishes to run the tests against
 */
public enum OpiumHelper {

	INSTANCE;

	private static final Logger LOG = CustomLogger.INSTANCE.getLogger(OpiumHelper.class);
	private static final String CONTENT_TYPE = "application/json";
	private static final String HEADER_CONTENT_TYPE = "Content-Type";
	private static final String EXECUTE_COMMAND = "/execute";
	private static final String DEVICE_HASH = "deviceHash";
	private static final String OPIUM_COMMAND = "command";
	private static final String STATUS_NOT_OK_ERROR = "STATUS CODE WAS NOT 200 OK. Actual response: ";

	private String opiumServer = ConfigHelper.getInstance().getOpiumCapabilities().getAddress();

	/**
	 * 
	 * @param sc:
	 *            Search Criteria for the desired device. Tests will run only if
	 *            all criteria that are given are found
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public JSONObject queryForDevice(SearchCriteria sc) throws ClientProtocolException, IOException {
		JSONObject foundDevice = null;
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(opiumServer + "/device");
		request.addHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE);
		LOG.info(String.format("Sending request %s", request.getRequestLine().toString()));
		HttpResponse response = client.execute(request);
		if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
			LOG.info("Successfully received response from service");
			String jsonString = EntityUtils.toString(response.getEntity());
			JSONArray jsonArray = new JSONArray(jsonString);
			listAvailableDevices(jsonArray);
			verifyAtLeastOneDeviceAvailable(jsonArray);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject device = jsonArray.getJSONObject(i);
				if (isDeviceAvailable(sc, device)) {
					LOG.info(String.format("Device[%d] matches search criteria", i));
					LOG.info("Found available device matching criteria at ip: " + device.getString("ip") + " with udid "
							+ device.getString("uid"));
					foundDevice = device;
					break;
				} else {
					LOG.info("Device[" + i + "] does not match search criteria");
				}
			}
		} else {
			throw new OpiumException("Response was not 200 OK. Actual response was: " + response.getStatusLine());
		}
		return foundDevice;
	}

	public void startAppiumServerForDevice(JSONObject device) throws ClientProtocolException, IOException {
		if (null != device) {
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(opiumServer + EXECUTE_COMMAND);
			post.setHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE);
			JSONObject json = new JSONObject();
			String command = buildStartServerCommand();
			json.put("ip", device.getString("ip")).put(DEVICE_HASH, device.getString("uid")).put(OPIUM_COMMAND,
					command);
			StringEntity request = new StringEntity(json.toString());
			LOG.info("Trying to start opium service for " + json);
			post.setEntity(request);
			HttpResponse response = client.execute(post);
			if (HttpURLConnection.HTTP_OK == response.getStatusLine().getStatusCode()) {
				LOG.info(String.format("Successfully started appium server at ip: %s, for device with udid: %s",
						device.getString("ip"), device.getString("uid")));
			} else {
				LOG.error("REQUEST WAS " + request);
				throw new OpiumException(STATUS_NOT_OK_ERROR + response.getStatusLine());
			}
		} else {
			throw new OpiumException("NO DEVICE MATCHING CTIERIA WAS FOUND. TRY AGAIN LATER");
		}
	}

	public void stopAppiumServerForDevice(JSONObject device) throws ClientProtocolException, IOException {
		HttpClient client = HttpClientBuilder.create().build();
		HttpDeleteWithBody delete = new HttpDeleteWithBody(opiumServer + EXECUTE_COMMAND);
		delete.setHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE);
		JSONObject json = new JSONObject();
		json.put("ip", device.getString("ip")).put(DEVICE_HASH, device.getString("uid")).put(OPIUM_COMMAND,
				ConfigHelper.getInstance().getAppiumServerCapabilities().getPort());
		StringEntity request = new StringEntity(json.toString());
		delete.setEntity(request);
		LOG.info("Trying to stop opium service for " + json);
		HttpResponse response = client.execute(delete);
		if (HttpURLConnection.HTTP_OK == response.getStatusLine().getStatusCode()) {
			LOG.info(String.format("Successfully stopped appium server at ip: %s, for device with udid: %s",
					device.getString("ip"), device.getString("uid")));
		} else {
			throw new OpiumException(STATUS_NOT_OK_ERROR + response.getStatusLine());
		}
	}

	public void stopAppiumServerForDevice() throws ClientProtocolException, IOException {
		HttpClient client = HttpClientBuilder.create().build();
		HttpDeleteWithBody delete = new HttpDeleteWithBody(opiumServer + EXECUTE_COMMAND);
		delete.setHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE);
		JSONObject json = new JSONObject();
		json.put("ip", ConfigHelper.getInstance().getAppiumServerCapabilities().getIp())
				.put("deviceHash", ConfigHelper.getInstance().getCapability("udid"))
				.put(OPIUM_COMMAND, ConfigHelper.getInstance().getAppiumServerCapabilities().getPort());
		StringEntity request = new StringEntity(json.toString());
		delete.setEntity(request);
		LOG.info("Trying to stop opium service for " + json);
		HttpResponse response = client.execute(delete);
		if (HttpURLConnection.HTTP_OK == response.getStatusLine().getStatusCode()) {
			LOG.info(String.format("Successfully stopped appium server at ip: %s, for device with udid: %s",
					ConfigHelper.getInstance().getAppiumServerCapabilities().getIp(),
					ConfigHelper.getInstance().getCapability("udid")));
		} else {
			throw new OpiumException(STATUS_NOT_OK_ERROR + response.getStatusLine());
		}
	}

	private void listAvailableDevices(JSONArray deviceArray) throws ParseException, IOException {
		if (deviceArray.length() > 0) {
			LOG.info("AVAILABLE LIST OF DEVICES");
			for (int i = 0; i < deviceArray.length(); i++) {
				LOG.info("DEVICE[" + i + "] " + deviceArray.getJSONObject(i));
			}
		}
	}

	private void verifyAtLeastOneDeviceAvailable(JSONArray deviceArray) {
		if (deviceArray.length() == 0) {
			throw new OpiumException("NO DEVICES AVAILABLE");
		}
	}

	private boolean isDeviceAvailable(SearchCriteria sc, JSONObject json) {
		boolean foundModel = true;
		boolean foundSdk = true;
		boolean foundAndroid = true;
		LOG.info("Looking for available device matching search criteria " + sc.toString());
		if (null != sc.getModel()) {
			if (stripJsonObject(json.getString("model")).matches(sc.getModel())) {
				foundModel = true;
			} else {
				LOG.warn(String.format("Model %s does not match criteria %s", stripJsonObject(json.getString("model")),
						sc.getModel()));
				foundModel = false;
			}
		}

		if (null != sc.getSdkVersion()) {
			if (stripJsonObject(json.getString("sdkVersion")).matches(sc.getSdkVersion())) {
				foundSdk = true;
			} else {
				LOG.warn(String.format("Sdk version %s does not match criteria %s",
						stripJsonObject(json.getString("sdkVersion")), sc.getSdkVersion()));
				foundSdk = false;
			}
		}

		if (null != sc.getAndroidVersion()) {
			if (stripJsonObject(json.getString("androidVersion")).matches(sc.getAndroidVersion())) {
				foundAndroid = true;
			} else {
				LOG.warn(String.format("Android version %s does not match criteria %s",
						stripJsonObject(json.getString("androidVersion")), sc.getAndroidVersion()));
				foundAndroid = false;
			}
		}
		return foundModel && foundSdk && foundAndroid;
	}

	private String stripJsonObject(String json) {
		return json.replace("[", "").replace("]", "");
	}

	private String buildStartServerCommand() {
		String commandString = "-p " + ConfigHelper.getInstance().getAppiumServerCapabilities().getPort()
				+ " --session-override";
		String udid = (String) ConfigHelper.getInstance().getDesiredCapabilities()
				.getCapability(CapabilitiesConstants.CAPAB_UDID);
		String bp = ConfigHelper.getInstance().getAppiumServerCapabilities().getBootstrapPort();
		String chromePort = ConfigHelper.getInstance().getAppiumServerCapabilities().getChromeDriverPort();
		if (null != udid) {
			commandString += " -U " + udid;
		}
		if (null != bp) {
			commandString += " -bp " + bp;
		}
		if (null != chromePort) {
			commandString += " --chromedriver-port " + chromePort;
		}
		return commandString;
	}

}
