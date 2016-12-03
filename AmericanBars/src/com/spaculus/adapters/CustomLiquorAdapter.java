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
import com.spaculus.beans.Liquor;
import com.spaculus.helpers.ConfigConstants;

public class CustomLiquorAdapter extends ArrayAdapter<Liquor> {

	private Context context;
	private List<Liquor> liquorList = null;

	public CustomLiquorAdapter(Context context, int resourceId, List<Liquor> items) {
		super(context, resourceId, items);
		this.context = context;
		this.liquorList = items;
	}

	//  Private View Holder Class
	private class ViewHolder {
		//  Mapping of all the views
		TextView tvLiquorName;
		TextView tvLiquorType;
		TextView tvABV;
		TextView tvProducer;
		ImageView ivLiquorLogo;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return liquorList.size();
	}

	@SuppressLint("InflateParams") 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		try {
			ViewHolder holder = null;
			
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.activity_liquor_search_list_item, null);

				// well set up the ViewHolder
				holder = new ViewHolder();
				
				// Mapping of all the views
				holder.tvLiquorName = (TextView) convertView.findViewById(R.id.tvLiquorName);
				holder.tvLiquorType = (TextView) convertView.findViewById(R.id.tvLiquorType);
				holder.tvABV = (TextView) convertView.findViewById(R.id.tvABV);
				holder.tvProducer = (TextView) convertView.findViewById(R.id.tvProducer);
				holder.ivLiquorLogo = (ImageView) convertView.findViewById(R.id.ivLiquorLogo);

				// store the holder with the view.
				convertView.setTag(holder);
			} 
			else {
				// we've just avoided calling findViewById() on resource every time
				// just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}

			//  Now, set the data here.
			holder.tvLiquorName.setText(liquorList.get(position).getTitle());
			holder.tvLiquorType.setText(liquorList.get(position).getType());
			holder.tvABV.setText(liquorList.get(position).getProof());
			holder.tvProducer.setText(liquorList.get(position).getProducer());
			
			//  Now, set the liquor logo here
			((BaseActivity)context).setLogo(ConfigConstants.ImageUrls.liquor_200, liquorList.get(position).getLiquor_image(), holder.ivLiquorLogo, R.drawable.no_image_liquor);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertView;
	}
}


