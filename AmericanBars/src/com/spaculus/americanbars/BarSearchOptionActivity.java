package com.spaculus.americanbars;

import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

public class BarSearchOptionActivity extends BaseActivity {

	private Button btnSearchAroundMe, btnSearchAnother, btnSearchHappy,
			btnSearchAnotherLocation;
	private ScrollView scrollView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_bar_search_option);
			createActionBar(ConfigConstants.Constants.FIND_BAR,
					R.layout.custom_actionbar, BarSearchOptionActivity.this,
					true);
			setActionBarFromChild(true, true, false, true, true);
			mappedAllViews();
			btnSearchAroundMe.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					redirectBarSearchAroundMeActivity(BarSearchOptionActivity.this, ConfigConstants.Constants.CONSTANT_FIRST, "");
					/*Intent intent = new Intent(BarSearchOptionActivity.this,BarSearchAroundMeActivity.class);
					intent.putExtra("flag", "first");
					startActivity(intent);*/
				} 
			});
			btnSearchAnother.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(BarSearchOptionActivity.this,
							BarSearchActivity.class);
					startActivity(intent);
				}
			});
			btnSearchHappy.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					/*Intent intent = new Intent(BarSearchOptionActivity.this, BarSearchHappyHoursGPSActivity.class);
					startActivity(intent);*/
					redirectBarSearchAroundMeActivity(BarSearchOptionActivity.this, ConfigConstants.Constants.CONSTANT_SECOND, Utils.getInstance().getCurrentDay());
				}
			});
			btnSearchAnotherLocation
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(
									BarSearchOptionActivity.this,
									BarSearchHappyHoursActivity.class);
							startActivity(intent);
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
			btnSearchAroundMe = (Button) findViewById(R.id.btnSearchAroundMe);
			btnSearchAnother = (Button) findViewById(R.id.btnSearchAnother);
			btnSearchHappy = (Button) findViewById(R.id.btnSearchHappy);
			btnSearchAnotherLocation = (Button) findViewById(R.id.btnSearchAnotherLocation);
			scrollView = (ScrollView) findViewById(R.id.scrollView);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode != Activity.RESULT_CANCELED) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
