package edu.wwu.csci412.cp3;

import java.util.regex.Pattern;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    SQL_Utils sql_utils = new SQL_Utils();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void updateView() {

    }

    public void register(View v) {
        //User input
        EditText fullNameEditText = (EditText) findViewById(R.id.editText_register_name);
        EditText emailAddressEditText = (EditText) findViewById(R.id.editText_register_email);
        EditText passwordEditText = (EditText) findViewById(R.id.editText_register_password);
        EditText confirmPasswordEditText = (EditText) findViewById(R.id.editText_register_confirm_password);

        String fullName = fullNameEditText.getText().toString();
        String email = emailAddressEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        boolean isValid = checkValidRegistration(fullName, email, password, confirmPassword);

        if(isValid) {
            if(addCredentials(email, password, fullName)){
                Intent myIntent = new Intent (this, LoginActivity.class);
                startActivity(myIntent);
                registerFinish();
            }else{
                //TODO: Add Error Message
            }
        }
    }

    //TODO: ask Ahmed if we need to push this logic(The act of checking) up to a server. Please no.....
    private boolean checkValidRegistration(String fullName, String email, String password, String confirmPassword){
        boolean isValid = true;

        //Clear errors
        TextView nameError = (TextView) findViewById(R.id.editText_register_name_error);
        TextView emailError = (TextView) findViewById(R.id.editText_register_email_error);
        TextView emailRepeatedError = (TextView) findViewById(R.id.editText_register_email_repeated_error);
        TextView passwordError = (TextView) findViewById(R.id.editText_register_password_error);
        TextView passwordMatchError = (TextView) findViewById(R.id.editText_register_password_confirm_error);

        nameError.setVisibility(View.INVISIBLE);
        emailError.setVisibility(View.INVISIBLE);
        emailRepeatedError.setVisibility(View.INVISIBLE);
        passwordError.setVisibility(View.INVISIBLE);
        passwordMatchError.setVisibility(View.INVISIBLE);

        //Technically people can legally change their name to ##***&&& in some countries so we are not checking for that
        if(fullName.length() == 0 || fullName == "" || fullName == null){
            nameError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        //Check email
        if(!checkValidEmail(email)){
            emailError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        //Check for repeated email
        if(!newEmail(email)){
            emailRepeatedError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        //Check password
        if(password.length() < 8){
            passwordError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        //Compare passwords
        if(!password.equals(confirmPassword)){
            passwordMatchError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }

    //Takes the valid credentials and inserts them into the database
    private boolean addCredentials(String email, String password, String fullName){
        String columnNames[] = {"email", "pass", "name"};
        String values[] = {email, password, fullName};
        return sql_utils.sqlInsert("registration", columnNames, values);
    }

    //Checks to make sure that the email does not already exist in the DB
    private boolean newEmail(String attemptEmail){
        String condition = "email='" + attemptEmail + "'";
        int numberOfEquivalentEmails = sql_utils.sqlRowCount("registration", condition);

        //not new email
        if(numberOfEquivalentEmails > 0){
            return false;
        }else{
            return true;
        }
    }

    //makes sure that a given string is a valid email address
    private boolean checkValidEmail(String emailAttempt){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (emailAttempt == null)
            return false;
        return pat.matcher(emailAttempt).matches();
    }

    private void registerFinish() {
        this.finish();
    }
}
