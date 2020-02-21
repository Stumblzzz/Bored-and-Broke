package edu.wwu.csci412.cp3;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class specific_activity extends FragmentActivity implements OnMapReadyCallback
{
    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specific_activity_sample);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        try
        {
            mapFragment.getMapAsync(this);
        }
        catch(NullPointerException npe)
        {
            //Big Error TODO
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng cfBuilding = new LatLng(48.732839, -122.485237);
        mMap.addMarker(new MarkerOptions().position(cfBuilding).title("Marker for Communications Facility"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cfBuilding));
    }

    public void backButton2(View view)
    {
        this.finish();
    }
}
