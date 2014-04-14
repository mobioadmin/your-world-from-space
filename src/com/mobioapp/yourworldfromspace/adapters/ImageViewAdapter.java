package com.mobioapp.yourworldfromspace.adapters;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobioapp.yourworldfromspace.R;
import com.mobioapp.yourworldfromspace.objects.Photo;
import com.mobioapp.yourworldfromspace.utils.Constants;
import com.mobioapp.yourworldfromspace.utils.URLs;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ImageViewAdapter extends PagerAdapter {

	// private String[] images;
	LayoutInflater inflater;
	Context context;
	DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	ViewPager pager;
	List<Photo> all_images = new ArrayList<Photo>();
	PhotoViewAttacher mAttacher;
	//int pos;
	String response;
	ProgressDialog pDialog;
	String image_id;

	public ImageViewAdapter(Context context) {
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		all_images = Constants.all_images;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public int getCount() {
		return all_images.size();
	}

	@Override
	public Object instantiateItem(ViewGroup view, final int position) {
		View imageLayout = inflater.inflate(R.layout.item_pager_image, view,
				false);
		assert imageLayout != null;
		final ImageView imageView = (ImageView) imageLayout
				.findViewById(R.id.image);
		/*
		 * final ProgressBar spinner = (ProgressBar) imageLayout
		 * .findViewById(R.id.loading);
		 */

		//pos = position;

		TextView caption = (TextView) imageLayout
				.findViewById(R.id.caption_textView2);
		TextView likes = (TextView) imageLayout
				.findViewById(R.id.like_textView2);

		Button details = (Button) imageLayout.findViewById(R.id.more_button3);
		Button like = (Button) imageLayout.findViewById(R.id.like_button1);
		Button share = (Button) imageLayout.findViewById(R.id.share_button2);

		details.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// custom dialog
				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.details_dialog);
				dialog.setTitle("Details");

				// set the custom dialog components - text, image and button
				TextView text = (TextView) dialog
						.findViewById(R.id.details_textmsg);
				text.setText(all_images.get(position).getDescription());

				dialog.show();
			}
		});

		like.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				image_id = all_images.get(position).getId();
				likeImage li = new likeImage();
				li.execute();
			}
		});

		share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bitmap bi = ((BitmapDrawable) imageView.getDrawable())
						.getBitmap();
				Intent share = new Intent(Intent.ACTION_SEND);
				share.setType("image/jpeg");
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				bi.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
				File f = new File(Constants.sd_path + "shared_image.jpg");
				try {
					f.createNewFile();
					FileOutputStream fo = new FileOutputStream(f);
					fo.write(bytes.toByteArray());
					fo.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
				context.startActivity(Intent
						.createChooser(share, "Share Image"));
			}
		});

		caption.setText(all_images.get(position).getCaption());
		likes.setText("Total Likes: " + all_images.get(position).getLikes());

		mAttacher = new PhotoViewAttacher(imageView);

		imageLoader.displayImage(all_images.get(position).getLink(), imageView,
				options, null);

		view.addView(imageLayout, 0);
		return imageLayout;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	private class likeImage extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Sending Like...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {

				HttpClient Client = new DefaultHttpClient();

				String URL = URLs.likeUrl + image_id;
				
				System.out.println("LIKE:::"+URL);

				// Create Request to server and get response

				HttpGet httpget = new HttpGet(URL);
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				response = Client.execute(httpget, responseHandler);
			} catch (Exception e) {
				// System.out.println(e.toString());
				response = "";
			}
			System.out.println("Response:::" + response);
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (response.contentEquals("Success")) {
				Toast.makeText(context, "Image Liked!", Toast.LENGTH_SHORT)
						.show();

			} else {
				Toast.makeText(context, "Couldn't Like!", Toast.LENGTH_SHORT)
						.show();
			}

		}
	}
}