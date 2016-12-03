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
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.spaculus.adapters.CustomBarAdapter;
import com.spaculus.beans.Bar;
import com.spaculus.beans.BarEvent;
import com.spaculus.beans.HappyHours;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.GPSTracker;
import com.spaculus.helpers.ParseJsonObject;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class BarSearchAroundMeActivity extends BaseActivity {

	private GoogleMap googleMap;
	private GPSTracker gps;
	private double latitude;
	private double longitude;
	private HashMap<String, String> dataSet;
	private String response = "";
	private AsyncSendLocation asyncSendLocation = null;

	/* For the Switch functionality */
	private RelativeLayout relativeLayoutMap;
	private ImageView ivSwitch;
	private ListView listViewBars;
	private boolean isMapVisible = true;

	// For the Bar List
	private ArrayList<Bar> barList = null;
	private CustomBarAdapter adapter = null;
	private ArrayList<HappyHours> happyHoursList = null;

	private String actionBarTitle = "";

	/* To get the values from the intent */
	private String isRedirectedFrom = "";
	private String selectedDay = "";

	/* Suggest New Bar functionality */
	private Button buttonSuggestNewBar;
	
	/* For the Admin Events functionality */
	private ArrayList<BarEvent> adminEventsList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_search_aroundme);

			/* To get the values */
			Bundle b = getIntent().getExtras();
			isRedirectedFrom = b.getString(ConfigConstants.Keys.KEY_IS_REDIRECTED_FROM);
			selectedDay = b.getString(ConfigConstants.Keys.KEY_DAY);

			if (isDirectAroundMe()) {
				actionBarTitle = ConfigConstants.Constants.BARS_NEAR_YOU;
			} else {
				actionBarTitle = ConfigConstants.Constants.HAPPY_HOURS_NEAR_YOU;
			}

			/*
			 * createActionBar(ConfigConstants.getInstance().FIND_BAR,
			 * R.layout.custom_actionbar, BarSearchAroundMeActivity.this, true);
			 */
			createActionBar(actionBarTitle, R.layout.custom_actionbar, BarSearchAroundMeActivity.this, true);
			setActionBarFromChild(true, true, false, true, true);
			gps = new GPSTracker(BarSearchAroundMeActivity.this);
			if (gps.canGetLocation()) {
				latitude = gps.getLatitude();
				longitude = gps.getLongitude();
				/*
				 * Toast.makeText( getApplicationContext(),
				 * "Your Location is - \nLat: " + latitude + "\nLong: " +
				 * longitude, Toast.LENGTH_LONG).show();
				 */
				if (isDirectAroundMe()) {
					dataSet = new HashMap<String, String>();
					dataSet.put("user_id", SessionManager.getInstance(BarSearchAroundMeActivity.this)
							.getData(SessionManager.KEY_USER_ID));
					dataSet.put("device_id", SessionManager.getInstance(BarSearchAroundMeActivity.this)
							.getData(SessionManager.KEY_DEVICE_ID));
					dataSet.put("unique_code", SessionManager.getInstance(BarSearchAroundMeActivity.this)
							.getData(SessionManager.KEY_UNIQUE_CODE));
					dataSet.put("limit", "");
					dataSet.put("offset", "");
					dataSet.put("state", "");
					dataSet.put("city", "");
					dataSet.put("zipcode", "");
					dataSet.put("title", "");
					dataSet.put("order_by", "bar_title#ASC");
					dataSet.put("address_j", "");
					dataSet.put("days", "");
					dataSet.put("lat", String.valueOf(latitude));
					dataSet.put("lang", String.valueOf(longitude));
					/*dataSet.put("lat", ConfigConstants.lat);
					dataSet.put("lang", ConfigConstants.lang);*/
				} else {
					dataSet = new HashMap<String, String>();
					dataSet.put("user_id", SessionManager.getInstance(BarSearchAroundMeActivity.this)
							.getData(SessionManager.KEY_USER_ID));
					dataSet.put("device_id", SessionManager.getInstance(BarSearchAroundMeActivity.this)
							.getData(SessionManager.KEY_DEVICE_ID));
					dataSet.put("unique_code", SessionManager.getInstance(BarSearchAroundMeActivity.this)
							.getData(SessionManager.KEY_UNIQUE_CODE));
					dataSet.put("limit", "");
					dataSet.put("offset", "");
					dataSet.put("state", "");
					dataSet.put("city", "");
					dataSet.put("zipcode", "");
					dataSet.put("title", "");
					dataSet.put("order_by", "bar_title#ASC");
					dataSet.put("address_j", "");
					dataSet.put("days", selectedDay);
					dataSet.put("lat", String.valueOf(latitude));
					dataSet.put("lang", String.valueOf(longitude));
					/*dataSet.put("lat", ConfigConstants.lat);
					dataSet.put("lang", ConfigConstants.lang);*/
				}
				if (Utils.getInstance().isNetworkAvailable(BarSearchAroundMeActivity.this)) {
					if (asyncSendLocation == null) {
						asyncSendLocation = new AsyncSendLocation();
						asyncSendLocation.execute();
					}
				} else {
					Utils.getInstance().showToastNoInternetAvailable(BarSearchAroundMeActivity.this);
				}
			} else {
				gps.showSettingsAlert();
			}
			mappedAllViews();

			/* Click on Switch Image for switching a view */
			ivSwitch.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						if (isMapVisible) {
							isMapVisible = false;
							ivSwitch.setImageResource(R.drawable.switch_map);
							relativeLayoutMap.setVisibility(View.GONE);
							listViewBars.setVisibility(View.VISIBLE);
						} else {
							isMapVisible = true;
							ivSwitch.setImageResource(R.drawable.switch_listing);
							relativeLayoutMap.setVisibility(View.VISIBLE);
							listViewBars.setVisibility(View.GONE);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			// ListView row click event
			listViewBars.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					try {
						// Now, redirect to the Full Mug Bar or Half Mug Bar
						// Details Screen
						redirectFullHalfMugBarDetailsActivity(BarSearchAroundMeActivity.this,
								barList.get(position).getId(), barList.get(position).getType());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			buttonSuggestNewBar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						redirectSuggestNewBarActivity(BarSearchAroundMeActivity.this);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mappedAllViews() {
		try {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			googleMap.getUiSettings().setZoomControlsEnabled(true);
			relativeLayoutMap = (RelativeLayout) findViewById(R.id.relativeLayoutMap);
			ivSwitch = (ImageView) findViewById(R.id.ivSwitch);
			listViewBars = (ListView) findViewById(R.id.listViewBars);
			buttonSuggestNewBar = (Button) findViewById(R.id.buttonSuggestNewBar);

			// Initialize adapter and array list here.
			barList = new ArrayList<Bar>();
			adapter = new CustomBarAdapter(BarSearchAroundMeActivity.this, R.layout.activity_bar_search_list_item, barList);
			listViewBars.setAdapter(adapter);
			/* For the Admin Events functionality */
			adminEventsList = new ArrayList<BarEvent>();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String performPostCall(String requestURL, HashMap<String, String> postDataParams) {
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
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(getPostDataString(postDataParams));

			writer.flush();
			writer.close();
			os.close();
			int responseCode = conn.getResponseCode();

			if (responseCode == HttpsURLConnection.HTTP_OK) {
				String line;
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
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

	private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
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

	public class AsyncSendLocation extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog = null;
		private String status = "";
		private int totalRecords = 0;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(BarSearchAroundMeActivity.this);
			dialog.setTitle("");
			dialog.setMessage("Please Wait");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String getResponse = performPostCall(ConfigConstants.Urls.bar_lists, dataSet);
			return getResponse;
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			asyncSendLocation = null;
			try {
				if (!response.equals("")) {
					JSONObject response = new JSONObject(result);
					status = response.getString("status");

					if (status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)) {
						/* For the Bar List */
						totalRecords = response.getInt("barlist_total");
						if (totalRecords > 0) {
							JSONObject jsonObjectBarList = response.getJSONObject("barlist");
							JSONArray jsonArrayResult = jsonObjectBarList.getJSONArray("result");
							for (int i = 0; i < jsonArrayResult.length(); i++) {
								JSONObject jsonObj = jsonArrayResult.getJSONObject(i);
								barList.add(new Bar(Utils.getInstance().isTagExists(jsonObj, "bar_id"),
										Utils.getInstance().isTagExists(jsonObj, "bar_title"),
										Utils.getInstance().isTagExists(jsonObj, "bar_type"),
										Utils.getInstance().isTagExists(jsonObj, "bar_desc"),
										Utils.getInstance().isTagExists(jsonObj, "owner_id"),
										Utils.getInstance().isTagExists(jsonObj, "address"),
										Utils.getInstance().isTagExists(jsonObj, "city"),
										Utils.getInstance().isTagExists(jsonObj, "state"),
										Utils.getInstance().isTagExists(jsonObj, "phone"),
										Utils.getInstance().isTagExists(jsonObj, "zipcode"),
										Utils.getInstance().isTagExists(jsonObj, "email"),
										Utils.getInstance().isTagExists(jsonObj, "bar_logo"),
										Utils.getInstance().isTagExists(jsonObj, "total_rating"),
										Utils.getInstance().isTagExists(jsonObj, "total_commnets"),
										Utils.getInstance().isTagExists(jsonObj, "lat"),
										Utils.getInstance().isTagExists(jsonObj, "lang")));
							}

							/* Now, for the Happy Hours Data */
							if (!isDirectAroundMe()) {
								JSONObject jsonObjHappyHours = response.getJSONObject("bar_happy_hours");
								JSONArray jsonArrayResultHappyHours = jsonObjHappyHours.getJSONArray("result");
								happyHoursList = new ArrayList<HappyHours>();
								ParseJsonObject objParseJsonObject = new ParseJsonObject();
								for (int i = 0; i < jsonArrayResultHappyHours.length(); i++) {
									JSONObject jsonObjHappyHour = jsonArrayResultHappyHours.getJSONObject(i);
									happyHoursList.add(objParseJsonObject.addHappyHoursObject(jsonObjHappyHour));
								}

								/*
								 * A method is used to set the Happy Hours data
								 * for the bar list.
								 */
								setHappyHoursForBar();
							}
						}
						
						/* For the Admin Events */
						if (!isDirectAroundMe()) {
							/* Now, here get the Admin events data */
							if (response.has("bar_events")) {
								JSONObject jsonObjBarEvents = response.getJSONObject("bar_events");
								if (jsonObjBarEvents.has("result")) {
									if (!jsonObjBarEvents.isNull("result")) {
										JSONArray jsonArrayEventsResult = jsonObjBarEvents.getJSONArray("result");
										for (int i = 0; i < jsonArrayEventsResult.length(); i++) {
											JSONObject jsonObj = jsonArrayEventsResult.getJSONObject(i);
											adminEventsList.add(new ParseJsonObject().addBarEventObject(jsonObj));
										}
									}
								}
							}
						}
						
						if(barList.size()+adminEventsList.size() > 0) {
							/*latitude = Double.parseDouble(ConfigConstants.lat);
							longitude = Double.parseDouble(ConfigConstants.lang);*/
							showMapCurrent(googleMap, latitude, longitude, "Name", ConfigConstants.Constants.USA);
							/* To show the current loction on the map */
							// showMapCurrent(googleMap, latitude, longitude,
							// ConfigConstants.CONSTANT_I_AM_HERE);
							@SuppressWarnings("unused")
							int res = 0;
							if (isDirectAroundMe()) {
								// res=R.drawable.map_full_mug;
								res = R.drawable.half_mug;
							} else {
								res = R.drawable.map_martini_glass;
							}

							/* For the Map - Show Markers functionality */
							List<Object> mapList = new ArrayList<Object>();
							for (int i = 0; i < barList.size(); i++) {
								mapList.add(barList.get(i));
							}
							for (int i = 0; i < adminEventsList.size(); i++) {
								mapList.add(adminEventsList.get(i));
							}
							/* Add markers onto the map */
							addMarkers(googleMap, BarSearchAroundMeActivity.this, isRedirectedFrom, mapList, isDirectAroundMe());

							/* Now, notify an adapter here. */
							adapter.notifyDataSetChanged();
							if(isDirectAroundMe()) {
								buttonSuggestNewBar.setVisibility(View.VISIBLE);	
							}
						}
						else {
							Utils.showToastLong(BarSearchAroundMeActivity.this, ConfigConstants.Messages.noRecordFound);
							if(isDirectAroundMe()) {
								buttonSuggestNewBar.setVisibility(View.VISIBLE);	
							}
						}
					} 
					else {
						Utils.showToastLong(BarSearchAroundMeActivity.this, ConfigConstants.Messages.noRecordFound);
						if(isDirectAroundMe()) {
							buttonSuggestNewBar.setVisibility(View.VISIBLE);	
						}
					}
				} 
				else {
					Utils.showToastLong(BarSearchAroundMeActivity.this, ConfigConstants.Messages.noRecordFound);
					if(isDirectAroundMe()) {
						buttonSuggestNewBar.setVisibility(View.VISIBLE);	
					}
				}
			} 
			catch (JSONException e) {
				e.printStackTrace();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * A method is used to know whether user is redirect here direct from the
	 * Around Me or Happy Hours Around Me.
	 */
	private boolean isDirectAroundMe() {
		boolean isDirectAroundMe = false;
		if (isRedirectedFrom.equals(ConfigConstants.Constants.CONSTANT_FIRST)) {
			isDirectAroundMe = true;
		} else {
			isDirectAroundMe = false;
		}
		return isDirectAroundMe;
	}

	/* A method is used to set the Happy Hours data for the bar list. */
	private void setHappyHoursForBar() {
		String happyHoursString = "";
		for (int i = 0; i < barList.size(); i++) {
			for (int j = 0; j < happyHoursList.size(); j++) {
				if (barList.get(i).getId().equals(happyHoursList.get(j).getBar_id())) {
					if (happyHoursString.isEmpty()) {
						happyHoursString = happyHoursList.get(j).getHour_from() + " - "
								+ happyHoursList.get(j).getHour_to();
					} else {
						happyHoursString += ", " + happyHoursList.get(j).getHour_from() + " - "
								+ happyHoursList.get(j).getHour_to();
					}
				}
			}
			barList.get(i).setHappyHoursString(happyHoursString);
		}
	}
}
