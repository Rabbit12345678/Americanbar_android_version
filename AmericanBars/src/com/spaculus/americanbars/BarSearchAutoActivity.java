package com.spaculus.americanbars;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.spaculus.adapters.CustomAutoSuggestAdapter;
import com.spaculus.beans.Bar;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.ParseJsonObject;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.Utils;
//import android.util.Log;

public class BarSearchAutoActivity extends BaseActivity{

	private TextView tvCancel, tvDone;
	private EditText etSearchHere;
	private ListView listViewBars;
	
	private ArrayList<Bar> barList = null;
 	private CustomAutoSuggestAdapter adapter = null;
 	
 	private AsynTaskAutoBarSearch objAsynTaskAutoBarSearch = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_bar_search_auto);

			// Mapping of all the views
			mappedAllViews();		
			
			//  Get the Search Bar Text data here.
			Bundle bundle = getIntent().getExtras();
			String searchText = bundle.getString(ConfigConstants.Keys.KEY_SEARCH_TEXT);
			etSearchHere.setText(searchText);
			
			//  To set the cursor position
			etSearchHere.setSelection(etSearchHere.getText().length());
			
			//  Initialize adapter and array list here.
			barList = new ArrayList<Bar>();
			adapter = new CustomAutoSuggestAdapter(BarSearchAutoActivity.this, R.layout.auto_suggest_list_item, barList);
			listViewBars.setAdapter(adapter);
			
			/* Now, here first of all initialize the AsyncTask's object */			
			objAsynTaskAutoBarSearch = new AsynTaskAutoBarSearch();
			
			//  Now, here get the Auto Search Bar List initially if user has already selected any bar
			if(!searchText.isEmpty()) {
				//  Now, get the search Bar List here
				if(Utils.getInstance() .isNetworkAvailable(BarSearchAutoActivity.this)) {
					/*new AsynTaskAutoBarSearch().execute();*/
					objAsynTaskAutoBarSearch.cancel(true);
					objAsynTaskAutoBarSearch = new AsynTaskAutoBarSearch();
					objAsynTaskAutoBarSearch.execute();
				}
				else{
					Utils.getInstance().showToastNoInternetAvailable(BarSearchAutoActivity.this);
				}
			}
			
			//  For the Search Filter (For the Bar name Functionality)
			etSearchHere.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					try {
							//  Now, first of all check whether length>0 or not. i.e. whether user has entered any text to search or not.
							if(!s.toString().isEmpty()){
								
								//  Now, check whether entered character is space or not as if it's space then we have to simply hide the listview
								String lastCharacter = s.toString().substring(s.toString().length()-1, s.toString().length());
								//  Means Space
								if(lastCharacter.equals(" ")){
									listViewBars.setVisibility(View.GONE);
								}
								//  Means not a Space
								else {
									//  Now, get the search Bar List here
									if(Utils.getInstance() .isNetworkAvailable(BarSearchAutoActivity.this)) {
										//new AsynTaskAutoBarSearch().execute();
										objAsynTaskAutoBarSearch.cancel(true);
										objAsynTaskAutoBarSearch = new AsynTaskAutoBarSearch();
										objAsynTaskAutoBarSearch.execute();
									}
									else{
										Utils.getInstance().showToastNoInternetAvailable(BarSearchAutoActivity.this);
									}
								}
							}
							else {
								//  Means user has yet not entered any text so simply do not need to do anything so simply hide the ListView
								listViewBars.setVisibility(View.GONE);
							}
						} 
					catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}
				
				@Override
				public void afterTextChanged(Editable s) {
				}
			});
			
			//  ListView row click event
			listViewBars.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					try {
						//  Now, set the selected text into the EdiText
						etSearchHere.setText(barList.get(position).getTitle());
						setResultCode();
						finish();
						onDestroy();
						//  Now, redirect to the Full Mug Bar or Half Mug Bar Details Screen
						redirectFullHalfMugBarDetailsActivity(BarSearchAutoActivity.this, barList.get(position).getId(), barList.get(position).getType());
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});		
			
			//  Cancel TextView click event
			tvCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					try {
						etSearchHere.setText("");
						setResultCode();
						finish();
						onDestroy();
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  Done TextView click event
			tvDone.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					try {
						setResultCode();
						finish();
						onDestroy();
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
	
	// This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			tvCancel = (TextView) findViewById(R.id.tvCancel);
			tvDone = (TextView) findViewById(R.id.tvDone);
			etSearchHere = (EditText) findViewById(R.id.etSearchHere);
			listViewBars = (ListView) findViewById(R.id.listViewBars);
		
			etSearchHere.requestFocus();
			showSoftKeyboard();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onBackPressed() {
		try {
			setResultCode();
			super.onBackPressed();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  This method is used to finish the Alpha View Activity.
	private void setResultCode() {
		try {
			Intent intent = new Intent();
			intent.putExtra(ConfigConstants.Keys.KEY_SEARCH_TEXT, etSearchHere.getText().toString().trim());
			setResult(ConfigConstants.ResultCodes.REQUEST_CODE_BAR_SEARCH,intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to get the Search Bar List
	public class AsynTaskAutoBarSearch extends AsyncTask<String, Void, String> {
		//private ProgressDialog pd = new ProgressDialog(BarSearchActivity1.this);
    	
    	@Override
		protected void onPreExecute() {
			try {
				super.onPreExecute();
				/*this.pd.setMessage(ConfigConstants.getInstance().loadingMessage);
				pd.setCanceledOnTouchOutside(false); 
				pd.setCancelable(false);
				this.pd.show();*/
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
		        nameValuePairs.add(new BasicNameValuePair("term", etSearchHere.getText().toString().trim()));
		        
	        	// Making a request to URL and getting response
				String responseString = sh.makeServiceCall(ConfigConstants.Urls.auto_suggest_bar, ServiceHandler.POST, nameValuePairs);
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
				/*if(this.pd.isShowing()) {
					this.pd.dismiss();
				}*/
				if(response != null) {
					JSONObject jsonObjParent = new JSONObject(response);
					String status = Utils.getInstance().isTagExists(jsonObjParent, "status");
					status = jsonObjParent.getString("status");
					
					if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)) {
						//  Now, here first of all clear the list
						barList.clear();
						
						JSONArray jsonArray = jsonObjParent.getJSONArray("barlist");
						ParseJsonObject objParseJsonObject = new ParseJsonObject();
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObj = jsonArray.getJSONObject(i);
							barList.add(objParseJsonObject.addBarObject(jsonObj));
						}
					
						//  Now, here hide the soft key board.
						//hideSoftKeyboard();
						
						//  Make ListView visible here.
						listViewBars.setVisibility(View.VISIBLE);
						//  notifying list adapter about data changes so that it renders the list view with updated data
						adapter.notifyDataSetChanged();
					}
					else {
						//  Make ListView hide here.
						listViewBars.setVisibility(View.GONE);
					}
				}
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}
		}		
		
		@Override
	    protected void onCancelled() {
	        super.onCancelled();
	        this.cancel(true);
	    }
    }
	
	//  Hide the soft key board on the click of Layout
	@SuppressWarnings("unused")
	private void hideSoftKeyboard(){
		//Log.i("Method hideSoftKeyboard","Method hideSoftKeyboard");
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(etSearchHere.getWindowToken(), 0);
	}
	
	//  Show the soft key board when EditText get focus
	private void showSoftKeyboard(){
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(etSearchHere, InputMethodManager.SHOW_IMPLICIT);
	}
}
