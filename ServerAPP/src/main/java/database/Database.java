package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Scanner;

public class Database {
    protected String DRIVER;
    protected String USER;
    protected String PASS;
    protected String DB_URL;
    protected static Connection con;
//    protected String DRIVER="com.mysql.cj.jdbc.Driver";
//    protected String USER="root";
//    protected String PASS="258307aA";
//    protected String DB_URL="jdbc:mysql://localhost:3306/tic_tac";
//    protected Connection con;

    public Database() {
        Hashtable<String, String> databaseInfo = readDatabaseData();
        if (!databaseInfo.isEmpty()) {
            String DB_Name = databaseInfo.get("databaseName").trim();
            DRIVER = databaseInfo.get("driver").trim();
            USER = databaseInfo.get("userName").trim();
            PASS = databaseInfo.get("pass");
            DB_URL = databaseInfo.get("databaseURL").trim() + "/" + DB_Name;
            try {
                connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Hashtable<String, String> readDatabaseData() {
        Hashtable<String, String> databaseData = new Hashtable<>();
        try {
            File databaseInfoFile = new File("database Info File.txt");
            Scanner myReader = new Scanner(databaseInfoFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data.split("=").length == 2) {
                    databaseData.put(data.split("=")[0], data.split("=")[1]);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("inside readDatabaseData : " + e);
        }
        return databaseData;
    }

    public boolean connect() {
        try {
            if(!isDbConnected()){
                Class.forName(DRIVER);
                con  = DriverManager.getConnection(DB_URL, USER, PASS);
            }
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("INSIDE The connect function" + e);
            return false;
        }
    }

    public boolean disconnect() {
        if (isDbConnected()) {
            try {
                con.close();
                return true;
            } catch (SQLException e) {
                System.out.println("inside the disconnect function " + e);
            }
        }
        return false;
    }

    public void reconnect() { //return 0 false if it can't connect again true if connect
        disconnect();
        connect();
    }

    public boolean isDbConnected() {
        try {
            return con != null && !con.isClosed();
        } catch (SQLException ignored) {
            System.err.println("no database connection");
            return false;
        }
    }
}
