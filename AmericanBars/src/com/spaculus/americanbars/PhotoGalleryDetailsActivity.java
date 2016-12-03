package com.spaculus.americanbars;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class PhotoGalleryDetailsActivity extends BaseActivityGallery {
	
	//  To get the selected gallery_id and gallery_name
	private String bar_gallery_id = "", gallery_name = "";
	
	/*To get the method name as this screen is used as Photo Gallery Details Screen and Album Details Screen.*/
	String web_service_method_name = "";
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_photo_gallery_details);
			
			//  To get the bar id
			Bundle b = getIntent().getExtras();
			bar_gallery_id = b.getString(ConfigConstants.Keys.KEY_BAR_GALLERY_ID);
			gallery_name = b.getString(ConfigConstants.Keys.KEY_GALLERY_NAME);
			web_service_method_name = b.getString(ConfigConstants.Keys.KEY_WEB_SERVICE_METHOD_NAME);
			
			// Create ActionBar
			createActionBar(gallery_name, R.layout.custom_actionbar, PhotoGalleryDetailsActivity.this, false);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);
			
			// Mapping of all the views
			mappedAllViews();
			
			//  For the Spinner
			bindSpinnerData(spinnerShare);
			
			//  Now, first of all get the Photo Gallery Details
			if(Utils.getInstance() .isNetworkAvailable(PhotoGalleryDetailsActivity.this)) {
				new AsynTaskGetPhotoGalleryDetails().execute();
			}
			else{
				Utils.getInstance().showToastNoInternetAvailable(PhotoGalleryDetailsActivity.this);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			// TODO Auto-generated method stub
			/* Gallery */
			tvGalleryName = (TextView) findViewById(R.id.tvGalleryName);
			ivShareImage = (ImageView)findViewById(R.id.ivShareImage);
			spinnerShare = (Spinner)findViewById(R.id.spinnerShare);
			mappedGalleryViews(ConfigConstants.ImageUrls.bar_gallery_thumb_big200by200, ConfigConstants.ImageUrls.bar_gallery_thumb_big600by600, ConfigConstants.ImageUrls.galleyOriginalImageURL, ConfigConstants.Constants.CONSTANT_YES);
			/* Gallery */
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to get the Photo Gallery Details
	public class AsynTaskGetPhotoGalleryDetails extends AsyncTask<String, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(PhotoGalleryDetailsActivity.this);
    	private String responseString = "";
    	private String status = "";
    	
    	@Override
		protected void onPreExecute() {
			try {
				// TODO Auto-generated method stub
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
	        	nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(PhotoGalleryDetailsActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(PhotoGalleryDetailsActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(PhotoGalleryDetailsActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("bar_gallery_id", bar_gallery_id));
		        			
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(web_service_method_name, ServiceHandler.POST, nameValuePairs);
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
					
					/* Gallery */
					if(jsonObjParent.has("gal")) {
						setGalleryWithTitleJSONArray( jsonObjParent.getJSONArray("gal"), gallery_name);
					}
					/* Gallery */
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
}
