package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class App {
	//java -classpath mysql-jdbc.jar:. main.App
    private final static String DB_USERNAME = "db138";//"db141";
    private final static String DB_PASSWORD = "fbf08767";//"e9577d46";
	private final static String DB_URL = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2712/"+DB_USERNAME+"?autoReconnect=true&useSSL=false";
    
	private final static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    static boolean is_table_created = false;
    
    private static Scanner input;
    private static Connection conn;
    public static void main(String[] args) throws FileNotFoundException, SQLException, InterruptedException{
    	//Load JDBC Driver "oracle.jdbc.driver.OracleDriver"
    	try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Unable to load the driver class!");
			return;
		}
    	
    	conn = null;
		Statement stmt = null;
		ResultSet rs = null;
        
        //example query to see the state of database (empty or not)
        String tableName = "Category";
        String query = "select count(*) from  " + tableName;
        System.out.println("Welcome to Sales System!");
        
		try {
			conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
	        stmt = conn.createStatement();
	        rs = stmt.executeQuery(query); 
	        System.out.println("\n********Tables Already Exist in Database!********\n");
	        is_table_created = true;
	        
	    } catch (Exception e ) {
	    	//table does not exist or some other problem
	    	//e.printStackTrace();
	    	System.out.println("\n*********Tables Currently Not Exist in Database!************\n");
	    	is_table_created = false;
	    }
		
	    stmt.close();
    	
	    initialMenu();
	    conn.close();
    }
    
    private static void initialMenu()throws SQLException, FileNotFoundException, InterruptedException{

        input = new Scanner(System.in);
        while (true) {
            System.out.println("\n--------------- Main Menu -------------------");
            System.out.println("What kind of operation would you like to perform?");
            System.out.println("1. Operations for administrator");
            System.out.println("2. Operation for salesperson");
            System.out.println("3. Operation for manager");
            System.out.println("4. Exit this program");
            System.out.print("Enter your Choice: ");
           
            if (input.hasNextInt()) {
                    switch (input.nextInt()) {
                        case 1:
                            administrator();
                            break;
                        case 2:
                            if (is_table_created){
                            	//salesperson();
                            }else {
                                System.out.println("\nNo table exists now! Please create table first.");
                                System.out.println("Return to the main menu");
                            }
                            break;
                        case 3:
                        	//manager();
                        	break;
                        case 4: 
                            System.out.println("\nExit Program, Bye!");
                            conn.close();
                            System.exit(0);
                        default:
                            System.out.println("\nUnknown action! Please select again.");
                            break;
                    }
                } else {
                    System.out.println("\nUnknown action! Please select again.");
                    input.next();
                } 
        }  
    }
    
    private static void executeQuery(String[] query) throws SQLException{
    	Statement stmt=null;
         try {
             stmt = conn.createStatement();
             
             for (String q : query) {
                 stmt.executeUpdate(q);
             }   
         } catch (SQLException e) {
             e.printStackTrace();
         }finally{
        	 stmt.close();
         }
    }
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ADMINISTRATOR~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    private static void administrator() throws SQLException, FileNotFoundException, InterruptedException{   
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("\nWhat kind of operation would you like to perform??");
           	System.out.println("1. Create All Tables");
            System.out.println("2. Delete All Tables");
            System.out.println("3. Load Data into Tables");
            System.out.println("4. Show Information in Tables");
            System.out.println("5. Return to main menu");
            System.out.print("Enter your choice: ");

            if (sc.hasNextInt()) {
                switch (sc.nextInt()) {
                case 1:
                	createTables();
                    break;
                case 2:
                    deleteTables();
                    break;
                case 3:
                    loadData();
                    break;
                case 4:
                    //showInfo();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("\nInvalid input.");
                    break;
                }
            } else {
                System.out.println("\nInvalid input.");
                try {
                	sc.next();
                }catch (NoSuchElementException e) {
                	break;
                }
            }
        }
    
    } 
    
    private static void createTables()  throws SQLException {  
        if ( is_table_created) {
            System.out.println("\nTables already exists. Try dropping tables first");
        } else{
            try{
                executeQuery(App.Queries.CREATETABLES);
                System.out.println("\nProcessing.....Done");
                is_table_created = true;
             } catch (SQLSyntaxErrorException e) {
                 System.out.println("\nTables already exists. Try dropping tables first");            
             }
        }   
    }
    
    private static void deleteTables()  throws SQLException {  
        if (!is_table_created) { 
            System.out.println("\nOops there are no tables to drop!");
        } else{
            try{
                executeQuery(App.Queries.DELETETABLES);
                System.out.println("\nProcessing.....Done");
                is_table_created = false;
            } catch (SQLSyntaxErrorException e) {
              System.out.println("\nOops there are no tables to drop!");            
            }
        }   
    }
    
    public static void loadData() throws FileNotFoundException, SQLException, InterruptedException{
    	System.out.println("\nType in the Source Data Folder Path: ");
    	Scanner input2 = new Scanner(System.in);
        String folderPath;
        folderPath = input2.nextLine();//read folder path from user input
    	
    	File folder = new File(folderPath);
    	File[] listOfFiles = null;
    	try {
    		
    		listOfFiles = folder.listFiles();
    		for (int i = 0; i < listOfFiles.length; i++) {
    			readFile(listOfFiles[i].getAbsolutePath());
        	}
    	} catch (NullPointerException e) {
    		System.err.println(folderPath + " directory does not exist.");
    		TimeUnit.SECONDS.sleep(1);
    		return;
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
		}
        PreparedStatement pstmt=null;
        //Connection conn2 = null;
        
        if(fileName.contains("category.txt")){//inserting into Category
        	pstmt = conn.prepareStatement(App.Queries.INSERTCATEGORIES);
            while (data.hasNext() == true) {
            	 pstmt.setInt(1, data.nextInt());
                 pstmt.setString(2, data.next());
                 pstmt.executeUpdate();
            }
            System.out.println("\nProcessing.....Done");

        } else if(fileName.contains("manufacturer.txt")){//inserting into Manufacturer
        	pstmt = conn.prepareStatement(App.Queries.INSERTMANUFACTURERS);
            while (data.hasNext() == true) {
                pstmt.setInt(1, data.nextInt());
                pstmt.setString(2, data.next());
                pstmt.setString(3, data.next());
                pstmt.setInt(4, data.nextInt());
                pstmt.executeUpdate();
            }
            System.out.println("\nProcessing.....Done");
        } 
            else if(fileName.contains("part.txt")){//inserting into Part
            pstmt = conn.prepareStatement(App.Queries.INSERTPARTS);
            while (data.hasNext() == true) {
            	pstmt.setInt(1, data.nextInt());
                pstmt.setString(2, data.next());
                pstmt.setInt(3, data.nextInt());
                pstmt.setInt(4, data.nextInt());
                pstmt.setInt(5, data.nextInt());
                pstmt.setInt(6, data.nextInt());
                pstmt.setInt(7, data.nextInt());
                pstmt.executeUpdate();
            }
            System.out.println("\nProcessing.....Done");
        } 
            else if(fileName.contains("salesperson.txt")){//insert into Salesperson
            pstmt = conn.prepareStatement(App.Queries.INSERTSALESPERSONS);
            while (data.hasNext() == true) {
            	pstmt.setInt(1, data.nextInt());
                pstmt.setString(2, data.next());
                pstmt.setString(3, data.next());
                pstmt.setInt(4, data.nextInt());
                pstmt.setInt(5, data.nextInt());
                pstmt.executeUpdate();
            }
            System.out.println("\nProcessing.....Done");
        }

        else if(fileName.contains("transaction.txt")){//insert into TransactionRecords
        	pstmt = conn.prepareStatement(App.Queries.INSERTTRANSACTIONS);
            while (data.hasNext() == true) {
            	pstmt.setInt(1, data.nextInt());
                pstmt.setInt(2, data.nextInt());
                pstmt.setInt(3, data.nextInt());
                pstmt.setString(4, data.next());
                System.out.println(pstmt);
                pstmt.executeUpdate();
            }
            System.out.println("\nProcessing.....Done");
            //conn2.close();
        }
    }
    
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/   
    private static class Queries {

        public final static String[] CREATETABLES = {

            "CREATE TABLE Category (cID INTEGER, cName CHAR(20),PRIMARY KEY (cID), CONSTRAINT Check_cID CHECK (cID >= 1 AND cID <= 9));",
            "CREATE TABLE Manufacturer (mID INTEGER, mName CHAR(20), mAddress CHAR(50), mPhoneNumber INTEGER, PRIMARY KEY (mID), CONSTRAINT Check_mID CHECK (mID >= 1 AND mID <= 99),CONSTRAINT Check_mPhoneNumber CHECK (mPhoneNumber >= 10000000 AND mPhoneNumber<= 99999999));",
            
            "CREATE TABLE Part" + "(pID INTEGER, pName CHAR(20), pPrice INTEGER, mID INTEGER, cID INTEGER, pWarrantyPeriod INTEGER, pAvailableQuantity INTEGER, PRIMARY KEY (pID),"
            		+ "CONSTRAINT Check_pID CHECK  (pID >= 1 AND pID <= 999),"
            		+ "CONSTRAINT Check_pPrice CHECK  (pPrice >= 1 AND pPrice <= 99999),"
            		+ "CONSTRAINT Check_mID	CHECK  (mID >= 1 AND mID <= 99),"
            		+ "CONSTRAINT Check_cID	CHECK  (cID >= 1 AND cID <= 9),"
            		+ "CONSTRAINT Check_pWarrantyPeriod	CHECK  (pWarrantyPeriod >= 1 AND pWarrantyPeriod <= 99),"
            		+ "CONSTRAINT Check_pAvailableQuantity	CHECK  (pAvailableQuantity >= 1 AND pAvailableQuantity <= 99));",
            
            "CREATE TABLE Salesperson" + "(sID INTEGER, sName CHAR(20), sAddress CHAR(50), sPhoneNumber INTEGER, sExperience INTEGER, PRIMARY KEY (sID),"
            		+ "CONSTRAINT Check_sID CHECK  (sID >= 1 AND sID <= 99),"
            		+ "CONSTRAINT Check_sPhoneNumber CHECK (sPhoneNumber >= 10000000 AND sPhoneNumber<= 99999999),"
            		+ "CONSTRAINT Check_sExperience	CHECK  (sExperience >= 1 AND sExperience <= 9));",            
          
            "CREATE TABLE TransactionRecords" + "(tID INTEGER, pID INTEGER, sID INTEGER, tDate DATE NOT NULL, PRIMARY KEY (tID),"
            		+ "CONSTRAINT Check_tID CHECK  (tID >= 1 AND tID <= 9999),"
            		+ "CONSTRAINT Check_pID	CHECK  (pID >= 1 AND pID <= 999),"
            		+ "CONSTRAINT Check_sID	CHECK  (sID >= 1 AND sID <= 99));"
         };

        public final static String[] DELETETABLES = {
        	"DROP TABLE Category;",
        	"DROP TABLE Manufacturer;",
            "DROP TABLE Part;",
            "DROP TABLE Salesperson;",
            "DROP TABLE TransactionRecords;"
         };

        public final static String INSERTCATEGORIES =
            "INSERT INTO Category VALUES(?,?);";

        public final static String INSERTMANUFACTURERS =
            "INSERT INTO Manufacturer VALUES(?,?,?,?);";

        public final static String INSERTPARTS =
            "INSERT INTO Part VALUES(?,?,?,?,?,?,?);";

        public final static String INSERTSALESPERSONS =
            "INSERT INTO Salesperson VALUES(?,?,?,?,?);";

        public final static String INSERTTRANSACTIONS =
            "INSERT INTO TransactionRecords VALUES(?,?,?,to_date(?, 'DD/MM/YYYY'));";
    }
}
