package com.spaculus.beans;

public class Review {

	// Private Variables
	/* For the Bar */
	private String comment_id = "";// review_id or comment_id
	private String parent_id = ""; // either beer_id or cocktail_id or liquor_id
	private String user_id = "";
	private String comment_title = "";
	private String comment = "";
	private String bar_rating = "";
	private String status = "";
	private String is_deleted = "";
	private String date_added = "";
	private String profile_image = "";
	private String first_name = "";
	private String last_name = "";

	/* For the Beer/Cocktail/Liquor */
	private String is_like = "";
	private String total_like = "";

	public Review() {
	}

	public String getUser_id() {
		return user_id;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getComment_title() {
		return comment_title;
	}

	public void setComment_title(String comment_title) {
		this.comment_title = comment_title;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getBar_rating() {
		return bar_rating;
	}

	public void setBar_rating(String bar_rating) {
		this.bar_rating = bar_rating;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(String is_deleted) {
		this.is_deleted = is_deleted;
	}

	public String getDate_added() {
		return date_added;
	}

	public void setDate_added(String date_added) {
		this.date_added = date_added;
	}

	public String getProfile_image() {
		return profile_image;
	}

	public void setProfile_image(String profile_image) {
		this.profile_image = profile_image;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getIs_like() {
		return is_like;
	}

	public void setIs_like(String is_like) {
		this.is_like = is_like;
	}

	public String getTotal_like() {
		return total_like;
	}

	public void setTotal_like(String total_like) {
		this.total_like = total_like;
	}

	public String getComment_id() {
		return comment_id;
	}

	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}
}
