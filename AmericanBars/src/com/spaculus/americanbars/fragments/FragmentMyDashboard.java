package com.spaculus.americanbars.fragments;

import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FragmentMyDashboard extends Fragment implements OnClickListener {
	
	private View rootView = null;
	
	private LinearLayout linearLayoutMyProfile;
	private LinearLayout linearLayoutMyAlbums;
	private LinearLayout linearLayoutMyFavoriteBars;
	private LinearLayout linearLayoutMyFavoriteBeers;
	private LinearLayout linearLayoutMyFavoriteCocktails;
	private LinearLayout linearLayoutMyFavoriteLiquors;
	private LinearLayout linearLayoutPrivacySettings;
	private LinearLayout linearLayoutChangePassword;
	private LinearLayout linearLayoutLogOut;
	private LinearLayout linearLayoutTriviaGame;
	private LinearLayout linearLayoutArticles;
	private LinearLayout linearLayoutHome;
	
	//  To know whether user is redirected to this screen from navigation drawer or not
	//  So on the basis of it we can show the action bar fields
	boolean isRedirectedFromMainActivity = false;
	
	public FragmentMyDashboard(boolean flag){
		this.isRedirectedFromMainActivity = flag;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
			rootView = inflater.inflate(R.layout.fragment_my_dashboard, container, false);
    
			//  For the Action Bar
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			if(isRedirectedFromMainActivity) {
				((BaseActivity)getActivity()).setActionBarFromChild(false, true, false, true, true);	
			}
			else {
				((BaseActivity)getActivity()).setActionBarFromChild(true, true, false, true, true);
			}
					
			// Mapping of all the views
			mappedAllViews();
			
			//  Click events of all the views
			clickEventsAllViews();
		} 
        catch (Exception e) {
			e.printStackTrace();
		}
        return rootView;
    }

	//  This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			linearLayoutMyProfile = (LinearLayout)rootView.findViewById(R.id.linearLayoutMyProfile);
			linearLayoutMyAlbums = (LinearLayout)rootView.findViewById(R.id.linearLayoutMyAlbums);
			linearLayoutMyFavoriteBars = (LinearLayout)rootView.findViewById(R.id.linearLayoutMyFavoriteBars);
			linearLayoutMyFavoriteBeers = (LinearLayout)rootView.findViewById(R.id.linearLayoutMyFavoriteBeers);
			linearLayoutMyFavoriteCocktails = (LinearLayout)rootView.findViewById(R.id.linearLayoutMyFavoriteCocktails);
			linearLayoutMyFavoriteLiquors = (LinearLayout)rootView.findViewById(R.id.linearLayoutMyFavoriteLiquors);
			linearLayoutPrivacySettings = (LinearLayout)rootView.findViewById(R.id.linearLayoutPrivacySettings);
			linearLayoutChangePassword = (LinearLayout)rootView.findViewById(R.id.linearLayoutChangePassword);
			linearLayoutLogOut = (LinearLayout)rootView.findViewById(R.id.linearLayoutLogOut);
			linearLayoutTriviaGame = (LinearLayout)rootView.findViewById(R.id.linearLayoutTriviaGame);
			linearLayoutArticles = (LinearLayout)rootView.findViewById(R.id.linearLayoutArticles);
			linearLayoutHome = (LinearLayout)rootView.findViewById(R.id.linearLayoutHome);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  Click events of all the views
	private void clickEventsAllViews() {
		try {
			//  Click events of all the layouts
			linearLayoutMyProfile.setOnClickListener(this);
			linearLayoutMyAlbums.setOnClickListener(this);
			linearLayoutMyFavoriteBars.setOnClickListener(this);
			linearLayoutMyFavoriteBeers.setOnClickListener(this);
			linearLayoutMyFavoriteCocktails.setOnClickListener(this);
			linearLayoutMyFavoriteLiquors.setOnClickListener(this);
			linearLayoutPrivacySettings.setOnClickListener(this);
			linearLayoutChangePassword.setOnClickListener(this);
			linearLayoutLogOut.setOnClickListener(this);
			linearLayoutTriviaGame.setOnClickListener(this);
			linearLayoutArticles.setOnClickListener(this);
			linearLayoutHome.setOnClickListener(this);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View view) {
		try {
			switch (view.getId()) {
			case R.id.linearLayoutMyProfile:
				((BaseActivity) getActivity()).redirectMyProfileActivity(getActivity());
				break;
			case R.id.linearLayoutMyAlbums:
				((BaseActivity) getActivity()).redirectFragmentMyAlbums(getActivity());
				break;
			case R.id.linearLayoutMyFavoriteBars:
				((BaseActivity) getActivity()).redirectFragmentMyFavoriteBars(getActivity());
				break;
			case R.id.linearLayoutMyFavoriteBeers:
				((BaseActivity) getActivity()).redirectFragmentMyFavoriteBeers(getActivity());
				break;
			case R.id.linearLayoutMyFavoriteCocktails:
				((BaseActivity) getActivity()).redirectFragmentMyFavoriteCocktails(getActivity());
				break;
			case R.id.linearLayoutMyFavoriteLiquors:
				((BaseActivity) getActivity()).redirectFragmentMyFavoriteLiquors(getActivity());
				break;
			case R.id.linearLayoutPrivacySettings:
				((BaseActivity) getActivity()).redirectFragmentPrivacySettings(getActivity());
				break;
			case R.id.linearLayoutChangePassword:
				((BaseActivity) getActivity()).redirectFragmentChangePassword(getActivity());
				break;
			case R.id.linearLayoutLogOut:
				((BaseActivity) getActivity()).doLogOut();
				break;
			case R.id.linearLayoutTriviaGame:
				((BaseActivity) getActivity()).redirectFragmentBarTriviaGame(getActivity());
				break;
			case R.id.linearLayoutArticles:
				((BaseActivity) getActivity()).redirectFragmentArticles(getActivity());
				break;
			case R.id.linearLayoutHome:
				((BaseActivity) getActivity()).redirectHomeActivity(getActivity());
				break;
			default:
				break;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
