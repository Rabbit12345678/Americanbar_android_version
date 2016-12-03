package com.spaculus.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.R;
import com.spaculus.beans.GalleryBean;

public class GalleryImageAdapter extends ArrayAdapter<GalleryBean> {

	private Context context;
	private List<GalleryBean> galleryImagesList = null;
	private String galleySmallImageURL = "";

	public GalleryImageAdapter(Context context, int resourceId, List<GalleryBean> items, String imageURL) {
		super(context, resourceId, items);
		this.context = context;
		this.galleryImagesList = items;
		this.galleySmallImageURL = imageURL;
	}

	//  Private View Holder Class
	private class ViewHolder {
		// Mapping of all the views
		ImageView imageView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return galleryImagesList.size();
	}

	@SuppressLint("InflateParams") 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		try {
			ViewHolder holder = null;
			
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.gallery_custom_image_view, null);

				// well set up the ViewHolder
				holder = new ViewHolder();
				
				// Mapping of all the views
				holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			
				// store the holder with the view.
				convertView.setTag(holder);
			} 
			else {
				// we've just avoided calling findViewById() on resource every time
				// just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}

			//  Now, set the bar gallery image here
			((BaseActivity)context).setLogo(galleySmallImageURL, galleryImagesList.get(position).getBar_image_name(), holder.imageView, R.drawable.gallery_small_place_holder);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertView;
	}
}


