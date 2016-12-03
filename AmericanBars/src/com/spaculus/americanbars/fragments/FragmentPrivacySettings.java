package com.spaculus.americanbars.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.spaculus.adapters.CustomPrivacySettingsAdapter;
import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.R;
import com.spaculus.beans.PrivacySettings;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class FragmentPrivacySettings extends Fragment {
	
	private View rootView = null;
	private ListView listViewPrivacySettings;
	private Button buttonSave, buttonCancel;
	private LinearLayout linearLayoutButtons;
	
	//private ArrayList<String> titleArrayList = null;
	private ArrayList<PrivacySettings> privacySettingsList = null;
 	private CustomPrivacySettingsAdapter adapter = null;
 	private String setting_id = "";
	
 	//  To know whether user is redirected to this screen from navigation drawer or not
	//  So on the basis of it we can show the action bar fields
	boolean isRedirectedFromMainActivity = false;
	
	public FragmentPrivacySettings(boolean flag){
		this.isRedirectedFromMainActivity = flag;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        try {
			rootView = inflater.inflate(R.layout.fragment_privacy_settings, container, false);
			
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
			
			//  Now, get the privacy settings data
			if(Utils.getInstance() .isNetworkAvailable(getActivity())) {
				new AsynTaskGetPrivacySettingsData().execute();
			}
			else{
				Utils.getInstance().showToastNoInternetAvailable(getActivity());
			}
			
			//  Save button click event
			buttonSave.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//  Now, save the privacy settings data
					if(Utils.getInstance() .isNetworkAvailable(getActivity())) {
						new AsynTaskSavePrivacySettingsData().execute();
					}
					else{
						Utils.getInstance().showToastNoInternetAvailable(getActivity());
					}
				}
			});
			
			//  Cancel button click event
			buttonCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					/* Set Default Data */
					setDefaultPrivacySettingsData();
					//  Hide the button's layout
					linearLayoutButtons.setVisibility(View.GONE);
				}
			});
			
		} 
        catch (Exception e) {
			e.printStackTrace();
		}
        return rootView;
    }
	
	//  This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			listViewPrivacySettings = (ListView) rootView.findViewById(R.id.listViewPrivacySettings);
			buttonSave = (Button) rootView.findViewById(R.id.btnSave);
			buttonCancel = (Button) rootView.findViewById(R.id.btnCancel);
			linearLayoutButtons = (LinearLayout) rootView.findViewById(R.id.linearLayoutButtons);
			
			//  Initialize adapter and array list here and set the default data.
			addPrivacySettingsData();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  Initialize adapter and array list here and set the default data.
	private void addPrivacySettingsData() {
		try {
			//  Initialize adapter and array list here.
			privacySettingsList = new ArrayList<PrivacySettings>();
			privacySettingsList.add(new PrivacySettings("First Name", "-1", "-1"));
			privacySettingsList.add(new PrivacySettings("Last Name", "-1", "-1"));
			privacySettingsList.add(new PrivacySettings("Gender", "-1", "-1"));
			privacySettingsList.add(new PrivacySettings("Address ", "-1", "-1"));
			privacySettingsList.add(new PrivacySettings("About", "-1", "-1"));
			privacySettingsList.add(new PrivacySettings("Album", "-1", "-1"));
			adapter = new CustomPrivacySettingsAdapter(getActivity(), R.layout.fragment_privacy_settings_list_item, privacySettingsList, FragmentPrivacySettings.this);
			listViewPrivacySettings.setAdapter(adapter);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to get the privacy settings data
	public class AsynTaskGetPrivacySettingsData extends AsyncTask<Void, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(getActivity());
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
		protected Void doInBackground(Void... params) {
			ServiceHandler sh = new ServiceHandler();
	        try {
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        	nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_UNIQUE_CODE)));
		        
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(ConfigConstants.Urls.privacy_settings, ServiceHandler.POST, nameValuePairs);
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
				if(responseString!=null) {
					jsonObjParent = new JSONObject(responseString);
					status = jsonObjParent.getString("status");
					//Log.i("status",status);
				}
				
				if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)) {
					JSONObject jsonObj = jsonObjParent.getJSONObject("getsetting");
					
					//  Get the setting_id value
					setting_id = Utils.getInstance().isTagExists(jsonObj, "setting_id");
					
					//  Now, set the existing data
					String[] data = {Utils.getInstance().isTagExists(jsonObj, "fname"), Utils.getInstance().isTagExists(jsonObj, "lname"), Utils.getInstance().isTagExists(jsonObj, "gender1"),
							Utils.getInstance().isTagExists(jsonObj, "address1"), Utils.getInstance().isTagExists(jsonObj, "abt"), Utils.getInstance().isTagExists(jsonObj, "album")};
					
					for(int i=0; i<privacySettingsList.size(); i++) {
						privacySettingsList.get(i).setHideSelected(data[i]);
						privacySettingsList.get(i).setDefaultHideSelected(data[i]);
					}
					
					/*Now, simply notify an adapter*/
					adapter.notifyDataSetChanged();
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			if(this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}	
    }
	
	//  AsyncTask to save the privacy settings data
	public class AsynTaskSavePrivacySettingsData extends AsyncTask<Void, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(getActivity());
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
		protected Void doInBackground(Void... params) {
			ServiceHandler sh = new ServiceHandler();
	        try {
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        	nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("setting_id", setting_id));
		        nameValuePairs.add(new BasicNameValuePair("fname", privacySettingsList.get(0).getHideSelected()));
		        nameValuePairs.add(new BasicNameValuePair("lname", privacySettingsList.get(1).getHideSelected()));
		        nameValuePairs.add(new BasicNameValuePair("gender1", privacySettingsList.get(2).getHideSelected()));
		        nameValuePairs.add(new BasicNameValuePair("address1", privacySettingsList.get(3).getHideSelected()));
		        nameValuePairs.add(new BasicNameValuePair("abt", privacySettingsList.get(4).getHideSelected()));
		        nameValuePairs.add(new BasicNameValuePair("album", privacySettingsList.get(5).getHideSelected()));
		        
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(ConfigConstants.Urls.update_privacy_settings, ServiceHandler.POST, nameValuePairs);
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
					//Toast.makeText(getActivity(), "Your settings changed successfully.", Toast.LENGTH_SHORT).show();
					Utils.toastLong(getActivity(), "Your settings changed successfully.");
					//  Hide the button's layout
					linearLayoutButtons.setVisibility(View.GONE);
					
					/* Now, here change the default settings value */
					for(int i=0; i<privacySettingsList.size(); i++) {
						privacySettingsList.get(i).setDefaultHideSelected(privacySettingsList.get(i).getHideSelected());
					}
				}
				else {
					//Toast.makeText(getActivity(), "Your settings are not changed. Please try again.", Toast.LENGTH_SHORT).show();
					Utils.toastLong(getActivity(), "Your settings are not changed. Please try again.");
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			if(this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}	
    }
	
	/* A method is used to show the visibility of the Buttons layout. */
	public void showButtonsLayout() {
		if(linearLayoutButtons.getVisibility()==View.GONE) {
			linearLayoutButtons.setVisibility(View.VISIBLE);
		}
	}
	
	/* A method is used to set the default privacy settings data. */
	private void setDefaultPrivacySettingsData() {
		/* Now, here change the original value by the default settings value */
		for(int i=0; i<privacySettingsList.size(); i++) {
			privacySettingsList.get(i).setHideSelected(privacySettingsList.get(i).getDefaultHideSelected());
		}
		/* Now, simply notify an adapter */
		adapter.notifyDataSetChanged();
	}
}
