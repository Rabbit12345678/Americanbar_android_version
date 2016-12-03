package com.spaculus.beans;

public class Cocktail {

	//  Private Variables
	private String id = "";
	private String name = "";
	private String ingredients = "";
	private String type = "";
	private String served = "";
	private String difficulty = "";
	private String status = "";
	private String cocktail_image = "";
	private String strength = "";
	private String date_added = "";
	private boolean selected = false;

	/* For the Cocktail Search Listing Screen */
	public Cocktail(String id, String name, String ingredients, String type, String served, String difficulty, String status, String cocktail_image) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.name = name;
		this.ingredients =ingredients;
		this.type = type;
		this.served = served;
		this.difficulty = difficulty;
		this.status = status;
		this.cocktail_image = cocktail_image;
	}
	
	/* For the Full Mug Bar Details Screen */
	public Cocktail(String id, String name, String type, String served, String strength, String cocktail_image) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.name = name;
		this.type = type;
		this.served = served;
		this.strength = strength;
		this.cocktail_image = cocktail_image;
	}

	/* For the My Favorite Cocktails functionality */
	public Cocktail(String id, String name, String type, String date_added, boolean selected, String cocktail_image) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.name = name;
		this.type = type;
		this.date_added = date_added;
		this.selected = selected;
		this.cocktail_image = cocktail_image;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getServed() {
		return served;
	}

	public void setServed(String served) {
		this.served = served;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCocktail_image() {
		return cocktail_image;
	}

	public void setCocktail_image(String cocktail_image) {
		this.cocktail_image = cocktail_image;
	}

	public String getStrength() {
		return strength;
	}

	public void setStrength(String strength) {
		this.strength = strength;
	}

	public String getDate_added() {
		return date_added;
	}

	public void setDate_added(String date_added) {
		this.date_added = date_added;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
