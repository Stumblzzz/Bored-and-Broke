package edu.wwu.csci412.cp4;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.*;
import java.io.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private int imageViewSelected;

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

        final String[] options = { "Take Photo", "Choose from Gallery", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (options[item]) {
                    case "Take Photo":
                        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 0);
                        break;
                    case "Choose from Gallery":
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , 1);
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
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

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
