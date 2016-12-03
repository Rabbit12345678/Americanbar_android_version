package com.spaculus.adapters;

import java.util.List;

import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.R;
import com.spaculus.americanbars.fragments.FragmentMyFavouriteBars;
import com.spaculus.beans.Bar;
import com.spaculus.helpers.ConfigConstants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomMyFavoriteBarsAdapter extends ArrayAdapter<Bar> {

	private Context context;
	private List<Bar> barList = null;
	private FragmentMyFavouriteBars objFragmentMyFavouriteBars = null;

	public CustomMyFavoriteBarsAdapter(Context c, int resourceId, List<Bar> items, FragmentMyFavouriteBars fragmentMyFavouriteBars) {
		super(c, resourceId, items);
		this.context = c;
		this.barList = items;
		this.objFragmentMyFavouriteBars = fragmentMyFavouriteBars;
	}

	//  Private View Holder Class
	private class ViewHolder {
		//  Mapping of all the views
		TextView tvTitleContent;
		TextView tvTypeContent;
		TextView tvDateContent;
		CheckBox checkBoxMyFavoriteBar;
		ImageView ivDeleteFavoriteBar;
		ImageView ivBarLogo;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return barList.size();
	}

	@SuppressLint("InflateParams") 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		try {
			ViewHolder holder = null;
			
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.fragment_my_favorite_bars_list_item, null);

				// well set up the ViewHolder
				holder = new ViewHolder();
				
				// Mapping of all the views
				holder.tvTitleContent = (TextView) convertView.findViewById(R.id.tvTitleContent);
				holder.tvTypeContent = (TextView) convertView.findViewById(R.id.tvTypeContent);
				holder.tvDateContent = (TextView) convertView.findViewById(R.id.tvDateContent);
				holder.checkBoxMyFavoriteBar = (CheckBox) convertView.findViewById(R.id.checkBoxMyFavoriteBar);
				holder.ivDeleteFavoriteBar = (ImageView) convertView.findViewById(R.id.ivDeleteFavoriteBar);
				holder.ivBarLogo = (ImageView) convertView.findViewById(R.id.ivBarLogo);
				
				// store the holder with the view.
				convertView.setTag(holder);
			} 
			else {
				// we've just avoided calling findViewById() on resource every time
				// just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}

			//  CheckBox click event
			holder.checkBoxMyFavoriteBar.setOnClickListener( new View.OnClickListener() {
				public void onClick(View v) {
					try {
						CheckBox cb = (CheckBox) v ;
						Bar objBar = (Bar) cb.getTag();  
						//Toast.makeText(context, "Clicked on Checkbox: " + cb.getText() +" is " + cb.isChecked(), Toast.LENGTH_LONG).show();
						objBar.setSelected(cb.isChecked());
						//  Now, check need to change the "Select All" button text or not
						objFragmentMyFavouriteBars.checkButtonText();
					} 
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			     }  
			});  
			
			//  Delete image functionality
			holder.ivDeleteFavoriteBar.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						//  To delete a single bar
						objFragmentMyFavouriteBars.showAlertDialogMethod(barList.get(position).getId());
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  Now, set the data here.
			
			//  Title
			holder.tvTitleContent.setText(barList.get(position).getTitle());
			
			//  Type
			if(barList.get(position).getType().equals(ConfigConstants.Constants.BAR_TYPE_FULL_MUG)) {
				holder.tvTypeContent.setText(ConfigConstants.Constants.FULL_MUG);	
			}
			else {
				holder.tvTypeContent.setText(ConfigConstants.Constants.HALF_MUG);
			}

			//  Date
			//holder.tvDateContent.setText(Utils.getInstance().convertDateTime(barList.get(position).getDate_added()));
			holder.tvDateContent.setText(barList.get(position).getDate_added());

			//  CheckBox Status
			holder.checkBoxMyFavoriteBar.setChecked(barList.get(position).isSelected());
			holder.checkBoxMyFavoriteBar.setTag(barList.get(position));
			
			//  Now, set the bar logo here
			((BaseActivity)context).setLogo(ConfigConstants.ImageUrls.barlogo_200, barList.get(position).getBar_logo(), holder.ivBarLogo, R.drawable.no_image_bar);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertView;
	}
}


