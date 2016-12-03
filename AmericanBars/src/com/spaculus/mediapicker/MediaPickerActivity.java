package com.spaculus.mediapicker;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
//import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.spaculus.americanbars.R;
import com.spaculus.beans.Media;
import com.spaculus.helpers.ConfigConstants;

@SuppressLint("NewApi")
public class MediaPickerActivity extends SherlockFragmentActivity {
	private GridView gridView;
	private ListView listView;
	private ViewFlipper viewFlipper;
	private GalleryAdapter galleryAdapter;
	private MediaAdapter imagesAdapter;
	private SherlockFragmentActivity activity;
	private Gallery selectedGallery;
	private Gallery[] results;
	private TextView noResultsView;
	private Button selectButton;
	private Button cancelButton;
	// private Button selectmButton, cancelmButton;
	private int maxSelected;
	private int currentScreen;
	private ArrayList<Integer> ignoreIds;
	int type = -1;
	String mediaType = "";

	/* For the Action Bar */
	private TextView tvTitleActionBar;
	private ImageView ivMediaCapture;
	// private Button buttonActionBarNext;
	boolean isOnActivityResultCalled = false;
	
	/* For the Captured image path */
	/*private String path = "", filePath = "";
	private Uri uri = null;*/

	@SuppressLint("InflateParams")
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			setContentView(R.layout.activity_media_picker);

			Intent intent = getIntent();
			maxSelected = intent.getIntExtra("max", 0);
			ignoreIds = intent.getIntegerArrayListExtra("selected");
			type = intent.getIntExtra("type", 1);
			//Log.i("type", "" + type);
			if (type == 0) {
				mediaType = ConfigConstants.Constants.CONSTANT_MEDIA_VIDEO;
			} 
			else {
				mediaType = ConfigConstants.Constants.CONSTANT_MEDIA_PICTURE;
			}

			if (ignoreIds == null) {
				ignoreIds = new ArrayList<Integer>();
			}

			currentScreen = 0;
			activity = this;
			selectedGallery = null;
			results = new Gallery[0];
			
			//  To set the action bar
			setActionBar();

			//  Mapping of all the views
			mappedAllViews();
			
			/* Click events of all the views */
			listView.setOnItemClickListener(galleryListener);
			gridView.setOnItemClickListener(mediaListener);
			cancelButton.setOnClickListener(cancelListener);
			selectButton.setOnClickListener(selectListener);

			// Action Bar Image Click Event (Either Capture Image or Capture Video)
			ivMediaCapture.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					try {
						Intent intent = null;
						// Means Picture
						if (mediaType.equals(ConfigConstants.Constants.CONSTANT_MEDIA_PICTURE)) {
							/*intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(intent, ConfigConstants.CAPTURE_IMAGE_RESULT_CODE);*/
							
							//uri = generateImageURI();
							intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
							/*intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);*/
							startActivityForResult(intent, ConfigConstants.ResultCodes.CAPTURE_IMAGE_RESULT_CODE);
							
							/*Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
						    mediaScanIntent.setData(uri);
						    sendBroadcast(mediaScanIntent);*/
						}
						// Means Video
						else {
							intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
							// Video is recorded 60 seconds i.e. 1 minute
							intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
							startActivityForResult(intent, ConfigConstants.ResultCodes.CAPTURE_VIDEO_RESULT_CODE);
							setResult(ConfigConstants.ResultCodes.CAPTURE_VIDEO_RESULT_CODE);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			if (!isOnActivityResultCalled) {
				new GalleryLoader(this, getString(R.string.mediapicker_loading_galleries)).execute();
			}
		} 
		catch (NotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// This method is used to set the action bar data here.
	@SuppressLint("InflateParams") 
	private void setActionBar() {
		try {
			// To set the color of the Action Bar
			getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_action_bar));

			View viewActionBar = getLayoutInflater().inflate(R.layout.custom_actionbar_multiple_media_selection, null);
			ActionBar.LayoutParams params = new ActionBar.LayoutParams(
					// Center the textview in the ActionBar !
					ActionBar.LayoutParams.WRAP_CONTENT,
					ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

			tvTitleActionBar = (TextView) viewActionBar.findViewById(R.id.tvTitleActionBar);
			ivMediaCapture = (ImageView) viewActionBar.findViewById(R.id.ivMediaCapture);
			// buttonActionBarNext = (Button)
			// viewActionBar.findViewById(R.id.btnActionBarNext);

			getActionBar().setCustomView(viewActionBar, params);
			getActionBar().setDisplayShowCustomEnabled(true);
			getActionBar().setDisplayShowTitleEnabled(false);
			getActionBar().setDisplayHomeAsUpEnabled(false);
			getActionBar().setHomeButtonEnabled(false);

			// To set the initial title of the Action Bar and Capture image which is
			// either picture/video
			setTitleImage(mediaType, tvTitleActionBar, ivMediaCapture);
		} 
		catch (NotFoundException e) {
			e.printStackTrace();
		}
	}
		
	// This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			gridView = (GridView) findViewById(R.id.mediapicker_grid);
			listView = (ListView) findViewById(R.id.mediapicker_list);
			viewFlipper = (ViewFlipper) findViewById(R.id.mediapicker_view);
			noResultsView = (TextView) findViewById(R.id.mediapicker_no_galleries);
			cancelButton = (Button) findViewById(R.id.mediapicker_button_cancel);
			selectButton = (Button) findViewById(R.id.mediapicker_button_select);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);
			isOnActivityResultCalled = true;
			
			if(data.getData()!=null){
				//  If media(image/video) is selected
				if((requestCode == ConfigConstants.ResultCodes.CAPTURE_IMAGE_RESULT_CODE || requestCode == ConfigConstants.ResultCodes.CAPTURE_VIDEO_RESULT_CODE) && resultCode == RESULT_OK){
					if (currentScreen == 0) {
						new GalleryLoader(MediaPickerActivity.this, getString(R.string.mediapicker_loading_galleries)).execute();
						isOnActivityResultCalled = false;
				    }
				}
			}
			/*else {
				Bitmap bitmap = (Bitmap)data.getExtras().get("data");
				String isImageSaved = saveToExternalSorage(bitmap);
				if(!isImageSaved.isEmpty()) {
					MediaScannerConnection.scanFile(this, new String[] { isImageSaved}, null, new MediaScannerConnection.OnScanCompletedListener() {
						public void onScanCompleted(String path, Uri uri) {
							//Log.i("ExternalStorage", "Scanned " + path + ":");
							//Log.i("ExternalStorage", "-> uri=" + uri);
							if (currentScreen == 0) {
								new GalleryLoader(MediaPickerActivity.this, getString(R.string.mediapicker_loading_galleries)).execute();
								isOnActivityResultCalled = false;
							}
						}
					});
				}
			}*/
			else {
				Bitmap bitmap = (Bitmap) data.getExtras().get("data");
				String isImageSaved = saveToExternalSorage(bitmap);
				if (!isImageSaved.isEmpty()) {
					MediaScannerConnection.scanFile(this, new String[] { isImageSaved }, null,
							new MediaScannerConnection.OnScanCompletedListener() {
								public void onScanCompleted(String path, Uri uri) {
									if (currentScreen == 0) {
										new GalleryLoader(MediaPickerActivity.this,
												getString(R.string.mediapicker_loading_galleries)).execute();
										isOnActivityResultCalled = false;
									}
								}
							});
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  To store image into the external storage i.e. onto the SD Card
	private String saveToExternalSorage(Bitmap bitmapImage) {
		File myDir = null;
		try {
			String root = Environment.getExternalStorageDirectory().toString();
			myDir = new File(root + "/American Bars");
			File file = null;
			String fileName = "image_" + String.valueOf(System.currentTimeMillis())+".jpg";
			
			if(!myDir.exists()){
				myDir.mkdirs();
			}
   
			file = new File (myDir, fileName);
			if (file.exists ()) file.delete ();
			
			//  Now, first of all check whether logoImage directory exists or not if not then create it
			//  Create imageDir
			File mypath = new File(myDir, fileName);

			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(mypath);
				//  Use the compress method on the BitMap object to write image to the OutputStream
				bitmapImage.compress(Bitmap.CompressFormat.PNG, 0, fos);
				fos.close();	
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return myDir.getAbsolutePath();
    }

	public View.OnClickListener cancelListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			try {
				setResult(RESULT_CANCELED);
				finish();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public View.OnClickListener selectListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			try {
				Intent intent = new Intent();
				ArrayList<Media> selected = getSelected();
				intent.putParcelableArrayListExtra("selectedMediaList", selected);
				setResult(ConfigConstants.ResultCodes.PICK_MEDIA_RESULT_CODE, intent);
				finish();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public AdapterView.OnItemClickListener galleryListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			try {
				selectedGallery = (Gallery) listView.getItemAtPosition(position);
				updateTitle();
				imagesAdapter = new MediaAdapter(activity, R.layout.activity_media_picker_grid_item, selectedGallery);
				gridView.setAdapter(imagesAdapter);
				imagesAdapter.notifyDataSetChanged();
				viewFlipper.showNext();
				activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				currentScreen = 1;
				// Here, hide the action bar image capture icon
				ivMediaCapture.setVisibility(View.GONE);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public AdapterView.OnItemClickListener mediaListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
			try {
				if (selectedGallery.isSelected(position)) {
					selectedGallery.deselect(position);
				} 
				else {
					int selectedCount = getSelectedCount();
					if (maxSelected != 0 && selectedCount == maxSelected) {
						Toast.makeText(activity, getString(R.string.mediapicker_max_selected, selectedCount), Toast.LENGTH_LONG).show();
					} 
					else {
						selectedGallery.select(position, imagesAdapter.getItem(position).getId(),imagesAdapter.getItem(position).getType(),
								imagesAdapter.getItem(position).getLocation(),imagesAdapter.getItem(position).getMediaPath(), 
								imagesAdapter.getItem(position).getMediaName(),imagesAdapter.getItem(position).getMediaExtension(), 
								imagesAdapter.getItem(position).getMediaSize(),imagesAdapter.getItem(position).getMediaType());
					}
				}
				updateTitle();
				imagesAdapter.notifyDataSetChanged();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		try {
			switch (item.getItemId()) {
			case android.R.id.home:
				if (currentScreen == 1) {
					viewFlipper.showPrevious();
					activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
					currentScreen = 0;
					// Here, show the action bar image capture icon
					ivMediaCapture.setVisibility(View.VISIBLE);
				}
				break;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void postGalleriesLoad(Gallery[] results) {
		try {
			this.results = results;
			if (results.length > 0) {
				galleryAdapter = new GalleryAdapter(this, R.layout.activity_media_picker_gallery, results);
				listView.setAdapter(galleryAdapter);
			} 
			else {
				noResultsView.setVisibility(View.VISIBLE);
				selectButton.setVisibility(View.INVISIBLE);
				cancelButton.setVisibility(View.INVISIBLE);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateTitle() {
		try {
			String title;
			if (maxSelected == 0) {
				title = getString(R.string.mediapicker_title_num, selectedGallery.getName(), this.getSelectedCount());
			} 
			else {
				title = getString(R.string.mediapicker_title_num_max, selectedGallery.getName(), this.getSelectedCount(), maxSelected);
			}
			tvTitleActionBar.setText(title);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setTitleImage(String mediaType, TextView textView, ImageView imageView) {
		try {
			if (mediaType.equals(ConfigConstants.Constants.CONSTANT_MEDIA_PICTURE)) {
				textView.setText("Choose Photos".toUpperCase(Locale.getDefault()));
				ivMediaCapture.setImageResource(R.drawable.media_picture);
			} 
			else {
				textView.setText("Choose Videos".toUpperCase(Locale.getDefault()));
				ivMediaCapture.setImageResource(R.drawable.media_video);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void postImagesLoad(ArrayList<Integer> results) {
		try {
			updateTitle();
			imagesAdapter = new MediaAdapter(this, R.layout.activity_media_picker_grid_item, selectedGallery);
			gridView.setAdapter(imagesAdapter);
			imagesAdapter.notifyDataSetChanged();
			viewFlipper.showNext();
			activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			currentScreen = 1;
			// Here, hide the action bar image capture icon
			ivMediaCapture.setVisibility(View.GONE);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getSelectedCount() {
		int selected = 0;
		try {
			for (Gallery g : results) {
				selected += g.getSelectedCount();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return selected;
	}

	public ArrayList<Media> getSelected() {
		ArrayList<Media> selected = new ArrayList<Media>();
		try {
			for (Gallery g : results) {
				selected.addAll(g.getSelected());
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return selected;
	}

	private class GalleryLoader extends AsyncTask<Void, Void, Gallery[]> {
		Activity activity;
		ProgressDialogFragment dialog;
		String loadingText;

		@SuppressWarnings("unused")
		public GalleryLoader() {
			super();
		}

		public GalleryLoader(Activity activity, String loadingText) {
			super();
			this.activity = activity;
			this.loadingText = loadingText;
			//Log.i("GalleryLoader", "GalleryLoader");
		}

		@Override
		protected Gallery[] doInBackground(Void... arg) {
			return GalleryAdapter.loadData(activity, ignoreIds, type, mediaType);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialogFragment(loadingText);
			dialog.show(getSupportFragmentManager(), "DIALOG_LOADING_GALLERY");
		}

		@Override
		protected void onPostExecute(Gallery[] results) {
			super.onPostExecute(results);
			ProgressDialogFragment d = (ProgressDialogFragment) getSupportFragmentManager().findFragmentByTag("DIALOG_LOADING_GALLERY");
			if (d != null) {
				d.dismiss();
			}
			postGalleriesLoad(results);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
				&& keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			onBackPressed();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		if (currentScreen == 1) {
			viewFlipper.showPrevious();
			activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			currentScreen = 0;
			ivMediaCapture.setVisibility(View.VISIBLE);
			// buttonActionBarNext.setVisibility(View.GONE);
		} 
		else {
			setResult(RESULT_CANCELED);
			finish();
		}
	}
}

class ProgressDialogFragment extends DialogFragment {
	private String message;

	public ProgressDialogFragment(String string) {
		this.message = string;
	}

	@Override
	public ProgressDialog onCreateDialog(Bundle savedInstanceState) {
		ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setCancelable(false);
		dialog.setIndeterminate(true);
		dialog.setMessage(message);
		return dialog;
	}
}

class MediaScannerWrapper implements MediaScannerConnection.MediaScannerConnectionClient {
    private MediaScannerConnection mConnection;
    private String mPath;
    private String mMimeType;

    // filePath - where to scan; 
    // mime type of media to scan i.e. "image/jpeg". 
    // use "*/*" for any media
    public MediaScannerWrapper(Context ctx, String filePath, String mime){
        mPath = filePath;
        mMimeType = mime;
        mConnection = new MediaScannerConnection(ctx, this);
    }

    // do the scanning
    public void scan() {
        mConnection.connect();
        //Log.e("Scan is started.","Scan is started.");
    }

    // start the scan when scanner is ready
    public void onMediaScannerConnected() {
        mConnection.scanFile(mPath, mMimeType);
        //Log.e("MediaScannerWrapper", "media file scanned: " + mPath);
    }

    public void onScanCompleted(String path, Uri uri) {
        // when scan is completes, update media file tags
    	//Log.e("Scan is completed.","Scan is completed.");
    }
}