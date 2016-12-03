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
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
//import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;

public class RegistrationIntentService extends IntentService {

	private static final String TAG = "RegIntentService";
	private String response = "";
	private HashMap<String, String> dataSet;

	public RegistrationIntentService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			InstanceID instanceID = InstanceID.getInstance(this);
			String token = instanceID.getToken(
					ConfigConstants.GOOGLE_SENDER_ID,
					GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
			if (token != null && token.length() > 0) {
				sendRegistrationToServer(token);
			}
		} catch (Exception e) {
			//Log.d(TAG, "Failed to complete token refresh", e);
			e.printStackTrace();
		}

	}

	private void sendRegistrationToServer(String token) {
		try {
			if (Utils.getInstance().isNetworkAvailable(
					RegistrationIntentService.this)) {
				new AsynTaskUpdateDeviceToken().execute(token);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class AsynTaskUpdateDeviceToken extends
			AsyncTask<String, Void, String> {
		private String getResponse = "";
		@Override
		protected void onPreExecute() {
			try {
				super.onPreExecute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				dataSet = new HashMap<String, String>();
				dataSet.put(
						"device_id",
						SessionManager.getInstance(
								RegistrationIntentService.this).getData(
								SessionManager.KEY_DEVICE_ID));
				dataSet.put("gcm_regid", params[0]);
				getResponse = performPostCall(ConfigConstants.Urls.register_android_device, dataSet);
			} catch (Exception e) {
				e.printStackTrace();
				getResponse = "";
			}
			return getResponse;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
	}

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

}
