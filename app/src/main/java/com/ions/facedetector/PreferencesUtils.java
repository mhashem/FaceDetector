package com.ions.facedetector;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author mhashem on 2/5/16.
 */
public class PreferencesUtils {

    public static final String PREFS_NAME = "face_detector_prefs";
    public static final String CAPTURED_IMAGE_NAME = "captured_image_name";
    public static final String RESULT_IMAGE_NAME = "result_image_name";

    public static void saveCapturedImageName(Context context, String imageFileName) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CAPTURED_IMAGE_NAME, imageFileName);
        editor.commit();
    }

    public static String getCapturedImageName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(CAPTURED_IMAGE_NAME, null);
    }

}
