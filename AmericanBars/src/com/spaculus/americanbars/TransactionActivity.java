package com.spaculus.americanbars;

import com.spaculus.americanbars.fragments.FragmentArticles;
import com.spaculus.americanbars.fragments.FragmentBarTriviaGame;
import com.spaculus.americanbars.fragments.FragmentChangePassword;
import com.spaculus.americanbars.fragments.FragmentMyAlbums;
import com.spaculus.americanbars.fragments.FragmentMyDashboard;
import com.spaculus.americanbars.fragments.FragmentMyFavouriteBars;
import com.spaculus.americanbars.fragments.FragmentMyFavouriteBeers;
import com.spaculus.americanbars.fragments.FragmentMyFavouriteCocktails;
import com.spaculus.americanbars.fragments.FragmentMyFavouriteLiquors;
import com.spaculus.americanbars.fragments.FragmentPrivacySettings;
import com.spaculus.americanbars.fragments.FragmentSignUp;
import com.spaculus.beans.Article;
import com.spaculus.helpers.ConfigConstants;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
//import android.util.Log;

public class TransactionActivity extends BaseActivity {
	
	//  To know from which screen user is redirected to this screen.
	private String title = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			//Log.i("onCreate", "onCreate");
			
			setContentView(R.layout.activity_transcation);
			
			//  Now, get the screen position data here
			Bundle b = getIntent().getExtras();
			title = b.getString(ConfigConstants.Keys.KEY_CONSTANT_REDIRECT_FROM);	
			
			//  Create ActionBar
			createActionBar(title, R.layout.custom_actionbar, TransactionActivity.this, true);
					
			//  Called this method to know which fragment need to be reload
			displayView(title);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.finish();
			this.onDestroy();
		}
	}
	
	private void displayView(String title) {
		try {
			// update the main content by replacing fragments
			Fragment fragment = null;
			//Log.i("onCreate", "onCreate");
			
			if (title.equals(ConfigConstants.Constants.SIGN_UP)) {
				fragment = new FragmentSignUp(false);
			} 
			else if (title.equals(ConfigConstants.Constants.SIGN_IN)) {
				//fragment = new FragmentSignIn(false);
			} 
			else if (title.equals(ConfigConstants.Constants.MY_FAV_BARS)) {
				fragment = new FragmentMyFavouriteBars(false);
			} 
			else if (title.equals(ConfigConstants.Constants.MY_FAV_BEERS)) {
				fragment = new FragmentMyFavouriteBeers(false);
			} 
			else if (title.equals(ConfigConstants.Constants.MY_FAV_COCKTAILS)) {
				fragment = new FragmentMyFavouriteCocktails(false);
			} 
			else if (title.equals(ConfigConstants.Constants.MY_FAV_LIQUORS)) {
				fragment = new FragmentMyFavouriteLiquors(false);
			} 
			else if (title.equals(ConfigConstants.Constants.MY_ALBUMS)) {
				fragment = new FragmentMyAlbums(false);
			} 
			else if (title.equals(ConfigConstants.Constants.PRIVACY_SETTINGS)) {
				fragment = new FragmentPrivacySettings(false);
			} 
			else if (title.equals(ConfigConstants.Constants.CHANGE_PASSWORD)) {
				fragment = new FragmentChangePassword(false);
			} 
			else if (title.equals(ConfigConstants.Constants.MY_DASHBOARD)) {
				fragment = new FragmentMyDashboard(false);
			} 
			else if (title.equals(ConfigConstants.Constants.ARTICLES)) {
				fragment = new FragmentArticles(false);
			} 
			else if (title.equals(ConfigConstants.Constants.BAR_TRIVIA_GAME)) {
				fragment = new FragmentBarTriviaGame(false);
			} 
			
			if (fragment != null) {
				final FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			} 
			else {
				// error in creating fragment
				//Log.e("TranscationActivity", "Error in creating fragment");
			}
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* For the Favorite Listing Screens */
	/* Click on Row */
	public void rowClickMethod(String id, String type, String flag) {
		try {
			if(flag.equals(ConfigConstants.Constants.MY_FAV_BARS)) {
				//  Now, redirect to the Full Mug Bar or Half Mug Bar Details Screen
				redirectFullHalfMugBarDetailsActivity(TransactionActivity.this, id, type);
			}
			else if(flag.equals(ConfigConstants.Constants.MY_FAV_BEERS)) {
				redirectBeerDetailsActivity(TransactionActivity.this, id);
			}
			else if(flag.equals(ConfigConstants.Constants.MY_FAV_COCKTAILS)) {
				redirectCocktailDetailsActivity(TransactionActivity.this, id);
			}
			else if(flag.equals(ConfigConstants.Constants.MY_FAV_LIQUORS)) {
				redirectLiquorDetailsActivity(TransactionActivity.this, id);
			}
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* For the Album Listing Screen - Click on the row */
	public void rowClickMethodArticle(Article objArticle) {
		redirectArticleDetailsActivity(TransactionActivity.this, objArticle);
	}
	
	/* For the Alert Dialog Negative Click */
	public void doNegativeClick() {
	    //  Nothing need to do here.
	    //Log.i("FragmentAlertDialog", "Negative click!");
	}
	
	@Override
	public void onBackPressed() {
		try {
			super.onBackPressed();
			this.finish();
			this.onDestroy();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
