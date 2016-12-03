package com.spaculus.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.spaculus.americanbars.R;
import com.spaculus.americanbars.fragments.FragmentPrivacySettings;
import com.spaculus.beans.PrivacySettings;
import com.spaculus.helpers.ConfigConstants;

public class CustomPrivacySettingsAdapter extends ArrayAdapter<PrivacySettings> {

	private Context context;
	private List<PrivacySettings> privacySettingsList = null;
	private FragmentPrivacySettings objFragmentPrivacySettings = null;

	public CustomPrivacySettingsAdapter(Context context, int resourceId, List<PrivacySettings> items, FragmentPrivacySettings fragmentPrivacySettings) {
		super(context, resourceId, items);
		this.context = context;
		this.privacySettingsList = items;
		this.objFragmentPrivacySettings = fragmentPrivacySettings;
	}

	//  Private View Holder Class
	private class ViewHolder {
		// Mapping of all the views
		TextView tvTitle;
		RadioButton radioButtonShow, radioButtonHide;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return privacySettingsList.size();
	}

	@SuppressLint("InflateParams") 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		try {
			ViewHolder holder = null;
			
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.fragment_privacy_settings_list_item, null);

				// well set up the ViewHolder
				holder = new ViewHolder();
				
				// Mapping of all the views
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
				holder.radioButtonShow = (RadioButton) convertView.findViewById(R.id.radioButtonShow);
				holder.radioButtonHide = (RadioButton) convertView.findViewById(R.id.radioButtonHide);

				// store the holder with the view.
				convertView.setTag(holder);
			} 
			else {
				// we've just avoided calling findViewById() on resource every time
				// just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}
			
			//  Radio Button Show click event
			holder.radioButtonShow.setOnClickListener( new View.OnClickListener() {
				public void onClick(View v) {
					try {
						RadioButton radioButton = (RadioButton) v ;
						if(radioButton.isChecked()) {
							privacySettingsList.get(position).setHideSelected(ConfigConstants.Constants.CONSTANT_ONE);
						}
						//  Show the button's layout
						objFragmentPrivacySettings.showButtonsLayout();
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
			     }  
			});  

			//  Radio Button Hide click event
			holder.radioButtonHide.setOnClickListener( new View.OnClickListener() {
				public void onClick(View v) {
					try {
						RadioButton radioButton = (RadioButton) v ;
						if(radioButton.isChecked()) {
							privacySettingsList.get(position).setHideSelected(ConfigConstants.Constants.CONSTANT_ZERO);
						}
						//  Show the button's layout
						objFragmentPrivacySettings.showButtonsLayout();
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
			     }  
			});  
			
			//  Now, set the data here.
			holder.tvTitle.setText(privacySettingsList.get(position).getTitle());
			
			//  For the Radio buttons
			//  Means no button is selected initially.
			if(privacySettingsList.get(position).getHideSelected().equals("-1")) {
				//  Means No buttons are selected initially
				holder.radioButtonShow.setChecked(false);
				holder.radioButtonHide.setChecked(false);
			}
			else if(privacySettingsList.get(position).getHideSelected().equals(ConfigConstants.Constants.CONSTANT_ONE)) {
				//  Means Show
				holder.radioButtonShow.setChecked(true);
				holder.radioButtonHide.setChecked(false);
			}
			else {
				//  Means Hide
				holder.radioButtonShow.setChecked(false);
				holder.radioButtonHide.setChecked(true);
			}
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
}


