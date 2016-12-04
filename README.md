# sales_system_db
 Implementation of a sales system for a computer part store so that all information about transactions, computer parts and salespersons is stored.

Files:
	
	App.java | contains the source code for our Sales System Java application
	
	App.class | byte code for App class, created after compilation of App.java
	
	App$Queries.class | byte code for Queries class, created after compilation of App.java
	
	mysql-jdbc.jar | jar provided for JDBC Oracle Database Driver
	
	sample_data | directory containing sample data (4 files listed below)
	 				->category.txt, manufacturer.txt, part.txt, salesperson.txt, transaction.txt
	
	
Methods of compilation and execution (Must connect to CSE VPN to use their MYSQL Server):
	1) Jump into the folder ——— (cd G53/)
	2) Compile java source code ——— (javac App.java)
	2) Execute the Sales System ——— (java -classpath mysql-jdbc.jar:. App) 


*DISCLAIMER: When you select Load Data into Tables in the Administrator Menu, you will be prompted to type in the Source Data Folder Path meaning you must input an absolute or relative path not just the folder name if the folder is not contained within the G53 directory.

