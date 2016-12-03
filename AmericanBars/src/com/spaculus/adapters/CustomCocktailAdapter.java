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
import com.spaculus.beans.Cocktail;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.Utils;

public class CustomCocktailAdapter extends ArrayAdapter<Cocktail> {

	private Context context;
	private List<Cocktail> cocktailList = null;

	public CustomCocktailAdapter(Context context, int resourceId, List<Cocktail> items) {
		super(context, resourceId, items);
		this.context = context;
		this.cocktailList = items;
	}

	//  Private View Holder Class
	private class ViewHolder {
		//  Mapping of all the views
		TextView tvCocktailName;
		TextView tvIngredients;
		TextView tvCocktailType;
		TextView tvServed;
		TextView tvDifficulty;
		ImageView ivCocktailLogo;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cocktailList.size();
	}

	@SuppressLint("InflateParams") 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		try {
			ViewHolder holder = null;
			
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.activity_cocktail_search_list_item, null);

				// well set up the ViewHolder
				holder = new ViewHolder();
				
				// Mapping of all the views
				holder.tvCocktailName = (TextView) convertView.findViewById(R.id.tvCocktailName);
				holder.tvIngredients = (TextView) convertView.findViewById(R.id.tvIngredients);
				holder.tvCocktailType = (TextView) convertView.findViewById(R.id.tvCocktailType);
				holder.tvServed = (TextView) convertView.findViewById(R.id.tvServed);
				holder.tvDifficulty = (TextView) convertView.findViewById(R.id.tvDifficulty);
				holder.ivCocktailLogo = (ImageView) convertView.findViewById(R.id.ivCocktailLogo);

				// store the holder with the view.
				convertView.setTag(holder);
			} 
			else {
				// we've just avoided calling findViewById() on resource every time
				// just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}

			//  Now, set the data here.
			holder.tvCocktailName.setText(cocktailList.get(position).getName());
			holder.tvIngredients.setText(Utils.getInstance().setHTMLText(cocktailList.get(position).getIngredients()));
			//holder.tvIngredients.setText(Utils.getInstance().setHTMLText(cocktailList.get(position).getIngredients()));
			holder.tvCocktailType.setText(cocktailList.get(position).getType());
			holder.tvServed.setText(cocktailList.get(position).getServed());
			holder.tvDifficulty.setText(cocktailList.get(position).getDifficulty());
			
			//  Now, set the cocktail logo here
			((BaseActivity)context).setLogo(ConfigConstants.ImageUrls.cocktail_200, cocktailList.get(position).getCocktail_image(), holder.ivCocktailLogo, R.drawable.no_image_cocktail);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertView;
	}
}


