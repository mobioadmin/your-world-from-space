package com.mobioapp.artisticearth;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.mobioapp.artisticearth.adapters.ImageViewAdapter;
import com.mobioapp.artisticearth.utils.Constants.Extra;
import com.mobioapp.earthasart.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageViewActivity extends Activity {

	private static final String STATE_POSITION = "STATE_POSITION";

	DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	ViewPager pager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_pager);

		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		//String[] imageUrls = bundle.getStringArray(Extra.IMAGES);
		int pagerPosition = bundle.getInt(Extra.IMAGE_POSITION, 0);

		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ImageViewAdapter(ImageViewActivity.this));
		pager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}


}