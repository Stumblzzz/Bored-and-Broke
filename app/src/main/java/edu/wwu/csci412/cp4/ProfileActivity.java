package edu.wwu.csci412.cp4;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.*;
import java.io.*;
import java.util.Arrays;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ProfileActivity extends AppCompatActivity {
    private int imageViewSelected;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_CAMERA = 1;
    private static String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        ImageView profilePicture = findViewById(R.id.imageView_profile_picture);
        //TODO: profilePicture.setImageURI(Uri.parse("https://boredandbrokebucket.s3-us-west-1.amazonaws.com/GOW.png"));

        updateView();
    }

    public void updateView() {
        ImageView profileBackground = findViewById(R.id.imageView_profile_background);
        ImageView profilePicture = findViewById(R.id.imageView_profile_picture);

        profileBackground.setImageResource(R.drawable.android_background);
        profilePicture.setImageResource(R.drawable.profile);

        imageViewSelected = 0;
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(android.app.Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        System.out.println("permission: " + permission);
        System.out.println("PERMISSION_GRANTED: " + PackageManager.PERMISSION_GRANTED);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static void verifyCameraPermissions(android.app.Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        System.out.println("permission: " + permission);
        System.out.println("PERMISSION_GRANTED: " + PackageManager.PERMISSION_GRANTED);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_CAMERA,
                    REQUEST_CAMERA
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        System.out.println("requestCode: " + requestCode);
        System.out.println("permissions: " + Arrays.toString(permissions));
        System.out.println("grantResults: " + Arrays.toString(grantResults));
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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

        verifyStoragePermissions(this);
        verifyCameraPermissions(this);

        showBuilder();
    }

    public void showBuilder() {
        final String[] options = { "Take Photo", "Choose from Gallery", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                switch (options[item]) {
                    case "Take Photo":
                        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePicture.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePicture, 0);
                        }
                        break;
                    case "Choose from Gallery":
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);
                        break;
                    default:
                        imageViewSelected = 0;
                        dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView imageView;

        switch (imageViewSelected) {
            case 1:
                imageView = findViewById(R.id.imageView_profile_background);
                break;
            case 2:
                imageView = findViewById(R.id.imageView_profile_picture);
                break;
            default:
                return;
        }

        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        System.out.println("Took Picture!");
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        imageView.setImageBitmap(imageBitmap);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        System.out.println("selectedImage: " + selectedImage);
                        System.out.println("filePathColumn: " + Arrays.toString(filePathColumn));

                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                                cursor.close();
                            }
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
