package com.mobioapp.yourworldfromspace.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.mobioapp.yourworldfromspace.R;
import com.mobioapp.yourworldfromspace.adapters.ImageListAdapter;
import com.mobioapp.yourworldfromspace.objects.Photo;
import com.mobioapp.yourworldfromspace.utils.Constants;
import com.mobioapp.yourworldfromspace.utils.Constants.Extra;
import com.mobioapp.yourworldfromspace.utils.DetectConnection;
import com.mobioapp.yourworldfromspace.utils.JSONParser;
import com.mobioapp.yourworldfromspace.utils.URLs;

public class ImageListFragment extends Fragment {


	// public static final String ARG_PLANET_NUMBER = "planet_number";
	ListView listView;
	JSONParser parser;
	JSONObject json_obj;
	//ProgressDialog pDialog;
	Photo photo = null;
	JSONParser jparser;
	String cat_id = "";
	JSONObject jobj = null;
	JSONObject json = new JSONObject();
	JSONArray valArray = null;

	boolean success;
	ImageListAdapter adapter = null;

	public ImageListFragment() {
		// Empty constructor required for fragment subclasses
		adapter = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_allimages,
				container, false);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		jparser = new JSONParser();
		// cat_id = getIntent().getStringExtra("cat");
		//cat_id = "1";
		// System.out.println("Category Id:::" + cat_id);

		listView = (ListView) getActivity().findViewById(R.id.listView2);
		adapter = new ImageListAdapter(getActivity());
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startImagePagerFragment(position);
			}
		});

		if (DetectConnection.checkInternetConnection(getActivity())) {
			ImageAsyncTask it = new ImageAsyncTask();
			it.execute();
		} else {
			// showDialog(ImageListActivity.this);
			Toast.makeText(getActivity(), "No Internet!", Toast.LENGTH_SHORT)
					.show();
		}
	}
	
	

	private void startImagePagerFragment(int position) {
		Fragment fragment = new ImageViewFragment();
		Bundle args = new Bundle();
		args.putInt(Extra.IMAGE_POSITION, position);
		fragment.setArguments(args);
		 //transaction.addToBackStack(null);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
	}

	/*
	 * AsyncTask to download categories
	 */
	private class ImageAsyncTask extends AsyncTask<Void, Void, String> {
		// private final Context context;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {

			try {
				dialog = ProgressDialog.show(getActivity(), "",
						"Loading Images...", true);
				//clearApplicationData();
				Constants.all_images.clear();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		@Override
		protected String doInBackground(Void... ignored) {

			try {
				
				String url = URLs.imgUrl;

				json = jparser.getJSONFromUrl(url);

				valArray = json.getJSONArray("images");

				for (int i = 0; i < valArray.length(); i++) {
					jobj = valArray.getJSONObject(i);
					photo = new Photo(jobj.getString("id"),
							jobj.getString("link"), jobj.getString("caption"),
							jobj.getString("likes"), "");
					
					//System.out.println("PHOTO:::"+jobj.getString("id")+":::"+jobj.getString("likes"));
					Constants.all_images.add(photo);
				}
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
				listView.setAdapter(adapter);
				
			} else {
				listView.setAdapter(null);
				Toast.makeText(getActivity(), "No Images!", Toast.LENGTH_SHORT)
						.show();
			}

			// json = null;
			//adapter.notifyDataSetChanged();
			listView.invalidate();
		}
	}
	


}
