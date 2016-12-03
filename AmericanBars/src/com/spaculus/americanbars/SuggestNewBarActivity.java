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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import br.com.sapereaude.maskedEditText.MaskedEditText;

public class SuggestNewBarActivity extends BaseActivityMedia {

	private RelativeLayout relativeLayoutSuggestNewBar;
	private EditText etBarName, etAddress, etCity, etState, etZipCode;
	private MaskedEditText etPhoneNumber;
	private Button buttonSave, buttonCancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_suggest_new_bar);
			
			//  Create Action Bar
			createActionBar("Bar Suggest", R.layout.custom_actionbar, SuggestNewBarActivity.this, true);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);
			
			// Mapping of all the views
			mappedAllViews();
			
			/* For the EditText Scrolling issue */
			/* Description Field */
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
			relativeLayoutSuggestNewBar.setOnTouchListener(new OnTouchListener() {
				@SuppressLint("ClickableViewAccessibility") 
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(v==relativeLayoutSuggestNewBar){
						hideSoftKeyboard();
						return true;
					}
					return false;
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
						String validationResponse  = Validation.getInstance().suggestNewBar(etBarName.getText().toString().trim(), etAddress.getText().toString().trim(), etCity.getText().toString().trim(), etState.getText().toString().trim(),
								etPhoneNumber.getText().toString().trim(), etZipCode.getText().toString().trim());
						
						//  Means all the fields are properly entered
						if(validationResponse.isEmpty()){
							//  Called the AsyncTask for the Suggest New Bar functionality
							if(Utils.getInstance().isNetworkAvailable(SuggestNewBarActivity.this)) {		
								new AsynTaskSuggestNewBar().execute();
							}
							else{
								Utils.getInstance().showToastNoInternetAvailable(SuggestNewBarActivity.this);
							}
						}
						else {
							Utils.toastLong(SuggestNewBarActivity.this, validationResponse);
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
			relativeLayoutSuggestNewBar = (RelativeLayout) findViewById(R.id.relativeLayoutSuggestNewBar);
			etBarName = (EditText) findViewById(R.id.etBarName);
			etAddress = (EditText) findViewById(R.id.etAddress);
			etCity = (EditText) findViewById(R.id.etCity);
			etState = (EditText) findViewById(R.id.etState);
			etPhoneNumber = (MaskedEditText) findViewById(R.id.etPhoneNumber);
			etZipCode = (EditText) findViewById(R.id.etZipCode);
			buttonSave = (Button)findViewById(R.id.btnSave);
			buttonCancel = (Button)findViewById(R.id.btnCancel);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  Hide the soft key board on the click of Layout
	private void hideSoftKeyboard() {
		try {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etBarName.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etAddress.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etCity.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etState.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etPhoneNumber.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etZipCode.getWindowToken(), 0);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask for the Suggest New Bar Functionality
	public class AsynTaskSuggestNewBar extends AsyncTask<Void, Void, Void> {
		private ProgressDialog pd = new ProgressDialog(SuggestNewBarActivity.this);
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
		        nameValuePairs.add(new BasicNameValuePair("bar_name", etBarName.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("address", etAddress.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("city", etCity.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("state", etState.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("phone_number", etPhoneNumber.getText().toString().trim()));
		        nameValuePairs.add(new BasicNameValuePair("zip_code", etZipCode.getText().toString().trim()));
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(ConfigConstants.Urls.suggest_bar, ServiceHandler.POST, nameValuePairs);
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
				}
				String message = "";
				if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)) {
					message = "Your bar suggestion sent successfully. Please wait for admin approval.";
				}
				else {
					message = "Your bar suggestion is not sent. Please try again.";
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(SuggestNewBarActivity.this);
				builder.setCancelable(true);
				builder.setMessage(message);
				builder.setInverseBackgroundForced(true);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						/* Now, simply finish and close this activity. */
						finish();
						onDestroy();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
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
