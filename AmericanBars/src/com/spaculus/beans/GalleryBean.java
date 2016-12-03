package com.spaculus.beans;

public class GalleryBean {

	//  Private Variables
	private String bar_gallery_id = "";
	private String image_title = "";
	private String bar_image_name = "";
	private String title = "";
	
	/* For the Full Mug Bar Screen */
	/* For the Event Details Screen */
	public GalleryBean(String bar_image_name) {
		// TODO Auto-generated constructor stub
		this.bar_image_name = bar_image_name;
	}
	
	/* For the Photo Gallery Details */
	public GalleryBean(String bar_image_name, String image_title) {
		// TODO Auto-generated constructor stub
		this.bar_image_name = bar_image_name;
		this.image_title = image_title;
	}
	
	/* For the Photo Gallery */
	public GalleryBean(String bar_gallery_id, String image_title, String bar_image_name, String title) {
		// TODO Auto-generated constructor stub
		this.bar_gallery_id = bar_gallery_id;
		this.image_title = image_title;
		this.bar_image_name = bar_image_name;
		this.title = title;
	}

	public String getBar_gallery_id() {
		return bar_gallery_id;
	}

	public void setBar_gallery_id(String bar_gallery_id) {
		this.bar_gallery_id = bar_gallery_id;
	}

	public String getImage_title() {
		return image_title;
	}

	public void setImage_title(String image_title) {
		this.image_title = image_title;
	}

	public String getBar_image_name() {
		return bar_image_name;
	}

	public void setBar_image_name(String bar_image_name) {
		this.bar_image_name = bar_image_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
