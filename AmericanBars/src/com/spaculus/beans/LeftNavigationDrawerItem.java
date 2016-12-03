package com.spaculus.beans;

public class LeftNavigationDrawerItem {
	
	private String title;
	private int imageResource = 0;
	
	public LeftNavigationDrawerItem(String title, int imageResouce){
		this.title = title;
		this.imageResource = imageResouce;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getImageResource() {
		return imageResource;
	}

	public void setImageResource(int imageResource) {
		this.imageResource = imageResource;
	}
}
