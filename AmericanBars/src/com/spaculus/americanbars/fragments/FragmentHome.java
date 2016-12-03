package com.spaculus.americanbars.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.R;

public class FragmentHome extends Fragment implements OnClickListener {
	
	private View rootView = null;
	
	private LinearLayout linearLayoutBarSearch;
	private LinearLayout linearLayoutBeerDirectory;
	private LinearLayout linearLayoutCocktailDirectory;
	private LinearLayout linearLayoutLiquorDirectory;
	private LinearLayout linearLayoutTaxiDirectory;
	private LinearLayout linearLayoutPhotoGallery;
	private LinearLayout linearLayoutTriviaGame;
	private LinearLayout linearLayoutArticles;
	
	public FragmentHome(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
			rootView = inflater.inflate(R.layout.fragment_home, container, false);
    
			//  For the Action Bar
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			((BaseActivity)getActivity()).setActionBarFromChild(false, false, true, false, false);
			
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
			linearLayoutBarSearch = (LinearLayout)rootView.findViewById(R.id.linearLayoutBarSearch);
			linearLayoutBeerDirectory = (LinearLayout)rootView.findViewById(R.id.linearLayoutBeerDirectory);
			linearLayoutCocktailDirectory = (LinearLayout)rootView.findViewById(R.id.linearLayoutCocktailDirectory);
			linearLayoutLiquorDirectory = (LinearLayout)rootView.findViewById(R.id.linearLayoutLiquorDirectory);
			linearLayoutTaxiDirectory = (LinearLayout)rootView.findViewById(R.id.linearLayoutTaxiDirectory);
			linearLayoutPhotoGallery = (LinearLayout)rootView.findViewById(R.id.linearLayoutPhotoGallery);
			linearLayoutTriviaGame = (LinearLayout)rootView.findViewById(R.id.linearLayoutTriviaGame);
			linearLayoutArticles = (LinearLayout)rootView.findViewById(R.id.linearLayoutArticles);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  Click events of all the views
	private void clickEventsAllViews() {
		try {
			//  Click events of all the layouts
			linearLayoutBarSearch.setOnClickListener(this);
			linearLayoutBeerDirectory.setOnClickListener(this);
			linearLayoutCocktailDirectory.setOnClickListener(this);
			linearLayoutLiquorDirectory.setOnClickListener(this);
			linearLayoutTaxiDirectory.setOnClickListener(this);
			linearLayoutPhotoGallery.setOnClickListener(this);
			linearLayoutTriviaGame.setOnClickListener(this);
			linearLayoutArticles.setOnClickListener(this);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View view) {
		try {
			switch (view.getId()) {
			case R.id.linearLayoutBarSearch:
				((BaseActivity) getActivity()).redirectBarSearchActivity(getActivity());
				break;
			case R.id.linearLayoutBeerDirectory:
				((BaseActivity) getActivity()).redirectBeerSearchListActivity(getActivity(), "");
				break;
			case R.id.linearLayoutCocktailDirectory:
				((BaseActivity) getActivity()).redirectCocktailSearchListActivity(getActivity(), "");
				break;
			case R.id.linearLayoutLiquorDirectory:
				((BaseActivity) getActivity()).redirectLiquorSearchListActivity(getActivity(), "");
				break;
			case R.id.linearLayoutTaxiDirectory:
				((BaseActivity) getActivity()).redirectTaxiSearchListActivity(getActivity());
				break;
			case R.id.linearLayoutPhotoGallery:
				//((BaseActivity) getActivity()).redirectPhotoGalleryActivity(getActivity());
				((BaseActivity) getActivity()).redirectFragmentMyDashboard(getActivity());
				break;
			case R.id.linearLayoutTriviaGame:
				((BaseActivity) getActivity()).redirectFragmentBarTriviaGame(getActivity());
				break;
			case R.id.linearLayoutArticles:
				((BaseActivity) getActivity()).redirectFragmentArticles(getActivity());
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
