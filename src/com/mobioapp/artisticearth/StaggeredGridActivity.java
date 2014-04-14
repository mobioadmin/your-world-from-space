package com.mobioapp.artisticearth;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.fahim.staggeredgridview.StaggeredGridView;
import com.mobioapp.artisticearth.objects.Photo;
import com.mobioapp.artisticearth.utils.Constants;
import com.mobioapp.artisticearth.utils.JSONParser;
import com.mobioapp.artisticearth.utils.StaggeredGridAdapter;
import com.mobioapp.artisticearth.utils.SampleData;
import com.mobioapp.artisticearth.utils.Constants.Extra;
import com.mobioapp.artisticearth.utils.URLs;
import com.mobioapp.earthasart.R;

public class StaggeredGridActivity extends Activity implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener {

    private static final String TAG = "StaggeredGridActivity";
    public static final String SAVED_DATA_KEY = "SAVED_DATA";

    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private StaggeredGridAdapter mAdapter;

    JSONParser jparser;
    boolean success;
    String cat_id="";
    String cat_name="";
    Photo photo = null;
    
    private ArrayList<String> imgData;
    private ArrayList<String> txtData;

    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgv);

		jparser = new JSONParser();
		cat_id = getIntent().getStringExtra("cat");
		cat_name = getIntent().getStringExtra("cat_name");
		
        setTitle("SGV");
        mGridView = (StaggeredGridView) findViewById(R.id.grid_view);

        LayoutInflater layoutInflater = getLayoutInflater();

        View header = layoutInflater.inflate(R.layout.list_item_header_footer, null);
        View footer = layoutInflater.inflate(R.layout.list_item_header_footer, null);
        TextView txtHeaderTitle = (TextView) header.findViewById(R.id.txt_title);
        TextView txtFooterTitle =  (TextView) footer.findViewById(R.id.txt_title);
        txtHeaderTitle.setText(cat_name);
        txtFooterTitle.setText("");

        mGridView.addHeaderView(header);
        mGridView.addFooterView(footer);
        
        // do we have saved data?
        if (savedInstanceState != null) {
            imgData = savedInstanceState.getStringArrayList(SAVED_DATA_KEY);
            txtData = savedInstanceState.getStringArrayList(SAVED_DATA_KEY);
        }
        
        ImageAsyncTask imgTask  = new ImageAsyncTask();
        imgTask.execute();
    }

//    @Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.menu_sgv_dynamic, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//			case R.id.col1:
//				mGridView.setColumnCount(1);
//				break;
//			case R.id.col2:
//				mGridView.setColumnCount(2);
//				break;
//			case R.id.col3:
//				mGridView.setColumnCount(3);
//				break;
//		}
//		return true;
//	}
	
    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(SAVED_DATA_KEY, imgData);
    }

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        Log.d(TAG, "onScrollStateChanged:" + scrollState);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem +
                            " visibleItemCount:" + visibleItemCount +
                            " totalItemCount:" + totalItemCount);
        // our handling
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d(TAG, "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
 //               onLoadMoreItems();
            }
        }
    }

//    private void onLoadMoreItems() {
//        final ArrayList<String> sampleData = SampleData.generateImageData();
//        for (String data : sampleData) {
//            mAdapter.add(data);
//        }
//        // stash all the data in our backing store
//        imgData.addAll(sampleData);
//        // notify the adapter that we can update now
//        mAdapter.notifyDataSetChanged();
//        mHasRequestedMore = false;
//    }

    private class ImageAsyncTask extends AsyncTask<Void, Void, String> {
		String[] status = null;
		// private final Context context;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {

			try {
				dialog = ProgressDialog.show(StaggeredGridActivity.this, "",
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
					System.out.println("ImageUrl: "+ jobj.getString("link"));
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

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(String message) {
			if (dialog != null) {
				dialog.dismiss();
			}
			if (success) {
				//listView.setAdapter(adapter);
				//mAdapter = new SampleAdapter(StaggeredGridActivity.this, R.id.txt_line1, R.id.img_cell, Constants.all_images);
				//mGridView.setAdapter(mAdapter);
				
				if (imgData == null) {
		            imgData = SampleData.generateImageData();
		            txtData = SampleData.generateTextData();
		        }

		      

		        
		        mAdapter = new StaggeredGridAdapter(StaggeredGridActivity.this, R.id.txt_line1,  R.id.img_cell, imgData, txtData);
		       
		        
		        for (String data : imgData) {
		            mAdapter.add(data);
		        }
		        
		        mGridView.setAdapter(mAdapter);
		        mGridView.setOnScrollListener(StaggeredGridActivity.this);
		        mGridView.setOnItemClickListener(StaggeredGridActivity.this);
				
			} else {
				mGridView.setAdapter(null);
				Toast.makeText(StaggeredGridActivity.this, "No Images!",
						Toast.LENGTH_SHORT).show();
			}

			// json = null;
			// adapter.notifyDataSetChanged();
			// listView.invalidate();
		}
	}
    
    
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
       // Toast.makeText(this, "Item Clicked: " + position, Toast.LENGTH_SHORT).show();
//    	Intent i = new Intent(StaggeredGridActivity.this, ImageViewActivity.class);
//    	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//    	startActivity(i);
    	startImagePagerActivity(position);
    }
    
	private void startImagePagerActivity(int position) {
		Intent intent = new Intent(this, ImageViewActivity.class);
		//intent.putExtra(Extra.IMAGES, Constants.IMAGES);
		intent.putExtra(Extra.IMAGE_POSITION, position-1);
		startActivity(intent);
	}
}
