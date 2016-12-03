package com.spaculus.americanbars;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spaculus.adapters.CustomAlphabeticGridViewAdapter;
import com.spaculus.adapters.CustomBarEventAdapter;
import com.spaculus.beans.Alphabet;
import com.spaculus.beans.BarEvent;
import com.spaculus.beans.SearchOptions;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.LoadMoreListView;
import com.spaculus.helpers.ParseJsonObject;
import com.spaculus.helpers.LoadMoreListView.OnLoadMoreListener;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
//import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class BarEventSearchListActivity extends BaseActivity{

	private LoadMoreListView listViewBarEvents;
	
	private TextView tvSearchTitle;
	private Button buttonSearch;
	private Spinner spinnerSearchOptions;
	
	private EditText etSearch;
	private ImageView ivSearch, ivAlphabates;
	
	//  For the Alphabetic drawer functionality
	private DrawerLayout mDrawerLayout;
	private GridView mDrawerList;

	//  For the Bar List
	private ArrayList<BarEvent> barEventsList = null;
 	private CustomBarEventAdapter adapter = null;
 	
 	//  For the Search Options
 	private ArrayList<SearchOptions> searchOptionsList = null;
 	private ArrayAdapter<String> adapterSearchOptions = null;
 	private String selectedSearchOption = "";
	
	//  For the Web service response
	//  Implement Paging functionality to get the people list
	//  current_page = page_number = offset(in web service method)
	private int offset = 0; 
	private int totalRecords = 0;
	private boolean isLoadMoreButtonClicked = false;
	
	//  For the Search functionality
	//private String selectedSearchText = "";
	
	//  For the Alphabetic functionality
	private String selectedAlphabet = "";
	
	private String[] numbers = new String[] { 
		"#", "1", "A", "B", "C", "D", "E",
		"F", "G", "H", "I", "J",
		"K", "L", "M", "N", "O",
		"P", "Q", "R", "S", "T",
		"U", "V", "W", "X", "Y", "Z"};
	
	private ArrayList<Alphabet> gridList = null;
	private CustomAlphabeticGridViewAdapter gridAdapter = null;
	
	//  To get the bar_id as if user is redirected from the bar details screen
	private String bar_id = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_bar_event_search_list);
			
			//  Create Action Bar
			createActionBar("Bar Events Listing", R.layout.custom_actionbar, BarEventSearchListActivity.this, true);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);
			
			//  Mapping of all the views
			mappedAllViews();
			
			//  To get the bar id
			Bundle b = getIntent().getExtras();
			bar_id = b.getString(ConfigConstants.Keys.KEY_BAR_ID);
			
			//  Initialize adapter and array list here.
			barEventsList = new ArrayList<BarEvent>();
			adapter = new CustomBarEventAdapter(BarEventSearchListActivity.this, R.layout.activity_bar_details_bar_events_list_item, barEventsList);
			listViewBarEvents.setAdapter(adapter);
			
			//  For the Spinner
			bindSpinnerData();
			
			//  Set a listener to be invoked when the list reaches the end
			listViewBarEvents.setOnLoadMoreListener(new OnLoadMoreListener() {
				public void onLoadMore() {
					try {
						isLoadMoreButtonClicked = true;
						if (totalRecords != 0) {
							if (offset < totalRecords && totalRecords>ConfigConstants.Constants.limit) {
								offset = barEventsList.size();
								//  Now, get the search Bar List here
								if(Utils.getInstance() .isNetworkAvailable(BarEventSearchListActivity.this)) {
									//Log.i("Load More Call", "Load More Call");
									new AsynTaskGetBarEventsList().execute(selectedSearchOption);
								}
								else{
									Utils.getInstance().showToastNoInternetAvailable(BarEventSearchListActivity.this);
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
						
						//  Set the button text value
						buttonSearch.setText(searchOptionsList.get(position).getTitle());
						
						//  To get the value
						selectedSearchOption = searchOptionsList.get(position).getValue();
						
						//  Now, call the web service to get the data again
						loadData();
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
			
			//  Search icon click event
			ivSearch.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					try {
						//  Now, here first of all hide the soft keyboard
						hideSoftKeyboard();
						
						//  Set the title text
						tvSearchTitle.setText("Search Result for "+etSearch.getText().toString().trim());
						
						//  Now, clear GridView selection
						clearGridViewSelection();
						gridAdapter.notifyDataSetChanged();
						
						//  Now, here initialize here offset again as for this we have to load initial data only
						offset = 0;
						isLoadMoreButtonClicked = false;
						
						//  Now, get the search Bar List here
						if(Utils.getInstance().isNetworkAvailable(BarEventSearchListActivity.this)) {
							//Log.i("Spinner Call", "Spinner Call");
							new AsynTaskGetBarEventsList().execute(selectedSearchOption);
						}
						else{
							Utils.getInstance().showToastNoInternetAvailable(BarEventSearchListActivity.this);
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  Alphabets icon click event
			ivAlphabates.setOnClickListener(new OnClickListener() {
				
				@SuppressLint("RtlHardcoded") 
				@Override
				public void onClick(View arg0) {
					try {
						if(mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
							mDrawerLayout.closeDrawer(Gravity.RIGHT);
						}
						else {
							mDrawerLayout.openDrawer(Gravity.RIGHT);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  ListView row click event
			listViewBarEvents.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					try {
						redirectBarEventDetailsActivity(BarEventSearchListActivity.this, barEventsList.get(position).getId(), barEventsList.get(position).getTitle());
					} 
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			mDrawerList.setOnItemClickListener(new OnItemClickListener() {
				@SuppressLint("RtlHardcoded") 
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					try {
						//  Now, clear the search text and title text both
						etSearch.setText("");
						tvSearchTitle.setText("");
						
						//  Now, clear GridView selection
						clearGridViewSelection();
						
						//  And now set the state	
						gridList.get(position).setSelected(true);	
						gridAdapter.notifyDataSetChanged();
						
						//  And now close the drawer
						mDrawerLayout.closeDrawer(Gravity.RIGHT);
						
						//  Now, first of all get the selected Alphabet
						selectedAlphabet  = gridList.get(position).getName();
						if(selectedAlphabet.equals("#")) {
							selectedAlphabet = "'-#";
						}
						else if(selectedAlphabet.equals("1")) {
							selectedAlphabet = "0-9";
						}
						
						//  Now, call the web service to get the data again
						loadData();
					} 
					catch (Exception e) {
						// TODO Auto-generated catch block
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
			// TODO Auto-generated method stub
			listViewBarEvents = (LoadMoreListView) findViewById(R.id.listViewBarEvents);
			tvSearchTitle = (TextView)findViewById(R.id.tvSearchTitle);
			buttonSearch = (Button)findViewById(R.id.btnSearch);
			spinnerSearchOptions = (Spinner)findViewById(R.id.spinnerSearchOptions);
			etSearch = (EditText)findViewById(R.id.etSearch);
			ivSearch = (ImageView)findViewById(R.id.ivSearch);
			ivAlphabates = (ImageView)findViewById(R.id.ivAlphabates);
			
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			mDrawerList = (GridView) findViewById(R.id.list_slidermenu);
			
			//  Generate the Alphabet list here.
			gridList = new ArrayList<Alphabet>();
			for (String s : numbers) {
			    gridList.add(new Alphabet(s, false));
			}
			gridAdapter = new CustomAlphabeticGridViewAdapter(BarEventSearchListActivity.this, R.layout.grid_list_item_alphabetic, gridList);
			mDrawerList.setAdapter(gridAdapter);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//  A method to load the data initially
	private void loadData() {
		try {
			//  Now, here initialize here offset again as for this we have to load initial data only
			offset = 0;
			isLoadMoreButtonClicked = false;
			//  Now, get the search Bar List here
			if(Utils.getInstance().isNetworkAvailable(BarEventSearchListActivity.this)) {
				//Log.i("Spinner Call", "Spinner Call");
				new AsynTaskGetBarEventsList().execute(selectedSearchOption);
			}
			else{
				Utils.getInstance().showToastNoInternetAvailable(BarEventSearchListActivity.this);
			}
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to get the Bar List based on the entered criteria
	public class AsynTaskGetBarEventsList extends AsyncTask<String, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(BarEventSearchListActivity.this);
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
	        	nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(BarEventSearchListActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(BarEventSearchListActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(BarEventSearchListActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("limit", ""+ConfigConstants.Constants.limit));
		        nameValuePairs.add(new BasicNameValuePair("offset", ""+offset));
		        nameValuePairs.add(new BasicNameValuePair("keyword", etSearch.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("alpha", selectedAlphabet));
		        nameValuePairs.add(new BasicNameValuePair("order_by", params[0]));
		        nameValuePairs.add(new BasicNameValuePair("bar_id", bar_id));
		        
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(ConfigConstants.Urls.getAllEvent, ServiceHandler.POST, nameValuePairs);
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
				
				listViewBarEvents.onLoadMoreComplete();
				
				try {
					JSONObject jsonObjParent = null;
					if(responseString!=null){
						jsonObjParent = new JSONObject(responseString);
						status = jsonObjParent.getString("status");
						//Log.i("status",status);
					}
					
					if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)){
						
						totalRecords = jsonObjParent.getInt("eventlist_total");
						
						//  Now, check whether data exists or not
						if(totalRecords>0) {
							if (!isLoadMoreButtonClicked) {
								barEventsList.clear();
							}
							
							JSONObject jsonObjectbarEventsList = jsonObjParent.getJSONObject("eventlist");
							JSONArray jsonArrayResult = jsonObjectbarEventsList.getJSONArray("result");
							
							for (int i = 0; i < jsonArrayResult.length(); i++) {
								JSONObject jsonObj = jsonArrayResult.getJSONObject(i);
								barEventsList.add(new ParseJsonObject().addBarEventObject(jsonObj));
								/*barEventsList.add(new BarEvent(Utils.getInstance().isTagExists(jsonObj, "event_id"), Utils.getInstance().isTagExists(jsonObj, "event_title"), Utils.getInstance().isTagExists(jsonObj, "event_desc"), 
										Utils.getInstance().isTagExists(jsonObj, "start_date"), Utils.getInstance().isTagExists(jsonObj, "event_image")));*/
							}
						
							//  Make ListView visible here.
							listViewBarEvents.setVisibility(View.VISIBLE);
							//  notifying list adapter about data changes so that it renders the list view with updated data
							adapter.notifyDataSetChanged();
							
							//  Scroll the ListView at the top
							if(!isLoadMoreButtonClicked) {
								listViewBarEvents.setSelection(0);
							}
							
							// Call onLoadMoreComplete when the LoadMore task, has finished
							((LoadMoreListView) listViewBarEvents).onLoadMoreComplete();
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
				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
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
				((LoadMoreListView) listViewBarEvents).onLoadMoreComplete();
			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
	
	//  To bind the spinner data
	private void bindSpinnerData() {
		try {
			searchOptionsList = new ArrayList<SearchOptions>();
			searchOptionsList.add(new SearchOptions("Name ASC", "event_title#ASC"));
			searchOptionsList.add(new SearchOptions("Name DESC", "event_title#DESC"));
			
			String[] tempSearchOptionsArray = new String[searchOptionsList.size()];
			for(int i=0; i<searchOptionsList.size(); i++) {
				tempSearchOptionsArray[i] = searchOptionsList.get(i).getTitle();
			}
			//  Initialize and bind the Spinner adapter here
			adapterSearchOptions = new ArrayAdapter<String>(BarEventSearchListActivity.this, android.R.layout.simple_list_item_1, tempSearchOptionsArray);
			adapterSearchOptions.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
			spinnerSearchOptions.setAdapter(adapterSearchOptions);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//  Hide the soft key board on the click of Layout
	private void hideSoftKeyboard(){
		try {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//  To clear the GridView Selection
	private void clearGridViewSelection() {
		try {
			//  Make selected alpha clear
			selectedAlphabet = "";
			//  Now, first of all clear all the states
			for(int i=0; i<gridList.size(); i++) {
				gridList.get(i).setSelected(false);	
			}
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//  A method to called when no data is available
	private void noDataAvailable() {
		try {
			if (isLoadMoreButtonClicked) {
				isLoadMoreButtonClicked = false;
				listViewBarEvents.hideFooterText();
				//Toast.makeText(BarEventSearchListActivity.this, ConfigConstants.getInstance().noMoreRecordFound, Toast.LENGTH_SHORT).show();
				Utils.showToastLong(BarEventSearchListActivity.this, ConfigConstants.Messages.noMoreRecordFound);
			} 
			else {
				listViewBarEvents.hideFooterText();
				listViewBarEvents.setVisibility(View.GONE);
				//Toast.makeText(BarEventSearchListActivity.this, ConfigConstants.getInstance().noRecordFound, Toast.LENGTH_SHORT).show();
				Utils.showToastLong(BarEventSearchListActivity.this, ConfigConstants.Messages.noRecordFound);
			}
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
