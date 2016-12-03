package com.spaculus.americanbars;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.login.LoginManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.spaculus.adapters.CustomRightMenuAdapter;
import com.spaculus.adapters.CustomSpinnerAdapter;
import com.spaculus.americanbars.dialogfragments.FragmentAlertDialogLogOut;
import com.spaculus.beans.Article;
import com.spaculus.beans.Bar;
import com.spaculus.beans.BarEvent;
import com.spaculus.beans.LeftNavigationDrawerItem;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;
import com.spaculus.helpers.Validation;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends Activity {

	//  For the Action Bar
	//  Action Bar Back icon
	public ImageView ivBackActionBar;
	//  Action Bar Title
	public TextView tvTitleActionBar;
	//  Action Bar Logo
	public ImageView ivLogoActionBar;
	//  Action Bar Menu icon
	public ImageView ivMenuActionBar;
	//  Search Bar icon
	public ImageView ivSearchBarActionBar;
	
	//  For the Share Options i.e. for the sharing functionality
	protected ArrayList<String> searchOptionsList = null;
	
	/* For the Menu functionality */
	 ListPopupWindow popupWindow = null;
	 
	/* An ArrayList to store the menu item values. */
	 ArrayList<LeftNavigationDrawerItem> listRightMenu = null;

	/* An Adapter for the Right Menu */
	CustomRightMenuAdapter adapter = null;
	int temp = 0;
	private int tempBar = 0;
	private int tempBarEvent = 0;
	
	//  AsyncTask LogOut message
	private String asyncTaskLogOutMessage = "Logging out....";
	
	/**
     * @purpose To create the action bar
     * @param title Action Bar Title
     * @param layout Action Bar Layout
     * @param activity Activity for that action bar
     * @param isRedirectFromProfileActivity
     */
    public void createActionBar(String title, int layout, final Activity activity, boolean isCaptialTitle){
		try {
			ActionBar mActionBar = getActionBar(); 
			
			mActionBar.setDisplayShowHomeEnabled(false);
			mActionBar.setDisplayShowTitleEnabled(false);
			
			LayoutInflater mInflater = LayoutInflater.from(activity);
			View mCustomView = mInflater.inflate(layout, null);
			
			// Now, mapped all the action bar views
			ivBackActionBar = (ImageView) mCustomView.findViewById(R.id.ivBackActionBar);
			tvTitleActionBar = (TextView) mCustomView.findViewById(R.id.tvTitleActionBar);
			ivLogoActionBar = (ImageView) mCustomView.findViewById(R.id.ivLogoActionBar);
			ivMenuActionBar = (ImageView) mCustomView.findViewById(R.id.ivMenuActionBar);
			ivSearchBarActionBar = (ImageView) mCustomView.findViewById(R.id.ivSearchBarActionBar);
			
			//  Set the action bar title
			if(isCaptialTitle) {
				tvTitleActionBar.setText(title.toUpperCase(Locale.getDefault()));	
			}
			else {
				tvTitleActionBar.setText(title);
			}
			
			mActionBar.setCustomView(mCustomView);
			mActionBar.setDisplayShowCustomEnabled(true);
			
			/*//  Now, here create the pop-up menu
			createPopUpMenu(title);*/
			
			/* Back Arrow icon click event */
			ivBackActionBar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						//  Simply close the current screen
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

	/**
	 * @purpose To change the action bar views from the activity or fragment
	 * @param isBackArrowVisible Flag for Back arrow visibility
	 * @param isTitleVisible Flag for Title visibility
	 * @param isLogoVisible Flag for Logo visibility
	 * @param isMenuIconVisible Flag for Menu icon visibility
	 * @param isSearchIconVisible Flag for Search icon visibility
	 */
	public void setActionBarFromChild(boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible, boolean isSearchIconVisible) {
		try {
			if(isBackArrowVisible) {
				ivBackActionBar.setVisibility(View.VISIBLE);
			}
			else {
				ivBackActionBar.setVisibility(View.GONE);
			}
			
			if(isTitleVisible) {
				tvTitleActionBar.setVisibility(View.VISIBLE);
			}
			else {
				tvTitleActionBar.setVisibility(View.GONE);
			}
			
			if(isLogoVisible) {
				ivLogoActionBar.setVisibility(View.VISIBLE);
			}
			else {
				ivLogoActionBar.setVisibility(View.INVISIBLE);
			}

			if(isMenuIconVisible) {
				ivMenuActionBar.setVisibility(View.VISIBLE);
			}
			else {
				ivMenuActionBar.setVisibility(View.INVISIBLE);
			}
			
			if(isSearchIconVisible) {
				ivSearchBarActionBar.setVisibility(View.VISIBLE);
			}
			else {
				ivSearchBarActionBar.setVisibility(View.INVISIBLE);
			}
			
			/* Menu icon click event */
			ivMenuActionBar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showPopUpMenu();
				}
			});
			
			ivSearchBarActionBar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//redirectBarSearchActivity(BaseActivity.this);
					redirectHomeActivity(BaseActivity.this);
				}
			});
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    /**
     * @purpose To set the action bar title
     * @param title Value of the action bar title
     */
    public void setActionBarTitle(String title) {
    	try {
			tvTitleActionBar.setText(title.toUpperCase(Locale.getDefault()));
		} 
    	catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * @purpose To update the action bar title
     * @param title Value of the action bar title
     */
    public void updateActionBarTitle(String title){
    	try {
			tvTitleActionBar.setText(title);
		} 
    	catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	/**
	 * @purpose To set the image through out the application
	 * @param pictureURL URL of a picture
	 * @param pictureName Name of a picture
	 * @param imageView ImageView to display an image
	 * @param resouceImage Place holder image
	 */
	public void setLogo(String pictureURL, String pictureName, ImageView imageView, int resouceImage){
		try{
			if(pictureName.length() != 0){
				Picasso.with(BaseActivity.this).load(pictureURL+pictureName).placeholder(resouceImage).into(imageView);
			}
			else{
				imageView.setImageResource(resouceImage);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setMapLogo(String pictureURL, ImageView imageView, int resouceImage){
		try{
			if(pictureURL.length() != 0){
				Picasso.with(BaseActivity.this).load(pictureURL).placeholder(resouceImage).into(imageView);
			}
			else{
				imageView.setImageResource(resouceImage);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	//  To show Tooltip
	/*public void showToolTip(View view, String tooltipText) {
		
		First Try
		try {
			//  Create the quick action view, passing the view anchor
			QuickActionView qa = QuickActionView.Builder( view );
			
			//  Set the adapter
			qa.setAdapter( new CustomTooltipAdapter(BaseActivity.this, tooltipText) );

			//  Show the View
			qa.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Second Try
		ChromeHelpPopup chromeHelpPopup = new ChromeHelpPopup(BaseActivity.this,tooltipText);
		chromeHelpPopup.show(view);
		
		Third Try
	}*/
	
	/**
	 * @purpose To get the review count value
	 * @param total_rating Value of Total Ratings
	 * @param total_comments Value of Total Comments
	 * @param imageViewStarArray An array of ImageView to display stars
	 */
	public void setStarReviews(String total_rating, String total_comments, ImageView[] imageViewStarArray) {
		try {
			/*
			 * Now, first of all check whether total_rating or total_comments values either 0 or null.
			 */
			if(!total_rating.isEmpty()) {
				if(!total_rating.equals("null")) {
					if(!total_rating.equals(null)) {
						if(!total_comments.isEmpty()) {
							if(!total_comments.equals("null")) {
								if(!total_comments.equals(null)) {
									int reviewCount = 0;
									//  Now, for the Reviews i.e. (total_rating/total_comments)
									if(total_rating.equals("null")) {
										reviewCount = 0;
									}
									else {
										reviewCount = Integer.parseInt(total_rating) / Integer.parseInt(total_comments);	
									}
									//  Now, set the stars
									setStars(reviewCount, imageViewStarArray);
								}
							}
						}
					}
				}
			}
		} 
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @purpose To set the review stars
	 * @param reviewCount Value of Review count
	 * @param imageViewStarArray An array of ImageView to display stars
	 */
	public void setStars(int reviewCount, ImageView[] imageViewStarArray) {
		try {
			// Now, set the stars
			for (int i = 0; i < 5; i++) {
				if (i + 1 <= reviewCount) {
					imageViewStarArray[i]
							.setImageResource(R.drawable.star_yellow);
				} else {
					imageViewStarArray[i]
							.setImageResource(R.drawable.star_white);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param googleMap A google map
	 * @param latitude Value of latitude
	 * @param longitude Value of longitude
	 * @param barName Value of bar name
	 * @param address Value of address
	 */
	protected void showMap(GoogleMap googleMap, double latitude,
			double longitude, String barName, String address) {
		try {
			LatLng objLatLong = new LatLng(latitude, longitude);

			googleMap.setMyLocationEnabled(true);
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(objLatLong,
					12));
			
			googleMap.addMarker(new MarkerOptions().title(barName)
					.snippet(address).position(objLatLong));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void showMapCurrent(GoogleMap googleMap, double latitude,
			double longitude, String barName, String address) {
		try {
			LatLng objLatLong = new LatLng(latitude, longitude);
			googleMap.setMyLocationEnabled(true);
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(objLatLong,12));
			/*googleMap.addMarker(new MarkerOptions().icon(
					BitmapDescriptorFactory.fromResource(R.drawable.mediapicker_checkmarker))
					.position(objLatLong));*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* A method is used for the Bar markers. */
	protected void addBarMarkers1(GoogleMap googleMap, final ArrayList<Bar> barList, final Activity cnt,int res, final String isRedirectedFrom) {
		try {
			googleMap.setMyLocationEnabled(true);
			for (int i = 0; i < barList.size(); i++) {
				tempBar = i;
				LatLng objLatLong = new LatLng(Double.parseDouble(barList.get(i).getLat()), Double.parseDouble(barList.get(i).getLang()));
				/*googleMap.addMarker(
						new MarkerOptions().title(barList.get(i).getTitle() + "~" + logo + "~"+ barList.get(i).getId()).snippet(barList.get(i).getAddress()).icon(BitmapDescriptorFactory.fromResource(res)).position(objLatLong));*/
				
				/* For the res - Map icon images */
				if(barList.get(i).getType().equals(ConfigConstants.Constants.BAR_TYPE_FULL_MUG)) {
					//int res1 = R.drawable.map_star;
					int res1 = R.drawable.map_star_01;
					/* Set value of i as title */
					googleMap.addMarker(new MarkerOptions().title(String.valueOf(i)).icon(BitmapDescriptorFactory.fromResource(res1)).position(objLatLong));
				}
				else {
					/* Set value of i as title */
					googleMap.addMarker(new MarkerOptions().title(String.valueOf(i)).icon(BitmapDescriptorFactory.fromResource(res)).position(objLatLong));
				}
				
				googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {
					@Override
					public View getInfoWindow(Marker arg0) {
						return null;
					}

					@SuppressLint("InflateParams")
					@Override
					public View getInfoContents(Marker arg0) {
						View v = getLayoutInflater().inflate(R.layout.map_info_window, null);
						ImageView ivBarLogo = (ImageView) v.findViewById(R.id.ivBarLogo);
						ImageView ivNextArrow = (ImageView) v.findViewById(R.id.ivNextArrow);
						TextView tvBarName = (TextView) v.findViewById(R.id.tvBarName);
						TextView tvBarAddress = (TextView) v.findViewById(R.id.tvBarAddress);
						
						/* Get the position here */
						int pos = Integer.parseInt(arg0.getTitle());
						
						/* Now, set the data here */
						tvBarName.setText(barList.get(pos).getTitle());
						if(isRedirectedFrom.equals(ConfigConstants.Constants.CONSTANT_FIRST)) {
							tvBarAddress.setText(barList.get(pos).getAddress());
						}
						else {
							tvBarAddress.setText(barList.get(pos).getHappyHoursString());
						}
						setMapLogo(ConfigConstants.ImageUrls.barlogo_200+barList.get(pos).getBar_logo(), ivBarLogo, R.drawable.no_image_bar);
						
						ivNextArrow.setId(tempBar);
						return v;
					}
				});
				googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
					@Override
					public void onInfoWindowClick(Marker arg0) {
						arg0.hideInfoWindow();
						int pos = Integer.parseInt(arg0.getTitle());//Integer.parseInt(arg0.getId().substring(1));
						redirectFullHalfMugBarDetailsActivity(cnt, barList.get(pos).getId(), barList.get(pos).getType());
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* A method is used for the Admin Event markers. */
	protected void addAdminEventMarkers1(GoogleMap googleMap, final ArrayList<BarEvent> adminEventsList, final Activity cnt,int res, final String isRedirectedFrom) {
		try {
			googleMap.setMyLocationEnabled(true);
			for (int i = 0; i < adminEventsList.size(); i++) {
				tempBarEvent = i;
				LatLng objLatLong = new LatLng(Double.parseDouble(adminEventsList.get(i).getEvent_lat()), Double.parseDouble(adminEventsList.get(i).getEvent_lng()));
				/*googleMap.addMarker(
						new MarkerOptions().title(barList.get(i).getTitle() + "~" + logo + "~"+ barList.get(i).getId()).snippet(barList.get(i).getAddress()).icon(BitmapDescriptorFactory.fromResource(res)).position(objLatLong));*/
				
				/* Set value of i as title */
				googleMap.addMarker(new MarkerOptions().title(String.valueOf(i)).icon(BitmapDescriptorFactory.fromResource(res)).position(objLatLong));
				
				googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {
					@Override
					public View getInfoWindow(Marker arg0) {
						return null;
					}

					@SuppressLint("InflateParams")
					@Override
					public View getInfoContents(Marker arg0) {
						View v = getLayoutInflater().inflate(R.layout.map_info_window, null);
						ImageView ivBarLogo = (ImageView) v.findViewById(R.id.ivBarLogo);
						ImageView ivNextArrow = (ImageView) v.findViewById(R.id.ivNextArrow);
						TextView tvBarName = (TextView) v.findViewById(R.id.tvBarName);
						TextView tvBarAddress = (TextView) v.findViewById(R.id.tvBarAddress);
						
						/* Get the position here */
						int pos = Integer.parseInt(arg0.getTitle());
						
						/* Now, set the data here */
						tvBarName.setText(adminEventsList.get(pos).getTitle());
						tvBarAddress.setText(adminEventsList.get(pos).getAddress());
						ivBarLogo.setImageResource(R.drawable.no_image_bar_event);
						
						ivNextArrow.setId(tempBarEvent);
						return v;
					}
				});
				googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
					@Override
					public void onInfoWindowClick(Marker arg0) {
						arg0.hideInfoWindow();
						int pos = Integer.parseInt(arg0.getTitle());//Integer.parseInt(arg0.getId().substring(1));
						redirectBarEventDetailsActivity(BaseActivity.this, adminEventsList.get(pos).getId(), adminEventsList.get(pos).getTitle());
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int index = 0;
	/* A method is used for the Bar markers And Admin Events List. */
	protected void addMarkers(GoogleMap googleMap, final Activity activity, final String isRedirectedFrom, final List<Object> list, boolean isDirectAroundMe) {
		try {
			googleMap.setMyLocationEnabled(true);
			for (int i = 0; i < list.size(); i++) {
				index = i;
				if(list.get(index).getClass() == Bar.class) {
					LatLng objLatLong = new LatLng(Double.parseDouble(((Bar) list.get(index)).getLat()), Double.parseDouble(((Bar) list.get(index)).getLang()));
					/* For the res - Map icon images */
					int res = 0;
					if(isDirectAroundMe) {
						if(((Bar) list.get(index)).getType().equals(ConfigConstants.Constants.BAR_TYPE_FULL_MUG)) {
							res = R.drawable.map_star_01;
						}
						else {
							res = R.drawable.half_mug;
						}
					}
					else {
						res = R.drawable.map_martini_glass;
					}
					/* Set value of i as title */
					googleMap.addMarker(new MarkerOptions().title(String.valueOf(index)).icon(BitmapDescriptorFactory.fromResource(res)).position(objLatLong));
					/*if(((Bar) list.get(index)).getType().equals(ConfigConstants.Constants.BAR_TYPE_FULL_MUG)) {
						//int res1 = R.drawable.map_star;
						int res1 = R.drawable.map_star_01;
						 Set value of i as title 
						googleMap.addMarker(new MarkerOptions().title(String.valueOf(index)).icon(BitmapDescriptorFactory.fromResource(res1)).position(objLatLong));
					}
					else {
						 Set value of i as title 
						googleMap.addMarker(new MarkerOptions().title(String.valueOf(index)).icon(BitmapDescriptorFactory.fromResource(res)).position(objLatLong));
					}*/
				}
				else {
					LatLng objLatLong = new LatLng(Double.parseDouble(((BarEvent) list.get(index)).getEvent_lat()), Double.parseDouble(((BarEvent) list.get(index)).getEvent_lng()));
					/* Set value of i as title */
					googleMap.addMarker(new MarkerOptions().title(String.valueOf(index)).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_circle_01)).position(objLatLong));
				}
				
				googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {
					@Override
					public View getInfoWindow(Marker arg0) {
						return null;
					}

					@SuppressLint("InflateParams")
					@Override
					public View getInfoContents(Marker arg0) {
						View v = getLayoutInflater().inflate(R.layout.map_info_window, null);
						ImageView ivBarLogo = (ImageView) v.findViewById(R.id.ivBarLogo);
						ImageView ivNextArrow = (ImageView) v.findViewById(R.id.ivNextArrow);
						TextView tvBarName = (TextView) v.findViewById(R.id.tvBarName);
						TextView tvBarAddress = (TextView) v.findViewById(R.id.tvBarAddress);
						
						/* Get the position here */
						int pos = Integer.parseInt(arg0.getTitle());
						
						if(list.get(pos).getClass() == Bar.class) {
							/* Now, set the data here */
							tvBarName.setText(((Bar) list.get(pos)).getTitle());
							if(isRedirectedFrom.equals(ConfigConstants.Constants.CONSTANT_FIRST)) {
								/*tvBarAddress.setText(((Bar) list.get(pos)).getAddress());*/
								tvBarAddress.setText(Utils.getInstance().setAddressForMap(((Bar) list.get(pos)).getAddress(), ((Bar) list.get(pos)).getCity(), ((Bar) list.get(pos)).getState(), ((Bar) list.get(pos)).getZipcode()));
							}
							else {
								tvBarAddress.setText(((Bar) list.get(pos)).getHappyHoursString());
							}
							setMapLogo(ConfigConstants.ImageUrls.barlogo_200+((Bar) list.get(pos)).getBar_logo(), ivBarLogo, R.drawable.no_image_bar);
						}
						else {
							/* Now, set the data here */
							tvBarName.setText(((BarEvent) list.get(pos)).getTitle());
							/*tvBarAddress.setText(((BarEvent) list.get(pos)).getAddress());*/
							tvBarAddress.setText(Utils.getInstance().setAddressForMap(((BarEvent) list.get(pos)).getAddress(), ((BarEvent) list.get(pos)).getCity(), ((BarEvent) list.get(pos)).getState(), ((BarEvent) list.get(pos)).getZipcode()));
							ivBarLogo.setImageResource(R.drawable.no_image_bar_event);
						}
						ivNextArrow.setId(temp);
						return v;
					}
				});
				googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
					@Override
					public void onInfoWindowClick(Marker arg0) {
						arg0.hideInfoWindow();
						/* Get the position here */
						int pos = Integer.parseInt(arg0.getTitle());
						if(list.get(pos).getClass() == Bar.class) {
							redirectFullHalfMugBarDetailsActivity(activity, ((Bar) list.get(pos)).getId(), ((Bar) list.get(pos)).getType());
						}
						else {
							redirectBarEventDetailsActivity(activity, ((BarEvent) list.get(pos)).getId(), ((BarEvent) list.get(pos)).getTitle());
						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* A method is used to hide a view if having an empty value o.w. show it. */
	/**
	 * @purpose A method is used to show the TextView if the value is not empty or blank o.w. hide the TextView
	 * @param textView
	 * @param value
	 */
	public void showHideView(TextView textView, String value) {
		if(value.isEmpty() || value.equals(" ")) {
			textView.setVisibility(View.GONE);
		}
		else {
			textView.setVisibility(View.VISIBLE);
			textView.setText(value);
		}
	}
	
	/**
	 * function to load map If map is not created it will create it for you
	 * @param ivGallerySelectedImage 
	 * @param galleryList 
	 * */
	/*private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}*/
	
	/**
	 * @purpose To redirect to a social page
	 * @param linkValue Value of a link on which need to redirect a user
	 */
	protected void redirectPage(String linkValue) {
		try {
			if(!linkValue.isEmpty()) {
				try {
				    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkValue));
				    startActivity(intent);
				} 
				catch(Exception e) {
				    e.printStackTrace();
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @purpose To share text on social web site
	 * @param packageName Value of the package name of a social application
	 * @param shareText Value of text that need to share
	 * @param flag Value of social network application
	 */
	protected void shareTextLink(String packageName, String shareText, String flag) {
		/*try {
			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			sharingIntent.setPackage(packageName);
			sharingIntent.putExtra(Intent.EXTRA_TEXT, shareText);
			startActivity(sharingIntent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}*/
		try {
			//  Create the intent to share a post on a particular selected application
			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			sharingIntent.setPackage(packageName);
			sharingIntent.putExtra(Intent.EXTRA_TITLE, "");
			sharingIntent.putExtra(Intent.EXTRA_TEXT, shareText);
			
			//  Now, check whether particular application is installed on device or not.
			PackageManager packManager = getPackageManager();
			List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(sharingIntent,  PackageManager.MATCH_DEFAULT_ONLY);
			
			boolean resolved = false;
			for(ResolveInfo resolveInfo: resolvedInfoList){
			    if(resolveInfo.activityInfo.packageName.startsWith(packageName)){
			    	sharingIntent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name );
			        resolved = true;
			        break;
			    }
			}
			if(resolved){
				startActivity(sharingIntent);
			}
			else{
				 Toast.makeText(this, flag+" application is not found.", Toast.LENGTH_LONG).show();
				 /*The following code is used to redirect a user into the browser is application is not not installed onto the device.*/
			    /*Intent i = new Intent();
			    i.putExtra(Intent.EXTRA_TEXT, shareText);
			    i.setAction(Intent.ACTION_VIEW);
			    i.setData(Uri.parse("https://twitter.com/intent/tweet?text=message&via=profileName"));
			    startActivity(i);*/
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to share an image with a text
	
	/**
	 * @purpose An AsyncTask to share an image with a text on the social web site
	 */
	public class AsynTaskShareImageWithText extends AsyncTask<String, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(BaseActivity.this);
    	private Uri uri = null;
    	private String packageName = "", flag = "", imageURL = "", imageName = "", imageTitle = "";
    	
    	@Override
		protected void onPreExecute() {
			try {
				// TODO Auto-generated method stub
				super.onPreExecute();
				
				this.pd.setMessage(ConfigConstants.Messages.sharingMessage);
				pd.setCanceledOnTouchOutside(false); 
				pd.setCancelable(false);
				this.pd.show();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}	

		@Override
		protected Void doInBackground(String... params) {
	        try {
	        	packageName = params[0];
	        	flag = params[1];
	        	imageURL = params[2];
	        	imageName = params[3];
	        	imageTitle = params[4];
	        	
				/* First of all here generate the remote image without downloading. */
	        	InputStream is = (InputStream) new URL(imageURL+imageName).getContent();
	        	Drawable mDrawable = Drawable.createFromStream(is, "src name");
	        	Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();
	        	String path = Images.Media.insertImage(getContentResolver(), mBitmap, "Image Description", null);
	        	uri = Uri.parse(path);
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
				
				if(uri != null) {
					//  Create the intent to share a post on a particular selected application
					Intent sharingIntent = new Intent(Intent.ACTION_SEND);
					sharingIntent.setType("image/*");
					sharingIntent.setPackage(packageName);
					sharingIntent.putExtra(Intent.EXTRA_TEXT, imageTitle);
					sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
					
					//  Now, check whether particular application is installed on device or not.
					PackageManager packManager = getPackageManager();
					List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(sharingIntent,  PackageManager.MATCH_DEFAULT_ONLY);
					
					boolean resolved = false;
					for(ResolveInfo resolveInfo: resolvedInfoList){
					    if(resolveInfo.activityInfo.packageName.startsWith(packageName)){
					    	sharingIntent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name );
					        resolved = true;
					        break;
					    }
					}
					if(resolved){
						startActivity(sharingIntent);
					}
					else{
						 Toast.makeText(BaseActivity.this, flag+" application is not found.", Toast.LENGTH_LONG).show();
					}
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			if(this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}
    }
	
	/* The following method is used to show logo for the description. */
	
	/**
	 * @purpose To show a dialog for description
	 * @param title Value of a title of alert dialog
	 * @param content Value of a content of alert dialog
	 */
	protected void showMoreDialog(String title, String content) {
		try {
			//final Dialog alert = new Dialog(FullMugBarDetailsActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
			final Dialog alert = new Dialog(BaseActivity.this);
			alert.requestWindowFeature(Window.FEATURE_NO_TITLE); //before     
			alert.setContentView(R.layout.dialog);

			ImageView ivCloseDialog = (ImageView) alert.findViewById(R.id.ivCloseDialog);
			TextView tvFullDescriptionTitle = (TextView) alert.findViewById(R.id.tvFullDescriptionTitle);
			TextView tvFullDescription = (TextView) alert.findViewById(R.id.tvFullDescription);

			//  Now, set the text here.
			//  Set Title
			tvFullDescriptionTitle.setText(Utils.getInstance().setHTMLText(title));
			//  Set Content
			tvFullDescription.setText(Utils.getInstance().setHTMLText(content));
			
			//  Now, show the dialog
			alert.show();

			ivCloseDialog.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (alert.isShowing()) {
							alert.dismiss();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void showMoreWebDialog(String title, String content) {
		try {
			//final Dialog alert = new Dialog(FullMugBarDetailsActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
			final Dialog alert = new Dialog(BaseActivity.this);
			alert.requestWindowFeature(Window.FEATURE_NO_TITLE); //before     
			alert.setContentView(R.layout.dialog_webview);

			ImageView ivCloseDialog = (ImageView) alert.findViewById(R.id.ivCloseDialog);
			TextView tvFullDescriptionTitle = (TextView) alert.findViewById(R.id.tvFullDescriptionTitle);
			WebView webFullDescription = (WebView) alert.findViewById(R.id.webFullDescription);
			
			webFullDescription.setBackgroundColor(Color.TRANSPARENT);
			tvFullDescriptionTitle.setText(Utils.getInstance().setHTMLText(title));
			webFullDescription.loadData("<font color=\"white\">" + content + "</font>","text/html","utf-8");
			alert.show();
			webFullDescription
					.setOnLongClickListener(new OnLongClickListener() {
						@Override
						public boolean onLongClick(View v) {
							return true;
						}
					});
			webFullDescription.setLongClickable(false);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			Window window = alert.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(
					android.graphics.Color.TRANSPARENT));
			lp.copyFrom(window.getAttributes());
			// This makes the dialog take up the full width
			lp.width = WindowManager.LayoutParams.MATCH_PARENT;
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			window.setAttributes(lp);
			
			ivCloseDialog.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						// TODO Auto-generated method stub
						if (alert.isShowing()) {
							alert.dismiss();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* The following method is used to bind the spinner data. */
	
	/**
	 * @purpose To bind spinner data for social web site sharing
	 * @param spinnerShare
	 */
	protected void bindSpinnerData(Spinner spinnerShare) {
		try {
			searchOptionsList = new ArrayList<String>();
			searchOptionsList.add("");
			searchOptionsList.add(ConfigConstants.Constants.CONSTANT_FACEBOOK);
			searchOptionsList.add(ConfigConstants.Constants.CONSTANT_TWITTER);
			//searchOptionsList.add(ConfigConstants.getInstance().CONSTANT_LINKEDIN);
			searchOptionsList.add(ConfigConstants.Constants.CONSTANT_GOOGLE_PLUS);
			//searchOptionsList.add(ConfigConstants.getInstance().CONSTANT_DRIBBLE);
			searchOptionsList.add(ConfigConstants.Constants.CONSTANT_PINTEREST);
			
			/*//  Initialize and bind the Spinner adapter here
			adapterSearchOptions = new ArrayAdapter<String>(FullMugBarDetailsActivity.this, android.R.layout.simple_list_item_1, searchOptionsList);
			adapterSearchOptions.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
			spinnerShare.setAdapter(adapterSearchOptions);
			*/
			int hidingItemIndex = 0;
			CustomSpinnerAdapter dataAdapter = new CustomSpinnerAdapter(BaseActivity.this, android.R.layout.simple_list_item_single_choice, searchOptionsList, hidingItemIndex);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
			spinnerShare.setAdapter(dataAdapter);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* A method is used to set the Add To Favorite button text. */
	
	/**
	 * @purpose To set the Add To Favorite button text
	 * @param button
	 * @param text Value of a text
	 */
	protected void setTextButtonAddToFavorite(Button button, String text, String flag) {
		try {
			/*if(text.equals(ConfigConstants.Constants.CONSTANT_ONE)) {
				button.setText(getResources().getString(R.string.btn_remove_favorite));
			}
			else {
				button.setText(getResources().getString(R.string.btn_add_to_favorite));
			}*/
			String fav = "", not_fav = "";
			if(flag.equals(ConfigConstants.Constants.BAR)) {
				fav = getResources().getString(R.string.btn_remove_favorite);
				not_fav = getResources().getString(R.string.btn_add_my_bar);
			}
			else if(flag.equals(ConfigConstants.Constants.BEER)) {
				fav = getResources().getString(R.string.btn_remove_favorite);
				not_fav = getResources().getString(R.string.btn_add_my_beer);
			}
			else if(flag.equals(ConfigConstants.Constants.COCKTAIL)) {
				fav = getResources().getString(R.string.btn_remove_favorite);
				not_fav = getResources().getString(R.string.btn_add_my_cocktail);
			}
			else if(flag.equals(ConfigConstants.Constants.LIQUOR)) {
				fav = getResources().getString(R.string.btn_remove_favorite);
				not_fav = getResources().getString(R.string.btn_add_my_liquor);
			}
			
			if(text.equals(ConfigConstants.Constants.CONSTANT_ONE)) {
				button.setText(fav);
			}
			else {
				button.setText(not_fav);
			}
		} 
		catch (NotFoundException e) {
			e.printStackTrace();
		}
	} 
	
	/**
	 * @purpose To set the Like This button value
	 * @param button
	 * @param text Value of a text
	 * @param flag To know from which screen this method is called as need to set an appropriate value
	 */
	protected void setTextButtonLike(Button button, String text, String flag) {
		try {
			String like = "", dislike = "";
			if(flag.equals(ConfigConstants.Constants.BAR)) {
				like = getResources().getString(R.string.btn_already_liked);
				dislike = getResources().getString(R.string.btn_like_this_bar);
			}
			else if(flag.equals(ConfigConstants.Constants.BEER)) {
				like = getResources().getString(R.string.btn_dislike_this_beer);
				dislike = getResources().getString(R.string.btn_like_this_beer);
			}
			else if(flag.equals(ConfigConstants.Constants.COCKTAIL)) {
				like = getResources().getString(R.string.btn_dislike_this_cocktail);
				dislike = getResources().getString(R.string.btn_like_this_cocktail);
			}
			else if(flag.equals(ConfigConstants.Constants.LIQUOR)) {
				like = getResources().getString(R.string.btn_dislike_this_liquor);
				dislike = getResources().getString(R.string.btn_like_this_liquor);
			}
			
			if(text.equals(ConfigConstants.Constants.CONSTANT_ONE)) {
				button.setText(like);
			}
			else {
				button.setText(dislike);
			}
		} 
		catch (NotFoundException e) {
			e.printStackTrace();
		}
	} 
	
	/**
	 * @purpose A method is used to create the pop-up menu. Call this method when you want to show the ListPopupWindow i.e. Right Menu
	 * @param title Value of Menu item title
	 */
	@SuppressLint("DefaultLocale") 
	private void createPopUpMenu(String title) {
		try {
			popupWindow = new ListPopupWindow(BaseActivity.this);
			listRightMenu = new ArrayList<LeftNavigationDrawerItem>();
			adapter = new CustomRightMenuAdapter(BaseActivity.this, R.layout.right_menu_list_item, listRightMenu);
			
			//  Now, first of all check whether user is logged in or not
			if(SessionManager.getInstance(BaseActivity.this).isLoggedIn() ) {
				listRightMenu.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.USER_PROFILE, R.drawable.menu_sign_in));
				
				if(!title.equals(ConfigConstants.Constants.MY_DASHBOARD.toUpperCase())) {
					listRightMenu.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.MY_DASHBOARD, R.drawable.menu_my_dashboard));
				}
			}
			else {
				listRightMenu.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.SIGN_IN, R.drawable.menu_sign_in));
				listRightMenu.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.SIGN_UP, R.drawable.menu_sign_up));
			}
			
			if(!isBarViewSelected(title)) {
				listRightMenu.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.BAR_SEARCH, R.drawable.menu_my_fav_bars));
			}
			
			if(!title.equals(ConfigConstants.Constants.BEER_LISTING.toUpperCase())) {
				listRightMenu.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.BEER_DIRECTORY, R.drawable.menu_my_fav_beers));
			}
			
			if(!title.equals(ConfigConstants.Constants.COCKTAIL_LISTING.toUpperCase())) {
				listRightMenu.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.COCKTAIL_DIRECTORY, R.drawable.menu_my_fav_cocktails));
			}
			
			if(!title.equals(ConfigConstants.Constants.LIQUOR_LISTING.toUpperCase())) {
				listRightMenu.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.LIQUOR_DIRECTORY, R.drawable.menu_my_fav_liquors));
			}
			
			if(!title.equals(ConfigConstants.Constants.TAXI_LISTING.toUpperCase())) {
				listRightMenu.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.TAXI_DIRECTORY, R.drawable.menu_taxi));
			}
			
			if(!title.equals(ConfigConstants.Constants.PHOTO_GALLERY.toUpperCase())) {
				listRightMenu.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.PHOTO_GALLERY, R.drawable.menu_photo_gallery));
			}
			
			if(!title.equals(ConfigConstants.Constants.MY_ALBUMS.toUpperCase())) {
				listRightMenu.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.MY_ALBUMS, R.drawable.menu_my_albums));
			}
			
			if(!title.equals(ConfigConstants.Constants.ARTICLES.toUpperCase())) {
				listRightMenu.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.ARTICLES, R.drawable.menu_articles));
			}
			
			//if(!title.equals(ConfigConstants.Constants.BAR_TRIVIA_GAME.toUpperCase()) || !title.equals(ConfigConstants.Titles.TITLE_TRIVIA.toUpperCase())) {
			if(!title.equals(ConfigConstants.Constants.BAR_TRIVIA_GAME.toUpperCase())) {
				listRightMenu.add(new LeftNavigationDrawerItem(ConfigConstants.Constants.BAR_TRIVIA_GAME, R.drawable.menu_trivia_game));
			}
			
			popupWindow.setAnchorView(ivMenuActionBar);
			popupWindow.setAdapter(adapter);
			//popupWindow.setWidth(280); // note: don't use pixels, use a dimen resource
			popupWindow.setContentWidth(measureContentWidth(adapter));
			popupWindow.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					try {
						/* A method is used to redirect from Right Menu */
						redirectRightMenu(listRightMenu.get(position).getTitle());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* A method is used to show the pop-up menu. */
	private void showPopUpMenu() {
		popupWindow.show();
	}
	
	/* A method is used to dismiss the pop-up menu. */
	private void dismissPopUpMenu() {
		try {
			if(popupWindow.isShowing()) {
				popupWindow.dismiss();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* For the Orientation Changes */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		try {
			super.onConfigurationChanged(newConfig);
			/*if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
			    Log.i("orientation", "Orientation changed to: Landscape");
			else
			    Log.i("orientation", "Orientation changed to: Portrait");*/
			
			/* Dismiss the PopUp Menu */
			dismissPopUpMenu();
			/* To change the action bar image logo */
			//ivLogoActionBar.setImageResource(R.drawable.actionbar_logo);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* To get the Right Menu's maximum row width */	
	private int measureContentWidth(ListAdapter listAdapter) {
	    int maxWidth = 0;
		try {
			ViewGroup mMeasureParent = null;
			maxWidth = 0;
			View itemView = null;
			int itemType = 0;

			final ListAdapter adapter = listAdapter;
			final int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
			final int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
			final int count = adapter.getCount();
			for (int i = 0; i < count; i++) {
			    final int positionType = adapter.getItemViewType(i);
			    if (positionType != itemType) {
			        itemType = positionType;
			        itemView = null;
			    }

			    if (mMeasureParent == null) {
			        mMeasureParent = new RelativeLayout(BaseActivity.this);
			    }

			    itemView = adapter.getView(i, itemView, mMeasureParent);
			    itemView.measure(widthMeasureSpec, heightMeasureSpec);

			    final int itemWidth = itemView.getMeasuredWidth();

			    if (itemWidth > maxWidth) {
			        maxWidth = itemWidth;
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return maxWidth;
	}
	
	/* A method is used to redirect from Right Menu */
	private void redirectRightMenu(String redirectFrom) {
		try {
			//  Profile Picture view click event
			if(redirectFrom.equals(ConfigConstants.Constants.USER_PROFILE)) {
				/*
				 * Now, first of check whether user is on the My Profile screen or not.
				 */
				try {
					// Check the foreground package
					ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
					List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
					@SuppressWarnings("unused")
					ComponentName topActivity = runningTasks.get(0).topActivity;
					 if(!runningTasks.get(0).topActivity.getClassName().equals("com.spaculus.americanbars.MyProfileActivity")) {
						 redirectMyProfileActivity(BaseActivity.this);
					 }
				} 
				catch (SecurityException e) {
					e.printStackTrace();
				}
			}
			//  Sign In
			else if(redirectFrom.equals(ConfigConstants.Constants.SIGN_IN)) {
				redirectFragmentSignIn(BaseActivity.this);
			}
			//  Sign Up
			else if(redirectFrom.equals(ConfigConstants.Constants.SIGN_UP)) {
					redirectFragmentSignUp(BaseActivity.this);
			}
			//  My Dash board
			else if(redirectFrom.equals(ConfigConstants.Constants.MY_DASHBOARD)) {
					redirectFragmentMyDashboard(BaseActivity.this);
			}
			//  Bar Search
			else if(redirectFrom.equals(ConfigConstants.Constants.BAR_SEARCH)) {
				redirectBarSearchActivity(BaseActivity.this);
			}
			//  Beer Directory
			else if(redirectFrom.equals(ConfigConstants.Constants.BEER_DIRECTORY)) {
				redirectBeerSearchListActivity(BaseActivity.this, "");
			}
			//  Cocktail Directory
			else if(redirectFrom.equals(ConfigConstants.Constants.COCKTAIL_DIRECTORY)) {
				redirectCocktailSearchListActivity(BaseActivity.this, "");
			}
			//  Liquor Directory
			else if(redirectFrom.equals(ConfigConstants.Constants.LIQUOR_DIRECTORY)) {
				redirectLiquorSearchListActivity(BaseActivity.this, "");
			}
			//  Taxi Directory
			else if(redirectFrom.equals(ConfigConstants.Constants.TAXI_DIRECTORY)) {
				redirectTaxiSearchListActivity(BaseActivity.this);
			}
			//  Photo Gallery
			else if(redirectFrom.equals(ConfigConstants.Constants.PHOTO_GALLERY)) {
				redirectPhotoGalleryActivity(BaseActivity.this);
			}
			//  My Albums
			else if(redirectFrom.equals(ConfigConstants.Constants.MY_ALBUMS)) {
				redirectFragmentMyAlbums(BaseActivity.this);
			}
			//  Articles
			else if(redirectFrom.equals(ConfigConstants.Constants.ARTICLES)) {
				redirectFragmentArticles(BaseActivity.this);
			}
			//  Bar Trivia Game
			else if(redirectFrom.equals(ConfigConstants.Constants.BAR_TRIVIA_GAME)) {
				redirectFragmentBarTriviaGame(BaseActivity.this);
			}
			
			/* Dismiss the PopUp Menu */
			dismissPopUpMenu();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume() {
		try {
			super.onResume();
			//  Now, here create the pop-up menu
			createPopUpMenu(tvTitleActionBar.getText().toString().trim());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/********************  The following methods are used to do transaction.  ********************/
	/*
	 * Direct Method Calls - All the following methods are used to redirect the activities.
	 */
	public void redirectBarSearchActivity(Activity activity) {
		try {
			Intent intent = new Intent(activity, BarSearchOptionActivity.class);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectBarSearchListActivity(Activity activity, String title, String state, String city, String zipcode) {
		try {
			Intent intent = new Intent(activity, BarSearchListActivity.class);
			intent.putExtra("title", title);
			intent.putExtra("state", state);
			intent.putExtra("city", city);
			intent.putExtra("zipcode", zipcode);
			intent.putExtra(ConfigConstants.Keys.KEY_IS_REDIRECTED_FROM, ConfigConstants.Constants.CONSTANT_FIRST);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectBarSearchListByLocationActivity(Activity activity,
			String title, String address, String day) {
		try {
			Intent intent = new Intent(activity, BarSearchListActivity.class);
			intent.putExtra("title", title);
			intent.putExtra("address", address);
			intent.putExtra("day", day);
			//intent.putExtra("flag", );
			intent.putExtra(ConfigConstants.Keys.KEY_IS_REDIRECTED_FROM, ConfigConstants.Constants.CONSTANT_SECOND);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectBeerSearchListActivity(Activity activity, String bar_id) {
		try {
			Intent intent = new Intent(activity, BeerSearchListActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_BAR_ID, bar_id);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectCocktailSearchListActivity(Activity activity, String bar_id) {
		try {
			Intent intent = new Intent(activity, CocktailSearchListActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_BAR_ID, bar_id);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectLiquorSearchListActivity(Activity activity, String bar_id) {
		try {
			Intent intent = new Intent(activity, LiquorSearchListActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_BAR_ID, bar_id);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectTaxiSearchListActivity(Activity activity) {
		try {
			Intent intent = new Intent(activity, TaxiSearchListActivity.class);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectBarEventSearchListActivity(Activity activity, String bar_id) {
		try {
			Intent intent = new Intent(activity, BarEventSearchListActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_BAR_ID, bar_id);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectMyProfileActivity(Activity activity) {
		try {
			Intent intent = new Intent(activity, MyProfileActivity.class);
			startActivity(intent);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectAddAlbumActivity(Activity activity, String bar_gallery_id) {
		try {
			Intent intent = new Intent(activity, AddAlbumActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_BAR_GALLERY_ID, bar_gallery_id);
			startActivity(intent);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectZoomImageViewActivity(Activity activity, String url) {
		try {
			Intent intent = new Intent(activity, ZoomImageViewURL.class);
			intent.putExtra(ConfigConstants.Keys.KEY_URL, url);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void redirectVideoPlayActivity(String videoID) {
		try {
			Intent intent = new Intent(BaseActivity.this, YouTubeVideoPlayActivity.class);
			intent.putExtra(YouTubeVideoPlayActivity.KEY_VIDEO_ID, videoID);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 	
	}
	
	public void redirectPhotoGalleryActivity(Activity activity) {
		try {
			Intent intent = new Intent(activity, PhotoGalleryActivity.class);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectFullHalfMugBarDetailsActivity(Activity activity, String bar_id, String type) {
		try {
			Intent intent = null;
			if(type.equals(ConfigConstants.Constants.BAR_TYPE_FULL_MUG)) {
				intent = new Intent(activity, FullMugBarDetailsActivity.class);
			}
			else {
				intent = new Intent(activity, HalfMugBarDetailsActivity.class);
			}
			intent.putExtra(ConfigConstants.Keys.KEY_BAR_ID, bar_id);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectBarEventDetailsActivity(Activity activity, String event_id, String event_name) {
		try {
			Intent intent = new Intent(activity, EventDetailsActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_EVENT_ID, event_id);
			intent.putExtra(ConfigConstants.Keys.KEY_EVENT_NAME, event_name);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectBeerDetailsActivity(Activity activity, String beer_id) {
		try {
			Intent intent = new Intent(activity, BeerDetailsActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_BEER_ID, beer_id);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectCocktailDetailsActivity(Activity activity, String cocktail_id) {
		try {
			Intent intent = new Intent(activity, CocktailDetailsActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_COCKTAIL_ID, cocktail_id);
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void redirectLiquorDetailsActivity(Activity activity, String liquor_id) {
		try {
			Intent intent = new Intent(activity, LiquorDetailsActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_LIQUOR_ID, liquor_id);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectTaxiDetailsActivity(Activity activity, String taxi_id) {
		try {
			Intent intent = new Intent(activity, TaxiDetailsActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_TAXI_ID, taxi_id);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void redirectPhotoGalleryDetailsActivity(Activity activity, String bar_gallery_id, String gallery_name, String web_service_method_name) {
		try {
			Intent intent = new Intent(activity, PhotoGalleryDetailsActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_BAR_GALLERY_ID, bar_gallery_id);
			intent.putExtra(ConfigConstants.Keys.KEY_GALLERY_NAME, gallery_name);
			intent.putExtra(ConfigConstants.Keys.KEY_WEB_SERVICE_METHOD_NAME, web_service_method_name);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param activity
	 * @param latitude
	 * @param longitude
	 * @param barName
	 * @param barAddress
	 */
	public void redirectToGoogleMapApplicationShowLocation(Activity activity, double latitude, double longitude, String barName, String barAddress) {
		try {
			Uri mapURI = Uri.parse("geo:"+latitude+","+longitude+"?q="+barName+", "+barAddress);
			Intent intent = new Intent(Intent.ACTION_VIEW, mapURI);
			intent.setPackage("com.google.android.apps.maps");
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectGoogleMapApplicationDrawPath(String destinationAddress) {
		try {
			/*Uri mapURI = Uri.parse("http://maps.google.com/maps?saddr=22.3143809,73.1724788&daddr=22.302816,73.2052353");
			//Uri mapURI = Uri.parse("http://maps.google.com/maps?daddr=22.3025225,73.2070768");
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, mapURI);
			startActivity(intent);*/ 
			
			//Uri gmmIntentUri = Uri.parse("google.navigation:q=AKS Boutique, Bajwada Main Road, Mandvi, Vadodara, Gujarat");
			Uri gmmIntentUri = Uri.parse("google.navigation:q="+destinationAddress);
			Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
			mapIntent.setPackage("com.google.android.apps.maps");
			startActivity(mapIntent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @purpose To do a phone call
	 * @param phoneNumber
	 */
	public void redirectToPhoneApplication(String phoneNumber) {
		try {
			if(!phoneNumber.isEmpty()) {
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + phoneNumber));
				startActivity(intent);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void redirectLoginActivity(Activity activity, boolean isFromLogout) {
		try {
			Intent intent = new Intent(activity, LoginActivity.class);
			if(isFromLogout) {
				//  Clear the Application Stack here
		    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			}
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void redirectMainActivity(Activity activity) {
		Intent intent = new Intent(activity, MainActivity.class);
		startActivity(intent);
	}
	
	protected void redirectForgotPasswordActivity(Activity activity) {
		try {
			Intent intent = new Intent(activity, ForgotPasswordActivity.class);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void redirectBarSearchAroundMeActivity(Activity activity, String isRedirectFrom, String day) {
		try {
			Intent intent = new Intent(activity, BarSearchAroundMeActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_IS_REDIRECTED_FROM, isRedirectFrom);
			intent.putExtra(ConfigConstants.Keys.KEY_DAY, day);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void redirectSuggestNewBarActivity(Activity activity) {
		try {
			Intent intent = new Intent(activity, SuggestNewBarActivity.class);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectArticleDetailsActivity(Activity activity, Article objArticle) {
		try {
			Intent intent = new Intent(activity, ArticleDetailsActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_ARTICLE_OBJECT, objArticle);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectBarReviewsListActivity(Activity activity, String bar_id) {
		try {
			Intent intent = new Intent(activity, BarReviewsListActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_BAR_ID, bar_id);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectBeerCommentsListActivity(Activity activity, String beer_id) {
		try {
			Intent intent = new Intent(activity, BeerCommentsListActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_BEER_ID, beer_id);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectCocktailCommentsListActivity(Activity activity, String cocktail_id) {
		try {
			Intent intent = new Intent(activity, CocktailCommentsListActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_COCKTAIL_ID, cocktail_id);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectLiquorCommentsListActivity(Activity activity, String liquor_id) {
		try {
			Intent intent = new Intent(activity, LiquorCommentsListActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_LIQUOR_ID, liquor_id);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectBeerCommentRepliesActivity(Activity activity, String beer_id, String master_comment_id) {
		try {
			Intent intent = new Intent(activity, BeerCommentRepliesActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_BEER_ID, beer_id);
			intent.putExtra(ConfigConstants.Keys.KEY_MASTER_COMMENT_ID, master_comment_id);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectCocktailCommentRepliesActivity(Activity activity, String cocktail_id, String master_comment_id) {
		try {
			Intent intent = new Intent(activity, CocktailCommentRepliesActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_COCKTAIL_ID, cocktail_id);
			intent.putExtra(ConfigConstants.Keys.KEY_MASTER_COMMENT_ID, master_comment_id);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectLiquorCommentRepliesActivity(Activity activity, String liquor_id, String master_comment_id) {
		try {
			Intent intent = new Intent(activity, LiquorCommentRepliesActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_LIQUOR_ID, liquor_id);
			intent.putExtra(ConfigConstants.Keys.KEY_MASTER_COMMENT_ID, master_comment_id);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Direct Method Calls - All the following methods are used to redirect the activities.
	 */
	
	/* Fragment Called through Transaction Activity */
	public void redirectFragmentSignUp(Activity activity) {
		try {
			Intent intent = new Intent(activity, TransactionActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_CONSTANT_REDIRECT_FROM, ConfigConstants.Constants.SIGN_UP);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectFragmentSignIn(Activity activity) {
		try {
			Intent intent = new Intent(activity, TransactionActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_CONSTANT_REDIRECT_FROM, ConfigConstants.Constants.SIGN_IN);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectFragmentMyDashboard(Activity activity) {
		try {
			Intent intent = new Intent(activity, TransactionActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_CONSTANT_REDIRECT_FROM, ConfigConstants.Constants.MY_DASHBOARD);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectFragmentMyFavoriteBars(Activity activity) {
		try {
			Intent intent = new Intent(activity, TransactionActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_CONSTANT_REDIRECT_FROM, ConfigConstants.Constants.MY_FAV_BARS);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectFragmentMyFavoriteBeers(Activity activity) {
		try {
			Intent intent = new Intent(activity, TransactionActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_CONSTANT_REDIRECT_FROM, ConfigConstants.Constants.MY_FAV_BEERS);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectFragmentMyFavoriteCocktails(Activity activity) {
		try {
			Intent intent = new Intent(activity, TransactionActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_CONSTANT_REDIRECT_FROM, ConfigConstants.Constants.MY_FAV_COCKTAILS);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectFragmentMyFavoriteLiquors(Activity activity) {
		try {
			Intent intent = new Intent(activity, TransactionActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_CONSTANT_REDIRECT_FROM, ConfigConstants.Constants.MY_FAV_LIQUORS);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectFragmentMyAlbums(Activity activity) {
		try {
			Intent intent = new Intent(activity, TransactionActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_CONSTANT_REDIRECT_FROM, ConfigConstants.Constants.MY_ALBUMS);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectFragmentPrivacySettings(Activity activity) {
		try {
			Intent intent = new Intent(activity, TransactionActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_CONSTANT_REDIRECT_FROM, ConfigConstants.Constants.PRIVACY_SETTINGS);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectFragmentChangePassword(Activity activity) {
		try {
			Intent intent = new Intent(activity, TransactionActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_CONSTANT_REDIRECT_FROM, ConfigConstants.Constants.CHANGE_PASSWORD);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectFragmentArticles(Activity activity) {
		try {
			Intent intent = new Intent(activity, TransactionActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_CONSTANT_REDIRECT_FROM, ConfigConstants.Constants.ARTICLES);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void redirectFragmentBarTriviaGame(Activity activity) {
		try {
			Intent intent = new Intent(activity, TransactionActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_CONSTANT_REDIRECT_FROM, ConfigConstants.Constants.BAR_TRIVIA_GAME);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/* Fragment Called through Transaction Activity */
	
	/********************  The following methods are used to do transaction.  ********************/
	
	/* To check whether Bar View is selected or not. */
	@SuppressLint("DefaultLocale")
	private boolean isBarViewSelected(String title) {
		boolean isBarViewSelected = false;
		if(title.equals(ConfigConstants.Constants.FIND_BAR.toUpperCase())) {
			isBarViewSelected = true;
		} 
		else if(title.equals(ConfigConstants.Constants.BAR_SEARCH.toUpperCase())) {
			isBarViewSelected = true;
		} 
		else if(title.equals(ConfigConstants.Constants.SEARCH_FOR_HAPPY_HOUR.toUpperCase())) {
			isBarViewSelected = true;
		}
		else if(title.equals(ConfigConstants.Constants.BARS_NEAR_YOU.toUpperCase())) {
			isBarViewSelected = true;
		}
		else if(title.equals(ConfigConstants.Constants.HAPPY_HOURS_NEAR_YOU.toUpperCase())) {
			isBarViewSelected = true;
		}
		/*else if(title.equals(ConfigConstants.getInstance().BARS.toUpperCase())) {
			isBarViewSelected = true;
		}*/ 
		return isBarViewSelected;
	}
	
	public void redirectHomeActivity(Activity activity) {
		try {
			Intent intent = new Intent(activity, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* A method is used for the Log Out functionality. */
	public void doLogOut() {
		DialogFragment newFragment = FragmentAlertDialogLogOut.newInstance("American Bars", "Are you sure you want to exit American Bars?", ConfigConstants.Constants.CONSTANT_YES, ConfigConstants.Constants.CONSTANT_NO);
	    newFragment.show(getFragmentManager(), "dialog");
	}
	
	//  For the LogOut functionality
	public void doPositiveClick() {
		try {
			//  Called the AsyncTask for the Logout functionality
			if(Utils.getInstance() .isNetworkAvailable(BaseActivity.this)) {		
				new AsynTaskLogout().execute();
			}
			else{
				Utils.getInstance().showToastNoInternetAvailable(BaseActivity.this);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask called on the Logout click
	private class AsynTaskLogout extends AsyncTask<Void, Void, Void> {
    	
		private ProgressDialog pd = new ProgressDialog(BaseActivity.this);
		private String responseString = "";
    	private String status = "";
		
    	@Override
		protected void onPreExecute() {
			try {
				// TODO Auto-generated method stub
				super.onPreExecute();
				this.pd.setMessage(asyncTaskLogOutMessage);
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
	        	nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(getApplicationContext()).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(getApplicationContext()).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(getApplicationContext()).getData(SessionManager.KEY_UNIQUE_CODE)));
	        	
		    	responseString = sh.makeServiceCall(ConfigConstants.Urls.user_logout, ServiceHandler.POST, nameValuePairs);
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
					JSONObject jsonObjParent = new JSONObject(responseString);
					status = jsonObjParent.getString("status");
					//Log.i("status",status);
					
					//  Means Successful Login
					if(status.equals("success")){
						
						/*
						 * Now, if user is logged in using facebook credentials then we need to remove it's credentials.
						 */
						if(SessionManager.getInstance(BaseActivity.this).getData(SessionManager.KEY_LOGIN_TYPE).equals(ConfigConstants.Constants.LOGIN_FACEBOOK)) {
							/* Facebook Logout functionality */
							try{
								LoginManager.getInstance().logOut();
							}
							catch(Exception e){
								e.printStackTrace();
							}
						}
						
						//  Clear the Shared Preferences data i.e. Clear all the Session data (This is because authentication is already over.)
						SessionManager.getInstance(getApplicationContext()).logoutUser();
						
						//  Now, here clear all the static instances of the helper classes.
						//ConfigConstants.getInstance().clearInstance();
						SessionManager.getInstance(getApplicationContext()).clearInstance();
						Utils.getInstance().clearInstance();
						Validation.getInstance().clearInstance();
						
						/*
						 * Now, simply redirect to the login screen by clearing all the previous stack
						 */
						redirectLoginActivity(BaseActivity.this, true);
					}
				}
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}
			if(this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}
    }
	
	public void doNegativeClick() {
	    //  Nothing need to do here.
	    //Log.i("FragmentAlertDialog", "Negative click!");
	}
 }
