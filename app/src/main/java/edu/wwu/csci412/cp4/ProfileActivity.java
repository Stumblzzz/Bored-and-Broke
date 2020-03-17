package edu.wwu.csci412.cp4;

import android.Manifest;
import android.content.ContentUris;
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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
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
    private Bitmap profileBitmap = null;
    private Bitmap backgroundBitmap = null;
    private String defaultProfilePath = "";
    private String defaultBackgroundPath = "";

    private int imageViewSelected = 0;
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

        verifyPermissions(this);

        ImageView profilePicture = findViewById(R.id.imageView_profile_picture);
        //TODO: profilePicture.setImageURI(Uri.parse("https://boredandbrokebucket.s3-us-west-1.amazonaws.com/GOW.png"));

        getStoredData(this);

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

        setPreferences(this);
    }

    public void getStoredData(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        this.profileBitmap = decodeBase64(pref.getString(PREFERENCE_PROFILE, defaultProfilePath));
        this.backgroundBitmap = decodeBase64(pref.getString(PREFERENCE_BACKGROUND, defaultBackgroundPath));
//        this.profilePath = pref.getString(PREFERENCE_PROFILE, defaultProfilePath);
//        this.backgroundPath = pref.getString(PREFERENCE_BACKGROUND, defaultBackgroundPath);
//
//        if(!profilePath.equals("")) {
//            File imgFile = new File(profilePath);
//
//            if(imgFile.exists()) {
//                profileBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            } else {
//                System.out.println("Failed to find image from path: " + profilePath);
//            }
//        }
//
//        if(!backgroundPath.equals("")) {
//            File imgFile = new File(backgroundPath);
//
//            if(imgFile.exists()) {
//                backgroundBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            } else {
//                System.out.println("Failed to find image from path: " + backgroundPath);
//            }
//        }
    }

    public void setPreferences(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = pref.edit();

        editor.putString(PREFERENCE_PROFILE, encodeTobase64(profileBitmap));
        editor.putString(PREFERENCE_BACKGROUND, encodeTobase64(backgroundBitmap));

        editor.apply();
    }

    public static String encodeTobase64(Bitmap image) {
        if(image == null) {
            return "";
        }

        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
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
                            Uri uri = saveBitmapToGallery(imageBitmap);

                            if(imageViewSelected == 1) {
                                backgroundBitmap = imageBitmap;
                                backgroundPath = uri.getPath();
                            }
                            else if(imageViewSelected == 2) {
                                profileBitmap = imageBitmap;
                                profilePath = uri.getPath();
                            }
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

        MediaStore.Images.Media.insertImage(getContentResolver(), bm, mFileName, timeStamp);

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "");

        return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + mFileName + ".jpg"));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
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
