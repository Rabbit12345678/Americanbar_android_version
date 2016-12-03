package com.spaculus.mediapicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.spaculus.americanbars.R;
import com.spaculus.beans.Media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleryAdapter extends ArrayAdapter<Gallery> {
  ArrayList<Gallery> galleries;
  Context context;
  int layoutItem;

  public GalleryAdapter(Context context, int textViewResourceId, Gallery[] objects) {
    super(context, textViewResourceId, objects);
    this.galleries = new ArrayList<Gallery>(Arrays.asList(objects));
    this.context = context;
    this.layoutItem = textViewResourceId;
  }

  @SuppressLint("ViewHolder") 
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

    View row = inflater.inflate(layoutItem, parent, false);

    TextView nameLabel = (TextView)row.findViewById(R.id.mediapicker_gallery_name);
    TextView numLabel = (TextView)row.findViewById(R.id.mediapicker_gallery_num);
    TextView locationLabel = (TextView)row.findViewById(R.id.mediapicker_gallery_location);
    ImageView thumbImage = (ImageView)row.findViewById(R.id.mediapicker_gallery_image);

    Gallery g = galleries.get(position);

    nameLabel.setText(g.getName());
    numLabel.setText(g.getNumInfo());
    locationLabel.setText(g.getLocationInfo());
    thumbImage.setImageBitmap(g.getThumbnail());

    return row;
  }

  public static Gallery[] loadData(Context c, ArrayList<Integer> ignoreIds, int type, String mediaType) {
    ArrayList<Gallery> galleries = new ArrayList<Gallery>();

    
    /*galleries.addAll(getGalleries(c, 0, 0, ignoreIds));
    galleries.addAll(getGalleries(c, 0, 1, ignoreIds));
    galleries.addAll(getGalleries(c, 1, 0, ignoreIds));
    galleries.addAll(getGalleries(c, 1, 1, ignoreIds));*/

    galleries.addAll(getGalleries(c, 0, type, ignoreIds, mediaType));
    galleries.addAll(getGalleries(c, 1, type, ignoreIds, mediaType));
    
    return galleries.toArray(new Gallery[galleries.size()]);
  }

  private static ArrayList<Gallery> getGalleries(Context c, int location, int type, ArrayList<Integer> ignoreIds, String mediaType) {
    String[] projection;
    String sort;

    Uri loc;
    HashMap<String, Gallery> found;

    found = new HashMap<String, Gallery>();

    if (type == 0) {
      projection = new String[]{
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Video.Media.DATE_ADDED,
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media.DISPLAY_NAME,
      };

      sort = MediaStore.Video.Media._ID + " DESC";

      if (location == 0) {
        loc = MediaStore.Video.Media.INTERNAL_CONTENT_URI;
      }
      else {
        loc = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
      }
    }
    else {
      projection = new String[]{
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.DISPLAY_NAME
      };

      sort = MediaStore.Images.Media._ID + " DESC";

      if (location == 0) {
        loc = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
      }
      else {
        loc = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
      }
    }

    Cursor cur = c.getContentResolver().query(loc, projection, null, null, sort);
    if (cur.moveToFirst()) {
      int id;
      String bucket;
      String data;
      long size;
      String displayName;
      String mediaExtension = "";
     
      @SuppressWarnings("unused")
      int idColumn, bucketColumn, dateColumn;
      int dataColumn, sizeColumn, displayNameColumn;

      if (type == 0) {
        idColumn = cur.getColumnIndex(MediaStore.Video.Media._ID);
        bucketColumn = cur.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        dateColumn = cur.getColumnIndex(MediaStore.Video.Media.DATE_ADDED);
        dataColumn = cur.getColumnIndex(MediaStore.Video.Media.DATA);
        sizeColumn = cur.getColumnIndex(MediaStore.Video.Media.SIZE);
        displayNameColumn = cur.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
      }
      else {
        idColumn = cur.getColumnIndex(MediaStore.Images.Media._ID);
        bucketColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        dateColumn = cur.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
        dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
        sizeColumn = cur.getColumnIndex(MediaStore.Images.Media.SIZE);
        displayNameColumn = cur.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
      }

      do {
        id = cur.getInt(idColumn);
        bucket = cur.getString(bucketColumn);
        data = cur.getString(dataColumn);
        size = cur.getLong(sizeColumn);
        displayName = cur.getString(displayNameColumn);
        if (ignoreIds.contains(id)) {
          continue;
        }

        Gallery g = null;
      
        if (found.containsKey(bucket)) {
          g = found.get(bucket);
          //g.addMedia(id);
       // File Extension
       			int dotposition = displayName.lastIndexOf(".");
       			mediaExtension = "."
       					+ displayName.substring(dotposition + 1, displayName.length());
       			//Log.i("mediaExtension", mediaExtension);
       			
       			
          g.addMedia(new Media(id, type, location, data, displayName, mediaExtension, size, mediaType));
        }
        else {
          g = new Gallery(bucket, type, location, 1, null, data, displayName, mediaExtension, size, mediaType);

          Bitmap thumb;
          BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize = 1;
			options.outHeight=(int)c.getResources().getDimension(R.dimen.dim_80);
			options.outWidth=(int)c.getResources().getDimension(R.dimen.dim_80);
			if (type == 0) {
				thumb = MediaStore.Video.Thumbnails.getThumbnail(c.getContentResolver(), id,
						MediaStore.Video.Thumbnails.MINI_KIND, options);
			} else {
				thumb = MediaStore.Images.Thumbnails.getThumbnail(c.getContentResolver(), id,
						MediaStore.Images.Thumbnails.MINI_KIND, options);
			}
          

          if (thumb == null) {
            g = null;
          }
          else {
            //g.addMedia(id);
        	// File Extension
     			int dotposition = displayName.lastIndexOf(".");
     			mediaExtension = "."
     					+ displayName.substring(dotposition + 1, displayName.length());
     			//Log.i("mediaExtension", mediaExtension);
     		
     			
        g.addMedia(new Media(id, type, location, data, displayName, mediaExtension, size, mediaType));
            g.setThumbnail(thumb);
          }
        }

        if (g != null) {
          found.put(bucket, g);
        }
      } while (cur.moveToNext());
      cur.close();
    }

    return new ArrayList<Gallery>(found.values());
  }
}
