package edu.wwu.csci412.cp4;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.*;
import java.io.*;
import java.util.Arrays;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class ProfileActivity extends AppCompatActivity {
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
    private static final int REQUEST_CAMERA = 1;
    private static String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        ImageView profilePicture = findViewById(R.id.imageView_profile_picture);
        //TODO: profilePicture.setImageURI(Uri.parse("https://boredandbrokebucket.s3-us-west-1.amazonaws.com/GOW.png"));

        // TODO: Get profile and background images from sharedpreferences
        updateView();
    }

    public void updateView() {
        ImageView profileBackground = findViewById(R.id.imageView_profile_background);
        ImageView profilePicture = findViewById(R.id.imageView_profile_picture);

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

        imageViewSelected = 0;
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

        System.out.println("READ: " + permissionRead + ", WRITE: " + permissionWrite + ", CAMERA: " + permissionCamera + ", GRANTED: " + PackageManager.PERMISSION_GRANTED);

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
                        } else {
                            Log.w("ProfileActivity", "Error: Failed to update ImageView.");
                        }
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();

                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(bitmap != null) {
                            if(imageViewSelected == 1) {
                                backgroundBitmap = bitmap;
                            }
                            else if(imageViewSelected == 2) {
                                profileBitmap = bitmap;
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
