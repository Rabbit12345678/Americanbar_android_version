package com.spaculus.mediapicker;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.spaculus.americanbars.R;
import com.spaculus.beans.Media;
import com.spaculus.lazylist.ImageLoaderMultipleFileUpload;

public class MediaAdapter extends ArrayAdapter<Media> {
  private static LayoutInflater inflater = null;
  private ArrayList<Media> media;
  @SuppressWarnings("unused")
  private Context context;
  private int layoutItem;
  private Gallery gallery;
  public ImageLoaderMultipleFileUpload imageLoader;

  @SuppressWarnings("static-access")
  public MediaAdapter(Context context, int textViewResourceId, Gallery gallery) {
    super(context, textViewResourceId, gallery.getMedia());
    this.media = gallery.getMedia();
    this.context = context;
    this.layoutItem = textViewResourceId;
    this.gallery = gallery;

    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.imageLoader = new ImageLoaderMultipleFileUpload(context);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View row;

    if (convertView == null) {
      row = inflater.inflate(layoutItem, parent, false);
    }
    else {
      row = convertView;
    }

    ImageView thumbImage = (ImageView)row.findViewById(R.id.mediapicker_grid_image);
    ImageView tickImage = (ImageView)row.findViewById(R.id.mediapicker_grid_tick);

    Integer id = media.get(position).getId();
    imageLoader.DisplayImage(id, gallery.getType(), thumbImage);

    if (gallery.isSelected(position)) {
      tickImage.setVisibility(View.VISIBLE);
    }
    else {
      tickImage.setVisibility(View.GONE);
    }

    return row;
  }

  public static ArrayList<Integer> getMedia(Context c, String bucket, int location, int type) {
    String[] projection;
    String sort;
    String filter;
    Uri loc;
    ArrayList<Integer> found;

    found = new ArrayList<Integer>();

    if (type == 0) {
      projection = new String[]{
        MediaStore.Video.Media._ID
      };

      sort = MediaStore.Video.Media.DATE_ADDED + " DESC";

      if (location == 0) {
        loc = MediaStore.Video.Media.INTERNAL_CONTENT_URI;
      }
      else {
        loc = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
      }

      filter = MediaStore.Video.Media.BUCKET_DISPLAY_NAME + " = '" + bucket + "'";
    }
    else {
      projection = new String[]{
              MediaStore.Images.Media._ID
      };

      sort = MediaStore.Images.Media.DATE_ADDED + " DESC";

      if (location == 0) {
        loc = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
      }
      else {
        loc = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
      }

      filter = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = '" + bucket + "'";
    }

    Cursor cur = c.getContentResolver().query(loc, projection, filter, null, sort);
    if (cur.moveToFirst()) {
      int id;
      int idColumn;

      if (type == 0) {
        idColumn = cur.getColumnIndex(MediaStore.Video.Media._ID);
      }
      else {
        idColumn = cur.getColumnIndex(MediaStore.Images.Media._ID);
      }

      do {
        id = cur.getInt(idColumn);

        found.add(id);
      }
      while (cur.moveToNext());
      cur.close();
    }

    return found;
  }
}
