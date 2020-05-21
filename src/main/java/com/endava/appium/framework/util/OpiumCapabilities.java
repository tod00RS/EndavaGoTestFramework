package com.endava.appium.framework.util;

import java.util.Set;

import org.json.simple.JSONObject;
import org.slf4j.Logger;

import com.endava.appium.framework.util.exceptions.OpiumException;

/**
 * CUSTOM CABABILITIES CLASS THAT HOLDS ALL THE DATA NEEDED TO START THE APPIUM SERVER REMOTELY VIA OPIUM
 * DATA IS READ BY CONFIGHELPER FROM A JSON CONFIGURATION FILE
 */
public class OpiumCapabilities {

	private static final Logger LOG = CustomLogger.INSTANCE.getLogger(OpiumCapabilities.class);

	private String address;
	private SearchCriteria opiumSearchCriteria;

	public OpiumCapabilities(JSONObject json) {
		this.address = (String) json.get(CapabilitiesConstants.OPIUM_SERVER_ADDRESS);
		this.opiumSearchCriteria = buildSearchCriteria(
				(JSONObject) json.get(CapabilitiesConstants.OPIUM_SEARCH_CRITERIA));
		verifyMandatoryDataIsPresent();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public SearchCriteria getOpiumSearchCriteria() {
		return opiumSearchCriteria;
	}

	public void setOpiumSearchCriteria(SearchCriteria opiumSearchCriteria) {
		this.opiumSearchCriteria = opiumSearchCriteria;
	}

	private SearchCriteria buildSearchCriteria(JSONObject json) {
		SearchCriteria sc = new SearchCriteria.SearchCriteriaBuilder().build();
		Set<?> jsonOpiumSearchCriteria = json.entrySet();
		for (Object o : jsonOpiumSearchCriteria) {
			String[] jsonObjects = o.toString().split("=");
			if ("model".equalsIgnoreCase(jsonObjects[0])) {
				sc.setModel(jsonObjects[1]);
				LOG.info("Set DEVICE search criteria for opium device TO: " + jsonObjects[1]);
			}
			if ("sdkversion".equalsIgnoreCase(jsonObjects[0])) {
				sc.setSdkVersion(jsonObjects[1]);
				LOG.info("Set SDK VERSION search criteria for opium device TO: " + jsonObjects[1]);
			}
			if ("androidversion".equalsIgnoreCase(jsonObjects[0])) {
				sc.setAndroidVersion(jsonObjects[1]);
				LOG.info("Set ANDROID VERSION search criteria for opium device TO: " + jsonObjects[1]);
			}
		}
		return sc;
	}

	private void verifyMandatoryDataIsPresent() {
		if (CommonUtils.notNull(this.address)) {
			LOG.info("Mandatory OPIUM CAPABILTIES data verification PASSED");
		} else {
			throw new OpiumException("ADDRESS IS MANDATORY FOR OPIUM TO START");
		}
	}

	@Override
	public String toString() {
		return "OpiumCapabilities [address=" + address + ", opiumSearchCriteria=" + opiumSearchCriteria + "]";
	}

}
