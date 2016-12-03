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

import com.spaculus.americanbars.R;
import com.spaculus.beans.LeftNavigationDrawerItem;
//import android.util.Log;

public class CustomLeftNavigationDrawerAdapter extends ArrayAdapter<LeftNavigationDrawerItem> {

	Context context;
	
	private List<LeftNavigationDrawerItem> leftNavDrawerList = null;
	
	public CustomLeftNavigationDrawerAdapter(Context context, int resourceId, List<LeftNavigationDrawerItem> items) {
		super(context, resourceId, items);
		this.context = context;
		this.leftNavDrawerList = items;
	}

	// Private View Holder Class
	private class ViewHolder {

		// Mapping of all the views
		TextView tvTitle;
		ImageView imageView;
		//TextView tvCounter;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return leftNavDrawerList.size();
	}

	ViewHolder holder = null;

	@SuppressLint("InflateParams") 
	public View getView(final int position, View convertView, ViewGroup parent) {

		try {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.sliding_drawer_list_item, null);

				// well set up the ViewHolder
				holder = new ViewHolder();

				// Mapping of all the views
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
				holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
				//holder.tvCounter = (TextView) convertView.findViewById(R.id.tvCounter);

				// store the holder with the view.
				convertView.setTag(holder);
			} 
			else {
				//Log.i("Convertview is not null.'", "Convertview is not null.'");
				// we've just avoided calling findViewById() on resource every time
				// just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}

			// Now, set all the check box status values
			//Log.i("Position value:", "" + position);
			
			LeftNavigationDrawerItem objLeftNavigationDrawerItem = leftNavDrawerList.get(position);
			
			//  Set title
			holder.tvTitle.setText(objLeftNavigationDrawerItem.getTitle());
			
			//  Set icon
			holder.imageView.setImageResource(objLeftNavigationDrawerItem.getImageResource());
			
     /* //  Set the counter
      //  First of all check whether we need to show/hide a counter.
			if(objLeftNavigationDrawerItem.getCounterVisibility()){
				holder.tvCounter.setText(objLeftNavigationDrawerItem.getCount());
			}else{
				// hide the counter view
				holder.tvCounter.setText("");
			}*/
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertView;
	}
}

