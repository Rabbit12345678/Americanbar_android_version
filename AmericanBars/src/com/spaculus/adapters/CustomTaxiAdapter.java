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
import com.spaculus.beans.Taxi;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.Utils;

public class CustomTaxiAdapter extends ArrayAdapter<Taxi> {

	private Context context;
	private List<Taxi> taxiList = null;

	public CustomTaxiAdapter(Context context, int resourceId, List<Taxi> items) {
		super(context, resourceId, items);
		this.context = context;
		this.taxiList = items;
	}

	//  Private View Holder Class
	private class ViewHolder {
		//  Mapping of all the views
		TextView tvTaxiComapnyName;
		TextView tvTaxiCompanyDescription;
		TextView tvTaxiCompanyAddress;
		ImageView ivTaxiCompanyLogo;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return taxiList.size();
	}

	@SuppressLint("InflateParams") 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		try {
			ViewHolder holder = null;
			
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.activity_taxi_search_list_item, null);

				// well set up the ViewHolder
				holder = new ViewHolder();
				
				// Mapping of all the views
				holder.tvTaxiComapnyName = (TextView) convertView.findViewById(R.id.tvTaxiComapnyName);
				holder.tvTaxiCompanyDescription = (TextView) convertView.findViewById(R.id.tvTaxiCompanyDescription);
				holder.tvTaxiCompanyAddress = (TextView) convertView.findViewById(R.id.tvTaxiCompanyAddress);
				holder.ivTaxiCompanyLogo = (ImageView) convertView.findViewById(R.id.ivTaxiCompanyLogo);
			
				// store the holder with the view.
				convertView.setTag(holder);
			} 
			else {
				// we've just avoided calling findViewById() on resource every time
				// just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}

			//  Now, set the data here.
			holder.tvTaxiComapnyName.setText(taxiList.get(position).getTaxi_company());
			/* Hide the description field if it's empty or blank */
			((BaseActivity)context).showHideView(holder.tvTaxiCompanyDescription, taxiList.get(position).getTaxi_desc());
			
			holder.tvTaxiCompanyAddress.setText(Utils.getInstance().setAddress(taxiList.get(position).getAddress(), taxiList.get(position).getCity(), taxiList.get(position).getState(), taxiList.get(position).getCmpn_zipcode()));
			
			//  Now, set the bar logo here
			((BaseActivity)context).setLogo(ConfigConstants.ImageUrls.user_thumb_200, taxiList.get(position).getTaxi_image(), holder.ivTaxiCompanyLogo, R.drawable.no_image_taxi);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertView;
	}
}


