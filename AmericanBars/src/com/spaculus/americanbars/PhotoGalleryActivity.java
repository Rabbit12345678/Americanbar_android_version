package com.spaculus.americanbars;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spaculus.adapters.CustomPhotoGalleryAdapter;
import com.spaculus.beans.GalleryBean;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class PhotoGalleryActivity extends BaseActivity {
	
	private GridView gridViewPhotoGallery;
	private ArrayList<GalleryBean> photoGalleryList;
	private CustomPhotoGalleryAdapter adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			//  Now, here first of all check whether Phone or Tablet.
			if(Utils.getInstance().isTablet(PhotoGalleryActivity.this)) {
				//Toast.makeText(PhotoGalleryActivity.this, "Tablet", Toast.LENGTH_SHORT).show();
				
				/* If tablet then check whether width is greater than 650 or not as then and only then load the tablet view o.w. phone view. */
				/* Width considered 650 as GridView width is 600 and padding is 15+15 = 30. */
				if(Utils.getInstance().getDeviceWidth(PhotoGalleryActivity.this) > 650) {
					setContentView(R.layout.activity_photo_gallery_tablet);
				}
				else {
					setContentView(R.layout.activity_photo_gallery_phone);
				}
			}
			else {
				//Toast.makeText(PhotoGalleryActivity.this, "Phone", Toast.LENGTH_SHORT).show();
				setContentView(R.layout.activity_photo_gallery_phone);
			}
			
			// Create ActionBar
			createActionBar(ConfigConstants.Constants.PHOTO_GALLERY, R.layout.custom_actionbar, PhotoGalleryActivity.this, true);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);
			ivSearchBarActionBar.setImageResource(R.drawable.menu_home_action_bar);
			
			// Mapping of all the views
			mappedAllViews();
			
			//  Now, first of all get the Photo Gallery
			if(Utils.getInstance() .isNetworkAvailable(PhotoGalleryActivity.this)) {
				new AsynTaskGetPhotoGallery().execute();
			}
			else{
				Utils.getInstance().showToastNoInternetAvailable(PhotoGalleryActivity.this);
			}
			
			//  Click event of Single GridView Item click
			gridViewPhotoGallery.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					try {
						redirectPhotoGalleryDetailsActivity(PhotoGalleryActivity.this, photoGalleryList.get(position).getBar_gallery_id(), photoGalleryList.get(position).getTitle(), ConfigConstants.Urls.get_gallery_by_id);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	/*@Override
	public void onConfigurationChanged(Configuration newConfig) {
		try {
			super.onConfigurationChanged(newConfig);
			if (Utils.getInstance().isTablet(PhotoGalleryActivity.this)) {
				if (Utils.getInstance().getDeviceWidth(
						PhotoGalleryActivity.this) > 650) {
					setContentView(R.layout.activity_photo_gallery_tablet);
				} else {
					setContentView(R.layout.activity_photo_gallery_phone);
				}
			} else {
				setContentView(R.layout.activity_photo_gallery_phone);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	// This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			// TODO Auto-generated method stub
			gridViewPhotoGallery = (GridView)findViewById(R.id.gridViewPhotoGallery);
			photoGalleryList = new ArrayList<GalleryBean>();
			adapter = new CustomPhotoGalleryAdapter(PhotoGalleryActivity.this, R.layout.activity_photo_gallery_list_item, photoGalleryList);
			gridViewPhotoGallery.setAdapter(adapter);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to get the Photo Gallery
	public class AsynTaskGetPhotoGallery extends AsyncTask<String, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(PhotoGalleryActivity.this);
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
	        	nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(PhotoGalleryActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(PhotoGalleryActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(PhotoGalleryActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        			
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(ConfigConstants.Urls.gallery, ServiceHandler.POST, nameValuePairs);
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
				
					if(jsonObjParent.has("gal")) {
						setPhotoGalleryData( jsonObjParent.getJSONObject("gal"));
					}
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
	
	//  A method is used to get and set the Photo Gallery data
	private void setPhotoGalleryData(JSONObject jsonObjectBarDetails) {
		try {
			JSONArray jsonArrayResult = jsonObjectBarDetails.getJSONArray("result");
			for (int i = 0; i < jsonArrayResult.length(); i++) {
				JSONObject jsonObj = jsonArrayResult.getJSONObject(i);
				
				photoGalleryList.add(new GalleryBean(Utils.getInstance().isTagExists(jsonObj, "bar_gallery_id"), Utils.getInstance().isTagExists(jsonObj, "image_title"), 
						Utils.getInstance().isTagExists(jsonObj, "bar_image_name"), Utils.getInstance().isTagExists(jsonObj, "title")));
			}
			
			//  Now, notify the adapter
			adapter.notifyDataSetChanged();
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
