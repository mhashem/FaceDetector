package com.ions.facedetector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * @author mhashem on 2/5/16.
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static final String APP_PICTURES_DIR = Environment.DIRECTORY_PICTURES.concat("/FaceDetector/");

    /**
     * @return File
     */
    public static File getAppPicturesStorageDirectory() {
        try {
            File storageDir = Environment.getExternalStoragePublicDirectory(APP_PICTURES_DIR);
            if (!storageDir.exists()) {
                if (storageDir.mkdirs()) {
                    Log.d(TAG, "storage directories created successfully");
                } else {
                    Log.w(TAG, "failed to create storage directory");
                }
            }
            return storageDir;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    /**
     * @return File
     */
    public static File createImageFile(String imageName) {
        Log.d(TAG, "attempting to create image file");
        try {
            return new File(getAppPicturesStorageDirectory().getAbsolutePath().concat("/").concat(imageName));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    public static String getPicturePath(String imageName) {
        try {
            File image = new File(getAppPicturesStorageDirectory().getAbsolutePath().concat("/").concat(imageName));
            if (image.exists()) {
                return image.getAbsolutePath();
            } else {
                Log.w(TAG, "image ".concat(imageName).concat(" doesn't exist"));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    public static File getPictureFile(String imageName) {
        try {
            File image = new File(getAppPicturesStorageDirectory().getAbsolutePath().concat("/").concat(imageName));
            if (image.exists()) {
                return image;
            } else {
                Log.w(TAG, "image ".concat(imageName).concat(" doesn't exist"));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    public static Bitmap getScaledBitmap(String imageName) {
        // Get the dimensions of the View
        int targetW = 300; //mCapturedImageView.getWidth();
        int targetH = 200; //mCapturedImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(getPicturePath(imageName), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(getPicturePath(imageName), bmOptions);
    }

    //    "contempt": 5.14648448E-08,
    //    "disgust": 9.180124E-06,
    //    "sadness": 1.889955E-05,
    //    "fear": 0.0001912825,
    //    "neutral": 0.0009861537,
    //    "anger": 0.00300731952,
    //    "surprise": 0.008229999
    //    "happiness": 0.9875571,
    public static String describeEmotion(double val) {

        if(val >= 0.98) {
            return "seems happy!";
        }

        if(val >= 0.008) {
            return "seems surprised!";
        }

        if(val >= 0.003) {
            return "seems angry!";
        }

        if(val >= 0.0009) {
            return "neutral";
        }

        if(val >= 0.00019) {
            return "fear!";
        }

        if(val >= 0.0000018) {
            return "why sad?";
        }

        if(val >= 9.1E06) {
            return "disgust!";
        }

        if(val >= 5.14E08) {
            return "contempt!";
        }


        return "I can't realise!";
    }

}
