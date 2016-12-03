package com.spaculus.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.R;
import com.spaculus.beans.LeftNavigationDrawerItem;
import com.spaculus.helpers.CircularImageView;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;
//import android.util.Log;

public class CustomRightMenuAdapter extends ArrayAdapter<LeftNavigationDrawerItem> {

	Context context;
	
	private List<LeftNavigationDrawerItem> listRightMenu = null;
	
	public CustomRightMenuAdapter(Context context, int resourceId, List<LeftNavigationDrawerItem> items) {
		super(context, resourceId, items);
		this.context = context;
		this.listRightMenu = items;
	}

	// Private View Holder Class
	private class ViewHolder {

		// Mapping of all the views
		TextView tvTitle;
		ImageView imageView;
		CircularImageView imageView1;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listRightMenu.size();
	}

	ViewHolder holder = null;

	@SuppressLint("InflateParams") 
	public View getView(final int position, View convertView, ViewGroup parent) {

		try {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				convertView = inflater.inflate(R.layout.right_menu_list_item, null);

				// well set up the ViewHolder
				holder = new ViewHolder();

				// Mapping of all the views
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
				holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
				holder.imageView1= (CircularImageView) convertView.findViewById(R.id.imageView1);

				// store the holder with the view.
				convertView.setTag(holder);
			} 
			else {
				//  Just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}
			
			//  Now, set the data here
			if(listRightMenu.get(position).getTitle().equals(ConfigConstants.Constants.USER_PROFILE)) {
				holder.imageView.setVisibility(View.GONE);
				holder.imageView1.setVisibility(View.VISIBLE);
				//  Set title
				holder.tvTitle.setText(Utils.getInstance().setCapitalLetter(SessionManager.getInstance(context).getData(SessionManager.KEY_NAME)));
				//  Set icon
				((BaseActivity) context).setLogo(ConfigConstants.ImageUrls.profilePictureURL, SessionManager.getInstance(context).getData(SessionManager.KEY_PROFILE_IMAGE_NAME), holder.imageView1, R.drawable.no_profile_picture);
			}
			else {
				holder.imageView.setVisibility(View.VISIBLE);
				holder.imageView1.setVisibility(View.GONE);
				//  Set title
				holder.tvTitle.setText(listRightMenu.get(position).getTitle());
				//  Set icon
				holder.imageView.setImageResource(listRightMenu.get(position).getImageResource());   
			}
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return convertView;
	}
}

