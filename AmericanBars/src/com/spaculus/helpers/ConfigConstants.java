package com.spaculus.helpers;

/**
 * @author Manali Sheth
 * @purpose This ConfigConstants class is used to declare all the constants
 *          which are used generally through out the application.
 */
public class ConfigConstants {

	/**
	 * Static instance of this class
	 */
	//private static ConfigConstants instanceConfigConstants = null;

	/**
	 * @purpose To get the static instance of this class
	 * @return instance of this class
	 */
	/*public static ConfigConstants getInstance() {
		if (instanceConfigConstants == null) {
			instanceConfigConstants = new ConfigConstants();
			// Log.i("New Instance", "New Instance");
		}
		return instanceConfigConstants;
	}*/

	/**
	 * @purpose To clear the instance of this class
	 */
	/*public void clearInstance() {
		instanceConfigConstants = null;
	}*/

	public interface Urls {
		/* Local Link */
		/*public final String url = "http://192.168.1.27/ADB/";*/
		/* Live Link */
		public final String url = "https://americanbars.com/";
		//public final String url = "https://www.americanbars.com/";
		
		public String webServiceURL = url + "api/";
		
		// Methods List
		public String checklogin = webServiceURL+"checklogin";
		public String user_logout = webServiceURL+"user_logout";
		public String forget_password = webServiceURL+"forget_password";
		public String reset_password = webServiceURL+"reset_password";
		public String user_register = webServiceURL+"user_register";
		public String user_change_password = webServiceURL+"user_change_password";
		public String auto_suggest_bar = webServiceURL+"auto_suggest_bar";
		public String bar_lists = webServiceURL+"bar_lists";
		public String beer_lists = webServiceURL+"beer_lists";
		public String cocktail_lists = webServiceURL+"cocktail_lists";
		public String liquor_lists = webServiceURL+"liquor_lists";
		public String taxi_lists = webServiceURL+"taxi_lists";
		public String bar_details = webServiceURL+"bar_details";
		public String getInTouch = webServiceURL+"getInTouch";
		public String event_details = webServiceURL+"event_details";
		public String bar_favorite = webServiceURL+"bar_favorite";
		public String bar_likes = webServiceURL+"bar_likes";
		public String getAllEvent = webServiceURL+"getAllEvent";
		public String beer_details = webServiceURL+"beer_details";
		public String beer_favorite = webServiceURL+"beer_favorite";
		public String beer_likes = webServiceURL+"beer_likes";
		public String cocktail_details = webServiceURL+"cocktail_details";
		public String cocktail_favorite = webServiceURL+"cocktail_favorite";
		public String cocktail_likes = webServiceURL+"cocktail_likes";
		public String liquor_details = webServiceURL+"liquor_details";
		public String liquor_favorite = webServiceURL+"liquor_favorite";
		public String liquor_likes = webServiceURL+"liquor_likes";
		public String taxi_details = webServiceURL+"taxi_details";
		public String gallery = webServiceURL+"gallery";
		public String get_gallery_by_id = webServiceURL+"get_gallery_by_id";
		public String getuserdashboard = webServiceURL+"getuserdashboard";
		public String editinfo = webServiceURL+"editinfo";
		public String favoritebar = webServiceURL+"favoritebar";
		public String deletefavbar = webServiceURL+"deletefavbar";
		public String favoritebeer = webServiceURL+"favoritebeer";
		public String deletefavbeer = webServiceURL+"deletefavbeer";
		public String favoritecocktail = webServiceURL+"favoritecocktail";
		public String deletefavcocktail = webServiceURL+"deletefavcocktail";
		public String favoriteliquor = webServiceURL+"favoriteliquor";
		public String deletefavliquor = webServiceURL+"deletefavliquor";
		public String get_user_album = webServiceURL+"get_user_album";
		public String bargallerydelete = webServiceURL+"bargallerydelete";
		public String edit_album = webServiceURL+"edit_album";
		public String add_album = webServiceURL+"add_album";
		public String remove_gallery_image = webServiceURL+"remove_gallery_image";
		public String privacy_settings = webServiceURL+"privacy_settings";
		public String update_privacy_settings = webServiceURL+"update_privacy_settings";
		public String get_album_by_id = webServiceURL+"get_album_by_id";
		public String get_contact_us_info = webServiceURL+"get_contact_us_info";
		public String send_inquiry = webServiceURL+"send_inquiry";
		public String fb_register = webServiceURL+"fb_register";
		public String register_android_device = webServiceURL+"register_android_device";
		public String beersuggest = webServiceURL+"beersuggest";
		public String cocktailsuggest = webServiceURL+"cocktailsuggest";
		public String liquorsuggest = webServiceURL+"liquorsuggest";
		public String suggest_bar = webServiceURL+"suggest_bar";
		public String add_report_bar = webServiceURL+"add_report_bar";
		
		/* For the Articles */
		public String listarticle = webServiceURL+"listarticle";
		public String add_rating = webServiceURL+"add_rating";
		
		/* For the Bars */
		public String add_bar_comment = webServiceURL+"add_bar_comment";
		public String barcomment = webServiceURL+"barcomment";
		
		/* For the Beers */
		public String add_beer_comment = webServiceURL+"add_beer_comment";
		public String beer_comments = webServiceURL+"beer_comments";
		public String beer_comment_likes = webServiceURL+"beer_comment_likes";
		public String beer_subcomments = webServiceURL+"beer_subcomments";
		public String add_beer_subcomment = webServiceURL+"add_beer_subcomment";
		public String remove_beer_comment = webServiceURL+"remove_beer_comment";
		
		/* For the Cocktail */
		public String add_cocktail_comment= webServiceURL+"add_cocktail_comment";
		public String cocktail_comments = webServiceURL+"cocktail_comments";
		public String cocktail_comment_likes = webServiceURL+"cocktail_comment_likes";
		public String cocktail_subcomments= webServiceURL+"cocktail_subcomments";
		public String add_cocktail_subcomment = webServiceURL+"add_cocktail_subcomment";
		public String remove_cocktail_comment = webServiceURL+"remove_cocktail_comment";
		
		/* For the Liquor */
		public String add_liquor_comment = webServiceURL+"add_liquor_comment";
		public String liquor_comments = webServiceURL+"liquor_comments";
		public String liquor_comment_likes = webServiceURL+"liquor_comment_likes";
		public String liquor_subcomments = webServiceURL+"liquor_subcomments";
		public String add_liquor_subcomment = webServiceURL+"add_liquor_subcomment";
		public String remove_liquor_comment = webServiceURL+"remove_liquor_comment";
		
		/* For the Bar Trivia Game */
		public String trivia = webServiceURL+"trivia";
		
		public String articledetail = webServiceURL+"articledetail";
	}

	public interface ImageUrls {
		public final String imageURL = Urls.url + "upload/";
		
		// Profile Picture URL
		public String profilePictureURL = imageURL + "user_thumb/";

		// Listing pages URL (200x200)
		public String barlogo_200 = imageURL + "barlogo_200/";
		public String beer_200 = imageURL + "beer_200/";
		public String cocktail_200 = imageURL + "cocktail_200/";
		public String liquor_200 = imageURL + "liquor_200/";
		public String user_thumb_200 = imageURL + "user_thumb_200/";
		
		// Details Page (240x240)
		public String barlogo_240 = imageURL + "barlogo_240/";
		public String beer_240 = imageURL + "beer_240/";
		public String cocktail_240 = imageURL + "cocktail_240/";
		public String liquor_240 = imageURL + "liquor_240/";
		public String user_thumb_240 = imageURL + "user_thumb_240/";

		// Details Page Listing (140x140)
		public String event_140 = imageURL + "event_140/";
		public String beer_140 = imageURL + "beer_140/";
		public String cocktail_140 = imageURL + "cocktail_140/";
		public String liquor_140 = imageURL + "liquor_140/";
		
		/* Full Bar Details Screen Gallery */
		public String galleySmallImageURL = imageURL + "bargallery_200/";
		public String galleyBigImageURL = imageURL + "bargallery_600/";
		public String galleyOriginalImageURL = imageURL + "bar_gallery_orig/";

		/* Bar Event Details Screen Gallery */
		public String galleySmallImageURL_bar_event = imageURL + "event_200/";
		public String galleyBigImageURL_bar_event = imageURL + "event_600/";
		public String galleyOriginalImageURL_bar_event = imageURL + "bar_eventgallery_orig/";

		/* Beer Details Screen */
		public String user_140 = imageURL + "user_140/";
		public String beer_600by400 = imageURL + "beer_600by400/";
		public String beer_orig = imageURL + "beer_orig/";

		/* Cocktail Details Screen */
		public String cocktail_600by400 = imageURL + "cocktail_600by400/";
		public String cocktail_orig = imageURL + "cocktail_orig/";

		/* Cocktail Details Screen */
		public String liquor_600by400 = imageURL + "liquor_600by400/";
		public String liquor_orig = imageURL + "liquor_orig/";
		
		/* Photo Gallery URL */
		/* Album Images URL */
		public String bar_gallery_thumb_big600by600 = imageURL + "bar_gallery_thumb_big600by600/";
		public String bar_gallery_thumb_big200by200 = imageURL + "bar_gallery_thumb_big200by200/";

		/* Add/Edit Album */
		public String bar_gallery_thumb_big140by140 = imageURL + "bar_gallery_thumb_big140by140/";

		/* Article */
		public String blog_thumb = imageURL + "blog_thumb/";
		public String blog300by300 = imageURL + "blog300by300/";
		/*public String blog1920by450 = imageURL + "blog1920by450/";*/
		
		/*Bar Trivia Game*/
		public String bar_pages_banner = imageURL + "bar_pages_banner/";
	}
	
	/* All the Constants */
	public interface Constants {
		/* Home Screen Constants */
		public final String HOME = "Home";
		public final String SIGN_IN = "Sign In";
		public final String SIGN_UP = "Sign Up";
		public final String PRIVACY_POLICY = "Privacy Policy";
		public final String CONTACT_US = "Contact Us";
		public final String TERMS_OF_USE = "Terms Of Use";

		/* Navigation Drawer Constants */
		public final String MY_DASHBOARD = "My Dashboard";
		public final String MY_FAV_BARS = "My Bar List";//"My Favorite Bars";
		public final String MY_FAV_BEERS = "My Beer List";//"My Favorite Beers";
		public final String MY_FAV_COCKTAILS = "My Cocktail List";//"My Favorite Cocktails";
		public final String MY_FAV_LIQUORS = "My Liquor List";//"My Favorite Liquors";
		public final String MY_ALBUMS = "My Albums";
		public final String PRIVACY_SETTINGS = "Privacy Settings";
		public final String CHANGE_PASSWORD = "Change Password";
		public final String LOG_OUT = "Log Out";
		public final String ARTICLES = "Articles";
		public final String BAR_TRIVIA_GAME = "Bar Trivia Game";

		/* Right Menu Constants */
		// public final String BAR_SEARCH = "Bar Search";
		public final String BAR_SEARCH = "Advanced Bar Search";
		public final String BEER_DIRECTORY = "Beer Directory";
		public final String COCKTAIL_DIRECTORY = "Cocktail Recipes";
		public final String LIQUOR_DIRECTORY = "Liquor Directory";
		public final String TAXI_DIRECTORY = "Taxi Directory";
		public final String PHOTO_GALLERY = "Photo Gallery";
		public final String USER_PROFILE = "User Profile";

		/*
		 * The following constants are used to remove screens from the Right
		 * Menu.
		 */
		public final String BEER_LISTING = "Beer Listing";
		public final String COCKTAIL_LISTING = "Cocktail Recipes";//"Cocktail Listing";
		public final String LIQUOR_LISTING = "Liquor Listing";
		public final String TAXI_LISTING = "Taxi Listing";
		/* For the Bar Changes */
		public final String FIND_BAR = "Find Bar";
		public final String BARS_NEAR_YOU = "BARS NEAR YOU";
		public final String HAPPY_HOURS_NEAR_YOU = "HAPPY HOURS NEAR YOU";
		// bar_search
		public final String SEARCH_FOR_HAPPY_HOUR = "SEARCH FOR HAPPY HOUR";
		// public final String BARS = "BARS";

		public final String CONSTANT_YES = "Yes";
		public final String CONSTANT_NO = "No";
		public final String CONSTANT_ONE = "1";
		public final String CONSTANT_ZERO = "0";
		public final String MALE = "male";
		public final String FEMALE = "female";

		public final String BAR_TYPE_FULL_MUG = "full_mug";
		public final String BAR_TYPE_HALF_MUG = "half_mug";

		public final String FULL_MUG = "Full Mug";
		public final String HALF_MUG = "Half Mug";

		/* Constants for the Sorting */
		public final String BAR_NAME_A_Z = "Bar Name A-Z";
		public final String BAR_NAME_Z_A = "Bar Name Z-A";
		public final String CITY_A_Z = "City A-Z";
		public final String CITY_Z_A = "City Z-A";
		public final String STATE_A_Z = "State A-Z";
		public final String STATE_Z_A = "State Z-A";
		public final String SORT_BY = "Sort By";
		public final String BLANK_TEXT = "";

		public final String BEER_NAME_A_Z = "Beer Name A-Z";
		public final String BEER_NAME_Z_A = "Beer Name Z-A";
		public final String BREWED_BY_A_Z = "Brewed By A-Z";
		public final String BREWED_BY_Z_A = "Brewed By Z-A";
		public final String CITY_PRODUCED_A_Z = "City Produced A-Z";
		public final String CITY_PRODUCED_Z_A = "City Produced Z-A";

		public final String COCKTAIL_NAME_A_Z = "Cocktail Name A-Z";
		public final String COCKTAIL_NAME_Z_A = "Cocktail Name Z-A";

		public final String LIQUOR_NAME_A_Z = "Liquor Name A-Z";
		public final String LIQUOR_NAME_Z_A = "Liquor Name Z-A";

		public final String COMPANY_NAME_A_Z = "Company Name A-Z";
		public final String COMPANY_NAME_Z_A = "Company Name Z-A";

		public final String COMPANY_ZIPCODE_A_Z = "Company Zipcode A-Z";
		public final String COMPANY_ZIPCODE_Z_A = "Company Zipcode Z-A";

		//  For the Date functionality
		public final String CONSTANT_BEFORE = "Before";
		public final String CONSTANT_EQUAL = "Equal";
		public final String CONSTANT_AFTER = "After";
		
		//  Add Album's Image Activity - To know whether web object or local object
		public final String WEB = "Web";
		public final String LOCAL = "Local";
		public final String DISPLAY = "Display";
		public final String ADD = "Add";
		
		/* For the Full Mug Bar Details Screen */
		/* Do not change this value as it's used into the comparison. */
		public final String LIQUOR = "liquor";
		public final String BAR = "bar";
		public final String BEER = "beer";
		public final String COCKTAIL = "cocktail";

		/* USA default Latitude and Longitude */
		public final double LATITUDE = 37.09024;
		public final double LONGITUDE = -95.712891;
		public final String USA = "USA";

		/* For the Sharing Links */
		public final String CONSTANT_FACEBOOK = "Facebook";
		public final String CONSTANT_TWITTER = "Twitter";
		public final String CONSTANT_LINKEDIN = "LinkedIn";
		public final String CONSTANT_GOOGLE_PLUS = "Google+";
		public final String CONSTANT_DRIBBLE = "Dribble";
		public final String CONSTANT_PINTEREST = "Pinterest";

		/* Constants for the Show More functionality */
		public final String DESCRIPTION = "Description";
		public final String HOW_TO_MAKE_IT_DESCRIPTION = "How To Make It";//"How To Make It Description";
		public final String INGREDIENTS_DESCRIPTION = "Ingredients";// "Ingredients Description";
		public final String ADB = "ADB";

		public final String ALBUM_IMAGE = "Album_Image";
		
		/* Constants for the Login Type */
		public String LOGIN_APPLICATION = "Application";
		public String LOGIN_FACEBOOK = "Facebook";
		
		/* Bar Search Around Me Activity */
		public final String CONSTANT_FIRST = "first";
		public final String CONSTANT_SECOND = "second";

		public final String CONSTANT_I_AM_HERE = "I am here.";

		/* For the Report Bar functionality */
		public final String CONSTANT_REPORT_BAR_CLOSED = "Closed";
		public final String CONSTANT_REPORT_BAR_WRONG_ADDRESS = "Has the Wrong Address";
		public final String CONSTANT_REPORT_BAR_IS_NOT_A_BAR = "Is not a Bar";
		public final String CONSTANT_REPORT_BAR_OTHER = "Other";
		
		/* For the Media Functionality */
		public String CONSTANT_MEDIA_PICTURE = "picture";
		public String CONSTANT_MEDIA_AUDIO = "audio";
		public String CONSTANT_MEDIA_VIDEO = "video";
		
		public String CONSTANT_LIKE = "Like";
		public String CONSTANT_LIKES = "Likes";
		
		/* For the Bar Special Hours */
		public String CONSTANT_BEER = "beer";
		public String CONSTANT_COCKTAIL = "cocktail";
		public String CONSTANT_LIQUOR = "liquor";
		public String CONSTANT_FOOD = "food";
		public String CONSTANT_OTHER = "other";
		
		public String CONSTANT_NONE = "None";
		public String CONSTANT_MONDAY = "Monday";
		public String CONSTANT_TUESDAY = "Tuesday";
		public String CONSTANT_WEDNESDAY = "Wednesday";
		public String CONSTANT_THURSDAY = "Thursday";
		public String CONSTANT_FRIDAY = "Friday";
		public String CONSTANT_SATURDAY = "Saturday";
		public String CONSTANT_SUNDAY = "Sunday";
		
		// Page Size Value
		// page_size = limit(in web service method)
		public final int limit = 10;
		
		/* For the Trivia Game */
		public final int CONSTANT_TOTAL_QUESTIONS = 20;
	}
	
	/* For the titles */
	public interface Titles {
		public final String TITLE_BEER_COMMENTS = "Beer Comments";
		public final String TITLE_COCKTAIL_COMMENTS = "Cocktail Comments";
		public final String TITLE_LIQUOR_COMMENTS = "Liquor Comments";
		
		public final String TITLE_BEER_COMMENT_REPLIES = "Beer Comment Replies";
		public final String TITLE_COCKTAIL_COMMENT_REPLIES = "Cocktail Comment Replies";
		public final String TITLE_LIQUOR_COMMENT_REPLIES = "Liquor Comment Replies";
		public final String TITLE_QUIZ = "Quiz";
		
		//public final String TITLE_TRIVIA = "Trivia";
	}
	
	/* All the Messages */
	public interface Messages {
		public final String RESPONSE_SUCCESS = "success";
		public final String RESPONSE_FAIL = "fail";
		public final String RESPONSE_INACTIVE = "inactive";
		public final String RESPONSE_NOT_REGISTERED = "not_registered";
		public final String RESPONSE_UNIQUE_FAILED = "unique_failed";
		public final String RESPONSE_AGE_FAILED = "age_failed";
		public final String RESPONSE_NOT_FOUND = "notfound";
		public final String mobileNumberToolTipText = "This is not a required field. Please provide your mobile phone information only if you want to receive promotional information via text from American Bars and bars listed in your favorite section.";
		public final String loadingMessage = "Loading....";
		public final String noRecordFound = "No records found";
		public final String noMoreRecordFound = "No more records found";
		public final String noVideoAvailable = "No Video Available";
		public final String maxUploadMediaMessage = "You cannot upload more than 16MB";
		public final String sharingMessage = "Sharing....";
		public final String selectRecordToDelete = "Please select a record to delete.";
		public final String noFavoriteBar = "My Favorite Bars list is empty.";
		public final String noFavoriteBeer = "My Favorite Beers list is empty.";
		public final String noFavoriteCocktail = "My Favorite Cocktails list is empty.";
		public final String noFavoriteLiquor = "My Favorite Liquors list is empty.";
		public final String noMyAlbum = "My Albums list is empty.";
	}
	
	/* All the Key values used for an intent */
	public interface Keys {
		public final String KEY_SEARCH_TEXT = "searchText";
		public final String KEY_CONSTANT_REDIRECT_FROM = "redirect_from";
		public final String KEY_EVENT_ID = "event_id";
		public final String KEY_EVENT_NAME = "event_name";
		public final String KEY_URL = "url";
		public final String KEY_BAR_ID = "bar_id";
		public final String KEY_BEER_ID = "beer_id";
		public final String KEY_COCKTAIL_ID = "cocktail_id";
		public final String KEY_LIQUOR_ID = "liquor_id";
		public final String KEY_TAXI_ID = "taxi_id";
		public final String KEY_BAR_GALLERY_ID = "bar_gallery_id";
		public final String KEY_GALLERY_NAME = "gallery_name";
		public final String KEY_WEB_SERVICE_METHOD_NAME = "web_service_method_name";
		public final String KEY_LATITUDE = "latitude";
		public final String KEY_LONGITUDE = "longitude";
		public final String KEY_BAR_NAME = "bar_name";
		public final String KEY_BAR_ADDRESS = "bar_address";
		public final String KEY_ARTICLE_OBJECT = "Article_Object";
		/* Bar Search Around Me Activity */
		public String KEY_IS_REDIRECTED_FROM = "is_redirected_from";
		public String KEY_DAY = "day";
		public final String KEY_MASTER_COMMENT_ID = "master_comment_id";
		public final String KEY_TRIVIA_GAME_LIST = "trivia_game_list";
		public final String KEY_TOTAL_CORRECT_ANSWERS = "total_correct_answers";
		public final String KEY_TOTAL_TIME = "total_time";
	}
	
	/*
	 * All the Result Code Constants that are used for the onActivityResult()
	 * method.
	 */
	public interface ResultCodes {
		public final int REQUEST_CODE_APLHA_VIEW = 1;
		public final int REQUEST_CODE_BAR_SEARCH = 2;
		public final int PICK_MEDIA_RESULT_CODE = 3;
		public final int RECORD_AUDIO_RESULT_CODE = 4;
		public final int CAPTURE_IMAGE_RESULT_CODE = 5;
		public final int CAPTURE_VIDEO_RESULT_CODE = 6;
	}
	
	/* For the Sharing URLs */
	public interface SharingURLs {
		public String shareAlbumDetatils = Urls.url + "article/detail/";// http://192.168.1.27/ADB/article/detail/MTE4
		public String shareTriviaGameURL = Urls.url+"trivia";
		public String shareBarDetailsURL = Urls.url+"bar/details/";
		public String shareBeerDetailsURL = Urls.url+"beer/detail/";
		public String shareCocktailDetailsURL = Urls.url+"cocktail/detail/";
		public String shareLiquorDetailsURL = Urls.url+"liquor/detail/";
	}
	
	public interface CMSPageURLs {
		/* URL and slug names for the CMS Pages */
		public String CMSPageURL = "https://americanbars.com/home/contentView/";
		public String slugPrivacyPolicy = "privacy-policy";
		public String slugTermsOfUse = "terms-of-use";
	}
	
	/* For the Date Formats */
	public interface DateFormats {
		public final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";// 24-hours format
		public final String MM_DD_YYYY = "MM-dd-yyyy";
		public final String HH_MM_SS = "HH:mm:ss";// 24-hours format
		public final String YYYY_MM_DD = "yyyy-MM-dd";
		public final String EEEE_MMM_dd_yyyy = "EEEE, MMM dd, yyyy";
		public final String EEEE = "EEEE";
	}

	/* Flag Variables */
	public static boolean isAddToFavoriteORLikeBarButtonClicked = false;
	public static boolean isAddToFavoriteORLikeBeerButtonClicked = false;
	public static boolean isAddToFavoriteORLikeCocktailButtonClicked = false;
	public static boolean isAddToFavoriteORLikeLiquorButtonClicked = false;
	public static boolean isAddEditAlbum = false;
	public static boolean isArticleRatingDone = false;
	public static boolean isBarReviewAdded = false;
	/*
	 * The following flag is used if user either add a new comment from the
	 * BeerCommentsListActivity or like/dislike a comment from it.
	 */
	public static boolean isBeerCommentAdded = false;
	public static boolean isCocktailCommentAdded = false;
	public static boolean isLiquorCommentAdded = false;
	
	public final static String GOOGLE_SENDER_ID = "463480640634";
	
	/*
	 * public final static String lat = "41.2791691"; public final static String
	 * lang = "-96.09981729999998";
	 */

	/* Apple's Lat Long */
	/*
	 * public final static String lat = "37.332331"; public final static String
	 * lang = "-122.031219";
	 */

	/* American Bar's Lat Long */
	/*public final static String lat = "33.767313";
	public final static String lang = "-118.190096";*/
}
