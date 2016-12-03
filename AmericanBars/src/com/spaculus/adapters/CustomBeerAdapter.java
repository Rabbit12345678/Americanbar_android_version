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
import com.spaculus.beans.Beer;
import com.spaculus.helpers.ConfigConstants;

public class CustomBeerAdapter extends ArrayAdapter<Beer> {

	private Context context;
	private List<Beer> beerList = null;

	public CustomBeerAdapter(Context context, int resourceId, List<Beer> items) {
		super(context, resourceId, items);
		this.context = context;
		this.beerList = items;
	}

	//  Private View Holder Class
	private class ViewHolder {
		//  Mapping of all the views
		TextView tvBeerName;
		TextView tvBeerType;
		TextView tvBrewedBy;
		TextView tvCityProduced;
		TextView tvWebsite;
		ImageView ivBeerLogo;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return beerList.size();
	}

	@SuppressLint("InflateParams") 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		try {
			ViewHolder holder = null;
			
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.activity_beer_search_list_item, null);

				// well set up the ViewHolder
				holder = new ViewHolder();
				
				// Mapping of all the views
				holder.tvBeerName = (TextView) convertView.findViewById(R.id.tvBeerName);
				holder.tvBeerType = (TextView) convertView.findViewById(R.id.tvBeerType);
				holder.tvBrewedBy = (TextView) convertView.findViewById(R.id.tvBrewedBy);
				holder.tvCityProduced = (TextView) convertView.findViewById(R.id.tvCityProduced);
				holder.tvWebsite = (TextView) convertView.findViewById(R.id.tvWebsite);
				holder.ivBeerLogo = (ImageView) convertView.findViewById(R.id.ivBeerLogo);

				// store the holder with the view.
				convertView.setTag(holder);
			} 
			else {
				// we've just avoided calling findViewById() on resource every time
				// just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}

			//  Now, set the data here.
			holder.tvBeerName.setText(beerList.get(position).getName());
			holder.tvBeerType.setText(beerList.get(position).getType());
			holder.tvBrewedBy.setText(beerList.get(position).getProducer());
			holder.tvCityProduced.setText(beerList.get(position).getCity_produced());
			holder.tvWebsite.setText(beerList.get(position).getBeer_website());
			
			//  Now, set the beer logo here
			((BaseActivity)context).setLogo(ConfigConstants.ImageUrls.beer_200, beerList.get(position).getBeer_image(), holder.ivBeerLogo, R.drawable.no_image_beer);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertView;
	}
}


