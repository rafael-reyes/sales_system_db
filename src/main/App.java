package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class App {
	
    private final static String DB_USERNAME = "db138";//"db141";
    private final static String DB_PASSWORD = "fbf08767";//"e9577d46";
	private final static String DB_URL = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2712/"+DB_USERNAME+"?autoReconnect=true&useSSL=false";
    
	private final static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    static boolean is_table_created = false;
    
    public static void main(String[] args) throws FileNotFoundException, SQLException{
    	//Load JDBC Driver "oracle.jdbc.driver.OracleDriver"

//    	try {
//			Class.forName("com.mysql.jdbc.Driver");
//		} catch (ClassNotFoundException e) {
//			System.err.println("Unable to load the driver class!");
//			return;
//		}
//    	
//    	Connection conn = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//        
//        //example query
//        String tableName = "part";
//        String query = "select count(*) from  " + tableName;
//
//		try {
//			conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
//	        stmt = conn.createStatement();
//	        rs = stmt.executeQuery(query);   
//	        System.out.println("hellooooo");
//	        is_table_created = true;
//	        
//	    } catch (Exception e ) {
//	    	//table does not exist or some other problem
//	    	e.printStackTrace();    
//	    	System.out.println("byeee");
//	    	is_table_created = false;
//	    }
//		initialMenu();
//		
//	    stmt.close();
//	    conn.close();
    	readData();
    }
    
    private static void initialMenu()throws SQLException, FileNotFoundException{
    	System.out.println("Welcome to Sales System!");
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println("\n--------------- Main Menu -------------------");
            System.out.println("What kind of operation would you like to perform?\n");
            System.out.println("1. Operations for administrator");
            System.out.println("2. Operation for salesperson");
            System.out.println("3. Operation for manager");
            System.out.println("4. Exit this program");
            System.out.print("Enter your Choice: ");
           
            if (input.hasNextInt()) {
                    switch (input.nextInt()) {
                        case 1:
                            //dataManipulation();
                            break;
                        case 2:
                            if (is_table_created){
                            	//dataOperation();
                            }else {
                                System.out.println("\nNo table exists now! Please create table first.");
                                System.out.println("Return to the main menu");
                                initialMenu();
                            }
                            break;
                        case 3:
                        	System.out.println("Do something");
                        	break;
                        case 4: 
                            System.out.println("\nExit Program, Bye!");
                            System.exit(0);
                        default:
                            System.out.println("\nUnknown action! Please select again.");
                    }
                } else {
                    System.out.println("\nUnknown action! Please select again.");
                    input.next();
                }
        }  
    }
    
    public static void readData() throws FileNotFoundException, SQLException{
    	System.out.println("\nType in the Source Data Folder Path: ");
    	Scanner input = new Scanner(System.in);
        String folderPath;
        
        folderPath = input.nextLine();//read folder path from user input
        input.close();
    	File folder = new File(folderPath);
    	File[] listOfFiles = null;
    	
    	try {
    		listOfFiles = folder.listFiles();
    		for (int i = 0; i < listOfFiles.length; i++) {
//        		System.out.println(listOfFiles[i].getAbsolutePath());
    			readFile(listOfFiles[i].getAbsolutePath());
        	}
    	} catch (NullPointerException e) {
    		System.err.println(folderPath + " directory does not exist.");
    		//initialMenu();
    	}	
    }
    public static void readFile(String fileName) throws FileNotFoundException, SQLException{
    	File file = new File(fileName);
    	Scanner data = null;
        try {
			data = new Scanner(file).useDelimiter("\t|\n");
		} catch (FileNotFoundException e) {
			System.err.println(fileName + " not found.");
            System.out.println("Back to Main Menu!");
            initialMenu();
		}
        if(fileName.contains("category.txt")){//insert into category
            while (data.hasNext() == true) {
            	System.out.println(data.nextInt()); //c_id
            	System.out.println(data.next()); //c_name
            }
            System.out.println("\nProcessing.....Done");

        } else if(fileName.contains("manufacturer.txt")){//insert into manufacturer
            while (data.hasNext() == true) {
                System.out.println(data.nextInt());//m_id
                System.out.println(data.next());//m_name
                System.out.println(data.next());//m_address
                System.out.println(data.nextInt());//m_phone number
            }
            System.out.println("\nProcessing.....Done");
        } 
            else if(fileName.contains("part.txt")){//insert into part
            while (data.hasNext() == true) {
            	System.out.println(data.nextInt());//p_id
            	System.out.println(data.next());//p_name
            	System.out.println(data.nextInt());//p_price
            	System.out.println(data.nextInt());//m_id
            	System.out.println(data.nextInt());//c_id
        		System.out.println(data.nextInt());//p_warranty
            	System.out.println(data.nextInt());//p_quality
            }
            System.out.println("\nProcessing.....Done");
        } 
            else if(fileName.contains("salesperson.txt")){//insert into salesperson
            while (data.hasNext() == true) {
            	System.out.println(data.nextInt());//s_id
            	System.out.println(data.next());//s_name
            	System.out.println(data.next());//s_address
            	System.out.println(data.nextInt());//s_phone
            	System.out.println(data.nextInt());//s_experience
            }
            System.out.println("\nProcessing.....Done");
        }

        else if(fileName.contains("transaction.txt")){//insert into transactions
            while (data.hasNext() == true) {
            	System.out.println(data.nextInt());//t_id
            	System.out.println(data.nextInt());//p_id
            	System.out.println(data.nextInt());//s_id
            	System.out.println(data.next());//t_date
            }
            System.out.println("\nProcessing.....Done");
        }
        data.close();
    }
}
