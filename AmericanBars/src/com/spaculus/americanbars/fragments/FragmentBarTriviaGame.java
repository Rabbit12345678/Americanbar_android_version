package com.spaculus.americanbars.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.spaculus.americanbars.BarTriviaGameActivity;
import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.R;
import com.spaculus.beans.TriviaGame;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.ParseJsonObject;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.SessionManager;
import com.spaculus.helpers.Utils;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class FragmentBarTriviaGame extends Fragment {
	private View rootView = null;
	private ImageView ivBarTriviaGame;
	private Button buttonStartTrivia;
	private AsynTaskGetTriviaQuestionAnswersList objAsynTaskGetTriviaQuestionAnswersList = null;
	private ArrayList<TriviaGame> triviaGameList = null;
	
	/* For the Background Image */
	private String backgroundImage = "";
	
	//  To know whether user is redirected to this screen from navigation drawer or not
	//  So on the basis of it we can show the action bar fields
	boolean isRedirectedFromMainActivity = false;

	public FragmentBarTriviaGame(boolean flag) {
		this.isRedirectedFromMainActivity = flag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		try {
			rootView = inflater.inflate(R.layout.fragment_bar_trivia_game, container, false);
			
			//  For the Action Bar
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			if (isRedirectedFromMainActivity) {
				((BaseActivity) getActivity()).setActionBarFromChild(false, true, false, true, true);
			} else {
				((BaseActivity) getActivity()).setActionBarFromChild(true, true, false, true, true);
			}

			//  Mapping of all the views
			mappedAllViews();
			
			buttonStartTrivia.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), BarTriviaGameActivity.class);
					intent.putParcelableArrayListExtra(ConfigConstants.Keys.KEY_TRIVIA_GAME_LIST, triviaGameList);
					startActivity(intent);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootView;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//  Now, get the Trivia Question-Answers List here
		if(Utils.getInstance().isNetworkAvailable(getActivity())) {
			if (objAsynTaskGetTriviaQuestionAnswersList == null) {
				if (Utils.getInstance().isNetworkAvailable(getActivity())) {
					objAsynTaskGetTriviaQuestionAnswersList = new AsynTaskGetTriviaQuestionAnswersList();
					objAsynTaskGetTriviaQuestionAnswersList.execute();
				} else {
					Utils.getInstance().showToastNoInternetAvailable(getActivity());
				}
			}
		}
		else{
			Utils.getInstance().showToastNoInternetAvailable(getActivity());
		}
	}

	// This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			ivBarTriviaGame = (ImageView) rootView.findViewById(R.id.ivBarTriviaGame);
			buttonStartTrivia = (Button) rootView.findViewById(R.id.buttonStartTrivia);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* An AnyncTask to get the Trivia Question-Answers List. */
	public class AsynTaskGetTriviaQuestionAnswersList extends AsyncTask<String, Void, String> {
		private ProgressDialog pd = new ProgressDialog(getActivity());
		@Override
		protected void onPreExecute() {
			try {
				super.onPreExecute();
				this.pd.setMessage(ConfigConstants.Messages.loadingMessage);
				pd.setCanceledOnTouchOutside(false);
				pd.setCancelable(false);
				this.pd.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				ServiceHandler sh = new ServiceHandler();
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("user_id", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_USER_ID)));
		        nameValuePairs.add(new BasicNameValuePair("device_id", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_DEVICE_ID)));
		        nameValuePairs.add(new BasicNameValuePair("unique_code", SessionManager.getInstance(getActivity()).getData(SessionManager.KEY_UNIQUE_CODE)));
	        	// Making a request to URL and getting response
		        String responseString = sh.makeServiceCall(ConfigConstants.Urls.trivia, ServiceHandler.POST, nameValuePairs);
	        	Log.i("responseString",responseString);
				return responseString;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String response) {
			try {
				super.onPostExecute(response);
				JSONObject jsonObjParent = null;
				if (response != null) {
					jsonObjParent = new JSONObject(response);
					String status = Utils.getInstance().isTagExists(jsonObjParent, "status");
					if (status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)) {
						if (jsonObjParent.has("result")) {
							JSONArray jsonArrayResult = jsonObjParent.getJSONArray("result");
							if (jsonArrayResult != null) {
								if (jsonArrayResult.length() > 0) {
									//  Initialize array list here.
									triviaGameList = new ArrayList<TriviaGame>();
									for (int i = 0; i < jsonArrayResult.length(); i++) {
										JSONObject jsonObj = jsonArrayResult.getJSONObject(i);
										triviaGameList.add(new ParseJsonObject().addTriviaGameObject(jsonObj));
									}
									Log.i("Trivia List Size is:", ""+triviaGameList.size());
								} 
								else {
									Utils.getInstance().displayToast(getActivity(), "Trivia Game Question Answers list is not available. Please try again.");
								}
							} 
							else {
								Utils.getInstance().displayToast(getActivity(), "Trivia Game Question Answers list is not available. Please try again.");
							}
						} 
						else {
							Utils.getInstance().displayToast(getActivity(), "Trivia Game Question Answers list is not available. Please try again.");
						}
						
						/* Now, get the background image data */
						if(jsonObjParent.has("imagedate")) {
							JSONObject jsonObjectImage = jsonObjParent.getJSONObject("imagedate");
							if(jsonObjectImage != null) {
								//backgroundImage = Utils.getInstance().isTagExists(jsonObjectImage, "trivia");
								backgroundImage = Utils.getInstance().isTagExists(jsonObjectImage, "find_trivia_app");
							}
						}
						
						/* Now, set the background image */
						if(!backgroundImage.isEmpty()) {
							/* Get singleton instance of ImageLoader */
							ImageLoader imageLoader = ImageLoader.getInstance();
							/* Initialize ImageLoader with configuration. Do it once. */
							imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
							imageLoader.displayImage(ConfigConstants.ImageUrls.bar_pages_banner+backgroundImage, ivBarTriviaGame);
						}
						else {
							ivBarTriviaGame.setImageResource(R.drawable.gallery_big_place_holder_01);
						}
					} 
					else {
						Utils.getInstance().displayToast(getActivity(), "Trivia Game Question Answers list is not available. Please try again.");
					}
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			objAsynTaskGetTriviaQuestionAnswersList = null;
			if (this.pd.isShowing()) {
				this.pd.dismiss();
			}
		}
	}
}
