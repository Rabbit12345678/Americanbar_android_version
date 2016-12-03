package com.spaculus.adapters;

import java.util.List;

import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.BeerCommentsListActivity;
import com.spaculus.americanbars.CocktailCommentsListActivity;
import com.spaculus.americanbars.LiquorCommentsListActivity;
import com.spaculus.americanbars.R;
import com.spaculus.beans.Review;
import com.spaculus.helpers.ConfigConstants;
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
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomCommentsAdapter extends ArrayAdapter<Review> {

	private Context context;
	private List<Review> commentsList = null;
	private Activity activity = null;
	private String isRedirectedFrom = "";

	public CustomCommentsAdapter(Context context, int resourceId, List<Review> items, Activity a, String flag) {
		super(context, resourceId, items);
		this.context = context;
		this.commentsList = items;
		this.activity = a;
		this.isRedirectedFrom = flag;
	}

	// Private View Holder Class
	private class ViewHolder {
		// Mapping of all the views
		ImageView ivProfilePicture;
		TextView tvName;
		TextView tvCommentTitle;
		TextView tvCommentDescription;
		TextView tvCommentDateTime;
		ImageView ivLikeComment;
		TextView tvLikeCommentCounter;
		TextView tvLikeText;
		LinearLayout linearLayoutLikeComment;
		TextView tvReply;
	}

	@Override
	public int getCount() {
		return commentsList.size();
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		try {
			ViewHolder holder = null;

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.activity_comments_list_item, null);

				// well set up the ViewHolder
				holder = new ViewHolder();

				// Mapping of all the views
				holder.ivProfilePicture = (ImageView) convertView.findViewById(R.id.ivProfilePicture);
				holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
				holder.tvCommentTitle = (TextView) convertView.findViewById(R.id.tvCommentTitle);
				holder.tvCommentDescription = (TextView) convertView.findViewById(R.id.tvCommentDescription);
				holder.tvCommentDateTime = (TextView) convertView.findViewById(R.id.tvCommentDateTime);
				holder.ivLikeComment = (ImageView) convertView.findViewById(R.id.ivLikeComment);
				holder.tvLikeCommentCounter = (TextView) convertView.findViewById(R.id.tvLikeCommentCounter);
				holder.tvLikeText = (TextView) convertView.findViewById(R.id.tvLikeText);
				holder.linearLayoutLikeComment = (LinearLayout) convertView.findViewById(R.id.linearLayoutLikeComment);
				holder.tvReply = (TextView) convertView.findViewById(R.id.tvReply);

				// store the holder with the view.
				convertView.setTag(holder);
			} else {
				// we've just avoided calling findViewById() on resource every
				// time
				// just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}
			
			/* Like icon click event */
			holder.linearLayoutLikeComment.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(isRedirectedFrom.equals(ConfigConstants.Titles.TITLE_BEER_COMMENTS)) {
						((BeerCommentsListActivity) activity).likeBeerComment(position);
					}
					else if(isRedirectedFrom.equals(ConfigConstants.Titles.TITLE_COCKTAIL_COMMENTS)) {
						((CocktailCommentsListActivity) activity).likeCocktailComment(position);
					}
					else if(isRedirectedFrom.equals(ConfigConstants.Titles.TITLE_LIQUOR_COMMENTS)) {
						((LiquorCommentsListActivity) activity).likeLiquorComment(position);
					}
				}
			});
			
			/* Reply click event */
			holder.tvReply.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(isRedirectedFrom.equals(ConfigConstants.Titles.TITLE_BEER_COMMENTS)) {
						((BaseActivity) activity).redirectBeerCommentRepliesActivity(activity, commentsList.get(position).getParent_id(), commentsList.get(position).getComment_id());
					}
					else if(isRedirectedFrom.equals(ConfigConstants.Titles.TITLE_COCKTAIL_COMMENTS)) {
						((BaseActivity) activity).redirectCocktailCommentRepliesActivity(activity, commentsList.get(position).getParent_id(), commentsList.get(position).getComment_id());
					}
					else if(isRedirectedFrom.equals(ConfigConstants.Titles.TITLE_LIQUOR_COMMENTS)) {
						((BaseActivity) activity).redirectLiquorCommentRepliesActivity(activity, commentsList.get(position).getParent_id(), commentsList.get(position).getComment_id());
					}
				}
			});

			/* Now, set the data here */
			((BaseActivity) context).setLogo(ConfigConstants.ImageUrls.user_140,
					commentsList.get(position).getProfile_image(), holder.ivProfilePicture,
					R.drawable.no_profile_picture);
			((BaseActivity) context).showHideView(holder.tvName,
					Utils.getInstance().setCapitalLetter(commentsList.get(position).getFirst_name() + " "
							+ commentsList.get(position).getLast_name()));
			((BaseActivity) context).showHideView(holder.tvCommentTitle,
					commentsList.get(position).getComment_title());
			((BaseActivity) context).showHideView(holder.tvCommentDescription,
					Utils.getInstance().setHTMLText(commentsList.get(position).getComment()).toString());
			((BaseActivity) context).showHideView(holder.tvCommentDateTime,
					commentsList.get(position).getDate_added());

			/* For the Like functionality */
			/* For the thumb image */
			if(commentsList.get(position).getIs_like().equals(ConfigConstants.Constants.CONSTANT_ONE)) {
				holder.ivLikeComment.setImageResource(R.drawable.thumb_down);
			}
			else {
				holder.ivLikeComment.setImageResource(R.drawable.thumb_up);
			}
			/* For the like counter */
			if(commentsList.get(position).getTotal_like().isEmpty()) {
				holder.tvLikeCommentCounter.setText(ConfigConstants.Constants.CONSTANT_ZERO);	
			}
			else {
				holder.tvLikeCommentCounter.setText(commentsList.get(position).getTotal_like());
			}
			/* For the like text */
			if(commentsList.get(position).getTotal_like().isEmpty()) {
				holder.tvLikeText.setText(ConfigConstants.Constants.CONSTANT_LIKE);
			}
			else {
				if(commentsList.get(position).getTotal_like().equals(ConfigConstants.Constants.CONSTANT_ZERO) || commentsList.get(position).getTotal_like().equals(ConfigConstants.Constants.CONSTANT_ONE)) {
					holder.tvLikeText.setText(ConfigConstants.Constants.CONSTANT_LIKE);
				}
				else {
					holder.tvLikeText.setText(ConfigConstants.Constants.CONSTANT_LIKES);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
}
