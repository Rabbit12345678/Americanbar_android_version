package com.spaculus.americanbars;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
//import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class TaxiDetailsActivity extends BaseActivity {

	//  To get the selected taxi id
	private String taxi_id = "";
	
	/* Header Views */
	private ImageView ivTaxiLogo;
	private TextView tvCompanyName, tvCompanyAddress, tvCompanyWebsiteAddress, tvCompanyMobileNumber, tvOwnerName, tvOwnerMobileNumber, tvAboutCompany;
	/* Header Views */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_taxi_details);
			
			// Create ActionBar
			createActionBar("Taxi Details", R.layout.custom_actionbar, TaxiDetailsActivity.this, true);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);
			
			// Mapping of all the views
			mappedAllViews();
			
			//  To get the bar id
			Bundle b = getIntent().getExtras();
			taxi_id = b.getString(ConfigConstants.Keys.KEY_TAXI_ID);
			
			//  Now, first of all get the taxi details
			if(Utils.getInstance() .isNetworkAvailable(TaxiDetailsActivity.this)) {
				new AsynTaskGetTaxiDetails().execute();
			}
			else{
				Utils.getInstance().showToastNoInternetAvailable(TaxiDetailsActivity.this);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		//  Click on a phone number to do a call
		tvCompanyMobileNumber.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					redirectToPhoneApplication(tvCompanyMobileNumber.getText().toString().trim());
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
			
		//  Click on a phone number to do a call
		tvOwnerMobileNumber.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					redirectToPhoneApplication(tvOwnerMobileNumber.getText().toString().trim());
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//  Click on Address
		tvCompanyAddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					redirectGoogleMapApplicationDrawPath(tvCompanyAddress.getText().toString().trim());
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
			/* Header Views */
			ivTaxiLogo = (ImageView)findViewById(R.id.ivTaxiLogo);
			tvCompanyName = (TextView)findViewById(R.id.tvCompanyName);
			tvCompanyAddress = (TextView)findViewById(R.id.tvCompanyAddress);
			tvCompanyWebsiteAddress = (TextView)findViewById(R.id.tvCompanyWebsiteAddress);
			tvCompanyMobileNumber = (TextView)findViewById(R.id.tvCompanyMobileNumber);
			tvOwnerName = (TextView)findViewById(R.id.tvOwnerName);
			tvOwnerMobileNumber = (TextView)findViewById(R.id.tvOwnerMobileNumber);
			tvAboutCompany = (TextView)findViewById(R.id.tvAboutCompany);
			/* Header Views */
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to get the Taxi Details
	public class AsynTaskGetTaxiDetails extends AsyncTask<String, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(TaxiDetailsActivity.this);
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
	        	nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(TaxiDetailsActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(TaxiDetailsActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(TaxiDetailsActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("limit", "3"));
		        nameValuePairs.add(new BasicNameValuePair("offset", "0"));
		        nameValuePairs.add(new BasicNameValuePair("taxi_id", taxi_id));
		        			
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(ConfigConstants.Urls.taxi_details, ServiceHandler.POST, nameValuePairs);
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
					/* Header Views */
					if(jsonObjParent.has("taxi_detail")) {
						setHeaderViews( jsonObjParent.getJSONObject("taxi_detail"));
					}
					/* Header Views */
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
	
	//  A method is used to get and set the header views data
	private void setHeaderViews(JSONObject jsonObjectBarDetails) {
		try {
			JSONArray jsonArrayResult = jsonObjectBarDetails.getJSONArray("result");
			JSONObject jsonObj = jsonArrayResult.getJSONObject(0);
			
			/* Header Views */
			//  Company Name
			tvCompanyName.setText(Utils.getInstance().isTagExists(jsonObj, "taxi_company"));
			//  Company Address
			tvCompanyAddress.setText(Utils.getInstance().setAddress(Utils.getInstance().isTagExists(jsonObj, "address"), Utils.getInstance().isTagExists(jsonObj, "city"), Utils.getInstance().isTagExists(jsonObj, "state"), Utils.getInstance().isTagExists(jsonObj, "cmpn_zipcode")));
			//  Company Website Address
			tvCompanyWebsiteAddress.setText(Utils.getInstance().isTagExists(jsonObj, "cmpn_website"));
			//  Company Mobile Number
			tvCompanyMobileNumber.setText(Utils.getInstance().isTagExists(jsonObj, "phone_number"));
			
			
			//  Now, if first_name and last_name both are null then we have to show ADB as the default owner name.
			//  And if Owner mobile number is null then we have to show it as empty.
			String first_name = Utils.getInstance().isTagExists(jsonObj, "first_name");
			String last_name = Utils.getInstance().isTagExists(jsonObj, "last_name");
			String owner_name = "";
			String owner_mobile_number = Utils.getInstance().isTagExists(jsonObj, "mobile_no");
			if(first_name.equals("null") || last_name.equals("null")) {
				owner_name = ConfigConstants.Constants.ADB;
			}
			else {
				owner_name = Utils.getInstance().setCapitalLetter(first_name+" "+last_name);
			}
			if(owner_mobile_number.equals("null")) {
				owner_mobile_number = "";
			}
			else {
				owner_mobile_number = Utils.getInstance().isTagExists(jsonObj, "mobile_no");
			}
			//  Owner Name
			tvOwnerName.setText(owner_name);
			//  Owner Mobile Number
			tvOwnerMobileNumber.setText(owner_mobile_number);
			//  About Company
			tvAboutCompany.setText(Utils.getInstance().isTagExists(jsonObj, "taxi_desc"));
			//  Taxi Logo
			setLogo(ConfigConstants.ImageUrls.user_thumb_240, Utils.getInstance().isTagExists(jsonObj, "taxi_image"), ivTaxiLogo, R.drawable.no_image_taxi);
			/* Header Views */
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
