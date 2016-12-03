package com.spaculus.americanbars;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spaculus.adapters.CustomCommentRepliesAdapter;
import com.spaculus.americanbars.dialogfragments.FragmentAlertDialogDelete;
import com.spaculus.beans.Reply;
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

public class LiquorCommentRepliesActivity extends BaseActivity {
 	private EditText etReply;
 	private Button buttonPostReply;
 	private LoadMoreListView listViewReplies;
 	private ArrayList<Reply> replyList = null;
 	private CustomCommentRepliesAdapter adapter = null;
 	private AsynTaskGetReplyList objAsynTaskGetReplyList = null;
 	
	//  For the Web service response
	//  Implement Paging functionality to get the people list
	//  current_page = page_number = offset(in web service method)
	private int offset = 0; 
	private int totalRecords = 0;
	private boolean isLoadMoreButtonClicked = false;
 	
	/* For the Add Reply functionality */
 	private AsynTaskPostReply objAsynTaskPostReply = null;
 	/* For the Delete Reply functionality */
	
	/* To get the value of a master_comment_id */
	private String liquor_id = "", master_comment_id = "";
	
	/* Delete Reply functionality */
 	private AsynTaskDeleteReply objAsynTaskDeleteReply = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_replies_list);
			
			//  Create Action Bar
			createActionBar(ConfigConstants.Titles.TITLE_LIQUOR_COMMENT_REPLIES, R.layout.custom_actionbar, LiquorCommentRepliesActivity.this, true);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);
			
			//  Mapping of all the views
			mappedAllViews();
			
			//  To get the liquor id
			Bundle b = getIntent().getExtras();
			liquor_id = b.getString(ConfigConstants.Keys.KEY_LIQUOR_ID);
			master_comment_id = b.getString(ConfigConstants.Keys.KEY_MASTER_COMMENT_ID);
			
			//  Initialize adapter and array list here.
			replyList = new ArrayList<Reply>();
			adapter = new CustomCommentRepliesAdapter(LiquorCommentRepliesActivity.this, R.layout.activity_replies_list_item, replyList, LiquorCommentRepliesActivity.this, ConfigConstants.Titles.TITLE_LIQUOR_COMMENT_REPLIES);
			listViewReplies.setAdapter(adapter);
			
			//  Now, get the Comments list here
			if(Utils.getInstance().isNetworkAvailable(LiquorCommentRepliesActivity.this)) {
				if (objAsynTaskGetReplyList == null) {
					if (Utils.getInstance().isNetworkAvailable(LiquorCommentRepliesActivity.this)) {
						objAsynTaskGetReplyList = new AsynTaskGetReplyList();
						objAsynTaskGetReplyList.execute();
					} else {
						Utils.getInstance().showToastNoInternetAvailable(LiquorCommentRepliesActivity.this);
					}
				}
			}
			else{
				Utils.getInstance().showToastNoInternetAvailable(LiquorCommentRepliesActivity.this);
			}
			
			//  Set a listener to be invoked when the list reaches the end
			listViewReplies.setOnLoadMoreListener(new OnLoadMoreListener() {
				public void onLoadMore() {
					try {
						isLoadMoreButtonClicked = true;
						if (totalRecords != 0) {
							if (offset < totalRecords && totalRecords > ConfigConstants.Constants.limit) {
								offset = replyList.size();
								//  Now, get the Reviews list here
								if (Utils.getInstance().isNetworkAvailable(LiquorCommentRepliesActivity.this)) {
									if (objAsynTaskGetReplyList == null) {
										if (Utils.getInstance().isNetworkAvailable(LiquorCommentRepliesActivity.this)) {
											objAsynTaskGetReplyList = new AsynTaskGetReplyList();
											objAsynTaskGetReplyList.execute();
										} else {
											Utils.getInstance().showToastNoInternetAvailable(LiquorCommentRepliesActivity.this);
										}
									}
								} 
								else {
									Utils.getInstance().showToastNoInternetAvailable(LiquorCommentRepliesActivity.this);
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
			
			buttonPostReply.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						//  First, hide the soft key board.
						hideSoftKeyboard();
						//  Now, first of all check all the validations
						String validationResponse  = Validation.getInstance().postReply(etReply.getText().toString().trim());
						
						//  Means all the fields are properly entered
						if(validationResponse.isEmpty()) {
							if(Utils.getInstance() .isNetworkAvailable(LiquorCommentRepliesActivity.this)) {
								if (objAsynTaskPostReply == null) {
									if (Utils.getInstance().isNetworkAvailable(LiquorCommentRepliesActivity.this)) {
										objAsynTaskPostReply = new AsynTaskPostReply();
										objAsynTaskPostReply.execute();
									} else {
										Utils.getInstance().showToastNoInternetAvailable(LiquorCommentRepliesActivity.this);
									}
								}
							}
							else{
								Utils.getInstance().showToastNoInternetAvailable(LiquorCommentRepliesActivity.this);
							}
						}
						else {
							Utils.toastLong(LiquorCommentRepliesActivity.this, validationResponse);
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			etReply.setOnTouchListener(new OnTouchListener() {
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					try {
						if (v.getId() == R.id.etReply) {
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
			etReply = (EditText)findViewById(R.id.etReply);
			buttonPostReply = (Button)findViewById(R.id.buttonPostReply);
			listViewReplies = (LoadMoreListView)findViewById(R.id.listViewReplies);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* An AnyncTask to get the Reply List. */
	public class AsynTaskGetReplyList extends AsyncTask<String, Void, String> {
		private ProgressDialog pd = new ProgressDialog(LiquorCommentRepliesActivity.this);
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
	        	nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(LiquorCommentRepliesActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(LiquorCommentRepliesActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(LiquorCommentRepliesActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("master_comment_id", master_comment_id));
		        nameValuePairs.add(new BasicNameValuePair("limit", ""+ConfigConstants.Constants.limit));
		        nameValuePairs.add(new BasicNameValuePair("offset", ""+offset));
	        	// Making a request to URL and getting response
				String responseString = sh.makeServiceCall(ConfigConstants.Urls.liquor_subcomments, ServiceHandler.POST, nameValuePairs);
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
				listViewReplies.onLoadMoreComplete();
				try {
					JSONObject jsonObjParent = null;
					if(result != null){
						jsonObjParent = new JSONObject(result);
						String status = Utils.getInstance().isTagExists(jsonObjParent, "status");
						if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)){
							totalRecords = jsonObjParent.getInt("liquor_subcomment_total");
							
							//  Now, check whether data exists or not
							if(totalRecords > 0) {
								if (!isLoadMoreButtonClicked) {
									replyList.clear();
								}
								if(jsonObjParent.has("liquor_subcomment")) {
									JSONArray jsonArrayBarComment = jsonObjParent.getJSONArray("liquor_subcomment");
									if(jsonArrayBarComment != null) {
										if(jsonArrayBarComment.length() > 0) {
											for (int i = 0; i < jsonArrayBarComment.length(); i++) {
												JSONObject jsonObj = jsonArrayBarComment.getJSONObject(i);
												replyList.add(new ParseJsonObject().addLiquorReplyObject(jsonObj));
											}
											//  Make ListView visible here.
											listViewReplies.setVisibility(View.VISIBLE);
											//tvNoReviews.setVisibility(View.GONE);
											//  notifying list adapter about data changes so that it renders the list view with updated data
											adapter.notifyDataSetChanged();
											
											//  Scroll the ListView at the top
											if(!isLoadMoreButtonClicked) {
												listViewReplies.setSelection(0);
											}
											// Call onLoadMoreComplete when the LoadMore task, has finished
											((LoadMoreListView) listViewReplies).onLoadMoreComplete();
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
			objAsynTaskGetReplyList = null;
			if(this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}	
		
		@Override
		protected void onCancelled() {
			try {
				// Notify the loading more operation has finished
				((LoadMoreListView) listViewReplies).onLoadMoreComplete();
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
				listViewReplies.hideFooterText();
				Utils.showToastLong(LiquorCommentRepliesActivity.this, ConfigConstants.Messages.noMoreRecordFound);
			} 
			else {
				listViewReplies.hideFooterText();
				listViewReplies.setVisibility(View.GONE);
				//tvNoReviews.setVisibility(View.VISIBLE);
				Utils.showToastLong(LiquorCommentRepliesActivity.this, ConfigConstants.Messages.noRecordFound);
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
			imm.hideSoftInputFromWindow(etReply.getWindowToken(), 0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* An AnyncTask for the post reply functionality. */
	public class AsynTaskPostReply extends AsyncTask<String, Void, String> {
		private ProgressDialog pd = new ProgressDialog(LiquorCommentRepliesActivity.this);
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
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(LiquorCommentRepliesActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(LiquorCommentRepliesActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(LiquorCommentRepliesActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("liquor_id", liquor_id));
		        nameValuePairs.add(new BasicNameValuePair("master_comment_id", master_comment_id));
		        nameValuePairs.add(new BasicNameValuePair("comment", etReply.getText().toString().trim()));
		        
	        	// Making a request to URL and getting response
		        String responseString = sh.makeServiceCall(ConfigConstants.Urls.add_liquor_subcomment, ServiceHandler.POST, nameValuePairs);
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
						Utils.getInstance().displayToast(LiquorCommentRepliesActivity.this, "Your reply is added successfully.");
						/* Now, here clear all the required fields */
						etReply.setText("");
						etReply.clearFocus();
						
						//  Now, get the Comments list here
						if(Utils.getInstance().isNetworkAvailable(LiquorCommentRepliesActivity.this)) {
							if (objAsynTaskGetReplyList == null) {
								if (Utils.getInstance().isNetworkAvailable(LiquorCommentRepliesActivity.this)) {
									objAsynTaskGetReplyList = new AsynTaskGetReplyList();
									objAsynTaskGetReplyList.execute();
								} else {
									Utils.getInstance().showToastNoInternetAvailable(LiquorCommentRepliesActivity.this);
								}
							}
						}
						else{
							Utils.getInstance().showToastNoInternetAvailable(LiquorCommentRepliesActivity.this);
						}
					}
					else {
						Utils.getInstance().displayToast(LiquorCommentRepliesActivity.this, "Your reply is not added. Please try again.");
					}
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}
			objAsynTaskPostReply = null;
			if (this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}
	}
	
	/* To show an alert dialog */
	public void showAlertDeleteReply(int position) {
		try {
			FragmentAlertDialogDelete objDeleteReply = new FragmentAlertDialogDelete("Delete Reply", "Are you sure you want to delete this reply?", "Yes", "No", 
					ConfigConstants.Titles.TITLE_LIQUOR_COMMENT_REPLIES, LiquorCommentRepliesActivity.this, position);
			objDeleteReply.show(getFragmentManager(), "dialog");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* A method is used to call an async for the delete reply. */
	public void deleteReply(int position) {
		if(Utils.getInstance() .isNetworkAvailable(LiquorCommentRepliesActivity.this)) {
			if (objAsynTaskDeleteReply == null) {
				if (Utils.getInstance().isNetworkAvailable(LiquorCommentRepliesActivity.this)) {
					objAsynTaskDeleteReply = new AsynTaskDeleteReply();
					objAsynTaskDeleteReply.execute(String.valueOf(position));
				} else {
					Utils.getInstance().showToastNoInternetAvailable(LiquorCommentRepliesActivity.this);
				}
			}
		}
		else{
			Utils.getInstance().showToastNoInternetAvailable(LiquorCommentRepliesActivity.this);
		}
}
	
	/* An AnyncTask for a delete functionality. */
	public class AsynTaskDeleteReply extends AsyncTask<String, Void, String> {
		private ProgressDialog pd = new ProgressDialog(LiquorCommentRepliesActivity.this);
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
				ServiceHandler sh = new ServiceHandler();
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(LiquorCommentRepliesActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(LiquorCommentRepliesActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(LiquorCommentRepliesActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("liquor_comment_id", replyList.get(position).getComment_id()));
	        	// Making a request to URL and getting response
		        String responseString = sh.makeServiceCall(ConfigConstants.Urls.remove_liquor_comment, ServiceHandler.POST, nameValuePairs);
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
						/* Now, remove the row */
						replyList.remove(position);
						 /*Now, here notify an adapter*/ 
						adapter.notifyDataSetChanged();
					}
					else {
						Utils.getInstance().displayToast(LiquorCommentRepliesActivity.this, "Please try again.");
					}
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}
			objAsynTaskDeleteReply = null;
			if (this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}
	}
}
