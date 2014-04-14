
package com.mobioapp.artisticearth.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Class for checking internet connection
 * 
 * @author Fida
 * @version 1.0
 */
public class DetectConnection {
    /*
     * Checking internet connection
     */
    public static boolean checkInternetConnection(Context context) {

        ConnectivityManager con_manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (con_manager.getActiveNetworkInfo() != null
                && con_manager.getActiveNetworkInfo().isAvailable()
                && con_manager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
