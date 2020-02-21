package edu.wwu.csci412.cp3;

import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class NotSignedInLandingPage extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_signed_in_landing_page);

        Button loginButton = (Button) findViewById(R.id.goToLogin);
        Button registerButton = (Button) findViewById(R.id.goToRegister);

        loginButton.setOnClickListener( new View.OnClickListener(){
            public void onClick(View v){
                goToLoginPage();
            }
        });

        registerButton.setOnClickListener( new View.OnClickListener(){
            public void onClick(View v){
                goToRegisterPage();
            }
        });
    }

    private void goToLoginPage(){
        Intent myIntent = new Intent (this, LoginActivity.class);
        startActivity(myIntent);
    }

    private void goToRegisterPage(){
        Intent myIntent = new Intent (this, RegisterActivity.class);
        startActivity(myIntent);
    }
}
