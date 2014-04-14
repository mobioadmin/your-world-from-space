package com.mobioapp.artisticearth.utils;

import java.util.ArrayList;
import java.util.List;

public class SampleData {

    public static final int SAMPLE_DATA_ITEM_COUNT = 30;

    public static ArrayList<String> generateImageData() {
      
        final ArrayList<String> imageData = new ArrayList<String>();
        
        System.out.println("Array Length::" +Constants.IMAGES.length);
        for(int i =0; i<Constants.all_images.size(); i++){
        	imageData.add(Constants.all_images.get(i).getLink());
        	System.out.println(Constants.all_images.get(i).getLink());
        }
        return imageData;
    }
    
    
    public static ArrayList<String> generateTextData() {
        final ArrayList<String> data = new ArrayList<String>(SAMPLE_DATA_ITEM_COUNT);
  
       
        for (int i = 0; i < Constants.all_images.size(); i++) {
            data.add(Constants.all_images.get(i).getCaption());
        }
    
        

        return data;
    }

}
