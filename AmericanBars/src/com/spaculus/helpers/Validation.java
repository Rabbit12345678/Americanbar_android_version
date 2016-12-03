package com.spaculus.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author comp-343
 * @see #getInstance() - To create the instance of this class
 * @see #isEmailValid(String) - To check whether email address is valid or not
 *  @see 
 * 
 * **/

public class Validation {

	//  To get the instance of this class
	private static Validation instanceValidation = null;
	public static Validation getInstance(){
        if(instanceValidation == null){
        	instanceValidation = new Validation();
            //Log.i("New Instance", "New Instance");
        }
        return instanceValidation;
    }
	
	//  Need to clear the instance
	public void clearInstance(){
		instanceValidation = null;
	}
	
	//  For the Email Validation
	private Pattern pattern = null;
	private Matcher matcher = null;
	
	/* Constants */
	public static String CONSTANT_EMAIL = "email";
	public static String CONSTANT_PASSWORD = "password";
	//public static String CONSTANT_USER_NAME = "user_name";
	//public static String CONSTANT_NAME = "name";
	
	/* Different Regular Expressions */
	//  Email Address Validation
	String emailRegularExpression =
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                  +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                  +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                  +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                  +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
	
	String passwordRegularExpression = "^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,16}$";
	
	/*//  Sign Up Screen (User name)
	//  Min-3, Max-20 characters; Letters, numbers and underscore only
	String userNameRegularExpression = "^[A-Za-z0-9_-]{3,20}$";
	
	//  Sign Up Screen (Name)
	//  Min-3, Max-20 characters; Letters only
	String nameRegularExpression = "^[A-Za-z]{3,20}$";*/
	
	public boolean isValid(String content, String flag){
		
		String regularExpression = "";
		
		if(flag.equals(CONSTANT_EMAIL)){
			regularExpression = emailRegularExpression;
		}
		else if(flag.equals(CONSTANT_PASSWORD)){
			regularExpression = passwordRegularExpression;
		}
		/*else if(flag.equals(CONSTANT_USER_NAME)){
			regularExpression = userNameRegularExpression;
		}
		else if(flag.equals(CONSTANT_NAME)){
			regularExpression = nameRegularExpression;
		}*/
		
		CharSequence inputStr = content;
		
	     pattern = Pattern.compile(regularExpression,Pattern.CASE_INSENSITIVE);
	     matcher = pattern.matcher(inputStr);
	
	     if(matcher.matches())
	        return true;
	     else
	        return false;
	}
	
	
	
	//  Login - Validation Method
	public String loginValidation(String uname, String pwd) {

		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(uname.length()==0){	
			//toastMessage = "Please enter Username or Email";
			toastMessage = "Please enter Email";
		}
		else if(pwd.length()==0){	
			toastMessage = "Please enter Password";
		}
		/*else if(!Validation.getInstance().isValid(uname, Validation.CONSTANT_USER_NAME) && !Validation.getInstance().isValid(uname, Validation.CONSTANT_EMAIL)){
			toastMessage = "Please enter valid Username or Email";
		}*/
		else if(!Validation.getInstance().isValid(uname, Validation.CONSTANT_EMAIL)){
			toastMessage = "Please enter valid Email";
		}
		return toastMessage;
	}
	
	//  Forgot Password - Validation Method
	public String forgotPasswordValidation(String email) {

		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(email.length()==0){	
			toastMessage = "Please enter Email Address";
		}
		else if(!Validation.getInstance().isValid(email, Validation.CONSTANT_EMAIL)){
			toastMessage = "Please enter valid Email Address";
		}
		return toastMessage;
	}
	
	//  Reset Password - Validation Method
	public String resetPasswordValidation(String verificationCode, String newPwd, String confirmPwd) {

		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(verificationCode.length()==0){	
			toastMessage = "Please enter Verification Code";
		}
		else if(newPwd.length()==0){
			toastMessage = "Please enter New Password";
		}
		else if(confirmPwd.length()==0){
			toastMessage = "Please enter Confirm Password";
		}
		else if(!(newPwd.equals(confirmPwd) ? true : false)){
			toastMessage = "Passwords are not match. Please enter the same passwords again.";
		}
		else if(!Validation.getInstance().isValid(newPwd, Validation.CONSTANT_PASSWORD)){
			toastMessage = "For Password, provide atleast 1 Number, 1 Special character,1 Alphabet and between 8 to 16 characters.";
		}
		return toastMessage;
	}
	
	//  Login - Validation Method
	public String changePassword(String oldPwd, String newPwd, String confirmNewPwd) {

		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(oldPwd.length()==0){	
			//toastMessage = "Please enter User name or Email";
			toastMessage = "Please enter Old Password";
		}
		else if(newPwd.length()==0){	
			toastMessage = "Please enter New Password";
		}
		else if(confirmNewPwd.length()==0){	
			toastMessage = "Please enter Confirm New Password";
		}
		else if(!(newPwd.equals(confirmNewPwd) ? true : false)){
			toastMessage = "Passwords are not match. Please enter the same passwords again.";
		}
		else if(!Validation.getInstance().isValid(newPwd, Validation.CONSTANT_PASSWORD)){
			toastMessage = "For Password, provide atleast 1 Number, 1 Special character,1 Alphabet and between 8 to 16 characters.";
		}
		return toastMessage;
	}
	
	//  Find Bar - Validation Method
	public String findBarValidation(String barName, String state, String city, String zipCode) {

		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(barName.length()==0 && state.length()==0 && city.length()==0 && zipCode.length()==0){	
			toastMessage = "Please provide atleast one field to search.";
		}
		return toastMessage;
	}
	
/*	//  Sign Up - Validation Method
	public String signUpValidation(String barFlyNickname, String fname, String lname, String email, String pwd, String confirmPwd, 
			String birthDate,boolean isAnyRadiobuttonChecked,String isCheckBoxChecked,String constantBirthDay) {

		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(barFlyNickname.length()==0){	
			//toastMessage = "Please enter Bar Fly Nickname";
			toastMessage = "Please enter Username";
		}
		else if(fname.length()==0){	
			toastMessage = "Please enter Firstname";
		}
		else if(lname.length()==0){	
			toastMessage = "Please enter Lastname";
		}
		else if(email.length()==0){	
			toastMessage = "Please enter Email Address";
		}
		else if(pwd.length()==0){
			toastMessage = "Please enter Password";
		}
		else if(confirmPwd.length()==0){
			toastMessage = "Please enter Confirm Password";
		}
		else if(!Validation.getInstance().isValid(email, Validation.CONSTANT_EMAIL)){
			toastMessage = "Please enter valid Email Address";
		}
		else if(!(pwd.equals(confirmPwd) ? true : false)){
			toastMessage = "Passwords are not match. Please enter the same passwords again.";
		}
		else if(!Validation.getInstance().isValid(pwd, Validation.CONSTANT_PASSWORD)){
			//toastMessage = "For Password, provide atleast 1 Number, 1 Special character,1 Alphabet and between 8 to 16 characters.";
			toastMessage = "All passwords must be between 8 and 16 characters and require that you use 1 number and one special character.";
		}
		else if(birthDate.equals(constantBirthDay)){
			toastMessage = "Please select BirthDay";
		}
		else if(!isAnyRadiobuttonChecked){
			toastMessage = "Please select Gender";
		}
		else if(!isCheckBoxChecked.equals(ConfigConstants.getInstance().CONSTANT_YES)) {
			toastMessage = "Please check the checkbox. It is required.";
		}
		return toastMessage;
	}*/
	
	//  Get In Touch - Validation Method
	public String getInTouchValidation(String name, String phone, String email, String comment) {

		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(name.length()==0){	
			toastMessage = "Please enter Name";
		}
		else if(phone.length()==0){	
			toastMessage = "Please enter Phone Number";
		}
		else if(email.length()==0){	
			toastMessage = "Please enter Email Address";
		}
		else if(comment.length()==0){	
			toastMessage = "Please enter Comment";
		}
		else if(!Validation.getInstance().isValid(email, Validation.CONSTANT_EMAIL)){
			toastMessage = "Please enter valid Email Address";
		}
		return toastMessage;
	}
	
	//  Edit Profile - Validation Method
	public String editProfile(String fname, String lname, String uname, String email, String aboutUser) {

		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(fname.length()==0){	
			toastMessage = "Please enter First Name";
		}
		else if(lname.length()==0){	
			toastMessage = "Please enter Last Name";
		}
		else if(uname.length()==0){	
			toastMessage = "Please enter User Name";
		}
		else if(email.length()==0){	
			toastMessage = "Please enter Email Address";
		}
		else if(aboutUser.length()==0){	
			toastMessage = "Please enter About User";
		}
		else if(!Validation.getInstance().isValid(email, Validation.CONSTANT_EMAIL)){
			toastMessage = "Please enter valid Email Address";
		}
		return toastMessage;
	}
	
	//  Add/Edit Album - Validation Method
	public String addEditAlbum(String albumName, int imagesListSize) {

		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(albumName.length()==0){	
			toastMessage = "Please enter Album Title";
		}
		else if(imagesListSize==0){	
			toastMessage = "Please select at least one Image.";
		}
		return toastMessage;
	}
	
	//  Contact Us - Validation Method
	public String contactUs(String name, String email, String subject, String message) {

		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(name.length()==0){	
			toastMessage = "Please enter Name";
		}
		else if(email.length()==0){	
			toastMessage = "Please enter Email Address";
		}
		else if(subject.length()==0){	
			toastMessage = "Please enter Subject";
		}
		else if(message.length()==0){	
			toastMessage = "Please enter Message";
		}
		else if(!Validation.getInstance().isValid(email, Validation.CONSTANT_EMAIL)){
			toastMessage = "Please enter valid Email Address";
		}
		return toastMessage;
	}
	
	//  Suggest New Beer - Validation Method
	public String suggestNewBeer(String beerTitle, String country) {

		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(beerTitle.length()==0){	
			toastMessage = "Please enter Beer Title";
		}
		else if(country.length()==0){	
			toastMessage = "Please enter Country";
		}
		return toastMessage;
	}
	
	//  Suggest New Cocktail - Validation Method
	public String suggestNewCocktail(String cocktailName) {

		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(cocktailName.length()==0){	
			toastMessage = "Please enter Cocktail Name";
		}
		return toastMessage;
	}
	
	//  Suggest New Liquor - Validation Method
	public String suggestNewLiquor(String liquorName) {

		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(liquorName.length()==0){	
			toastMessage = "Please enter Liquor Name";
		}
		return toastMessage;
	}
	
	//  Suggest New Bar - Validation Method
	public String suggestNewBar(String barName, String address, String city, String state, String phoneNumber, String zipCode) {
		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(barName.length()==0){	
			toastMessage = "Please enter Bar Name";
		}
		else if(address.length()==0){	
			toastMessage = "Please enter Address";
		}
		else if(city.length()==0){	
			toastMessage = "Please enter City";
		}
		else if(state.length()==0){	
			toastMessage = "Please enter State";
		}
		else if(phoneNumber.length()==0){	
			toastMessage = "Please enter Phone Number";
		}
		else if(zipCode.length()==0){	
			toastMessage = "Please enter Zip Code";
		}
		return toastMessage;
	}
	
	//  Write A Review - Validation Method
	public String writeAReview(String reviewTitle, String reviewDesc, float rating) {
		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(reviewTitle.length()==0){	
			toastMessage = "Please enter Review title";
		}
		else if(reviewDesc.length()==0){	
			toastMessage = "Please enter Review description";
		}
		else if(rating <= 0){	
			toastMessage = "Please select Rating";
		}
		return toastMessage;
	}
	
	//  Report A Bar - Validation Method
	public String reportABar(String email, String selectedReportType, String desc) {
		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(email.length()==0){	
			toastMessage = "Please enter Email";
		}
		else if(!Validation.getInstance().isValid(email, Validation.CONSTANT_EMAIL)){
			toastMessage = "Please enter valid Email";
		}
		else if(selectedReportType.equals(ConfigConstants.Constants.CONSTANT_REPORT_BAR_OTHER)){	
			if(desc.isEmpty()) {
				toastMessage = "Please enter Description";
			}
		}
		return toastMessage;
	}
	
	//  Post A Comment - Validation Method
	public String postAComment(String commentTitle, String commentDesc) {
		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(commentTitle.length()==0){	
			toastMessage = "Please enter Comment title";
		}
		else if(commentDesc.length()==0){	
			toastMessage = "Please enter Comment description";
		}
		return toastMessage;
	}
	
	//  Post A Reply of a Comment - Validation Method
	public String postReply(String reply) {
		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(reply.length()==0){	
			toastMessage = "Please enter Reply";
		}
		return toastMessage;
	}
	
	//  Happy Hours and Events Search - Validation Method
	public String happyHoursAndEventsSearch(String barName, String address, String day) {
		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(barName.length()==0){	
			toastMessage = "Please enter Reply";
		}
		return toastMessage;
	}
	
	//  Find Happy Hours and Events - Validation Method
	public String findHappyHoursAndEventsValidation(String barName, String address, String day, String selectDayConstant) {
		String toastMessage = "";
		
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(barName.length()==0 && address.length()==0 && day.equals(selectDayConstant)){	
			toastMessage = "Please provide atleast one field to search.";
		}
		return toastMessage;
	}
	
	//  Face book Login - Validation Method
	public String fbLoginValidation(String email) {
		String toastMessage = "";
		//  Here, all the fields are mandatory so first of all check that empty fields validations first.
		if(email.length()==0){	
			toastMessage = "Please provide a facebook permission to access your email address.";
		}
		else if(!Validation.getInstance().isValid(email, Validation.CONSTANT_EMAIL)){
			toastMessage = "Please enter valid facebook email";
		}
		return toastMessage;
	}
}
