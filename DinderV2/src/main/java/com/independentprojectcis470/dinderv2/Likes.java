package com.independentprojectcis470.dinderv2;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

/**
 * Created by graham on 11/21/2015.
 */
public class Likes extends ListFragment {

	private View rootView;

	private ListView listV;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);

		LinearLayout wrapper = new LinearLayout(getActivity());
		rootView = inflater.inflate(R.layout.activity_likes, wrapper, false);

		listV = (ListView) rootView.findViewById(android.R.id.list);

		return rootView;
	}

	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);

		GlobalVariables globVars = ((GlobalVariables)getContext().getApplicationContext());

		//sending the addresses to the adapter to be used to retrieve titles from hashmap passed
		List<String> vals = globVars.getTitles();
		String[] values = vals.toArray(new String[0]);

		RestAdapter rest = new RestAdapter(this.getActivity(), values, globVars.getAddresses());

		listV.setAdapter(rest);
	}
}
