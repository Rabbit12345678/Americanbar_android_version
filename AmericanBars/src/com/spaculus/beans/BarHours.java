package com.spaculus.beans;

public class BarHours {
	
	//  Private Variables
	private String days = "";
	private String start_from = "";
	private String start_to = "";
	private String is_colsed ="";
	
	public BarHours(String days, String start_from, String start_to, String is_colsed) {
		// TODO Auto-generated constructor stub
		this.days = days;
		this.start_from = start_from;
		this.start_to = start_to;
		this.is_colsed = is_colsed;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getStart_from() {
		return start_from;
	}

	public void setStart_from(String start_from) {
		this.start_from = start_from;
	}

	public String getStart_to() {
		return start_to;
	}

	public void setStart_to(String start_to) {
		this.start_to = start_to;
	}

	public String getIs_colsed() {
		return is_colsed;
	}

	public void setIs_colsed(String is_colsed) {
		this.is_colsed = is_colsed;
	}
}
