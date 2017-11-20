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
import language.learning.exercise.ExerciseType;
import language.learning.exercise.KnowledgeLevel;
import language.learning.logger.LoggerWrapper;
import language.learning.user.User;

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
		log.info("Database created");
	}

	/**
	 * Method for accessing the database object.
	 * 
	 * @return singleton database instance
	 */
	synchronized public static Database getInstance() {
		log.info("Database instance getter");

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
		log.info("Connect to database: " + DATABASE_URL + " with " + DATABASE_USERNAME + " - " + DATABASE_PASSWORD);

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
		connection.setAutoCommit(true);
	}

	@Override
	public boolean disconnect() {
		log.info("Disconnect from database.");

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
		
		try {
			preparedStatement = connection.prepareStatement(queryUser);
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();

		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		if (resultSet != null) {
			
			try {
				if (resultSet.next()) {
					user = new User();
					user.setUserName(resultSet.getString("USERNAME"));
					user.setPasswordHash(resultSet.getString("USERPASSWORD"));
					user.setScore(resultSet.getInt("USERSCORE"));
					user.setUserLevel(KnowledgeLevel.values()[resultSet.getInt("KNOWLEDGELEVELID") - 1]);
				}
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		return user;
	}

	
	@Override
	public boolean addUser(User user) {
		log.info("Add user: " + user.getUsername());
		
		int isAdmin = user.isAdmin() ? 1 : 0;
		boolean success = false;
		
		String insertString = 
				"INSERT INTO APPLICATIONUSER(ID, USERNAME, USERPASSWORD, USERSCORE, KNOWLEDGELEVELID, ISADMIN) "
				+ "VALUES (USER_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";		
		
		try {
			PreparedStatement preparedInsert = connection.prepareStatement(insertString);
			preparedInsert.setString(1, user.getUsername());
			preparedInsert.setString(2, user.getPasswordHash());
			preparedInsert.setInt(3, user.getScore());
			preparedInsert.setInt(4, user.getUserLevel().ordinal() + 1);
			preparedInsert.setInt(5, isAdmin);
			
			if (preparedInsert.executeUpdate() > 0) {
				success = true;
			}
			else {
				success = false;
			}			
			
		} catch (SQLException e) {
			log.error(e.getMessage());
			success = false;
		}
		
		return success;
	}

	@Override
	public int deleteUser(String username) {
		log.info("Delete user: " + username);
		
		int deletedCount = -1;
		
		String deleteString = "DELETE FROM APPLICATIONUSER WHERE USERNAME = ?";
		
		try {
			PreparedStatement preparedDelete = connection.prepareStatement(deleteString);
			preparedDelete.setString(1, username);
			
			deletedCount = preparedDelete.executeUpdate();
			
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		return deletedCount;
	}
	
	@Override
	public void updateUserScore(User user, int score) {
		log.info("Update " + user.getUsername() + "'s score from: " + user.getScore() + " to: " + score);
		
		String updateString = "UPDATE APPLICATIONUSER SET USERSCORE = ? WHERE USERNAME = ? AND USERPASSWORD = ?";
		
		try {
			PreparedStatement preparedUpdate = connection.prepareStatement(updateString);
			preparedUpdate.setInt(1, score);
			preparedUpdate.setString(2, user.getUsername());
			preparedUpdate.setString(3, user.getPasswordHash());
			
			int modified = preparedUpdate.executeUpdate();
			
			log.info("Number of modified rows: " + modified);
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public void updateUserLevel(User user, KnowledgeLevel level) {
		log.info("Update " + user.getUsername() + "'s level from: " + user.getUserLevel() + " to: " + level);
		
		String updateString = "UPDATE APPLICATIONUSER SET KNOWLEDGELEVELID = ? WHERE USERNAME = ? AND USERPASSWORD = ?";
		
		try {
			PreparedStatement preparedUpdate = connection.prepareStatement(updateString);
			preparedUpdate.setInt(1, level.ordinal() + 1);
			preparedUpdate.setString(2, user.getUsername());
			preparedUpdate.setString(3, user.getPasswordHash());
			
			int modified = preparedUpdate.executeUpdate();
			
			log.info("Number of modified rows: " + modified);
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public List<Exercise> getAllExercise(ExerciseType type) {
		log.info("Get all exercise with type: " + type);
		
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
				exerciseList = resultSetToExerciseList(resultSet);
			}
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		return exerciseList;
	}

	@Override
	public List<Exercise> getExercisesWithUserLevel(ExerciseType type, KnowledgeLevel level, boolean onlyAtLevel) {
		log.info("Get " + type + " exercise with level: " + level + ", " + onlyAtLevel);
		
		List<Exercise> exerciseList = new ArrayList<>();
		
		String table = "";
		String queryString = "";
		
		if (type == ExerciseType.SENTENCE) {
			table = "SENTENCEEXERCISE";
		}
		else {
			table = "WORDEXERCISE";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ").append(table).append(" WHERE KNOWLEDGELEVELID ");
		
		if (onlyAtLevel) {
			sb.append("= ");
		}
		else {
			sb.append("<= ");
		}
		sb.append("?");
		
		queryString = sb.toString();
		log.info("Query string: " + queryString);
		
		try {
			PreparedStatement preparedStm = connection.prepareStatement(queryString);
			preparedStm.setInt(1, level.ordinal() + 1);
			
			ResultSet resultSet = preparedStm.executeQuery();
			
			if (resultSet != null) {
				log.error("not null");
				exerciseList = resultSetToExerciseList(resultSet);
			}
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		
		return exerciseList;
	}
	
	private List<Exercise> resultSetToExerciseList(ResultSet resultSet) throws SQLException {
		List<Exercise> exerciseList = new ArrayList<>();
		
		while (resultSet.next()) {
			Exercise exercise = new Exercise();
			exercise.setEnglish(resultSet.getString("ENGLISH"));
			exercise.setHungarian(resultSet.getString("HUNGARIAN"));
			exercise.setExerciseLevel(KnowledgeLevel.values()[resultSet.getInt("KNOWLEDGELEVELID") - 1]);
			
			exerciseList.add(exercise);
		}
		log.error("no next");
		
		return exerciseList;
	}

	@Override
	public int addExercise(Exercise exercise, ExerciseType type) {
		log.info("Add execise: " + exercise.getEnglish() + " - " + exercise.getHungarian());

		int nextVal = 0;

		// Query the next value of the ID to be inserted
		String sequenceNextQuery = "SELECT EXERCISE_SEQ.NEXTVAL FROM DUAL";
		try {
			PreparedStatement sequenceStm = connection.prepareStatement(sequenceNextQuery);
			ResultSet rs = sequenceStm.executeQuery();

			if (rs != null) {
				if (rs.next()) {
					nextVal = rs.getInt(1);
					log.info("EXERCISE_SEQ.NEXTVAL: " + nextVal);
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
	public boolean deleteExercise(Exercise exercise) {
		log.info("Delete " + exercise.getEnglish() + " - " + exercise.getHungarian());
		
		String deleteString = "";
		int deleted = 0;
		
		if (exercise.getExerciseType() == ExerciseType.SENTENCE) {
			deleteString = "DELETE SENTENCEEXERCISE WHERE ENGLISH = ? AND HUNGARIAN = ?";
		}
		else {
			deleteString = "DELETE WORDEXERCISE WHERE ENGLISH = ? AND HUNGARIAN = ?";
		}
		
		try {
			PreparedStatement preparedDelete = connection.prepareStatement(deleteString);
			deleted = preparedDelete.executeUpdate();
			
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
		
		// Deleted a row if deleted is greater than zero.
		return deleted > 0;
	}


}
