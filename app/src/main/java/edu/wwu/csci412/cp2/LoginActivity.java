package edu.wwu.csci412.cp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    SQL_Utils sql_utils = new SQL_Utils();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
    }

    public void updateView() {

    }

    public void login(View v) {
        EditText editTextEmail = (EditText) findViewById(R.id.editText_login_email);
        EditText editTextPassword = (EditText) findViewById(R.id.editText_login_password);
        TextView textViewLoginError = (TextView) findViewById(R.id.login_error);
        textViewLoginError.setVisibility(View.INVISIBLE);
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if(checkCredentials(email, password)){
            Intent mapsIntent = new Intent (this, MapsActivity.class);
            this.startActivity(mapsIntent);
        }else{
            textViewLoginError.setVisibility(View.VISIBLE);
        }
    }

    public void loginFinish() {
        this.finish();
    }

    public void register(View v) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);

        this.startActivity(registerIntent);
    }

    private boolean checkCredentials(String email, String password){
        String tableName = "registration";
        String condition = "(email = '" + email + "' AND pass = '" + password + "')";

        int rowsWithCredentials = sql_utils.sqlRowCount(tableName, condition);

        //Multiple email login error
        if(rowsWithCredentials > 1){
            Log.w("Login Error", "There should never be more than 1 of the same email");
            return false;
        }

        return (rowsWithCredentials == 1) ? (true) : (false);
    }
}
