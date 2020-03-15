package edu.wwu.csci412.cp4;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import java.sql.ResultSet;
import java.sql.SQLException;

public class activity_list extends AppCompatActivity
{
    SQL_Utils sqlUtils = new SQL_Utils();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_list);

        //SQL query below
        SQLCloseConnection sqlCloseConnection = sqlUtils.sqlSelectNoWhere("activities");

        try {
            populateScrollView(sqlCloseConnection.getResultSet());
            sqlCloseConnection.closeSQLConn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void backButton(View view)
    {
        this.finish();
    }

    public void populateScrollView(ResultSet results) throws SQLException
    {
        TableLayout layout = new TableLayout(this);

        //Builds title card
        TextView pageTitle = new TextView(this);
        pageTitle.setTextSize(25);
        pageTitle.setBackgroundColor(Color.parseColor("#80000000"));
        pageTitle.setText(R.string.activities_near_you);
        pageTitle.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(pageTitle);

        Button backButton = new Button(this);
        backButton.setText(R.string.back);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                activity_list.this.finish();
            }
        });
        backButton.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(backButton);

        Display current = this.getWindowManager().getDefaultDisplay();

        ScrollView scrollView = new ScrollView(this);
        LinearLayout internalForScrollView = new LinearLayout(this);
        internalForScrollView.setOrientation(LinearLayout.VERTICAL);

        try
        {
            while(results.next())
            {
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                tableRow.setWeightSum(10);
                tableRow.setBackgroundColor(Color.parseColor("#80000000"));

                TextView rowTitle = new TextView(this);
                rowTitle.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 9f));
                rowTitle.setText(results.getString(1));

                TextView descriptionView = new TextView(this);
                descriptionView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 2f));
                String description = results.getString(2)+ "\nCreated by: " + results.getString(4) + "\nDescription: "
                        + results.getString(3) + "\nLatitude: " + results.getString(5) + "\nLongitude: " + results.getString(6);
                descriptionView.setText(description);
                descriptionView.setPadding(0,20,0,20);

                Button buttonView = new Button(this);
                buttonView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 3f));
                buttonView.setText(R.string.more_info_here);
                int activity_id = results.getInt(1);
                buttonView.setOnClickListener( new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        Intent myIntent  = new Intent (activity_list.this, specific_activity.class);
                        myIntent.putExtra("activity_id", activity_id);
                        activity_list.this.startActivity(myIntent);
                    }
                });

                //Builds Table Row
                tableRow.addView(rowTitle);
                tableRow.addView(descriptionView);
                tableRow.addView(buttonView);

                //Puts TableRow in View to be added to scroll view
                internalForScrollView.addView(tableRow);
            }

            scrollView.addView(internalForScrollView);
            layout.addView(scrollView);

            setContentView(layout);
        }
        catch (SQLException e)
        {
            Toast.makeText(this, "ERROR GENERATING ACTIVITY LIST", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            this.finish();
        }
    }
}