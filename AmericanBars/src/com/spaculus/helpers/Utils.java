package com.spaculus.helpers;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.WordUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
//import android.util.Log;

/**
 * @author Manali Sheth
 * @purpose This Utility class is used to declare all the methods which are used
 *          generally through out the application.
 */
public class Utils {

	/**
	 * Static instance of this class
	 */
	private static Utils instanceUtility = null;

	/**
	 * @purpose To get the static instance of this class
	 * @return instance of this class
	 */
	public static Utils getInstance() {
		if (instanceUtility == null) {
			instanceUtility = new Utils();
			//Log.i("New Instance", "New Instance");
		}
		return instanceUtility;
	}

	/**
	 * @purpose To clear the instance of this class
	 */
	public void clearInstance() {
		instanceUtility = null;
	}

	/**
	 * @purpose To check whether Internet Connection is available or not.
	 * @param context
	 * @return boolean
	 */
	public boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		if ((activeNetworkInfo != null) && (activeNetworkInfo.isConnected())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @purpose To check whether string is null or not
	 * @param checkString
	 * @return String value
	 */
	public static String isStringNull(String checkString) {
		String responseString = (checkString == null) ? "e.getMessage() is empty."
				: checkString;
		return responseString;
	}

	/**
	 * @purpose To check whether json response contains particular tag or not
	 * @param jsonObj
	 * @param tagName
	 * @return String value which is either tag value or empty string
	 */
	public String isTagExists(JSONObject jsonObj, String tagName) {
		String value = "";
		try {
			if (jsonObj.has(tagName)) {
				value = jsonObj.getString(tagName);
				if (value.equals("null")) {
					value = "";
				}
			} else {
				value = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	//  Method to set the Date onto the button
	public String updateDate(Calendar c){

		//  Set the required date format
		String myDateFormat = "MM/dd/yyyy"; 
        SimpleDateFormat sdf = new SimpleDateFormat(myDateFormat, Locale.US);

        String ans = sdf.format(c.getTime());
        //Log.i("Selected Date is:",ans);
        
        return ans;
	}
	
	//  To check whether user has selected future date or not as if it is then we can't allow it as the birth date.
	public String isFutureDate(Calendar myCalendar){
		String checkDateString = "";
		//  To compare the selected date with the today date
        Calendar todayDateObj = Calendar.getInstance(); 
        todayDateObj.set(Calendar.HOUR_OF_DAY, 0);
        todayDateObj.set(Calendar.MINUTE, 0);
        todayDateObj.set(Calendar.SECOND, 0);
        todayDateObj.set(Calendar.MILLISECOND, 0);
      
        //Log.i("Before-myCalendar",""+myCalendar.getTime());
        //Log.i("Before-todayDate",""+todayDateObj.getTime());
        
        if(myCalendar.equals(todayDateObj)){
        	//Log.i("Equal","Equal");
            checkDateString = ConfigConstants.Constants.CONSTANT_EQUAL;
		} 
        else if(myCalendar.before(todayDateObj)){	
        	//Log.i("Before","Before");
            checkDateString = ConfigConstants.Constants.CONSTANT_BEFORE;
        }
        else{
        	//Log.i("After","After");
        	checkDateString = ConfigConstants.Constants.CONSTANT_AFTER;
        }
		return checkDateString;
	}
	
	//  This method is used to set the address.
	public String setAddress(String address, String city, String state, String zipcode) {
		String wholeAddress = "";
		if(!address.isEmpty()) {
			wholeAddress += address;
		}
		if(!city.isEmpty()) {
			if(!wholeAddress.isEmpty()) {
				wholeAddress += "\n"+city;
			}
			else {
				wholeAddress += city;
			}
		}
		if(!state.isEmpty()) {
			if(!wholeAddress.isEmpty()) {
				wholeAddress += ", "+state;
			}
			else {
				wholeAddress += state;
			}
		}
		if(!zipcode.isEmpty()) {
			if(!wholeAddress.isEmpty()) {
				//wholeAddress += ", "+zipcode;
				wholeAddress += " "+zipcode;
			}
			else {
				wholeAddress += zipcode;
			}
		}
		return wholeAddress;
	}
	
	public String setAddressForMap(String address, String city, String state, String zipcode) {
		String wholeAddress = "";
		if(!address.isEmpty()) {
			wholeAddress += address;
		}
		if(!city.isEmpty()) {
			if(!wholeAddress.isEmpty()) {
				wholeAddress += ", "+city;
			}
			else {
				wholeAddress += city;
			}
		}
		if(!state.isEmpty()) {
			if(!wholeAddress.isEmpty()) {
				wholeAddress += ", "+state;
			}
			else {
				wholeAddress += state;
			}
		}
		if(!zipcode.isEmpty()) {
			if(!wholeAddress.isEmpty()) {
				wholeAddress += " "+zipcode;
			}
			else {
				wholeAddress += zipcode;
			}
		}
		return wholeAddress;
	}
	
	//  A method to convert time from 24-hours format to 12-hours format
	@SuppressLint({ "SimpleDateFormat", "DefaultLocale" }) 
	public String convertTime(String time) {
		String convertedTime = "";
		try {
			SimpleDateFormat dateFormatInput = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
			Date d = dateFormatInput.parse(time);
			SimpleDateFormat dateFormatOutput = new SimpleDateFormat("hh:mm a");
			convertedTime = dateFormatOutput.format(d).toLowerCase();
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertedTime;
	}
	
	//  A method to convert date from yyyy-MM-dd format to MM-dd-yyyy format
	@SuppressLint({ "SimpleDateFormat", "DefaultLocale" }) 
	public String convertDate(String date) {
		String convertedDate = "";
		try {
			SimpleDateFormat dateFormatInput = new SimpleDateFormat("yyyy-MM-dd"); 
			Date d = dateFormatInput.parse(date);
			SimpleDateFormat dateFormatOutput = new SimpleDateFormat("MM-dd-yyyy");
			convertedDate = dateFormatOutput.format(d).toLowerCase();
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertedDate;
	}
	
	//  A method to convert date-time from yyyy-MM-dd HH:mm:ss format to MM-dd-yyyy hh:mm:ss format
	@SuppressLint({ "SimpleDateFormat", "DefaultLocale" }) 
	public String convertDateTime(String date) {
		String convertDateTime = "";
		try {
			SimpleDateFormat dateFormatInput = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			Date d = dateFormatInput.parse(date);
			SimpleDateFormat dateFormatOutput = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
			convertDateTime = dateFormatOutput.format(d).toLowerCase();
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertDateTime;
	}
	
	/* A method is used to show the HTML Text. */
	public Spanned setHTMLText(String text) {
		return Html.fromHtml(text);
	}
	
	/* The following method is used to capitalize a first letter of each word. */
	public String setCapitalLetter(String text) {
		return WordUtils.capitalize(text);
	}
	
	/* A method is used to know whether phone or tablet. */
	public boolean isTablet(Context context) {
	    return (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	  }
	
	/* A method is used to get the device's Width and Height. */
	public int getDeviceWidth(Activity activity) {
		Display mDisplay = activity.getWindowManager().getDefaultDisplay();
		final int width  = mDisplay.getWidth();
		return width;
	}
	
	public int getDeviceHeight(Activity activity) {
		Display mDisplay = activity.getWindowManager().getDefaultDisplay();
		final int height = mDisplay.getHeight();
		return height;
	}
	
	/* A method is used to set the image which is selected from the device. */
	public void setImageDevice(Context context, String mediaPath, ImageView imageView) {
		Bitmap d = new BitmapDrawable(context.getResources(), mediaPath).getBitmap();
		int nh = (int) (d.getHeight() * (512.0 / d.getWidth()));
		Bitmap scaled = Bitmap.createScaledBitmap(d, 512, nh, true);
		imageView.setImageBitmap(scaled);
		/*Bitmap bmThumbnail = decodeSampledBitmapFromPath(mediaPath, 100, 100);
		imageView.setImageBitmap(bmThumbnail);*/
	}
	
	/*private Bitmap decodeSampledBitmapFromPath(String path, int reqWidth,
			int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap bmp = BitmapFactory.decodeFile(path, options);
		return bmp;
	}
	
	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}*/
	
	/**** Method for Setting the Height of the ListView dynamically.
	 **** Hack to fix the issue of not showing all the items of the ListView
	 **** when placed inside a ScrollView  ****/
	/* This method is used to set the fixed height of the ListView. */
	public void setListViewHeightBasedOnChildren(ListView listView) {
	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null)
	        return;

	    int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
	    int totalHeight = 0;
	    View view = null;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	        view = listAdapter.getView(i, view, listView);
	        if (i == 0)
	            view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

	        view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
	        totalHeight += view.getMeasuredHeight();
	    }
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
	    listView.requestLayout();
	}
	
	/* To know whether size is more than 150 or not */
	public boolean isMaxSizeExceed(double totalSizeInBytes) {
		boolean isMaxSizeExceed = false;
		double totalSizeInMB = convertBytes(totalSizeInBytes);
		//Log.i("totalSizeInMB", "" + totalSizeInMB);
		
		if (totalSizeInMB > 16) {
			isMaxSizeExceed = true;
		} else {
			isMaxSizeExceed = false;
		}
		return isMaxSizeExceed;
	}
	
	/* A method to convert total bytes into the MB */
	private double convertBytes(double totalSizeBytes) {
		double totalMBSize = 0;
		double MEGABYTE = 1024L * 1024L;
		totalMBSize = Math.ceil(totalSizeBytes / MEGABYTE);
		/*Log.i("totalSizeBytes", "" + totalSizeBytes);
		Log.i("totalMBSize", "" + totalMBSize);*/
		return totalMBSize;
	}
	
	/* A method is used to get the Youtube video id from the url. */
	public String getYoutubeVideoId(String youtubeUrl) {
		String video_id="";
		if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http")) {
			String expression = "^.*((youtu.be"+ "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
			CharSequence input = youtubeUrl;
			Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(input);
			if (matcher.matches()) {
				String groupIndex1 = matcher.group(7);
				if(groupIndex1!=null && groupIndex1.length()==11)
					video_id = groupIndex1;
			}
		}
		return video_id;
	 }
	
	@SuppressWarnings("unused")
	private static Toast longToast, shortToast1;
	
	/*public static void toastLong(Activity activity, String msg) {
		if (longToast != null) {
			longToast.cancel();
		}
		longToast = Toast.makeText(activity, msg, Toast.LENGTH_LONG);
		// longToast.setGravity(Gravity.BOTTOM, 0, 0);
		longToast.show();
	}*/
	
	public static void toastLong(Activity activity, String msg) {
		/*if (longToast != null) {
			longToast.cancel();
		}
		longToast = Toast.makeText(activity, msg, Toast.LENGTH_LONG);
		longToast.show();

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				longToast.cancel();
			}
		}, 40000);*/
		showAlert(activity, msg);
	}
	
	/*A method is used to show an alert for a selected player.*/
	public static void showAlert(Activity activity, String message) {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
		builder1.setMessage(message);
		builder1.setCancelable(true);
		builder1.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		AlertDialog objAlertDialog = builder1.create();
		objAlertDialog.show();
	}
	
	/* A method is used to show the toast. */
	public static void showToastLong(Context c, String msg) {
		if (longToast != null) {
			longToast.cancel();
		}
		longToast = Toast.makeText(c, msg, Toast.LENGTH_LONG);
		//longToast.setGravity(Gravity.CENTER, 0, 0);
		longToast.show();
	}
	
	/**
	 * @purpose To display a toast when Internet is not available
	 * @param activity
	 */
	public void showToastNoInternetAvailable(Activity activity) {
		/*Toast.makeText(activity, "No Internet Available", Toast.LENGTH_SHORT)
				.show();*/
		showToastLong(activity, "No Internet Available");
	}
	
	/* To display a toast. */
	public void displayToast(Activity activity, String message) {
		Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
	}
	
/*
	public static void toastShort(Context c, String msg) {
		if (shortToast != null) {
			shortToast.cancel();
		}
		shortToast = Toast.makeText(c, msg, Toast.LENGTH_SHORT);
		shortToast.setGravity(Gravity.CENTER, 0, 0);
		shortToast.show();
	}*/
	
	/**
	 * @purpose To hide the soft keyboard on touch of layout
	 * @param context An object of a context
	 * @param etArray An array of the EditText
	 */
	public void hideSoftKeyboard(Context context, EditText[] etArray){
		try {
			//Log.i("Method hideSoftKeyboard","Method hideSoftKeyboard");
			InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
			for(int i=0;i <etArray.length; i++) {
				imm.hideSoftInputFromWindow(etArray[i].getWindowToken(), 0);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* A method is used to get the formatted date. */
	public String getFormattedDate(String existingDateString, String existingDateFormatString, String newDateFormatString) {
		SimpleDateFormat existingDateFormat = null;
		SimpleDateFormat newDateFormat = null;
		Date myDate = null;
		existingDateFormat = new SimpleDateFormat(existingDateFormatString,Locale.getDefault());
		newDateFormat = new SimpleDateFormat(newDateFormatString, Locale.getDefault());
		try {
			myDate = existingDateFormat.parse(existingDateString);
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
		String newDateString = newDateFormat.format(myDate);
		return newDateString;
	}
	
	/* A method is used to convert a string into the Base64. */
	public String convertToBase64(String string) {
		String base64String = "";
		try {
			/* Now, convert a string into the Base 64 */
			byte[] data;
			data = string.getBytes("UTF-8");
			base64String = Base64.encodeToString(data, Base64.DEFAULT);
			Log.i("Base 64 ", base64String);
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return base64String;
	}
	
	/* A method is used to convert float string into the integer string. */
	public String convertFloatToInteger(String floatSring) {
		/*float f1 = Float.parseFloat(floatSring);
		int i = (int) f1;
		return String.valueOf(i);*/
		double pointValue = Double.parseDouble(floatSring);
		return ""+(long) (pointValue);
	}
	
	/* A method is used to get the current day. */
	public String getCurrentDay() {
		SimpleDateFormat dayFormat = new SimpleDateFormat(ConfigConstants.DateFormats.EEEE, Locale.getDefault());
		Calendar calendar = Calendar.getInstance();
		return dayFormat.format(calendar.getTime());
	}
}
