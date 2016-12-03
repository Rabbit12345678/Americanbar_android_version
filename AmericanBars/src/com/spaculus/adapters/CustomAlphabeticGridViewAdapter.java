package com.spaculus.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.spaculus.americanbars.R;
import com.spaculus.beans.Alphabet;

public class CustomAlphabeticGridViewAdapter extends ArrayAdapter<Alphabet> {

	private Context context;
	private List<Alphabet> alphabateList = null;

	public CustomAlphabeticGridViewAdapter(Context context, int resourceId, List<Alphabet> items) {
		super(context, resourceId, items);
		this.context = context;
		this.alphabateList = items;
	}

	//  Private View Holder Class
	private class ViewHolder {
		// Mapping of all the views
		Button button;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return alphabateList.size();
	}

	@SuppressLint("InflateParams") 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		try {
			ViewHolder holder = null;
			
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.grid_list_item_alphabetic, null);

				// well set up the ViewHolder
				holder = new ViewHolder();
				
				// Mapping of all the views
				holder.button = (Button) convertView.findViewById(R.id.btn);

				// store the holder with the view.
				convertView.setTag(holder);
			} 
			else {
				// we've just avoided calling findViewById() on resource every time
				// just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}

			//  Now, set the data here.
			holder.button.setText(alphabateList.get(position).getName());
			
			//  Now, To maintain the selected text
			if(alphabateList.get(position).isSelected()) {
				holder.button.setTextColor(context.getResources().getColor(R.color.lightBrownColor));
				alphabateList.get(position).setSelected(true);
			}
			else {
				holder.button.setTextColor(context.getResources().getColor(R.color.whiteColor));
				alphabateList.get(position).setSelected(false);
			}
		} 
		catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}
}


