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

public class ResetPasswordActivity extends BaseActivity{

	private EditText etVerificationCode;
	private EditText etNewPassword;
	private EditText etConfirmNewPassword;
	private Button buttonResetPassword;
	
	//  To hide the soft key board when user clicks any where onto the screen.
	private RelativeLayout relativeLayoutResetPassword;
	
	private ScrollView scrollView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_reset_password);
			
			// Create ActionBar
			createActionBar("Reset Password", R.layout.custom_actionbar, ResetPasswordActivity.this, true);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, false, false);
			
			// Mapping of all the views
			mappedAllViews();
			
			//  To hide the soft key board on the click of anywhere onto the screen 
			relativeLayoutResetPassword.setOnTouchListener(new OnTouchListener() {
					
					@SuppressLint("ClickableViewAccessibility") 
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						
						if(v==relativeLayoutResetPassword){
							hideSoftKeyboard();
							return true;
						}
						return false;
					}
			});
			
			//  Reset Password button click
			buttonResetPassword.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						//  First, hide the soft key board.
						hideSoftKeyboard();
						
						//  Now, first of all check all the validations
						String validationResponse  = Validation.getInstance().resetPasswordValidation(etVerificationCode.getText().toString().trim(), etNewPassword.getText().toString().trim(), etConfirmNewPassword.getText().toString().trim());
						
						//  Means all the fields are properly entered
						if(validationResponse.isEmpty()){
							//  Called the AsyncTask to send the email
							if(Utils.getInstance().isNetworkAvailable(ResetPasswordActivity.this)) {		
								new AsynTaskResetPassword().execute();
							}
							else{
								Utils.getInstance().showToastNoInternetAvailable(ResetPasswordActivity.this);
							}
						}
						else {
							//  Show respective validation message
							//Toast.makeText(ResetPasswordActivity.this, validationResponse, Toast.LENGTH_SHORT).show();
							Utils.toastLong(ResetPasswordActivity.this, validationResponse);
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
			etVerificationCode = (EditText)findViewById(R.id.etVerificationCode);
			etNewPassword = (EditText)findViewById(R.id.etNewPassword);
			etConfirmNewPassword = (EditText)findViewById(R.id.etConfirmNewPassword);
			buttonResetPassword = (Button)findViewById(R.id.btnResetPassword);
			relativeLayoutResetPassword = (RelativeLayout)findViewById(R.id.relativeLayoutResetPassword);
			scrollView = (ScrollView)findViewById(R.id.scrollView);
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
			imm.hideSoftInputFromWindow(etVerificationCode.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etNewPassword.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etConfirmNewPassword.getWindowToken(), 0);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask for the Reset Password Functionality
	public class AsynTaskResetPassword extends AsyncTask<Void, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(ResetPasswordActivity.this);
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
	        	nameValuePairs.add(new BasicNameValuePair("forget_password_code", etVerificationCode.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("password", etNewPassword.getText().toString().trim()));
		        
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(ConfigConstants.Urls.reset_password, ServiceHandler.POST, nameValuePairs);
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
				
				//  Means email is sent to the entered email id
				if(status.equals("success")){
					//Toast.makeText(ResetPasswordActivity.this, "Your password is reset. Please login with the new password.", Toast.LENGTH_SHORT).show();
					Utils.toastLong(ResetPasswordActivity.this, "Your password is reset. Please login with the new password.");
					//  Close this activity
					finish();
					onDestroy();
				}
				else{
					//Toast.makeText(ResetPasswordActivity.this, "Password is not reset. Please try again.", Toast.LENGTH_LONG).show();
					Utils.toastLong(ResetPasswordActivity.this, "Password is not reset. Please try again.");
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
