package com.spaculus.americanbars;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.GoogleMap;
import com.spaculus.beans.Bar;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.GPSTracker;
import com.spaculus.helpers.Utils;
import com.spaculus.helpers.Validation;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

public class BarSearchHappyHoursActivity extends BaseActivity {

	private Button buttonDay;
	private ScrollView scrollView;
	private Button btnSearch;
	@SuppressWarnings("unused")
	private GoogleMap googleMap;
	@SuppressWarnings("unused")
	private GPSTracker gps;
	@SuppressWarnings("unused")
	private double latitude;
	@SuppressWarnings("unused")
	private double longitude;
	@SuppressWarnings("unused")
	private HashMap<String, String> dataSet;
	@SuppressWarnings("unused")
	private String response = "";
	//private AsyncSearch asyncSearch = null;
	@SuppressWarnings("unused")
	private ArrayList<Bar> barList = null;
	private Button btnStartTypingName;
	private Spinner spinner;
	private String[] days = {
			ConfigConstants.Constants.CONSTANT_NONE,
			ConfigConstants.Constants.CONSTANT_MONDAY,
			ConfigConstants.Constants.CONSTANT_TUESDAY,
			ConfigConstants.Constants.CONSTANT_WEDNESDAY,
			ConfigConstants.Constants.CONSTANT_THURSDAY,
			ConfigConstants.Constants.CONSTANT_FRIDAY,
			ConfigConstants.Constants.CONSTANT_SATURDAY,
			ConfigConstants.Constants.CONSTANT_SUNDAY};
	private EditText edtAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_bar_search_happy_another_location);
			
			/*createActionBar(ConfigConstants.getInstance().FIND_BAR, R.layout.custom_actionbar, BarSearchHappyHoursActivity.this, true);*/
			createActionBar(ConfigConstants.Constants.SEARCH_FOR_HAPPY_HOUR, R.layout.custom_actionbar, BarSearchHappyHoursActivity.this, true);
			setActionBarFromChild(true, true, false, true, true);
			mappedAllViews();
			ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, days);
			adapter_state.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
			spinner.setAdapter(adapter_state);
			btnSearch.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//  Now, first of all check all the validations
					String validationResponse  = Validation.getInstance().findHappyHoursAndEventsValidation(btnStartTypingName.getText().toString().trim(), edtAddress.getText().toString().trim(), 
							buttonDay.getText().toString().trim(), getResources().getString(R.string.txt_select_day));
					
					//  Means all the fields are properly entered
					if(validationResponse.isEmpty()) {
						/* Now, check whether day is selected or not. */
						if(buttonDay.getText().toString().trim().equals(getResources().getString(R.string.txt_select_day))) {
							redirectBarSearchListByLocationActivity(BarSearchHappyHoursActivity.this, btnStartTypingName.getText().toString().trim(), edtAddress.getText().toString(), "");
						}
						else {
							redirectBarSearchListByLocationActivity(BarSearchHappyHoursActivity.this, btnStartTypingName.getText().toString().trim(), edtAddress.getText().toString(), buttonDay.getText().toString());	
						}
					}
					else {
						//  Show respective validation message
						Utils.toastLong(BarSearchHappyHoursActivity.this, validationResponse);
					}
				}
			});
			btnStartTypingName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						Intent intent = new Intent(
								BarSearchHappyHoursActivity.this,
								AlphaViewActivity.class);
						intent.putExtra(
								ConfigConstants.Keys.KEY_SEARCH_TEXT,
								btnStartTypingName.getText().toString().trim());
						startActivityForResult(intent,
								ConfigConstants.ResultCodes.REQUEST_CODE_APLHA_VIEW);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			buttonDay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					spinner.performClick();
				}
			});

			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg,
						int position, long arg3) {
					try {
						if(arg0.getItemAtPosition(position).toString().equals(ConfigConstants.Constants.CONSTANT_NONE)) {
							buttonDay.setText(getResources().getString(R.string.txt_select_day));
						}
						else {
							buttonDay.setText(arg0.getItemAtPosition(position).toString());	
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		try {
			super.onConfigurationChanged(newConfig);
			scrollView.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_bar_search));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mappedAllViews() {
		try {
			btnStartTypingName = (Button) findViewById(R.id.btnStartTypingName);
			buttonDay = (Button) findViewById(R.id.buttonDay);
			spinner = (Spinner) findViewById(R.id.spinner);
			btnSearch = (Button) findViewById(R.id.btnSearch);
			scrollView = (ScrollView) findViewById(R.id.scrollView);
			edtAddress = (EditText) findViewById(R.id.etAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);

			if (resultCode != Activity.RESULT_CANCELED) {
				if (requestCode == ConfigConstants.ResultCodes.REQUEST_CODE_APLHA_VIEW) {
					Bundle bundle = data.getExtras();
					String searchText = bundle.getString(ConfigConstants
							.Keys.KEY_SEARCH_TEXT);
					btnStartTypingName.setText(searchText);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
