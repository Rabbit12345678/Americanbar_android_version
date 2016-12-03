package com.spaculus.adapters;

import java.util.List;

import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.R;
import com.spaculus.beans.Review;
import com.spaculus.helpers.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomBarReviewsAdapter extends ArrayAdapter<Review> {

	private Context context;
	private List<Review> barReviewsList = null;

	public CustomBarReviewsAdapter(Context context, int resourceId, List<Review> items) {
		super(context, resourceId, items);
		this.context = context;
		this.barReviewsList = items;
	}

	// Private View Holder Class
	private class ViewHolder {
		// Mapping of all the views
		TextView tvRaveTitle;
		//TextView tvRaveFormattedDate;
		TextView tvRaveDesc;
		TextView tvRaveDateTime;
		TextView tvRaveUserName;
		ImageView[] imageStarReviewArray = null;
	}

	@Override
	public int getCount() {
		return barReviewsList.size();
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		try {
			ViewHolder holder = null;

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.activity_bar_details_reviews_list_item, null);

				// well set up the ViewHolder
				holder = new ViewHolder();

				// Mapping of all the views
				holder.tvRaveTitle = (TextView) convertView.findViewById(R.id.tvRaveTitle);
				//holder.tvRaveFormattedDate = (TextView) convertView.findViewById(R.id.tvRaveFormattedDate);
				holder.tvRaveDesc = (TextView) convertView.findViewById(R.id.tvRaveDesc);
				holder.tvRaveDateTime = (TextView) convertView.findViewById(R.id.tvRaveDateTime);
				holder.tvRaveUserName = (TextView) convertView.findViewById(R.id.tvRaveUserName);
				holder.imageStarReviewArray = new ImageView[5];
				holder.imageStarReviewArray[0] = (ImageView) convertView.findViewById(R.id.ivStarRave1);
				holder.imageStarReviewArray[1] = (ImageView) convertView.findViewById(R.id.ivStarRave2);
				holder.imageStarReviewArray[2] = (ImageView) convertView.findViewById(R.id.ivStarRave3);
				holder.imageStarReviewArray[3] = (ImageView) convertView.findViewById(R.id.ivStarRave4);
				holder.imageStarReviewArray[4] = (ImageView) convertView.findViewById(R.id.ivStarRave5);

				// store the holder with the view.
				convertView.setTag(holder);
			} else {
				// we've just avoided calling findViewById() on resource every
				// time
				// just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}

			/* Now, set the data here */
			holder.tvRaveTitle.setText(barReviewsList.get(position).getComment_title());
			holder.tvRaveDesc.setText(Utils.getInstance().setHTMLText(barReviewsList.get(position).getComment()));
			holder.tvRaveDateTime.setText(barReviewsList.get(position).getDate_added());
			holder.tvRaveUserName.setText(
					barReviewsList.get(position).getFirst_name() + " " + barReviewsList.get(position).getLast_name());
			((BaseActivity) context).setStars(Integer.parseInt(barReviewsList.get(position).getBar_rating()),
					holder.imageStarReviewArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
}
