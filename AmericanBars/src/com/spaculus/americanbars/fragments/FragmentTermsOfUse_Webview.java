package com.spaculus.americanbars.fragments;

import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class FragmentTermsOfUse_Webview extends Fragment {
	
	private View rootView = null;
	private WebView webView;
	private ProgressBar progressBar;
	
	//  To know whether user is redirected to this screen from navigation drawer or not
	//  So on the basis of it we can show the action bar fields
	boolean isRedirectedFromMainActivity = false;
	
	/* To know which CMS page need to load */
	@SuppressWarnings("unused")
	private String slugName = "";
	
	public FragmentTermsOfUse_Webview(boolean flag, String slugValue){
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
			
			/*webView.setWebViewClient(new myWebClient());
			webView.getSettings().setJavaScriptEnabled(true);
			webView.getSettings().setBuiltInZoomControls(true);
			webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);*/
			//webView.loadUrl(ConfigConstants.getInstance().CMSPageURL+slugName);
			webView.setBackgroundColor(Color.TRANSPARENT);
			/*webView.getSettings().setLoadWithOverviewMode(true);
			webView.getSettings().setUseWideViewPort(true);
			webView.getSettings().setBuiltInZoomControls(true);
			webView.getSettings().setDefaultZoom(ZoomDensity.FAR);*/
			webView.getSettings().setBuiltInZoomControls(true);
			String data = "<p>Welcome to Colorado! Now, watch your ash! Boulder County craft breweries are working together to make the public aware about a bug.&nbsp;</p>\n\n<p>\"WATCH YOUR ASH!\" one side of the paper coaster cheerfully warns drinkers from under their glass. The other side encourages people to slow the spread of emerald ash borer by not taking hard wood out of Boulder County and removing affected ash trees.</p>\n\n<p><img alt=\"Emerald Ash Borer\" src=\"https://americanbars.com/upload/barlogo/1460059571_eab_emerging_website.jpg\" style=\"height:378px; width:650px\" /></p>\n\n<p><img alt=\"\" src=\"https://americanbars.com/upload/barlogo/1460059639_Emerald-Ash-borer-damage.jpg\" style=\"height:413px; width:650px\" /></p>\n\n<p>Emerald ash borer has only been confirmed within in Colorado in the city of Boulder, according to the county's Emerald Ash Borer webpage.</p>\n\n<p>Gabi Boerkircher, a communications specialist with the county, came up with the idea for the coasters during one of the monthly meetings the county employees have about emerald ash borer. (hmmm…I wonder where they hold these monthly meetings, eh?)&nbsp;</p>\n\n<p>The goal is to try to reach a lot of the folks who may not be following traditional news outlets,</p>\n\n<p>Yes, there are places in sophisticated places in the nation where people don’t actually look at news. Heavens! Therefore the county used $750 of leftover grant funding to print 5,000 coasters. (Yes, we paused also…Leftover Grant Money? Impossible! And 5,000 coasters in a college town — University of Colorado is located there — that should last, what? About three hours?)</p>\n\n<p><img alt=\"EAB\" src=\"https://americanbars.com/upload/barlogo/1460059718_Still0406_00000_1459981058556_1435821_ver1.0.jpg\" style=\"height:366px; width:650px\" /></p>\n\n<p>And here is the kicker: The coasters went to select bars and pubs in Longmont, Lafayette, Louisville, Erie, Superior, Lyons, Gunbarrel and Niwot. Yes, the problem is in Boulder, but the coasters went to outlying towns.&nbsp;</p>\n\n<p>Yes, Boulder watering holes didn't receive the coasters because there's already major emerald ash borer awareness efforts there and there's quite a few bars in the city.</p>\n\n<p>Teddy McMurdo, a manager at the Pumphouse Brewery in Longmont, said Pumphouse received ash borer coasters, but they're already out. Patrons found the coasters so interesting, many took them home, McMurdo said.</p>\n\n<p>\"And they were free, so we were happy to spread the word,\" he said.</p>\n\n<p>Jean Ditslear, owner of 300 Suns Brewing in Longmont, said she received three stacks of the coasters and is just starting to use them.</p>\n\n<p>\"I think it's cute,\" Ditslear said. \"It's a really unique way to spread the word. It's really clever and they have kind of a captive audience sitting there drinking beer.\"</p>\n\n<p>Ditslear added that her customers think the \"WATCH YOUR ASH!\" slogan is pretty funny.</p>\n\n<p>The campaign has been so successful, they plan to print another 5,000 coasters soon.&nbsp;</p>\n\n<p>So, environmental issues, social messaging, and community awareness all community awareness…all from beer coasters.&nbsp;</p>\n\n<p>What new ideas have you generated today?</p>\n";
			//data == html data which you want to load 
			//webView.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");
			
			data = data.replaceAll("width:650px", "width:100%");
			//NSString *temp = [htmlString stringByReplacingOccurrencesOfString:@"width:650px" withString:@"width:100%"];
			webView.loadData("<font color=\"white\">" + data + "</font>","text/html","utf-8");
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
	
	public class myWebClient extends WebViewClient {
    	@Override
    	public void onPageStarted(WebView view, String url, Bitmap favicon) {
    		try {
				super.onPageStarted(view, url, favicon);
			} 
    		catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	@SuppressLint("SetJavaScriptEnabled")
		@Override
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
    		try {
				//view.loadUrl(url);
    			String data = "<p>Welcome to Colorado! Now, watch your ash! Boulder County craft breweries are working together to make the public aware about a bug.&nbsp;</p>\n\n<p>\"WATCH YOUR ASH!\" one side of the paper coaster cheerfully warns drinkers from under their glass. The other side encourages people to slow the spread of emerald ash borer by not taking hard wood out of Boulder County and removing affected ash trees.</p>\n\n<p><img alt=\"Emerald Ash Borer\" src=\"https://americanbars.com/upload/barlogo/1460059571_eab_emerging_website.jpg\" style=\"height:378px; width:650px\" /></p>\n\n<p><img alt=\"\" src=\"https://americanbars.com/upload/barlogo/1460059639_Emerald-Ash-borer-damage.jpg\" style=\"height:413px; width:650px\" /></p>\n\n<p>Emerald ash borer has only been confirmed within in Colorado in the city of Boulder, according to the county's Emerald Ash Borer webpage.</p>\n\n<p>Gabi Boerkircher, a communications specialist with the county, came up with the idea for the coasters during one of the monthly meetings the county employees have about emerald ash borer. (hmmm…I wonder where they hold these monthly meetings, eh?)&nbsp;</p>\n\n<p>The goal is to try to reach a lot of the folks who may not be following traditional news outlets,</p>\n\n<p>Yes, there are places in sophisticated places in the nation where people don’t actually look at news. Heavens! Therefore the county used $750 of leftover grant funding to print 5,000 coasters. (Yes, we paused also…Leftover Grant Money? Impossible! And 5,000 coasters in a college town — University of Colorado is located there — that should last, what? About three hours?)</p>\n\n<p><img alt=\"EAB\" src=\"https://americanbars.com/upload/barlogo/1460059718_Still0406_00000_1459981058556_1435821_ver1.0.jpg\" style=\"height:366px; width:650px\" /></p>\n\n<p>And here is the kicker: The coasters went to select bars and pubs in Longmont, Lafayette, Louisville, Erie, Superior, Lyons, Gunbarrel and Niwot. Yes, the problem is in Boulder, but the coasters went to outlying towns.&nbsp;</p>\n\n<p>Yes, Boulder watering holes didn't receive the coasters because there's already major emerald ash borer awareness efforts there and there's quite a few bars in the city.</p>\n\n<p>Teddy McMurdo, a manager at the Pumphouse Brewery in Longmont, said Pumphouse received ash borer coasters, but they're already out. Patrons found the coasters so interesting, many took them home, McMurdo said.</p>\n\n<p>\"And they were free, so we were happy to spread the word,\" he said.</p>\n\n<p>Jean Ditslear, owner of 300 Suns Brewing in Longmont, said she received three stacks of the coasters and is just starting to use them.</p>\n\n<p>\"I think it's cute,\" Ditslear said. \"It's a really unique way to spread the word. It's really clever and they have kind of a captive audience sitting there drinking beer.\"</p>\n\n<p>Ditslear added that her customers think the \"WATCH YOUR ASH!\" slogan is pretty funny.</p>\n\n<p>The campaign has been so successful, they plan to print another 5,000 coasters soon.&nbsp;</p>\n\n<p>So, environmental issues, social messaging, and community awareness all community awareness…all from beer coasters.&nbsp;</p>\n\n<p>What new ideas have you generated today?</p>\n";
				//data == html data which you want to load 
    			view.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");
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
