package com.spaculus.americanbars;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spaculus.adapters.CustomCommentsAdapter;
import com.spaculus.beans.Review;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.LoadMoreListView;
import com.spaculus.helpers.LoadMoreListView.OnLoadMoreListener;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class LiquorCommentsListActivity extends BaseActivity {
 	private EditText etCommentTitle, etCommentDescription;
 	private Button buttonPostComment;
 	private LoadMoreListView listViewLiquorComments;
 	//private TextView tvNoReviews;
 	private ArrayList<Review> liquorCommentsList = null;
 	private CustomCommentsAdapter adapter = null;
 	private AsynTaskGetLiquorCommentsList objAsynTaskGetLiquorCommentsList = null;
 	
	//  For the Web service response
	//  Implement Paging functionality to get the people list
	//  current_page = page_number = offset(in web service method)
	private int offset = 0; 
	private int totalRecords = 0;
	private boolean isLoadMoreButtonClicked = false;
 	
	/* For the Post Comment functionality */
 	private AsynTaskPostComment objAsynTaskPostComment = null;
 	/* For the Post Comment functionality */
	
	/* To get the value of a liquor_id */
	private String liquor_id = "";
	
	/* Like Liquor Comment functionality */
 	private AsynTaskLikeLiquorComment objAsynTaskLikeLiquorComment = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_comments_list);
			
			//  Create Action Bar
			createActionBar(ConfigConstants.Titles.TITLE_LIQUOR_COMMENTS, R.layout.custom_actionbar, LiquorCommentsListActivity.this, true);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);
			
			//  Mapping of all the views
			mappedAllViews();
			
			//  To get the bar id
			Bundle b = getIntent().getExtras();
			liquor_id = b.getString(ConfigConstants.Keys.KEY_LIQUOR_ID);
			
			//  Initialize adapter and array list here.
			liquorCommentsList = new ArrayList<Review>();
			adapter = new CustomCommentsAdapter(LiquorCommentsListActivity.this, R.layout.activity_comments_list_item, liquorCommentsList, LiquorCommentsListActivity.this, ConfigConstants.Titles.TITLE_LIQUOR_COMMENTS);
			listViewLiquorComments.setAdapter(adapter);
			
			//  Now, get the Comments list here
			if(Utils.getInstance().isNetworkAvailable(LiquorCommentsListActivity.this)) {
				if (objAsynTaskGetLiquorCommentsList == null) {
					if (Utils.getInstance().isNetworkAvailable(LiquorCommentsListActivity.this)) {
						objAsynTaskGetLiquorCommentsList = new AsynTaskGetLiquorCommentsList();
						objAsynTaskGetLiquorCommentsList.execute();
					} else {
						Utils.getInstance().showToastNoInternetAvailable(LiquorCommentsListActivity.this);
					}
				}
			}
			else{
				Utils.getInstance().showToastNoInternetAvailable(LiquorCommentsListActivity.this);
			}
			
			//  Set a listener to be invoked when the list reaches the end
			listViewLiquorComments.setOnLoadMoreListener(new OnLoadMoreListener() {
				public void onLoadMore() {
					try {
						isLoadMoreButtonClicked = true;
						if (totalRecords != 0) {
							if (offset < totalRecords && totalRecords > ConfigConstants.Constants.limit) {
								offset = liquorCommentsList.size();
								//  Now, get the Reviews list here
								if (Utils.getInstance().isNetworkAvailable(LiquorCommentsListActivity.this)) {
									if (objAsynTaskGetLiquorCommentsList == null) {
										if (Utils.getInstance().isNetworkAvailable(LiquorCommentsListActivity.this)) {
											objAsynTaskGetLiquorCommentsList = new AsynTaskGetLiquorCommentsList();
											objAsynTaskGetLiquorCommentsList.execute();
										} else {
											Utils.getInstance().showToastNoInternetAvailable(LiquorCommentsListActivity.this);
										}
									}
								} 
								else {
									Utils.getInstance().showToastNoInternetAvailable(LiquorCommentsListActivity.this);
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
							if(Utils.getInstance() .isNetworkAvailable(LiquorCommentsListActivity.this)) {
								if (objAsynTaskPostComment == null) {
									if (Utils.getInstance().isNetworkAvailable(LiquorCommentsListActivity.this)) {
										objAsynTaskPostComment = new AsynTaskPostComment();
										objAsynTaskPostComment.execute();
									} else {
										Utils.getInstance().showToastNoInternetAvailable(LiquorCommentsListActivity.this);
									}
								}
							}
							else{
								Utils.getInstance().showToastNoInternetAvailable(LiquorCommentsListActivity.this);
							}
						}
						else {
							Utils.toastLong(LiquorCommentsListActivity.this, validationResponse);
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			etCommentDescription.setOnTouchListener(new OnTouchListener() {
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					try {
						if (v.getId() == R.id.etCommentDescription) {
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
			etCommentTitle = (EditText)findViewById(R.id.etCommentTitle);
			etCommentDescription = (EditText)findViewById(R.id.etCommentDescription);
			buttonPostComment = (Button)findViewById(R.id.buttonPostComment);
			listViewLiquorComments = (LoadMoreListView)findViewById(R.id.listViewComments);
			//tvNoReviews = (TextView)findViewById(R.id.tvNoReviews);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* An AnyncTask to get the Liquor Comments List. */
	public class AsynTaskGetLiquorCommentsList extends AsyncTask<String, Void, String> {
		private ProgressDialog pd = new ProgressDialog(LiquorCommentsListActivity.this);
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
	        	nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(LiquorCommentsListActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(LiquorCommentsListActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(LiquorCommentsListActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("liquor_id", liquor_id));
		        nameValuePairs.add(new BasicNameValuePair("limit", ""+ConfigConstants.Constants.limit));
		        nameValuePairs.add(new BasicNameValuePair("offset", ""+offset));
	        	// Making a request to URL and getting response
				String responseString = sh.makeServiceCall(ConfigConstants.Urls.liquor_comments, ServiceHandler.POST, nameValuePairs);
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
				listViewLiquorComments.onLoadMoreComplete();
				try {
					JSONObject jsonObjParent = null;
					if(result != null){
						jsonObjParent = new JSONObject(result);
						String status = Utils.getInstance().isTagExists(jsonObjParent, "status");
						if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)){
							totalRecords = jsonObjParent.getInt("liquor_comment_total");
							
							//  Now, check whether data exists or not
							if(totalRecords > 0) {
								if (!isLoadMoreButtonClicked) {
									liquorCommentsList.clear();
								}
								if(jsonObjParent.has("liquor_comment")) {
									JSONArray jsonArrayBarComment = jsonObjParent.getJSONArray("liquor_comment");
									if(jsonArrayBarComment != null) {
										if(jsonArrayBarComment.length() > 0) {
											for (int i = 0; i < jsonArrayBarComment.length(); i++) {
												JSONObject jsonObj = jsonArrayBarComment.getJSONObject(i);
												liquorCommentsList.add(new ParseJsonObject().addLiquorCommentObject(jsonObj));
											}
											//  Make ListView visible here.
											listViewLiquorComments.setVisibility(View.VISIBLE);
											//tvNoReviews.setVisibility(View.GONE);
											//  notifying list adapter about data changes so that it renders the list view with updated data
											adapter.notifyDataSetChanged();
											
											//  Scroll the ListView at the top
											if(!isLoadMoreButtonClicked) {
												listViewLiquorComments.setSelection(0);
											}
											// Call onLoadMoreComplete when the LoadMore task, has finished
											((LoadMoreListView) listViewLiquorComments).onLoadMoreComplete();
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
							noDataAvailable();
						}
					}
				} 
				catch (JSONException e) {
					e.printStackTrace();
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			objAsynTaskGetLiquorCommentsList = null;
			if(this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}	
		
		@Override
		protected void onCancelled() {
			try {
				// Notify the loading more operation has finished
				((LoadMoreListView) listViewLiquorComments).onLoadMoreComplete();
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
				listViewLiquorComments.hideFooterText();
				Utils.showToastLong(LiquorCommentsListActivity.this, ConfigConstants.Messages.noMoreRecordFound);
			} 
			else {
				listViewLiquorComments.hideFooterText();
				listViewLiquorComments.setVisibility(View.GONE);
				//tvNoReviews.setVisibility(View.VISIBLE);
				Utils.showToastLong(LiquorCommentsListActivity.this, ConfigConstants.Messages.noRecordFound);
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
			imm.hideSoftInputFromWindow(etCommentTitle.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etCommentDescription.getWindowToken(), 0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* An AnyncTask for the post comment functionality. */
	public class AsynTaskPostComment extends AsyncTask<String, Void, String> {
		private ProgressDialog pd = new ProgressDialog(LiquorCommentsListActivity.this);
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
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(LiquorCommentsListActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(LiquorCommentsListActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(LiquorCommentsListActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
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
						Utils.getInstance().displayToast(LiquorCommentsListActivity.this, "Your comment is added successfully.");
						ConfigConstants.isLiquorCommentAdded = true;
						/* Now, here clear all the required fields */
						etCommentTitle.setText("");
						etCommentDescription.setText("");
						etCommentTitle.clearFocus();
						etCommentDescription.clearFocus();
						
						//  Now, get the Comments list here
						if(Utils.getInstance().isNetworkAvailable(LiquorCommentsListActivity.this)) {
							if (objAsynTaskGetLiquorCommentsList == null) {
								if (Utils.getInstance().isNetworkAvailable(LiquorCommentsListActivity.this)) {
									objAsynTaskGetLiquorCommentsList = new AsynTaskGetLiquorCommentsList();
									objAsynTaskGetLiquorCommentsList.execute();
								} else {
									Utils.getInstance().showToastNoInternetAvailable(LiquorCommentsListActivity.this);
								}
							}
						}
						else{
							Utils.getInstance().showToastNoInternetAvailable(LiquorCommentsListActivity.this);
						}
					}
					else {
						Utils.getInstance().displayToast(LiquorCommentsListActivity.this, "Your comment is not added. Please try again.");
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
	
	/* A method is used to call an async for the like comment. */
	public void likeLiquorComment(int position) {
		if(Utils.getInstance() .isNetworkAvailable(LiquorCommentsListActivity.this)) {
			if (objAsynTaskLikeLiquorComment == null) {
				if (Utils.getInstance().isNetworkAvailable(LiquorCommentsListActivity.this)) {
					objAsynTaskLikeLiquorComment = new AsynTaskLikeLiquorComment();
					objAsynTaskLikeLiquorComment.execute(String.valueOf(position));
				} else {
					Utils.getInstance().showToastNoInternetAvailable(LiquorCommentsListActivity.this);
				}
			}
		}
		else{
			Utils.getInstance().showToastNoInternetAvailable(LiquorCommentsListActivity.this);
		}
}
	
	/* An AnyncTask for a like liquor comment functionality. */
	public class AsynTaskLikeLiquorComment extends AsyncTask<String, Void, String> {
		private ProgressDialog pd = new ProgressDialog(LiquorCommentsListActivity.this);
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
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(LiquorCommentsListActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(LiquorCommentsListActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(LiquorCommentsListActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
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
						ConfigConstants.isLiquorCommentAdded = true;
						/* Now, here update the values */
						liquorCommentsList.get(position).setIs_like(Utils.getInstance().isTagExists(jsonObjParent, "is_like"));
						liquorCommentsList.get(position).setTotal_like(Utils.getInstance().isTagExists(jsonObjParent, "total_like"));
						
						/* Now, here notify an adapter */
						adapter.notifyDataSetChanged();
					}
					else {
						Utils.getInstance().displayToast(LiquorCommentsListActivity.this, "Please try again.");
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
