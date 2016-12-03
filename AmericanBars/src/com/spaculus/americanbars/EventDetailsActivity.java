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
import com.spaculus.beans.BarEventDateTime;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.HTTPOST;
import com.spaculus.helpers.ParseJsonObject;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
//import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EventDetailsActivity extends BaseActivityGallery {
	private TextView tvEventName, tvOrganizedBy, tvAddress, tvEventCategory, tvDescription;
	private LinearLayout hiddenLinearLayoutEventDate;
	
	/* Google Map */
	private GoogleMap googleMap;
	private Button buttonGetDirections;
	private double latitude = 0.0, longitude = 0.0;
	/* Google Map */
	
	//  To get event id and name values
	private String event_id = "", event_name = ""; 
	
	/* For the Event Date - Time functionality */
	private ArrayList<BarEventDateTime> barEventDateTimeList = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_event_details);
			
			//  To get the event id and name
			Bundle b = getIntent().getExtras();
			event_id = b.getString(ConfigConstants.Keys.KEY_EVENT_ID);
			//Log.i("event_id", ""+event_id);
			event_name = b.getString(ConfigConstants.Keys.KEY_EVENT_NAME);
			
			// Create ActionBar
			createActionBar(event_name, R.layout.custom_actionbar, EventDetailsActivity.this, false);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);
			
			// Mapping of all the views
			mappedAllViews();
			
			//  Now, get the event details
			if(Utils.getInstance() .isNetworkAvailable(EventDetailsActivity.this)) {
				new AsynTaskGetBarEventDetails().execute();
			}
			else{
				Utils.getInstance().showToastNoInternetAvailable(EventDetailsActivity.this);
			}
			
			//  Get Directions button click event
			buttonGetDirections.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						/* Redirect to the Google Maps Application */
						redirectToGoogleMapApplicationShowLocation(EventDetailsActivity.this, latitude, longitude, tvOrganizedBy.getText().toString().trim(), tvAddress.getText().toString().trim());
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
		tvAddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					redirectGoogleMapApplicationDrawPath(tvAddress.getText().toString().trim());
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			tvEventName= (TextView)findViewById(R.id.tvEventName);
			tvOrganizedBy= (TextView)findViewById(R.id.tvOrganizedBy);
			tvAddress= (TextView)findViewById(R.id.tvAddress);
			tvEventCategory= (TextView)findViewById(R.id.tvEventCategory);
			tvDescription= (TextView)findViewById(R.id.tvDescription);
			hiddenLinearLayoutEventDate = (LinearLayout)findViewById(R.id.hiddenLinearLayoutEventDate);
			barEventDateTimeList = new ArrayList<BarEventDateTime>();
			
			/* Gallery */
			mappedGalleryViews(ConfigConstants.ImageUrls.galleySmallImageURL_bar_event, ConfigConstants.ImageUrls.galleyBigImageURL_bar_event, ConfigConstants.ImageUrls.galleyOriginalImageURL_bar_event, ConfigConstants.Constants.CONSTANT_NO);
			/* Gallery */

			/* Google Map */
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			googleMap.getUiSettings().setZoomControlsEnabled(true);
			buttonGetDirections = (Button)findViewById(R.id.btnGetDirections);
			/* Google Map */
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to get the Event Details
	public class AsynTaskGetBarEventDetails extends AsyncTask<String, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(EventDetailsActivity.this);
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
		protected Void doInBackground(String... params) {
	        try {
	        	ServiceHandler sh = new ServiceHandler();
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        	nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(EventDetailsActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(EventDetailsActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(EventDetailsActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("limit", "3"));
		        nameValuePairs.add(new BasicNameValuePair("offset", "0"));
		        nameValuePairs.add(new BasicNameValuePair("event_id", event_id));
			
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(ConfigConstants.Urls.event_details, ServiceHandler.POST, nameValuePairs);
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
				if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)) {
					/* Set Event Category */
					tvEventCategory.setText(jsonObjParent.getString("event_cat"));
					/* Set basic event details */
					setBasicEventDetails(jsonObjParent.getJSONObject("event_detail"));
					/* Set Event Date and Time details */
					setEventDateTimeDetails(jsonObjParent.getJSONArray("eventtime"));
					/* Gallery */
					setGalleryWithOutTitle(jsonObjParent.getJSONArray("event_gallery"));
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
	
	//  A method is used to get and set the basic event details data
	private void setBasicEventDetails(JSONObject jsonObjectBarDetails) {
		try {
			/* Now, set the basic data here. */
			tvEventName.setText(Utils.getInstance().isTagExists(jsonObjectBarDetails, "event_title"));
			tvOrganizedBy.setText(Utils.getInstance().isTagExists(jsonObjectBarDetails, "bar_title"));
			tvAddress.setText(Utils.getInstance().setAddress(Utils.getInstance().isTagExists(jsonObjectBarDetails, "address"), Utils.getInstance().isTagExists(jsonObjectBarDetails, "city"), Utils.getInstance().isTagExists(jsonObjectBarDetails, "state"), Utils.getInstance().isTagExists(jsonObjectBarDetails, "zipcode")));
			tvDescription.setText(Utils.getInstance().isTagExists(jsonObjectBarDetails, "event_desc"));
		
			/* Show Map */
			if(tvAddress.getText().toString().trim().isEmpty()) {
				//  Then pass default USA's lat and long.
				showMap(googleMap, ConfigConstants.Constants.LATITUDE, ConfigConstants.Constants.LONGITUDE, tvEventName.getText().toString().trim(), ConfigConstants.Constants.USA);
				latitude = ConfigConstants.Constants.LATITUDE;
				longitude = ConfigConstants.Constants.LONGITUDE;
			}
			else {
				//  Now, display map after getting lat and long from the address
				if (Utils.getInstance().isNetworkAvailable(EventDetailsActivity.this)) {
					new AsynTaskGetLatLong().execute();
				} 
				else {
					Toast.makeText(getApplicationContext(), "No Internet Available",Toast.LENGTH_LONG).show();
				}
			}
			/* Show Map */
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//  A method is used to get and set the Events Date and Time details
	private void setEventDateTimeDetails(JSONArray jsonArrayEventDateTime) {
		try {
			//  Now, here clear the list
			if(barEventDateTimeList.size()>0) {
				barEventDateTimeList.clear();
			}
			for (int i = 0; i < jsonArrayEventDateTime.length(); i++) {
				JSONObject jsonObj = jsonArrayEventDateTime.getJSONObject(i);
				barEventDateTimeList.add(new ParseJsonObject().addBarEventDateTimeObject(jsonObj));
			}
			
			if(hiddenLinearLayoutEventDate.getChildCount() != 0) {
				hiddenLinearLayoutEventDate.removeAllViews();
			}
			if(barEventDateTimeList.size()>0) {
				hiddenLinearLayoutEventDate.setVisibility(View.VISIBLE);
				//  Now, set the Bar Event Date - Time's data
				setEventDateTimeDetailsData();
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * A method is used to create the custom view and add the layout into the
	 * Linear Layout for the Bar Event Date-Time's data
	 */
	@SuppressLint("InflateParams") 
	public void setEventDateTimeDetailsData() {
		try {
			LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			for (int i = 0; i < barEventDateTimeList.size(); i++) {
				final int index = i;
				View customView = inflater.inflate(R.layout.activity_bar_event_date_time_list_item, null);
				
				TextView tvDate = (TextView) customView.findViewById(R.id.tvDate);
				TextView tvTime = (TextView) customView.findViewById(R.id.tvTime);
				
				tvDate.setTag(index);
				tvTime.setTag(index);
				
				/* Now, set the data here */
				tvDate.setText(Utils.getInstance().getFormattedDate(barEventDateTimeList.get(index).getEventdate(), ConfigConstants.DateFormats.YYYY_MM_DD, ConfigConstants.DateFormats.EEEE_MMM_dd_yyyy));
				tvTime.setText(barEventDateTimeList.get(index).getEventstarttime()+" - "+barEventDateTimeList.get(index).getEventendtime());
			
				//  Now, add the custom view to the layout
				hiddenLinearLayoutEventDate.addView(customView,index);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to get the Latitude and Longitude from the address
	public class AsynTaskGetLatLong extends AsyncTask<Void, Void, Void> {
    	
    	//private ProgressDialog pd = new ProgressDialog(EventDetailsActivity.this);
    	private String responseString = "";
    	private String status = "";
    	
    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				/*pd.setMessage("Loading...");
				pd.setCanceledOnTouchOutside(false); 
				pd.setCancelable(false);
				pd.show();*/
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}		

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
						
	        try {
	        	 // Prepare a request object
	    	    HTTPOST httpost = new HTTPOST();
	    	    responseString = httpost.getResponseByXML("http://maps.google.com/maps/api/geocode/json?address="+tvAddress.getText().toString().trim().replaceAll(" ", "%20").replaceAll("\n", "%0A")+"&sensor=false", "");
	    	    //Log.i("Response is: ",responseString);
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
				/*if(this.pd.isShowing()) {
					this.pd.dismiss();
				}*/
				if(responseString!=null){
					JSONObject jsonObjParent = new JSONObject(responseString);
					status = jsonObjParent.getString("status");
					//Log.i("status",status);	
					
					if(status.equals("OK")){
						latitude = ((JSONArray)jsonObjParent.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location") .getDouble("lat");
				    	longitude = ((JSONArray)jsonObjParent.get("results")).getJSONObject(0) .getJSONObject("geometry").getJSONObject("location").getDouble("lng");
					    //Log.d("latitude", ""+latitude);
					    //Log.d("longitude", ""+longitude);
					    showMap(googleMap, latitude, longitude, tvEventName.getText().toString().trim(), tvAddress.getText().toString().trim());
					}
					else{
						//  Set the default latitude and longitude for the USA
						showMap(googleMap, ConfigConstants.Constants.LATITUDE, ConfigConstants.Constants.LONGITUDE, tvEventName.getText().toString().trim(), ConfigConstants.Constants.USA);
					}
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
    }	
}
