package com.spaculus.beans;

public class Bar {

	private String id = "";
	private String title = "";
	private String type = "";
	private String desciption = "";
	private String owner_id = "";
	private String address = "";
	private String city = "";
	private String state = "";
	private String phone = "";
	private String zipcode = "";
	private String email = "";
	private String bar_logo = "";
	private String total_rating = "";
	private String total_comments = "";
	private String date_added = "";
	private boolean selected = false;
	private String lat = "";
	private String lang = "";

	/* For the Happy Hours list */
	private String happyHoursString = "";

	public Bar() {
	}

	public Bar(String id, String title, String type, String desciption, String owner_id, String address, String city,
			String state, String phone, String zipcode, String email, String bar_logo, String total_rating,
			String total_comments) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.title = title;
		this.type = type;
		this.desciption = desciption;
		this.owner_id = owner_id;
		this.address = address;
		this.city = city;
		this.state = state;
		this.phone = phone;
		this.zipcode = zipcode;
		this.email = email;
		this.bar_logo = bar_logo;
		this.total_rating = total_rating;
		this.total_comments = total_comments;
	}

	public Bar(String id, String title, String type, String desciption, String owner_id, String address, String city,
			String state, String phone, String zipcode, String email, String bar_logo, String total_rating,
			String total_comments, String lat, String lang) {
		this.id = id;
		this.title = title;
		this.type = type;
		this.desciption = desciption;
		this.owner_id = owner_id;
		this.address = address;
		this.city = city;
		this.state = state;
		this.phone = phone;
		this.zipcode = zipcode;
		this.email = email;
		this.bar_logo = bar_logo;
		this.total_rating = total_rating;
		this.total_comments = total_comments;
		this.lat = lat;
		this.lang = lang;
	}

	/* For the My Favorite Bars functionality */
	public Bar(String id, String title, String type, String date_added, boolean selected, String bar_logo) {
		this.id = id;
		this.title = title;
		this.type = type;
		this.date_added = date_added;
		this.selected = selected;
		this.bar_logo = bar_logo;
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

	public String getDesciption() {
		return desciption;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}

	public String getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBar_logo() {
		return bar_logo;
	}

	public void setBar_logo(String bar_logo) {
		this.bar_logo = bar_logo;
	}

	public String getTotal_rating() {
		return total_rating;
	}

	public void setTotal_rating(String total_rating) {
		this.total_rating = total_rating;
	}

	public String getTotal_comments() {
		return total_comments;
	}

	public void setTotal_comments(String total_comments) {
		this.total_comments = total_comments;
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

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getHappyHoursString() {
		return happyHoursString;
	}

	public void setHappyHoursString(String happyHoursString) {
		this.happyHoursString = happyHoursString;
	}
}
