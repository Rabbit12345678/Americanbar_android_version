package com.spaculus.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.R;
import com.spaculus.beans.GalleryBean;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.Utils;

public class CustomPhotoGalleryAdapter extends ArrayAdapter<GalleryBean> {

	private Context context;
	private List<GalleryBean> photoGalleryList = null;

	public CustomPhotoGalleryAdapter(Context context, int resourceId, List<GalleryBean> items) {
		super(context, resourceId, items);
		this.context = context;
		this.photoGalleryList = items;
	}

	//  Private View Holder Class
	private class ViewHolder {
		// Mapping of all the views
		ImageView ivGalleryImage;
		TextView tvGalleryName;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return photoGalleryList.size();
	}

	@SuppressLint("InflateParams") 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		try {
			ViewHolder holder = null;
			
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				//  Now, here first of all check whether Phone or Tablet.
				/*if(((BaseActivity) context).isTablet(context)) {
					//Toast.makeText(PhotoGalleryActivity.this, "Tablet", Toast.LENGTH_SHORT).show();
					convertView = inflater.inflate(R.layout.grid_list_item_photo_gallery_tablet, null);
				}
				else {
					//Toast.makeText(PhotoGalleryActivity.this, "Phone", Toast.LENGTH_SHORT).show();
					convertView = inflater.inflate(R.layout.grid_list_item_photo_gallery_phone, null);
				}*/
				convertView = inflater.inflate(R.layout.activity_photo_gallery_list_item, null);
				
				// well set up the ViewHolder
				holder = new ViewHolder();
				
				//  Mapping of all the views
				holder.ivGalleryImage = (ImageView) convertView.findViewById(R.id.ivGalleryImage);
				holder.tvGalleryName = (TextView) convertView.findViewById(R.id.tvGalleryName);
			
				// store the holder with the view.
				convertView.setTag(holder);
			} 
			else {
				// we've just avoided calling findViewById() on resource every time
				// just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}

			//  Now, set the data here.
			
			//  Set Name
			holder.tvGalleryName.setText(Utils.getInstance().setHTMLText(photoGalleryList.get(position).getTitle()));
			
			//  For the ImageView set dimensions
			//  Now, here first of all check whether Phone or Tablet.
			/*if(((BaseActivity) context).isTablet(context)) {
				holder.ivGalleryImage.setLayoutParams(new RelativeLayout.LayoutParams(
						(int)context.getResources().getDimension(R.dimen.grid_view_image_width_tablet),
						(int)context.getResources().getDimension(R.dimen.grid_view_image_height_tablet)));
			}
			else {
				holder.ivGalleryImage.setLayoutParams(new RelativeLayout.LayoutParams(
						(int)context.getResources().getDimension(R.dimen.grid_view_image_width),
						(int)context.getResources().getDimension(R.dimen.grid_view_image_height)));
			}*/
			holder.ivGalleryImage.setLayoutParams(new RelativeLayout.LayoutParams(
					(int)context.getResources().getDimension(R.dimen.grid_view_image_width),
					(int)context.getResources().getDimension(R.dimen.grid_view_image_height)));
			
			//  Set Image
			((BaseActivity) context).setLogo(ConfigConstants.ImageUrls.bar_gallery_thumb_big600by600, photoGalleryList.get(position).getBar_image_name(), holder.ivGalleryImage, R.drawable.gallery_big_place_holder);
		} 
		catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
}


