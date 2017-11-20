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
	public boolean connect() throws ClassNotFoundException, SQLException {
		log.info("Connect to database: " + DATABASE_URL + " with " + DATABASE_USERNAME + " - " + DATABASE_PASSWORD);

		boolean successful = true;

		// For the first time
		if (connection == null) {

			connectToDatabase();

		} else {

			// If there has been a connection.close() call
			if (connection.isClosed()) {
				connectToDatabase();
			} else {
				successful = false;
			}

		}

		return successful;
	}

	/**
	 * Creates the connection to the database.
	 * 
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
	public boolean disconnect() throws SQLException {
		log.info("Disconnect from database.");

		if (connection != null) {
			connection.close();
		}

		return true;
	}

	@Override
	public User getUser(String username) throws SQLException {

		User user = null;

		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;

		String queryUser = "SELECT * " + "FROM APPLICATIONUSER " + "WHERE USERNAME = ?";

		preparedStatement = connection.prepareStatement(queryUser);
		preparedStatement.setString(1, username);
		resultSet = preparedStatement.executeQuery();

		if (resultSet != null) {

			if (resultSet.next()) {
				user = new User();
				user.setUserName(resultSet.getString("USERNAME"));
				user.setPasswordHash(resultSet.getString("USERPASSWORD"));
				user.setScore(resultSet.getInt("USERSCORE"));
				user.setUserLevel(KnowledgeLevel.values()[resultSet.getInt("KNOWLEDGELEVELID") - 1]);
			}

		}

		return user;
	}

	@Override
	public boolean addUser(User user) throws SQLException {
		log.info("Add user: " + user.getUsername());

		int isAdmin = user.isAdmin() ? 1 : 0;
		boolean success = false;

		String insertString = "INSERT INTO APPLICATIONUSER(ID, USERNAME, USERPASSWORD, USERSCORE, KNOWLEDGELEVELID, ISADMIN) "
				+ "VALUES (USER_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";

		PreparedStatement preparedInsert = connection.prepareStatement(insertString);
		preparedInsert.setString(1, user.getUsername());
		preparedInsert.setString(2, user.getPasswordHash());
		preparedInsert.setInt(3, user.getScore());
		preparedInsert.setInt(4, user.getUserLevel().ordinal() + 1);
		preparedInsert.setInt(5, isAdmin);

		if (preparedInsert.executeUpdate() > 0) {
			success = true;
		} else {
			success = false;
		}

		return success;
	}

	@Override
	public int deleteUser(String username) throws SQLException {
		log.info("Delete user: " + username);

		int deletedCount = -1;

		String deleteString = "DELETE FROM APPLICATIONUSER WHERE USERNAME = ?";

		PreparedStatement preparedDelete = connection.prepareStatement(deleteString);
		preparedDelete.setString(1, username);

		deletedCount = preparedDelete.executeUpdate();

		return deletedCount;
	}

	@Override
	public void updateUserScore(User user, int score) throws SQLException {
		log.info("Update " + user.getUsername() + "'s score from: " + user.getScore() + " to: " + score);

		String updateString = "UPDATE APPLICATIONUSER SET USERSCORE = ? WHERE USERNAME = ? AND USERPASSWORD = ?";

		PreparedStatement preparedUpdate = connection.prepareStatement(updateString);
		preparedUpdate.setInt(1, score);
		preparedUpdate.setString(2, user.getUsername());
		preparedUpdate.setString(3, user.getPasswordHash());

		int modified = preparedUpdate.executeUpdate();

		log.info("Number of modified rows: " + modified);
	}

	@Override
	public void updateUserLevel(User user, KnowledgeLevel level) throws SQLException {
		log.info("Update " + user.getUsername() + "'s level from: " + user.getUserLevel() + " to: " + level);

		String updateString = "UPDATE APPLICATIONUSER SET KNOWLEDGELEVELID = ? WHERE USERNAME = ? AND USERPASSWORD = ?";

		PreparedStatement preparedUpdate = connection.prepareStatement(updateString);
		preparedUpdate.setInt(1, level.ordinal() + 1);
		preparedUpdate.setString(2, user.getUsername());
		preparedUpdate.setString(3, user.getPasswordHash());

		int modified = preparedUpdate.executeUpdate();

		log.info("Number of modified rows: " + modified);
	}

	@Override
	public List<Exercise> getAllExercise(ExerciseType type) throws SQLException {
		log.info("Get all exercise with type: " + type);

		List<Exercise> exerciseList = new ArrayList<>();

		String queryString = "";

		if (type == ExerciseType.SENTENCE) {
			queryString = "SELECT * FROM SENTENCEEXERCISE";
		} else {
			queryString = "SELECT * FROM WORDEXERCISE";
		}

		PreparedStatement preparedStm = connection.prepareStatement(queryString);

		ResultSet resultSet = preparedStm.executeQuery();

		if (resultSet != null) {
			exerciseList = resultSetToExerciseList(resultSet);
		}

		return exerciseList;
	}

	@Override
	public List<Exercise> getExercisesWithUserLevel(ExerciseType type, 
			KnowledgeLevel level, boolean onlyAtLevel) throws SQLException {
		
		log.info("Get " + type + " exercise with level: " + level + ", " + onlyAtLevel);

		List<Exercise> exerciseList = new ArrayList<>();

		String table = "";
		String queryString = "";

		if (type == ExerciseType.SENTENCE) {
			table = "SENTENCEEXERCISE";
		} else {
			table = "WORDEXERCISE";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ").append(table).append(" WHERE KNOWLEDGELEVELID ");

		if (onlyAtLevel) {
			sb.append("= ");
		} else {
			sb.append("<= ");
		}
		sb.append("?");

		queryString = sb.toString();
		log.info("Query string: " + queryString);

		PreparedStatement preparedStm = connection.prepareStatement(queryString);
		preparedStm.setInt(1, level.ordinal() + 1);

		ResultSet resultSet = preparedStm.executeQuery();

		if (resultSet != null) {
			log.error("not null");
			exerciseList = resultSetToExerciseList(resultSet);
		}

		return exerciseList;
	}

	/**
	 * Converts the result set to exercise list.
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private List<Exercise> resultSetToExerciseList(ResultSet resultSet) throws SQLException {
		List<Exercise> exerciseList = new ArrayList<>();

		while (resultSet.next()) {
			Exercise exercise = new Exercise();
			exercise.setEnglish(resultSet.getString("ENGLISH"));
			exercise.setHungarian(resultSet.getString("HUNGARIAN"));
			exercise.setKnowledgeLevel(KnowledgeLevel.values()[resultSet.getInt("KNOWLEDGELEVELID") - 1]);

			exerciseList.add(exercise);
		}
		log.error("no next");

		return exerciseList;
	}

	@Override
	public int addExercise(Exercise exercise, User user) throws SQLException {
		log.info("Add execise: " + exercise.getEnglish() + " - " + exercise.getHungarian());

		int addCount = 0;

		// Get id to a user
		int userID = getUserIdByUserName(user.getUsername());

		String insertSql = "";
		if (exercise.getExerciseType() == ExerciseType.WORD) {
			insertSql = "INSERT INTO WORDEXERCISE VALUES(WORDEXERCISE_SEQ.NEXTVAL, ?, ?, ?, ?)";
		} else {
			insertSql = "INSERT INTO SENTENCEEXERCISE VALUES(SENTENCEEXERCISE_SEQ.NEXTVAL, ?, ?, ?, ?)";
		}

		PreparedStatement insertStm = connection.prepareStatement(insertSql);
		insertStm.setString(1, exercise.getEnglish());
		insertStm.setString(2, exercise.getHungarian());
		insertStm.setInt(3, userID);
		insertStm.setInt(4, exercise.getKnowledgeLevel().ordinal() + 1);

		addCount = insertStm.executeUpdate();

		return addCount;
	}

	@Override
	public boolean deleteExercise(Exercise exercise) throws SQLException {
		log.info("Delete " + exercise.getEnglish() + " - " + exercise.getHungarian());

		String deleteWord = "DELETE SENTENCEEXERCISE WHERE ENGLISH = ? AND HUNGARIAN = ?";
		String deleteSentence = "DELETE WORDEXERCISE WHERE ENGLISH = ? AND HUNGARIAN = ?";
		int deleted = 0;

		PreparedStatement preparedDeleteWord = connection.prepareStatement(deleteWord);
		preparedDeleteWord.setString(1, exercise.getEnglish());
		preparedDeleteWord.setString(2, exercise.getHungarian());
		deleted = preparedDeleteWord.executeUpdate();
		
		PreparedStatement preparedDeleteSentence = connection.prepareStatement(deleteSentence);
		preparedDeleteSentence.setString(1, exercise.getEnglish());
		preparedDeleteSentence.setString(2, exercise.getHungarian());
		deleted += preparedDeleteSentence.executeUpdate();		

		// Deleted a row if deleted is greater than zero.
		return deleted > 0;
	}

	/**
	 * Returns the corresponding id of the given user name.
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	private int getUserIdByUserName(String username) throws SQLException {
		log.info("Get user id to " + username);

		int id = -1;

		String queryString = "SELECT ID FROM APPLICATIONUSER WHERE USERNAME = ?";

		PreparedStatement preparedQuery = connection.prepareStatement(queryString);
		ResultSet rs = preparedQuery.executeQuery();

		if (rs != null) {
			if (rs.next()) {
				id = rs.getInt("ID");
			}
		}

		return id;
	}

}
