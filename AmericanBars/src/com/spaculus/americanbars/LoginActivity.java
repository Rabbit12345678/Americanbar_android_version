package com.spaculus.americanbars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;
import com.spaculus.helpers.Validation;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @purpose This class is responsible to do login with the application in a
 *          normal way, Facebook Login or Twitter Login.
 * @author Manali Sheth
 * @date 09/11/2015
 */
/**
 * @author comp-503
 *
 */
public class LoginActivity extends BaseActivity implements OnClickListener, OnTouchListener {
	
	private RelativeLayout relativeLayoutSignIn;
	private EditText etEmail, etPassword;
	private CheckBox checkBoxRememberMe;
	private Button buttonSignIn, buttonCreateAccount;
	private TextView tvForgotPassword;
	private Button buttonFacebook;
	private LoginButton buttonFacebookAuth;
	
	private EditText[] etArray;

	/* For the Face book functionality */
	private CallbackManager callbackManagerFacebook;
	
	/* For the Web service call */ 
	//  AsyncTask Login message
	private String asyncTaskLoginMessage = "Logging in....";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			FacebookSdk.sdkInitialize(getApplicationContext());
			
			setContentView(R.layout.fragment_sign_in);
			
			createActionBar(ConfigConstants.Constants.SIGN_IN, R.layout.custom_actionbar, LoginActivity.this, true);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(false, true, false, false, false);	
			
			mappedAllViews();
			
			/* For the Remember Me functionality */
			if(SessionManager.getInstance(LoginActivity.this).getData(SessionManager.KEY_CHECKBOX_STATUS).equals(ConfigConstants.Constants.CONSTANT_YES)) {
				etEmail.setText(SessionManager.getInstance(LoginActivity.this).getData(SessionManager.KEY_EMAIL));
				etPassword.setText(SessionManager.getInstance(LoginActivity.this).getData(SessionManager.KEY_PASSWORD));
				checkBoxRememberMe.setChecked(true);
			}
			else {
				checkBoxRememberMe.setChecked(false);
			}
			
			buttonFacebookAuth.registerCallback(callbackManagerFacebook,
					new FacebookCallback<LoginResult>() {
				@Override
				public void onSuccess(LoginResult loginResult) {
					if (AccessToken.getCurrentAccessToken() != null) {
						RequestData();
					}
				}
				@Override
				public void onCancel() {
				}
				@Override
				public void onError(FacebookException error) {
				}
			});
			/*if (AccessToken.getCurrentAccessToken() != null) {
				RequestData();
			}*/
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		try {
			super.onConfigurationChanged(newConfig);
			relativeLayoutSignIn.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_login));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @purpose This method is used to do the mapping of all the views.
	 */
	private void mappedAllViews() {
		try {	
			relativeLayoutSignIn = (RelativeLayout) findViewById(R.id.relativeLayoutSignIn);
			etEmail = (EditText) findViewById(R.id.etEmail);
			etPassword = (EditText) findViewById(R.id.etPassword);
			checkBoxRememberMe = (CheckBox) findViewById(R.id.checkBoxRememberMe);
			tvForgotPassword = (TextView) findViewById(R.id.tvForgotPwd);
			buttonSignIn = (Button) findViewById(R.id.btnSignIn);
			buttonCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
			buttonFacebook = (Button) findViewById(R.id.buttonFacebook);
			buttonFacebookAuth = (LoginButton) findViewById(R.id.buttonFacebookAuth);
			buttonFacebookAuth.setReadPermissions(Arrays.asList("email"));
			
			etArray = new EditText[] { etEmail, etPassword };
			
			callbackManagerFacebook = CallbackManager.Factory.create();
			
			/* All click events */
			tvForgotPassword.setOnClickListener(this);
			buttonCreateAccount.setOnClickListener(this);
			buttonSignIn.setOnClickListener(this);
			buttonFacebook.setOnClickListener(this);
			
			/* All touch click events */
			relativeLayoutSignIn.setOnTouchListener(this);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View view) {
		try {
			switch (view.getId()) {
			case R.id.tvForgotPwd:
				redirectForgotPasswordActivity(LoginActivity.this);
				break;
			case R.id.buttonCreateAccount:
				try {
					redirectFragmentSignUp(LoginActivity.this);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.btnSignIn:
				loginNowClick();
				break;
			case R.id.buttonFacebook:
				buttonFacebookAuth.performClick();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v == relativeLayoutSignIn) {
			Utils.getInstance().hideSoftKeyboard(LoginActivity.this, etArray);
			return true;
		}
		return false;
	}

	private void loginNowClick() {
		try {
			//  First, hide the soft key board.
			Utils.getInstance().hideSoftKeyboard(LoginActivity.this, etArray);
			
			//  Now, first of all check all the validations
			String validationResponse  = Validation.getInstance().loginValidation(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
			
			//  Means all the fields are properly entered
			if(validationResponse.isEmpty()){
				
				//  Called the AsyncTask to do Login
				if(Utils.getInstance() .isNetworkAvailable(LoginActivity.this)) {		
					new AsynTaskLogin().execute();
				}
				else{
					Utils.getInstance().showToastNoInternetAvailable(LoginActivity.this);
				}
			}
			else {
				//  Show respective validation message
				//Toast.makeText(getActivity(), validationResponse, Toast.LENGTH_SHORT).show();
				Utils.toastLong(LoginActivity.this, validationResponse);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to do Login
	public class AsynTaskLogin extends AsyncTask<Void, Void, Void> {
    	private ProgressDialog pd = new ProgressDialog(LoginActivity.this);
    	private String responseString = "";
    	private String status = "";
    	private String user_id = "", unique_code = "", imageName = "", name = "";
    	
    	@Override
		protected void onPreExecute() {
			try {
				super.onPreExecute();
				this.pd.setMessage(asyncTaskLoginMessage);
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
	        	nameValuePairs.add(new BasicNameValuePair("email", etEmail.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("password", etPassword.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(LoginActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		       
	        	// Making a request to url and getting response 
		        responseString = sh.makeServiceCall(ConfigConstants.Urls.checklogin, ServiceHandler.POST, nameValuePairs);
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
					JSONObject jsonObj = new JSONObject(responseString);
					status = Utils.getInstance().isTagExists(jsonObj, "status");

					//  Means Successful Login
					if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)) {
						
						//  If login is done successfully then get all the other data.
						user_id = Utils.getInstance().isTagExists(jsonObj, "user_id");
						unique_code = Utils.getInstance().isTagExists(jsonObj, "unique_code");
						imageName = Utils.getInstance().isTagExists(jsonObj, "image");
						name = Utils.getInstance().isTagExists(jsonObj, "first_name")+" "+Utils.getInstance().isTagExists(jsonObj, "last_name");
						
						//  Create the login session here.
						String checkBoxStatus = "";
						String email = "", pwd = "";
						if(checkBoxRememberMe.isChecked()) {
							checkBoxStatus = ConfigConstants.Constants.CONSTANT_YES;
							email = etEmail.getText().toString().trim();
							pwd = etPassword.getText().toString().trim();
						}
						else {
							checkBoxStatus = ConfigConstants.Constants.CONSTANT_NO;
							email = "";
							pwd = "";
						}
						
						SessionManager.getInstance(LoginActivity.this).createLoginSession(user_id, unique_code, email, checkBoxStatus, imageName, name, pwd, 
								ConfigConstants.Constants.LOGIN_APPLICATION);
						
						/* Finish this activity and Redirect to How To Play Activity */
						finish();
						onDestroy();
						redirectMainActivity(LoginActivity.this);
					}
					//  User is Inactive.
					else if(status.equals(ConfigConstants.Messages.RESPONSE_INACTIVE)){
						//Toast.makeText(getActivity(), "Your account is not activated. Please activate your account first.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(LoginActivity.this, "Your account is not activated. Please activate your account first.");
					}
					//  User is not registered with the application.
					else if(status.equals(ConfigConstants.Messages.RESPONSE_NOT_REGISTERED)){
						//Toast.makeText(getActivity(), "This Email ID is not registered with the application.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(LoginActivity.this, "This Email ID is not registered with the application.");
					}
					//  Means fail 
					else{
						//Toast.makeText(getActivity(), "Invalid Credentials. Either entered Username Or Password is invalid.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(LoginActivity.this, "Invalid Credentials. Either entered Username Or Password is invalid.");
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
	
	/* To get the Facebook User Data */
	private void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object,GraphResponse response) {
                try { 
                	JSONObject jsonObject = response.getJSONObject();
                    if(jsonObject != null) {
                        /*
						 * Now, check whether get the data from the facebook or not
						 */
                    	String id = "", email = "", first_name = "", last_name = "", picture = "";
                    	id = Utils.getInstance().isTagExists(jsonObject, "id");
						if(!id.isEmpty()) {
							/* Now, check whether email permission is given or not. */
							email = Utils.getInstance().isTagExists(jsonObject, "email");
							String validationResponse  = Validation.getInstance().fbLoginValidation(email);
							if(validationResponse.isEmpty()) {
								first_name = Utils.getInstance().isTagExists(jsonObject, "first_name");
								last_name = Utils.getInstance().isTagExists(jsonObject, "last_name");
								picture = Utils.getInstance().isTagExists(jsonObject, "picture");
								//  Called the AsyncTask to do Login with Facebook
								if(Utils.getInstance() .isNetworkAvailable(LoginActivity.this)) {
									new AsynTaskLoginWithFacebook().execute(first_name, last_name, id, email, picture);
								}
								else{
									Utils.getInstance().showToastNoInternetAvailable(LoginActivity.this);
								}
							}
							else {
								Utils.toastLong(LoginActivity.this, validationResponse);
								clearFacebookCredentials();
							}
						}
						else {
							clearFacebookCredentials();
						}
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }
 
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManagerFacebook.onActivityResult(requestCode, resultCode, data);
    }
    
    //  AsyncTask to do Login with Facebook
	public class AsynTaskLoginWithFacebook extends AsyncTask<String, Void, Void> {
		
    	private ProgressDialog pd = new ProgressDialog(LoginActivity.this);
    	private String responseString = "";
    	private String status = "";
    	private String user_id = "", unique_code = "", imageName = "", name = "";
    	
    	@Override
		protected void onPreExecute() {
			try {
				super.onPreExecute();
				this.pd.setMessage(asyncTaskLoginMessage);
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
		protected Void doInBackground(String... params) {
	        try {
	        	ServiceHandler sh = new ServiceHandler();
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        	nameValuePairs.add(new BasicNameValuePair("first_name", params[0]));
	        	nameValuePairs.add(new BasicNameValuePair("last_name", params[1]));
		        nameValuePairs.add(new BasicNameValuePair("fb_id", params[2]));
		        nameValuePairs.add(new BasicNameValuePair("email", params[3]));
		        nameValuePairs.add(new BasicNameValuePair("fb_img", params[4]));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(LoginActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		      
	        	// Making a request to url and getting response 
		        responseString = sh.makeServiceCall(ConfigConstants.Urls.fb_register, ServiceHandler.POST, nameValuePairs);
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
					JSONObject jsonObj = new JSONObject(responseString);
					status = Utils.getInstance().isTagExists(jsonObj, "status");

					//  Means Successful Login
					if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)) {
						
						//  If login is done successfully then get all the other data.
						user_id = Utils.getInstance().isTagExists(jsonObj, "user_id");
						unique_code = Utils.getInstance().isTagExists(jsonObj, "unique_code");
						imageName = Utils.getInstance().isTagExists(jsonObj, "image");
						name = Utils.getInstance().isTagExists(jsonObj, "first_name")+" "+Utils.getInstance().isTagExists(jsonObj, "last_name");
						
						SessionManager.getInstance(LoginActivity.this).createLoginSession(user_id, unique_code, "", ConfigConstants.Constants.CONSTANT_NO, 
								imageName, name, "", ConfigConstants.Constants.LOGIN_FACEBOOK);
						
						/* Finish this activity and Redirect to How To Play Activity */
						finish();
						onDestroy();
						redirectMainActivity(LoginActivity.this);
					}
					//  User is Inactive.
					else if(status.equals(ConfigConstants.Messages.RESPONSE_INACTIVE)){
						//Toast.makeText(getActivity(), "Your account is not activated. Please activate your account first.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(LoginActivity.this, "Your account is not activated. Please activate your account first.");
					}
					//  User is not registered with the application.
					else if(status.equals(ConfigConstants.Messages.RESPONSE_NOT_REGISTERED)){
						//Toast.makeText(getActivity(), "This Email ID is not registered with the application.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(LoginActivity.this, "This Email ID is not registered with the application.");
					}
					//  Means fail 
					else {
						//Toast.makeText(getActivity(), "Invalid Credentials. Either entered Username Or Password is invalid.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(LoginActivity.this, "Invalid Credentials. Either entered Username Or Password is invalid.");
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
	
	/* A method is used to clear the facebook credentials. */
	private void clearFacebookCredentials() {
		/* Facebook Logout functionality */
		try{
			LoginManager.getInstance().logOut();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
