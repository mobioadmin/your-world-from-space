package com.mobioapp.yourworldfromspace.fragments;

import com.mobioapp.yourworldfromspace.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutUsFragment extends Fragment {
	//public static final String ARG_PLANET_NUMBER = "planet_number";

	public AboutUsFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_aboutus,
				container, false);
	
		return rootView;
	}
}
