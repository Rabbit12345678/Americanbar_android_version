package com.spaculus.americanbars.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.ContactUsViewMapActivity;
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
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentContactUs extends Fragment {
	
	private View rootView = null;
	private RelativeLayout relativeLayoutContactUs;
	@SuppressWarnings("unused")
	private TextView tvAddress, tvPhone, tvEmail, tvWebsite;
	private ImageView ivShowMap;
	private EditText etName, etEmail, etSubject, etWriteMessageHere;
	private Button buttonSubmit;
	
	//  To know whether user is redirected to this screen from navigation drawer or not
	//  So on the basis of it we can show the action bar fields
	boolean isRedirectedFromMainActivity = false;
	
	public FragmentContactUs(boolean flag){
		this.isRedirectedFromMainActivity = flag;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
			rootView = inflater.inflate(R.layout.fragment_contact_us, container, false);
			
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
			
			//  Called the AsyncTask to get the contact us information
			if(Utils.getInstance() .isNetworkAvailable(getActivity())) {		
				new AsynTaskGetContactUsInformation().execute();
			}
			else{
				Utils.getInstance().showToastNoInternetAvailable(getActivity());
			}

			//  To hide the soft key board on the click of anywhere onto the screen 
			relativeLayoutContactUs.setOnTouchListener(new OnTouchListener() {
					
					@SuppressLint("ClickableViewAccessibility") 
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						
						if(v==relativeLayoutContactUs){
							hideSoftKeyboard();
							return true;
						}
						return false;
					}
			});
			
			/* For the EditText Scrolling issue */
			//  About User
			etWriteMessageHere.setOnTouchListener(new OnTouchListener() {
			    @SuppressLint("ClickableViewAccessibility") 
			    @Override
			    public boolean onTouch(View v, MotionEvent event) {
			        try {
						if (v.getId() == R.id.etWriteMessageHere) {
						    v.getParent().requestDisallowInterceptTouchEvent(true);
						    switch (event.getAction() & MotionEvent.ACTION_MASK) {
						    case MotionEvent.ACTION_UP:
						        v.getParent().requestDisallowInterceptTouchEvent(false);
						        break;
						    }
						}
					} 
			        catch (Exception e) {
						e.printStackTrace();
					}
			        return false;
			    }
			});
			
			/* Map icon click event */
			ivShowMap.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), ContactUsViewMapActivity.class);
					intent.putExtra("address", tvAddress.getText().toString().trim());
					startActivity(intent);
				}
			});
			
			//  Submit button click event
			buttonSubmit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						//  First, hide the soft key board.
						hideSoftKeyboard();
						
						//  Now, first of all check all the validations
						String validationResponse  = Validation.getInstance().contactUs(etName.getText().toString().trim(), etEmail.getText().toString().trim(), etSubject.getText().toString().trim(), etWriteMessageHere.getText().toString().trim());
						
						//  Means all the fields are properly entered
						if(validationResponse.isEmpty()){
							
							//  Called the AsyncTask to send the email
							if(Utils.getInstance() .isNetworkAvailable(getActivity())) {		
								new AsynTaskSubmitContactForm().execute();
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
	
	//  This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			relativeLayoutContactUs = (RelativeLayout)rootView.findViewById(R.id.relativeLayoutContactUs);
			tvAddress = (TextView)rootView.findViewById(R.id.tvAddress);
			tvPhone = (TextView)rootView.findViewById(R.id.tvPhone);
			tvEmail = (TextView)rootView.findViewById(R.id.tvEmail);
			tvWebsite = (TextView)rootView.findViewById(R.id.tvWebsite);
			ivShowMap = (ImageView)rootView.findViewById(R.id.ivShowMap);
			etName = (EditText)rootView.findViewById(R.id.etName);
			etEmail = (EditText)rootView.findViewById(R.id.etEmail);
			etSubject = (EditText)rootView.findViewById(R.id.etSubject);
			etWriteMessageHere = (EditText)rootView.findViewById(R.id.etWriteMessageHere);
			buttonSubmit = (Button)rootView.findViewById(R.id.btnSubmit);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  To hide the soft key board on the click of Layout
	private void hideSoftKeyboard(){
		try {
			InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etSubject.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etWriteMessageHere.getWindowToken(), 0);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to get the contact us information
	public class AsynTaskGetContactUsInformation extends AsyncTask<Void, Void, Void> {
		
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
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_UNIQUE_CODE)));
		       
	        	// Making a request to url and getting response 
		        responseString = sh.makeServiceCall(ConfigConstants.Urls.get_contact_us_info, ServiceHandler.POST, nameValuePairs);
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
					
					JSONObject jsonObj = jsonObjParent.getJSONObject("info");
				
					//  Set Address
					tvAddress.setText(Utils.getInstance().isTagExists(jsonObj, "site_address"));
					
					//  Set Email
					tvEmail.setText(Utils.getInstance().isTagExists(jsonObj, "site_email"));
					
					//  Set Web site
					tvWebsite.setText("www."+Utils.getInstance().isTagExists(jsonObj, "site_name"));
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
	
	//  AsyncTask to Change Password
	public class AsynTaskSubmitContactForm extends AsyncTask<Void, Void, Void> {
		
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
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("name", etName.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("email", etEmail.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("subject", etSubject.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("message", etWriteMessageHere.getText().toString().trim()));
		        
	        	// Making a request to url and getting response 
		        responseString = sh.makeServiceCall(ConfigConstants.Urls.send_inquiry, ServiceHandler.POST, nameValuePairs);
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
				if(responseString!=null){
					JSONObject jsonObjParent = new JSONObject(responseString);
					status = jsonObjParent.getString("status");
					//Log.i("status",status);
					
					//  Means success
					if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)){
						//Toast.makeText(getActivity(), "Your inquiry email has been sent successfully.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(getActivity(), "Your inquiry email has been sent successfully.");
						
						//  Now, clear all the data here.
						etName.setText("");
						etEmail.setText("");
						etSubject.setText("");
						etWriteMessageHere.setText("");
					}
					//  Means fail 
					else{
						//Toast.makeText(getActivity(), "Your inquiry email is not sent. Please try again.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(getActivity(), "Your inquiry email is not sent. Please try again.");
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
