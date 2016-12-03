package com.spaculus.adapters;

import java.util.List;

import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.R;
import com.spaculus.beans.Article;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomArticlesAdapter extends ArrayAdapter<Article> {

	private Context context;
	private List<Article> articlesList = null;

	public CustomArticlesAdapter(Context c, int resourceId, List<Article> items) {
		super(c, resourceId, items);
		this.context = c;
		this.articlesList = items;
	}

	//  Private View Holder Class
	private class ViewHolder {
		//  Mapping of all the views
		ImageView ivArticleImage;
		TextView tvArticleTitle;
		TextView tvArticleDescription;
	}

	@Override
	public int getCount() {
		return articlesList.size();
	}

	@SuppressLint("InflateParams") 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		try {
			ViewHolder holder = null;
			
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.fragment_articles_list_item, null);

				// well set up the ViewHolder
				holder = new ViewHolder();
				
				// Mapping of all the views
				holder.ivArticleImage = (ImageView) convertView.findViewById(R.id.ivArticleImage);
				holder.tvArticleTitle = (TextView) convertView.findViewById(R.id.tvArticleTitle);
				holder.tvArticleDescription = (TextView) convertView.findViewById(R.id.tvArticleDescription);
				
				// store the holder with the view.
				convertView.setTag(holder);
			} 
			else {
				// we've just avoided calling findViewById() on resource every time
				// just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}

			/* Now, set the data here */
			holder.tvArticleTitle.setText(Utils.getInstance().setCapitalLetter(articlesList.get(position).getBlog_title()));
			holder.tvArticleDescription.setText(Utils.getInstance().setHTMLText(articlesList.get(position).getBlog_description()));
			
			((BaseActivity)context).setLogo(ConfigConstants.ImageUrls.blog300by300, articlesList.get(position).getBlog_image(), holder.ivArticleImage, R.drawable.no_article);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
}


