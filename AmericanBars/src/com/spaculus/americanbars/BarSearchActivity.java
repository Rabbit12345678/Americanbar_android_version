package com.spaculus.americanbars;

import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.Utils;
import com.spaculus.helpers.Validation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class BarSearchActivity extends BaseActivity{

	private RelativeLayout relativeLayoutFindBar;
	private Button buttonStartTypingName;
	private EditText etState, etCity, etZipCode;
	private Button buttonStartYourSearch;
	private ScrollView scrollView;
	
	//  For the Web service response
	//  Implement Paging functionality to get the people list
	//  current_page = page_number = offset(in web service method)
	//private int offset = 0; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_bar_search);
			
			//  Create Action Bar
			//createActionBar(ConfigConstants.getInstance().FIND_BAR, R.layout.custom_actionbar, BarSearchActivity.this, true);
			createActionBar(ConfigConstants.Constants.BAR_SEARCH, R.layout.custom_actionbar, BarSearchActivity.this, true);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);
			
				//  Mapping of all the views
				mappedAllViews();
				
				//  To hide the soft key board on the click of anywhere onto the screen 
				relativeLayoutFindBar.setOnTouchListener(new OnTouchListener() {
					@SuppressLint("ClickableViewAccessibility") 
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(v==relativeLayoutFindBar){
							hideSoftKeyboard();
							return true;
						}
						return false;
					}
				});
				
				//  Button Start Typing Name click event
				buttonStartTypingName.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						try {
							Intent intent = new Intent(BarSearchActivity.this, AlphaViewActivity.class);
							intent.putExtra(ConfigConstants.Keys.KEY_SEARCH_TEXT, buttonStartTypingName.getText().toString().trim());
							startActivityForResult(intent, ConfigConstants.ResultCodes.REQUEST_CODE_APLHA_VIEW);
						} 
						catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				
				//  Button Start Your Search Activity
				buttonStartYourSearch.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						try {
							//  First, hide the soft key board.
							hideSoftKeyboard();
							
							//  Now, first of all check all the validations
							String validationResponse  = Validation.getInstance().findBarValidation(buttonStartTypingName.getText().toString().trim(), etState.getText().toString().trim(), 
									etCity.getText().toString().trim(), etZipCode.getText().toString().trim());
							
							//  Means all the fields are properly entered
							if(validationResponse.isEmpty()) {
								redirectBarSearchListActivity(BarSearchActivity.this, buttonStartTypingName.getText().toString().trim(), etState.getText().toString().trim(), 
										etCity.getText().toString().trim(), etZipCode.getText().toString().trim());
							}
							else {
								//  Show respective validation message
								//Toast.makeText(BarSearchActivity.this, validationResponse, Toast.LENGTH_SHORT).show();
								Utils.toastLong(BarSearchActivity.this, validationResponse);
							}
						} 
						catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					}
				});
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		try {
			super.onConfigurationChanged(newConfig);
			scrollView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_bar_search));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			relativeLayoutFindBar = (RelativeLayout) findViewById(R.id.relativeLayoutFindBar);
			buttonStartTypingName = (Button) findViewById(R.id.btnStartTypingName);
			etState = (EditText) findViewById(R.id.etState);
			etCity = (EditText) findViewById(R.id.etCity);
			etZipCode = (EditText) findViewById(R.id.etZipCode);
			buttonStartYourSearch = (Button)findViewById(R.id.btnStartYourSearch);
			scrollView = (ScrollView)findViewById(R.id.scrollView);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//  Hide the soft key board on the click of Layout
	private void hideSoftKeyboard(){
		try {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etState.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etCity.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etZipCode.getWindowToken(), 0);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);
			
			if(resultCode!=Activity.RESULT_CANCELED){
				if(requestCode==ConfigConstants.ResultCodes.REQUEST_CODE_APLHA_VIEW){
					
					//  Get the Search Bar Text data here.
					Bundle bundle = data.getExtras();
					String searchText = bundle.getString(ConfigConstants.Keys.KEY_SEARCH_TEXT);
					//  Now, set the text
					buttonStartTypingName.setText(searchText);;
				}
			}
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
