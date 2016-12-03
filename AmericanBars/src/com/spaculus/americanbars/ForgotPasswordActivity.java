package com.spaculus.americanbars;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.Utils;
import com.spaculus.helpers.Validation;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class ForgotPasswordActivity extends BaseActivity{
	
	private EditText etEmail;
	private Button buttonSend;
	
	//  To hide the soft key board when user clicks any where onto the screen.
	private RelativeLayout relativeLayoutForgotPassword;
	
	private ScrollView scrollView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_forgot_password);
			
			// Create ActionBar
			createActionBar("Forgot Password", R.layout.custom_actionbar, ForgotPasswordActivity.this, true);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, false, false);
			
			// Mapping of all the views
			mappedAllViews();
			
			//  To hide the soft key board on the click of anywhere onto the screen 
			relativeLayoutForgotPassword.setOnTouchListener(new OnTouchListener() {
					
					@SuppressLint("ClickableViewAccessibility") 
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(v==relativeLayoutForgotPassword){
							hideSoftKeyboard();
							return true;
						}
						return false;
					}
			});
			
			buttonSend.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						//  First, hide the soft key board.
						hideSoftKeyboard();
						
						//  Now, first of all check all the validations
						String validationResponse  = Validation.getInstance().forgotPasswordValidation(etEmail.getText().toString().trim());
						
						//  Means all the fields are properly entered
						if(validationResponse.isEmpty()){
							//  Called the AsyncTask to send the email for the Forgot Password functionality
							if(Utils.getInstance().isNetworkAvailable(ForgotPasswordActivity.this)) {		
								new AsynTaskForgotPassword().execute();
							}
							else{
								Utils.getInstance().showToastNoInternetAvailable(ForgotPasswordActivity.this);
							}
						}
						else {
							//  Show respective validation message
							//Toast.makeText(ForgotPasswordActivity.this, validationResponse, Toast.LENGTH_SHORT).show();
							Utils.toastLong(ForgotPasswordActivity.this, validationResponse);
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
	
	
	// This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		
		try {
			etEmail = (EditText) findViewById(R.id.etEmail);
			buttonSend = (Button) findViewById(R.id.btnSend);
			relativeLayoutForgotPassword = (RelativeLayout) findViewById(R.id.relativeLayoutForgotPassword);
			scrollView = (ScrollView) findViewById(R.id.scrollView);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//  Hide the soft key board on the click of Layout
	private void hideSoftKeyboard(){
		try {
			//Log.i("Method hideSoftKeyboard","Method hideSoftKeyboard");
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask for the Forgot Password Functionality
	public class AsynTaskForgotPassword extends AsyncTask<Void, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(ForgotPasswordActivity.this);
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
	        try {
	        	ServiceHandler sh = new ServiceHandler();
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("email", etEmail.getText().toString().trim()));
		        
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(ConfigConstants.Urls.forget_password, ServiceHandler.POST, nameValuePairs);
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
					JSONObject jsonObj = new JSONObject(responseString);
					status = jsonObj.getString("status");
					//Log.i("status",status);
				}
				
				if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)){
					Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
					startActivity(intent);
							
					//  Close this activity
					finish();
					onDestroy();
				}
				//  User is Inactive.
				else if(status.equals(ConfigConstants.Messages.RESPONSE_INACTIVE)){
					//Toast.makeText(ForgotPasswordActivity.this, "Inactive user. Please contact your admin.", Toast.LENGTH_SHORT).show();
					Utils.toastLong(ForgotPasswordActivity.this, "Inactive user. Please contact your admin.");
				}
				//  User is not registered with the application.
				else if(status.equals(ConfigConstants.Messages.RESPONSE_NOT_REGISTERED)){
					//Toast.makeText(ForgotPasswordActivity.this, "This Email ID is not registered with the application.", Toast.LENGTH_SHORT).show();
					Utils.toastLong(ForgotPasswordActivity.this, "This Email ID is not registered with the application.");
				}
				//  If this Email ID is not exists.
				else if(status.equals(ConfigConstants.Messages.RESPONSE_NOT_FOUND)){
					//Toast.makeText(ForgotPasswordActivity.this, "This Email ID is not registered with the application.", Toast.LENGTH_SHORT).show();
					Utils.toastLong(ForgotPasswordActivity.this, "This Email ID is not registered with the application.");
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
