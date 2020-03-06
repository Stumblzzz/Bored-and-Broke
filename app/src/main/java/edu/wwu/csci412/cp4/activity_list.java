package edu.wwu.csci412.cp4;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class activity_list extends AppCompatActivity
{
    SQL_Utils sqlUtils = new SQL_Utils();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        //SQL query below
        ResultSet results = sqlUtils.sqlSelect("activities", "1=1" );

        try {
            populateScrollView(results);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void backButton(View view)
    {
        this.finish();
    }

    public void populateScrollView(ResultSet results) throws SQLException {
        ScrollView activity_list_scroll_view = findViewById(R.id.activity_list_scroll_view);
        View tempTableRow = null;
        int i = 0;

        try
        {
            while(results.next())
            {
                tempTableRow = (TableRow) View.inflate(this, R.layout.activity_list_row, null);

                //This would be where we take care of image stuff if we get it working
                TextView tempImageText = (TextView) tempTableRow.findViewById(R.id.sample_image_text);
                tempImageText.setText(R.string.SampleImageTextDescription);

                //This is the middle section with information about the activity
                TextView tempDescriptionText = (TextView) tempTableRow.findViewById(R.id.sample_activity_information);
                String description = results.getString(0) + "\n" + results.getString(1) + "\n" + results.getString(3) + "\n"
                        + results.getString(4) + "\n" + results.getString(5) + "\n";
                tempDescriptionText.setText(description);

                //This is where we hypothetically have a button set up that will go to a specific_activity page that has this activities information
                Button tempButton = (Button) tempTableRow.findViewById(R.id.activity_list_sample_button);

                activity_list_scroll_view.addView(tempTableRow);
            }
        }
        catch (SQLException e)
        {
            Toast.makeText(this, "ERROR GENERATING ACTIVITY LIST", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            this.finish();
        }

    }
}
