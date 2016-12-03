package com.spaculus.americanbars;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.MultipartUtility;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;
import com.spaculus.helpers.Validation;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import br.com.sapereaude.maskedEditText.MaskedEditText;

public class MyProfileActivity extends BaseActivityMedia {

	private RelativeLayout relativeLayoutMyProfile;
	private ImageView ivProfilePicture, ivEditProfilePicture, ivEditProfileDetails;
	private EditText etFirstName, etLastName, etUserName;
	private RadioButton radioButtonMale, radioButtonFemale;
	private EditText etEmail, etAboutUser, etAddress, etCity, etState, etZipCode;
	private MaskedEditText etMobileNumber;
	private EditText etFacebookLink, etGooglePlusLink, etTwitterLink, etLinkedInLink, etPinterestLink, etInstagramLink;
	private Button buttonSave, buttonCancel;
	
	//  To disable or enable all the fields
	private boolean CONSTANT_DISABLE = false;
	private boolean CONSTANT_ENABLE = true;
	
	//  To store the existing profile image name
	private String existingProfileImageName = "";
	
	//  To edit the profile picture
	private String selectedMediaPath = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_my_profile);
			
			//  Create Action Bar
			createActionBar("My Profile", R.layout.custom_actionbar, MyProfileActivity.this, true);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);
			
			// Mapping of all the views
			mappedAllViews();
			
			//  Now, first of all get the user details
			if(Utils.getInstance() .isNetworkAvailable(MyProfileActivity.this)) {
				new AsynTaskGetUserProfileDetails().execute();
			}
			else{
				Utils.getInstance().showToastNoInternetAvailable(MyProfileActivity.this);
			}
			
			/* For the EditText Scrolling issue */
			//  About User
			etAboutUser.setOnTouchListener(new OnTouchListener() {
			    @SuppressLint("ClickableViewAccessibility") 
			    @Override
			    public boolean onTouch(View v, MotionEvent event) {
			        try {
						if (v.getId() == R.id.etAboutUser) {
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
			
			//  Address
			etAddress.setOnTouchListener(new OnTouchListener() {
			    @SuppressLint("ClickableViewAccessibility") 
			    @Override
			    public boolean onTouch(View v, MotionEvent event) {
			        try {
						if (v.getId() == R.id.etAddress) {
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
			
			//  To hide the soft key board on the click of anywhere onto the screen 
			relativeLayoutMyProfile.setOnTouchListener(new OnTouchListener() {
					
					@SuppressLint("ClickableViewAccessibility") 
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if(v==relativeLayoutMyProfile){
							hideSoftKeyboard();
							return true;
						}
						return false;
					}
			});
			
			//  Edit icon click event
			ivEditProfileDetails.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						//  Now, if views are enable then disable them and vice versa.
						if(etFirstName.isEnabled()) {
							disableEnableAllFields(CONSTANT_DISABLE);
						}
						else {
							disableEnableAllFields(CONSTANT_ENABLE);
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  Upload profile picture click event
			ivEditProfilePicture.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						selectMediaSingle(ConfigConstants.Constants.CONSTANT_MEDIA_PICTURE, 1, CONSTANT_MY_PROFILE_ACTIVITY_SINGLE_SELECTION, MyProfileActivity.this);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  Save button click event
			buttonSave.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						//  First, hide the soft key board.
						hideSoftKeyboard();
									
						//  Now, first of all check all the validations
						String validationResponse  = Validation.getInstance().editProfile(etFirstName.getText().toString().trim(), etLastName.getText().toString().trim(), etUserName.getText().toString().trim(), 
								etEmail.getText().toString().trim(), etAboutUser.getText().toString().trim());
						
						//  Means all the fields are properly entered
						if(validationResponse.isEmpty()){
							//  Called the AsyncTask to send the email for the Forgot Password functionality
							if(Utils.getInstance().isNetworkAvailable(MyProfileActivity.this)) {		
								new AsynTaskEditProfileMultiPart().execute();
							}
							else{
								Utils.getInstance().showToastNoInternetAvailable(MyProfileActivity.this);
							}
						}
						else {
							//  Show respective validation message
							//Toast.makeText(MyProfileActivity.this, validationResponse, Toast.LENGTH_SHORT).show();
							Utils.toastLong(MyProfileActivity.this, validationResponse);
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
					}	
				}
			});
			
			//  Cancel button click event
			buttonCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						//  Simply close this activity
						finish();
						onDestroy();
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
	
	// This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			// TODO Auto-generated method stub
			relativeLayoutMyProfile = (RelativeLayout) findViewById(R.id.relativeLayoutMyProfile);
			ivProfilePicture = (ImageView)findViewById(R.id.ivProfilePicture);
			ivEditProfilePicture = (ImageView)findViewById(R.id.ivEditProfilePicture);
			ivEditProfileDetails = (ImageView)findViewById(R.id.ivEditProfileDetails);
			etFirstName = (EditText) findViewById(R.id.etFirstName);
			etLastName = (EditText) findViewById(R.id.etLastName);
			etUserName = (EditText) findViewById(R.id.etUserName);
			radioButtonMale = (RadioButton) findViewById(R.id.radioButtonMale);
			radioButtonFemale = (RadioButton) findViewById(R.id.radioButtonFemale);
			etEmail = (EditText) findViewById(R.id.etEmail);
			etAboutUser = (EditText) findViewById(R.id.etAboutUser);
			etAddress = (EditText) findViewById(R.id.etAddress);
			etCity = (EditText) findViewById(R.id.etCity);
			etState = (EditText) findViewById(R.id.etState);
			etZipCode = (EditText) findViewById(R.id.etZipCode);
			etMobileNumber = (MaskedEditText) findViewById(R.id.etMobileNumber);
			etFacebookLink = (EditText) findViewById(R.id.etFacebookLink);
			etGooglePlusLink = (EditText) findViewById(R.id.etGooglePlusLink);
			etTwitterLink = (EditText) findViewById(R.id.etTwitterLink);
			etLinkedInLink = (EditText) findViewById(R.id.etLinkedInLink);
			etPinterestLink = (EditText) findViewById(R.id.etPinterestLink);
			etInstagramLink = (EditText) findViewById(R.id.etInstagramLink);
			buttonSave = (Button)findViewById(R.id.btnSave);
			buttonCancel = (Button)findViewById(R.id.btnCancel);
			
			//  Initially, disable all the fields
			disableEnableAllFields(CONSTANT_DISABLE);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  Hide the soft key board on the click of Layout
	private void hideSoftKeyboard() {
		try {
			//Log.i("Method hideSoftKeyboard","Method hideSoftKeyboard");
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etFirstName.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etLastName.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etUserName.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etAboutUser.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etAddress.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etCity.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etState.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etZipCode.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etMobileNumber.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etFacebookLink.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etGooglePlusLink.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etTwitterLink.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etLinkedInLink.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etPinterestLink.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etInstagramLink.getWindowToken(), 0);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  A method is used to disable/enable all the fields.
	private void disableEnableAllFields(boolean flagStatus) {
		try {
			ivProfilePicture.setEnabled(flagStatus);
			etFirstName.setEnabled(flagStatus);
			etLastName.setEnabled(flagStatus);
			etUserName.setEnabled(flagStatus);
			radioButtonMale.setEnabled(flagStatus);
			radioButtonFemale.setEnabled(flagStatus);
			etEmail.setEnabled(flagStatus);
			etAboutUser.setEnabled(flagStatus);
			etAddress.setEnabled(flagStatus);
			etCity.setEnabled(flagStatus);
			etState.setEnabled(flagStatus);
			etZipCode.setEnabled(flagStatus);
			etMobileNumber.setEnabled(flagStatus);
			etFacebookLink.setEnabled(flagStatus);
			etGooglePlusLink.setEnabled(flagStatus);
			etTwitterLink.setEnabled(flagStatus);
			etLinkedInLink.setEnabled(flagStatus);
			etPinterestLink.setEnabled(flagStatus);
			etInstagramLink.setEnabled(flagStatus);
			
			//  Means need to show the Edit Profile Picture icon and buttons
			if(flagStatus) {
				ivEditProfilePicture.setVisibility(View.VISIBLE);
				buttonSave.setVisibility(View.VISIBLE);
				buttonCancel.setVisibility(View.VISIBLE);
				
				//  Need to change the Edit Profile Details image run time
				ivEditProfileDetails.setImageResource(R.drawable.edit_profile);
			}
			else {
				ivEditProfilePicture.setVisibility(View.GONE);
				buttonSave.setVisibility(View.GONE);
				buttonCancel.setVisibility(View.GONE);
				
				//  Need to change the Edit Profile Details image run time
				ivEditProfileDetails.setImageResource(R.drawable.edit_profile_grey);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to get the User Profile Details
	public class AsynTaskGetUserProfileDetails extends AsyncTask<Void, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(MyProfileActivity.this);
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
	        try {
	        	ServiceHandler sh = new ServiceHandler();
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        	nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(MyProfileActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(MyProfileActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(MyProfileActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        			
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(ConfigConstants.Urls.getuserdashboard, ServiceHandler.POST, nameValuePairs);
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
				
					if(jsonObjParent.has("getalldata")) {
						setProfileData( jsonObjParent.getJSONObject("getalldata"));
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
	
	//  A method is used to get and set the user profile's data
	private void setProfileData(JSONObject jsonObj) {
		try {
			//  First Name
			etFirstName.setText(Utils.getInstance().isTagExists(jsonObj, "first_name"));
			//  Last Name
			etLastName.setText(Utils.getInstance().isTagExists(jsonObj, "last_name"));
			//  User Name
			etUserName.setText(Utils.getInstance().isTagExists(jsonObj, "nick_name"));
			//  Gender
			if(Utils.getInstance().isTagExists(jsonObj, "gender").equals(ConfigConstants.Constants.MALE)) {
				radioButtonMale.setChecked(true);
				radioButtonFemale.setChecked(false);
			}
			else {
				radioButtonMale.setChecked(false);
				radioButtonFemale.setChecked(true);
			}
			//  Email
			etEmail.setText(Utils.getInstance().isTagExists(jsonObj, "email"));
			//  About User
			etAboutUser.setText(Utils.getInstance().setHTMLText(Utils.getInstance().isTagExists(jsonObj, "about_user")));
			//  Address
			etAddress.setText(Utils.getInstance().isTagExists(jsonObj, "address"));
			//  City
			etCity.setText(Utils.getInstance().isTagExists(jsonObj, "user_city"));
			//  State
			etState.setText(Utils.getInstance().isTagExists(jsonObj, "user_state"));
			//  Zip Code
			etZipCode.setText(Utils.getInstance().isTagExists(jsonObj, "user_zip"));
			//  Mobile Number
			etMobileNumber.setText(Utils.getInstance().isTagExists(jsonObj, "mobile_no"));
			//  Facebook Link
			etFacebookLink.setText(Utils.getInstance().isTagExists(jsonObj, "fb_link"));
			//  Google Plus Link
			etGooglePlusLink.setText(Utils.getInstance().isTagExists(jsonObj, "gplus_link"));
			//  Twitter Link
			etTwitterLink.setText(Utils.getInstance().isTagExists(jsonObj, "twitter_link"));
			//  Linked In Link
			etLinkedInLink.setText(Utils.getInstance().isTagExists(jsonObj, "linkedin_link"));
			//  Pinterest Link
			etPinterestLink.setText(Utils.getInstance().isTagExists(jsonObj, "pinterest_link"));
			//  Instagram Link
			etInstagramLink.setText(Utils.getInstance().isTagExists(jsonObj, "instagram_link"));
			//  Set Profile Picture
			existingProfileImageName = Utils.getInstance().isTagExists(jsonObj, "profile_image");
			setLogo(ConfigConstants.ImageUrls.profilePictureURL, existingProfileImageName, ivProfilePicture, R.drawable.no_profile_picture);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* The following method is used to set the profile picture. */
	public void setProfilePicture (String mediaPath) {
		selectedMediaPath = mediaPath;
		try {
			Utils.getInstance().setImageDevice(MyProfileActivity.this, selectedMediaPath, ivProfilePicture);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask for the Edit Profile Functionality
	public class AsynTaskEditProfileMultiPart extends AsyncTask<Void, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(MyProfileActivity.this);
    	private String responseString = "";
    	private String status = "";
    	private String imagename = "";
    	/*private HttpURLConnection connection = null;
		private DataOutputStream outputStream = null;
		private DataInputStream inputStream = null;*/
		private String charset = "UTF-8";
		//private File[] uploadFileArray =  new File[mediaList.size()];
		private File uploadFile = null;
		private String response = "";
		
    	@Override
		protected void onPreExecute() {
			try {
				// TODO Auto-generated method stub
				super.onPreExecute();
				
				/*if(mediaList.size()>0) {
					uploadFile = new File(mediaList.get(0).getMediaPath());
				}*/
				if(!selectedMediaPath.isEmpty()) {
					uploadFile = new File(selectedMediaPath);
				}
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
				// Making a request to URL and getting response
	            responseString  = ConfigConstants.Urls.editinfo;
	        	//Log.i("responseString",responseString);
	        	
	        	String gender = "";
	        	if(radioButtonMale.isChecked()) {
	        		gender = ConfigConstants.Constants.MALE;
	        	}
	        	else {
	        		gender = ConfigConstants.Constants.FEMALE;
	        	}
	        
	        	MultipartUtility multipart = new MultipartUtility(responseString, charset);
	        	//if(mediaList.size()>0) {
	        	if(!selectedMediaPath.isEmpty()) {
	        		multipart.addFilePart("file_up", uploadFile);
	        	}
	        	multipart.addFormField("user_id", SessionManager.getInstance(MyProfileActivity.this).getData(SessionManager.KEY_USER_ID));
	        	multipart.addFormField("device_id", SessionManager.getInstance(MyProfileActivity.this).getData(SessionManager.KEY_DEVICE_ID));
	        	multipart.addFormField("unique_code", SessionManager.getInstance(MyProfileActivity.this).getData(SessionManager.KEY_UNIQUE_CODE));
	        	multipart.addFormField("first_name", etFirstName.getText().toString().trim());
	        	multipart.addFormField("last_name", etLastName.getText().toString().trim());
	        	multipart.addFormField("nick_name", etUserName.getText().toString().trim());
	        	multipart.addFormField("gender", gender);
	        	multipart.addFormField("email", etEmail.getText().toString().trim());
	        	multipart.addFormField("about_user", etAboutUser.getText().toString().trim());
	        	multipart.addFormField("address", etAddress.getText().toString().trim());
	        	multipart.addFormField("user_city", etCity.getText().toString().trim());
	        	multipart.addFormField("user_state", etState.getText().toString().trim());
	        	multipart.addFormField("user_zip", etZipCode.getText().toString().trim());
	        	multipart.addFormField("mobile_no", etMobileNumber.getText().toString().trim());
	        	multipart.addFormField("fb_link", etFacebookLink.getText().toString().trim());
	        	multipart.addFormField("gplus_link", etGooglePlusLink.getText().toString().trim());
	        	multipart.addFormField("twitter_link", etTwitterLink.getText().toString().trim());
	        	multipart.addFormField("linkedin_link", etLinkedInLink.getText().toString().trim());
	        	multipart.addFormField("pinterest_link", etPinterestLink.getText().toString().trim());
	        	multipart.addFormField("instagram_link", etInstagramLink.getText().toString().trim());
	        	multipart.addFormField("pre_profile_image", existingProfileImageName);
	               
	            List<String> responseUploadDocument = multipart.finish();
	            System.out.println("SERVER REPLIED1:");
	            for (String line : responseUploadDocument) {
	                //Log.i("line", line);
	                response = line;
	            }
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
				if(response!=null) {
					JSONObject jsonParentObj = new JSONObject(response);
					status = jsonParentObj.getString("status");
					
					//  Now, if the response is in success then get the image name.
					JSONObject jsonObject = jsonParentObj.getJSONObject("res");
					String tempStatus = jsonObject.getString("status");
					if(tempStatus.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)){
						imagename = jsonObject.getString("imagename");
					}
				}
				
				if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)){
					//Toast.makeText(MyProfileActivity.this, "Profile details are updated successfully.", Toast.LENGTH_SHORT).show();
					Utils.toastLong(MyProfileActivity.this, "Profile details are updated successfully.");
					
					//  Now, here update the profile picture and name of a user.
					SessionManager.getInstance(MyProfileActivity.this).updateSessionNameProfilePicture(imagename, etFirstName.getText().toString().trim()+" "+etLastName.getText().toString().trim());
				}
				else {
					//Toast.makeText(MyProfileActivity.this, "Profile details are not updated. Please try again.", Toast.LENGTH_SHORT).show();
					Utils.toastLong(MyProfileActivity.this, "Profile details are not updated. Please try again.");
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
