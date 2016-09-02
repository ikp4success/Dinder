package com.independentprojectcis470.dinderv2;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by graham on 11/23/2015.
 */
public class GlobalVariables extends Application {

	private List<String> titles;
	private List<String> addresses;

	public GlobalVariables(){
		this.titles = new ArrayList<String>();
		this.addresses = new ArrayList<String>();
	}

	public List<String> getTitles(){
		return this.titles;
	}

	public List<String> getAddresses(){
		return this.addresses;
	}

	public void addToLikedRestaurantList(String title, String address){
		if(this.titles == null || this.addresses == null) {
			this.titles = new ArrayList<String>();
			this.addresses = new ArrayList<String>();
		}
		this.titles.add(title);
		this.addresses.add(address);
	}
}
