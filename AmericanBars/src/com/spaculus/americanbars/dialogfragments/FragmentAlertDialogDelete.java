package com.spaculus.americanbars.dialogfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.spaculus.americanbars.AddAlbumActivity;
import com.spaculus.americanbars.R;
import com.spaculus.americanbars.BeerCommentRepliesActivity;
import com.spaculus.americanbars.CocktailCommentRepliesActivity;
import com.spaculus.americanbars.LiquorCommentRepliesActivity;
import com.spaculus.americanbars.TransactionActivity;
import com.spaculus.americanbars.fragments.FragmentMyAlbums;
import com.spaculus.americanbars.fragments.FragmentMyFavouriteBars;
import com.spaculus.americanbars.fragments.FragmentMyFavouriteBeers;
import com.spaculus.americanbars.fragments.FragmentMyFavouriteCocktails;
import com.spaculus.americanbars.fragments.FragmentMyFavouriteLiquors;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.Utils;

public class FragmentAlertDialogDelete extends DialogFragment {

	private String subject = "";
	private String message = "";
	private String positiveButtonText = "";
	private String negativeButtonText = "";
	private String flag = "";
	private String id = "";
	private boolean isImageFromWeb = false;
	private int index =  -1;
	
	//  All the Parent Views
	private FragmentMyFavouriteBars objFragmentMyFavouriteBars = null;
	private FragmentMyFavouriteBeers objFragmentMyFavouriteBeers = null;
	private FragmentMyFavouriteCocktails objFragmentMyFavouriteCocktails = null;
	private FragmentMyFavouriteLiquors objFragmentMyFavouriteLiquors = null;
	private FragmentMyAlbums objFragmentMyAlbums = null;
	private AddAlbumActivity objAddAlbumActivity = null;
	private BeerCommentRepliesActivity objBeerCommentRepliesActivity = null;
	private CocktailCommentRepliesActivity objCocktailCommentRepliesActivity = null;
	private LiquorCommentRepliesActivity objLiquorCommentRepliesActivity = null;
	
	public FragmentAlertDialogDelete(String alertSubject, String alertMessage, String posButtonText, String negButtonText, String flag1, String selectedID, FragmentMyFavouriteBars fragmentMyFavouriteBars) {
		// TODO Auto-generated constructor stub
		this.subject = alertSubject;
		this.message = alertMessage;
		this.positiveButtonText = posButtonText;
		this.negativeButtonText = negButtonText;
		this.flag = flag1;
		this.id = selectedID;
		this.objFragmentMyFavouriteBars = fragmentMyFavouriteBars;
	}

	public FragmentAlertDialogDelete(String alertSubject, String alertMessage, String posButtonText, String negButtonText, String flag1, String selectedID, FragmentMyFavouriteBeers fragmentMyFavouriteBeers) {
		// TODO Auto-generated constructor stub
		this.subject = alertSubject;
		this.message = alertMessage;
		this.positiveButtonText = posButtonText;
		this.negativeButtonText = negButtonText;
		this.flag = flag1;
		this.id = selectedID;
		this.objFragmentMyFavouriteBeers = fragmentMyFavouriteBeers;
	}
	
	public FragmentAlertDialogDelete(String alertSubject, String alertMessage, String posButtonText, String negButtonText, String flag1, String selectedID, FragmentMyFavouriteCocktails fragmentMyFavouriteCocktails) {
		// TODO Auto-generated constructor stub
		this.subject = alertSubject;
		this.message = alertMessage;
		this.positiveButtonText = posButtonText;
		this.negativeButtonText = negButtonText;
		this.flag = flag1;
		this.id = selectedID;
		this.objFragmentMyFavouriteCocktails = fragmentMyFavouriteCocktails;
	}
	
	public FragmentAlertDialogDelete(String alertSubject, String alertMessage, String posButtonText, String negButtonText, String flag1, String selectedID, FragmentMyFavouriteLiquors fragmentMyFavouriteLiquors) {
		// TODO Auto-generated constructor stub
		this.subject = alertSubject;
		this.message = alertMessage;
		this.positiveButtonText = posButtonText;
		this.negativeButtonText = negButtonText;
		this.flag = flag1;
		this.id = selectedID;
		this.objFragmentMyFavouriteLiquors = fragmentMyFavouriteLiquors;
	}
	
	public FragmentAlertDialogDelete(String alertSubject, String alertMessage, String posButtonText, String negButtonText, String flag1, String selectedID, FragmentMyAlbums fragmentMyAlbums) {
		// TODO Auto-generated constructor stub
		this.subject = alertSubject;
		this.message = alertMessage;
		this.positiveButtonText = posButtonText;
		this.negativeButtonText = negButtonText;
		this.flag = flag1;
		this.id = selectedID;
		this.objFragmentMyAlbums = fragmentMyAlbums;
	}
	
	public FragmentAlertDialogDelete(String alertSubject, String alertMessage, String posButtonText, String negButtonText, String flag1, String selectedID, AddAlbumActivity addAlbumActivity, boolean isImageFromWeb, int index) {
		// TODO Auto-generated constructor stub
		this.subject = alertSubject;
		this.message = alertMessage;
		this.positiveButtonText = posButtonText;
		this.negativeButtonText = negButtonText;
		this.flag = flag1;
		this.id = selectedID;
		this.objAddAlbumActivity = addAlbumActivity;
		this.isImageFromWeb = isImageFromWeb;
		this.index = index;
	}
	
	public FragmentAlertDialogDelete(String alertSubject, String alertMessage, String posButtonText, String negButtonText, String flag1, BeerCommentRepliesActivity beerCommentRepliesActivity, int index) {
		// TODO Auto-generated constructor stub
		this.subject = alertSubject;
		this.message = alertMessage;
		this.positiveButtonText = posButtonText;
		this.negativeButtonText = negButtonText;
		this.flag = flag1;
		this.objBeerCommentRepliesActivity = beerCommentRepliesActivity;
		this.index = index;
	}
	
	public FragmentAlertDialogDelete(String alertSubject, String alertMessage, String posButtonText, String negButtonText, String flag1, CocktailCommentRepliesActivity cocktailCommentRepliesActivity, int index) {
		// TODO Auto-generated constructor stub
		this.subject = alertSubject;
		this.message = alertMessage;
		this.positiveButtonText = posButtonText;
		this.negativeButtonText = negButtonText;
		this.flag = flag1;
		this.objCocktailCommentRepliesActivity = cocktailCommentRepliesActivity;
		this.index = index;
	}
	
	public FragmentAlertDialogDelete(String alertSubject, String alertMessage, String posButtonText, String negButtonText, String flag1, LiquorCommentRepliesActivity liquorCommentRepliesActivity, int index) {
		// TODO Auto-generated constructor stub
		this.subject = alertSubject;
		this.message = alertMessage;
		this.positiveButtonText = posButtonText;
		this.negativeButtonText = negButtonText;
		this.flag = flag1;
		this.objLiquorCommentRepliesActivity = liquorCommentRepliesActivity;
		this.index = index;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	
		return new AlertDialog.Builder(getActivity())
				.setIcon(R.drawable.alert)
				.setTitle(subject)
				.setMessage(message)
				.setPositiveButton(positiveButtonText,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								try {
									//  My Favorite Bars
									if(flag.equals(ConfigConstants.Constants.MY_FAV_BARS)) {
										//  Now, called as AsyncTasl to delete a single/multiple favorite bars
										if(Utils.getInstance() .isNetworkAvailable(getActivity())) {
											objFragmentMyFavouriteBars.new AsyncTaskDeleteBar().execute(id);
										}
										else{
											Utils.getInstance().showToastNoInternetAvailable(getActivity());
										}
									}
									//  My Favorite Beers
									else if(flag.equals(ConfigConstants.Constants.MY_FAV_BEERS)) {
											//  Now, called as AsyncTasl to delete a single/multiple favorite beers
											if(Utils.getInstance() .isNetworkAvailable(getActivity())) {
												objFragmentMyFavouriteBeers.new AsyncTaskDeleteBeer().execute(id);
											}
											else{
												Utils.getInstance().showToastNoInternetAvailable(getActivity());
											}
									}
									//  My Favorite Cocktails
									else if(flag.equals(ConfigConstants.Constants.MY_FAV_COCKTAILS)) {
											//  Now, called as AsyncTasl to delete a single/multiple favorite cocktails
											if(Utils.getInstance() .isNetworkAvailable(getActivity())) {
												objFragmentMyFavouriteCocktails.new AsyncTaskDeleteCocktail().execute(id);
											}
											else{
												Utils.getInstance().showToastNoInternetAvailable(getActivity());
											}
									}
									//  My Favorite Liquors
									else if(flag.equals(ConfigConstants.Constants.MY_FAV_LIQUORS)) {
											//  Now, called as AsyncTasl to delete a single/multiple favorite liquors
											if(Utils.getInstance() .isNetworkAvailable(getActivity())) {
												objFragmentMyFavouriteLiquors.new AsyncTaskDeleteLiquor().execute(id);
											}
											else{
												Utils.getInstance().showToastNoInternetAvailable(getActivity());
										    }
									}
									//  My Albums
									else if(flag.equals(ConfigConstants.Constants.MY_ALBUMS)) {
											//  Now, called as AsyncTasl to delete a single/multiple favorite liquors
											if(Utils.getInstance() .isNetworkAvailable(getActivity())) {
												objFragmentMyAlbums.new AsyncTaskDeleteAlbum().execute(id);
											}
											else{
												Utils.getInstance().showToastNoInternetAvailable(getActivity());
										    }
									}
									//  AddAlbumActivity
									else if(flag.equals(ConfigConstants.Constants.ALBUM_IMAGE)) {
										/*
										 * Now, called as AsyncTask to delete an album image.
										 * First of all check whether local or web object.
										 */
										if(isImageFromWeb) {
											//  Web Object
											if(Utils.getInstance() .isNetworkAvailable(getActivity())) {
												//  Web Object
												objAddAlbumActivity.new AsyncTaskDeleteAlbumImage().execute(id, String.valueOf(index));
											}
											else{
												Utils.getInstance().showToastNoInternetAvailable(getActivity());
										    }
										}
										else {
											//  Local Object
											objAddAlbumActivity.new AsyncTaskDeleteAlbumImageLocal().execute(String.valueOf(index));
										}
									}
									//  Beer Comment Replies
									else if(flag.equals(ConfigConstants.Titles.TITLE_BEER_COMMENT_REPLIES)) {
										/* Now, called a method for the delete reply */
										if(Utils.getInstance() .isNetworkAvailable(getActivity())) {
											objBeerCommentRepliesActivity.deleteReply(index);
										}
										else{
											Utils.getInstance().showToastNoInternetAvailable(getActivity());
									    }
									}
									//  Cocktail Comment Replies
									else if(flag.equals(ConfigConstants.Titles.TITLE_COCKTAIL_COMMENT_REPLIES)) {
										/* Now, called a method for the delete reply */
										if(Utils.getInstance() .isNetworkAvailable(getActivity())) {
											objCocktailCommentRepliesActivity.deleteReply(index);
										}
										else{
											Utils.getInstance().showToastNoInternetAvailable(getActivity());
									    }
									}
									//  Liquor Comment Replies
									else if(flag.equals(ConfigConstants.Titles.TITLE_LIQUOR_COMMENT_REPLIES)) {
										/* Now, called a method for the delete reply */
										if(Utils.getInstance() .isNetworkAvailable(getActivity())) {
											objLiquorCommentRepliesActivity.deleteReply(index);
										}
										else{
											Utils.getInstance().showToastNoInternetAvailable(getActivity());
									    }
									}
								} 
								catch (Exception e) {
									e.printStackTrace();
								}
							}
						})
				.setNegativeButton(negativeButtonText,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								try {
									//  Transaction Activity
									if(flag.equals(ConfigConstants.Constants.MY_FAV_BARS)){
										((TransactionActivity) getActivity()).doNegativeClick();
									}
								} 
								catch (Exception e) {
									e.printStackTrace();
								}
							}
						}).create();
	}
}
