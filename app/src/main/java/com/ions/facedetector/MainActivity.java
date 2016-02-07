package com.ions.facedetector;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Simple application to demonstrate the usage of :
 * - implicit intents
 * - working with 3rd party libraries
 * <p/>
 * References:
 * - <a href="http://developer.android.com/training/basics/data-storage/shared-preferences.html">Saving Key-Value Sets</a>
 * - <a href="http://developer.android.com/training/camera/photobasics.html">Taking Photos Simply</a>
 */
public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private final int CAPTURE_IMAGE_CODE = 1;
    private final int PERMISSION_WRITE_EXTERNAL_STORAGE = 2;

    private ProgressDialog mProgressDialog;

    private FloatingActionButton mCameraFab;
    private ImageView mCapturedImageView, mResultImageView;
    private TextView mAgeTextView, mEmotionTextView, mResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // bind Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // bind views
        mCapturedImageView = (ImageView) findViewById(R.id.captured_image_view);
        mResultImageView = (ImageView) findViewById(R.id.result_image_view);
        mAgeTextView = (TextView) findViewById(R.id.age_text_view);
        mEmotionTextView = (TextView) findViewById(R.id.emotion_text_view);
        mResultTextView = (TextView) findViewById(R.id.result_text_view);

        // create ProgressDialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.progress_dialog_title));


        mCameraFab = (FloatingActionButton) findViewById(R.id.fab);
        mCameraFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * Using and implicit intent to launch the Camera and take a picture
                 */
                Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                /**
                 * Ensure that there's a camera activity to handle the intent
                 */
                if (captureImageIntent.resolveActivity(getPackageManager()) != null) {

                    String imageFileName = new SimpleDateFormat("yyyyMMddhhmm'.jpg'", Locale.getDefault()).format(new Date());
                    File photoFile = Utils.createImageFile(imageFileName);
                    PreferencesUtils.saveCapturedImageName(MainActivity.this, imageFileName);

                    /**
                     * Continue only if the File was successfully created
                     */
                    if (photoFile != null) {

                        /**
                         * Save captured image to the file specified
                         */
                        captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

                        /**
                         * The result will be handled
                         */
                        startActivityForResult(captureImageIntent, CAPTURE_IMAGE_CODE);
                    } else {
                        Log.w(TAG, "Failed to create photo file");
                    }
                }
            }
        });

        /**
         * Android 6.0 Permissions
         *
         * make sure storage permission is granted to continue work
         */
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // only for marshmallow and newer versions
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Snackbar.make(mCameraFab, getString(R.string.permission_say_why), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.grant), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
                                }
                            }).show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.d(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() fired");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() fired");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() fired");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() fired");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() fired");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CAPTURE_IMAGE_CODE) {
            Uri photoTakenUri = Uri.fromFile(Utils.getPictureFile(PreferencesUtils.getCapturedImageName(this)));

            Log.d(TAG, photoTakenUri.getAuthority());

            Bitmap imageBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(photoTakenUri, getContentResolver());
            mCapturedImageView.setImageBitmap(imageBitmap);

            //mCapturedImageView.setImageBitmap(Utils.getScaledBitmap(PreferencesUtils.getCapturedImageName(this)));
            new DetectionTask().execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private class DetectionTask extends AsyncTask<Void, String, Face[]> {

        private boolean mSucceed = true;

        private Bitmap imageBitmap;

        private long startTime = 0, stopTime = 0;

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            setButtonsEnabledStatus(false);
        }

        @Override
        protected Face[] doInBackground(Void... params) {
            // Get an instance of face service client to detect faces in image.

            startTime = SystemClock.currentThreadTimeMillis();

            String capturedImageName = PreferencesUtils.getCapturedImageName(MainActivity.this);
            imageBitmap = Utils.getScaledBitmap(capturedImageName);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            FaceServiceClient faceServiceClient = new FaceServiceRestClient(BuildConfig.FACE_DETECTION_KEY);

            try {
                publishProgress("Detecting...");

                // Start detection.
                return faceServiceClient.detect(
                        inputStream,  /* Input stream of image to detect */
                        true,       /* Whether to return face ID */
                        true,       /* Whether to return face landmarks */
                        /* Which face attributes to analyze, currently we support:
                           age,gender,headPose,smile,facialHair */
                        new FaceServiceClient.FaceAttributeType[]{
                                FaceServiceClient.FaceAttributeType.Age,
                                FaceServiceClient.FaceAttributeType.Gender,
                                FaceServiceClient.FaceAttributeType.FacialHair,
                                FaceServiceClient.FaceAttributeType.Smile,
                                FaceServiceClient.FaceAttributeType.HeadPose
                        });
            } catch (Exception e) {
                e.printStackTrace();
                mSucceed = false;
                publishProgress(e.getMessage());
                Log.e(TAG, e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setMessage(progress[0]);
        }

        @Override
        protected void onPostExecute(Face[] result) {

            stopTime = SystemClock.currentThreadTimeMillis();

            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            setButtonsEnabledStatus(true);

            if(result != null && result.length > 0) {
                Bitmap e = ImageHelper.drawFaceRectanglesOnBitmap(imageBitmap, result, true);
                mResultImageView.setImageBitmap(e);
                mResultTextView.setText(String.format(Locale.getDefault(), "%d faces detected in %.2f ms", result.length, (double)(stopTime - startTime)));
                mAgeTextView.setText(String.valueOf(result[0].faceAttributes.age));
                mEmotionTextView.setText(String.valueOf(result[0].faceAttributes.smile));
            }
            else {
                mResultTextView.setText(getString(R.string.no_result_detected));
            }

            //result[0].faceAttributes.facialHair.

        }
    }

    public void setButtonsEnabledStatus(boolean status) {
        mCameraFab.setEnabled(status);
    }

//    "anger": 0.00300731952,
//    "contempt": 5.14648448E-08,
//    "disgust": 9.180124E-06,
//    "fear": 0.0001912825,
//    "happiness": 0.9875571,
//    "neutral": 0.0009861537,
//    "sadness": 1.889955E-05,
//    "surprise": 0.008229999


}
