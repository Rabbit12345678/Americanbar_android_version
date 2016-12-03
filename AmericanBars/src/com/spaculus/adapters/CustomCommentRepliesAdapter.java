package com.spaculus.adapters;

import java.util.List;

import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.R;
import com.spaculus.americanbars.BeerCommentRepliesActivity;
import com.spaculus.americanbars.CocktailCommentRepliesActivity;
import com.spaculus.americanbars.LiquorCommentRepliesActivity;
import com.spaculus.beans.Reply;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomCommentRepliesAdapter extends ArrayAdapter<Reply> {

	private Context context;
	private List<Reply> commentRepliesList = null;
	private Activity activity = null;
	private String isRedirectedFrom = "";
	
	public CustomCommentRepliesAdapter(Context context, int resourceId, List<Reply> items, Activity a, String flag) {
		super(context, resourceId, items);
		this.context = context;
		this.commentRepliesList = items;
		this.activity = a;
		this.isRedirectedFrom = flag;
	}

	// Private View Holder Class
	private class ViewHolder {
		// Mapping of all the views
		ImageView ivProfilePicture;
		TextView tvName;
		TextView tvReply;
		TextView tvReplyDateTime;
		ImageView ivDeleteReply;
	}

	@Override
	public int getCount() {
		return commentRepliesList.size();
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		try {
			ViewHolder holder = null;

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.activity_replies_list_item, null);

				// well set up the ViewHolder
				holder = new ViewHolder();

				// Mapping of all the views
				holder.ivProfilePicture = (ImageView) convertView.findViewById(R.id.ivProfilePicture);
				holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
				holder.tvReply = (TextView) convertView.findViewById(R.id.tvReply);
				holder.tvReplyDateTime = (TextView) convertView.findViewById(R.id.tvReplyDateTime);
				holder.ivDeleteReply = (ImageView) convertView.findViewById(R.id.ivDeleteReply);

				// store the holder with the view.
				convertView.setTag(holder);
			} else {
				// we've just avoided calling findViewById() on resource every
				// time
				// just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}

			/* Like icon click event */
			holder.ivDeleteReply.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(isRedirectedFrom.equals(ConfigConstants.Titles.TITLE_BEER_COMMENT_REPLIES)) {
						((BeerCommentRepliesActivity) activity).showAlertDeleteReply(position);
					}
					else if(isRedirectedFrom.equals(ConfigConstants.Titles.TITLE_COCKTAIL_COMMENT_REPLIES)) {
						((CocktailCommentRepliesActivity) activity).showAlertDeleteReply(position);
					}
					else if(isRedirectedFrom.equals(ConfigConstants.Titles.TITLE_LIQUOR_COMMENT_REPLIES)) {
						((LiquorCommentRepliesActivity) activity).showAlertDeleteReply(position);
					}
				}
			});

			/* Now, set the data here */
			((BaseActivity) context).setLogo(ConfigConstants.ImageUrls.user_140,
					commentRepliesList.get(position).getProfile_image(), holder.ivProfilePicture, R.drawable.no_profile_picture);
			((BaseActivity) context).showHideView(holder.tvName, Utils.getInstance().setCapitalLetter(
					commentRepliesList.get(position).getFirst_name() + " " + commentRepliesList.get(position).getLast_name()));
			((BaseActivity) context).showHideView(holder.tvReply,
					Utils.getInstance().setHTMLText(commentRepliesList.get(position).getComment()).toString());
			((BaseActivity) context).showHideView(holder.tvReplyDateTime, commentRepliesList.get(position).getDate_added());

			/* For the Delete functionality */
			if (commentRepliesList.get(position).getUser_id()
					.equals(SessionManager.getInstance(context).getData(SessionManager.KEY_USER_ID))) {
				holder.ivDeleteReply.setVisibility(View.VISIBLE);
			} else {
				holder.ivDeleteReply.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
}
