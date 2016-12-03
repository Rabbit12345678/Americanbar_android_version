package com.spaculus.americanbars;

import com.spaculus.helpers.ConfigConstants;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class QuizActivity extends BaseActivity implements OnClickListener {
	private TextView tvTotalScore, tvTotalTime;
	private ImageView[] ivShareArray = null;
	private Button buttonTryAgain;

	/* To get the value of a total correct answers and total time taken */
	private String totalCorrectAnswers = "", totalTimeTaken = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_quiz);

			// Create Action Bar
			createActionBar(ConfigConstants.Titles.TITLE_QUIZ, R.layout.custom_actionbar, QuizActivity.this, true);
			// boolean isBackArrowVisible, boolean isTitleVisible, boolean
			// isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);

			// Mapping of all the views
			mappedAllViews();

			/*
			 * To get the value of a total correct answers and total time taken
			 */
			Bundle b = getIntent().getExtras();
			totalCorrectAnswers = b.getString(ConfigConstants.Keys.KEY_TOTAL_CORRECT_ANSWERS);
			totalTimeTaken = b.getString(ConfigConstants.Keys.KEY_TOTAL_TIME);

			/* Display total correct answers and total time taken */
			tvTotalScore.setText(totalCorrectAnswers + "/" + ConfigConstants.Constants.CONSTANT_TOTAL_QUESTIONS);
			tvTotalTime.setText(totalTimeTaken);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			tvTotalScore = (TextView) findViewById(R.id.tvTotalScore);
			tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);
			ivShareArray = new ImageView[4];
			ivShareArray[0] = (ImageView) findViewById(R.id.ivFacebookShare);
			ivShareArray[1] = (ImageView) findViewById(R.id.ivTwitterShare);
			ivShareArray[2] = (ImageView) findViewById(R.id.ivGooglePlusShare);
			ivShareArray[3] = (ImageView) findViewById(R.id.ivPinterestShare);
			buttonTryAgain = (Button) findViewById(R.id.buttonTryAgain);
			
			/* All click events */
			for(int i=0; i<ivShareArray.length; i++) {
				ivShareArray[i].setOnClickListener(this);
			}
			buttonTryAgain.setOnClickListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View view) {
		try {
			switch (view.getId()) {
			case R.id.ivFacebookShare:
				socialShareLink("com.facebook.katana", ConfigConstants.Constants.CONSTANT_FACEBOOK);
				break;
			case R.id.ivTwitterShare:
				socialShareLink("com.twitter.android", ConfigConstants.Constants.CONSTANT_TWITTER);
				break;
			case R.id.ivGooglePlusShare:
				socialShareLink("com.google.android.apps.plus", ConfigConstants.Constants.CONSTANT_GOOGLE_PLUS);
				break;
			case R.id.ivPinterestShare:
				socialShareLink("com.pinterest", ConfigConstants.Constants.CONSTANT_PINTEREST);
				break;
			case R.id.buttonTryAgain:
				/* Simple close and finish this activity */
				finish();
				onDestroy();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* A method is used to share a link. */
	private void socialShareLink(String packageName, String constantOption) {
		//  Now, call a method to share a link
		shareTextLink(packageName, ConfigConstants.SharingURLs.shareTriviaGameURL, constantOption);
	}
}
