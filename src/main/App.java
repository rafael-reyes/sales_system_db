package main;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
	
    private final static String DB_USERNAME = "db138";//"db141";
    private final static String DB_PASSWORD = "fbf08767";//"e9577d46";
	private final static String DB_URL = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2712/"+DB_USERNAME+"?autoReconnect=true&useSSL=false";
	private final static String DB_URL2 = "jdbc:oracle:oci8:@"+DB_USERNAME+".cse.cuhk.edu.hk";

    
    public static void main(String[] args) throws FileNotFoundException, SQLException{
    	//Load JDBC Driver "oracle.jdbc.driver.OracleDriver"
    	try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Unable to load the driver class!");
			return;
		}
    	
		Connection conn = null;
		Statement stmt = null;
        ResultSet rs = null;
        
        //example query
        String tableName = "part";
        String query = "select count(*) from  " + tableName;

		try {
			conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
	        stmt = conn.createStatement();
	        rs = stmt.executeQuery(query);      
	    } catch (Exception e ) {
	    	//table does not exist or some other problem
	    	e.printStackTrace();    
	    }

	        stmt.close();
	        conn.close();
    }
}
