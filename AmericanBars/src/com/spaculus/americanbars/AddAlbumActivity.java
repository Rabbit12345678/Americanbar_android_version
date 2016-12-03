package com.spaculus.americanbars;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spaculus.americanbars.dialogfragments.FragmentAlertDialogDelete;
import com.spaculus.beans.Media;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.MultipartUtility;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;
import com.spaculus.helpers.Validation;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class AddAlbumActivity extends BaseActivityMedia {
	
	private RelativeLayout relativeLayoutAddAlbum;
	private EditText etTitle;
	private ImageView ivAddImage;
	private LinearLayout hiddenLinearLayoutAlbumImages;
	//private ListView listViewAlbumImages;
	private Button buttonStatus;
	private Spinner spinnerStaus;
	private Button buttonCreateAlbum, buttonCancel;
	
	//  To get the bar_gallery_id
	private String bar_gallery_id = "";
	
	//  For the Spinner functionality
	private ArrayList<String> statusOptionsList = null;
 	private String selectedStatus = "";
 	
	/* To display existing Album's Images functionality */ 
 	private ArrayList<Media> albumImagesList = null;
 	//private CustomAddAlbumImagesAdapter adapter = null;
 	
	/*
	 * The following contants are used to identify whether image is added or
	 * edited.
	 */
 	private int CONSTANT_ADD_IMAGE =  -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_add_album);
			
			//  To get the bar id
			Bundle b = getIntent().getExtras();
			bar_gallery_id = b.getString(ConfigConstants.Keys.KEY_BAR_GALLERY_ID);
					
			//  Create Action Bar
			createActionBar(bar_gallery_id.isEmpty() ? "Add Album" : "Edit Album", R.layout.custom_actionbar, AddAlbumActivity.this, true);
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);
			
			// Mapping of all the views
			mappedAllViews();
			
			//  For the Edit Album
			if(!bar_gallery_id.isEmpty()) {
				//  Now, get the existing Album List here.
				if(Utils.getInstance() .isNetworkAvailable(AddAlbumActivity.this)) {
					new AsynTaskGetAlbumImagesList().execute();
				}
				else{
					Utils.getInstance().showToastNoInternetAvailable(AddAlbumActivity.this);
				}
			}
			
			//  To hide the soft key board on the click of anywhere onto the screen 
			relativeLayoutAddAlbum.setOnTouchListener(new OnTouchListener() {
					@SuppressLint("ClickableViewAccessibility") 
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if(v==relativeLayoutAddAlbum){
							hideSoftKeyboard();
							return true;
						}
						return false;
					}
			});
			
			//  Status button click event for the Spinner
			buttonStatus.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					try {
						spinnerStaus.performClick();
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
					
			//  Spinner for the Status Options
			spinnerStaus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg, int position, long arg3) {
					try {
						//Log.i("onItemSelected", "onItemSelected");
						
						//  Set the button text value
						buttonStatus.setText(statusOptionsList.get(position));
						
						//  To get the value
						selectedStatus = statusOptionsList.get(position);
						
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
			
			//  Add New Image
			ivAddImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						/*
						 * Now, first of all check whether user has selected one image to upload or not.
						 */
						boolean isLocalImageSelected = false;
						for(int i=0; i<albumImagesList.size(); i++) {
							if(!albumImagesList.get(i).isImageFromWeb()) {
								isLocalImageSelected = true;
								break;
							}
						}
						if(!isLocalImageSelected) {
							selectMediaMultiple(ConfigConstants.Constants.CONSTANT_MEDIA_PICTURE, 1, CONSTANT_ADD_ALBUM_ACTIVITY_MULTIPLE_SELECTION, AddAlbumActivity.this);//Single Selection
						}
						else {
							//Toast.makeText(AddAlbumActivity.this, "You can upload only one image at a time.", Toast.LENGTH_SHORT).show();
							Utils.toastLong(AddAlbumActivity.this, "You can upload only one image at a time.");
						}
						/*selectMediaMultiple(ConfigConstants.getInstance().CONSTANT_MEDIA_PICTURE, 0, CONSTANT_ADD_ALBUM_ACTIVITY_MULTIPLE_SELECTION, AddAlbumActivity.this);//Multiple Selection*/
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  Add/Edit Album button click event
			buttonCreateAlbum.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						//  First, hide the soft key board.
						hideSoftKeyboard();
									
						//  Now, first of all check all the validations
						String validationResponse  = Validation.getInstance().addEditAlbum(etTitle.getText().toString().trim(), albumImagesList.size());
						
						//  Means all the fields are properly entered
						if(validationResponse.isEmpty()){
							
							//  Now, first of check the size of all the images (which are selected local only)
							double totalSizeInBytes = 0;
							for(int i=0;i<albumImagesList.size();i++){
								totalSizeInBytes += albumImagesList.get(i).getMediaSize();
							}
							if(Utils.getInstance().isMaxSizeExceed(totalSizeInBytes)){
								//Toast.makeText(getApplicationContext(), ConfigConstants.getInstance().maxUploadMediaMessage, Toast.LENGTH_SHORT).show();
								Utils.toastLong(AddAlbumActivity.this, ConfigConstants.Messages.maxUploadMediaMessage);
							}
							else {
								//  Called the AsyncTask to send the email for the Forgot Password functionality
								if(Utils.getInstance().isNetworkAvailable(AddAlbumActivity.this)) {		
									new AsynTaskAddEditAlbumMultiPart().execute();
								}
								else{
									Utils.getInstance().showToastNoInternetAvailable(AddAlbumActivity.this);
								}
							}
						}
						else {
							//  Show respective validation message
							//Toast.makeText(AddAlbumActivity.this, validationResponse, Toast.LENGTH_SHORT).show();
							Utils.toastLong(AddAlbumActivity.this, validationResponse);
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
						//  Now, here simply close this activity
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
			relativeLayoutAddAlbum = (RelativeLayout)findViewById(R.id.relativeLayoutAddAlbum);
			etTitle = (EditText) findViewById(R.id.etTitle);
			ivAddImage = (ImageView) findViewById(R.id.ivAddImage);
			//listViewAlbumImages = (ListView) findViewById(R.id.listViewAlbumImages);
			hiddenLinearLayoutAlbumImages = (LinearLayout)findViewById(R.id.hiddenLinearLayoutAlbumImages);
			buttonStatus = (Button) findViewById(R.id.btnStatus);
			spinnerStaus = (Spinner) findViewById(R.id.spinnerStaus);
			buttonCreateAlbum = (Button)findViewById(R.id.btnCreateAlbum);
			buttonCancel = (Button)findViewById(R.id.btnCancel);
			//  Set the "Create Album" button text.
			if(bar_gallery_id.isEmpty()) {
				/*buttonCreateAlbum.setText(getResources().getString(R.string.btn_create_album))*/;
				buttonCreateAlbum.setText(getResources().getString(R.string.btn_save));
			}
			else {
				/*buttonCreateAlbum.setText(getResources().getString(R.string.btn_edit_album));*/
				buttonCreateAlbum.setText(getResources().getString(R.string.btn_save));
			}
			//  For the Spinner functionality
			statusOptionsList = new ArrayList<String>();
			statusOptionsList.add("Active");
			statusOptionsList.add("Inactive");
			//  Initialize and bind the Spinner adapter here
			ArrayAdapter<String> adapterStausOptions = new ArrayAdapter<String>(AddAlbumActivity.this, android.R.layout.simple_list_item_1, statusOptionsList);
			adapterStausOptions.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
			spinnerStaus.setAdapter(adapterStausOptions);
			
			/* To display existing Album's Images functionality */
			albumImagesList = new ArrayList<Media>();
			//adapter = new CustomAddAlbumImagesAdapter(AddAlbumActivity.this, R.layout.activity_add_album_list_item, albumImagesList);
			//listViewAlbumImages.setAdapter(adapter);
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
			imm.hideSoftInputFromWindow(etTitle.getWindowToken(), 0);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask to get the Album's images list
	public class AsynTaskGetAlbumImagesList extends AsyncTask<String, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(AddAlbumActivity.this);
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
		protected Void doInBackground(String... params) {
			ServiceHandler sh = new ServiceHandler();
	        try {
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        	nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(AddAlbumActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(AddAlbumActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(AddAlbumActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("bar_gallery_id", bar_gallery_id));
		        
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(ConfigConstants.Urls.edit_album, ServiceHandler.POST, nameValuePairs);
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
				JSONObject jsonObjParent = null;
				if(responseString!=null){
					jsonObjParent = new JSONObject(responseString);
					status = jsonObjParent.getString("status");
					//Log.i("status",status);
				}
				
				if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)){
					/* Set Title and Status */
					if(jsonObjParent.has("gallery")) {
						setTitleStatus( jsonObjParent.getJSONObject("gallery"));
					}
					/* Set Album's Images */
					if(jsonObjParent.has("galleryimages")) {
						setAlbumImages(jsonObjParent.getJSONArray("galleryimages"));
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
	
	//  A method is used to get and set the gallery title, status and images data
	private void setTitleStatus(JSONObject jsonObj) {
		try {	
			//  Title
			etTitle.setText(Utils.getInstance().isTagExists(jsonObj, "title"));
			
			//  Status
			if(Utils.getInstance().isTagExists(jsonObj, "status").equals("Active")) {
				spinnerStaus.setSelection(0);
			}
			else {
				spinnerStaus.setSelection(1);
			}
			buttonStatus.setText(Utils.getInstance().isTagExists(jsonObj, "status"));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  A method is used to get and set the Album's images data
	private void setAlbumImages(JSONArray jsonArray) {
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				albumImagesList.add(new Media(Utils.getInstance().isTagExists(jsonObj, "bar_image_id"), Utils.getInstance().isTagExists(jsonObj, "bar_gallery_id"), 
						Utils.getInstance().isTagExists(jsonObj, "bar_image_name"), Utils.getInstance().isTagExists(jsonObj, "image_title"), true));
			}
			
			/*//  Now, here notify an adapter
			notifyAdapter();*/
			
			/* A method is used to reset all the views. */
			resetViews();
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * A method is used to create the custom view and add the layout into the
	 * Linear Layout for the Album's Images data
	 */
	@SuppressLint("InflateParams") 
	public void setAlbumImagesData(int selectedPosition, ArrayList<Media> mediaList1, Media objMedia) {
		try {
			
			//  Add Case
			if(selectedPosition==CONSTANT_ADD_IMAGE){
				//for (int i = 0; i < mediaList1.size(); i++) {
				for (int i = hiddenLinearLayoutAlbumImages.getChildCount(); i < mediaList1.size(); i++) {
					final int index = i;
					setData(index, mediaList1.get(index), selectedPosition);
				}
			}
			//  Edit Case
			/* As here edit case so here there is only one object at 0th position. */
			else {
				setData(selectedPosition, objMedia, selectedPosition);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressLint("InflateParams") @SuppressWarnings("unused")
	private void setData(final int index, final Media objMedia, int selectedPosition) {
		
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View customView = inflater.inflate(R.layout.activity_add_album_list_item, null);
		
		ImageView ivAlbumImage = (ImageView) customView.findViewById(R.id.ivAlbumImage);
		EditText etImageTitle = (EditText) customView.findViewById(R.id.etImageTitle);
		final ImageView ivDeleteAlbumImage = (ImageView) customView.findViewById(R.id.ivDeleteAlbumImage);
		TextWatcher textWatcher = null;
		
		ivAlbumImage.setTag(objMedia.getId());
		etImageTitle.setTag(objMedia.getId());
		ivDeleteAlbumImage.setTag(index);
		
		/* For the Image Title */
		/* Remove any existing TextWatcher that will be keyed to the wrong ListItem */
        if (textWatcher != null)
            etImageTitle.removeTextChangedListener(textWatcher);
        
        /* Keep a reference to the TextWatcher so that we can remove it later */
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
         	   //monitoringList.get(position).setTextAnswer(s.toString());// = s.toString();
         	   objMedia.setImage_title(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        etImageTitle.addTextChangedListener(textWatcher);
        
		//  Now, set the data
		//  Title
		etImageTitle.setText(objMedia.getImage_title());
		
		//  Image - Now, check whether web or local 
		if(objMedia.isImageFromWeb()) {
			setLogo(ConfigConstants.ImageUrls.bar_gallery_thumb_big140by140, objMedia.getBar_image_name(), ivAlbumImage, R.drawable.gallery_small_place_holder);
		}
		else {
			Utils.getInstance().setImageDevice(AddAlbumActivity.this, objMedia.getMediaPath(), ivAlbumImage);
		}
		
		//  Now, add the custom view to the layout
		//  Now, here first of all check whether Add/Edit case
		//  Add Case
		if(selectedPosition==-1) {
			//hiddenLinearLayoutAlbumImages.addView(customView,index);
			hiddenLinearLayoutAlbumImages.addView(customView);
		}
		else {
			hiddenLinearLayoutAlbumImages.removeViewAt(selectedPosition);
			hiddenLinearLayoutAlbumImages.addView(customView,selectedPosition);
		}
		
		//  Add/Edit Image functionality
		ivAlbumImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					selectMediaSingle(ConfigConstants.Constants.CONSTANT_MEDIA_PICTURE, 1, CONSTANT_ADD_ALBUM_ACTIVITY_SINGLE_SELECTION, AddAlbumActivity.this, index);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//  Delete image functionality
		ivDeleteAlbumImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					/* First of all check whether more than 1 image or not */
					if(albumImagesList.size()>0) {
						//  For the Delete functionality
						FragmentAlertDialogDelete objCreateDelete = new FragmentAlertDialogDelete("Delete Album Image", "Are you sure you want to delete this album image?", "OK", "Cancel", ConfigConstants.Constants.ALBUM_IMAGE, objMedia.getBar_image_id(), AddAlbumActivity.this, objMedia.isImageFromWeb(), index);
						objCreateDelete.show(getFragmentManager(), "dialog");
					}
					else {
						//Toast.makeText(AddAlbumActivity.this, "At least one image must be selected.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(AddAlbumActivity.this, "At least one image must be selected.");
					}
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
		
	/* The following method is used to edit the existing web or local selected media object. */
	/* This method is called when user clicked on the existing picture to change the image. */
	public void editMediaObject(ArrayList<Media> mediaList1, int selectedPosition) {
		try {
			
			String image_title = albumImagesList.get(selectedPosition).getImage_title();
			String bar_image_id = albumImagesList.get(selectedPosition).getBar_image_id();
			String bar_image_name = albumImagesList.get(selectedPosition).getBar_image_name();
			
			//  Now, here first of all replace the albumImagesList's object at the selected position
			albumImagesList.set(selectedPosition, new Media(mediaList1.get(0).getMediaPath(), mediaList1.get(0).getMediaName(), mediaList1.get(0).getMediaExtension(), mediaList1.get(0).getMediaSize(),
					image_title, bar_image_id, bar_image_name, false));
			
			//  Now, set the image_title, bar_image_id and bar_image_name for the mediaList1 too.
			mediaList1.get(0).setImage_title(image_title);
			mediaList1.get(0).setBar_gallery_id(bar_image_id);
			mediaList1.get(0).setBar_image_name(bar_image_name);
			
			//  Now, set the Album's Images Data
			//setAlbumImagesData(albumImagesList.get(selectedPosition), selectedPosition);
			setAlbumImagesData(selectedPosition, null, albumImagesList.get(selectedPosition));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* A method is used to add a single or multiple local media objects. */
	/* This method is called when user clicked on the Add image icon to add totally a new image.*/
	public void addLocalObject(ArrayList<Media> mediaList1) {
		try {
			for(int i=0; i<mediaList1.size(); i++) {
				albumImagesList.add( new Media(mediaList1.get(i).getMediaPath(), mediaList1.get(i).getMediaName(), mediaList1.get(i).getMediaExtension(), mediaList1.get(i).getMediaSize(),
						"", "", "", false));
			}
			//  Now, set the Album's Images Data
			//setAlbumImagesData(mediaList1, -1);
			//setAlbumImagesData(albumImagesList, CONSTANT_ADD_IMAGE);
			setAlbumImagesData(CONSTANT_ADD_IMAGE, albumImagesList, null);
			
			/* Hide Add image because of single selection */
			/*showHideAddImage(View.GONE);*/
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  AsyncTask for the Add/Edit Album Functionality
	public class AsynTaskAddEditAlbumMultiPart extends AsyncTask<Void, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(AddAlbumActivity.this);
    	private String responseString = "";
    	private String status = "";
    	/*private HttpURLConnection connection = null;
		private DataOutputStream outputStream = null;
		private DataInputStream inputStream = null;*/
		private String charset = "UTF-8";
		private File[] uploadFileArray =  new File[albumImagesList.size()];
		//private File uploadFile = null;
		private String response = "";
		
    	@Override
		protected void onPreExecute() {
			try {
				super.onPreExecute();
				for(int i=0;i<albumImagesList.size();i++){
					if(isWebObject(i)) {
						//uploadFileArray[i] = null;
					}
					else {
						uploadFileArray[i] = new File(albumImagesList.get(i).getMediaPath());	
					}
					//totalSize += uploadFileArray[i].length();
				}
				/*if(mediaList.size()>0) {
					uploadFile = new File(mediaList.get(0).getMediaPath());
				}*/
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
	            responseString  = ConfigConstants.Urls.add_album;
	        	//Log.i("responseString",responseString);
	        	
	        	MultipartUtility multipart = new MultipartUtility(responseString, charset);
	        	for(int i=0;i<albumImagesList.size();i++){
	        		//  Means Web Object
	        		if(isWebObject(i)) {
	        			multipart.addFilePart("photo_image[]", uploadFileArray[i], "WS_"+albumImagesList.get(i).getBar_image_name(), false);
	        			multipart.addFormField("pre_img[]", albumImagesList.get(i).getBar_image_name());
	        			multipart.addFormField("image_id[]", albumImagesList.get(i).getBar_image_id());
	        			multipart.addFormField("image_title[]", albumImagesList.get(i).getImage_title());
	        		}
	        		//  Means Local Object
	        		else {
	        			multipart.addFilePart("photo_image[]", uploadFileArray[i], "DEVICE_"+albumImagesList.get(i).getMediaName(), true);
	        			multipart.addFormField("pre_img[]", albumImagesList.get(i).getBar_image_name());
	        			multipart.addFormField("image_id[]", albumImagesList.get(i).getBar_image_id());
	        			multipart.addFormField("image_title[]", albumImagesList.get(i).getImage_title());
	        		}
	            }
	        	multipart.addFormField("user_id", SessionManager.getInstance(AddAlbumActivity.this).getData(SessionManager.KEY_USER_ID));
	        	multipart.addFormField("device_id", SessionManager.getInstance(AddAlbumActivity.this).getData(SessionManager.KEY_DEVICE_ID));
	        	multipart.addFormField("unique_code", SessionManager.getInstance(AddAlbumActivity.this).getData(SessionManager.KEY_UNIQUE_CODE));
	        	multipart.addFormField("bar_gallery_id", bar_gallery_id);
	        	multipart.addFormField("status", selectedStatus);
	        	multipart.addFormField("title", etTitle.getText().toString().trim());
	               
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
				if(response!=null){
					//Log.i("response", response);
					JSONObject jsonObj = new JSONObject(response);
					status = jsonObj.getString("status");
					//Log.i("status",status);
				}
				
				if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)){
					if(bar_gallery_id.isEmpty()) {
						//Toast.makeText(AddAlbumActivity.this, "Your album is added successfully.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(AddAlbumActivity.this, "Your album is added successfully.");
					}
					else {
						//Toast.makeText(AddAlbumActivity.this, "Your album is edited successfully.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(AddAlbumActivity.this, "Your album is edited successfully.");
					}
					/*Either album is added or edited.*/
					ConfigConstants.isAddEditAlbum = true;
					
					//  Now, simply close this activity
					finish();
					onDestroy();
				}
				else {
					if(bar_gallery_id.isEmpty()) {
						//Toast.makeText(AddAlbumActivity.this, "Your album is not added. Please try again.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(AddAlbumActivity.this, "Your album is not added. Please try again.");
					}
					else {
						//Toast.makeText(AddAlbumActivity.this, "Your album is not edited. Please try again.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(AddAlbumActivity.this, "Your album is not edited. Please try again.");
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
	
	/* A method is used to check whether object is local or not. */
	private boolean isWebObject(int position) {
		boolean isWebObject = false;
		if(albumImagesList.get(position).isImageFromWeb()) {
			isWebObject = true;
		}
		else {
			isWebObject = false;
		}
		return isWebObject;
	}
	
	/* A method is used to delete a selected image. */
	public void deleteView(int index) {
		hiddenLinearLayoutAlbumImages.removeViewAt(index);
		albumImagesList.remove(index);
		
		//  Now, reset all the views
		resetViews();
	}
	
	/* A method is used to reset all the views. */
	private void resetViews() {
		if(hiddenLinearLayoutAlbumImages.getChildCount() != 0) {
			hiddenLinearLayoutAlbumImages.removeAllViews();
		}
		if(albumImagesList.size()>0) {
			hiddenLinearLayoutAlbumImages.setVisibility(View.VISIBLE);
			//  Now, set the Album's Images Data
			setAlbumImagesData(CONSTANT_ADD_IMAGE, albumImagesList, null);
		}
		else {
			//hiddenLinearLayoutAlbumImages.setVisibility(View.GONE);
		}
	}
	
	//  AsyncTask to delete an album's image (From Web)
	public class AsyncTaskDeleteAlbumImage extends AsyncTask<String, Void, Void> {
		
		private ProgressDialog pd = new ProgressDialog(AddAlbumActivity.this);
    	private String responseString = "";
    	private String status = "";
    	private String indexToDelete = "";
    	
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
		protected Void doInBackground(String... params) {
	        try {
	        	indexToDelete = params[1];
	        	ServiceHandler sh = new ServiceHandler();
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        	nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(AddAlbumActivity.this).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(AddAlbumActivity.this).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(AddAlbumActivity.this).getData(SessionManager.KEY_UNIQUE_CODE)));
		        nameValuePairs.add(new BasicNameValuePair("bar_image_id", params[0]));
		        			
	        	// Making a request to URL and getting response
				responseString = sh.makeServiceCall(ConfigConstants.Urls.remove_gallery_image, ServiceHandler.POST, nameValuePairs);
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
				JSONObject jsonObjParent = null;
				if(responseString!=null){
					jsonObjParent = new JSONObject(responseString);
					status = jsonObjParent.getString("status");
					//Log.i("status",status);
				}
				
				if(status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)) {
					//Toast.makeText(AddAlbumActivity.this, "Your album image deleted successfully.", Toast.LENGTH_SHORT).show();
					Utils.toastLong(AddAlbumActivity.this, "Your album image deleted successfully.");
					//  Now, here we need to remove the view
					deleteView(Integer.parseInt(indexToDelete));
				}
				else {
					//Toast.makeText(AddAlbumActivity.this, "Your album image is not deleted. Please try again.", Toast.LENGTH_SHORT).show();
					Utils.toastLong(AddAlbumActivity.this, "Your album image is not deleted. Please try again.");
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
	
	//  AsyncTask to delete an album's image (From Local)
	public class AsyncTaskDeleteAlbumImageLocal extends AsyncTask<String, Void, Void> {
    	private String indexToDelete = "";
    	private ProgressDialog pd = new ProgressDialog(AddAlbumActivity.this);
    	
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
		protected Void doInBackground(String... params) {
	        try {
	        	indexToDelete = params[0];
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
				//  Now, here we need to remove the view
				deleteView(Integer.parseInt(indexToDelete));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			if(this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}
	}
	
	/* A method is used to set the visibility to of add image icon as we have to allow an user to add only image at a time. 
	private void showHideAddImage(int status) {
		ivAddImage.setVisibility(status);
	}*/
}
