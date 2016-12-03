package com.spaculus.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
//import android.util.Log;

/**
 * @author Manali Sheth
 * @purpose This SessionManager class is used to manage the session of the application. i.e. to manage all the shared preferences functionality.
 */
public class SessionManager {
	
	//  Shared Preferences
	private SharedPreferences pref;
	
	//  Editor for Shared preferences
	private Editor editor;
	
	//  Context
	private Context _context;
	
	//  Shared pref mode
	private int PRIVATE_MODE = 0;
	
	//  Shared Pref file name
	private String PREF_NAME = "AmericanBarsPrefFile";
	
	//  All Shared Preferences Keys
	//  Make all the variables public to access from outside
	private String IS_LOGIN = "IsLoggedIn";
	public static String KEY_DEVICE_ID = "device_id";	
	public static String KEY_USER_ID = "user_id";
	public static String KEY_UNIQUE_CODE = "unique_code";
	public static String KEY_EMAIL = "email";
	public static String KEY_CHECKBOX_STATUS = "checkbox_status";
	public static String KEY_PROFILE_IMAGE_NAME = "profile_image_name";
	public static String KEY_NAME = "name";
	public static String KEY_PASSWORD = "password";
	//  This variable is used to know whether user is logged in using face book or application credentials.
	public static String KEY_LOGIN_TYPE = "login_type";
	
	/**
	 * Static instance of this class
	 */
	private static SessionManager instanceSessionManager = null;
	
	/**
	 * @purpose To get the static instance of this class
	 * @return instance of this class
	 */
	public static SessionManager getInstance(Context context){
        if(instanceSessionManager == null){
        	instanceSessionManager = new SessionManager(context);
            //Log.i("New Instance", "New Instance");
        }
        return instanceSessionManager;
    }
	
	/**
	 * @purpose To clear the instance of this class
	 */
	public void clearInstance(){
		instanceSessionManager = null;
	}
	
	//  Constructor
	@SuppressLint("CommitPrefEdits") 
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	/**
	 * @purpose To store device id at Login time
	 * @param device_id
	 */
	public void storeDeviceID(String device_id){
		editor.putString(KEY_DEVICE_ID, device_id);
		editor.commit();
	}	
	
	/**
	 * @purpose To store user_id, unique_code, reg_type, first_name, last_name at login time
	 * @param user_id
	 * @param unique_code
	 * @param email
	 * @param imageName 
	 * @param name 
	 * @param password 
	 */
	public void createLoginSession(String user_id, String unique_code, String email, String checkBoxStatus, String imageName, String name, String password, String login_type){
		
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		
		editor.putString(KEY_USER_ID, user_id);
		editor.putString(KEY_UNIQUE_CODE, unique_code);
		editor.putString(KEY_EMAIL, email);
		editor.putString(KEY_CHECKBOX_STATUS, checkBoxStatus);
		editor.putString(KEY_PROFILE_IMAGE_NAME, imageName);
		editor.putString(KEY_NAME, name);
		editor.putString(KEY_PASSWORD, password);
		editor.putString(KEY_LOGIN_TYPE, login_type);
		
		// commit changes
		editor.commit();
	}	
	
	/**
	 * @purpose To check whether user is logged in or not
	 * @return boolean
	 */
	public boolean checkLogin(){
		// Check login status
		if(this.isLoggedIn()){
			//  Means the user is already logged in.
			return true;
		}
		else{
			//  Means the first time log in.
			return false;
		}
	}

	/**
	 * @purpose To get the shared preference key value i.e. To get the stored session data
	 * @param KEY
	 * @return Shared Preference Key Value
	 */
	public String getData(String KEY){
		String data_value = "";
		try {
			data_value = pref.getString(KEY, null) == null ? "" : pref.getString(KEY, null);
		} catch (Exception e) {
			data_value = "";
			e.printStackTrace();
		}
		return data_value;
	}
	
	/**
	 * @purpose To clear all the stored session details i.e. shared preference details
	 */
	public void logoutUser(){
		// Clearing all data from Shared Preferences
		//editor.clear();
		
		//  Now, we have to clear all the data except email id
		editor.remove(IS_LOGIN);
		//editor.remove(KEY_DEVICE_ID);
		editor.remove(KEY_USER_ID);
		editor.remove(KEY_UNIQUE_CODE);
		editor.remove(KEY_PROFILE_IMAGE_NAME);
		editor.remove(KEY_NAME);
		editor.remove(KEY_LOGIN_TYPE);
		/*private String IS_LOGIN = "IsLoggedIn";
		public static String KEY_DEVICE_ID = "device_id";	
		public static String KEY_USER_ID = "user_id";
		public static String KEY_UNIQUE_CODE = "unique_code";
		//public static String KEY_EMAIL = "email";
		//public static String KEY_CHECKBOX_STATUS = "checkbox_status";
		public static String KEY_PROFILE_IMAGE_NAME = "	profile_image_name";
		public static String KEY_NAME = "name";
		//public static String KEY_PASSWORD = "password";
		public static String KEY_LOGIN_TYPE = "login_type";*/
		
		editor.commit();
	}
	
	/**
	 * @purpose To check whether user is logged in or not
	 * @return Login State Value
	 */
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}
	
	/**
	 * @purpose To get all the shared preference key values 
	 */
	/*public void getAllSharedPreferenceData(){
		//  Get all the Shared preference values
				
		Map<String,?> keys = pref.getAll();
		for(Map.Entry<String,?> entry : keys.entrySet()){
			Log.i("map values",entry.getKey() + ": " +entry.getValue().toString());            
		 }
	}*/
	
	/**
	 * @purpose To update the first_name, last_name  and profile image after profile details updated from my profile screen
	 * @param imageName 
	 * @param name 
	 */
	public void updateSessionNameProfilePicture(String imageName, String name) {
		editor.putString(KEY_PROFILE_IMAGE_NAME, imageName);
		editor.putString(KEY_NAME, name);
		// commit changes
		editor.commit();
	}	
}
