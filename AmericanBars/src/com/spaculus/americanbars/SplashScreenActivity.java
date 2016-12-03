package com.spaculus.americanbars;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.spaculus.helpers.SessionManager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
//import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;

public class SplashScreenActivity extends BaseActivity implements Runnable {
	private static int SPLASH_TIME_OUT = 2000;
	private Handler mHandler = null;
	private LinearLayout linearLayoutSplashScreen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			getDeviceID();
			setContentView(R.layout.activity_splash_screen);
			mappedAllViews();
			
			/*
			 * This code is used to get the Hash key for the Facebook Registration.
			 */
			/*try {
				PackageInfo info = getPackageManager().getPackageInfo("com.spaculus.americanbars",PackageManager.GET_SIGNATURES);
				for (Signature signature : info.signatures) {
					MessageDigest md = MessageDigest.getInstance("SHA");
					md.update(signature.toByteArray());
					Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}*/

			runSplashScreen();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		try {
			super.onConfigurationChanged(newConfig);
			linearLayoutSplashScreen.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.splash_screen));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mappedAllViews() {
		try {
			linearLayoutSplashScreen = (LinearLayout) findViewById(R.id.linearLayoutSplashScreen);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void runSplashScreen() {
		try {
			mHandler = new Handler();
			mHandler.postDelayed(this, SPLASH_TIME_OUT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		try {
			super.onDestroy();
			mHandler.removeCallbacks(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			registerDeviceToken();
			/* Now, first of all check whether user is logged in or not. */
			if(SessionManager.getInstance(SplashScreenActivity.this).isLoggedIn()) {
				redirectMainActivity(SplashScreenActivity.this);
			}
			else {
				redirectLoginActivity(SplashScreenActivity.this, false);
			}
			//  Close an activity
			finish();
			onDestroy();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void registerDeviceToken() {
		try {
			@SuppressWarnings("unused")
			int resultCode = GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(this.getApplicationContext());
			//if (resultCode == ConnectionResult.SUCCESS) {
				startService(new Intent(SplashScreenActivity.this,
						RegistrationIntentService.class));
			/*} else if (resultCode == ConnectionResult.SERVICE_MISSING
					|| resultCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED
					|| resultCode == ConnectionResult.SERVICE_DISABLED) {
				Toast.makeText(SplashScreenActivity.this,
						"Please Install Google Play Services",
						Toast.LENGTH_SHORT).show();
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getDeviceID() {
		try {
			//Log.i("getDeviceID() method called.", "getDeviceID() method called.");
			try {
				TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				
				if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
					// Means the device has not calling facilities. So need to get the MacId
					WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
					WifiInfo wInfo = wifiManager.getConnectionInfo();
					SessionManager.getInstance(SplashScreenActivity.this).storeDeviceID("Mac-" + wInfo.getMacAddress());
					//Log.i("Tag(MacID):", "Mac-" + wInfo.getMacAddress());
				} 
				else {
					// Getting the IMEINumber
					SessionManager.getInstance(SplashScreenActivity.this).storeDeviceID("IMEI-" + tm.getDeviceId());
					//Log.i("Tag(IMEI):", "IMEI-" + tm.getDeviceId());
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
				//Log.i("AsyncTask,Login,Excpetion is:",Utils.isStringNull(e.getMessage()));
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
