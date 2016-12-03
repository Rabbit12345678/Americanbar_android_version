package com.spaculus.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class Media implements Parcelable {

	//  Private Variables
	/* For the Multiple Media Selection Functionality */
	private int id = 0;
	private int type = 0;
	private  int location = 0;
	private String mediaPath = "";
	private String mediaName = "";
	private String mediaExtension = "";
	private long mediaSize = 0;
	private String mediaType = "";
	
	/* For the Album functionality */
	private String bar_image_id = "";
	private String bar_gallery_id = "";
	private String bar_image_name = "";
	private String image_title = "";
	
	/* This parameter is used to know whether an image is changed from local or not. */
	private boolean isImageFromWeb = false;
	
	/* This parameter is used to know whether a text is changed from local or not. */
	//private boolean isTextFromWeb = false;
	
	public Media(){}
	
	/* For the Multiple Media Selection Functionality */
	public Media(int id, int type, int location, String mediaPath, String mediaName, String mediaExtension, long mediaSize, String mediaType) {
		this.id = id;
		this.type = type;
		this.location = location;
		this.mediaPath = mediaPath;
		this.mediaName = mediaName;
		this.mediaExtension = mediaExtension;
		this.mediaSize = mediaSize;
		this.mediaType = mediaType;
	}
	
	/* For the Albums Functionality */
	public Media(String bar_image_id, String bar_gallery_id, String bar_image_name, String image_title, boolean isImageFromWeb) {
		this.bar_image_id = bar_image_id;
		this.bar_gallery_id = bar_gallery_id;
		this.bar_image_name = bar_image_name;
		this.image_title = image_title;
		this.isImageFromWeb = isImageFromWeb;
		//this.isTextFromWeb = isTextFromWeb;
	}
	
	/* For the Album's Add/Edit Image Functionality */	
	public Media(String mediaPath, String mediaName, String mediaExtension, long mediaSize, String image_title, String bar_image_id, String bar_image_name, boolean isImageFromWeb) {
		this.mediaPath = mediaPath;
		this.mediaName = mediaName;
		this.mediaExtension = mediaExtension;
		this.mediaSize = mediaSize;
		this.image_title = image_title;
		this.bar_image_id = bar_image_id;
		this.bar_image_name = bar_image_name;
		this.isImageFromWeb = isImageFromWeb;
		//this.isTextFromWeb = isTextFromWeb;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getMediaPath() {
		return mediaPath;
	}

	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}

	public String getMediaExtension() {
		return mediaExtension;
	}

	public void setMediaExtension(String mediaExtension) {
		this.mediaExtension = mediaExtension;
	}

	public long getMediaSize() {
		return mediaSize;
	}

	public void setMediaSize(long mediaSize) {
		this.mediaSize = mediaSize;
	}
	
	public String getBar_image_id() {
		return bar_image_id;
	}

	public void setBar_image_id(String bar_image_id) {
		this.bar_image_id = bar_image_id;
	}

	public String getBar_gallery_id() {
		return bar_gallery_id;
	}

	public void setBar_gallery_id(String bar_gallery_id) {
		this.bar_gallery_id = bar_gallery_id;
	}

	public String getBar_image_name() {
		return bar_image_name;
	}

	public void setBar_image_name(String bar_image_name) {
		this.bar_image_name = bar_image_name;
	}

	public String getImage_title() {
		return image_title;
	}

	public void setImage_title(String image_title) {
		this.image_title = image_title;
	}

	public boolean isImageFromWeb() {
		return isImageFromWeb;
	}

	public void setImageFromWeb(boolean isImageFromWeb) {
		this.isImageFromWeb = isImageFromWeb;
	}

	/*public boolean isTextFromWeb() {
		return isTextFromWeb;
	}

	public void setTextFromWeb(boolean isTextFromWeb) {
		this.isTextFromWeb = isTextFromWeb;
	}*/
	
	/*All the following methods are used for the Multiple Media Selection functionality.*/

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(id);
		parcel.writeInt(type);
		parcel.writeInt(location);
		parcel.writeString(mediaPath);
		parcel.writeString(mediaName);
		parcel.writeString(mediaExtension);
		parcel.writeLong(mediaSize);
		parcel.writeString(mediaType);
	}

	public static final Parcelable.Creator<Media> CREATOR = new Parcelable.Creator<Media>() {
		public Media createFromParcel(Parcel in) {
			return new Media(in);
		}

		public Media[] newArray(int size) {
			return new Media[size];
		}
	};

	private Media(Parcel in) {
		id = in.readInt();
		type = in.readInt();
		location = in.readInt();
		mediaPath = in.readString();
		mediaName = in.readString();
		mediaExtension = in.readString();
		mediaSize = in.readLong();
		mediaType = in.readString();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public static Parcelable.Creator<Media> getCreator() {
			return CREATOR;
	}
	
	/*Web
	
	galImage.gal_bar_gallery_id = [CommonUtils getNotNullString:[dict valueForKey:@"bar_gallery_id"]];
    galImage.gal_bar_image_id = [CommonUtils getNotNullString:[dict valueForKey:@"bar_image_id"]];
    
    
    galImage.gal_bar_image_name = [CommonUtils getNotNullString:[dict valueForKey:@"bar_image_name"]];
    galImage.gal_pre_img_name = galImage.gal_bar_image_name;
    galImage.gal_pre_img_id = galImage.gal_bar_image_id;
    galImage.gal_image_title = [CommonUtils getNotNullString:[dict valueForKey:@"image_title"]];
    galImage.gal_imgEdited = NO;
    galImage.gal_from_WS = YES;
    
    
    Local
    galImage.gal_pre_img_name = [CommonUtils getNotNullString:[dict valueForKey:@"gal_pre_img_name"]];
    
    //if image is changed
    galImage.gal_pre_img_id = [CommonUtils getNotNullString:[dict valueForKey:@"gal_pre_img_id"]];
    galImage.gal_bar_image_name = [CommonUtils getNotNullString:[dict valueForKey:@"bar_image_name"]];
  //if image is changed
    
    galImage.gal_image_title = [CommonUtils getNotNullString:[dict valueForKey:@"image_title"]];
    galImage.gal_type = [CommonUtils getNotNullString:[dict valueForKey:@"type"]];
    galImage.gal_imgEdited = YES;
    galImage.gal_localImage = [dict valueForKey:@"image"];
    
    galImage.gal_from_WS = NO;*/
    
}