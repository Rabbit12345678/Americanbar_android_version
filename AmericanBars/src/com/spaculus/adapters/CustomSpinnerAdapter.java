package com.spaculus.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private int hidingItemIndex;

    public CustomSpinnerAdapter(Context context, int textViewResourceId, ArrayList<String> objects, int hidingItemIndex) {
        super(context, textViewResourceId, objects);
        this.hidingItemIndex = hidingItemIndex;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = null;
        
        try {
			if (position == hidingItemIndex) {
			    TextView textView = new TextView(getContext());
			    textView.setVisibility(View.GONE);
			    view = textView;
			} else {
			    view = super.getDropDownView(position, null, parent);
			}
		} 
        catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return view;
    }
}
