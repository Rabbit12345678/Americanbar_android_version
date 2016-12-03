package com.spaculus.americanbars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import com.spaculus.helpers.ConfigConstants;

public class AlphaViewActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.activity_alpha_view);
			getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			
			//  Get the Search Bar Text data here.
			Bundle bundle = getIntent().getExtras();
			String searchText = bundle.getString(ConfigConstants.Keys.KEY_SEARCH_TEXT);

			Intent intent = new Intent(AlphaViewActivity.this, BarSearchAutoActivity.class);
			intent.putExtra(ConfigConstants.Keys.KEY_SEARCH_TEXT, searchText);
			startActivityForResult(intent, ConfigConstants.ResultCodes.REQUEST_CODE_BAR_SEARCH);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);
			
			if(resultCode!=Activity.RESULT_CANCELED){
				if(requestCode==ConfigConstants.ResultCodes.REQUEST_CODE_BAR_SEARCH){
					
					//  Get the Search Bar Text data here.
					Bundle bundle = data.getExtras();
					String searchText = bundle.getString(ConfigConstants.Keys.KEY_SEARCH_TEXT);
					
					Intent intent = new Intent();
					intent.putExtra(ConfigConstants.Keys.KEY_SEARCH_TEXT, searchText);
			        setResult(ConfigConstants.ResultCodes.REQUEST_CODE_APLHA_VIEW,intent);
					finish();
					onDestroy();
				}
			}
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
