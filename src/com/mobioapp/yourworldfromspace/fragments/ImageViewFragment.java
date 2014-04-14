package com.mobioapp.yourworldfromspace.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobioapp.yourworldfromspace.R;
import com.mobioapp.yourworldfromspace.adapters.ImageViewAdapter;
import com.mobioapp.yourworldfromspace.utils.Constants.Extra;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageViewFragment extends Fragment {

	private static final String STATE_POSITION = "STATE_POSITION";

	DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	ViewPager pager;

	
	public ImageViewFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_imagepager, container,
				false);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		

		//Bundle bundle = getIntent().getExtras();
		//assert bundle != null;
		
		//int pagerPosition = bundle.getInt(Extra.IMAGE_POSITION, 0);
		 int pagerPosition = getArguments().getInt(Extra.IMAGE_POSITION);

		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		

		pager = (ViewPager) getActivity().findViewById(R.id.pager);
		pager.setAdapter(new ImageViewAdapter(getActivity()));
		pager.setCurrentItem(pagerPosition);
		
	
		
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}
	
}
