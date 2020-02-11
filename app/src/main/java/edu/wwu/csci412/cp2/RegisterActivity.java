package edu.wwu.csci412.cp2;

import java.sql.*;

import java.util.regex.Pattern;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.util.Log;

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
            if(addCredentials(email, password)){
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
            emailRepeatedError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        //Check for repeated email
        if(repeatedEmail(email)){
            emailError.setVisibility(View.VISIBLE);
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
    private boolean addCredentials(String email, String password){
        try {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://database-1.ckxktd7argpj.us-west-1.rds.amazonaws.com/registration", "admin", "BoredAndBroke1!");
                        Statement stmt = con.createStatement();
                        PreparedStatement prepStatement = con.prepareStatement("INSERT INTO `registration`.`email_pass` (`email`,`password`) VALUES (?,?);");
                        prepStatement.setString(1,email);
                        prepStatement.setString(2,password);
                        prepStatement.execute();
                        con.close();
                    } catch (Exception e) {
                        Log.e("Broken SQL", e.toString());
                    }
                }
            }).start();
            return true;
        }catch(Exception e){
            return false;
        }
    }

    //Checks to make sure that the email does not already exist in the DB
    private boolean repeatedEmail(String attemptEmail){
        final ResultSet[] resultSetArray = new ResultSet[1];
        final int[] rowCount = new int[1];

        try {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://database-1.ckxktd7argpj.us-west-1.rds.amazonaws.com/registration", "admin", "BoredAndBroke1!");
                        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        ResultSet rset = stmt.executeQuery("SELECT COUNT(*) FROM `registration`.`email_pass` WHERE `email`='" + attemptEmail + "';");
                        resultSetArray[0] = rset;

                        con.close();
                    } catch (Exception e) {
                        Log.e("Broken SQL", e.toString());
                    }
                }
            }).start();
        } catch(Exception e){
            Log.e("Broken SQL", e.toString());
        }

        //Not a new email
        //if(rowCount[0] != 0){
        //    return false;
        //}else{
        //    return true;
        //}
        //TODO: Fix Commented Code
        return false;
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
