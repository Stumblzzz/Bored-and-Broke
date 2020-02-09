package edu.wwu.csci412.cp2;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
    }

    public void updateView() {

    }

    public void register(View v) {

        registerFinish();
    }

    public void registerFinish() {
        this.finish();
    }
}
