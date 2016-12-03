package com.spaculus.americanbars;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.android.youtube.player.YouTubeThumbnailView.OnInitializedListener;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LiquorDetailsActivity extends BaseActivity implements OnInitializedListener {

	//  To get the selected liquor id
	private String liquor_id = "";
	
	/* Header Views */
	private ImageView ivLiquorLogo;
	private TextView tvLiquorName, tvType, tvABV, tvProducer;
	/* Header Views */
	
	/* Description */
	private TextView tvDescription;
	private RelativeLayout relativeLayoutShowMoreDesc;
	/* Description */

	/* Liquor Comments */
	private LinearLayout hiddenLinearLayoutReviews;
	private ArrayList<Review> liquorCommentsList = null;
	/* Liquor Comments */
	
	/* Picture/Video */
	private String description = "", videoURL = "", image_name = "", upload_type = "";
	private YouTubeThumbnailView ivVideoThumbnail;
	private String videoID = "";
	private RelativeLayout relativeLayoutVideo;
	private ImageView ivPicture;
	/* Picture/Video */
	
	/* Sharing functionality */
	private ImageView ivShare;
	private Spinner spinnerShare;
	//  For the Share Options
	//private ArrayList<String> searchOptionsList = null;
 	private String selectedSearchOption = "";
 	//  To prevent the spinner for a default call
 	private boolean isShareImageClickedOnce = false;
	/* Sharing functionality */
 	
 	private Button buttonAddToFavorite, buttonLikeThisLiquor;
 	
 	/* Write A Comment functionality */
 	private EditText etCommentTitle, etCommentDescription;
 	private Button buttonPostComment;
 	private RelativeLayout relativeLayoutViewAllRavesAndRants;
 	private AsynTaskPostComment objAsynTaskPostComment = null;
 	/* Write A Comment functionality */
 	
	/* Like Liquor Comment functionality */
 	private AsynTaskLikeLiquorComment objAsynTaskLikeLiquorComment = null;
 	
 	private String liquor_slug = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_liquor_details);
			
			// Create ActionBar
			createActionBar("Liquor Details", R.layout.custom_actionbar, LiquorDetailsActivity.this, true);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);
			
			// Mapping of all the views
			mappedAllViews();
			
			//  To get the bar id
			Bundle b = getIntent().getExtras();
			liquor_id = b.getString(ConfigConstants.Keys.KEY_LIQUOR_ID);
			
			//  For the Spinner
			bindSpinnerData(spinnerShare);
			
			//  Now, first of all get the liquor details
			if(Utils.getInstance() .isNetworkAvailable(LiquorDetailsActivity.this)) {
				new AsynTaskGetLiquorDetails().execute();
			}
			else{
				Utils.getInstance().showToastNoInternetAvailable(LiquorDetailsActivity.this);
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
			
			//  To play a Video
			relativeLayoutVideo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						if(!videoURL.isEmpty()) {
							redirectVideoPlayActivity(videoID);
						}
						else {
							//Toast.makeText(LiquorDetailsActivity.this, ConfigConstants.getInstance().noVideoAvailable, Toast.LENGTH_SHORT).show();
							Utils.showToastLong(LiquorDetailsActivity.this, ConfigConstants.Messages.noVideoAvailable);
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  To zoom an image
			ivPicture.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						//  To zoom an activity
						redirectZoomImageViewActivity(LiquorDetailsActivity.this, ConfigConstants.ImageUrls.liquor_orig+image_name);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  Share Click Event
			ivShare.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						isShareImageClickedOnce = true;
						//Toast.makeText(FullMugBarDetailsActivity.this, "Share Image is clicked.", Toast.LENGTH_SHORT).show();
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
							shareTextLink(packageName, ConfigConstants.SharingURLs.shareLiquorDetailsURL+liquor_slug, constantOption);
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
			
			//  Add To Favorite button click event
			buttonAddToFavorite.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						ConfigConstants.isAddToFavoriteORLikeLiquorButtonClicked = true;
						//  Now, first of all check whether user is already logged in or not
						if(SessionManager.getInstance(LiquorDetailsActivity.this).isLoggedIn()) {
							//  Called the AsyncTask for Add To Favorite functionality
							if(Utils.getInstance().isNetworkAvailable(LiquorDetailsActivity.this)) {		
								new AsynTaskAddToFavorite().execute();
							}
							else{
								Utils.getInstance().showToastNoInternetAvailable(LiquorDetailsActivity.this);
							}
						}
						else {
							//  Means user is not logged in
							redirectFragmentSignIn(LiquorDetailsActivity.this);
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  Like This Bar button click event
			buttonLikeThisLiquor.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						ConfigConstants.isAddToFavoriteORLikeLiquorButtonClicked = true;
						//  Now, first of all check whether user is already logged in or not
						if(SessionManager.getInstance(LiquorDetailsActivity.this).isLoggedIn()) {
							//  Called the AsyncTask for Like This Liquor functionality
							if(Utils.getInstance().isNetworkAvailable(LiquorDetailsActivity.this)) {		
								new AsynTaskLikeThisLiquor().execute();
							}
							else{
								Utils.getInstance().showToastNoInternetAvailable(LiquorDetailsActivity.this);
							}
						}
						else {
							//  Means user is not logged in
							redirectFragmentSignIn(LiquorDetailsActivity.this);
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			buttonPostComment.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						//  First, hide the soft key board.
						hideSoftKeyboard();
						//  Now, first of all check all the validations
						String validationResponse  = Validation.getInstance().postAComment(etCommentTitle.getText().toString().trim(), etCommentDescription.getText().toString().trim());
						
						//  Means all the fields are properly entered
						if(validationResponse.isEmpty()) {
							if(Utils.getInstance() .isNetworkAvailable(LiquorDetailsActivity.this)) {
								if (objAsynTaskPostComment == null) {
									if (Utils.getInstance().isNetworkAvailable(LiquorDetailsActivity.this)) {
										objAsynTaskPostComment = new AsynTaskPostComment();
										objAsynTaskPostComment.execute();
									} else {
										Utils.getInstance().showToastNoInternetAvailable(LiquorDetailsActivity.this);
									}
								}
							}
							else{
								Utils.getInstance().showToastNoInternetAvailable(LiquorDetailsActivity.this);
							}
						}
						else {
							Utils.toastLong(LiquorDetailsActivity.this, validationResponse);
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  View All - Comments click event
			relativeLayoutViewAllRavesAndRants.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						redirectLiquorCommentsListActivity(LiquorDetailsActivity.this, liquor_id);
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
	}
	
	@Override
	protected void onResume() {
		try {
			super.onResume();
			//  Need to load the data again if the Add To Favorite button or Like This Bar button is clicked
			if(ConfigConstants.isAddToFavoriteORLikeLiquorButtonClicked || ConfigConstants.isLiquorCommentAdded) {
				ConfigConstants.isAddToFavoriteORLikeLiquorButtonClicked = false;
				ConfigConstants.isLiquorCommentAdded = false;
			
				//  Now, check whether user has done login or not
				if(SessionManager.getInstance(LiquorDetailsActivity.this).isLoggedIn()) {
					//  Now, first of all get the bar details
					if(Utils.getInstance() .isNetworkAvailable(LiquorDetailsActivity.this)) {
						new AsynTaskGetLiquorDetails().execute();
					}
					else{
						Utils.getInstance().showToastNoInternetAvailable(LiquorDetailsActivity.this);
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
			// TODO Auto-generated method stub
			/* Header Views */
			ivLiquorLogo = (ImageView)findViewById(R.id.ivLiquorLogo);
			tvLiquorName = (TextView)findViewById(R.id.tvLiquorName);
			tvType = (TextView)findViewById(R.id.tvType);
			tvABV = (TextView)findViewById(R.id.tvABV);
			tvProducer = (TextView)findViewById(R.id.tvProducer);
			/* Header Views */

			/* Description */
			tvDescription = (TextView)findViewById(R.id.tvDescription);
			relativeLayoutShowMoreDesc = (RelativeLayout)findViewById(R.id.relativeLayoutShowMoreDesc);
			/* Description */

			/* Raves And Rants */
			liquorCommentsList = new ArrayList<Review>();
			hiddenLinearLayoutReviews = (LinearLayout)findViewById(R.id.hiddenLinearLayoutReviews);
			/* Raves And Rants */
			
			/* Video */
			ivVideoThumbnail = (YouTubeThumbnailView) findViewById(R.id.ivVideoThumbnail);
			relativeLayoutVideo = (RelativeLayout)findViewById(R.id.relativeLayoutVideo);
			ivPicture = (ImageView)findViewById(R.id.ivPicture);
			/* Video */
			
			/* Sharing functionality */
			ivShare = (ImageView)findViewById(R.id.ivShare);
			spinnerShare = (Spinner)findViewById(R.id.spinnerShare);
			/* Sharing functionality */
			
			buttonAddToFavorite = (Button)findViewById(R.id.btnAddToFavorite);
			buttonLikeThisLiquor = (Button)findViewById(R.id.btnLikeThisLiquor);
			
			/* Write A Comment functionality */
			etCommentTitle = (EditText)findViewById(R.id.etCommentTitle);
			etCommentDescription = (EditText)findViewById(R.id.etCommentDescription);
			buttonPostComment = (Button)findViewById(R.id.buttonPostComment);
			relativeLayoutViewAllRavesAndRants = (RelativeLayout)findViewById(R.id.relativeLayoutViewAllRavesAndRants);
			/* Write A Comment functionality */
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to get the Liquor Details
	public class AsynTaskGetLiquorDetails extends AsyncTask<String, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(LiquorDetailsActivity.this);
    	private String responseString = "";
    	private String status = "";
    	
    	@Override
		protected void onPreExecute() {
			try {
				// TODO Auto-generated method stub
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
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
	        try {
	        	ServiceHandler sh = new ServiceHandler();
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        	nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(LiquorDetailsActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(LiquorDetailsActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(LiquorDetailsActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("limit", "3"));
		        nameValuePairs.add(new BasicNameValuePair("offset", "0"));
		        nameValuePairs.add(new BasicNameValuePair("liquor_id", liquor_id));
		        			
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(ConfigConstants.Urls.liquor_details, ServiceHandler.POST, nameValuePairs);
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
				
					/* Header Views / Description */
					if(jsonObjParent.has("liquor_detail")) {
						setHeaderViews( jsonObjParent.getJSONObject("liquor_detail"));
					}
					/* Header Views / Description */
				
					/* Liquor Comments */
					if(jsonObjParent.has("liquorcomments")) {
						setLiquorComments( jsonObjParent.getJSONObject("liquorcomments"));	
					}
					/* Liquor Comments */
				}
			} 
			catch (JSONException e) {
				// TODO Auto-generated catch block
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
			
			//  Get all the required data
			description = Utils.getInstance().isTagExists(jsonObj, "liquor_description");
			
			/* Header Views */
			//  Title
			tvLiquorName.setText(Utils.getInstance().isTagExists(jsonObj, "liquor_title"));
			//  Type
			tvType.setText(Utils.getInstance().isTagExists(jsonObj, "type"));
			//  ABV
			tvABV.setText(Utils.getInstance().isTagExists(jsonObj, "proof"));
			//  Producer
			tvProducer.setText(Utils.getInstance().isTagExists(jsonObj, "producer"));
			//  Liquor Logo
			setLogo(ConfigConstants.ImageUrls.liquor_240, Utils.getInstance().isTagExists(jsonObj, "liquor_image"), ivLiquorLogo, R.drawable.no_image_liquor);
			/* Header Views */
		
			/* Description */
			tvDescription.setText(Utils.getInstance().setHTMLText(description));
			if(tvDescription.length()==0) {
				relativeLayoutShowMoreDesc.setVisibility(View.GONE);
			}
			else {
				relativeLayoutShowMoreDesc.setVisibility(View.VISIBLE);
			}
			/* Description */
		
			/* Picture/Video */
			upload_type = Utils.getInstance().isTagExists(jsonObj, "upload_type");
			videoURL = Utils.getInstance().isTagExists(jsonObj, "video_link");
			image_name = Utils.getInstance().isTagExists(jsonObj, "image_default");
			liquor_slug = Utils.getInstance().isTagExists(jsonObj, "liquor_slug");
			
			//  Now, check whether picture or video is any available or not
			if(!upload_type.isEmpty()) {
				//  Now, check whether image or video
				if(upload_type.equals("image")) {
					//  Means image
					ivPicture.setVisibility(View.VISIBLE);
					relativeLayoutVideo.setVisibility(View.GONE);
					//  Set Picture
					setLogo(ConfigConstants.ImageUrls.liquor_600by400, image_name, ivPicture, R.drawable.gallery_big_place_holder);
				}
				else {
					//  Means Video
					ivPicture.setVisibility(View.GONE);
					relativeLayoutVideo.setVisibility(View.VISIBLE);
					if(!videoURL.isEmpty()) {
						videoID = Utils.getInstance().getYoutubeVideoId(videoURL);
						//Initialize the thumbnail
						ivVideoThumbnail.initialize(getResources().getString(R.string.DEVELOPER_KEY), LiquorDetailsActivity.this);
					} else{
						ivVideoThumbnail.setImageResource(R.drawable.video_place_holder_01);
					}
				}
			}
			else {
				ivPicture.setVisibility(View.VISIBLE);
				ivPicture.setImageResource(R.drawable.media_place_holder);
				relativeLayoutVideo.setVisibility(View.GONE);
			}
			/* Picture/Video */
			
			/* For the Add To Favorite, Like This Bar buttons click event */
			/*
			 * We need to consider the following values if the user is logged in
			 * o.w. not.
			 */
			if(SessionManager.getInstance(LiquorDetailsActivity.this).isLoggedIn()) {
				//  Add To Favorite button
				setTextButtonAddToFavorite(buttonAddToFavorite, Utils.getInstance().isTagExists(jsonObj, "fav_liquor"), ConfigConstants.Constants.LIQUOR);
				
				//  Like This Bar button
				setTextButtonLike(buttonLikeThisLiquor, Utils.getInstance().isTagExists(jsonObj, "like_liquor"), ConfigConstants.Constants.LIQUOR);
			}
			/* For the Add To Favorite, Like This Bar buttons click event */
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	//  A method is used to get and set the Liquor Reviews data
	private void setLiquorComments(JSONObject jsonObjectBarDetails) {
		try {
			//  Now, here clear the list
			if(liquorCommentsList.size()>0) {
				liquorCommentsList.clear();
			}
						
			JSONArray jsonArrayResult = jsonObjectBarDetails.getJSONArray("result");
			for (int i = 0; i < jsonArrayResult.length(); i++) {
				JSONObject jsonObj = jsonArrayResult.getJSONObject(i);
				liquorCommentsList.add(new ParseJsonObject().addLiquorCommentObject(jsonObj));
			}
			
			if(hiddenLinearLayoutReviews.getChildCount() != 0) {
				hiddenLinearLayoutReviews.removeAllViews();
			}
			if(liquorCommentsList.size()>0) {
				hiddenLinearLayoutReviews.setVisibility(View.VISIBLE);
				relativeLayoutViewAllRavesAndRants.setVisibility(View.VISIBLE);
				//  Now, set the Liquor Comments Data
				setReviewsData();
			}
			else {
				relativeLayoutViewAllRavesAndRants.setVisibility(View.GONE);
			}
		} 
		catch (JSONException e) {
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
			
			for (int i = 0; i < liquorCommentsList.size(); i++) {
				final int index = i;
				View customView = inflater.inflate(R.layout.activity_comments_list_item, null);
				
				ImageView ivProfilePicture = (ImageView) customView.findViewById(R.id.ivProfilePicture);
				TextView tvName = (TextView) customView.findViewById(R.id.tvName);
				TextView tvCommentTitle = (TextView) customView.findViewById(R.id.tvCommentTitle);
				TextView tvCommentDescription = (TextView) customView.findViewById(R.id.tvCommentDescription);
				TextView tvCommentDateTime = (TextView) customView.findViewById(R.id.tvCommentDateTime);
				ImageView ivLikeComment = (ImageView) customView.findViewById(R.id.ivLikeComment);
				TextView tvLikeCommentCounter = (TextView) customView.findViewById(R.id.tvLikeCommentCounter);
				TextView tvLikeText = (TextView) customView.findViewById(R.id.tvLikeText);
				LinearLayout linearLayoutLikeComment = (LinearLayout) customView.findViewById(R.id.linearLayoutLikeComment);
				TextView tvReply = (TextView) customView.findViewById(R.id.tvReply);
				
				ivProfilePicture.setTag(index);
				tvName.setTag(index);
				tvCommentTitle.setTag(index);
				tvCommentDescription.setTag(index);
				tvCommentDateTime.setTag(index);
				ivLikeComment.setTag(index);
				tvLikeCommentCounter.setTag(index);
				tvLikeText.setTag(index);
				linearLayoutLikeComment.setTag(index);
				tvReply.setTag(index);
				
				/* Like icon click event */
				linearLayoutLikeComment.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(Utils.getInstance() .isNetworkAvailable(LiquorDetailsActivity.this)) {
							if (objAsynTaskLikeLiquorComment == null) {
								if (Utils.getInstance().isNetworkAvailable(LiquorDetailsActivity.this)) {
									objAsynTaskLikeLiquorComment = new AsynTaskLikeLiquorComment();
									objAsynTaskLikeLiquorComment.execute(String.valueOf(index));
								} else {
									Utils.getInstance().showToastNoInternetAvailable(LiquorDetailsActivity.this);
								}
							}
						}
						else{
							Utils.getInstance().showToastNoInternetAvailable(LiquorDetailsActivity.this);
						}
					}
				});
				
				/* Reply click event */
				tvReply.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						redirectLiquorCommentRepliesActivity(LiquorDetailsActivity.this, liquor_id, liquorCommentsList.get(index).getComment_id());
					}
				});
				
				//  Now, set the data
				//  Set Liquor Comment's Logo
				setLogo(ConfigConstants.ImageUrls.user_140, liquorCommentsList.get(index).getProfile_image(), ivProfilePicture, R.drawable.no_profile_picture);
				
				//  Set Name
				showHideView(tvName, Utils.getInstance().setCapitalLetter(liquorCommentsList.get(index).getFirst_name()+" "+liquorCommentsList.get(index).getLast_name()));
				
				//  Set Comment Title
				showHideView(tvCommentTitle, liquorCommentsList.get(index).getComment_title());
				
				//  Set Comment
				showHideView(tvCommentDescription, Utils.getInstance().setHTMLText(liquorCommentsList.get(index).getComment()).toString());
				
				//  Set Date-Time
				showHideView(tvCommentDateTime, liquorCommentsList.get(index).getDate_added());
				
				/* For the Like functionality */
				/* For the thumb image */
				if(liquorCommentsList.get(index).getIs_like().equals(ConfigConstants.Constants.CONSTANT_ONE)) {
					ivLikeComment.setImageResource(R.drawable.thumb_down);
				}
				else {
					ivLikeComment.setImageResource(R.drawable.thumb_up);
				}
				/* For the like counter */
				if(liquorCommentsList.get(index).getTotal_like().isEmpty()) {
					tvLikeCommentCounter.setText(ConfigConstants.Constants.CONSTANT_ZERO);	
				}
				else {
					tvLikeCommentCounter.setText(liquorCommentsList.get(index).getTotal_like());
				}
				/* For the like text */
				if(liquorCommentsList.get(index).getTotal_like().isEmpty()) {
					tvLikeText.setText(ConfigConstants.Constants.CONSTANT_LIKE);
				}
				else {
					if(liquorCommentsList.get(index).getTotal_like().equals(ConfigConstants.Constants.CONSTANT_ZERO) || liquorCommentsList.get(index).getTotal_like().equals(ConfigConstants.Constants.CONSTANT_ONE)) {
						tvLikeText.setText(ConfigConstants.Constants.CONSTANT_LIKE);
					}
					else {
						tvLikeText.setText(ConfigConstants.Constants.CONSTANT_LIKES);
					}
				}
				
				//  Now, add the custom view to the layout
				hiddenLinearLayoutReviews.addView(customView,index);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
    public void onInitializationSuccess(YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
        try {
			if(!videoURL.isEmpty()) {
				loader.setVideo(videoID);	
			}
		} 
        catch (Exception e) {
			e.printStackTrace();
		}
    }

    @Override
    public void onInitializationFailure(YouTubeThumbnailView thumbnailView, YouTubeInitializationResult errorReason) {
        try {
			final String errorMessage = errorReason.toString();
			if(!videoURL.isEmpty()) {
				Toast.makeText(LiquorDetailsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
			}
		} 
        catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	//  AsyncTask for the Add To Favorite Functionality
	public class AsynTaskAddToFavorite extends AsyncTask<Void, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(LiquorDetailsActivity.this);
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
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(LiquorDetailsActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(LiquorDetailsActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(LiquorDetailsActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("liquor_id", liquor_id));
		        
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(ConfigConstants.Urls.liquor_favorite, ServiceHandler.POST, nameValuePairs);
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
					setTextButtonAddToFavorite(buttonAddToFavorite, Utils.getInstance().isTagExists(jsonObj, "message"), ConfigConstants.Constants.LIQUOR);
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
	
	//  AsyncTask for the Like This Liquor Functionality
	public class AsynTaskLikeThisLiquor extends AsyncTask<Void, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(LiquorDetailsActivity.this);
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
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(LiquorDetailsActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(LiquorDetailsActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(LiquorDetailsActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("liquor_id", liquor_id));
		        
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(ConfigConstants.Urls.liquor_likes, ServiceHandler.POST, nameValuePairs);
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
					setTextButtonLike(buttonLikeThisLiquor, Utils.getInstance().isTagExists(jsonObj, "message"), ConfigConstants.Constants.LIQUOR);
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
			imm.hideSoftInputFromWindow(etCommentTitle.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etCommentDescription.getWindowToken(), 0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* An AnyncTask for the post comment functionality. */
	public class AsynTaskPostComment extends AsyncTask<String, Void, String> {
		private ProgressDialog pd = new ProgressDialog(LiquorDetailsActivity.this);
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
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(LiquorDetailsActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(LiquorDetailsActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(LiquorDetailsActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("liquor_id", liquor_id));
		        nameValuePairs.add(new BasicNameValuePair("comment_title", etCommentTitle.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("comment", etCommentDescription.getText().toString().trim()));
	        	// Making a request to URL and getting response
		        String responseString = sh.makeServiceCall(ConfigConstants.Urls.add_liquor_comment, ServiceHandler.POST, nameValuePairs);
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
						Utils.getInstance().displayToast(LiquorDetailsActivity.this, "Your comment is added successfully.");
						/* Now, here clear all the required fields */
						etCommentTitle.setText("");
						etCommentDescription.setText("");
						etCommentTitle.clearFocus();
						etCommentDescription.clearFocus();
						
						//  Now, first of all get the liquor details
						if(Utils.getInstance() .isNetworkAvailable(LiquorDetailsActivity.this)) {
							new AsynTaskGetLiquorDetails().execute();
						}
						else{
							Utils.getInstance().showToastNoInternetAvailable(LiquorDetailsActivity.this);
						}
					}
					else {
						Utils.getInstance().displayToast(LiquorDetailsActivity.this, "Your comment is not added. Please try again.");
					}
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}
			objAsynTaskPostComment = null;
			if (this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}
	}
	
	/* An AnyncTask for a like liquor comment functionality. */
	public class AsynTaskLikeLiquorComment extends AsyncTask<String, Void, String> {
		private ProgressDialog pd = new ProgressDialog(LiquorDetailsActivity.this);
		int position = -1;
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
				position = Integer.parseInt(params[0]);
				String is_like = "";
				if(liquorCommentsList.get(position).getIs_like().isEmpty()) {
					is_like = "0";
				}	
				else {
					is_like = liquorCommentsList.get(position).getIs_like();
				}
				ServiceHandler sh = new ServiceHandler();
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(LiquorDetailsActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(LiquorDetailsActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(LiquorDetailsActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("liquor_id", liquor_id));
		        nameValuePairs.add(new BasicNameValuePair("is_like", is_like));
		        nameValuePairs.add(new BasicNameValuePair("liquor_comment_id", liquorCommentsList.get(position).getComment_id()));
	        	// Making a request to URL and getting response
		        String responseString = sh.makeServiceCall(ConfigConstants.Urls.liquor_comment_likes, ServiceHandler.POST, nameValuePairs);
	        	Log.i("responseString",responseString);
	        	/*{"total_comment":"1","status":"success","is_like":"1"}*/
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
						
						/* Now, here update the values */
						liquorCommentsList.get(position).setIs_like(Utils.getInstance().isTagExists(jsonObjParent, "is_like"));
						liquorCommentsList.get(position).setTotal_like(Utils.getInstance().isTagExists(jsonObjParent, "total_like"));
						
						/* Now, update the comment list */
						if(hiddenLinearLayoutReviews.getChildCount() != 0) {
							hiddenLinearLayoutReviews.removeAllViews();
						}
						if(liquorCommentsList.size()>0) {
							//  Now, set the Liquor Comments Data
							setReviewsData();
						}
						/*{"total_comment":"1","status":"success","is_like":"1"}*/
					}
					else {
						Utils.getInstance().displayToast(LiquorDetailsActivity.this, "Please try again.");
					}
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}
			objAsynTaskLikeLiquorComment = null;
			if (this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}
	}
}
