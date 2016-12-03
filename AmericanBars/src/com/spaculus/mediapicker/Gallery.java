package com.spaculus.mediapicker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.spaculus.beans.Media;

public class Gallery {
  private int type;
  private int location;
  @SuppressWarnings("unused")
  private int num;
  private Bitmap thumbnail;
  private String name;
  private HashMap<Integer, Media> selected;
  private ArrayList<Media> media;
  //private ArrayList<MediaItem> mediaList;
  
  String mediaPath = "";
 	String mediaName = "";
 	String mediaExtension = "";
 	long mediaSize = 0;

 	String mediaType = "";


  /*public Gallery() {
    this.name = "";
    this.type = 0;
    this.location = 0;
    this.num = 0;
    this.thumbnail = null;
    this.selected = new HashMap<Integer, MediaItem>();
    //this.media = new ArrayList<Integer>();
    this.media = new ArrayList<MediaItem>();
  }*/

  @SuppressLint("UseSparseArrays") 
  public Gallery (String name, int type, int location, int num, Bitmap thumbnail, String mediaPath, String mediaName, String mediaExtension, long mediaSize, String mediaType) {
    this.name = name;
    this.type = type;
    this.location = location;
    this.mediaPath = mediaPath;
	this.mediaName = mediaName;
	this.mediaExtension = mediaExtension;
	this.mediaSize = mediaSize;
	this.mediaType = mediaType;
    this.num = num;
    this.thumbnail = thumbnail;
    this.selected = new HashMap<Integer, Media>();
    //this.media = new ArrayList<Integer>();
    this.media = new ArrayList<Media>();
    
  }

  /*public void addMedia(int mediaId) {
    this.media.add(mediaId);
  }*/
  
  public void addMedia(Media objMediaItem) {
	    this.media.add(objMediaItem);
	  }

  
  /*public ArrayList<Integer> getMedia() {
    return media;
  }*/
  
  public ArrayList<Media> getMedia() {
	    return media;
	  }

  public int getSelectedCount() {
    return selected.size();
  }

  public Collection<Media> getSelected() {
    return selected.values();
  }

  public boolean isSelected(int index) {
    if (selected.containsKey(index)) {
      return true;
    }
    return false;
  }

  public void select(int index, int id, int type, int location, String mediaPath, String mediaName, String mediaExtension, long mediaSize, String mediaType) {
	  Media m = new Media(id, type, location, mediaPath, mediaName, mediaExtension, mediaSize, mediaType);  
    selected.put(index, m);
  }

  public void deselect(int index) {
    selected.remove(index);
  }

  public int getType() {
    return type;
  }

  public int getNum() {
    return media.size();
  }

  public Bitmap getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(Bitmap thumb) {
    this.thumbnail = thumb;
  }

  public String getName() {
    return name;
  }

  public int getLocation() {
    return location;
  }

  public String getLocationInfo() {
    if (location == 0) {
      return "Internal storage";
    }
    else {
      return "External storage";
    }
  }

  public String getNumInfo() {
    if (type == 0) {
      return this.getNum() + " videos";
    }
    else {
      return this.getNum() + " images";
    }
  }

  @Override
  public String toString() {
    StringBuilder info = new StringBuilder();

    info.append(getLocationInfo());
    info.append(" / ");
    info.append(getNumInfo());

    return info.toString();
  }
}
