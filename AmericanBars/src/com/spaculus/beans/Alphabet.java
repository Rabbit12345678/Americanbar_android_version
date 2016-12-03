package com.spaculus.beans;

public class Alphabet {
	
	//  Private Variables
    private String name = "";
    private boolean selected = false;

   public Alphabet(String name, boolean selected) {
	// TODO Auto-generated constructor stub
	   this.name = name;
	   this.selected = selected;
   }

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
