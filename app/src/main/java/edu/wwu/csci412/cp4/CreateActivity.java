package edu.wwu.csci412.cp4;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CreateActivity extends AppCompatActivity implements LocationListener{

    SQL_Utils sql_utils = new SQL_Utils();

    protected LocationManager locationManager;

    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_activity_page);
    }

    public void activityCreate(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);

            Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

            EditText hostEditText = (EditText) findViewById(R.id.Name_of_Host);
            EditText activityNameEditText = (EditText) findViewById(R.id.specific_activity_title) ;
            EditText descriptionEditText = (EditText) findViewById(R.id.activity_description);

            String Name = hostEditText.getText().toString();
            String activityName = activityNameEditText.getText().toString();
            String activityDescription = descriptionEditText.getText().toString();

            String[] names = new String[5];

            names[0] = "activityName";
            names[1] = "description";
            names[2] = "name";
            names[3] = "latitude";
            names[4] = "longitude";

            String[] insertActivity = new String[5];

            insertActivity[0] = activityName;
            insertActivity[1] = activityDescription;
            insertActivity[2] = Name;
            insertActivity[3] = String.valueOf(location.getLatitude());
            insertActivity[4] = String.valueOf(location.getLongitude());

            try {
                sql_utils.sqlInsert("activities",names, insertActivity);
            }

            catch (Exception e){
                e.printStackTrace();
            }

            this.finish();

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Latitude","status");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

}
