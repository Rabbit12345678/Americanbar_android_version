package com.spaculus.americanbars;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spaculus.adapters.GalleryImageAdapter;
import com.spaculus.beans.GalleryBean;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.Utils;
//import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class BaseActivityGallery extends BaseActivity { 

	/* Gallery */
	private ImageView ivGallerySelectedImage;
	protected TextView tvGalleryName;
	protected ImageView ivShareImage;
	protected Spinner spinnerShare;
	private Gallery gallery;
	private ImageView ivGalleryLeftArrow;
	private ImageView ivGalleryRightArrow;
	protected TextView tvNoBarGallery;
	private int selectedImagePosition = 0;
	private ArrayList<GalleryBean> galleryList = null;
	private GalleryImageAdapter adapterGallery;
	/* Gallery */
	
	private String galleySmallImageURL_local = "";
	private String galleyBigImageURL_local = "";
	private String galleyOriginalImageURL_local = "";
	
	/* To identify whether need to set the image titles or not. */
	private String isNeedToShowImageTitle = "";
	
	//  For the Share Options
	private String selectedSearchOption = "";
	//  To prevent the spinner for a default call
 	private boolean isShareImageClickedOnce = false;
	
	//  Now, mapped all the gallery views
	protected void mappedGalleryViews(String galleySmallImageURL1, String galleyBigImageURL1, String galleyOriginalImageURL1, String isNeedToShowImageTitle1){
		
		try {
			this.galleySmallImageURL_local = galleySmallImageURL1;
			this.galleyBigImageURL_local = galleyBigImageURL1;
			this.galleyOriginalImageURL_local = galleyOriginalImageURL1;
			this.isNeedToShowImageTitle = isNeedToShowImageTitle1;
			
			ivGallerySelectedImage = (ImageView) findViewById(R.id.ivGallerySelectedImage);
			ivGalleryLeftArrow = (ImageView) findViewById(R.id.ivGalleryLeftArrow);
			ivGalleryRightArrow = (ImageView) findViewById(R.id.ivGalleryRightArrow);
			
			gallery = (Gallery) findViewById(R.id.gallery);
			
			/*DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			MarginLayoutParams mlp = (MarginLayoutParams) gallery.getLayoutParams();
			mlp.setMargins((int) -(metrics.widthPixels / 3), mlp.topMargin, mlp.rightMargin, mlp.bottomMargin);*/
			
			galleryList = new ArrayList<GalleryBean>();
			adapterGallery = new GalleryImageAdapter(BaseActivityGallery.this, R.layout.gallery_custom_image_view, galleryList, galleySmallImageURL_local);
			gallery.setAdapter(adapterGallery);
			/*gallery.setSelection(1);*/
			
			//  Gallery 
			gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
					try {
						selectedImagePosition = pos;
						
						/* For the Arrow Image */
						if (selectedImagePosition > 0 && selectedImagePosition < galleryList.size() - 1) {
							/*ivGalleryLeftArrow.setImageDrawable(getResources().getDrawable(R.drawable.arrow_left_enabled));
							ivGalleryRightArrow.setImageDrawable(getResources().getDrawable(R.drawable.arrow_right_enabled));*/
							ivGalleryLeftArrow.setVisibility(View.VISIBLE);
							ivGalleryRightArrow.setVisibility(View.VISIBLE);
						} 
						else if (selectedImagePosition == 0) {
							//ivGalleryLeftArrow.setImageDrawable(getResources().getDrawable(R.drawable.arrow_left_disabled));
							ivGalleryLeftArrow.setVisibility(View.GONE);
						} 
						else if (selectedImagePosition == galleryList.size() - 1) {
							//ivGalleryRightArrow.setImageDrawable(getResources().getDrawable(R.drawable.arrow_right_disabled));
							ivGalleryRightArrow.setVisibility(View.GONE);
						}
						
						/* Set the selected below small into the above big image. */
						setSelectedImage();
						
						/* Now, check whether need to set the image title or not. */
						if(isNeedToShowImageTitle.equals(ConfigConstants.Constants.CONSTANT_YES)) {
							setSelectedImageName();
						}
					} 
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
			
			//  Click on image to zoom
			ivGallerySelectedImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						//  To zoom an activity
						redirectZoomImageViewActivity(BaseActivityGallery.this, galleyOriginalImageURL_local+galleryList.get(selectedImagePosition).getBar_image_name());
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  Share image click event
			ivShareImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						isShareImageClickedOnce = true;
						spinnerShare.performClick();
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
					try {
						
						/*Get image uri from imageview*/
						/*Toast.makeText(BaseActivityGallery.this, "Share Image is clicked.", Toast.LENGTH_SHORT).show();
						//ImageView siv = (ImageView) findViewById(R.id.ivResult);
						Drawable mDrawable = ivGallerySelectedImage.getDrawable();
						Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

						String path = Images.Media.insertImage(getContentResolver(), 
						    mBitmap, "Image Description", null);

						Uri uri = Uri.parse(path);*/
						
					//  Now, first of all get the Photo Gallery Details
						if(Utils.getInstance() .isNetworkAvailable(BaseActivityGallery.this)) {
							new AsynTaskShareImageWithText().execute();
						}
						else{
							Utils.getInstance().showToastNoInternetAvailable(BaseActivityGallery.this);
						}
						
						
						   /* InputStream is = (InputStream) new URL(galleyOriginalImageURL_local+galleryList.get(selectedImagePosition).getBar_image_name()).getContent();
						    Drawable mDrawable = Drawable.createFromStream(is, "src name");
						    
						    Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

						    String path = Images.Media.insertImage(getContentResolver(), 
								    mBitmap, "Image Description", null);

								Uri uri = Uri.parse(path);
						
						
						
						Intent sharingIntent = new Intent(Intent.ACTION_SEND);
						//Uri screenshotUri = Uri.fromFile(imageFileToShare);
						sharingIntent.setType("image/*");
						sharingIntent.setPackage("com.whatsapp");
						sharingIntent.putExtra(Intent.EXTRA_TEXT, galleryList.get(selectedImagePosition).getImage_title());
						sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
						startActivity(sharingIntent);*/
					} 
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
			});
			
			//  Spinner for the Search Options
			spinnerShare.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg, int position, long arg3) {
					try {
						if(isShareImageClickedOnce) {
							//Log.i("onItemSelected", "onItemSelected");
							//  To get the value
							selectedSearchOption = searchOptionsList.get(position);
							String packageName = "";
							String constantOption = "";
							if(selectedSearchOption.equals(ConfigConstants.Constants.CONSTANT_FACEBOOK)) {
								packageName = "com.facebook.katana";
								constantOption = ConfigConstants.Constants.CONSTANT_FACEBOOK;
							}
							else if(selectedSearchOption.equals(ConfigConstants.Constants.CONSTANT_TWITTER)) {
								packageName = "com.twitter.android";
								constantOption = ConfigConstants.Constants.CONSTANT_TWITTER;
							}
							/*else if(selectedSearchOption.equals(ConfigConstants.getInstance().CONSTANT_LINKEDIN)) {
								packageName = "com.linkedin.android";
								constantOption = ConfigConstants.getInstance().CONSTANT_LINKEDIN;
							}*/
							else if(selectedSearchOption.equals(ConfigConstants.Constants.CONSTANT_GOOGLE_PLUS)) {
								packageName = "com.google.android.apps.plus";
								constantOption = ConfigConstants.Constants.CONSTANT_GOOGLE_PLUS;
							}
							else if(selectedSearchOption.equals(ConfigConstants.Constants.CONSTANT_PINTEREST)) {
								packageName = "com.pinterest";
								constantOption = ConfigConstants.Constants.CONSTANT_PINTEREST;
							}

							String[] valuesArray = {packageName, constantOption, galleyOriginalImageURL_local, galleryList.get(selectedImagePosition).getBar_image_name(), galleryList.get(selectedImagePosition).getImage_title()};
							//  Now, call an AsyncTask to share an image with a link
							if(Utils.getInstance() .isNetworkAvailable(BaseActivityGallery.this)) {
								new AsynTaskShareImageWithText().execute(valuesArray);
							}
							else{
								Utils.getInstance().showToastNoInternetAvailable(BaseActivityGallery.this);
							}
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
					}	
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		ivGalleryLeftArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (selectedImagePosition > 0) {
					--selectedImagePosition;
				}
				gallery.setSelection(selectedImagePosition, false);
			}
		});

		ivGalleryRightArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (selectedImagePosition < galleryList.size() - 1) {
					++selectedImagePosition;
				}
				gallery.setSelection(selectedImagePosition, false);
			}
		});
	}
	
	/*
	 * A method is used to set the image into the ImageView which is selected from the Gallery.
	 */
	private void setSelectedImage() {
		try {
			/*BitmapDrawable bd = (BitmapDrawable) galleryList.get(selectedImagePosition);
			Bitmap b = Bitmap.createScaledBitmap(bd.getBitmap(), (int) (bd.getIntrinsicHeight() * 0.9), (int) (bd.getIntrinsicWidth() * 0.7), false);
			selectedImageView.setImageBitmap(b);
			selectedImageView.setScaleType(ScaleType.FIT_XY);*/
			setLogo(galleyBigImageURL_local, galleryList.get(selectedImagePosition).getBar_image_name(), ivGallerySelectedImage, R.drawable.gallery_big_place_holder);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * A method is used to set the image's name.
	 */
	private void setSelectedImageName() {
		try {
			//tvGalleryName.setText(Utils.getInstance().setCapitalLetter(galleryList.get(selectedImagePosition).getImage_title()));
			tvGalleryName.setText(galleryList.get(selectedImagePosition).getImage_title());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * A method is used to get and set the Gallery Data (Full Mug Bar Details Screen)
	 */
	protected void setGalleryWithTitleJSONObject(JSONObject jsonObjectBarDetails) {
		try {
			String galleryImageName = "";
			JSONArray jsonArrayResult = jsonObjectBarDetails.getJSONArray("result");
			for (int i = 0; i < jsonArrayResult.length(); i++) {
				JSONObject jsonObj = jsonArrayResult.getJSONObject(i);
				galleryList.add(new GalleryBean(Utils.getInstance().isTagExists(jsonObj, "bar_image_name")));
				galleryImageName = Utils.getInstance().isTagExists(jsonObj, "title");	
			}
			
			/* Now, here check whether gallery is exists or not. */
			if (galleryList.size() > 0) {
				//  Set Gallery Folder Name
				gallery.setVisibility(View.VISIBLE);
				tvGalleryName.setVisibility(View.VISIBLE);
				tvGalleryName.setText(Utils.getInstance().setCapitalLetter(galleryImageName));
				//  Now, notify the adapter
				adapterGallery.notifyDataSetChanged();
				//  For the Right Arrow Visibility
				showHideRightArrow();
				gallery.setSelection(selectedImagePosition, false);
				tvNoBarGallery.setVisibility(View.GONE);
				ivGallerySelectedImage.setVisibility(View.VISIBLE);
			}
			else {
				//  Means No gallery data is available.
				gallery.setVisibility(View.GONE);
				tvGalleryName.setVisibility(View.GONE);
				tvNoBarGallery.setVisibility(View.VISIBLE);
				ivGallerySelectedImage.setVisibility(View.GONE);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * A method is used to get and set the Gallery Data (Photo Gallery Details Screen)
	 */
	protected void setGalleryWithTitleJSONArray(JSONArray jsonArrayResult, String title) {
		try {
			for (int i = 0; i < jsonArrayResult.length(); i++) {
				JSONObject jsonObj = jsonArrayResult.getJSONObject(i);
				galleryList.add(new GalleryBean(Utils.getInstance().isTagExists(jsonObj, "bar_image_name"), Utils.getInstance().isTagExists(jsonObj, "image_title")));
			}
			//  Set Gallery Folder Name
			gallery.setVisibility(View.VISIBLE);
			tvGalleryName.setVisibility(View.VISIBLE);
			//tvGalleryName.setText(Utils.getInstance().setCapitalLetter(title));
			//  Now, notify the adapter
			adapterGallery.notifyDataSetChanged();
			//  For the Right Arrow Visibility
			showHideRightArrow();
			
			if (galleryList.size() > 0) {
				gallery.setSelection(selectedImagePosition, false);
			}
			else {
				//  Means No gallery data is available.
				gallery.setVisibility(View.GONE);
				tvGalleryName.setVisibility(View.GONE);
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * A method is used to get and set the Gallery Data (Bar Event Details Screen)
	 */
	protected void setGalleryWithOutTitle(JSONArray jsonArray) {
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				galleryList.add(new GalleryBean(Utils.getInstance().isTagExists(jsonObj, "event_image_name")));
			}
			//  Now, notify the adapter
			adapterGallery.notifyDataSetChanged();
			//  For the Right Arrow Visibility
			showHideRightArrow();
			
			if (galleryList.size() > 0) {
				gallery.setSelection(selectedImagePosition, false);
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}	
	
	/*
	 * A method is used to hide the right arrow if there is only one image into the Gallery
	 */
	private void showHideRightArrow() {
		if (galleryList.size() <= 1) {
			//ivGalleryRightArrow.setImageDrawable(getResources().getDrawable(R.drawable.arrow_right_disabled));
			ivGalleryRightArrow.setVisibility(View.GONE);
		}
		else {
			ivGalleryRightArrow.setVisibility(View.VISIBLE);
		}
	}
}
