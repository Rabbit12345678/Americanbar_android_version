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
import com.spaculus.beans.Bar;
import com.spaculus.helpers.ConfigConstants;

public class CustomBarAdapter extends ArrayAdapter<Bar> {

	private Context context;
	private List<Bar> barList = null;

	public CustomBarAdapter(Context context, int resourceId, List<Bar> items) {
		super(context, resourceId, items);
		this.context = context;
		this.barList = items;
	}

	//  Private View Holder Class
	private class ViewHolder {
		//  Mapping of all the views
		TextView tvBarName;
		TextView tvBarAddress;
		TextView tvBarPhoneNumber;
		ImageView ivBarLogo;
		ImageView ivFullHalfMug;
		ImageView[] imageStarArray = null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return barList.size();
	}

	ViewHolder holder = null;
	
	@SuppressLint("InflateParams") 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		try {
			
			// well set up the ViewHolder
			holder = new ViewHolder();
			
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.activity_bar_search_list_item, null);

				// well set up the ViewHolder
				holder = new ViewHolder();
				
				// Mapping of all the views
				holder.tvBarName = (TextView) convertView.findViewById(R.id.tvBarName);
				holder.tvBarAddress = (TextView) convertView.findViewById(R.id.tvBarAddress);
				holder.tvBarPhoneNumber = (TextView) convertView.findViewById(R.id.tvBarPhoneNumber);
				holder.ivBarLogo = (ImageView) convertView.findViewById(R.id.ivBarLogo);
				holder.ivFullHalfMug = (ImageView) convertView.findViewById(R.id.ivFullHalfMug);
				holder.imageStarArray = new ImageView[5];
				holder.imageStarArray[0] = (ImageView) convertView.findViewById(R.id.ivStar1);
				holder.imageStarArray[1] = (ImageView) convertView.findViewById(R.id.ivStar2);
				holder.imageStarArray[2] = (ImageView) convertView.findViewById(R.id.ivStar3);
				holder.imageStarArray[3] = (ImageView) convertView.findViewById(R.id.ivStar4);
				holder.imageStarArray[4] = (ImageView) convertView.findViewById(R.id.ivStar5);

				// store the holder with the view.
				convertView.setTag(holder);
			} 
			else {
				// we've just avoided calling findViewById() on resource every time
				// just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}
			
			/* Now, set the default view here. */
			recycleView();

			//  Now, set the data here.
			holder.tvBarName.setText(barList.get(position).getTitle());
			holder.tvBarAddress.setText(barList.get(position).getAddress()+", "+barList.get(position).getCity()+", "+barList.get(position).getState()+" "+barList.get(position).getZipcode());
			holder.tvBarPhoneNumber.setText(barList.get(position).getPhone());
			
			//  Now, set the bar logo here
			((BaseActivity)context).setLogo(ConfigConstants.ImageUrls.barlogo_200, barList.get(position).getBar_logo(), holder.ivBarLogo, R.drawable.no_image_bar);
			
			//  Now, for the half or full bug bar image
			if(barList.get(position).getType().equals(ConfigConstants.Constants.BAR_TYPE_FULL_MUG)) {
				holder.ivFullHalfMug.setImageResource(R.drawable.full_mug);
			}
			else {
				holder.ivFullHalfMug.setImageResource(R.drawable.half_mug);
			}
			
			//  Now, for the review count functionality
			((BaseActivity) context).setStarReviews(barList.get(position).getTotal_rating(), barList.get(position).getTotal_comments(), holder.imageStarArray);
			/*int reviewCount = 0;
			//  Now, for the Reviews i.e. (total_rating/total_comments)
			if(barList.get(position).getTotal_rating().equals("null")) {
				reviewCount = 0;
			}
			else {
				reviewCount = Integer.parseInt(barList.get(position).getTotal_rating()) / Integer.parseInt(barList.get(position).getTotal_comments());	
			}*/
			
			/*if(reviewCount==1) {
				holder.imageStarArray[0].setImageResource(R.drawable.star_yellow);
				holder.imageStarArray[1].setImageResource(R.drawable.star_white);
				holder.imageStarArray[2].setImageResource(R.drawable.star_white);
				holder.imageStarArray[3].setImageResource(R.drawable.star_white);
				holder.imageStarArray[4].setImageResource(R.drawable.star_white);
			}
			else if(reviewCount==2) {
				holder.imageStarArray[0].setImageResource(R.drawable.star_yellow);
				holder.imageStarArray[1].setImageResource(R.drawable.star_yellow);
				holder.imageStarArray[2].setImageResource(R.drawable.star_white);
				holder.imageStarArray[3].setImageResource(R.drawable.star_white);
				holder.imageStarArray[4].setImageResource(R.drawable.star_white);
			}
			else if(reviewCount==3) {
				holder.imageStarArray[0].setImageResource(R.drawable.star_yellow);
				holder.imageStarArray[1].setImageResource(R.drawable.star_yellow);
				holder.imageStarArray[2].setImageResource(R.drawable.star_yellow);
				holder.imageStarArray[3].setImageResource(R.drawable.star_white);
				holder.imageStarArray[4].setImageResource(R.drawable.star_white);
			}
			else if(reviewCount==4) {
				holder.imageStarArray[0].setImageResource(R.drawable.star_yellow);
				holder.imageStarArray[1].setImageResource(R.drawable.star_yellow);
				holder.imageStarArray[2].setImageResource(R.drawable.star_yellow);
				holder.imageStarArray[3].setImageResource(R.drawable.star_yellow);
				holder.imageStarArray[4].setImageResource(R.drawable.star_white);
			}
			else if(reviewCount==5) {
				holder.imageStarArray[0].setImageResource(R.drawable.star_yellow);
				holder.imageStarArray[1].setImageResource(R.drawable.star_yellow);
				holder.imageStarArray[2].setImageResource(R.drawable.star_yellow);
				holder.imageStarArray[3].setImageResource(R.drawable.star_yellow);
				holder.imageStarArray[4].setImageResource(R.drawable.star_yellow);
			}*/
			
			/*for(int i=0; i<5; i++) {
				if(i+1 <= reviewCount) {
					holder.imageStarArray[i].setImageResource(R.drawable.star_yellow);
				}
				else {
					holder.imageStarArray[i].setImageResource(R.drawable.star_white);
				}
			}*/
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertView;
	}
	
	/* A method is used to set the default view. */
	private void recycleView() {
		holder.tvBarName.setText("");
		holder.tvBarAddress.setText("");
		holder.tvBarPhoneNumber.setText("");
		holder.ivBarLogo.setImageResource(R.drawable.no_image_bar);
		holder.ivFullHalfMug.setImageResource(R.drawable.full_mug);
		for (int i = 0; i < holder.imageStarArray.length; i++) {
			holder.imageStarArray[i].setImageResource(R.drawable.star_white);
		}
	}
}


