package com.mobioapp.yourworldfromspace.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

import com.mobioapp.yourworldfromspace.objects.Photo;



public final class Constants {
	
	public static String sd_path = Environment.getExternalStorageDirectory() + File.separator;
	
	
	public static List<Photo> all_images = new ArrayList<Photo>();

	

	private Constants() {
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
	
	public static class Extra {
		public static final String IMAGES = "com.nostra13.example.universalimageloader.IMAGES";
		public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	}
}
