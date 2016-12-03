package com.spaculus.americanbars;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;

import com.spaculus.beans.Media;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.mediapicker.MediaPickerActivity;

public class BaseActivityMedia extends BaseActivity {
	
	//  For the Media Functionality
	private ArrayList<Media> mediaList = new ArrayList<Media>();
	
	//  To know from which screen user is redirected to this screen
	private String flag = "";
	
	/* For the Add Album Activity as when need to change the particular position's image */
	private int selectedPosition = 0;
	
	//  Constants to identify
	public String CONSTANT_MY_PROFILE_ACTIVITY_SINGLE_SELECTION = "MyProfileActivity";
	public String CONSTANT_ADD_ALBUM_ACTIVITY_SINGLE_SELECTION = "AddAlbumActivity";
	public String CONSTANT_SUGGEST_NEW_BEER_ACTIVITY_SINGLE_SELECTION = "SuggestNewBeerActivity";
	public String CONSTANT_SUGGEST_NEW_COCKTAIL_ACTIVITY_SINGLE_SELECTION = "SuggestNewCocktailActivity";
	public String CONSTANT_SUGGEST_NEW_LIQUOR_ACTIVITY_SINGLE_SELECTION = "SuggestNewLiquorActivity";
	public String CONSTANT_ADD_ALBUM_ACTIVITY_MULTIPLE_SELECTION = "AddAlbumActivity_Multiple";
	
	//  All the Parent Views
	private MyProfileActivity objMyProfileActivity = null;
	private AddAlbumActivity objAddAlbumActivity = null;
	private SuggestNewBeerActivity objSuggestNewBeerActivity = null;
	private SuggestNewCocktailActivity objSuggestNewCocktailActivity = null;
	private SuggestNewLiquorActivity objSuggestNewLiquorActivity = null;
	
	/* A method is used to know from which parent this method is called. */
	/* Single Selection */
	public void selectMediaSingle(String selectedMediaType, int maximumMediaAllowed, String redirectFlag, MyProfileActivity myProfileActivity){
		try {
			this.flag = redirectFlag;
			this.objMyProfileActivity = myProfileActivity;
			redirectMediaActivity(selectedMediaType, maximumMediaAllowed);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selectMediaSingle(String selectedMediaType, int maximumMediaAllowed, String redirectFlag, AddAlbumActivity addAlbumActivity, int position){
		try {
			this.selectedPosition = position;
			this.flag = redirectFlag;
			this.objAddAlbumActivity = addAlbumActivity;
			redirectMediaActivity(selectedMediaType, maximumMediaAllowed);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selectMediaSingle(String selectedMediaType, int maximumMediaAllowed, String redirectFlag, SuggestNewBeerActivity suggestNewBeerActivity){
		try {
			this.flag = redirectFlag;
			this.objSuggestNewBeerActivity = suggestNewBeerActivity;
			redirectMediaActivity(selectedMediaType, maximumMediaAllowed);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selectMediaSingle(String selectedMediaType, int maximumMediaAllowed, String redirectFlag, SuggestNewCocktailActivity suggestNewCocktailActivity){
		try {
			this.flag = redirectFlag;
			this.objSuggestNewCocktailActivity = suggestNewCocktailActivity;
			redirectMediaActivity(selectedMediaType, maximumMediaAllowed);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void selectMediaSingle(String selectedMediaType, int maximumMediaAllowed, String redirectFlag, SuggestNewLiquorActivity suggestNewLiquorActivity){
		try {
			this.flag = redirectFlag;
			this.objSuggestNewLiquorActivity = suggestNewLiquorActivity;
			redirectMediaActivity(selectedMediaType, maximumMediaAllowed);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* Single Selection */
	
	
	/* Multiple Selection */
	public void selectMediaMultiple(String selectedMediaType, int maximumMediaAllowed, String redirectFlag, AddAlbumActivity addAlbumActivity){
		try {
			this.flag = redirectFlag;
			this.objAddAlbumActivity = addAlbumActivity;
			redirectMediaActivity(selectedMediaType, maximumMediaAllowed);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/* Multiple Selection */
	
	/* To select a picture/video from gallery */
	private void redirectMediaActivity(String selectedMediaType, int maximumMediaAllowed) {
		try {
			int mediaType = -1;
			if(selectedMediaType.equals(ConfigConstants.Constants.CONSTANT_MEDIA_PICTURE)){
				mediaType = 1;
			}
			else{
				mediaType = 0;
			}
			Intent intent = new Intent(BaseActivityMedia.this, MediaPickerActivity.class);
			intent.putExtra("type", mediaType);
			intent.putExtra("max", maximumMediaAllowed);
			startActivityForResult(intent, ConfigConstants.ResultCodes.PICK_MEDIA_RESULT_CODE);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if(data != null){
				if(requestCode == resultCode){
					
					//  If media(image/video) is selected
					if(requestCode == ConfigConstants.ResultCodes.PICK_MEDIA_RESULT_CODE) {
						
						/*//  Now, first of all check whether for the Single Media Selection or not
						if(isSingleMediaSelection()) {
							//  Now, here clear the media list
							if(mediaList.size()>0) {
								mediaList.clear();
							}
						}*/
						
						//  Now, here clear the media list
						if(mediaList.size()>0) {
							mediaList.clear();
						}
						
						//Log.i("Media is selected", "Media is selected");
						Bundle bundle = data.getExtras();
						
						ArrayList<Media> selectedMediaList = bundle.getParcelableArrayList("selectedMediaList");
						for(int i=0;i<selectedMediaList.size();i++){
							//Log.i("Path-"+i, selectedMediaList.get(i).getMediaPath());
						}
						
						//  Now, add all the data into the main list
						mediaList.addAll(selectedMediaList);
						
						//  Now, check to which screen now user need to redirect.
						redirectTo();
					}
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * To check whether this activity is called for the Single or Multiple Media
	 * Selection. As it is for single media then we have to clear the media list o.w. not.
	 */
	/*private boolean isSingleMediaSelection() {
		boolean isSingleMediaSelection = false;
		try {
			if(flag.equals(CONSTANT_MY_PROFILE_ACTIVITY_SINGLE_SELECTION) || flag.equals(CONSTANT_ADD_ALBUM_ACTIVITY_SINGLE_SELECTION)) {
				isSingleMediaSelection = true;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return isSingleMediaSelection;
	}*/
	
	/* A method is used to called the respective screens method. */
	private void redirectTo() {
		try {
				if(flag.equals(CONSTANT_MY_PROFILE_ACTIVITY_SINGLE_SELECTION)) {
						objMyProfileActivity.setProfilePicture(mediaList.get(0).getMediaPath());
				}
				else if(flag.equals(CONSTANT_ADD_ALBUM_ACTIVITY_SINGLE_SELECTION)) {
					objAddAlbumActivity.editMediaObject(mediaList, selectedPosition);
				}
				else if(flag.equals(CONSTANT_ADD_ALBUM_ACTIVITY_MULTIPLE_SELECTION)) {
					objAddAlbumActivity.addLocalObject(mediaList);
				}
				else if(flag.equals(CONSTANT_SUGGEST_NEW_BEER_ACTIVITY_SINGLE_SELECTION)) {
					objSuggestNewBeerActivity.setProfilePicture(mediaList.get(0).getMediaPath());
				}
				else if(flag.equals(CONSTANT_SUGGEST_NEW_COCKTAIL_ACTIVITY_SINGLE_SELECTION)) {
					objSuggestNewCocktailActivity.setProfilePicture(mediaList.get(0).getMediaPath());
				}
				else if(flag.equals(CONSTANT_SUGGEST_NEW_LIQUOR_ACTIVITY_SINGLE_SELECTION)) {
					objSuggestNewLiquorActivity.setProfilePicture(mediaList.get(0).getMediaPath());
				}
			} 
		catch (Exception e) {
				e.printStackTrace();
			}
	}
}
