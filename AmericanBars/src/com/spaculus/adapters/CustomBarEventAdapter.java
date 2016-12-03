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
import com.spaculus.beans.BarEvent;
import com.spaculus.helpers.ConfigConstants;

public class CustomBarEventAdapter extends ArrayAdapter<BarEvent> {

	private Context context;
	private List<BarEvent> barEventsList = null;

	public CustomBarEventAdapter(Context context, int resourceId, List<BarEvent> items) {
		super(context, resourceId, items);
		this.context = context;
		this.barEventsList = items;
	}

	//  Private View Holder Class
	private class ViewHolder {
		//  Mapping of all the views
		ImageView ivBarEventLogo;
		TextView tvBarEventName;
		TextView tvBarEventDesc;
		TextView tvBarEventDateTime;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return barEventsList.size();
	}

	@SuppressLint("InflateParams") 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		try {
			ViewHolder holder = null;
			
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.activity_bar_details_bar_events_list_item, null);

				// well set up the ViewHolder
				holder = new ViewHolder();
				
				// Mapping of all the views
				holder.ivBarEventLogo = (ImageView) convertView.findViewById(R.id.ivBarEventLogo);
				holder.tvBarEventName = (TextView) convertView.findViewById(R.id.tvBarEventName);
				holder.tvBarEventDesc = (TextView) convertView.findViewById(R.id.tvBarEventDesc);
				holder.tvBarEventDateTime = (TextView) convertView.findViewById(R.id.tvBarEventDateTime);

				// store the holder with the view.
				convertView.setTag(holder);
			} 
			else {
				// we've just avoided calling findViewById() on resource every time
				// just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}

			//  Now, set the data here.
			//  Set Logo
			((BaseActivity) context).setLogo(ConfigConstants.ImageUrls.event_140, barEventsList.get(position).getEvent_image(), holder.ivBarEventLogo, R.drawable.no_image_bar_event);
			
			//  Set Name
			holder.tvBarEventName.setText(barEventsList.get(position).getTitle());
			
			//  Set Description
			holder.tvBarEventDesc.setText(barEventsList.get(position).getDesc());
			
			//  Set Date-Time
			holder.tvBarEventDateTime.setText(barEventsList.get(position).getStart_date());
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return convertView;
	}
}


