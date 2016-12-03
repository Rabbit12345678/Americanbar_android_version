package com.spaculus.americanbars.fragments;

import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.R;
import com.spaculus.helpers.ConfigConstants;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

@SuppressWarnings("deprecation")
public class FragmentTermsOfUse extends Fragment {
	
	private View rootView = null;
	private WebView webView;
	private ProgressBar progressBar;
	
	//  To know whether user is redirected to this screen from navigation drawer or not
	//  So on the basis of it we can show the action bar fields
	boolean isRedirectedFromMainActivity = false;
	
	/* To know which CMS page need to load */
	private String slugName = "";
	
	public FragmentTermsOfUse(boolean flag, String slugValue){
		this.isRedirectedFromMainActivity = flag;
		this.slugName = slugValue;
	}
	
	@SuppressLint("SetJavaScriptEnabled") 
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		try {
			rootView = inflater.inflate(R.layout.fragment_terms_of_use, container, false);
			
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
			
			webView.setWebViewClient(new myWebClient());
			webView.getSettings().setJavaScriptEnabled(true);
			webView.getSettings().setBuiltInZoomControls(true);
			webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			webView.loadUrl(ConfigConstants.CMSPageURLs.CMSPageURL+slugName);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
        return rootView;
    }
	
	//  This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			webView = (WebView)rootView.findViewById(R.id.webView);
			progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class myWebClient extends WebViewClient {
    	@Override
    	public void onPageStarted(WebView view, String url, Bitmap favicon) {
    		try {
				super.onPageStarted(view, url, favicon);
			} 
    		catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	@Override
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
    		try {
				view.loadUrl(url);
			} 
    		catch (Exception e) {
				e.printStackTrace();
			}
    		return true;
    	}
    	
    	@Override
    	public void onPageFinished(WebView view, String url) {
    		try {
				super.onPageFinished(view, url);
				progressBar.setVisibility(View.GONE);
			} 
    		catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }
    
    /*// To handle "Back" key press event for WebView to go back to previous screen.
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
			web.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}*/
}
