package com.spaculus.americanbars.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.spaculus.americanbars.BaseActivity;
import com.spaculus.americanbars.MainActivity;
import com.spaculus.americanbars.R;
import com.spaculus.helpers.ConfigConstants;
import com.spaculus.helpers.ServiceHandler;
import com.spaculus.helpers.Utils;
import com.spaculus.helpers.Validation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import br.com.sapereaude.maskedEditText.MaskedEditText;

public class FragmentSignUp extends Fragment implements OnFocusChangeListener{
	
	private View rootView = null;
	private RelativeLayout relativeLayoutSignUp;
	private EditText etBarFlyNickname=null, etFirstName, etLastName, etEmail, etPassword, etConfirmPassword;
	private MaskedEditText etMobileNumber;
	private Button buttonBirthday, buttonSignUp;
	private ImageView ivMobileNumberInfo, ivBirthdayInfo;
	private RadioButton radioButtonMale, radioButtonFemale;
	@SuppressWarnings("unused")
	private RadioGroup rgGender;
	private CheckBox checkBoxInformation;
	private ScrollView scrollView;
	@SuppressWarnings("unused")
	private LinearLayout linearLayoutBirthday;
	
	//  For the Birthday functionality
	private DatePickerDialog myDatePickerDialog = null;
	private Calendar myCalendar = null;
	String checkDateString = "";
	
	//  AsyncTask Sign Up message
	private String asyncTaskSignUpMessage = "Creating account....";
	
	/*For the Mobile Information Pop-Up*/
	private LinearLayout linearLayoutTooltip;
	
	//  To know whether user is redirected to this screen from navigation drawer or not
	//  So on the basis of it we can show the action bar fields
	boolean isRedirectedFromMainActivity = false;
	
	/* For the Validations */
	private boolean isValid = true; //  Just to show red box for all the invalid fields but message will be shown only for the first invalid field.
	private String toastMessage = "";
	
	public FragmentSignUp(boolean flag) {
		this.isRedirectedFromMainActivity = flag;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
			rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
			
			//  For the Action Bar
			//boolean isBackArrowVisible, boolean isTitleVisible, boolean isLogoVisible, boolean isMenuIconVisible
			if(isRedirectedFromMainActivity) {
				((BaseActivity)getActivity()).setActionBarFromChild(false, true, false, false, false);	
			}
			else {
				((BaseActivity)getActivity()).setActionBarFromChild(true, true, false, false, false);
			}
			
			// Mapping of all the views
			mappedAllViews();
			
			//  To hide the soft key board on the click of anywhere onto the screen 
			relativeLayoutSignUp.setOnTouchListener(new OnTouchListener() {
					
					@SuppressLint("ClickableViewAccessibility") 
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						
						if(v==relativeLayoutSignUp){
							hideSoftKeyboard();
							//hideToolTipLayout();
							return true;
						}
						return false;
					}
			});
				
			//  Show Mobile information Tooltip
			ivMobileNumberInfo.setOnClickListener(new OnClickListener() {
				@SuppressLint("NewApi") 
				@Override
				public void onClick(View view) {
					try {
						//((BaseActivity) getActivity()).showToolTip(view, ConfigConstants.getInstance().mobileNumberToolTipText);
						if(linearLayoutTooltip.getVisibility()==View.VISIBLE){
							linearLayoutTooltip.setVisibility(View.GONE);
						}
						else {
							linearLayoutTooltip.setVisibility(View.VISIBLE);
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			/* All the foucs changes listeners */
			etEmail.setOnFocusChangeListener(this);
			etPassword.setOnFocusChangeListener(this);
			etConfirmPassword.setOnFocusChangeListener(this);
			
			//  Birthday
			ivBirthdayInfo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						myDatePickerDialog = new DatePickerDialog(getActivity(), objDatePickerDialog, myCalendar.get(Calendar.YEAR), 
								myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
						 
						myDatePickerDialog.show();
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  Sign Up button click event
			buttonSignUp.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						//  First, hide the soft key board.
						hideSoftKeyboard();
						
						//  Now, check whether user has checked the check box or not
						String isCheckBoxChecked = "";
						if(checkBoxInformation.isChecked()) {
							isCheckBoxChecked = ConfigConstants.Constants.CONSTANT_YES;
						}
						else {
							isCheckBoxChecked = ConfigConstants.Constants.CONSTANT_NO;
						}
						
						boolean isAnyRadiobuttonChecked=false;
						if(radioButtonMale.isChecked() || radioButtonFemale.isChecked()){
							isAnyRadiobuttonChecked=true;
						}
						else{
							isAnyRadiobuttonChecked=false;
						}
						//  Now, first of all check all the validations
						String validationResponse  = signUpValidation(etBarFlyNickname.getText().toString().trim(), etFirstName.getText().toString().trim(), etLastName.getText().toString().trim(), 
								etEmail.getText().toString().trim(), etPassword.getText().toString().trim(), 
								etConfirmPassword.getText().toString().trim(),buttonBirthday.getText().toString().trim(),
								isAnyRadiobuttonChecked,isCheckBoxChecked,
								getResources().getString(R.string.txt_birthday));
						
						//  Means all the fields are properly entered
						if(validationResponse.isEmpty()){
							
							//  Called the AsyncTask to send the email
							if(Utils.getInstance() .isNetworkAvailable(getActivity())) {		
								new AsynTaskSignUp().execute();
							}
							else{
								Utils.getInstance().showToastNoInternetAvailable(getActivity());
							}
						}
						else {
							//  Show respective validation message
							//Toast.makeText(getActivity(), validationResponse, Toast.LENGTH_SHORT).show();
							Utils.toastLong(getActivity(), validationResponse);
							/*
							 * Now, here clear the isValid field's value and the
							 * toastMessage's text
							 */
							isValid = true;
							toastMessage = "";
						}
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
        return rootView;
    }
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		try {
			super.onConfigurationChanged(newConfig);
			scrollView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_login));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//  This method is used to do the mapping of all the views.
	private void mappedAllViews() {
		try {
			relativeLayoutSignUp = (RelativeLayout)rootView.findViewById(R.id.relativeLayoutSignUp);
			
			etBarFlyNickname = (EditText)rootView.findViewById(R.id.etBarFlyNickname);
			etFirstName = (EditText)rootView.findViewById(R.id.etFirstName);
			etLastName = (EditText)rootView.findViewById(R.id.etLastName);
			etEmail = (EditText)rootView.findViewById(R.id.etEmail);
			etMobileNumber = (MaskedEditText)rootView.findViewById(R.id.etMobileNumber);
			etPassword = (EditText)rootView.findViewById(R.id.etPassword);
			etConfirmPassword = (EditText)rootView.findViewById(R.id.etConfirmPassword);
			
			buttonBirthday = (Button)rootView.findViewById(R.id.btnBirthday);
			buttonSignUp = (Button)rootView.findViewById(R.id.btnSignUp);
			
			ivMobileNumberInfo = (ImageView)rootView.findViewById(R.id.ivMobileNumberInfo);
			ivBirthdayInfo = (ImageView)rootView.findViewById(R.id.ivBirthdayInfo);
			
			radioButtonMale = (RadioButton)rootView.findViewById(R.id.radioButtonMale);
			radioButtonFemale = (RadioButton)rootView.findViewById(R.id.radioButtonFemale);
			rgGender = (RadioGroup)rootView.findViewById(R.id.radioGroupGender);
			
			checkBoxInformation = (CheckBox)rootView.findViewById(R.id.checkBoxInformation);
			linearLayoutTooltip = (LinearLayout)rootView.findViewById(R.id.linearLayoutTooltip);
			scrollView = (ScrollView)rootView.findViewById(R.id.scrollView);
			linearLayoutBirthday = (LinearLayout)rootView.findViewById(R.id.linearLayoutBirthday);
			
			//  For the Birthday (Set Today's date here)
			myCalendar = Calendar.getInstance();
			//buttonBirthday.setText(Utils.getInstance().updateDate(myCalendar));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  To hide the soft key board on the click of Layout
	private void hideSoftKeyboard() {
		try {
			////Log.i("Method hideSoftKeyboard","Method hideSoftKeyboard");
			InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etBarFlyNickname.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etFirstName.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etLastName.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etMobileNumber.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etConfirmPassword.getWindowToken(), 0);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//  To hide the tool tip layout
	@SuppressWarnings("unused")
	private void hideToolTipLayout() {
		if(linearLayoutTooltip.getVisibility()==View.VISIBLE){
			linearLayoutTooltip.setVisibility(View.GONE);
		}
	}
	
    //  For the Date Picker Dialog
    DatePickerDialog.OnDateSetListener objDatePickerDialog = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                int dayOfMonth) {
            try {
				//  Set the values because for the date comparison
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				myCalendar.set(Calendar.HOUR_OF_DAY, 0);
				myCalendar.set(Calendar.MINUTE, 0);
				myCalendar.set(Calendar.SECOND, 0);
				myCalendar.set(Calendar.MILLISECOND, 0);
				 
				//  Check Date Validity
				checkDateString = Utils.getInstance().isFutureDate(myCalendar);
				
				if(checkDateString.equals(ConfigConstants.Constants.CONSTANT_EQUAL)){            	
					//  Update the date
					buttonBirthday.setText(Utils.getInstance().updateDate(myCalendar));
				}
				else if(checkDateString.equals(ConfigConstants.Constants.CONSTANT_BEFORE)){	
					//  Update the date
					buttonBirthday.setText(Utils.getInstance().updateDate(myCalendar));
				}
				else if(checkDateString.equals(ConfigConstants.Constants.CONSTANT_AFTER)){
					//Toast.makeText(getActivity(), "Future Date is not allowed.", Toast.LENGTH_SHORT).show();
					Utils.toastLong(getActivity(), "Future Date is not allowed.");
				    //  Then show the current date again (i.e we have to show the current time also again)
				    myCalendar = Calendar.getInstance();
					buttonBirthday.setText(Utils.getInstance().updateDate(myCalendar));
				}
			} 
            catch (Exception e) {
				e.printStackTrace();
			}
        }
    };
    
	//  AsyncTask to do Sign Up
	public class AsynTaskSignUp extends AsyncTask<Void, Void, Void> {
		
    	private ProgressDialog pd = new ProgressDialog(getActivity());
    	private String responseString = "";
    	private String status = "";
    	//private String user_id = "", unique_code = "", imageName = "", name = "";
    	
    	@Override
		protected void onPreExecute() {
			try {
				super.onPreExecute();
				this.pd.setMessage(asyncTaskSignUpMessage);
				pd.setCanceledOnTouchOutside(false); 
				pd.setCancelable(false);
				this.pd.show();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}	
    	
		@SuppressLint("DefaultLocale") 
		@Override
		protected Void doInBackground(Void... params) {
	        try {
	        	ServiceHandler sh = new ServiceHandler();
	        	//  For Date
	        	String dateString=buttonBirthday.getText().toString().trim();
	        	String[] dateArray = dateString.split("/");
	        	String month = dateArray[0];
	        	String day = dateArray[1];
	        	String year = dateArray[2];
	        	
	        	//  For gender
	        	String gender = "";
	        	if(radioButtonMale.isChecked()) {
	        		gender = "male";
	        	}
	        	else {
	        		gender = "female";
	        	}
	        	
	        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        	nameValuePairs.add(new BasicNameValuePair("email", etEmail.getText().toString().trim()));
	        	nameValuePairs.add(new BasicNameValuePair("first_name", etFirstName.getText().toString().trim()));
	        	nameValuePairs.add(new BasicNameValuePair("last_name", etLastName.getText().toString().trim()));
	        	nameValuePairs.add(new BasicNameValuePair("nick_name", etBarFlyNickname.getText().toString().trim()));
	        	nameValuePairs.add(new BasicNameValuePair("password", etPassword.getText().toString().trim()));
	        	nameValuePairs.add(new BasicNameValuePair("mobile_no", etMobileNumber.getText().toString().trim()));
	        	nameValuePairs.add(new BasicNameValuePair("month", month));
	        	nameValuePairs.add(new BasicNameValuePair("day", day));
	        	nameValuePairs.add(new BasicNameValuePair("year", year));
	        	nameValuePairs.add(new BasicNameValuePair("gender", gender));
		        
		       /* 1. email
		        2. first_name
		        3. last_name
		        4. nick_name
		        5. password
		        6. mobile_no
		        7. month
		        8. day
		        9. year
		        10. gender*/
		       
	        	// Making a request to url and getting response 
		        responseString = sh.makeServiceCall(ConfigConstants.Urls.user_register, ServiceHandler.POST, nameValuePairs);
	        	//Log.i("responseString",responseString);
	        	/*{"status":"age_failed"}*/
	        }
	        catch(Exception e) {
	        	e.printStackTrace();
	        	//Log.i("AsyncTask,Login,Excpetion is:",Utils.isStringNull(e.getMessage()));
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
					if (status.equals(ConfigConstants.Messages.RESPONSE_SUCCESS)) {
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setCancelable(true);
						//builder.setMessage("Your account has been successfully activated. Please check your email to confirm.");
						builder.setMessage("Thank you for registering with American Bars. In order to complete your registration, please check your email and click on the verification link.");
						builder.setInverseBackgroundForced(true);
						builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								if (isRedirectedFromMainActivity) {
									((MainActivity) getActivity()).reloadData();
								} else {
									getActivity().finish();
									onDestroy();
								}
							}
						});
						AlertDialog alert = builder.create();
						alert.show();
					}
					//  Already Registered
					else if(status.equals(ConfigConstants.Messages.RESPONSE_UNIQUE_FAILED)){
						//Toast.makeText(getActivity(), "Your entered Email ID is already registered with this application.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(getActivity(), "Your entered Email ID is already registered with this application.");
					}
					//  Age is not valid
					else if(status.equals(ConfigConstants.Messages.RESPONSE_AGE_FAILED)){
						//Toast.makeText(getActivity(), "Your age must be 21 years or above plese enter proper birth date.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(getActivity(),"Your age must be 21 years or above plese enter proper birth date.");
					}
					//  Means fail 
					else{
						//Toast.makeText(getActivity(), "Your registation with the application is not done. Please try again.", Toast.LENGTH_SHORT).show();
						Utils.toastLong(getActivity(), "Your registation with the application is not done. Please try again.");
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
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		try {
			switch (v.getId()) {
				case R.id.etEmail:
					emailValidation();
					break;
				case R.id.etPassword:
					passwordValidation();
					break;
				case R.id.etConfirmPassword:
					confirmPasswordValidation();
					break;
				default:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* A method is used for the email validation on focus change. */
	private void emailValidation() {
		String email = etEmail.getText().toString().trim();
		if(email.length() > 0) {
			if(!Validation.getInstance().isValid(email, Validation.CONSTANT_EMAIL)) {
				setErrorBackgroundSelector(etEmail);
			}
			else {
				setOriginalBackgroundColor(etEmail);
			}
		}
		else {
			setOriginalBackgroundColor(etEmail);
		}
	}
	
	/* A method is used for the password validation on focus change. */
	private void passwordValidation() {
		String password = etPassword.getText().toString().trim();
		if(password.length() > 0) {
			if(!Validation.getInstance().isValid(password, Validation.CONSTANT_PASSWORD)) {
				setErrorBackgroundSelector(etPassword);
			}
			else {
				setOriginalBackgroundColor(etPassword);
			}
		}
		else {
			setOriginalBackgroundColor(etPassword);
		}
	}
	
	/* A method is used for the confirm password validation on focus change. */
	private void confirmPasswordValidation() {
		String password = etPassword.getText().toString().trim();
		String confirmPassword = etConfirmPassword.getText().toString().trim();
		if(confirmPassword.length() > 0) {
			if(!(password.equals(confirmPassword) ? true : false)) {
				setErrorBackgroundSelector(etConfirmPassword);
			}
			else {
				setOriginalBackgroundColor(etConfirmPassword);
			}
		}
		else {
			setOriginalBackgroundColor(etConfirmPassword);
		}
	}

	//  Sign Up - Validation Method
	public String signUpValidation(String barFlyNickname, String fname, String lname, String email, String pwd, String confirmPwd, 
			String birthDate,boolean isAnyRadiobuttonChecked,String isCheckBoxChecked,String constantBirthDay) {
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		
		/* User Name */
		if(barFlyNickname.length()==0) {
			invalidFieldWithSetBackground(etBarFlyNickname, "Please enter Username");
		}
		else {
			setOriginalBackgroundColor(etBarFlyNickname);
		}
		
		/* First Name */
		if(fname.length()==0) {	
			invalidFieldWithSetBackground(etFirstName, "Please enter Firstname");
		}
		else {
			setOriginalBackgroundColor(etFirstName);
		}
		
		/* Last Name */
		if(lname.length()==0) {	
			invalidFieldWithSetBackground(etLastName, "Please enter Lastname");
		}
		else {
			setOriginalBackgroundColor(etLastName);
		}
		
		/* Email */
		if(email.length()==0) {	
			invalidFieldWithSetBackground(etEmail, "Please enter Email Address");
		}
		else if(!Validation.getInstance().isValid(email, Validation.CONSTANT_EMAIL)) {
			invalidFieldWithSetBackground(etEmail, "Please enter valid Email Address");
		}
		else {
			setOriginalBackgroundColor(etEmail);
		}
		
		/* Password */
		if(pwd.length()==0) {	
			invalidFieldWithSetBackground(etPassword, "Please enter Password");
		}
		else if(!Validation.getInstance().isValid(pwd, Validation.CONSTANT_PASSWORD)) {
			invalidFieldWithSetBackground(etPassword, "All passwords must be between 8 and 16 characters and require that you use 1 number and one special character.");
		}
		else {
			setOriginalBackgroundColor(etPassword);
		}
		
		/* Confirm Password */
		if(confirmPwd.length()==0) {	
			invalidFieldWithSetBackground(etConfirmPassword, "Please enter Confirm Password");
		}
		else {
			setOriginalBackgroundColor(etConfirmPassword);
		}
		
		/* Passwords match Validation */
		if(pwd.length()>0 && confirmPwd.length()>0) {
			if(!(pwd.equals(confirmPwd) ? true : false)) {
				invalidFieldWithSetBackground(etConfirmPassword, "Passwords are not match. Please enter the same passwords again.");
			}
		}
		else {
			setOriginalBackgroundColor(etConfirmPassword);
		}
		
		/* Birth Date Validation */
		if(birthDate.equals(constantBirthDay)) {
			invalidField("Please select BirthDay");
		}
		
		/* Radio Button Gender Selection */
		if(!isAnyRadiobuttonChecked) {
			invalidField("Please select Gender");
		}

		/* Check box Selection */
		if(!isCheckBoxChecked.equals(ConfigConstants.Constants.CONSTANT_YES)) {
			invalidField("Please check the checkbox. It is required.");
		}
		
		return toastMessage;
	}
	
	/* A method is used to set the invalid field background and it's value. */
	private void invalidFieldWithSetBackground(EditText editText, String message) {
		setErrorBackgroundSelector(editText);
		invalidField(message);
	}
	
	private void invalidField(String message) {
		if(isValid) {
			toastMessage = message;
		}
		isValid = false;
	}
	
	/*
	 * A method is used to set the white color i.e. the original selector
	 * background.
	 */
	private void setOriginalBackgroundColor(EditText et) {
		et.setBackgroundColor(getResources().getColor(R.color.whiteColor));
	}
	@SuppressWarnings("unused")
	private void setOriginalBackgroundColor(LinearLayout linearLayout) {
		linearLayout.setBackgroundColor(getResources().getColor(R.color.whiteColor));
	}
	
	/*
	 * A method is used to set the red color i.e. the error selector
	 * background.
	 */
	private void setErrorBackgroundSelector(EditText et) {
		et.setBackgroundResource(R.drawable.selector_edittext_red_color);
	}
	@SuppressWarnings("unused")
	private void setErrorBackgroundSelector(LinearLayout linearLayout) {
		linearLayout.setBackgroundResource(R.drawable.selector_edittext_red_color);
	}
}
