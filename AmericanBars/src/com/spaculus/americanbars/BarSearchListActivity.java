package com.spaculus.americanbars;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spaculus.adapters.CustomBarAdapter;
import com.spaculus.beans.Bar;
import com.spaculus.beans.SearchOptions;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.LoadMoreListView;
import com.spaculus.helpers.LoadMoreListView.OnLoadMoreListener;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class BarSearchListActivity extends BaseActivity{

	private LoadMoreListView listViewBars;
	private TextView tvSearchTitle;
	private Button buttonSearch;
	private Spinner spinnerSearchOptions;
	
	//  For the Bar List
	private ArrayList<Bar> barList = null;
 	private CustomBarAdapter adapter = null;
 	
 	//  For the Search Options
 	private ArrayList<SearchOptions> searchOptionsList = null;
 	private ArrayAdapter<String> adapterSearchOptions = null;
 	private String selectedSearchOption = "";
	
	//  To get the data from the previous screen
	private String title = "", state = "", city = "", zipcode = "", searchText= "";
	
	//  For the Web service response
	//  Implement Paging functionality to get the people list
	//  current_page = page_number = offset(in web service method)
	private int offset = 0; 
	private int totalRecords = 0;
	private boolean isLoadMoreButtonClicked = false;
	private String address = "", day = "";
	//Bundle bundle;
	private String actionBarTitle = "";
	
	/* To get the values from the intent */
 	private String isRedirectedFrom = "";
 	
 	/* Suggest New Bar functionality */
	private Button buttonSuggestNewBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_bar_search_list);
			
			//  Get the Search Bar Text data here.
			/* To get the values */
			Bundle b = getIntent().getExtras();
			isRedirectedFrom = b.getString(ConfigConstants.Keys.KEY_IS_REDIRECTED_FROM);
			
			if(isRedirectedFrom.equals(ConfigConstants.Constants.CONSTANT_FIRST)) {
				title = b.getString("title");
				state = b.getString("state");
				city = b.getString("city");
				zipcode = b.getString("zipcode");
				actionBarTitle = "BAR";
			}
			else {
				title = b.getString("title");
				address = b.getString("address");
				day = b.getString("day");
				actionBarTitle = "BARS WITH HAPPY HOURS";
			}
			
			//  Create Action Bar
			//createActionBar("Bar Listing", R.layout.custom_actionbar, BarSearchListActivity.this, true);
			createActionBar(actionBarTitle, R.layout.custom_actionbar, BarSearchListActivity.this, true);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);
			
			//  Mapping of all the views
			mappedAllViews();
			
			//  Initialize adapter and array list here.
			barList = new ArrayList<Bar>();
			adapter = new CustomBarAdapter(BarSearchListActivity.this, R.layout.activity_bar_search_list_item, barList);
			listViewBars.setAdapter(adapter);
			
			//  This method is used to set title for the search criteria.
			setTitle();
			
			//  For the Spinner
			bindSpinnerData();
			
			/*//  Now, get the search Bar List here
			if(Utils.getInstance() .isNetworkAvailable(BarSearchListActivity.this)) {
				new AsynTaskGetBarList().execute();
			}
			else{
				Utils.getInstance().showToastNoInternetAvailable(BarSearchListActivity.this);
			}*/
			
			//  Set a listener to be invoked when the list reaches the end
			listViewBars.setOnLoadMoreListener(new OnLoadMoreListener() {
				public void onLoadMore() {
					try {
						isLoadMoreButtonClicked = true;
						if (totalRecords != 0) {
							if (offset < totalRecords && totalRecords>ConfigConstants.Constants.limit) {
								offset = barList.size();
								//  Now, get the search Bar List here
								if(Utils.getInstance() .isNetworkAvailable(BarSearchListActivity.this)) {
									//Log.i("Load More Call", "Load More Call");
									new AsynTaskGetBarList().execute(selectedSearchOption);
								}
								else{
									Utils.getInstance().showToastNoInternetAvailable(BarSearchListActivity.this);
								}
							}
							else {
								//  A method to called when no data is available
								noDataAvailable();
							}
						}
					} 
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			//  Search button click event for the Spinner
			buttonSearch.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					try {
						spinnerSearchOptions.performClick();
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  Spinner for the Search Options
			spinnerSearchOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg, int position, long arg3) {
					try {
						//Log.i("onItemSelected", "onItemSelected");
						
						//  Now, here initialize here offset again as for this we have to load initial data only
						offset = 0;
						isLoadMoreButtonClicked = false;
						
						//  Set the button text value
						buttonSearch.setText(searchOptionsList.get(position).getTitle());
						
						//  To get the value
						selectedSearchOption = searchOptionsList.get(position).getValue();
						
						//  Now, get the search Bar List here
						if(Utils.getInstance() .isNetworkAvailable(BarSearchListActivity.this)) {
							//Log.i("Spinner Call", "Spinner Call");
							new AsynTaskGetBarList().execute(selectedSearchOption);
						}
						else{
							Utils.getInstance().showToastNoInternetAvailable(BarSearchListActivity.this);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
			
			//  ListView row click event
			listViewBars.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					try {
						//  Now, redirect to the Full Mug Bar or Half Mug Bar Details Screen
						redirectFullHalfMugBarDetailsActivity(BarSearchListActivity.this, barList.get(position).getId(), barList.get(position).getType());
					} 
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			buttonSuggestNewBar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						redirectSuggestNewBarActivity(BarSearchListActivity.this);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//  This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			listViewBars = (LoadMoreListView) findViewById(R.id.listViewBars);
			tvSearchTitle = (TextView)findViewById(R.id.tvSearchTitle);
			buttonSearch = (Button)findViewById(R.id.btnSearch);
			spinnerSearchOptions = (Spinner)findViewById(R.id.spinnerSearchOptions);
			buttonSuggestNewBar = (Button) findViewById(R.id.buttonSuggestNewBar);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//  This method is used to set title for the search criteria.
	private void setTitle() {
		try {
			if(!title.isEmpty()) {
				searchText += title;
			}
			if(!state.isEmpty()) {
				if(!searchText.isEmpty()) {
					searchText += ", "+state;
				}
				else {
					searchText += state;
				}
			}
			if(!city.isEmpty()) {
				if(!searchText.isEmpty()) {
					searchText += ", "+city;
				}
				else {
					searchText += city;
				}
			}
			if(!zipcode.isEmpty()) {
				if(!searchText.isEmpty()) {
					searchText += ", "+zipcode;
				}
				else {
					searchText += zipcode;
				}
			}
			tvSearchTitle.setText("Search Result for "+searchText);
			/*title, state, city, zipcode*/
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to get the Bar List based on the entered criteria
	public class AsynTaskGetBarList extends AsyncTask<String, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(BarSearchListActivity.this);
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
				if (isRedirectedFrom.equals(ConfigConstants.Constants.CONSTANT_FIRST)) {
					nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(BarSearchListActivity.this).getData(SessionManager.KEY_USER_ID)));
					nameValuePairs.add(new BasicNameValuePair("device_id",SessionManager.getInstance(BarSearchListActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
					nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(BarSearchListActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
					nameValuePairs.add(new BasicNameValuePair("limit", ""+ ConfigConstants.Constants.limit));
					nameValuePairs.add(new BasicNameValuePair("offset", ""+ offset));
					nameValuePairs.add(new BasicNameValuePair("title",title));
					nameValuePairs.add(new BasicNameValuePair("state",state));
					nameValuePairs.add(new BasicNameValuePair("city", city));
					nameValuePairs.add(new BasicNameValuePair("zipcode",zipcode));
					nameValuePairs.add(new BasicNameValuePair("order_by",params[0]));
				}  
				else {
					nameValuePairs.add(new BasicNameValuePair("user_id",SessionManager.getInstance(BarSearchListActivity.this).getData(	SessionManager.KEY_USER_ID)));
						nameValuePairs.add(new BasicNameValuePair("device_id",
								SessionManager.getInstance(
										BarSearchListActivity.this).getData(
										SessionManager.KEY_DEVICE_ID)));
						nameValuePairs.add(new BasicNameValuePair("unique_code",
								SessionManager.getInstance(
										BarSearchListActivity.this).getData(
										SessionManager.KEY_UNIQUE_CODE)));
						nameValuePairs.add(new BasicNameValuePair("limit", ""
								+ ConfigConstants.Constants.limit));
						nameValuePairs.add(new BasicNameValuePair("offset", ""
								+ offset));
						nameValuePairs.add(new BasicNameValuePair("title", title));
						nameValuePairs.add(new BasicNameValuePair("address_j",
								address));
						nameValuePairs.add(new BasicNameValuePair("days", day));
						nameValuePairs.add(new BasicNameValuePair("order_by",
								params[0]));
						nameValuePairs.add(new BasicNameValuePair("state", ""));
						nameValuePairs.add(new BasicNameValuePair("city", ""));
						nameValuePairs.add(new BasicNameValuePair("zipcode", ""));
						nameValuePairs.add(new BasicNameValuePair("lat", ""));
						nameValuePairs.add(new BasicNameValuePair("lang", ""));
					}
				responseString = sh.makeServiceCall(ConfigConstants.Urls.bar_lists,ServiceHandler.POST, nameValuePairs);
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
				listViewBars.onLoadMoreComplete();

				JSONObject jsonObjParent = null;
				if(responseString!=null){
					jsonObjParent = new JSONObject(responseString);
					status = jsonObjParent.getString("status");
					//Log.i("status",status);
				}
				
				if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)){
					
					totalRecords = jsonObjParent.getInt("barlist_total");
					
					//  Now, check whether data exists or not
					if(totalRecords>0) {
						if (!isLoadMoreButtonClicked) {
							barList.clear();
						}
						
						JSONObject jsonObjectBarList = jsonObjParent.getJSONObject("barlist");
						
						JSONArray jsonArrayResult = jsonObjectBarList.getJSONArray("result");
						
						for (int i = 0; i < jsonArrayResult.length(); i++) {
							JSONObject jsonObj = jsonArrayResult.getJSONObject(i);
							barList.add(new Bar(jsonObj.getString("bar_id"),
									jsonObj.getString("bar_title"), jsonObj
											.getString("bar_type"), jsonObj
											.getString("bar_desc"), jsonObj
											.getString("owner_id"), jsonObj
											.getString("address"), jsonObj
											.getString("city"), jsonObj
											.getString("state"), jsonObj
											.getString("phone"), jsonObj
											.getString("zipcode"), jsonObj
											.getString("email"), jsonObj
											.getString("bar_logo"), jsonObj
											.getString("total_rating"), jsonObj
											.getString("total_commnets")));
						}
					
						//  Make ListView visible here.
						listViewBars.setVisibility(View.VISIBLE);
						//  notifying list adapter about data changes so that it renders the list view with updated data
						adapter.notifyDataSetChanged();
						
						//  Scroll the ListView at the top
						if(!isLoadMoreButtonClicked) {
							listViewBars.setSelection(0);
						}
						// Call onLoadMoreComplete when the LoadMore task, has finished
						((LoadMoreListView) listViewBars).onLoadMoreComplete();
						buttonSuggestNewBar.setVisibility(View.GONE);
					}
					else {
						//  A method to called when no data is available
						noDataAvailable();
						buttonSuggestNewBar.setVisibility(View.VISIBLE);
					}	
				}
				else {
					//  A method to called when no data is available
					noDataAvailable();
					buttonSuggestNewBar.setVisibility(View.VISIBLE);
				}
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}
			if(this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}	
		
		@Override
		protected void onCancelled() {
			try {
				// Notify the loading more operation has finished
				((LoadMoreListView) listViewBars).onLoadMoreComplete();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
	
	//  To bind the spinner data
	private void bindSpinnerData() {
		try {
			searchOptionsList = new ArrayList<SearchOptions>();
			searchOptionsList.add(new SearchOptions(ConfigConstants.Constants.SORT_BY, ConfigConstants.Constants.BLANK_TEXT));
			searchOptionsList.add(new SearchOptions(ConfigConstants.Constants.BAR_NAME_A_Z, "bar_title#ASC"));
			searchOptionsList.add(new SearchOptions(ConfigConstants.Constants.BAR_NAME_Z_A, "bar_title#DESC"));
			searchOptionsList.add(new SearchOptions(ConfigConstants.Constants.CITY_A_Z, "city#ASC"));
			searchOptionsList.add(new SearchOptions(ConfigConstants.Constants.CITY_Z_A, "city#DESC"));
			searchOptionsList.add(new SearchOptions(ConfigConstants.Constants.STATE_A_Z, "state#ASC"));
			searchOptionsList.add(new SearchOptions(ConfigConstants.Constants.STATE_Z_A, "state#DESC"));
			
			String[] tempSearchOptionsArray = new String[searchOptionsList.size()];
			for(int i=0; i<searchOptionsList.size(); i++) {
				tempSearchOptionsArray[i] = searchOptionsList.get(i).getTitle();
			}
			
			//  Initialize and bind the Spinner adapter here
			adapterSearchOptions = new ArrayAdapter<String>(BarSearchListActivity.this, android.R.layout.simple_list_item_1, tempSearchOptionsArray);
			adapterSearchOptions.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
			spinnerSearchOptions.setAdapter(adapterSearchOptions);
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
				listViewBars.hideFooterText();
				/*Toast.makeText(BarSearchListActivity.this,
						ConfigConstants.getInstance().noMoreRecordFound,
						Toast.LENGTH_SHORT).show();*/
				Utils.showToastLong(BarSearchListActivity.this, ConfigConstants.Messages.noMoreRecordFound);
			} else {
				listViewBars.hideFooterText();
				listViewBars.setVisibility(View.GONE);
				/*Toast.makeText(BarSearchListActivity.this,
						ConfigConstants.getInstance().noRecordFound,
						Toast.LENGTH_SHORT).show();*/
				Utils.showToastLong(BarSearchListActivity.this, ConfigConstants.Messages.noRecordFound);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
