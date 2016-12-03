package com.spaculus.beans;

public class Beer {

	//  Private Variables
	private String id = "";
	private String name = "";
	private String type = "";
	private String producer = "";
	private String city_produced = "";
	private String beer_image = "";
	private String status = "";
	private String beer_website = "";
	private String date_added = "";
	private boolean selected = false;

	/* For the Beer Search Listing Screen */
	public Beer(String id, String name, String type, String producer, String city_produced, String beer_image, String status, String beer_website) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.name = name;
		this.type = type;
		this.producer = producer;
		this.city_produced = city_produced;
		this.beer_image = beer_image;
		this.status = status;
		this.beer_website = beer_website;
	}
	
	/* For the Full Mug Bar Details Screen */
	public Beer(String id, String name, String type, String producer, String city_produced, String beer_image) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.name = name;
		this.type = type;
		this.producer = producer;
		this.city_produced = city_produced;
		this.beer_image = beer_image;
	}
	
	/* For the My Favorite Beers functionality */
	public Beer(String id, String name, String type, String date_added, boolean selected, String producer, String beer_image) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.name = name;
		this.type = type;
		this.date_added = date_added;
		this.selected = selected;
		this.producer = producer;
		this.beer_image = beer_image;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getCity_produced() {
		return city_produced;
	}

	public void setCity_produced(String city_produced) {
		this.city_produced = city_produced;
	}

	public String getBeer_image() {
		return beer_image;
	}

	public void setBeer_image(String beer_image) {
		this.beer_image = beer_image;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBeer_website() {
		return beer_website;
	}

	public void setBeer_website(String beer_website) {
		this.beer_website = beer_website;
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
