package com.spaculus.americanbars;

import java.util.ArrayList;

import com.spaculus.beans.TriviaGame;
import com.spaculus.helpers.ConfigConstants;

import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BarTriviaGameActivity extends BaseActivity implements OnClickListener {
	private TextView tvQuestionNumber, tvTimer;
	private TextView tvQuestion;
	private Button[] buttonAnswerOptionArray = null;
	private Button buttonQuitGame, buttonStartNewGame, buttonNext;
	private ProgressBar progressBar;
	private ArrayList<TriviaGame> triviaGameList = null;

	/* To manage the Question-Answers Sequence */
	private int position = 0;

	// private String CONSTANT_NEXT = "Next";
	private String CONSTANT_FINISH = "Finish";

	/* For the Timer functionality */
	private CountDownTimer objCountDownTimer = null;
	private final long startTime = 20 * 1000;
	private final long interval = 1 * 1000;
	private final long totalTimeDuration = 20;
	private int counter = ConfigConstants.Constants.CONSTANT_TOTAL_QUESTIONS;
	
	/* Right-Wrong Answers functionality */
	private int totalCorrectAnswers = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_bar_trivia_game);

			// Create Action Bar
			createActionBar(ConfigConstants.Constants.BAR_TRIVIA_GAME, R.layout.custom_actionbar, BarTriviaGameActivity.this, true);
			// boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			setActionBarFromChild(true, true, false, true, true);

			// Mapping of all the views
			mappedAllViews();

			/* To get the Trivia Game List */
			Bundle b = getIntent().getExtras();
			triviaGameList = b.getParcelableArrayList(ConfigConstants.Keys.KEY_TRIVIA_GAME_LIST);

			/* Set data initially */
			setDataInitially();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(objCountDownTimer != null) {
			objCountDownTimer.cancel();
		}
		Log.e("onPause called", "onPause called");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(objCountDownTimer != null) {
			objCountDownTimer.cancel();
		}
		Log.e("onDestroy called", "onDestroy called");
	}*/

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		/* Stop Timer */
		stopTimer();
		//Log.e("onBackPressed called", "onBackPressed called");
	}
	
	// This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			tvQuestionNumber = (TextView) findViewById(R.id.tvQuestionNumber);
			tvTimer = (TextView) findViewById(R.id.tvTimer);
			tvQuestion = (TextView) findViewById(R.id.tvQuestion);
			buttonAnswerOptionArray = new Button[4];
			buttonAnswerOptionArray[0] = (Button) findViewById(R.id.buttonAnserOption1);
			buttonAnswerOptionArray[1] = (Button) findViewById(R.id.buttonAnserOption2);
			buttonAnswerOptionArray[2] = (Button) findViewById(R.id.buttonAnserOption3);
			buttonAnswerOptionArray[3] = (Button) findViewById(R.id.buttonAnserOption4);
			buttonQuitGame = (Button) findViewById(R.id.buttonQuitGame);
			buttonStartNewGame = (Button) findViewById(R.id.buttonStartNewGame);
			buttonNext = (Button) findViewById(R.id.buttonNext);
			progressBar = (ProgressBar) findViewById(R.id.progressBar);

			// Initialize array list here.
			triviaGameList = new ArrayList<TriviaGame>();
			
			/* All click events */
			buttonNext.setOnClickListener(this);
			buttonStartNewGame.setOnClickListener(this);
			buttonQuitGame.setOnClickListener(this);
			
			for(int i=0; i<buttonAnswerOptionArray.length; i++) {
				buttonAnswerOptionArray[i].setOnClickListener(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View view) {
		try {
			switch (view.getId()) {
			case R.id.buttonNext:
				nextClick();
				break;
			case R.id.buttonStartNewGame:
				startNewGameClick();
				break;
			case R.id.buttonQuitGame:
				quitClick();
				break;
			case R.id.buttonAnserOption1:
				answerOptionClick(0);
				break;
			case R.id.buttonAnserOption2:
				answerOptionClick(1);
				break;
			case R.id.buttonAnserOption3:
				answerOptionClick(2);
				break;
			case R.id.buttonAnserOption4:
				answerOptionClick(3);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* A method is used to set the data initially. */
	private void setDataInitially() {
		try {
			progressBar.setVisibility(View.VISIBLE);
			buttonQuitGame.setEnabled(false);
			buttonStartNewGame.setEnabled(false);
			buttonNext.setEnabled(false);
			for(int i=0; i<buttonAnswerOptionArray.length; i++) {
				buttonAnswerOptionArray[i].setEnabled(true);
				buttonAnswerOptionArray[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_selector));
			}
			tvTimer.setText(String.valueOf(startTime / 1000));
			tvTimer.setText("" + counter);
			//Log.e("tvTimer text: ", ""+tvTimer.getText().toString().trim());
			/* Show Question Number */
			tvQuestionNumber.setText((position + 1) + "/" + ConfigConstants.Constants.CONSTANT_TOTAL_QUESTIONS);
			/* Set Finish text for the Next button */
			if ((position + 1) == ConfigConstants.Constants.CONSTANT_TOTAL_QUESTIONS) {
				buttonNext.setText(CONSTANT_FINISH);
			}
			/* Show Question */
			tvQuestion.setText(triviaGameList.get(position).getQuestion());
			/* Show Answers */
			for (int i = 0; i < 4; i++) {
				buttonAnswerOptionArray[i].setText(triviaGameList.get(position).getAnswerOptionsList().get(i));
				buttonAnswerOptionArray[i].setPadding(20, 10, 20, 10);
			}

			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					objCountDownTimer = new MyCountDownTimer(startTime, interval);
					objCountDownTimer.start();
					progressBar.setVisibility(View.GONE);
					buttonQuitGame.setEnabled(true);
					buttonStartNewGame.setEnabled(true);
					buttonNext.setEnabled(true);
				}
			}, 1000);
		} 
		catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

	public class MyCountDownTimer extends CountDownTimer {
		public MyCountDownTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		@Override
		public void onFinish() {
			try {
				position++;
				if(position == ConfigConstants.Constants.CONSTANT_TOTAL_QUESTIONS) {
					redirectQuizActivity();
				}
				else {
					counter = ConfigConstants.Constants.CONSTANT_TOTAL_QUESTIONS;
					/* Set data initially */
					setDataInitially();
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {
			try {
				counter -= 1;
				tvTimer.setText("" + counter);
				triviaGameList.get(position).setTimeTaken(totalTimeDuration - (millisUntilFinished / 1000)); 
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/* A method is used when next button is click. */
	private void nextClick() {
		try {
			/* Stop Timer */
			stopTimer();
			if (!buttonNext.getText().toString().trim().equals(CONSTANT_FINISH)) {
				position++;
				counter = ConfigConstants.Constants.CONSTANT_TOTAL_QUESTIONS;
				/* Set data initially */
				setDataInitially();
			}
			else {
				progressBar.setVisibility(View.GONE);
				buttonQuitGame.setEnabled(true);
				buttonStartNewGame.setEnabled(true);
				buttonNext.setEnabled(true);
				redirectQuizActivity();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* A method is used when start new game button is click. */
	private void startNewGameClick() {
		/* Stop Timer */
		stopTimer();
		/* Simply, close this activity. */
		finish();
		onDestroy();
	}
	
	/* A method is used when quit button is click. */
	private void quitClick() {
		/* Stop Timer */
		stopTimer();
		/* Redirect to the Home Page directly */
		redirectHomeActivity(BarTriviaGameActivity.this);
	}
	
	/* A method is when one of the answer options is clicked. */
	private void answerOptionClick(int selectedIndex) {
		try {
			String selectedAnswer = buttonAnswerOptionArray[selectedIndex].getText().toString().trim();
			String rightAnswer = buttonAnswerOptionArray[Integer.parseInt(triviaGameList.get(position).getAnswer())-1].getText().toString().trim(); 
			/* Means Correct Answer */
			if(selectedAnswer.equals(rightAnswer)) {
				buttonAnswerOptionArray[selectedIndex].setBackgroundColor(getResources().getColor(R.color.greenColor));
				totalCorrectAnswers++;
			}
			/* Means Wrong Answer */
			else {
				buttonAnswerOptionArray[selectedIndex].setBackgroundColor(getResources().getColor(R.color.redColor));
				for(int i=0; i<buttonAnswerOptionArray.length; i++) {
					if(buttonAnswerOptionArray[i].getText().toString().trim().equals(rightAnswer)) {
						buttonAnswerOptionArray[i].setBackgroundColor(getResources().getColor(R.color.greenColor));
						break;
					}
				}
			}
			for(int i=0; i<buttonAnswerOptionArray.length; i++) {
				buttonAnswerOptionArray[i].setEnabled(false);
			}
			/* Stop Timer */
			stopTimer();
		} 
		catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/* A method is used to stop a timer. */
	private void stopTimer() {
		if(objCountDownTimer != null) {
			objCountDownTimer.cancel();
		}
	}
	
	/* A method is used to redirect Quiz activity. */
	private void redirectQuizActivity() {
		/* Get total time */
		long totalTime = 0; // in milliseconds
		for(int i=0; i<triviaGameList.size(); i++) {
			totalTime += triviaGameList.get(i).getTimeTaken();
		}
		Intent intent = new Intent(BarTriviaGameActivity.this, QuizActivity.class);
		intent.putExtra(ConfigConstants.Keys.KEY_TOTAL_CORRECT_ANSWERS, ""+totalCorrectAnswers);
		intent.putExtra(ConfigConstants.Keys.KEY_TOTAL_TIME, ""+formatHHMMSS(totalTime));
		startActivity(intent);
		/* Now, close and finish this activity. */
		finish();
		onDestroy();
	}
	
	/* A method is used to convert seconds into the hh:mm:ss format. */
	private String formatHHMMSS(long secondsCount) {
		/* Calculate the seconds to display */
		int seconds = (int) (secondsCount %60);
		secondsCount -= seconds;
		/* Calculate the minutes to display */
	    long minutesCount = secondsCount / 60;
	    long minutes = minutesCount % 60;
	    minutesCount -= minutes;
	    /* Calculate the hours to display */
	    long hoursCount = minutesCount / 60;
	    Log.e("Original Value is: ", "" + hoursCount + ":" + minutes + ":" + seconds);
	    /*String hoursString = String.valueOf(hoursCount);
	    String minutesString = String.valueOf(minutes);
	    String secondsString = String.valueOf(seconds);*/
	    String hoursString = String.valueOf(hoursCount).length()==1 ? ("0"+String.valueOf(hoursCount)) : String.valueOf(hoursCount);
	    String minutesString = String.valueOf(minutes).length()==1 ? ("0"+String.valueOf(minutes)) : String.valueOf(minutes);
	    String secondsString = String.valueOf(seconds).length()==1 ? ("0"+String.valueOf(seconds)) : String.valueOf(seconds);
	    return hoursString+":"+minutesString+":"+secondsString;
	}
}
