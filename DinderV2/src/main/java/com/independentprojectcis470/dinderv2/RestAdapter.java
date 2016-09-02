package com.independentprojectcis470.dinderv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by graham on 11/21/2015.
 */
public class RestAdapter extends ArrayAdapter<String> {
	Context context;
	String[] titles;
	List<String> addresses;

	public RestAdapter(Context context, String[] items, List<String> addresses) {
		super(context, R.layout.custom_likes_layout, items);
		this.context = context;
		this.titles = items;
		this.addresses = addresses;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View row = inflater.inflate(R.layout.custom_likes_layout, null);

		//display recipe of the label is clicked
		TextView label = (TextView) row.findViewById(R.id.label);
		label.setText(titles[position]);

		//add click listener to open google maps intent with address of restaurant
		ImageView mapsIcon = (ImageView) row.findViewById(R.id.maps);
		mapsIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(addresses.get(position)));
				Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
				mapIntent.setPackage("com.google.android.apps.maps");
				context.startActivity(mapIntent);
			}
		});

		return (row);
	}
}