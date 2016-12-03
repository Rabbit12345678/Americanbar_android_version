package com.spaculus.americanbars;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spaculus.adapters.CustomBarReviewsAdapter;
import com.spaculus.beans.Review;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.LoadMoreListView;
import com.spaculus.helpers.ParseJsonObject;
import com.spaculus.helpers.LoadMoreListView.OnLoadMoreListener;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class BarReviewsListActivity extends BaseActivity {
 	private EditText etReviewTitle, etReviewDescription;
 	private RatingBar ratingBarReview;
 	private Button buttonPostReview;
 	private LoadMoreListView listViewBarReviews;
 	private TextView tvNoReviews;
 	private ArrayList<Review> barReviewsList = null;
 	private CustomBarReviewsAdapter adapter = null;
 	private AsynTaskGetBarReviewsList objAsynTaskGetBarReviewsList = null;
 	
	//  For the Web service response
	//  Implement Paging functionality to get the people list
	//  current_page = page_number = offset(in web service method)
	private int offset = 0; 
	private int totalRecords = 0;
	private boolean isLoadMoreButtonClicked = false;
 	
	/* For the Post Review functionality */
 	private float selectedRating = 0.0f;
 	private AsynTaskPostReview objAsynTaskPostReview = null;
 	/* For the Post Review functionality */
	
	/* To get the value of a bar_id */
	private String bar_id = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_bar_reviews_list);
			
			//  Create Action Bar
			createActionBar("Bar Reviews", R.layout.custom_actionbar, BarReviewsListActivity.this, true);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);
			
			//  Mapping of all the views
			mappedAllViews();
			
			//  To get the bar id
			Bundle b = getIntent().getExtras();
			bar_id = b.getString(ConfigConstants.Keys.KEY_BAR_ID);
			
			//  Initialize adapter and array list here.
			barReviewsList = new ArrayList<Review>();
			adapter = new CustomBarReviewsAdapter(BarReviewsListActivity.this, R.layout.activity_bar_details_bar_events_list_item, barReviewsList);
			listViewBarReviews.setAdapter(adapter);
			
			//  Now, get the Reviews list here
			if(Utils.getInstance().isNetworkAvailable(BarReviewsListActivity.this)) {
				if (objAsynTaskGetBarReviewsList == null) {
					if (Utils.getInstance().isNetworkAvailable(BarReviewsListActivity.this)) {
						objAsynTaskGetBarReviewsList = new AsynTaskGetBarReviewsList();
						objAsynTaskGetBarReviewsList.execute();
					} else {
						Utils.getInstance().showToastNoInternetAvailable(BarReviewsListActivity.this);
					}
				}
			}
			else{
				Utils.getInstance().showToastNoInternetAvailable(BarReviewsListActivity.this);
			}
			
			//  Set a listener to be invoked when the list reaches the end
			listViewBarReviews.setOnLoadMoreListener(new OnLoadMoreListener() {
				public void onLoadMore() {
					try {
						isLoadMoreButtonClicked = true;
						if (totalRecords != 0) {
							if (offset < totalRecords && totalRecords > ConfigConstants.Constants.limit) {
								offset = barReviewsList.size();
								//  Now, get the Reviews list here
								if (Utils.getInstance().isNetworkAvailable(BarReviewsListActivity.this)) {
									if (objAsynTaskGetBarReviewsList == null) {
										if (Utils.getInstance().isNetworkAvailable(BarReviewsListActivity.this)) {
											objAsynTaskGetBarReviewsList = new AsynTaskGetBarReviewsList();
											objAsynTaskGetBarReviewsList.execute();
										} else {
											Utils.getInstance().showToastNoInternetAvailable(BarReviewsListActivity.this);
										}
									}
								} 
								else {
									Utils.getInstance().showToastNoInternetAvailable(BarReviewsListActivity.this);
								}
							} else {
								// A method to called when no data is available
								noDataAvailable();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
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
						if(validationResponse.isEmpty()){
							if(Utils.getInstance() .isNetworkAvailable(BarReviewsListActivity.this)) {
								if (objAsynTaskPostReview == null) {
									if (Utils.getInstance().isNetworkAvailable(BarReviewsListActivity.this)) {
										objAsynTaskPostReview = new AsynTaskPostReview();
										objAsynTaskPostReview.execute();
									} else {
										Utils.getInstance().showToastNoInternetAvailable(BarReviewsListActivity.this);
									}
								}
							}
							else{
								Utils.getInstance().showToastNoInternetAvailable(BarReviewsListActivity.this);
							}
						}
						else {
							Utils.toastLong(BarReviewsListActivity.this, validationResponse);
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
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
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			etReviewTitle = (EditText)findViewById(R.id.etReviewTitle);
			etReviewDescription = (EditText)findViewById(R.id.etReviewDescription);
			ratingBarReview = (RatingBar)findViewById(R.id.ratingBarReview);
			buttonPostReview = (Button)findViewById(R.id.buttonPostReview);
			listViewBarReviews = (LoadMoreListView)findViewById(R.id.listViewBarReviews);
			tvNoReviews = (TextView)findViewById(R.id.tvNoReviews);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* An AnyncTask to get the Bar Reviews List. */
	public class AsynTaskGetBarReviewsList extends AsyncTask<String, Void, String> {
		private ProgressDialog pd = new ProgressDialog(BarReviewsListActivity.this);
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
		protected String doInBackground(String... params) {
			try {
				ServiceHandler sh = new ServiceHandler();
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        	nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(BarReviewsListActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(BarReviewsListActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(BarReviewsListActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("bar_id", bar_id));
		        nameValuePairs.add(new BasicNameValuePair("limit", ""+ConfigConstants.Constants.limit));
		        nameValuePairs.add(new BasicNameValuePair("offset", ""+offset));
	        	// Making a request to URL and getting response
				String responseString = sh.makeServiceCall(ConfigConstants.Urls.barcomment, ServiceHandler.POST, nameValuePairs);
				return responseString;
	        }
	        catch(Exception e) {
	        	e.printStackTrace();
	        }
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			try {
				super.onPostExecute(result);
				listViewBarReviews.onLoadMoreComplete();
				try {
					JSONObject jsonObjParent = null;
					if(result != null){
						jsonObjParent = new JSONObject(result);
						String status = Utils.getInstance().isTagExists(jsonObjParent, "status");
						if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)){
							totalRecords = jsonObjParent.getInt("barcomment_total");
							
							//  Now, check whether data exists or not
							if(totalRecords > 0) {
								if (!isLoadMoreButtonClicked) {
									barReviewsList.clear();
								}
								if(jsonObjParent.has("barcomment")) {
									JSONArray jsonArrayBarComment = jsonObjParent.getJSONArray("barcomment");
									if(jsonArrayBarComment != null) {
										if(jsonArrayBarComment.length() > 0) {
											//ParseJsonObject objParseJsonObject = new ParseJsonObject();
											for (int i = 0; i < jsonArrayBarComment.length(); i++) {
												JSONObject jsonObj = jsonArrayBarComment.getJSONObject(i);
												barReviewsList.add(new ParseJsonObject().addBarReviewObject(jsonObj));
											}
											//  Make ListView visible here.
											listViewBarReviews.setVisibility(View.VISIBLE);
											tvNoReviews.setVisibility(View.GONE);
											//  notifying list adapter about data changes so that it renders the list view with updated data
											adapter.notifyDataSetChanged();
											
											//  Scroll the ListView at the top
											if(!isLoadMoreButtonClicked) {
												listViewBarReviews.setSelection(0);
											}
											// Call onLoadMoreComplete when the LoadMore task, has finished
											((LoadMoreListView) listViewBarReviews).onLoadMoreComplete();
										}
										else {
											//  A method to called when no data is available
											noDataAvailable();
										}
									}
									else {
										//  A method to called when no data is available
										noDataAvailable();
									}
								}
								else {
									//  A method to called when no data is available
									noDataAvailable();
								}
							}
							else{
								//  A method to called when no data is available
								noDataAvailable();
							}
						}
						else {
							//  A method to called when no data is available
							noDataAvailable();}
					}
				} 
				catch (JSONException e) {
					e.printStackTrace();
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			objAsynTaskGetBarReviewsList = null;
			if(this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}	
		
		@Override
		protected void onCancelled() {
			try {
				// Notify the loading more operation has finished
				((LoadMoreListView) listViewBarReviews).onLoadMoreComplete();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
	
	//  A method to called when no data is available
	private void noDataAvailable() {
		try {
			if (isLoadMoreButtonClicked) {
				isLoadMoreButtonClicked = false;
				listViewBarReviews.hideFooterText();
				Utils.showToastLong(BarReviewsListActivity.this, ConfigConstants.Messages.noMoreRecordFound);
			} 
			else {
				listViewBarReviews.hideFooterText();
				listViewBarReviews.setVisibility(View.GONE);
				tvNoReviews.setVisibility(View.VISIBLE);
				Utils.showToastLong(BarReviewsListActivity.this, ConfigConstants.Messages.noRecordFound);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  To hide the soft key board on the click of Layout
	private void hideSoftKeyboard(){
		try {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etReviewTitle.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etReviewDescription.getWindowToken(), 0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* An AnyncTask for the post review functionality. */
	public class AsynTaskPostReview extends AsyncTask<String, Void, String> {
		private ProgressDialog pd = new ProgressDialog(BarReviewsListActivity.this);
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
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(BarReviewsListActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(BarReviewsListActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(BarReviewsListActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
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
						Utils.getInstance().displayToast(BarReviewsListActivity.this, "Your review is added successfully.");
						ConfigConstants.isBarReviewAdded = true;
						/* Now, here clear all the required fields */
						etReviewTitle.setText("");
						etReviewDescription.setText("");
						ratingBarReview.setRating(0);
						selectedRating = 0;
						etReviewDescription.clearFocus();
						etReviewTitle.clearFocus();
						
						//  Now, get the Reviews list here
						if(Utils.getInstance().isNetworkAvailable(BarReviewsListActivity.this)) {
							if (objAsynTaskGetBarReviewsList == null) {
								if (Utils.getInstance().isNetworkAvailable(BarReviewsListActivity.this)) {
									objAsynTaskGetBarReviewsList = new AsynTaskGetBarReviewsList();
									objAsynTaskGetBarReviewsList.execute();
								} else {
									Utils.getInstance().showToastNoInternetAvailable(BarReviewsListActivity.this);
								}
							}
						}
						else{
							Utils.getInstance().showToastNoInternetAvailable(BarReviewsListActivity.this);
						}
					}
					else {
						Utils.getInstance().displayToast(BarReviewsListActivity.this, "Your review is not added. Please try again.");
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
