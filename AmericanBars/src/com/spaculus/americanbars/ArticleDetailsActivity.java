package com.spaculus.americanbars;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.spaculus.beans.Article;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class ArticleDetailsActivity extends BaseActivity {
	private TextView tvArticleTitle, tvArticleDate;
	private RatingBar ratingBarArticle;
	@SuppressWarnings("unused")
	private ImageView ivShare, ivArticleImage;
	private Spinner spinnerShare;
	private ProgressBar progressBar;
	private WebView webView;
	
	/* For the Rating Bar functionality */
	private float selectedRating = 0.0f;
	private AsynTaskUpdateRating objAsynTaskUpdateRating = null;
	/* For the Rating Bar functionality */
	
	/* Sharing functionality */
 	private String selectedSearchOption = "";
 	//  To prevent the spinner for a default call
 	private boolean isShareImageClickedOnce = false;
	/* Sharing functionality */
 	
	/* To get the Article's object */
 	private Article objArticle = null;
 	
 	private AsynTaskGetArticleDetails objAsynTaskGetArticleDetails = null;
 	private String articleDetailsURL = "";
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_article_details);
			
			// Create ActionBar
			createActionBar("Article Details", R.layout.custom_actionbar, ArticleDetailsActivity.this, true);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);
			
			/* To get the Article's object */
			Intent intent = getIntent();
			objArticle = (Article) intent.getParcelableExtra(ConfigConstants.Keys.KEY_ARTICLE_OBJECT);
			
			// Mapping of all the views
			mappedAllViews();
			
			//  For the Spinner
			bindSpinnerData(spinnerShare);
			
			/* Get the Article Details data here. */
			if (objAsynTaskGetArticleDetails == null) {
				if (Utils.getInstance().isNetworkAvailable(ArticleDetailsActivity.this)) {
					objAsynTaskGetArticleDetails = new AsynTaskGetArticleDetails();
					objAsynTaskGetArticleDetails.execute();
				} else {
					Utils.getInstance().showToastNoInternetAvailable(ArticleDetailsActivity.this);
				}
			}
		
			//  Share Click Event
			ivShare.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						isShareImageClickedOnce = true;
						Utils.getInstance().displayToast(ArticleDetailsActivity.this, "Share Image is clicked.");
						spinnerShare.performClick();
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			//  Spinner for the Search Options
			spinnerShare.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg, int position, long arg3) {
					try {
						if(isShareImageClickedOnce) {
							//Log.i("onItemSelected", "onItemSelected");
							//  To get the value
							selectedSearchOption = searchOptionsList.get(position);
							String packageName = "";
							String constantOption = "";
							if(selectedSearchOption.equals(ConfigConstants.Constants.CONSTANT_FACEBOOK)) {
								packageName = "com.facebook.katana";
								constantOption = ConfigConstants.Constants.CONSTANT_FACEBOOK;
							}
							else if(selectedSearchOption.equals(ConfigConstants.Constants.CONSTANT_TWITTER)) {
								packageName = "com.twitter.android";
								constantOption = ConfigConstants.Constants.CONSTANT_TWITTER;
							}
							/*else if(selectedSearchOption.equals(ConfigConstants.getInstance().CONSTANT_LINKEDIN)) {
								packageName = "com.linkedin.android";
								constantOption = ConfigConstants.getInstance().CONSTANT_LINKEDIN;
							}*/
							else if(selectedSearchOption.equals(ConfigConstants.Constants.CONSTANT_GOOGLE_PLUS)) {
								packageName = "com.google.android.apps.plus";
								constantOption = ConfigConstants.Constants.CONSTANT_GOOGLE_PLUS;
							}
							else if(selectedSearchOption.equals(ConfigConstants.Constants.CONSTANT_PINTEREST)) {
								packageName = "com.pinterest";
								constantOption = ConfigConstants.Constants.CONSTANT_PINTEREST;
							}
							
							/* Now, here generate a sharing link first of all */
							String shareURL = ConfigConstants.SharingURLs.shareAlbumDetatils+Utils.getInstance().convertToBase64(objArticle.getBlog_id());
							//  Now, call a method to share a link
							shareTextLink(packageName, shareURL, constantOption);
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
					}	
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		ratingBarArticle.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				selectedRating = rating;
				/* Now, here call a web service to update the rating value */
				//if(rating <= 0) {
					if (objAsynTaskUpdateRating == null) {
						if (Utils.getInstance().isNetworkAvailable(ArticleDetailsActivity.this)) {
							objAsynTaskUpdateRating = new AsynTaskUpdateRating();
							objAsynTaskUpdateRating.execute();
						} else {
							Utils.getInstance().showToastNoInternetAvailable(ArticleDetailsActivity.this);
						}
					}
				/*}
				else {
					Utils.toastLong(ArticleDetailsActivity.this, "Please select Rating");
				}*/
			}
		});
	}
	
	// This method is used to do the mapping of all the views.
	@SuppressLint("SetJavaScriptEnabled")
	private void mappedAllViews() {
		try {
			tvArticleTitle = (TextView)findViewById(R.id.tvArticleTitle);
			tvArticleDate = (TextView)findViewById(R.id.tvArticleDate);
			ratingBarArticle = (RatingBar)findViewById(R.id.ratingBarArticle);
			ivShare = (ImageView)findViewById(R.id.ivShare);
			spinnerShare = (Spinner)findViewById(R.id.spinnerShare);
			ivArticleImage = (ImageView)findViewById(R.id.ivArticleImage);
			progressBar = (ProgressBar)findViewById(R.id.progressBar);
			webView = (WebView)findViewById(R.id.webView);
			webView.setBackgroundColor(Color.TRANSPARENT);
			webView.setWebViewClient(new myWebClient());
			webView.getSettings().setJavaScriptEnabled(true);
			webView.getSettings().setBuiltInZoomControls(false);
			webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			
			/* Now, set the Article's data here. */
			/* Title */
			tvArticleTitle.setText(Utils.getInstance().setCapitalLetter(objArticle.getBlog_title()));
			/* Date */
			tvArticleDate.setText(Utils.getInstance().getFormattedDate(objArticle.getDate_added(), ConfigConstants.DateFormats.YYYY_MM_DD_HH_MM_SS, ConfigConstants.DateFormats.MM_DD_YYYY));
			/* Ratings */
			if(objArticle.getTotal_rating().isEmpty()) {
				ratingBarArticle.setIsIndicator(false);
			}
			else {
				ratingBarArticle.setRating(Float.parseFloat(objArticle.getTotal_rating()));
				ratingBarArticle.setIsIndicator(true);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
		
	/* An AnyncTask for the report a bar functionality. */
	public class AsynTaskUpdateRating extends AsyncTask<String, Void, String> {
		private ProgressDialog pd = new ProgressDialog(ArticleDetailsActivity.this);
		@Override
		protected void onPreExecute() {
			try {
				super.onPreExecute();
				this.pd.setMessage(ConfigConstants.Messages.loadingMessage);
				pd.setCanceledOnTouchOutside(false);
				pd.setCancelable(false);
				this.pd.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				ServiceHandler sh = new ServiceHandler();
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(ArticleDetailsActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(ArticleDetailsActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(ArticleDetailsActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("blog_id", objArticle.getBlog_id()));
		        nameValuePairs.add(new BasicNameValuePair("blog_rating", ""+selectedRating));
	        	// Making a request to URL and getting response
		        String responseString = sh.makeServiceCall(ConfigConstants.Urls.add_rating, ServiceHandler.POST, nameValuePairs);
	        	Log.i("responseString",responseString);
				return responseString;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String response) {
			try {
				super.onPostExecute(response);
				JSONObject jsonObjParent = null;
				if (response != null) {
					jsonObjParent = new JSONObject(response);
					String status = Utils.getInstance().isTagExists(jsonObjParent, "status");
					if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)) {
						Utils.getInstance().displayToast(ArticleDetailsActivity.this, "Your article rating is updated successfully.");
						ratingBarArticle.setIsIndicator(true);
						ConfigConstants.isArticleRatingDone = true;
					}
					else {
						Utils.getInstance().displayToast(ArticleDetailsActivity.this, "Your article rating is not updated. Please try again.");
						ratingBarArticle.setIsIndicator(false);
					}
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}
			objAsynTaskUpdateRating = null;
			if (this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}
	}
	
	/* An AnyncTask to get the article details here. */
	public class AsynTaskGetArticleDetails extends AsyncTask<String, Void, String> {
		private ProgressDialog pd = new ProgressDialog(ArticleDetailsActivity.this);
		@Override
		protected void onPreExecute() {
			try {
				super.onPreExecute();
				this.pd.setMessage(ConfigConstants.Messages.loadingMessage);
				pd.setCanceledOnTouchOutside(false);
				pd.setCancelable(false);
				this.pd.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				ServiceHandler sh = new ServiceHandler();
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(ArticleDetailsActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(ArticleDetailsActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(ArticleDetailsActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("article_id", objArticle.getBlog_id()));
	        	// Making a request to URL and getting response
		        String responseString = sh.makeServiceCall(ConfigConstants.Urls.articledetail, ServiceHandler.POST, nameValuePairs);
	        	Log.i("responseString",responseString);
				return responseString;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String response) {
			try {
				super.onPostExecute(response);
				JSONObject jsonObjParent = null;
				if (response != null) {
					jsonObjParent = new JSONObject(response);
					String status = Utils.getInstance().isTagExists(jsonObjParent, "status");
					if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)) {
						articleDetailsURL = Utils.getInstance().isTagExists(jsonObjParent, "url");
						/* Now, load the url here. */
						webView.loadUrl(articleDetailsURL);
					}
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}
			objAsynTaskGetArticleDetails = null;
			if (this.pd.isShowing()) {
				this.pd.dismiss();
			}
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
}
