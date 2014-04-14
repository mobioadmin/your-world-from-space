package com.mobioapp.artisticearth;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.mobioapp.artisticearth.adapters.ImageListAdapter;
import com.mobioapp.artisticearth.objects.Photo;
import com.mobioapp.artisticearth.utils.Constants;
import com.mobioapp.artisticearth.utils.Constants.Extra;
import com.mobioapp.artisticearth.utils.DetectConnection;
import com.mobioapp.artisticearth.utils.JSONParser;
import com.mobioapp.artisticearth.utils.URLs;
import com.mobioapp.earthasart.R;

public class ImageListActivity extends Activity {

	// String[] imageUrls;
	GridView listView;
	JSONParser parser;
	JSONObject json_obj;
	ProgressDialog pDialog;
	JSONArray jArray_accept;
	List<Photo> all_photos = new ArrayList<Photo>();
	Photo photo = null;
	JSONParser jparser;
	String cat_id = "";

	boolean success;
	ImageListAdapter adapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_grid);

		jparser = new JSONParser();
		cat_id = getIntent().getStringExtra("cat");

		System.out.println("Category Id:::" + cat_id);

		listView = (GridView) findViewById(R.id.listView2);
		adapter = new ImageListAdapter(ImageListActivity.this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startImagePagerActivity(position);
			}
		});

		if (DetectConnection.checkInternetConnection(ImageListActivity.this)) {
			ImageAsyncTask it = new ImageAsyncTask();
			it.execute();
		} else {
			showDialog(ImageListActivity.this);
		}

	}

	private void startImagePagerActivity(int position) {
		Intent intent = new Intent(this, ImageViewActivity.class);
		intent.putExtra(Extra.IMAGES, Constants.IMAGES);
		intent.putExtra(Extra.IMAGE_POSITION, position);
		startActivity(intent);
	}

	public void showDialog(Context c) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);

		// set title
		alertDialogBuilder.setTitle("Alert");

		// set dialog message
		alertDialogBuilder.setMessage("No Internet Connection!")
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						finish();
					}
				});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	/*
	 * AsyncTask to download categories
	 */
	private class ImageAsyncTask extends AsyncTask<Void, Void, String> {
		String[] status = null;
		// private final Context context;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {

			try {
				dialog = ProgressDialog.show(ImageListActivity.this, "",
						"Loading Images...", true);
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
				String url = URLs.imgUrl + cat_id;
				System.out.println("Url: " + url);
				json = jparser.getJSONFromUrl(url);
				System.out.println("Json Object::: " + json.toString());
				valArray = json.getJSONArray("images");
				System.out.println("Cat:::" + cat_id + ":::::"
						+ valArray.length());

				for (int i = 0; i < valArray.length(); i++) {
					jobj = valArray.getJSONObject(i);
					photo = new Photo(jobj.getString("id"),
							jobj.getString("link"), jobj.getString("caption"),
							jobj.getString("likes"));
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
				Toast.makeText(ImageListActivity.this, "No Images!",
						Toast.LENGTH_SHORT).show();
			}

			// json = null;
			// adapter.notifyDataSetChanged();
			// listView.invalidate();
		}
	}

}