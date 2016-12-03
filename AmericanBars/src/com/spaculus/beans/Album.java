package com.spaculus.beans;

public class Album {
	
	//  Private Variables
	private String bar_gallery_id = "";
	private String bar_id = "";
	private String title = "";
	private String description = "";
	private String date_added = "";
	private String gallery = "";
	private String status = "";
	private String reorder = "";
	private boolean selected = false;
	
	/* For the My Albums List */
	public Album(String bar_gallery_id, String bar_id, String title, String description, String date_added, String gallery, String status, String reorder, boolean selected) {
		// TODO Auto-generated constructor stub
		this.bar_gallery_id = bar_gallery_id;
		this.bar_id = bar_id;
		this.title = title;
		this.description = description;
		this.date_added = date_added;
		this.gallery = gallery;
		this.status = status;
		this.reorder = reorder;
		this.selected = selected;
	}

	public String getBar_gallery_id() {
		return bar_gallery_id;
	}

	public void setBar_gallery_id(String bar_gallery_id) {
		this.bar_gallery_id = bar_gallery_id;
	}

	public String getBar_id() {
		return bar_id;
	}

	public void setBar_id(String bar_id) {
		this.bar_id = bar_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate_added() {
		return date_added;
	}

	public void setDate_added(String date_added) {
		this.date_added = date_added;
	}

	public String getGallery() {
		return gallery;
	}

	public void setGallery(String gallery) {
		this.gallery = gallery;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReorder() {
		return reorder;
	}

	public void setReorder(String reorder) {
		this.reorder = reorder;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
