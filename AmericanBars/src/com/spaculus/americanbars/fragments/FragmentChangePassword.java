package com.spaculus.americanbars.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.R;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;
import com.spaculus.helpers.Validation;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class FragmentChangePassword extends Fragment {
	
	private View rootView = null;
	private RelativeLayout relativeLayoutChangePassword;
	private EditText etOldPassword;
	private EditText etNewPassword;
	private EditText etConfirmNewPassword;
	private Button buttonChangePassword;
	private ScrollView scrollView;
	
	//  To know whether user is redirected to this screen from navigation drawer or not
	//  So on the basis of it we can show the action bar fields
	boolean isRedirectedFromMainActivity = false;
	
	public FragmentChangePassword(boolean flag){
		this.isRedirectedFromMainActivity = flag;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
			rootView = inflater.inflate(R.layout.fragment_change_password, container, false);
			
			//  For the Action Bar
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			if(isRedirectedFromMainActivity) {
				((BaseActivity)getActivity()).setActionBarFromChild(false, true, false, true, true);	
			}
			else {
				((BaseActivity)getActivity()).setActionBarFromChild(true, true, false, true, true);
			}
			
			// Mapping of all the views
			mappedAllViews();

			//  To hide the soft key board on the click of anywhere onto the screen 
			relativeLayoutChangePassword.setOnTouchListener(new OnTouchListener() {
					
					@SuppressLint("ClickableViewAccessibility") 
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						
						if(v==relativeLayoutChangePassword){
							hideSoftKeyboard();
							return true;
						}
						return false;
					}
			});
			
			//  Change Password button click event
			buttonChangePassword.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						//  First, hide the soft key board.
						hideSoftKeyboard();
						
						//  Now, first of all check all the validations
						String validationResponse  = Validation.getInstance().changePassword(etOldPassword.getText().toString().trim(), etNewPassword.getText().toString().trim(), 
								etConfirmNewPassword.getText().toString().trim());
						
						//  Means all the fields are properly entered
						if(validationResponse.isEmpty()){
							
							//  Called the AsyncTask to send the email
							if(Utils.getInstance() .isNetworkAvailable(getActivity())) {		
								new AsynTaskChangePassword().execute();
							}
							else{
								Utils.getInstance().showToastNoInternetAvailable(getActivity());
							}
						}
						else {
							//  Show respective validation message
							//Toast.makeText(getActivity(), validationResponse, Toast.LENGTH_SHORT).show();
							Utils.toastLong(getActivity(), validationResponse);
						}
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
        return rootView;
    }
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		try {
			super.onConfigurationChanged(newConfig);
			scrollView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_login));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			relativeLayoutChangePassword = (RelativeLayout)rootView.findViewById(R.id.relativeLayoutChangePassword);
			etOldPassword = (EditText)rootView.findViewById(R.id.etOldPassword);
			etNewPassword = (EditText)rootView.findViewById(R.id.etNewPassword);
			etConfirmNewPassword = (EditText)rootView.findViewById(R.id.etConfirmNewPassword);
			buttonChangePassword = (Button)rootView.findViewById(R.id.btnChangePassword);
			scrollView = (ScrollView)rootView.findViewById(R.id.scrollView);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  To hide the soft key board on the click of Layout
	private void hideSoftKeyboard(){
		try {
			////Log.i("Method hideSoftKeyboard","Method hideSoftKeyboard");
			InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etOldPassword.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etNewPassword.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etConfirmNewPassword.getWindowToken(), 0);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to Change Password
	public class AsynTaskChangePassword extends AsyncTask<Void, Void, Void> {
		
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
    	
		@SuppressLint("DefaultLocale") 
		@Override
		protected Void doInBackground(Void... params) {
	        try {
	        	ServiceHandler sh = new ServiceHandler();
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        	nameValuePairs.add(new BasicNameValuePair("old_pass", etOldPassword.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("new_pass", etNewPassword.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_UNIQUE_CODE)));
		       
	        	// Making a request to url and getting response 
		        responseString = sh.makeServiceCall(ConfigConstants.Urls.user_change_password, ServiceHandler.POST, nameValuePairs);
	        	//Log.i("responseString",responseString);
	        	/*{"user_id":"218","email":"ios.jekil1@spaculus.info","image":"","first_name":"jekil1","last_name":"patel1","mobile_no":"9876543210","unique_code":"55e57eac3d4e7IMEI-911414650292650","device_id":"IMEI-911414650292650","status":"success"}*/
	        }
	        catch(Exception e) {
	        	e.printStackTrace();
	        	//Log.i("AsyncTask,Login,Excpetion is:",Utils.isStringNull(e.getMessage()));
	        }
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			try {
				super.onPostExecute(result);
				if(responseString!=null){
					JSONObject jsonObjParent = new JSONObject(responseString);
					status = jsonObjParent.getString("status");
					//Log.i("status",status);
					
					//  Means success
					if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)){
						//Toast.makeText(getActivity(), "Your password has been changed successfuly.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(getActivity(), "Your password has been changed successfuly.");
						/* Now, clear all the fields */
						etOldPassword.setText("");
						etNewPassword.setText("");
						etConfirmNewPassword.setText("");
					}
					else if(status.equals("old_password_did_not_match_with_current_password")) {
						//Toast.makeText(getActivity(), "Old password did not match with a current password.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(getActivity(), "Old password did not match with a current password.");
					}
					//  Means fail 
					else{
						//Toast.makeText(getActivity(), "Your password is not changed. Please try again.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(getActivity(), "Your password is not changed. Please try again.");
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
}
