package com.spaculus.americanbars;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.imagezoom.ImageAttacher;
import com.imagezoom.ImageAttacher.OnMatrixChangedListener;
import com.imagezoom.ImageAttacher.OnPhotoTapListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.spaculus.helpers.ConfigConstants;

public class ZoomImageViewURL extends BaseActivity {

	private ImageView ivDisplayImage;
	
	//  URL of the Image
	private String url = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			// Remove title bar
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);

			setContentView(R.layout.activity_zoom_imageview_url);
			
			ivDisplayImage = (ImageView) findViewById(R.id.ivDisplayImage);

			// Get the Screen name here
			Bundle b = getIntent().getExtras();
			url = b.getString(ConfigConstants.Keys.KEY_URL);
			//Log.i("url", url);

			// Get singleton instance of ImageLoader
			ImageLoader imageLoader = ImageLoader.getInstance();
			// Initialize ImageLoader with configuration. Do it once.
			imageLoader.init(ImageLoaderConfiguration.createDefault(this));
			// Load and display image asynchronously

			DisplayImageOptions options = new DisplayImageOptions.Builder()
					// .showStubImage(R.drawable.ic_launcher) //this is the image
					// that will be displayed if download fails
					.resetViewBeforeLoading()
					// .cacheInMemory()
					.bitmapConfig(Bitmap.Config.RGB_565)
					.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
					.showStubImage(R.drawable.gallery_big_place_holder).build();

			imageLoader.displayImage(url, ivDisplayImage, options);
			
			//  For the image zooming functionality
			usingSimpleImage(ivDisplayImage);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void usingSimpleImage(ImageView imageView) {
		try {
			ImageAttacher mAttacher = new ImageAttacher(imageView);
			ImageAttacher.MAX_ZOOM = 6.0f; // Double the current Size
			ImageAttacher.MIN_ZOOM = 1.0f; // Half the current Size
			MatrixChangeListener mMaListener = new MatrixChangeListener();
			mAttacher.setOnMatrixChangeListener(mMaListener);
			PhotoTapListener mPhotoTap = new PhotoTapListener();
			mAttacher.setOnPhotoTapListener(mPhotoTap);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class PhotoTapListener implements OnPhotoTapListener {

		@Override
		public void onPhotoTap(View arg0, float arg1, float arg2) {
			// TODO Auto-generated method stub
		}
	}

	private class MatrixChangeListener implements OnMatrixChangedListener {
		@Override
		public void onMatrixChanged(RectF rect) {
		}
	}
}
