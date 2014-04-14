package com.mobioapp.artisticearth.utils;


import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.fahim.staggeredgridview.util.DynamicHeightTextView;
import com.mobioapp.earthasart.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/***
 * ADAPTER
 */

public class StaggeredGridAdapter extends ArrayAdapter<String> {

    private static final String TAG = "SampleAdapter";
	DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new ImageLoaderHelper.AnimateFirstDisplayListener();
	protected ImageLoader imageLoader;

    static class ViewHolder {
        DynamicHeightTextView txtLineOne;
        //Button btnGo;
        ImageView imgView;
    }

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private final ArrayList<Integer> mBackgroundColors;
    private Context context;
    private ArrayList<String> imgData;
    private ArrayList<String> txtData;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public StaggeredGridAdapter(final Context context, final int textViewResourceId, int ImageViewId, ArrayList<String> imgData, ArrayList<String> txtData) {
        super(context, textViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        mBackgroundColors = new ArrayList<Integer>();
        mBackgroundColors.add(R.color.orange);
        mBackgroundColors.add(R.color.green);
        mBackgroundColors.add(R.color.blue);
        mBackgroundColors.add(R.color.yellow);
        mBackgroundColors.add(R.color.grey);
        this.context = context;
        this.imgData = imgData;
        this.txtData = txtData;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder vh;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(context));
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisc(true).displayer(new SimpleBitmapDisplayer())
				.build();
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_sample, parent, false);
            vh = new ViewHolder();
            vh.txtLineOne = (DynamicHeightTextView) convertView.findViewById(R.id.txt_line1);
       //     vh.btnGo = (Button) convertView.findViewById(R.id.btn_go);
            vh.imgView = (ImageView) convertView.findViewById(R.id.img_cell);

            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);
        int backgroundIndex = position >= mBackgroundColors.size() ?
                position % mBackgroundColors.size() : position;

        convertView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));

        Log.d(TAG, "getView position:" + position + " h:" + positionHeight);

        vh.txtLineOne.setHeightRatio(positionHeight);
      //  vh.txtLineOne.setText(getItem(position) + position);
        vh.txtLineOne.setText(txtData.get(position).toString()  + " " + position);
		imageLoader.displayImage(imgData.get(position).toString(),
				vh.imgView, options, animateFirstListener);
		System.out.println("Image Url:" + getItem(position));

//        vh.btnGo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                Toast.makeText(getContext(), "Button Clicked Position " +
//                        position, Toast.LENGTH_SHORT).show();
//            }
//        });

        return convertView;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }
}