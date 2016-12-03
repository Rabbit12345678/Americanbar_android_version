package com.spaculus.helpers;

import java.util.ArrayList;

import org.json.JSONObject;

import com.spaculus.beans.Bar;
import com.spaculus.beans.BarEvent;
import com.spaculus.beans.BarEventDateTime;
import com.spaculus.beans.BarSpecialHours;
import com.spaculus.beans.HappyHours;
import com.spaculus.beans.Reply;
import com.spaculus.beans.Review;
import com.spaculus.beans.Taxi;
import com.spaculus.beans.TriviaGame;

import android.annotation.SuppressLint;

@SuppressLint("NewApi")
public class ParseJsonObject {

	/* Bar Review */
	public Review addBarReviewObject(JSONObject jsonObj) {
		Review objReview = new Review();
		try {
			objReview.setComment_id(Utils.getInstance().isTagExists(jsonObj, "bar_comment_id"));
			objReview.setParent_id(Utils.getInstance().isTagExists(jsonObj, "bar_id"));
			objReview.setUser_id(Utils.getInstance().isTagExists(jsonObj, "user_id"));
			objReview.setComment_title(Utils.getInstance().isTagExists(jsonObj, "comment_title"));
			objReview.setComment(Utils.getInstance().isTagExists(jsonObj, "comment"));
			objReview.setBar_rating(Utils.getInstance().isTagExists(jsonObj, "bar_rating"));
			objReview.setStatus(Utils.getInstance().isTagExists(jsonObj, "status"));
			objReview.setIs_deleted(Utils.getInstance().isTagExists(jsonObj, "is_deleted"));
			objReview.setDate_added(Utils.getInstance().isTagExists(jsonObj, "date_added"));
			objReview.setProfile_image(Utils.getInstance().isTagExists(jsonObj, "profile_image"));
			objReview.setFirst_name(Utils.getInstance().isTagExists(jsonObj, "first_name"));
			objReview.setLast_name(Utils.getInstance().isTagExists(jsonObj, "last_name"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objReview;
	}

	/* Beer Comment */
	public Review addBeerCommentObject(JSONObject jsonObj) {
		Review objReview = new Review();
		try {
			// Now, if user_id=0 means Admin has done entry so we need to show
			// first_name last_name as ADB.
			String user_id = Utils.getInstance().isTagExists(jsonObj, "user_id");
			String first_name = Utils.getInstance().isTagExists(jsonObj, "first_name");
			String last_name = Utils.getInstance().isTagExists(jsonObj, "last_name");
			if (user_id.equals(ConfigConstants.Constants.CONSTANT_ZERO)) {
				first_name = ConfigConstants.Constants.ADB;
				last_name = "";
			}
			objReview.setComment_id(Utils.getInstance().isTagExists(jsonObj, "beer_comment_id"));
			objReview.setParent_id(Utils.getInstance().isTagExists(jsonObj, "beer_id"));
			objReview.setProfile_image(Utils.getInstance().isTagExists(jsonObj, "profile_image"));
			objReview.setFirst_name(first_name);
			objReview.setLast_name(last_name);
			objReview.setUser_id(user_id);
			objReview.setComment_title(Utils.getInstance().isTagExists(jsonObj, "comment_title"));
			objReview.setComment(Utils.getInstance().isTagExists(jsonObj, "comment"));
			objReview.setDate_added(Utils.getInstance().isTagExists(jsonObj, "date_added"));
			objReview.setIs_like(Utils.getInstance().isTagExists(jsonObj, "is_like"));
			objReview.setTotal_like(Utils.getInstance().isTagExists(jsonObj, "total_like"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objReview;
	}
	
	/* Cocktail Comment */
	public Review addCocktailCommentObject(JSONObject jsonObj) {
		Review objReview = new Review();
		try {
			// Now, if user_id=0 means Admin has done entry so we need to show
			// first_name last_name as ADB.
			String user_id = Utils.getInstance().isTagExists(jsonObj, "user_id");
			String first_name = Utils.getInstance().isTagExists(jsonObj, "first_name");
			String last_name = Utils.getInstance().isTagExists(jsonObj, "last_name");
			if (user_id.equals(ConfigConstants.Constants.CONSTANT_ZERO)) {
				first_name = ConfigConstants.Constants.ADB;
				last_name = "";
			}
			objReview.setComment_id(Utils.getInstance().isTagExists(jsonObj, "cocktail_comment_id"));
			objReview.setParent_id(Utils.getInstance().isTagExists(jsonObj, "cocktail_id"));
			objReview.setProfile_image(Utils.getInstance().isTagExists(jsonObj, "profile_image"));
			objReview.setFirst_name(first_name);
			objReview.setLast_name(last_name);
			objReview.setUser_id(user_id);
			objReview.setComment_title(Utils.getInstance().isTagExists(jsonObj, "comment_title"));
			objReview.setComment(Utils.getInstance().isTagExists(jsonObj, "comment"));
			objReview.setDate_added(Utils.getInstance().isTagExists(jsonObj, "date_added"));
			objReview.setIs_like(Utils.getInstance().isTagExists(jsonObj, "is_like"));
			objReview.setTotal_like(Utils.getInstance().isTagExists(jsonObj, "total_like"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objReview;
	}
	
	/* Liquor Comment */
	public Review addLiquorCommentObject(JSONObject jsonObj) {
		Review objReview = new Review();
		try {
			// Now, if user_id=0 means Admin has done entry so we need to show
			// first_name last_name as ADB.
			String user_id = Utils.getInstance().isTagExists(jsonObj, "user_id");
			String first_name = Utils.getInstance().isTagExists(jsonObj, "first_name");
			String last_name = Utils.getInstance().isTagExists(jsonObj, "last_name");
			if (user_id.equals(ConfigConstants.Constants.CONSTANT_ZERO)) {
				first_name = ConfigConstants.Constants.ADB;
				last_name = "";
			}
			objReview.setComment_id(Utils.getInstance().isTagExists(jsonObj, "liquor_comment_id"));
			objReview.setParent_id(Utils.getInstance().isTagExists(jsonObj, "liquor_id"));
			objReview.setProfile_image(Utils.getInstance().isTagExists(jsonObj, "profile_image"));
			objReview.setFirst_name(first_name);
			objReview.setLast_name(last_name);
			objReview.setUser_id(user_id);
			objReview.setComment_title(Utils.getInstance().isTagExists(jsonObj, "comment_title"));
			objReview.setComment(Utils.getInstance().isTagExists(jsonObj, "comment"));
			objReview.setDate_added(Utils.getInstance().isTagExists(jsonObj, "date_added"));
			objReview.setIs_like(Utils.getInstance().isTagExists(jsonObj, "is_like"));
			objReview.setTotal_like(Utils.getInstance().isTagExists(jsonObj, "total_like"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objReview;
	}
	
	/* Beer Comment Reply */
	public Reply addBeerReplyObject(JSONObject jsonObj) {
		Reply objReply = new Reply();
		try {
			// Now, if user_id=0 means Admin has done entry so we need to show
			// first_name last_name as ADB.
			String user_id = Utils.getInstance().isTagExists(jsonObj, "user_id");
			String first_name = Utils.getInstance().isTagExists(jsonObj, "first_name");
			String last_name = Utils.getInstance().isTagExists(jsonObj, "last_name");
			if (user_id.equals(ConfigConstants.Constants.CONSTANT_ZERO)) {
				first_name = ConfigConstants.Constants.ADB;
				last_name = "";
			}
			objReply.setComment_id(Utils.getInstance().isTagExists(jsonObj, "beer_comment_id"));
			objReply.setParent_id(Utils.getInstance().isTagExists(jsonObj, "beer_id"));
			objReply.setProfile_image(Utils.getInstance().isTagExists(jsonObj, "profile_image"));
			objReply.setFirst_name(first_name);
			objReply.setLast_name(last_name);
			objReply.setUser_id(user_id);
			objReply.setComment(Utils.getInstance().isTagExists(jsonObj, "comment"));
			objReply.setDate_added(Utils.getInstance().isTagExists(jsonObj, "date_added"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objReply;
	}
	
	/* Cocktail Comment Reply */
	public Reply addCocktailReplyObject(JSONObject jsonObj) {
		Reply objReply = new Reply();
		try {
			// Now, if user_id=0 means Admin has done entry so we need to show
			// first_name last_name as ADB.
			String user_id = Utils.getInstance().isTagExists(jsonObj, "user_id");
			String first_name = Utils.getInstance().isTagExists(jsonObj, "first_name");
			String last_name = Utils.getInstance().isTagExists(jsonObj, "last_name");
			if (user_id.equals(ConfigConstants.Constants.CONSTANT_ZERO)) {
				first_name = ConfigConstants.Constants.ADB;
				last_name = "";
			}
			objReply.setComment_id(Utils.getInstance().isTagExists(jsonObj, "cocktail_comment_id"));
			objReply.setParent_id(Utils.getInstance().isTagExists(jsonObj, "cocktail_id"));
			objReply.setProfile_image(Utils.getInstance().isTagExists(jsonObj, "profile_image"));
			objReply.setFirst_name(first_name);
			objReply.setLast_name(last_name);
			objReply.setUser_id(user_id);
			objReply.setComment(Utils.getInstance().isTagExists(jsonObj, "comment"));
			objReply.setDate_added(Utils.getInstance().isTagExists(jsonObj, "date_added"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objReply;
	}
	
	/* Liquor Comment Reply */
	public Reply addLiquorReplyObject(JSONObject jsonObj) {
		Reply objReply = new Reply();
		try {
			// Now, if user_id=0 means Admin has done entry so we need to show
			// first_name last_name as ADB.
			String user_id = Utils.getInstance().isTagExists(jsonObj, "user_id");
			String first_name = Utils.getInstance().isTagExists(jsonObj, "first_name");
			String last_name = Utils.getInstance().isTagExists(jsonObj, "last_name");
			if (user_id.equals(ConfigConstants.Constants.CONSTANT_ZERO)) {
				first_name = ConfigConstants.Constants.ADB;
				last_name = "";
			}
			objReply.setComment_id(Utils.getInstance().isTagExists(jsonObj, "liquor_comment_id"));
			objReply.setParent_id(Utils.getInstance().isTagExists(jsonObj, "liquor_id"));
			objReply.setProfile_image(Utils.getInstance().isTagExists(jsonObj, "profile_image"));
			objReply.setFirst_name(first_name);
			objReply.setLast_name(last_name);
			objReply.setUser_id(user_id);
			objReply.setComment(Utils.getInstance().isTagExists(jsonObj, "comment"));
			objReply.setDate_added(Utils.getInstance().isTagExists(jsonObj, "date_added"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objReply;
	}
	
	/* Trivia Game */
	public TriviaGame addTriviaGameObject(JSONObject jsonObj) {
		TriviaGame objTriviaGame = new TriviaGame();
		ArrayList<String> answerOptionsList = new ArrayList<String>();
		try {
			objTriviaGame.setTrivia_id(Utils.getInstance().isTagExists(jsonObj, "trivia_id"));
			objTriviaGame.setQuestion(Utils.getInstance().isTagExists(jsonObj, "question"));
			for(int i=1; i<=4; i++) {
				answerOptionsList.add(Utils.getInstance().isTagExists(jsonObj, ""+i));
			}
			objTriviaGame.setAnswerOptionsList(answerOptionsList);
			objTriviaGame.setAnswer(Utils.getInstance().isTagExists(jsonObj, "answer"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objTriviaGame;
	}
	
	/* A method is used to add the bar event. */
	public BarEvent addBarEventObject(JSONObject jsonObj) {
		BarEvent objBarEvent = new BarEvent();
		try {
			objBarEvent.setId(Utils.getInstance().isTagExists(jsonObj, "event_id"));
			objBarEvent.setTitle(Utils.getInstance().isTagExists(jsonObj, "event_title"));
			objBarEvent.setDesc(Utils.getInstance().isTagExists(jsonObj, "event_desc"));
			objBarEvent.setStart_date(Utils.getInstance().isTagExists(jsonObj, "eventdate"));
			objBarEvent.setEvent_image(Utils.getInstance().isTagExists(jsonObj, "event_image"));
			objBarEvent.setEvent_lat(Utils.getInstance().isTagExists(jsonObj, "event_lat"));
			objBarEvent.setEvent_lng(Utils.getInstance().isTagExists(jsonObj, "event_lng"));
			objBarEvent.setAddress(Utils.getInstance().isTagExists(jsonObj, "address"));
			objBarEvent.setCity(Utils.getInstance().isTagExists(jsonObj, "city"));
			objBarEvent.setState(Utils.getInstance().isTagExists(jsonObj, "state"));
			objBarEvent.setZipcode(Utils.getInstance().isTagExists(jsonObj, "zipcode"));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return objBarEvent;
	}
	
	/* A method is used to add the bar special hours object. */
	public BarSpecialHours addBarSpecialHourObject(JSONObject jsonObj, int positionFirst, int positionLast) {
		BarSpecialHours objBarSpecialHours = new BarSpecialHours();
		try {
			objBarSpecialHours.setDays(Utils.getInstance().isTagExists(jsonObj, "days"));
			objBarSpecialHours.setHour_from(Utils.getInstance().isTagExists(jsonObj, "hour_from"));
			objBarSpecialHours.setHour_to(Utils.getInstance().isTagExists(jsonObj, "hour_to"));
			objBarSpecialHours.setCat(Utils.getInstance().isTagExists(jsonObj, "cat"));
			objBarSpecialHours.setBeer_name(Utils.getInstance().isTagExists(jsonObj, "beer_name"));
			objBarSpecialHours.setSp_beer_price(Utils.getInstance().isTagExists(jsonObj, "sp_beer_price"));
			objBarSpecialHours.setCocktail_name(Utils.getInstance().isTagExists(jsonObj, "cocktail_name"));
			objBarSpecialHours.setSp_cocktail_price(Utils.getInstance().isTagExists(jsonObj, "sp_cocktail_price"));
			objBarSpecialHours.setLiquor_name(Utils.getInstance().isTagExists(jsonObj, "liquor_name"));
			objBarSpecialHours.setSp_liquor_price(Utils.getInstance().isTagExists(jsonObj, "sp_liquor_price"));
			objBarSpecialHours.setFood_name(Utils.getInstance().isTagExists(jsonObj, "food_name"));
			objBarSpecialHours.setFood_price(Utils.getInstance().isTagExists(jsonObj, "food_price"));
			objBarSpecialHours.setOther_name(Utils.getInstance().isTagExists(jsonObj, "other_name"));
			objBarSpecialHours.setOther_price(Utils.getInstance().isTagExists(jsonObj, "other_price"));
			/* For the Header */
			if(positionFirst == 0) {
				objBarSpecialHours.setShowHeader(true);
			}
			else{
				objBarSpecialHours.setShowHeader(false);
			}
			/* For the Footer */
			if(positionFirst == positionLast) {
				objBarSpecialHours.setShowFooter(true);
			}
			else{
				objBarSpecialHours.setShowFooter(false);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return objBarSpecialHours;
	}
	
	/* A method is used to add the bar event details date time. */
	public BarEventDateTime addBarEventDateTimeObject(JSONObject jsonObj) {
		BarEventDateTime objBarEventDateTime = new BarEventDateTime();
		try {
			objBarEventDateTime.setSss_event_time_id(Utils.getInstance().isTagExists(jsonObj, "sss_event_time_id"));
			objBarEventDateTime.setEventdate(Utils.getInstance().isTagExists(jsonObj, "eventdate"));
			objBarEventDateTime.setEventstarttime(Utils.getInstance().isTagExists(jsonObj, "eventstarttime"));
			objBarEventDateTime.setEventendtime(Utils.getInstance().isTagExists(jsonObj, "eventendtime"));
			objBarEventDateTime.setEvent_id(Utils.getInstance().isTagExists(jsonObj, "event_id"));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return objBarEventDateTime;
	}
	
	/* A method is used to add the bar object. */
	public Bar addBarObject(JSONObject jsonObj) {
		Bar objBar = new Bar();
		try {
			objBar.setId(Utils.getInstance().isTagExists(jsonObj, "bar_id"));
			objBar.setType(Utils.getInstance().isTagExists(jsonObj, "bar_type"));
			objBar.setTitle(Utils.getInstance().isTagExists(jsonObj, "bar_title"));
			objBar.setAddress(Utils.getInstance().isTagExists(jsonObj, "address"));
			objBar.setCity(Utils.getInstance().isTagExists(jsonObj, "city"));
			objBar.setState(Utils.getInstance().isTagExists(jsonObj, "state"));
			objBar.setZipcode(Utils.getInstance().isTagExists(jsonObj, "zipcode"));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return objBar;
	}
	
	/* A method is used to add the Taxi's object. */
	public Taxi addTaxiObject(JSONObject jsonObj) {
		Taxi objTaxi = new Taxi();
		try {
			objTaxi.setTaxi_id(Utils.getInstance().isTagExists(jsonObj, "taxi_id"));
			objTaxi.setTaxi_company(Utils.getInstance().isTagExists(jsonObj, "taxi_company"));
			objTaxi.setAddress(Utils.getInstance().isTagExists(jsonObj, "address"));
			objTaxi.setCity(Utils.getInstance().isTagExists(jsonObj, "city"));
			objTaxi.setState(Utils.getInstance().isTagExists(jsonObj, "state"));
			objTaxi.setPhone_number(Utils.getInstance().isTagExists(jsonObj, "phone_number"));
			objTaxi.setStatus(Utils.getInstance().isTagExists(jsonObj, "status"));
			objTaxi.setCmpn_zipcode(Utils.getInstance().isTagExists(jsonObj, "cmpn_zipcode"));
			objTaxi.setTaxi_image(Utils.getInstance().isTagExists(jsonObj, "taxi_image"));
			objTaxi.setTaxi_desc(Utils.getInstance().isTagExists(jsonObj, "taxi_desc"));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return objTaxi;
	}
	
	/* A method is used to add the Happy Hour's object. */
	public HappyHours addHappyHoursObject(JSONObject jsonObj) {
		HappyHours objHappyHours = new HappyHours();
		try {
			objHappyHours.setBar_id(Utils.getInstance().isTagExists(jsonObj, "bar_id"));
			objHappyHours.setHour_from(Utils.getInstance().isTagExists(jsonObj, "hour_from"));
			objHappyHours.setHour_to(Utils.getInstance().isTagExists(jsonObj, "hour_to"));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return objHappyHours;
	}
	/* A method is used to add the Album's object. */
	/*public Album addAlbumObject(JSONObject jsonObj) {
		Album objAlbum = new Album();
		try {
			objAlbum.setBar_gallery_id(Utils.getInstance().isTagExists(jsonObj, "bar_gallery_id"));
			objAlbum.setBar_id(Utils.getInstance().isTagExists(jsonObj, "bar_id"));
			objAlbum.setTitle(Utils.getInstance().isTagExists(jsonObj, "title"));
			objAlbum.setDescription(Utils.getInstance().isTagExists(jsonObj, "description"));
			objAlbum.setDate_added(Utils.getInstance().isTagExists(jsonObj, "date_added"));
			objAlbum.setGallery(Utils.getInstance().isTagExists(jsonObj, "gallery"));
			objAlbum.setStatus(Utils.getInstance().isTagExists(jsonObj, "status"));
			objAlbum.setReorder(Utils.getInstance().isTagExists(jsonObj, "reorder"));
			objAlbum.setSelected(false);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return objAlbum;
	}*/
}
