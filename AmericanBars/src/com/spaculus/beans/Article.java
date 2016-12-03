package com.spaculus.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {

	private String blog_description_with_image = "";
	private String blog_description = "";
	private String blog_image = "";
	private String date_added = "";
	private String blog_id = "";
	private String blog_title = "";
	private String total_rating = "";
	
	public Article() {}

	public String getBlog_id() {
		return blog_id;
	}

	public void setBlog_id(String blog_id) {
		this.blog_id = blog_id;
	}

	public String getBlog_title() {
		return blog_title;
	}

	public void setBlog_title(String blog_title) {
		this.blog_title = blog_title;
	}

	public String getBlog_image() {
		return blog_image;
	}

	public void setBlog_image(String blog_image) {
		this.blog_image = blog_image;
	}

	public String getBlog_description() {
		return blog_description;
	}

	public void setBlog_description(String blog_description) {
		this.blog_description = blog_description;
	}

	public String getDate_added() {
		return date_added;
	}

	public void setDate_added(String date_added) {
		this.date_added = date_added;
	}

	public String getTotal_rating() {
		return total_rating;
	}

	public void setTotal_rating(String total_rating) {
		this.total_rating = total_rating;
	}
	
	public String getBlog_description_with_image() {
		return blog_description_with_image;
	}

	public void setBlog_description_with_image(String blog_description_with_image) {
		this.blog_description_with_image = blog_description_with_image;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(blog_id);
		parcel.writeString(blog_title);
		parcel.writeString(blog_image);
		parcel.writeString(blog_description);
		parcel.writeString(date_added);
		parcel.writeString(total_rating);
		parcel.writeString(blog_description_with_image);
	}

	public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
		public Article createFromParcel(Parcel in) {
			return new Article(in);
		}

		public Article[] newArray(int size) {
			return new Article[size];
		}
	};

	private Article(Parcel in) {
		blog_id = in.readString();
		blog_title = in.readString();
		blog_image = in.readString();
		blog_description = in.readString();
		date_added = in.readString();
		total_rating = in.readString();
		blog_description_with_image = in.readString();
	}

	public static Parcelable.Creator<Article> getCreator() {
		return CREATOR;
	}
}
