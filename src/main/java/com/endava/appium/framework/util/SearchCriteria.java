package com.endava.appium.framework.util;

/**
 *	Search criteria that opium uses to find free devices in Network
 *	If search criteria is met, then tests can be run against that said device
 */
public class SearchCriteria {

	private String model;
	private String sdkVersion;
	private String androidVersion;

	public SearchCriteria(SearchCriteriaBuilder builder) {
		this.model = builder.model;
		this.sdkVersion = builder.sdkVersion;
		this.androidVersion = builder.androidVersion;
	}

	public String getModel() {
		return model;
	}

	public String getSdkVersion() {
		return sdkVersion;
	}

	public String getAndroidVersion() {
		return androidVersion;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}

	@Override
	public String toString() {
		return "Search criteria [model=" + model + ", sdkVersion=" + sdkVersion + ", androidVersion=" + androidVersion
				+ "]";
	}

	public static class SearchCriteriaBuilder {
		private String model;
		private String sdkVersion;
		private String androidVersion;

		public SearchCriteriaBuilder withModel(String model) {
			this.model = model;
			return this;
		}

		public SearchCriteriaBuilder withSdkVersion(String sdkVersion) {
			this.sdkVersion = sdkVersion;
			return this;
		}

		public SearchCriteriaBuilder withAndroidVersion(String androidVersion) {
			this.androidVersion = androidVersion;
			return this;
		}

		public SearchCriteria build() {
			return new SearchCriteria(this);
		}

	}

}
