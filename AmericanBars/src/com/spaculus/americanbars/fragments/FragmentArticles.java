package com.spaculus.americanbars.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spaculus.adapters.CustomArticlesAdapter;
import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.R;
import com.spaculus.americanbars.TransactionActivity;
import com.spaculus.beans.Article;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.LoadMoreListView;
import com.spaculus.helpers.LoadMoreListView.OnLoadMoreListener;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentArticles extends Fragment {
	private View rootView = null;
	private LoadMoreListView listViewArticles;
	private EditText etSearch;
	private ImageView ivSearch, ivRefresh;

	//  For the Bar List
	private ArrayList<Article> articlesList = null;
 	private CustomArticlesAdapter adapter = null;
 	
 	private AsynTaskGetArticlesList objAsynTaskGetArticlesList = null;
	
	//  For the Web service response
	//  Implement Paging functionality to get the people list
	//  current_page = page_number = offset(in web service method)
	private int offset = 0; 
	private int totalRecords = 0;
	private boolean isLoadMoreButtonClicked = false;
	
	//  To know whether user is redirected to this screen from navigation drawer or not
	//  So on the basis of it we can show the action bar fields
	boolean isRedirectedFromMainActivity = false;
	
	public FragmentArticles(boolean flag){
		this.isRedirectedFromMainActivity = flag;
	}
	private RelativeLayout relativeLayoutArticles; 
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
			rootView = inflater.inflate(R.layout.fragment_articles, container, false);
			
			//  For the Action Bar
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			if(isRedirectedFromMainActivity) {
				((BaseActivity)getActivity()).setActionBarFromChild(false, true, false, true, true);	
			}
			else {
				((BaseActivity)getActivity()).setActionBarFromChild(true, true, false, true, true);
			}
			
			//  Mapping of all the views
			mappedAllViews();
			
			relativeLayoutArticles.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					hideSoftKeyboard();
				}
			});
			//  Initialize adapter and array list here.
			articlesList = new ArrayList<Article>();
			adapter = new CustomArticlesAdapter(getActivity(), R.layout.fragment_articles_list_item, articlesList);
			listViewArticles.setAdapter(adapter);
			
			//  Now, get the search My Favorite Bars List here
			loadData();
			
			//  Set a listener to be invoked when the list reaches the end
			listViewArticles.setOnLoadMoreListener(new OnLoadMoreListener() {
				public void onLoadMore() {
					try {
						if (totalRecords != 0) {
							isLoadMoreButtonClicked = true;
							if (offset < totalRecords && totalRecords>ConfigConstants.Constants.limit) {
								offset = articlesList.size();
								//  Now, get the search Articles List here
								if (objAsynTaskGetArticlesList == null) {
									if (Utils.getInstance().isNetworkAvailable(getActivity())) {
										objAsynTaskGetArticlesList = new AsynTaskGetArticlesList();
										objAsynTaskGetArticlesList.execute();
									} else {
										Utils.getInstance().showToastNoInternetAvailable(getActivity());
									}
								}
							}
							else {
								//  A method to called when no data is available
								noDataAvailable();
							}
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
				
			//  Search icon click event
			ivSearch.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					performSearch();
				}
			});
				
			//  Refresh icon click event
			ivRefresh.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						//  Now, here first of all hide the soft keyboard
						hideSoftKeyboard();
					
						//  Clear the Search Text
						etSearch.setText("");
						//  Now, here initialize here offset again as for this we have to load initial data only
						loadData();
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
				
			//  ListView row click event
			listViewArticles.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					try {
						if(isRedirectedFromMainActivity) {
							((BaseActivity) getActivity()).redirectArticleDetailsActivity(getActivity(), articlesList.get(position));
						}
						else {
							((TransactionActivity) getActivity()).rowClickMethodArticle(articlesList.get(position));
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			    @Override
			    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
			            performSearch();
			            return true;
			        }
			        return false;
			    }
			});
		} 
        catch (Exception e) {
			e.printStackTrace();
		}
        return rootView;
    }
	
	@Override
	public void onResume() {
		super.onResume();
		/*  This need to be implement as if user has change the article ratings then here we have to update this list.  */
		if(ConfigConstants.isArticleRatingDone) {
			ConfigConstants.isArticleRatingDone = false;
			//  Now, get the article list here
			loadData();
		}
	}
	
	//  This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			listViewArticles = (LoadMoreListView) rootView.findViewById(R.id.listViewArticles);
			etSearch = (EditText) rootView.findViewById(R.id.etSearch);
			ivSearch = (ImageView) rootView.findViewById(R.id.ivSearch);
			ivRefresh = (ImageView) rootView.findViewById(R.id.ivRefresh);
			relativeLayoutArticles = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutArticles);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to get the Article List based on the entered criteria
	public class AsynTaskGetArticlesList extends AsyncTask<String, Void, String> {
		private ProgressDialog pd = new ProgressDialog(getActivity());
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
	        	nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("limit", ""+ConfigConstants.Constants.limit));
		        nameValuePairs.add(new BasicNameValuePair("offset", ""+offset));
		        nameValuePairs.add(new BasicNameValuePair("keyword", etSearch.getText().toString().trim()));
	        	// Making a request to URL and getting response
				String responseString = sh.makeServiceCall(ConfigConstants.Urls.listarticle, ServiceHandler.POST, nameValuePairs);
	        	return responseString;
	        }
	        catch(Exception e) {
	        	e.printStackTrace();
	        }
			return null;
		}
		
		@Override
		protected void onPostExecute(String response) {
			try {
				super.onPostExecute(response);
				listViewArticles.onLoadMoreComplete();
				JSONObject jsonObjParent = null;
				if (response != null) {
					jsonObjParent = new JSONObject(response);
					String status = jsonObjParent.getString("status");
					if (status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)) {
						totalRecords = jsonObjParent.getInt("article_total");
						// Now, check whether data exists or not
						if (totalRecords > 0) {
							if (!isLoadMoreButtonClicked) {
								articlesList.clear();
							}
							if (jsonObjParent.has("result")) {
								if (!jsonObjParent.isNull("result")) {
									JSONArray jsonArrayResult = jsonObjParent.getJSONArray("result");
									for (int i = 0; i < jsonArrayResult.length(); i++) {
										JSONObject jsonObj = jsonArrayResult.getJSONObject(i);
										addArticleObject(jsonObj);
									}
									// Make ListView visible here.
									listViewArticles.setVisibility(View.VISIBLE);
									// notifying list adapter about data
									// changes so that it renders the list
									// view with updated data
									adapter.notifyDataSetChanged();

									// Call onLoadMoreComplete when the
									// LoadMore task, has finished
									((LoadMoreListView) listViewArticles).onLoadMoreComplete();
								}
							} else {
								// A method to called when no data is available
								noDataAvailable();
							}
						} else {
							// A method to called when no data is available
							noDataAvailable();
						}
					}
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
			objAsynTaskGetArticlesList = null;
			if(this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}	
		
		@Override
		protected void onCancelled() {
			try {
				// Notify the loading more operation has finished
				((LoadMoreListView) listViewArticles).onLoadMoreComplete();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
	
	//  Hide the soft key board on the click of Layout
	private void hideSoftKeyboard(){
		try {
			//Log.i("Method hideSoftKeyboard","Method hideSoftKeyboard");
			InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  A method to called when no data is available
	private void noDataAvailable() {
		try {
			if (isLoadMoreButtonClicked) {
				isLoadMoreButtonClicked = false;
				listViewArticles.hideFooterText();
				//Toast.makeText(getActivity(), ConfigConstants.getInstance().noMoreRecordFound, Toast.LENGTH_SHORT).show();
				Utils.showToastLong(getActivity(), ConfigConstants.Messages.noMoreRecordFound);
			} 
			else {
				listViewArticles.hideFooterText();
				listViewArticles.setVisibility(View.GONE);
				//Toast.makeText(getActivity(), ConfigConstants.getInstance().noRecordFound, Toast.LENGTH_SHORT).show();
				Utils.showToastLong(getActivity(), ConfigConstants.Messages.noRecordFound);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  A method to load the data initially
	private void loadData() {
		try {
			//  Now, here initialize here offset again as for this we have to load initial data only
			offset = 0;
			isLoadMoreButtonClicked = false;
			//  Now, get the Articles List here
			if (objAsynTaskGetArticlesList == null) {
				if (Utils.getInstance().isNetworkAvailable(getActivity())) {
					objAsynTaskGetArticlesList = new AsynTaskGetArticlesList();
					objAsynTaskGetArticlesList.execute();
				} else {
					Utils.getInstance().showToastNoInternetAvailable(getActivity());
				}
			}
			else{
				Utils.getInstance().showToastNoInternetAvailable(getActivity());
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* A method is used for the search functionality. */
	private void performSearch() {
		try {
			//  Now, here first of all hide the soft keyboard
			hideSoftKeyboard();
			
			//  Now, here initialize here offset again as for this we have to load initial data only
			loadData();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* A method is used to add the article object. */
	private void addArticleObject(JSONObject jsonObj) {
		Article objArticle = new Article();
		objArticle.setBlog_id(Utils.getInstance().isTagExists(jsonObj, "blog_id"));
		objArticle.setBlog_title(Utils.getInstance().isTagExists(jsonObj, "blog_title"));
		objArticle.setBlog_image(Utils.getInstance().isTagExists(jsonObj, "blog_image"));
		objArticle.setBlog_description(Utils.getInstance().isTagExists(jsonObj, "blog_description"));
		objArticle.setDate_added(Utils.getInstance().isTagExists(jsonObj, "date_added"));
		objArticle.setTotal_rating(Utils.getInstance().isTagExists(jsonObj, "total_rating"));
		objArticle.setBlog_description_with_image(Utils.getInstance().isTagExists(jsonObj, "blog_description_with_image"));
		articlesList.add(objArticle);
	}
}
