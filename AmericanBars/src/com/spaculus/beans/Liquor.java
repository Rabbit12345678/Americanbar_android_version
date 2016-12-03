package com.spaculus.beans;

public class Liquor {

	//  Private Variables
	private String id = "";
	private String title = "";
	private String type = "";
	private String proof = "";
	private String producer = "";
	private String status = "";
	private String liquor_image = "";
	private String date_added = "";
	private boolean selected = false;
	
	/* For the Liquor Search Listing Screen */
	public Liquor(String id, String title, String type, String proof, String producer, String status, String liquor_image) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.title = title;
		this.type = type;
		this.proof = proof;
		this.producer = producer;
		this.status = status;
		this.liquor_image = liquor_image;
	}

	/* For the Full Mug Bar Details Screen */
	public Liquor(String id, String title, String type, String proof, String producer, String liquor_image) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.title = title;
		this.type = type;
		this.proof = proof;
		this.producer = producer;
		this.liquor_image = liquor_image;
	}
	
	/* For the My Favorite Liquors functionality */
	public Liquor(String id, String title, String type, String date_added, boolean selected, String producer, String liquor_image) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.title = title;
		this.type = type;
		this.date_added = date_added;
		this.selected = selected;
		this.producer = producer;
		this.liquor_image = liquor_image;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProof() {
		return proof;
	}

	public void setProof(String proof) {
		this.proof = proof;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLiquor_image() {
		return liquor_image;
	}

	public void setLiquor_image(String liquor_image) {
		this.liquor_image = liquor_image;
	}

	public String getDate_added() {
		return date_added;
	}

	public void setDate_added(String date_added) {
		this.date_added = date_added;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
