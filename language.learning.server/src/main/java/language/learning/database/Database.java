package language.learning.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.jws.soap.SOAPBinding.Use;

import org.apache.log4j.Logger;

import language.learning.logger.LoggerWrapper;
import language.learning.user.User;
import language.learning.user.UserLevel;

public class Database implements IDatabase {

	private static final Logger log = (new LoggerWrapper(Database.class.getName())).getLog();

	// Lock object
	private static Object lockObject = new Object();
	
	// Database connection parameters
	private static final String DATABASE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String DATABASE_URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String DATABASE_USERNAME = "SYSTEM";
	private static final String DATABASE_PASSWORD = "password";
	
	private Connection connection = null;
	
	// Singleton instance of this class
	private static Database instance = null;
	
	/**
	 * Default constructor. Private because of the singleton pattern.
	 */
	private Database() {
		log.trace("Database created");
	}
	
	/**
	 * Method for accessing the database object.	
	 * @return singleton database instance
	 */
	synchronized public static Database getInstance() {
		log.trace("Database instance getter");
		
		// Double-checked locking
		if (instance == null) {			
			synchronized (lockObject) {				
				if (instance == null) {	
					
					instance = new Database();
				}
			}
		}
		
		return instance;		
	}
	
	@Override
	public boolean connect() {
		
		boolean successful = true;
		
		if (connection == null) {
			try {
				Class.forName(DATABASE_DRIVER);
				connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			} catch (SQLException | ClassNotFoundException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				successful = false;
			}
		}
		
		return successful;
	}

	@Override
	public User getUser(String username) {
		
		User user = null;
		
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		
		
		String queryUser = 
		"SELECT * " 
		+ "FROM APPLICATIONUSER " 
		+ "WHERE USERNAME = ?";
		
		try {
			preparedStatement = connection.prepareStatement(queryUser);
			preparedStatement.setString(1, "dummyuser");			
			resultSet = preparedStatement.executeQuery();
			log.error("parameterized statement executed");
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		if (resultSet != null) {
			try {
				if (resultSet.next()) {
					log.error("creating new user");
					user = new User();
					user.setUserName(resultSet.getString("USERNAME"));
					user.setPasswordHash(resultSet.getInt("USERPASSWORD"));
					user.setScore(resultSet.getInt("USERSCORE"));
					System.err.println(UserLevel.values()[0]);
					//System.err.println(resultSet.getInt(4));
					user.setUserLevel(UserLevel.values()[resultSet.getInt("KNOWLEDGELEVELID") - 1]);
				}
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
		else {
			log.error("result set is null");
		}
						
		return user;
	}

	@Override
	public int addExercise() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
