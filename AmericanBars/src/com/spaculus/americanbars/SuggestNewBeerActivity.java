package com.spaculus.americanbars;

import java.io.File;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.MultipartUtility;
import com.spaculus.helpers.SessionManager;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SuggestNewBeerActivity extends BaseActivityMedia {

	private RelativeLayout relativeLayoutSuggestNewBeer;
	private EditText etBeerTitle, etDescription, etType, etABV, etBrewedBy, etCityProduced, etState, etCountry, etWebsite;
	private ImageView ivBeerImage, ivAddBeerImage;
	private Button buttonSave, buttonCancel;
	private String selectedMediaPath = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_suggest_new_beer);
			
			//  Create Action Bar
			createActionBar("Beer Suggest", R.layout.custom_actionbar, SuggestNewBeerActivity.this, true);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);
			
			// Mapping of all the views
			mappedAllViews();
			
			/* For the EditText Scrolling issue */
			/* Description Field */
			etDescription.setOnTouchListener(new OnTouchListener() {
			    @SuppressLint("ClickableViewAccessibility") 
			    @Override
			    public boolean onTouch(View v, MotionEvent event) {
			        try {
						if (v.getId() == R.id.etDescription) {
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
			relativeLayoutSuggestNewBeer.setOnTouchListener(new OnTouchListener() {
				@SuppressLint("ClickableViewAccessibility") 
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(v==relativeLayoutSuggestNewBeer){
						hideSoftKeyboard();
						return true;
					}
					return false;
				}
			});
			
			//  Upload profile picture click event
			ivAddBeerImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						selectMediaSingle(ConfigConstants.Constants.CONSTANT_MEDIA_PICTURE, 1, CONSTANT_SUGGEST_NEW_BEER_ACTIVITY_SINGLE_SELECTION, SuggestNewBeerActivity.this);
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
						String validationResponse  = Validation.getInstance().suggestNewBeer(etBeerTitle.getText().toString().trim(), etCountry.getText().toString().trim());
						
						//  Means all the fields are properly entered
						if(validationResponse.isEmpty()){
							//  Called the AsyncTask for the Suggest New Beer functionality
							if(Utils.getInstance().isNetworkAvailable(SuggestNewBeerActivity.this)) {		
								new AsynTaskSuggestNewBeerMultiPart().execute();
							}
							else{
								Utils.getInstance().showToastNoInternetAvailable(SuggestNewBeerActivity.this);
							}
						}
						else {
							Utils.toastLong(SuggestNewBeerActivity.this, validationResponse);
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
			relativeLayoutSuggestNewBeer = (RelativeLayout) findViewById(R.id.relativeLayoutSuggestNewBeer);
			etBeerTitle = (EditText) findViewById(R.id.etBeerTitle);
			etDescription = (EditText) findViewById(R.id.etDescription);
			etType = (EditText) findViewById(R.id.etType);
			etABV = (EditText) findViewById(R.id.etABV);
			etBrewedBy = (EditText) findViewById(R.id.etBrewedBy);
			etCityProduced = (EditText) findViewById(R.id.etCityProduced);
			etState = (EditText) findViewById(R.id.etState);
			etCountry = (EditText) findViewById(R.id.etCountry);
			etWebsite = (EditText) findViewById(R.id.etWebsite);
			ivBeerImage = (ImageView)findViewById(R.id.ivBeerImage);
			ivAddBeerImage = (ImageView)findViewById(R.id.ivAddBeerImage);
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
			//Log.i("Method hideSoftKeyboard","Method hideSoftKeyboard");
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etBeerTitle.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etDescription.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etType.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etABV.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etBrewedBy.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etCityProduced.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etState.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etCountry.getWindowToken(), 0);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* The following method is used to set the profile picture. */
	public void setProfilePicture (String mediaPath) {
		selectedMediaPath = mediaPath;
		try {
			Utils.getInstance().setImageDevice(SuggestNewBeerActivity.this, selectedMediaPath, ivBeerImage);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask for the Suggest New Beer Functionality
	public class AsynTaskSuggestNewBeerMultiPart extends AsyncTask<Void, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(SuggestNewBeerActivity.this);
    	private String responseString = "";
    	private String status = "";
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
	            responseString  = ConfigConstants.Urls.beersuggest;
	        
	        	MultipartUtility multipart = new MultipartUtility(responseString, charset);
	        	if(!selectedMediaPath.isEmpty()) {
	        		multipart.addFilePart("beer_image", uploadFile);
	        	}
	        	multipart.addFormField("user_id", SessionManager.getInstance(SuggestNewBeerActivity.this).getData(SessionManager.KEY_USER_ID));
	        	multipart.addFormField("device_id", SessionManager.getInstance(SuggestNewBeerActivity.this).getData(SessionManager.KEY_DEVICE_ID));
	        	multipart.addFormField("unique_code", SessionManager.getInstance(SuggestNewBeerActivity.this).getData(SessionManager.KEY_UNIQUE_CODE));
	        	multipart.addFormField("beer_name", etBeerTitle.getText().toString().trim());
	        	multipart.addFormField("beer_desc", etDescription.getText().toString().trim());
	        	multipart.addFormField("beer_type", etType.getText().toString().trim());
	        	multipart.addFormField("abv", etABV.getText().toString().trim());
	        	multipart.addFormField("producer", etBrewedBy.getText().toString().trim());
	        	multipart.addFormField("city_produced", etCityProduced.getText().toString().trim());
	        	multipart.addFormField("beer_state", etState.getText().toString().trim());
	        	multipart.addFormField("beer_country", etCountry.getText().toString().trim());
	        	multipart.addFormField("beer_website", etWebsite.getText().toString().trim());
	        	
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
					status = Utils.getInstance().isTagExists(jsonParentObj, "status");
				}
				String message = "";
				if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)) {
					message = "Your beer suggestion sent successfully. Please wait for admin approval.";
				}
				else {
					message = "Your beer suggestion is not sent. Please try again.";
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(SuggestNewBeerActivity.this);
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
