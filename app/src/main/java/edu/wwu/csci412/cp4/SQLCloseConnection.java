package edu.wwu.csci412.cp4;

import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLCloseConnection
{
    private Statement statement;
    private Connection connection;
    private ResultSet resultSet;

    public SQLCloseConnection() {}

    public void closeSQLConn()
    {
        try
        {
            statement.close();
            connection.close();
        }
        catch (Exception e)
        {
            Log.e("Broken SQL: ", e.toString());
        }
    }

    public ResultSet getResultSet()
    {
        return resultSet;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }
}
