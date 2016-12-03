package com.spaculus.americanbars;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import com.google.android.gms.maps.GoogleMap;
import com.spaculus.beans.Bar;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.GPSTracker;

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

public class BarSearchHappyHoursGPSActivity extends BaseActivity {

	private EditText btnDay;
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
	private String response = "";
	@SuppressWarnings("unused")
	//private AsyncSearch asyncSearch = null;
	private ArrayList<Bar> barList = null;
	private Button btnStartTypingName;
	private Spinner spinner;
	private String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_bar_search_happy_gps_location);
			/*createActionBar(ConfigConstants.getInstance().FIND_BAR, R.layout.custom_actionbar, BarSearchHappyHoursGPSActivity.this, true);*/
			createActionBar(ConfigConstants.Constants.SEARCH_FOR_HAPPY_HOUR, R.layout.custom_actionbar, BarSearchHappyHoursGPSActivity.this, true);
			setActionBarFromChild(true, true, false, true, true);
			mappedAllViews();
			ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, days);
			adapter_state.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
			spinner.setAdapter(adapter_state);
			
			btnSearch.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					redirectBarSearchAroundMeActivity(BarSearchHappyHoursGPSActivity.this, ConfigConstants.Constants.CONSTANT_SECOND, btnDay.getText().toString().trim());
					/*Intent intent = new Intent(BarSearchHappyHoursGPSActivity.this,BarSearchAroundMeActivity.class);
					intent.putExtra("flag", "second");
					startActivity(intent);*/
				}
			});
			btnStartTypingName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					try {
						Intent intent = new Intent(
								BarSearchHappyHoursGPSActivity.this,
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

			btnDay.setOnClickListener(new OnClickListener() {

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
						btnDay.setText(arg0.getItemAtPosition(position)
								.toString());
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
			btnDay = (EditText) findViewById(R.id.btDay);
			spinner = (Spinner) findViewById(R.id.spinner);
			btnSearch = (Button) findViewById(R.id.btnSearch);
			scrollView = (ScrollView) findViewById(R.id.scrollView);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/*	public class AsyncSearch extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog = null;
		@SuppressWarnings("unused")
		private String status = "";

		@Override
		protected void onPreExecute() {
			dataSet = new HashMap<String, String>();
			dataSet.put(
					"user_id",
					SessionManager.getInstance(
							BarSearchHappyHoursGPSActivity.this).getData(
							SessionManager.KEY_USER_ID));
			dataSet.put(
					"device_id",
					SessionManager.getInstance(
							BarSearchHappyHoursGPSActivity.this).getData(
							SessionManager.KEY_DEVICE_ID));
			dataSet.put(
					"unique_code",
					SessionManager.getInstance(
							BarSearchHappyHoursGPSActivity.this).getData(
							SessionManager.KEY_UNIQUE_CODE));
			dataSet.put("limit", "");
			dataSet.put("offset", "");
			dataSet.put("state", "");
			dataSet.put("city", "");
			dataSet.put("zipcode", "");
			dataSet.put("order_by", "");
			dataSet.put("address_j", "");
			dataSet.put("days", btnDay.getText().toString());
			 dataSet.put("lat", String.valueOf(latitude));
			 dataSet.put("lang", String.valueOf(longitude));
			//dataSet.put("lat", ConfigConstants.lat);
			//dataSet.put("lang", ConfigConstants.lang);
			dialog = new ProgressDialog(BarSearchHappyHoursGPSActivity.this);
			dialog.setTitle("");
			dialog.setMessage("Please Wait");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String getResponse = performPostCall(
					ConfigConstants.getInstance().webServiceURL
							+ ConfigConstants.getInstance().bar_lists, dataSet);
			return getResponse;
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			asyncSearch = null;
			try {
				if (!response.equals("")) {
					JSONObject response = new JSONObject(result);
					status = response.getString("status");
					JSONObject jsonObjectBarList = response
							.getJSONObject("barlist");
					JSONArray jsonArrayResult = jsonObjectBarList
							.getJSONArray("result");
					for (int i = 0; i < jsonArrayResult.length(); i++) {
						JSONObject jsonObj = jsonArrayResult.getJSONObject(i);
						barList.add(new Bar(jsonObj.getString("bar_id"),
								jsonObj.getString("bar_title"), jsonObj
										.getString("bar_type"), jsonObj
										.getString("bar_desc"), jsonObj
										.getString("owner_id"), jsonObj
										.getString("address"), jsonObj
										.getString("city"), jsonObj
										.getString("state"), jsonObj
										.getString("phone"), jsonObj
										.getString("zipcode"), jsonObj
										.getString("email"), jsonObj
										.getString("bar_logo"), jsonObj
										.getString("total_rating"), jsonObj
										.getString("total_commnets"), jsonObj
										.getString("lat"), jsonObj
										.getString("lang")));
					}
				}
				showMapCurrent(googleMap, latitude, longitude, "Name",
						ConfigConstants.getInstance().USA);
				addMarkers(googleMap, barList,BarSearchHappyHoursGPSActivity.this,R.drawable.marker);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
*/
	public String performPostCall(String requestURL,
			HashMap<String, String> postDataParams) {
		URL url;
		response = "";
		try {
			url = new URL(requestURL);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(15000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					os, "UTF-8"));
			writer.write(getPostDataString(postDataParams));

			writer.flush();
			writer.close();
			os.close();
			int responseCode = conn.getResponseCode();

			if (responseCode == HttpsURLConnection.HTTP_OK) {
				String line;
				BufferedReader br = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				while ((line = br.readLine()) != null) {
					response += line;
				}
			} else {
				response = "";

			}
		} catch (Exception e) {
			e.printStackTrace();
			response = "";
		}

		return response;
	}

	private String getPostDataString(HashMap<String, String> params)
			throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}
		return result.toString();
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
