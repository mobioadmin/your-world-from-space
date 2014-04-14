package com.mobioapp.artisticearth.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobioapp.artisticearth.objects.Photo;
import com.mobioapp.artisticearth.utils.Constants;
import com.mobioapp.earthasart.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageListAdapter extends BaseAdapter {

	DisplayImageOptions options;
	Context context;
	LayoutInflater inflater;
	List<Photo> all_images = new ArrayList<Photo>();
	ImageLoader imageLoader;

	public ImageListAdapter(Context context) {
		super();
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		all_images = Constants.all_images;
	}

	@Override
	public int getCount() {
		return all_images.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.image_row_layout, parent, false);
			holder = new ViewHolder();
			assert view != null;
			holder.imageView = (ImageView) view.findViewById(R.id.img_image);
			holder.textView = (TextView) view.findViewById(R.id.img_name);
			holder.textView2 = (TextView) view.findViewById(R.id.img_vote);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.textView.setText(all_images.get(position).getCaption());
		holder.textView2.setText(all_images.get(position).getLikes());
		imageLoader.displayImage(all_images.get(position).getLink(), holder.imageView,
				options, null);

		return view;
	}

	class ViewHolder {
		ImageView imageView;
		TextView textView,textView2;
	}
}
