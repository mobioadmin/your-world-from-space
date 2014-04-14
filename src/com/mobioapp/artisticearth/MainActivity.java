package com.mobioapp.artisticearth;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mobioapp.artisticearth.adapters.MainListAdapter;
import com.mobioapp.artisticearth.objects.Category;
import com.mobioapp.artisticearth.utils.Constants;
import com.mobioapp.artisticearth.utils.DetectConnection;
import com.mobioapp.artisticearth.utils.JSONParser;
import com.mobioapp.artisticearth.utils.URLs;
import com.mobioapp.earthasart.R;

public class MainActivity extends Activity {

	ListView lv;
	MainListAdapter adapter;
	List<Category> cats = new ArrayList<Category>();
	Category cat;
	JSONParser jparser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		jparser = new JSONParser();

		lv = (ListView) findViewById(R.id.listView1);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Constants.all_images.clear();
				Intent i = new Intent(MainActivity.this,
						ImageListActivity.class);
				i.putExtra("cat", cats.get(position).getId());
				i.putExtra("cat_name", cats.get(position).getCaption());
				startActivity(i);
			}
		});

		if (DetectConnection.checkInternetConnection(MainActivity.this)) {
			CategoryAsyncTask it = new CategoryAsyncTask();
			it.execute();
		} else {
			showDialog(MainActivity.this);
		}

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
						MainActivity.this.finish();
					}
				});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {

		case R.id.action_about:
			Intent intent = new Intent(this, AboutUsActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * AsyncTask to download categories
	 */
	private class CategoryAsyncTask extends AsyncTask<Void, Void, String> {
		String[] status = null;
		// private final Context context;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {

			try {
				dialog = ProgressDialog.show(MainActivity.this, "",
						"Loading Categories...", true);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		@Override
		protected String doInBackground(Void... ignored) {

			try {
				JSONObject json = jparser.getJSONFromUrl(URLs.catUrl);
				JSONArray valArray = json.getJSONArray("category");
				JSONObject jobj;
				for (int i = 0; i < valArray.length(); i++) {
					jobj = valArray.getJSONObject(i);
					if (!jobj.getString("count").contentEquals("0")) {
						cat = new Category(jobj.getString("id"),
								jobj.getString("image"), "",
								jobj.getString("name"), jobj.getString("count"));
						cats.add(cat);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return "ok";
		}

		@Override
		protected void onPostExecute(String message) {
			if (dialog != null) {
				dialog.dismiss();
			}
			adapter = new MainListAdapter(MainActivity.this, cats);
			lv.setAdapter(adapter);
		}
	}

}
