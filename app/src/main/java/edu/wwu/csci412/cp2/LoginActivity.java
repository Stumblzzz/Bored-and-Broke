package edu.wwu.csci412.cp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
    }

    public void updateView() {

    }

    public void login(View v) {

    }

    public void register(View v) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);

        this.startActivity(registerIntent);
    }
}
