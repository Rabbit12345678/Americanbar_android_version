package com.spaculus.helpers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

//import android.util.Log;

public class ServiceHandler {

	static String response = null;
	public final static int GET = 1;
	public final static int POST = 2;

	public ServiceHandler() {

	}

	/*
	 * Making service call
	 * @url - url to make request
	 * @method - http request method
	 * */
	//  For the GET type of method
	public String makeServiceCall(String url, int method) {
		return this.makeServiceCall(url, method, null);
	}

	/*
	 * Making service call
	 * @url - url to make request
	 * @method - http request method
	 * @params - http request params
	 * */
	//  For the POST type of method
	public String makeServiceCall(String url, int method, List<NameValuePair> params) {
		try {
			//Log.i("URL Value is:",url);
			// http client
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;
			
			// Checking http request method type
			if (method == POST) {	
				HttpPost httpPost = new HttpPost(url);
				//  Adding post params
				if (params != null) {
					httpPost.setEntity(new UrlEncodedFormEntity(params));
				}
				httpResponse = httpClient.execute(httpPost);
				//Log.i("httpResponse",httpResponse.toString());
			} 
			
			else if (method == GET) {
				// appending params to url
				if (params != null) {
					String paramString = URLEncodedUtils.format(params, "utf-8");
					url += "?" + paramString;
				}
				HttpGet httpGet = new HttpGet(url);
				httpResponse = httpClient.execute(httpGet);
			}
			
			
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			
			if(responseCode==200){
				httpEntity = httpResponse.getEntity();
			      if(httpEntity != null) {
			    	  response = EntityUtils.toString(httpEntity);
			          //Log.i("response",response);
	           }
			}
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			//Log.i("UnsupportedEncodingException:",Utils.isStringNull(e.getMessage()));
		} 
		catch (ClientProtocolException e) {
			e.printStackTrace();
			//Log.i("ClientProtocolException:",Utils.isStringNull(e.getMessage()));
		} 
		catch (IOException e) {
			e.printStackTrace();
			//Log.i("IOException:",Utils.isStringNull(e.getMessage()));
		}
		//Log.i("Here, the response value is:",response);
		return response;
	}
}
