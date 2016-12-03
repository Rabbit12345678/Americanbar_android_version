package com.spaculus.americanbars;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.spaculus.beans.Beer;
import com.spaculus.beans.Cocktail;
import com.spaculus.beans.Liquor;
import com.spaculus.beans.Review;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.ParseJsonObject;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;
import com.spaculus.helpers.Validation;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class HalfMugBarDetailsActivity extends BaseActivity implements OnClickListener {

	//  To get the selected bar id
	private String bar_id = "";
	
	/* Header Views */
	@SuppressWarnings("unused")
	private RelativeLayout relativeLayoutHeader;
	private ImageView ivBarLogo;
	private TextView tvBarName, tvBarAddress, tvBarWebsite, tvBarPhoneNumber;
	@SuppressWarnings("unused")
	private LinearLayout linearLayoutStars;
	private ImageView[] ivStarRatingsArray = null;
	/* Header Views */
	/* Description */
	private TextView tvDescription;
	private RelativeLayout relativeLayoutShowMoreDesc;
	/* Description */
	/* Beer Served at Bar */
	private LinearLayout hiddenLinearLayoutBeers;
	private ArrayList<Beer> beersList = null;
	private RelativeLayout relativeLayoutViewAllBeers;
	private TextView tvNoBeers;
	/* Beer Served at Bar */
	/* Cocktail/Liquors Served at Bar */
	private LinearLayout hiddenLinearLayoutCocktailsLiquors;
	private ArrayList<Cocktail> cocktailsList = null;
	private ArrayList<Liquor> liquorList = null;
	private RelativeLayout relativeLayoutViewAllCocktails;
	private TextView tvNoCocktailsLiquors;
	/* Cocktail/Liquors Served at Bar */
	/* Raves And Rants */
	private LinearLayout hiddenLinearLayoutReviews;
	private ArrayList<Review> barReviewsList = null;
	private TextView tvNoReviews;
	/* Raves And Rants */
	
	//  To check whether Cocktails or Liquors
	//  To display full information
	private String serve_as = "", description = ""; //videoURL = "";
	
	/* Google Map */
	private GoogleMap googleMap;
	private Button buttonGetDirections;
	private double latitude = 0.0, longitude = 0.0;
	/* Google Map */
	
	/* Sharing functionality */
	private ImageView[] ivShareArray = null;
	private ImageView ivShare;
	private String facebook_link = "", twitter_link = "", linkedin_link = "", google_plus_link = "", dribble_link = "", pinterest_link = "";
	private Spinner spinnerShare;
	//  For the Share Options
	//private ArrayList<String> searchOptionsList = null;
 	//private ArrayAdapter<String> adapterSearchOptions = null;
 	private String selectedSearchOption = "";
 	//  To prevent the spinner for a default call
 	private boolean isShareImageClickedOnce = false;
	/* Sharing functionality */
 	
 	private Button buttonAddToFavorite, buttonLikeThisBar;
 	
 	private RelativeLayout relativeLayoutHalfMugBar;
 	
 	/* For the Report Bar functionality */
 	private EditText etEmailReportBar, etDescReportThisBar;
 	private RadioButton radioButtonClosed, radioButtonWrongAddress, radioButtonIsNotABar, radioButtonOther;
 	private Button buttonReportThisBar;
 	private String selectedRadioType = ConfigConstants.Constants.CONSTANT_REPORT_BAR_CLOSED;
 	private AsynTaskReportABar objAsynTaskReportABar = null;
 	/* For the Report Bar functionality */
 	
 	/* Write A Review functionality */
 	private EditText etReviewTitle, etReviewDescription;
 	private RatingBar ratingBarReview;
 	private Button buttonPostReview;
 	private RelativeLayout relativeLayoutViewAllRavesAndRants;
 	private float selectedRating = 0.0f;
 	private AsynTaskPostReview objAsynTaskPostReview = null;
	/* Write A Review functionality */
 	
 	private String bar_slug = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_half_mug_bar_details);
			
			// Create ActionBar
			createActionBar("Half Mug Bar", R.layout.custom_actionbar, HalfMugBarDetailsActivity.this, true);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);
			
			// Mapping of all the views
			mappedAllViews();
			
			//  To get the bar id
			Bundle b = getIntent().getExtras();
			bar_id = b.getString(ConfigConstants.Keys.KEY_BAR_ID);
			
			//  For the Spinner
			bindSpinnerData(spinnerShare);
			
			//  Now, first of all get the bar details
			if(Utils.getInstance() .isNetworkAvailable(HalfMugBarDetailsActivity.this)) {
				new AsynTaskGetBarDetails().execute();
			}
			else{
				Utils.getInstance().showToastNoInternetAvailable(HalfMugBarDetailsActivity.this);
			}
			
			//  Show More - Description
			relativeLayoutShowMoreDesc.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						//  Now, show the alert dialog for the Description
						showMoreDialog(ConfigConstants.Constants.DESCRIPTION, description);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			/*//  To play a Video
			relativeLayoutVideo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					
					if(!videoURL.isEmpty()) {
						Intent intent = new Intent(HalfMugBarDetailsActivity.this, YouTubeVideoPlayActivity.class);
						intent.putExtra(YouTubeVideoPlayActivity.KEY_VIDEO_ID, videoID);
						startActivity(intent); 	
					}
					else {
						Toast.makeText(HalfMugBarDetailsActivity.this, "No Video Available", Toast.LENGTH_SHORT).show();
					}
					
				}
			});*/
			
			//  Sharing / Redirect Page Links Click event
			for(int i=0; i<ivShareArray.length; i++) {
				ivShareArray[i].setOnClickListener(this);
			}
			
			//  Share Click Event
			ivShare.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						isShareImageClickedOnce = true;
						//Toast.makeText(HalfMugBarDetailsActivity.this, "Share Image is clicked.", Toast.LENGTH_SHORT).show();
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
							
							//  Now, call a method to share a link
							shareTextLink(packageName, ConfigConstants.SharingURLs.shareBarDetailsURL+bar_slug, constantOption);
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
			
			//  View All - Beers List click event
			relativeLayoutViewAllBeers.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						redirectBeerSearchListActivity(HalfMugBarDetailsActivity.this, bar_id);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  View All - Cocktails / Liquor List click event
			relativeLayoutViewAllCocktails.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						//  Means Liquor
						if(serve_as.equals(ConfigConstants.Constants.LIQUOR)) {
							redirectLiquorSearchListActivity(HalfMugBarDetailsActivity.this, bar_id);
						}
						//  Means Cocktails
						else {
							redirectCocktailSearchListActivity(HalfMugBarDetailsActivity.this, bar_id);
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  Add To Favorite button click event
			buttonAddToFavorite.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						ConfigConstants.isAddToFavoriteORLikeBarButtonClicked = true;
						//  Now, first of all check whether user is already logged in or not
						if(SessionManager.getInstance(HalfMugBarDetailsActivity.this).isLoggedIn()) {
							//  Called the AsyncTask for Add To Favorite functionality
							if(Utils.getInstance().isNetworkAvailable(HalfMugBarDetailsActivity.this)) {		
								new AsynTaskAddToFavorite().execute();
							}
							else{
								Utils.getInstance().showToastNoInternetAvailable(HalfMugBarDetailsActivity.this);
							}
						}
						else {
							//  Means user is not logged in
							redirectFragmentSignIn(HalfMugBarDetailsActivity.this);
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  Like This Bar button click event
			buttonLikeThisBar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						ConfigConstants.isAddToFavoriteORLikeBarButtonClicked = true;
						//  Now, first of all check whether user is already logged in or not
						if(SessionManager.getInstance(HalfMugBarDetailsActivity.this).isLoggedIn()) {
						//  Called the AsyncTask for Like This Bar functionality
							if(Utils.getInstance().isNetworkAvailable(HalfMugBarDetailsActivity.this)) {		
								new AsynTaskLikeThisBar().execute();
							}
							else{
								Utils.getInstance().showToastNoInternetAvailable(HalfMugBarDetailsActivity.this);
							}
						}
						else {
							//  Means user is not logged in
							redirectFragmentSignIn(HalfMugBarDetailsActivity.this);
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  Get Directions button click event
			buttonGetDirections.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						/* Redirect to the Google Maps Application */
						redirectToGoogleMapApplicationShowLocation(HalfMugBarDetailsActivity.this, latitude, longitude, tvBarName.getText().toString().trim(), tvBarAddress.getText().toString().trim());
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  Click on a phone number to do a call
			tvBarPhoneNumber.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						redirectToPhoneApplication(tvBarPhoneNumber.getText().toString().trim());
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		//  Click on Address
		tvBarAddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					redirectGoogleMapApplicationDrawPath(tvBarAddress.getText().toString().trim());
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		radioButtonClosed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedRadioType = ConfigConstants.Constants.CONSTANT_REPORT_BAR_CLOSED;
				etDescReportThisBar.setVisibility(View.GONE);
			}
		});
		
		radioButtonWrongAddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedRadioType = ConfigConstants.Constants.CONSTANT_REPORT_BAR_WRONG_ADDRESS;
				etDescReportThisBar.setVisibility(View.GONE);
			}
		});
		
		radioButtonIsNotABar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedRadioType = ConfigConstants.Constants.CONSTANT_REPORT_BAR_IS_NOT_A_BAR;
				etDescReportThisBar.setVisibility(View.GONE);
			}
		});
		
		radioButtonOther.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedRadioType = ConfigConstants.Constants.CONSTANT_REPORT_BAR_OTHER;
				etDescReportThisBar.setVisibility(View.VISIBLE);
			}
		});
		
		buttonReportThisBar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//  First, hide the soft key board.
				hideSoftKeyboard();
				//  Now, first of all check all the validations
				String validationResponse  = Validation.getInstance().reportABar(etEmailReportBar.getText().toString().trim(), selectedRadioType, etDescReportThisBar.getText().toString().trim());
				
				//  Means all the fields are properly entered
				if(validationResponse.isEmpty()){
					if (objAsynTaskReportABar == null) {
						if (Utils.getInstance().isNetworkAvailable(HalfMugBarDetailsActivity.this)) {
							objAsynTaskReportABar = new AsynTaskReportABar();
							objAsynTaskReportABar.execute();
						} else {
							Utils.getInstance().showToastNoInternetAvailable(HalfMugBarDetailsActivity.this);
						}
					}
					else{
						Utils.getInstance().showToastNoInternetAvailable(HalfMugBarDetailsActivity.this);
					}
				}
				else {
					Utils.toastLong(HalfMugBarDetailsActivity.this, validationResponse);
				}
			}
		});
		
		relativeLayoutHalfMugBar.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility") 
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(v==relativeLayoutHalfMugBar){
					hideSoftKeyboard();
					return true;
				}
				return false;
			}
		});
		
		ratingBarReview.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				selectedRating = rating;
			}
		});
		
		buttonPostReview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					//  First, hide the soft key board.
					hideSoftKeyboard();
					//  Now, first of all check all the validations
					String validationResponse  = Validation.getInstance().writeAReview(etReviewTitle.getText().toString().trim(), etReviewDescription.getText().toString().trim(), selectedRating);
					
					//  Means all the fields are properly entered
					if(validationResponse.isEmpty()) {
						if(Utils.getInstance() .isNetworkAvailable(HalfMugBarDetailsActivity.this)) {
							if (objAsynTaskPostReview == null) {
								if (Utils.getInstance().isNetworkAvailable(HalfMugBarDetailsActivity.this)) {
									objAsynTaskPostReview = new AsynTaskPostReview();
									objAsynTaskPostReview.execute();
								} else {
									Utils.getInstance().showToastNoInternetAvailable(HalfMugBarDetailsActivity.this);
								}
							}
						}
						else{
							Utils.getInstance().showToastNoInternetAvailable(HalfMugBarDetailsActivity.this);
						}
					}
					else {
						Utils.toastLong(HalfMugBarDetailsActivity.this, validationResponse);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//  View All - Reviews click event
		relativeLayoutViewAllRavesAndRants.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					redirectBarReviewsListActivity(HalfMugBarDetailsActivity.this, bar_id);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		etDescReportThisBar.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					if (v.getId() == R.id.etDescReportThisBar) {
						v.getParent().requestDisallowInterceptTouchEvent(true);
						switch (event.getAction() & MotionEvent.ACTION_MASK) {
						case MotionEvent.ACTION_UP:
							v.getParent().requestDisallowInterceptTouchEvent(false);
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		});
		
		etReviewDescription.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					if (v.getId() == R.id.etReviewDescription) {
						v.getParent().requestDisallowInterceptTouchEvent(true);
						switch (event.getAction() & MotionEvent.ACTION_MASK) {
						case MotionEvent.ACTION_UP:
							v.getParent().requestDisallowInterceptTouchEvent(false);
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		});
	}
	
	@Override
	protected void onResume() {
		try {
			super.onResume();
			/* This is implemented in case if user is not logged in when click on Add To Favorite or Like This Bar buttons and if then do a login. */
			//  Need to load the data again if the Add To Favorite button or Like This Bar button is clicked
			if(ConfigConstants.isAddToFavoriteORLikeBarButtonClicked || ConfigConstants.isBarReviewAdded) {
				ConfigConstants.isAddToFavoriteORLikeBarButtonClicked = false;
				ConfigConstants.isBarReviewAdded = false;
			
				//  Now, check whether user has done login or not
				if(SessionManager.getInstance(HalfMugBarDetailsActivity.this).isLoggedIn()) {
					//  Now, first of all get the bar details
					if(Utils.getInstance() .isNetworkAvailable(HalfMugBarDetailsActivity.this)) {
						new AsynTaskGetBarDetails().execute();
					}
					else{
						Utils.getInstance().showToastNoInternetAvailable(HalfMugBarDetailsActivity.this);
					}
				}	
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			/* Header Views */
			relativeLayoutHeader = (RelativeLayout)findViewById(R.id.relativeLayoutHeader);
			ivBarLogo = (ImageView)findViewById(R.id.ivBarLogo);
			tvBarName = (TextView)findViewById(R.id.tvBarName);
			tvBarAddress = (TextView)findViewById(R.id.tvBarAddress);
			tvBarWebsite = (TextView)findViewById(R.id.tvBarWebsite);
			tvBarPhoneNumber = (TextView)findViewById(R.id.tvBarPhoneNumber);
			linearLayoutStars = (LinearLayout)findViewById(R.id.linearLayoutStars);
			ivStarRatingsArray = new ImageView[5];
			ivStarRatingsArray[0] = (ImageView)findViewById(R.id.ivStar1);
			ivStarRatingsArray[1] = (ImageView)findViewById(R.id.ivStar2);
			ivStarRatingsArray[2] = (ImageView)findViewById(R.id.ivStar3);
			ivStarRatingsArray[3] = (ImageView)findViewById(R.id.ivStar4);
			ivStarRatingsArray[4] = (ImageView)findViewById(R.id.ivStar5);
			/* Header Views */
			/* Description */
			tvDescription = (TextView)findViewById(R.id.tvDescription);
			relativeLayoutShowMoreDesc = (RelativeLayout)findViewById(R.id.relativeLayoutShowMoreDesc);
			/* Description */
			/* Beer Served at Bar */
			beersList = new ArrayList<Beer>();
			hiddenLinearLayoutBeers = (LinearLayout)findViewById(R.id.hiddenLinearLayoutBeers);
			relativeLayoutViewAllBeers = (RelativeLayout)findViewById(R.id.relativeLayoutViewAllBeers);
			tvNoBeers = (TextView)findViewById(R.id.tvNoBeers);
			/* Beer Served at Bar */
			/* Cocktail/Liquors Served at Bar */
			cocktailsList = new ArrayList<Cocktail>();
			liquorList = new ArrayList<Liquor>();
			hiddenLinearLayoutCocktailsLiquors = (LinearLayout)findViewById(R.id.hiddenLinearLayoutCocktailsLiquors);
			relativeLayoutViewAllCocktails = (RelativeLayout)findViewById(R.id.relativeLayoutViewAllCocktails);
			tvNoCocktailsLiquors = (TextView)findViewById(R.id.tvNoCocktailsLiquors);
			/* Cocktail/Liquors Served at Bar */
			/* Raves And Rants */
			barReviewsList = new ArrayList<Review>();
			hiddenLinearLayoutReviews = (LinearLayout)findViewById(R.id.hiddenLinearLayoutReviews);
			tvNoReviews = (TextView)findViewById(R.id.tvNoReviews);
			/* Raves And Rants */	
			/* Google Map */
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			googleMap.getUiSettings().setZoomControlsEnabled(true);
			buttonGetDirections = (Button)findViewById(R.id.btnGetDirections);
			/* Google Map */
			/* Sharing functionality */
			ivShareArray = new ImageView[6];
			ivShareArray[0] = (ImageView)findViewById(R.id.ivShareFacebook);
			ivShareArray[1] = (ImageView)findViewById(R.id.ivShareTwitter);
			ivShareArray[2] = (ImageView)findViewById(R.id.ivShareLinkedIn);
			ivShareArray[3] = (ImageView)findViewById(R.id.ivShareGooglePlus);
			ivShareArray[4] = (ImageView)findViewById(R.id.ivShareDribble);
			ivShareArray[5] = (ImageView)findViewById(R.id.ivSharePinterest);
			ivShare = (ImageView)findViewById(R.id.ivShare);
			spinnerShare = (Spinner)findViewById(R.id.spinnerShare);
			/* Sharing functionality */
			
			buttonAddToFavorite = (Button)findViewById(R.id.btnAddToFavorite);
			buttonLikeThisBar = (Button)findViewById(R.id.btnLikeThisBar);
			
			/* For the Report Bar functionality */
			etEmailReportBar = (EditText)findViewById(R.id.etEmailReportBar);
			etDescReportThisBar = (EditText)findViewById(R.id.etDescReportThisBar);
			radioButtonClosed = (RadioButton)findViewById(R.id.radioButtonClosed);
			radioButtonWrongAddress = (RadioButton)findViewById(R.id.radioButtonWrongAddress);
			radioButtonIsNotABar = (RadioButton)findViewById(R.id.radioButtonIsNotABar);
			radioButtonOther = (RadioButton)findViewById(R.id.radioButtonOther);
			buttonReportThisBar = (Button)findViewById(R.id.btnReportThisBar);
			
			/* Set the Email id */
			etEmailReportBar.setText(SessionManager.getInstance(HalfMugBarDetailsActivity.this).getData(SessionManager.KEY_EMAIL));
			/* For the Report Bar functionality */

			relativeLayoutHalfMugBar = (RelativeLayout)findViewById(R.id.relativeLayoutHalfMugBar);
			
			/* Write A Review functionality */
			etReviewTitle = (EditText)findViewById(R.id.etReviewTitle);
			etReviewDescription = (EditText)findViewById(R.id.etReviewDescription);
			ratingBarReview = (RatingBar)findViewById(R.id.ratingBarReview);
			buttonPostReview = (Button)findViewById(R.id.buttonPostReview);
			relativeLayoutViewAllRavesAndRants = (RelativeLayout)findViewById(R.id.relativeLayoutViewAllRavesAndRants);
			/* Write A Review functionality */
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to get the Bar Details
	public class AsynTaskGetBarDetails extends AsyncTask<String, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(HalfMugBarDetailsActivity.this);
    	private String responseString = "";
    	private String status = "";
    	
    	@Override
		protected void onPreExecute() {
			try {
				super.onPreExecute();
				this.pd.setMessage(ConfigConstants.Messages.loadingMessage);
				pd.setCanceledOnTouchOutside(false); 
				pd.setCancelable(false);
				this.pd.show();
			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	

		@Override
		protected Void doInBackground(String... params) {
	        try {
	        	ServiceHandler sh = new ServiceHandler();
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        	nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(HalfMugBarDetailsActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(HalfMugBarDetailsActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(HalfMugBarDetailsActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("limit", "3"));
		        nameValuePairs.add(new BasicNameValuePair("offset", "0"));
		        nameValuePairs.add(new BasicNameValuePair("bar_id", bar_id));
			
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(ConfigConstants.Urls.bar_details, ServiceHandler.POST, nameValuePairs);
	        	//Log.i("responseString",responseString);
	        }
	        catch(Exception e) {
	        	e.printStackTrace();
	        }
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			try {
				super.onPostExecute(result);
				JSONObject jsonObjParent = null;
				if(responseString!=null){
					jsonObjParent = new JSONObject(responseString);
					status = jsonObjParent.getString("status");
					//Log.i("status",status);
				}
				
				if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)){
				
					/* Header Views / Description / Payment Type Accepted */
					if(jsonObjParent.has("bardetails")) {
						setHeaderViews( jsonObjParent.getJSONObject("bardetails"));
					}
					/* Header Views / Description / Payment Type Accepted */
					
					/* Beers Served at Bar */
					if(jsonObjParent.has("beerserved")) {
						setBeers( jsonObjParent.getJSONObject("beerserved"));
					}
					/* Beers Served at Bar */
					/* Cocktails/Liquors Served at Bar */
					if(serve_as.equals(ConfigConstants.Constants.LIQUOR)) {
						if(jsonObjParent.has("liquorserved")) {
							setCocktailsOrLiquors( jsonObjParent.getJSONObject("liquorserved"));
						}
					}
					else {
						if(jsonObjParent.has("cocktailserved")) {
							setCocktailsOrLiquors( jsonObjParent.getJSONObject("cocktailserved"));	
						}
					}
					/* Cocktails/Liquors Served at Bar */
					/* Bar Reviews */
					if(jsonObjParent.has("barreview")) {
						setBarReviews( jsonObjParent.getJSONObject("barreview"));	
					}
					/* Bar Reviews */
				}
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}
			if(this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}	
    }
	
	//  A method is used to get and set the header views data
	private void setHeaderViews(JSONObject jsonObjectBarDetails) {
		try {
			JSONArray jsonArrayResult = jsonObjectBarDetails.getJSONArray("result");
			JSONObject jsonObj = jsonArrayResult.getJSONObject(0);
			
			/* Header Views */
			//  Title
			tvBarName.setText(Utils.getInstance().isTagExists(jsonObj, "bar_title"));
			//  Address
			tvBarAddress.setText(Utils.getInstance().setAddress(Utils.getInstance().isTagExists(jsonObj, "address"), Utils.getInstance().isTagExists(jsonObj, "city"), Utils.getInstance().isTagExists(jsonObj, "state"), Utils.getInstance().isTagExists(jsonObj, "zipcode")));
			//  Web site
			if(Utils.getInstance().isTagExists(jsonObj, "website").isEmpty()) {
				tvBarWebsite.setVisibility(View.GONE);
			}
			else {
				tvBarWebsite.setVisibility(View.VISIBLE);
				tvBarWebsite.setText(Utils.getInstance().isTagExists(jsonObj, "website"));	
			}
			//  Phone
			tvBarPhoneNumber.setText(Utils.getInstance().isTagExists(jsonObj, "phone"));
			//  Now, for the review count functionality
			setStarReviews(Utils.getInstance().isTagExists(jsonObj, "total_rating"), Utils.getInstance().isTagExists(jsonObj, "total_commnets"), ivStarRatingsArray);
			//  Bar Logo
			setLogo(ConfigConstants.ImageUrls.barlogo_240, Utils.getInstance().isTagExists(jsonObj, "bar_logo"), ivBarLogo, R.drawable.no_image_bar);
			/* Header Views */
			
			/* Description */
			//tvDescription.setText(Utils.getInstance().isTagExists(jsonObj, "bar_desc"));
			tvDescription.setText(Utils.getInstance().setHTMLText(Utils.getInstance().isTagExists(jsonObj, "bar_desc")));
			if(tvDescription.length()==0) {
				relativeLayoutShowMoreDesc.setVisibility(View.GONE);
			}
			else {
				relativeLayoutShowMoreDesc.setVisibility(View.VISIBLE);
			}
			/* Description */
			
			//  Get all the required data
			serve_as = Utils.getInstance().isTagExists(jsonObj, "serve_as");
			description = Utils.getInstance().isTagExists(jsonObj, "bar_desc");
			facebook_link = Utils.getInstance().isTagExists(jsonObj, "facebook_link");
			twitter_link = Utils.getInstance().isTagExists(jsonObj, "twitter_link");
			linkedin_link = Utils.getInstance().isTagExists(jsonObj, "linkedin_link");
			google_plus_link = Utils.getInstance().isTagExists(jsonObj, "google_plus_link");
			dribble_link = Utils.getInstance().isTagExists(jsonObj, "dribble_link");
			pinterest_link = Utils.getInstance().isTagExists(jsonObj, "pinterest_link");
			bar_slug = Utils.getInstance().isTagExists(jsonObj, "bar_slug");
			
			//  Now, here hide the share page link icon if any page link is not available.
			String[] linksArray = {facebook_link, twitter_link, linkedin_link, google_plus_link, dribble_link, pinterest_link};
			for(int i=0; i<ivShareArray.length; i++) {
				if(linksArray[i].isEmpty()) {
					ivShareArray[i].setVisibility(View.GONE);
				}
				else {
					ivShareArray[i].setVisibility(View.VISIBLE);
				}
			}
			
			/* Show Map */
			if(Utils.getInstance().isTagExists(jsonObj, "lat").isEmpty() || Utils.getInstance().isTagExists(jsonObj, "lang").isEmpty()) {
				//  Then pass default USA's lat and long.
				showMap(googleMap, ConfigConstants.Constants.LATITUDE, ConfigConstants.Constants.LONGITUDE, tvBarName.getText().toString().trim(), ConfigConstants.Constants.USA);
				latitude = ConfigConstants.Constants.LATITUDE;
				longitude = ConfigConstants.Constants.LONGITUDE;
			}
			else {
				showMap(googleMap, Double.parseDouble(Utils.getInstance().isTagExists(jsonObj, "lat")), Double.parseDouble(Utils.getInstance().isTagExists(jsonObj, "lang")), tvBarName.getText().toString().trim(), tvBarAddress.getText().toString().trim());
				latitude = Double.parseDouble(Utils.getInstance().isTagExists(jsonObj, "lat"));
				longitude = Double.parseDouble(Utils.getInstance().isTagExists(jsonObj, "lang"));
			}
			/* Show Map */
			
			/* For the Add To Favorite, Like This Bar buttons click event */
			/*
			 * We need to consider the following values if the user is logged in
			 * o.w. not.
			 */
			if(SessionManager.getInstance(HalfMugBarDetailsActivity.this).isLoggedIn()) {
				//  Add To Favorite button
				setTextButtonAddToFavorite(buttonAddToFavorite, Utils.getInstance().isTagExists(jsonObj, "fav_bar"), ConfigConstants.Constants.BAR);
				
				//  Like This Bar button
				setTextButtonLike(buttonLikeThisBar, Utils.getInstance().isTagExists(jsonObj, "like_bar"), ConfigConstants.Constants.BAR);
			}
			/* For the Add To Favorite, Like This Bar buttons click event */
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	//  A method is used to get and set the Beers served at bar data
	private void setBeers(JSONObject jsonObjectBarDetails) {
		try {
			//  Now, here clear the list
			if(beersList.size()>0) {
				beersList.clear();
			}
			JSONArray jsonArrayResult = jsonObjectBarDetails.getJSONArray("result");
			for (int i = 0; i < jsonArrayResult.length(); i++) {
				JSONObject jsonObj = jsonArrayResult.getJSONObject(i);
				beersList.add(new Beer(Utils.getInstance().isTagExists(jsonObj, "beer_id"), Utils.getInstance().isTagExists(jsonObj, "beer_name"), Utils.getInstance().isTagExists(jsonObj, "beer_type"), 
						Utils.getInstance().isTagExists(jsonObj, "producer"), Utils.getInstance().isTagExists(jsonObj, "city_produced"), Utils.getInstance().isTagExists(jsonObj, "beer_image")));
			}
			
			if(hiddenLinearLayoutBeers.getChildCount() != 0) {
				hiddenLinearLayoutBeers.removeAllViews();
			}
			if(beersList.size()>0) {
				hiddenLinearLayoutBeers.setVisibility(View.VISIBLE);
				relativeLayoutViewAllBeers.setVisibility(View.VISIBLE);
				tvNoBeers.setVisibility(View.GONE);
				//  Now, set the Beers served at bar's data
				setBeersData();
			}
			else {
				relativeLayoutViewAllBeers.setVisibility(View.GONE);
				tvNoBeers.setVisibility(View.VISIBLE);
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	//  A method is used to get and set the Beers served at bar data
	private void setCocktailsOrLiquors(JSONObject jsonObjectBarDetails) {
		try {
			//  Now, here clear the list
			if(liquorList.size()>0) {
				liquorList.clear();
			}
			if(cocktailsList.size()>0) {
				cocktailsList.clear();
			}
			JSONArray jsonArrayResult = jsonObjectBarDetails.getJSONArray("result");
			for (int i = 0; i < jsonArrayResult.length(); i++) {
				JSONObject jsonObj = jsonArrayResult.getJSONObject(i);
				
				if(serve_as.equals(ConfigConstants.Constants.LIQUOR)) { 
					liquorList.add(new Liquor(Utils.getInstance().isTagExists(jsonObj, "liquor_id"), Utils.getInstance().isTagExists(jsonObj, "liquor_title"), Utils.getInstance().isTagExists(jsonObj, "type"), 
							Utils.getInstance().isTagExists(jsonObj, "proof"), Utils.getInstance().isTagExists(jsonObj, "producer"), Utils.getInstance().isTagExists(jsonObj, "liquor_image")));
				}
				else {
					cocktailsList.add(new Cocktail(Utils.getInstance().isTagExists(jsonObj, "cocktail_id"), Utils.getInstance().isTagExists(jsonObj, "cocktail_name"), Utils.getInstance().isTagExists(jsonObj, "type"), 
							Utils.getInstance().isTagExists(jsonObj, "served"), Utils.getInstance().isTagExists(jsonObj, "strength"), Utils.getInstance().isTagExists(jsonObj, "cocktail_image")));
				}
			}
			
			if(hiddenLinearLayoutCocktailsLiquors.getChildCount() != 0) {
				hiddenLinearLayoutCocktailsLiquors.removeAllViews();
			}
			if(cocktailsList.size()>0 || liquorList.size()>0) {
				hiddenLinearLayoutCocktailsLiquors.setVisibility(View.VISIBLE);
				relativeLayoutViewAllCocktails.setVisibility(View.VISIBLE);
				tvNoCocktailsLiquors.setVisibility(View.GONE);
				//  Now, set the Cocktails/Liquors served at bar's data
				if(serve_as.equals(ConfigConstants.Constants.LIQUOR)) { 
					setLiquorData();
				}
				else {
					setCocktailsData();
				}
			}
			else {
				relativeLayoutViewAllCocktails.setVisibility(View.GONE);
				tvNoCocktailsLiquors.setVisibility(View.VISIBLE);
			}
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//  A method is used to get and set the Bar Reviews data
	private void setBarReviews(JSONObject jsonObjectBarDetails) {
		try {
			//  Now, here clear the list
			if(barReviewsList.size()>0) {
				barReviewsList.clear();
			}
			JSONArray jsonArrayResult = jsonObjectBarDetails.getJSONArray("result");
			for (int i = 0; i < jsonArrayResult.length(); i++) {
				JSONObject jsonObj = jsonArrayResult.getJSONObject(i);
				barReviewsList.add(new ParseJsonObject().addBarReviewObject(jsonObj));
			}
			
			if(hiddenLinearLayoutReviews.getChildCount() != 0) {
				hiddenLinearLayoutReviews.removeAllViews();
			}
			if(barReviewsList.size()>0) {
				hiddenLinearLayoutReviews.setVisibility(View.VISIBLE);
				relativeLayoutViewAllRavesAndRants.setVisibility(View.VISIBLE);
				tvNoReviews.setVisibility(View.GONE);
				//  Now, set the Beers served at bar's data
				setReviewsData();
			}
			else {
				relativeLayoutViewAllRavesAndRants.setVisibility(View.GONE);
				tvNoReviews.setVisibility(View.VISIBLE);
			}
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * A method is used to create the custom view and add the layout into the
	 * Linear Layout for the Beers data
	 */
	@SuppressLint("InflateParams") 
	public void setBeersData() {
		try {
			LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			for (int i = 0; i < beersList.size(); i++) {
				final int index = i;
				View customView = inflater.inflate(R.layout.activity_bar_details_beer_served_list_item, null);
				
				ImageView ivBeerLogo = (ImageView) customView.findViewById(R.id.ivBeerLogo);
				TextView tvBeerName = (TextView) customView.findViewById(R.id.tvBeerName);
				TextView tvBeerType = (TextView) customView.findViewById(R.id.tvBeerType);
				TextView tvBeerProducer = (TextView) customView.findViewById(R.id.tvBeerProducer);
				TextView tvBeerCityProduced = (TextView) customView.findViewById(R.id.tvBeerCityProduced);
				
				ivBeerLogo.setTag(index);
				tvBeerName.setTag(index);
				tvBeerType.setTag(index);
				tvBeerProducer.setTag(index);
				tvBeerCityProduced.setTag(index);
				ivBeerLogo.setImageResource(R.drawable.no_image_beer);
				
				//  Now, set the data
				//  Set Logo
				setLogo(ConfigConstants.ImageUrls.beer_140, beersList.get(index).getBeer_image(), ivBeerLogo, R.drawable.no_image_beer);
				
				//  Set Beer Name
				tvBeerName.setText(beersList.get(index).getName());
				
				//  Set Beer Type
				tvBeerType.setText(beersList.get(index).getType());
				
				//  Set Producer
				tvBeerProducer.setText(beersList.get(index).getProducer());
				
				//  Set City Produced
				tvBeerCityProduced.setText(beersList.get(index).getCity_produced());
				
				//  Now, add the custom view to the layout
				hiddenLinearLayoutBeers.addView(customView,index);
				
				//  Click event of whole row
				customView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						try {
							redirectBeerDetailsActivity(HalfMugBarDetailsActivity.this, beersList.get(index).getId());
						} 
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * A method is used to create the custom view and add the layout into the
	 * Linear Layout for the Liquor data
	 */
	@SuppressLint("InflateParams") 
	public void setLiquorData() {
		try {
			LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			for (int i = 0; i < liquorList.size(); i++) {
				final int index = i;
				View customView = inflater.inflate(R.layout.activity_bar_details_beer_served_list_item, null);
				
				ImageView ivBeerLogo = (ImageView) customView.findViewById(R.id.ivBeerLogo);
				TextView tvBeerName = (TextView) customView.findViewById(R.id.tvBeerName);
				TextView tvBeerType = (TextView) customView.findViewById(R.id.tvBeerType);
				TextView tvBeerProducer = (TextView) customView.findViewById(R.id.tvBeerProducer);
				TextView tvBeerCityProduced = (TextView) customView.findViewById(R.id.tvBeerCityProduced);
				
				ivBeerLogo.setTag(index);
				tvBeerName.setTag(index);
				tvBeerType.setTag(index);
				tvBeerProducer.setTag(index);
				tvBeerCityProduced.setTag(index);
				ivBeerLogo.setImageResource(R.drawable.no_image_liquor);
				
				//  Now, set the data
				//  Set Logo
				setLogo(ConfigConstants.ImageUrls.liquor_140, liquorList.get(index).getLiquor_image(), ivBeerLogo, R.drawable.no_image_liquor);
				
				//  Set Liquor Name
				tvBeerName.setText(liquorList.get(index).getTitle());
				
				//  Set Liquor Type
				tvBeerType.setText(liquorList.get(index).getType());
				
				//  Set Liquor Producer
				tvBeerProducer.setText(liquorList.get(index).getProducer());
				
				//  Set Liquor Proof
				tvBeerCityProduced.setText(liquorList.get(index).getProof());
				
				//  Now, add the custom view to the layout
				hiddenLinearLayoutCocktailsLiquors.addView(customView,index);
				
				//  Click event of whole row
				customView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						try {
							redirectLiquorDetailsActivity(HalfMugBarDetailsActivity.this, liquorList.get(index).getId());
						} 
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * A method is used to create the custom view and add the layout into the
	 * Linear Layout for the Cocktails data
	 */
	@SuppressLint("InflateParams") 
	public void setCocktailsData() {
		try {
			LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			for (int i = 0; i < cocktailsList.size(); i++) {
				final int index = i;
				View customView = inflater.inflate(R.layout.activity_bar_details_beer_served_list_item, null);
				
				ImageView ivBeerLogo = (ImageView) customView.findViewById(R.id.ivBeerLogo);
				TextView tvBeerName = (TextView) customView.findViewById(R.id.tvBeerName);
				TextView tvBeerType = (TextView) customView.findViewById(R.id.tvBeerType);
				TextView tvBeerProducer = (TextView) customView.findViewById(R.id.tvBeerProducer);
				TextView tvBeerCityProduced = (TextView) customView.findViewById(R.id.tvBeerCityProduced);
				
				ivBeerLogo.setTag(index);
				tvBeerName.setTag(index);
				tvBeerType.setTag(index);
				tvBeerProducer.setTag(index);
				tvBeerCityProduced.setTag(index);
				ivBeerLogo.setImageResource(R.drawable.no_image_cocktail);
				
				//  Now, set the data
				//  Set Logo
				setLogo(ConfigConstants.ImageUrls.cocktail_140, cocktailsList.get(index).getCocktail_image(), ivBeerLogo, R.drawable.no_image_cocktail);
				
				//  Set Cocktail Name
				tvBeerName.setText(cocktailsList.get(index).getName());
				
				//  Set Cocktail Type
				tvBeerType.setText(cocktailsList.get(index).getType());
				
				//  Set Cocktail Served
				tvBeerProducer.setText(cocktailsList.get(index).getServed());
				
				//  Set Cocktail Strength
				tvBeerCityProduced.setText(cocktailsList.get(index).getStrength());
				
				//  Now, add the custom view to the layout
				hiddenLinearLayoutCocktailsLiquors.addView(customView,index);
				
				//  Click event of whole row
				customView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						try {
							redirectCocktailDetailsActivity(HalfMugBarDetailsActivity.this, cocktailsList.get(index).getId());
						} 
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * A method is used to create the custom view and add the layout into the
	 * Linear Layout for the Reviews data
	 */
	@SuppressLint("InflateParams") 
	public void setReviewsData() {
		try {
			LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			for (int i = 0; i < barReviewsList.size(); i++) {
				final int index = i;
				View customView = inflater.inflate(R.layout.activity_bar_details_reviews_list_item, null);
				
				TextView tvRaveTitle = (TextView) customView.findViewById(R.id.tvRaveTitle);
				TextView tvRaveFormattedDate = (TextView) customView.findViewById(R.id.tvRaveFormattedDate);
				TextView tvRaveDesc = (TextView) customView.findViewById(R.id.tvRaveDesc);
				TextView tvRaveDateTime = (TextView) customView.findViewById(R.id.tvRaveDateTime);
				TextView tvRaveUserName = (TextView) customView.findViewById(R.id.tvRaveUserName);
				ImageView[] imageStarReviewArray = null;
				imageStarReviewArray = new ImageView[5];
				imageStarReviewArray[0] = (ImageView) customView.findViewById(R.id.ivStarRave1);
				imageStarReviewArray[1] = (ImageView) customView.findViewById(R.id.ivStarRave2);
				imageStarReviewArray[2] = (ImageView) customView.findViewById(R.id.ivStarRave3);
				imageStarReviewArray[3] = (ImageView) customView.findViewById(R.id.ivStarRave4);
				imageStarReviewArray[4] = (ImageView) customView.findViewById(R.id.ivStarRave5);
			
				tvRaveTitle.setTag(index);
				tvRaveFormattedDate.setTag(index);
				tvRaveDesc.setTag(index);
				tvRaveDateTime.setTag(index);
				tvRaveUserName.setTag(index);
				for(int j=0; j<5; j++) {
					imageStarReviewArray[i].setTag(index);
				}
				
				//  Now, set the data
				
				//  Set Comment Title
				tvRaveTitle.setText(barReviewsList.get(index).getComment_title());
				
				//  Set Comment
				//tvRaveDesc.setText(barReviewsList.get(index).getComment());
				tvRaveDesc.setText(Utils.getInstance().setHTMLText(barReviewsList.get(index).getComment()));
				
				//  Set Date Added
				tvRaveDateTime.setText(barReviewsList.get(index).getDate_added());
				
				//  Set Name
				tvRaveUserName.setText(barReviewsList.get(index).getFirst_name()+" "+barReviewsList.get(index).getLast_name());
				
				//  Now, set the stars
				setStars(Integer.parseInt(barReviewsList.get(index).getBar_rating()), imageStarReviewArray);
				
				//  Now, add the custom view to the layout
				hiddenLinearLayoutReviews.addView(customView,index);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  Sharing / Redirect Page Links Click event
	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.ivShareFacebook:
				redirectPage(facebook_link);
				break;
			case R.id.ivShareTwitter:
				redirectPage(twitter_link);
				break;
			case R.id.ivShareLinkedIn:
				redirectPage(linkedin_link);
				break;
			case R.id.ivShareGooglePlus:
				redirectPage(google_plus_link);
				break;
			case R.id.ivShareDribble:
				redirectPage(dribble_link);
				break;
			case R.id.ivSharePinterest:
				redirectPage(pinterest_link);
				break;

			default:
				break;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask for the Add To Favorite Functionality
	public class AsynTaskAddToFavorite extends AsyncTask<Void, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(HalfMugBarDetailsActivity.this);
    	private String responseString = "";
    	private String status = "";
    	
    	@Override
		protected void onPreExecute() {
			try {
				super.onPreExecute();
				this.pd.setMessage(ConfigConstants.Messages.loadingMessage);
				pd.setCanceledOnTouchOutside(false); 
				pd.setCancelable(false);
				this.pd.show();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}	

		@Override
		protected Void doInBackground(Void... params) {
	        try {
	        	ServiceHandler sh = new ServiceHandler();
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(HalfMugBarDetailsActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(HalfMugBarDetailsActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(HalfMugBarDetailsActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("bar_id", bar_id));
		        
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(ConfigConstants.Urls.bar_favorite, ServiceHandler.POST, nameValuePairs);
	        	//Log.i("responseString",responseString);
	        }
	        catch(Exception e) {
	        	e.printStackTrace();
	        }
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			try {
				super.onPostExecute(result);
				JSONObject jsonObj = null;
				if(responseString!=null) {
					jsonObj = new JSONObject(responseString);
					status = jsonObj.getString("status");
					//Log.i("status",status);
				}
				
				if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)){
					
					//  Add To Favorite button
					setTextButtonAddToFavorite(buttonAddToFavorite, Utils.getInstance().isTagExists(jsonObj, "message"), ConfigConstants.Constants.BAR);
					/*{"status":"success","message":"0"}*/
				}
				//  Means fail
				else if(status.equals(ConfigConstants.Messages.RESPONSE_FAIL)){
				}
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}
			
			if(this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}		
    }
	
	//  AsyncTask for the Like This Bar Functionality
	public class AsynTaskLikeThisBar extends AsyncTask<Void, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(HalfMugBarDetailsActivity.this);
    	private String responseString = "";
    	private String status = "";
    	
    	@Override
		protected void onPreExecute() {
			
			try {
				super.onPreExecute();
				
				this.pd.setMessage(ConfigConstants.Messages.loadingMessage);
				pd.setCanceledOnTouchOutside(false); 
				pd.setCancelable(false);
				this.pd.show();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}	

		@Override
		protected Void doInBackground(Void... params) {
	        try {
	        	ServiceHandler sh = new ServiceHandler();
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(HalfMugBarDetailsActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(HalfMugBarDetailsActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(HalfMugBarDetailsActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("bar_id", bar_id));
		        
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(ConfigConstants.Urls.bar_likes, ServiceHandler.POST, nameValuePairs);
	        	//Log.i("responseString",responseString);
	        }
	        catch(Exception e) {
	        	e.printStackTrace();
	        }
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			try {
				super.onPostExecute(result);
				JSONObject jsonObj = null;
				if(responseString!=null) {
					jsonObj = new JSONObject(responseString);
					status = jsonObj.getString("status");
					//Log.i("status",status);
				}
				
				if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)){
					
					//  Add To Favorite button
					setTextButtonLike(buttonLikeThisBar, Utils.getInstance().isTagExists(jsonObj, "message"), ConfigConstants.Constants.BAR);
					/*{"status":"success","message":"0"}*/
				}
				//  Means fail
				else if(status.equals(ConfigConstants.Messages.RESPONSE_FAIL)){
				}
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}
			if(this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}		
    }
	
	//  To hide the soft key board on the click of Layout
	private void hideSoftKeyboard(){
		try {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etEmailReportBar.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etDescReportThisBar.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etReviewTitle.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etReviewDescription.getWindowToken(), 0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* An AnyncTask for the report a bar functionality. */
	public class AsynTaskReportABar extends AsyncTask<String, Void, String> {
		private ProgressDialog pd = new ProgressDialog(HalfMugBarDetailsActivity.this);
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
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(HalfMugBarDetailsActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(HalfMugBarDetailsActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(HalfMugBarDetailsActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("bar_id", bar_id));
		        nameValuePairs.add(new BasicNameValuePair("email", etEmailReportBar.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("desc", etDescReportThisBar.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("report_type", selectedRadioType));
	        	// Making a request to URL and getting response
		        String responseString = sh.makeServiceCall(ConfigConstants.Urls.add_report_bar, ServiceHandler.POST, nameValuePairs);
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
						//Utils.getInstance().displayToast(HalfMugBarDetailsActivity.this, "Your bar report sent successfully.");
						Utils.getInstance().displayToast(HalfMugBarDetailsActivity.this, "Your report has been sent successfully to bar owner.");
					}
					else {
						Utils.getInstance().displayToast(HalfMugBarDetailsActivity.this, "Your bar report is not sent. Please try again.");
					}
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}
			objAsynTaskReportABar = null;
			if (this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}
	}
	
	/* An AnyncTask for the post review functionality. */
	public class AsynTaskPostReview extends AsyncTask<String, Void, String> {
		private ProgressDialog pd = new ProgressDialog(HalfMugBarDetailsActivity.this);
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
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(HalfMugBarDetailsActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(HalfMugBarDetailsActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(HalfMugBarDetailsActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("bar_id", bar_id));
		        nameValuePairs.add(new BasicNameValuePair("comment_title", etReviewTitle.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("comment", etReviewDescription.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("rating", ""+Utils.getInstance().convertFloatToInteger(""+selectedRating)));
	        	// Making a request to URL and getting response
		        String responseString = sh.makeServiceCall(ConfigConstants.Urls.add_bar_comment, ServiceHandler.POST, nameValuePairs);
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
						Utils.getInstance().displayToast(HalfMugBarDetailsActivity.this, "Your review is added successfully.");
						/* Now, here clear all the required fields */
						etReviewTitle.setText("");
						etReviewDescription.setText("");
						ratingBarReview.setRating(0);
						selectedRating = 0;
						etReviewDescription.clearFocus();
						etReviewTitle.clearFocus();
						
						/*
						 * Now, here call the web service again to get the bar
						 * details
						 */
						//  Now, first of all get the bar details
						if(Utils.getInstance() .isNetworkAvailable(HalfMugBarDetailsActivity.this)) {
							new AsynTaskGetBarDetails().execute();
						}
						else{
							Utils.getInstance().showToastNoInternetAvailable(HalfMugBarDetailsActivity.this);
						}
					}
					else {
						Utils.getInstance().displayToast(HalfMugBarDetailsActivity.this, "Your review is not added. Please try again.");
					}
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}
			objAsynTaskPostReview = null;
			if (this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}
	}
}
