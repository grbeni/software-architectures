package language.learning.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import language.learning.exercise.Exercise;
import language.learning.exercise.ExerciseLevel;
import language.learning.exercise.ExerciseType;
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

	// Object representing the connection
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
	 * 
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
		log.trace("Connect to database: " + DATABASE_URL + " with " + DATABASE_USERNAME + " - " + DATABASE_PASSWORD);

		boolean successful = true;

		// For the first time
		if (connection == null) {
			try {
				connectToDatabase();
			} catch (SQLException | ClassNotFoundException e) {
				log.error(e.getMessage());

				successful = false;
			}
		} 
		else {
			try {
				// If there has been a connection.close() call
				if (connection.isClosed()) {
					connectToDatabase();
				} else {
					successful = false;
				}
			} catch (SQLException | ClassNotFoundException e) {
				log.error(e.getMessage());
			}
		}

		return successful;
	}
	
	/**
	 * Creates the connection to the database.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void connectToDatabase() throws ClassNotFoundException, SQLException {
		Class.forName(DATABASE_DRIVER);
		connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
		// Maybe Serializable is a bit extreme
		connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
	}

	@Override
	public boolean disconnect() {
		log.trace("Disconnect from database.");

		boolean successful = true;

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
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

		String queryUser = "SELECT * " + "FROM APPLICATIONUSER " + "WHERE USERNAME = ?";
		log.error("username: " + username);
		try {
			preparedStatement = connection.prepareStatement(queryUser);
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();

		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		if (resultSet != null) {
			log.error("result set is not null");
			try {
				if (resultSet.next()) {
					log.error("resultSet has next");
					user = new User();
					user.setUserName(resultSet.getString("USERNAME"));
					user.setPasswordHash(resultSet.getInt("USERPASSWORD"));
					user.setScore(resultSet.getInt("USERSCORE"));
					user.setUserLevel(UserLevel.values()[resultSet.getInt("KNOWLEDGELEVELID") - 1]);
				}
				else {
					log.error("resultSet does not have next");
				}
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		return user;
	}

	@Override
	public int addExercise(Exercise exercise, ExerciseType type) {
		log.trace("Add execise: " + exercise.getEnglish() + " - " + exercise.getHungarian());

		int nextVal = 0;

		// Query the next value of the ID to be inserted
		String sequenceNextQuery = "SELECT EXERCISE_SEQ.NEXTVAL FROM DUAL";
		try {
			PreparedStatement sequenceStm = connection.prepareStatement(sequenceNextQuery);
			ResultSet rs = sequenceStm.executeQuery();

			if (rs != null) {
				if (rs.next()) {
					nextVal = rs.getInt(1);
					log.trace("EXERCISE_SEQ.NEXTVAL: " + nextVal);
				}
			}

		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		if (nextVal != 0) {
			String insertSql = "";
			if (type == ExerciseType.WORD) {
				insertSql = "INSERT INTO WORDEXERCISE VALUES(?, ?, ?, ?)";
			} else {
				insertSql = "INSERT INTO SENTENCEEXERCISE VALUES(?, ?, ?, ?)";
			}
			try {
				log.error(insertSql);
				PreparedStatement insertStm = connection.prepareStatement(insertSql);
				
				insertStm.setInt(1, nextVal);
				insertStm.setString(2, exercise.getEnglish());
				insertStm.setString(3, exercise.getHungarian());
				insertStm.setInt(4, exercise.getExerciseLevel().ordinal() + 1);

				insertStm.executeUpdate();

			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		return nextVal;
	}

	@Override
	public List<Exercise> getAllExercise(ExerciseType type) {
		log.trace("Get all exercise with type: " + type);
		
		List<Exercise> exerciseList = new ArrayList<>();
		
		String queryString = "";
		
		if (type == ExerciseType.SENTENCE) {
			queryString = "SELECT * FROM SENTENCEEXERCISE";
		}
		else {
			queryString = "SELECT * FROM WORDEXERCISE";
		}
		
		try {
			PreparedStatement preparedStm = connection.prepareStatement(queryString);
			
			ResultSet resultSet = preparedStm.executeQuery();
			
			if (resultSet != null) {
				while (resultSet.next()) {
					Exercise exercise = new Exercise();
					exercise.setEnglish(resultSet.getString("ENGLISH"));
					exercise.setHungarian(resultSet.getString("HUNGARIAN"));
					exercise.setExerciseLevel(ExerciseLevel.values()[resultSet.getInt("KNOWLEDGELEVELID") - 1]);
					
					exerciseList.add(exercise);
				}
			}
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		return exerciseList;
	}

}
