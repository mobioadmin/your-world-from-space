package com.mobioapp.yourworldfromspace.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.senab.photoview.PhotoViewAttacher;

import com.mobioapp.yourworldfromspace.R;
import com.mobioapp.yourworldfromspace.objects.Photo;
import com.mobioapp.yourworldfromspace.utils.Constants;
import com.mobioapp.yourworldfromspace.utils.DetectConnection;
import com.mobioapp.yourworldfromspace.utils.JSONParser;
import com.mobioapp.yourworldfromspace.utils.URLs;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WeekImageFragment extends Fragment {
	// public static final String ARG_PLANET_NUMBER = "planet_number";
	PhotoViewAttacher mAttacher;
	ImageView iv;
	boolean success;
	JSONParser jparser;
	Photo photo = null;
	DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	TextView tv1, tv2, tv3;

	public WeekImageFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_imageweek,
				container, false);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		jparser = new JSONParser();

		iv = (ImageView) getActivity().findViewById(R.id.week_imageView1);

		tv1 = (TextView) getActivity().findViewById(R.id.cap_textView1);
		//tv2 = (TextView)getActivity().findViewById(R.id.des_textView1);
		tv3 = (TextView) getActivity().findViewById(R.id.likes_textView3);

		mAttacher = new PhotoViewAttacher(iv);

		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		if (DetectConnection.checkInternetConnection(getActivity())) {
			ImageAsyncTask iAT = new ImageAsyncTask();
			iAT.execute();
		} else {
			// showDialog(ImageWeekActivity.this);
			Toast.makeText(getActivity(), "No Internet!", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/*
	 * AsyncTask to download categories
	 */
	private class ImageAsyncTask extends AsyncTask<Void, Void, String> {
		//String[] status = null;
		// private final Context context;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {

			try {
				dialog = ProgressDialog.show(getActivity(), "",
						"Loading Image...", true);
				// Constants.all_images.clear();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		@Override
		protected String doInBackground(Void... ignored) {

			try {
				JSONObject jobj = null;
				JSONObject json = new JSONObject();
				JSONArray valArray = null;
				// String url = Constants.imgUrl + cat_id;

				json = jparser.getJSONFromUrl(URLs.weekUrl);
				// System.out.println("Json Object::: " + json.toString());
				valArray = json.getJSONArray("images");

				jobj = valArray.getJSONObject(0);
				photo = new Photo(jobj.getString("id"), jobj.getString("link"),
						jobj.getString("caption"), jobj.getString("likes"), jobj.getString("description"));
				// Constants.all_images.add(photo);

				success = true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Json Exception");
				success = false;
			}

			return "ok";
		}

		@Override
		protected void onPostExecute(String message) {
			if (dialog != null) {
				dialog.dismiss();
			}
			if (success) {
				tv1.setText(photo.getCaption());
				//tv2.setText(photo.getDescription());
				tv3.setText("Total Likes: " + photo.getLikes());
				imageLoader.displayImage(photo.getLink(), iv, options, null);
			} else {

				Toast.makeText(getActivity(), "No Image!", Toast.LENGTH_SHORT)
						.show();
			}

		}
	}

}
