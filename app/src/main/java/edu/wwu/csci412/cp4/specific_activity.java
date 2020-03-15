package edu.wwu.csci412.cp4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.sql.ResultSet;
import java.sql.SQLException;

public class specific_activity extends FragmentActivity implements OnMapReadyCallback
{
    private GoogleMap mMap;
    SQL_Utils sqlUtils = new SQL_Utils();
    int currentActivity = -1;
    double coord_lat = -1;
    double coord_long = -1;
    String title = "";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specific_activity_sample);

        Intent intent = getIntent();
        int id = intent.getIntExtra("activity_id", 1);
        currentActivity = id;
        SQLCloseConnection sqlCloseConnection = sqlUtils.sqlSelect("activities", "ID = " + id);

        try
        {
            updateView(sqlCloseConnection.getResultSet());
            sqlCloseConnection.closeSQLConn();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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

        LatLng tempLocation = new LatLng(coord_lat, coord_long);
        mMap.addMarker(new MarkerOptions().position(tempLocation).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tempLocation));
    }

    public void updateView(ResultSet results) throws SQLException
    {
        if(results.next())
        {
            TextView title = findViewById(R.id.specific_activity_title);
            title.setText(results.getString(2));

            TextView descriptionView = findViewById(R.id.specific_activity_info);
            String description = "User: " + results.getString(4) + "\nDescription: " + results.getString(3) + "\nLatitude: "
                    + results.getString(5) + "\nLongitude: " + results.getString(6);
            descriptionView.setText(description);

            this.title = results.getString(2);
            coord_lat = results.getDouble(5);
            coord_long = results.getDouble(6);
        }
    }

    public void backButton2(View view)
    {
        this.finish();
    }
}
