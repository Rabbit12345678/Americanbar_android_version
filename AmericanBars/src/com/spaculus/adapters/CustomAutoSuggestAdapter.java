package com.spaculus.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.spaculus.americanbars.R;
import com.spaculus.beans.Bar;
import com.spaculus.helpers.Utils;

public class CustomAutoSuggestAdapter extends ArrayAdapter<Bar> {

	private Context context;
	private List<Bar> barList = null;

	public CustomAutoSuggestAdapter(Context context, int resourceId, List<Bar> items) {
		super(context, resourceId, items);
		this.context = context;
		this.barList = items;
	}

	//  Private View Holder Class
	private class ViewHolder {
		// Mapping of all the views
		TextView tvName;
		TextView tvAddress;
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
				convertView = inflater.inflate(R.layout.auto_suggest_list_item, null);

				// well set up the ViewHolder
				holder = new ViewHolder();
				
				// Mapping of all the views
				holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
				holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);

				// store the holder with the view.
				convertView.setTag(holder);
			} 
			else {
				// we've just avoided calling findViewById() on resource every time
				// just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}

			//  Now, set the data here.
			holder.tvName.setText(barList.get(position).getTitle());
			holder.tvAddress.setText(Utils.getInstance().setAddressForMap(barList.get(position).getAddress(), barList.get(position).getCity(), barList.get(position).getState(), barList.get(position).getZipcode()));
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertView;
	}
}


