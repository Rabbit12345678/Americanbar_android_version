package com.spaculus.beans;

public class PrivacySettings {

	// Private Variables
	private String title = "";
	/* 0 for hide and 1 for show */
	private String hideSelected = "0";
	private String defaultHideSelected = "0";

	public PrivacySettings(String title, String hideSelected, String defaultHideSelected) {
		this.title = title;
		this.hideSelected = hideSelected;
		this.defaultHideSelected = defaultHideSelected;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHideSelected() {
		return hideSelected;
	}

	public void setHideSelected(String hideSelected) {
		this.hideSelected = hideSelected;
	}

	public String getDefaultHideSelected() {
		return defaultHideSelected;
	}

	public void setDefaultHideSelected(String defaultHideSelected) {
		this.defaultHideSelected = defaultHideSelected;
	}

}
