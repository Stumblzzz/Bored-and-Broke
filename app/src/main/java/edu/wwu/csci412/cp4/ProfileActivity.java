package edu.wwu.csci412.cp4;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class ProfileActivity extends AppCompatActivity {
    private static final String PREFERENCE_PROFILE = "profile";
    private static final String PREFERENCE_BACKGROUND = "background";

    private String profilePath = null;
    private String backgroundPath = null;
    private String defaultProfilePath = "";
    private String defaultBackgroundPath = "";

    private int imageViewSelected = 0;
    private Bitmap profileBitmap = null;
    private Bitmap backgroundBitmap = null;
    // Storage Permissions
    private static final int REQUEST_PERMISSION = 1;
    private static String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        ImageView profilePicture = findViewById(R.id.imageView_profile_picture);
        //TODO: profilePicture.setImageURI(Uri.parse("https://boredandbrokebucket.s3-us-west-1.amazonaws.com/GOW.png"));

        getStoredData(this);

        // TODO: Get profile and background images from sharedpreferences
        updateView();
    }

    public void updateView() {
        ImageView profileBackground = findViewById(R.id.imageView_profile_background);
        ImageView profilePicture = findViewById(R.id.imageView_profile_picture);
        TextView profileName = findViewById(R.id.textView_name);

        if(backgroundBitmap == null) {
            profileBackground.setImageResource(R.drawable.android_background);
        } else {
            profileBackground.setImageBitmap(backgroundBitmap);
        }

        if(profileBitmap == null) {
            profilePicture.setImageResource(R.drawable.profile);
        } else {
            profilePicture.setImageBitmap(profileBitmap);
        }

        if(LoginActivity.user == null) {
            profileName.setText(R.string.profile_name);
        } else {
            profileName.setText(LoginActivity.user.getUsername());
        }

        imageViewSelected = 0;
    }

    public void getStoredData(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        this.profilePath = pref.getString(PREFERENCE_PROFILE, defaultProfilePath);
        this.backgroundPath = pref.getString(PREFERENCE_BACKGROUND, defaultBackgroundPath);

        if(defaultProfilePath.equals("")) {

        }
    }

    public void setPreferences(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = pref.edit();

        editor.putString(PREFERENCE_PROFILE, profilePath);
        editor.putString(PREFERENCE_BACKGROUND, backgroundPath);

        editor.apply();
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void verifyPermissions(android.app.Activity activity) {
        // Check if we have write permission
        int permissionWrite = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionRead = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCamera = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (permissionWrite != PackageManager.PERMISSION_GRANTED
                || permissionRead != PackageManager.PERMISSION_GRANTED
                || permissionCamera != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS,
                    REQUEST_PERMISSION
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Please give your permission.", Toast.LENGTH_LONG).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void selectImage(View view) {
        switch (view.getId()) {
            case R.id.imageView_profile_background:
                imageViewSelected = 1;
                break;
            case R.id.imageView_profile_picture:
                imageViewSelected = 2;
                break;
            default:
                imageViewSelected = -1;
                break;
        }

        verifyPermissions(this);

        showBuilder();
    }

    public void showBuilder() {
        final String[] options = { "Take Photo", "Choose from Gallery", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your picture");

        builder.setItems(options, (dialog, item) -> {

            switch (options[item]) {
                case "Take Photo":
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePicture.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePicture, 0);
                    }
                    break;
                case "Choose from Gallery":
                    Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT);
                    pickPhoto.setType("image/*");
                    startActivityForResult(pickPhoto, 1);
                    break;
                default:
                    imageViewSelected = 0;
                    dialog.dismiss();
            }
        });

        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        if(imageBitmap != null) {
                            if(imageViewSelected == 1) {
                                backgroundBitmap = imageBitmap;
                            }
                            else if(imageViewSelected == 2) {
                                profileBitmap = imageBitmap;
                            }

                            Uri uri = saveBitmapToGallery(imageBitmap);

                            System.out.println("URI: " + uri);
                        } else {
                            Log.w("ProfileActivity", "Error: Failed to update ImageView.");
                        }
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();

                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(bitmap != null) {
                            if(imageViewSelected == 1) {
                                backgroundBitmap = bitmap;
                                backgroundPath = getPath(this, selectedImage);
                            }
                            else if(imageViewSelected == 2) {
                                profileBitmap = bitmap;
                                profilePath = getPath(this, selectedImage);
                            }
                        } else {
                            Log.w("ProfileActivity", "Error: Failed to update ImageView.");
                        }
                    }

                    break;
            }
        }

        updateView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Uri saveBitmapToGallery(Bitmap bm) {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";

        MediaStore.Images.Media.insertImage(getContentResolver(), bm, mFileName , timeStamp);

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "IMG_FOLDER");

        return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + mFileName));
    }

    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }

        if(result == null) {
            result = "Not found";
        }

        return result;
    }

    Bitmap drawable_from_url(String url) throws java.net.MalformedURLException, java.io.IOException {

        HttpURLConnection connection = (HttpURLConnection)new URL(url) .openConnection();
        connection.setRequestProperty("User-agent","Mozilla/4.0");

        connection.connect();
        InputStream input = connection.getInputStream();

        return BitmapFactory.decodeStream(input);
    }

    public void displayActivities(View view) {

    }

    public void displayPhotos(View view) {

    }

    public void displayReviews(View view) {

    }

    public void back() {
        this.finish();
    }
}
