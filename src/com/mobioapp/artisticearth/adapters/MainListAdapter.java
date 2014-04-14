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

import com.mobioapp.artisticearth.objects.Category;
import com.mobioapp.earthasart.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MainListAdapter extends BaseAdapter {

	DisplayImageOptions options;
	Context context;
	LayoutInflater inflater;
	List<Category> all_cats = new ArrayList<Category>();
	ImageLoader imageLoader;

	public MainListAdapter(Context context, List<Category> all_cats) {
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

		this.all_cats = all_cats;
	}

	@Override
	public int getCount() {
		return all_cats.size();
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
			view = inflater
					.inflate(R.layout.category_row_layout, parent, false);
			holder = new ViewHolder();
			assert view != null;
			holder.imageView = (ImageView) view.findViewById(R.id.cat_image);

			holder.textView = (TextView) view.findViewById(R.id.cat_name);
			holder.textView2 = (TextView) view.findViewById(R.id.cat_count);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.textView.setText(all_cats.get(position).getCaption());
		holder.textView2.setText(all_cats.get(position).getCount());
		imageLoader.displayImage(all_cats.get(position).getImage(),
				holder.imageView, options, null);

		return view;
	}

	class ViewHolder {
		ImageView imageView;
		TextView textView,textView2;
	}
}
