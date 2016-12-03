package com.spaculus.americanbars;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.HTTPOST;
import com.spaculus.helpers.Utils;

public class ContactUsViewMapActivity extends BaseActivity {

	/* Google Map */
	private GoogleMap googleMap;
	/* Google Map */
	
	//  To get an address value
	private String address = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_contact_us_view_map);
		
		//  To get the event id and name
		Bundle b = getIntent().getExtras();
		address = b.getString("address");
		//address = "249 East Ocean Blvd., Suite 670 Long Beach, California 90802";
		//Log.i("address", ""+address);
		
		// Mapping of all the views
		mappedAllViews();
		
		// Create ActionBar
		createActionBar("Our Location", R.layout.custom_actionbar, ContactUsViewMapActivity.this, true);
		//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
		setActionBarFromChild(true, true, false, true, true);
		
		/* Show Map */
		if(address.isEmpty()) {
			//  Then pass default USA's lat and long.
			showMap(googleMap, ConfigConstants.Constants.LATITUDE, ConfigConstants.Constants.LONGITUDE, "Our Location", ConfigConstants.Constants.USA);
		}
		else {
			//  Now, display map after getting lat and long from the address
			if (Utils.getInstance().isNetworkAvailable(ContactUsViewMapActivity.this)) {
				new AsynTaskGetLatLong().execute();
			} 
			else {
				Toast.makeText(getApplicationContext(), "No Internet Available",Toast.LENGTH_LONG).show();
			}
		}
		/* Show Map */
	}
	
	// This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			/* Google Map */
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			googleMap.getUiSettings().setZoomControlsEnabled(true);
			/* Google Map */
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to get the Latitude and Longitude from the address
	public class AsynTaskGetLatLong extends AsyncTask<Void, Void, Void> {
		private ProgressDialog pd = new ProgressDialog(ContactUsViewMapActivity.this);
    	private String responseString = "";
    	private String status = "";
    	
    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				pd.setMessage("Loading...");
				pd.setCanceledOnTouchOutside(false); 
				pd.setCancelable(false);
				pd.show();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}		

		@Override
		protected Void doInBackground(Void... params) {
	        try {
	        	 // Prepare a request object
	    	    HTTPOST httpost = new HTTPOST();
	    	    responseString = httpost.getResponseByXML("http://maps.google.com/maps/api/geocode/json?address="+address.replaceAll(" ", "%20")+"&sensor=false", "");
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
				if(this.pd.isShowing()) {
					this.pd.dismiss();
				}
				if(responseString!=null){
					JSONObject jsonObjParent = new JSONObject(responseString);
					status = jsonObjParent.getString("status");
					//Log.i("status",status);	
					
					if(status.equals("OK")){
						double latitude = ((JSONArray)jsonObjParent.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location") .getDouble("lat");
				    	double longitude = ((JSONArray)jsonObjParent.get("results")).getJSONObject(0) .getJSONObject("geometry").getJSONObject("location").getDouble("lng");
					    //Log.d("latitude", ""+latitude);
					    //Log.d("longitude", ""+longitude);
					    showMap(googleMap, latitude, longitude, "Our Location", address);
					}
					else{
						//  Set the default latitude and longitude for the USA
						showMap(googleMap, ConfigConstants.Constants.LATITUDE, ConfigConstants.Constants.LONGITUDE, "Our Location", ConfigConstants.Constants.USA);
					}
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
    }	
}
