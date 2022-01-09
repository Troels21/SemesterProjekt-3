package dataAccesLayer;

import exceptions.OurException;
import model.Aftale;
import model.AftaleListe;

import java.sql.*;

public class SQL {

    private SQL() {
    }

    static private final SQL SQLOBJ = new SQL();

    static public SQL getSqlOBJ() {
        return SQLOBJ;
    }

    private final String url = "jdbc:mysql://130.225.170.248:3306/listeDB";
    private final String DatabaseUser = "testbruger1";
    private final String DatabasePassword ="LodretMaleriPuppe"; //System.getenv("SqlKode"); //tomcat system startups

    public Connection myConn;
    public Statement myStatement;

    public void makeConnectionSQL() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        myConn = DriverManager.getConnection(url, DatabaseUser, DatabasePassword);
        myStatement = myConn.createStatement();
    }

    public void removeConnectionSQL() {
        try {
            if (!myConn.isClosed()) {
                myConn.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}

