package edu.wwu.csci412.cp2;

import java.sql.*;
import android.util.Log;

public class SQL_Utils {

    //A helper class that makes using our sql database easier
    public SQL_Utils() {

    }

    //Given a database this method will open a connection to that database
    private Connection getConnection(){
        Connection conn[] = new Connection[1];
        conn[0] = null;

        try {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        conn[0] = DriverManager.getConnection("jdbc:mysql://boredandbroke.mysql.database.azure.com:3306/boredandbroke", "boredandbroke@boredandbroke", "asdfASDF1!");
                    } catch (Exception e) {
                        Log.e("Broken SQL: ", e.toString());
                    }
                }
            });
            t.start();
            t.join();
        }catch(Exception e){
            Log.e("Broken SQL: ", e.toString());
        }
        return conn[0];
    }

    public int sqlRowCount(String table, String condition){
        Connection conn = getConnection();
        int rowCount[] = new int[1];
        rowCount[0] = 0;
        String query = "SELECT * FROM " + table + " WHERE " + condition + ";";

        try {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        Statement stmt = conn.createStatement();
                        ResultSet rset = stmt.executeQuery(query);
                        //Count Rows
                        while(rset.next()){
                            rowCount[0]++;
                        }
                        stmt.close();
                        conn.close();
                    } catch (Exception e) {
                        Log.e("Broken SQL: ", e.toString());
                    }
                }
            });
            t.start();
            t.join();
        } catch (Exception e) {
            Log.e("Broken SQL: ", e.toString());
        }
        return rowCount[0];
    }

    public boolean sqlInsert(String table, String[] columnNames, String[] values){
        Connection conn = getConnection();

        //Create format of (columnName, columnName,......., columnName) for prepared statement query
        String columnNameQuerySection = "";
        int lastIndex = columnNames.length - 1;
        for(int i = 0; i < columnNames.length; i++){
            if(i != lastIndex){
                columnNameQuerySection += columnNames[i] + ",";
            }else{
                columnNameQuerySection += columnNames[i];
            }
        }

        //Create format of (?,?,?,.....,?) for prepared statement query
        String valuesQuerySection = "";
        lastIndex = columnNames.length - 1;
        for(int i = 0; i < values.length; i++){
            if(i != lastIndex){
                valuesQuerySection += "?,";
            }else{
                valuesQuerySection += "?";
            }
        }

        String query = "INSERT INTO " + table + "(" + columnNameQuerySection + ") VALUES (" + valuesQuerySection + ");";
        try {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        Statement stmt = conn.createStatement();
                        PreparedStatement prepStatement = conn.prepareStatement(query);
                        for(int i = 0; i < values.length; i++){
                            prepStatement.setString(i+1, values[i]);
                        }
                        prepStatement.execute();
                        stmt.close();
                        conn.close();
                    } catch (Exception e) {
                        Log.e("Broken SQL", e.toString());
                    }
                }
            });
            t.start();
            t.join();
        }catch(Exception e){
            return false;
        }

        return true;
    }
}
