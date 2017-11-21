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
import language.learning.exercise.ExerciseWithImage;
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
				user.setUsername(resultSet.getString("USERNAME"));
				user.setPasswordHash(resultSet.getString("USERPASSWORD"));
				user.setScore(resultSet.getInt("USERSCORE"));
				int isadmin = resultSet.getInt("ISADMIN");
				user.setAdmin(isadmin == 1);
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
			exerciseList = resultSetToExerciseList(resultSet, type);
		}

		return exerciseList;
	}

	@Override
	public List<Exercise> getExercisesWithUserLevel(ExerciseType type, KnowledgeLevel level, boolean onlyAtLevel)
			throws SQLException {

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
			exerciseList = resultSetToExerciseList(resultSet, type);
		}

		return exerciseList;
	}

	/**
	 * Converts the result set to exercise list.
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private List<Exercise> resultSetToExerciseList(ResultSet resultSet, ExerciseType tpye) throws SQLException {
		List<Exercise> exerciseList = new ArrayList<>();

		while (resultSet.next()) {
			Exercise exercise = new Exercise();
			exercise.setEnglish(resultSet.getString("ENGLISH"));
			exercise.setHungarian(resultSet.getString("HUNGARIAN"));
			exercise.setKnowledgeLevel(KnowledgeLevel.values()[resultSet.getInt("KNOWLEDGELEVELID") - 1]);
			exercise.setExerciseType(tpye);

			exerciseList.add(exercise);
		}
		log.error("Returned exercises: " + exerciseList);

		return exerciseList;
	}

	@Override
	public int addExercise(Exercise exercise, String username) throws SQLException {
		log.info("Add execise: " + exercise.getEnglish() + " - " + exercise.getHungarian());

		int addCount = 0;

		// Get id to a user
		int userID = getUserIdByUserName(username);

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
	public int addImageExercise(ExerciseWithImage exercise, String username) throws SQLException {
		log.info("Insert exercise with image " + exercise.getEnglish());

		int addCount = 0;

		// Get id to a user
		int userID = getUserIdByUserName(username);

		String insertSql = "INSERT INTO IMAGEEXERCISE VALUES(IMAGEEXERCISE_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";

		PreparedStatement insertStm = connection.prepareStatement(insertSql);
		insertStm.setString(1, exercise.getEnglish());
		insertStm.setString(2, exercise.getHungarian());
		insertStm.setInt(3, userID);
		insertStm.setInt(4, exercise.getKnowledgeLevel().ordinal() + 1);
		insertStm.setBytes(5, exercise.getImage());

		addCount = insertStm.executeUpdate();

		return addCount;
	}

	@Override
	public boolean deleteExercise(Exercise exercise, String username) throws SQLException {
		log.info("Delete " + exercise.getEnglish() + " - " + exercise.getHungarian());

		if (hasAuthority(username, exercise.getEnglish())) {

		} else {
			return false;
		}

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

	private boolean hasAuthority(String username, String english) throws SQLException {

		boolean isAdmin = false;
		int id = -1;

		String queryUser = "SELECT ID, ISADMIN FROM APPLICATIONUSER WHERE USERNAME = ?";
		PreparedStatement selectUser = connection.prepareStatement(queryUser);
		selectUser.setString(1, username);
		ResultSet rs = selectUser.executeQuery();

		if (rs != null) {
			if (rs.next()) {
				int admin = rs.getInt("ISADMIN");
				isAdmin = admin == 1;
				id = rs.getInt("ID");
			} else {
				return false;
			}
		} else {
			return false;
		}

		if (isAdmin) {
			return true;
		}

		String queryWord = "SELECT * FROM WORDEXERCISE WHERE USERID = ? AND ENGLISH = ?";
		String querySentence = "SELECT * FROM SENTENCEEXERCISE WHERE USERID = ? AND ENGLISH = ?";
		String queryImage = "SELECT * FROM IMAGEEXERCISE WHERE USERID = ? AND ENGLISH = ?";

		PreparedStatement selectWord = connection.prepareStatement(queryWord);
		selectWord.setInt(1, id);
		selectWord.setString(2, english);
		ResultSet rsW = selectWord.executeQuery();

		PreparedStatement selectSentence = connection.prepareStatement(querySentence);
		selectSentence.setInt(1, id);
		selectSentence.setString(2, english);
		ResultSet rsS = selectSentence.executeQuery();

		PreparedStatement selectImage = connection.prepareStatement(queryImage);
		selectImage.setInt(1, id);
		selectImage.setString(2, english);
		ResultSet rsI = selectImage.executeQuery();

		if (rsW != null) {
			if (rsW.next()) {
				return true;
			}
		}
		if (rsS != null) {
			if (rsS.next()) {
				return true;
			}
		}
		if (rsI != null) {
			if (rsI.next()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the corresponding id of the given user name.
	 * 
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

	@Override
	public List<ExerciseWithImage> getExerciseWithImage(KnowledgeLevel level, boolean onlyAtLevel) throws SQLException {
		log.info("Get exercises with image " + level);

		List<ExerciseWithImage> exerciseList = new ArrayList<>();

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM IMAGEEXERCISE WHERE KNOWLEDGELEVELID ");

		if (onlyAtLevel) {
			sb.append("= ");
		} else {
			sb.append("<= ");
		}

		sb.append("?");

		String queryString = sb.toString();
		log.info("Query string: " + queryString);

		PreparedStatement preparedQuery = connection.prepareStatement(queryString);
		preparedQuery.setInt(1, level.ordinal() + 1);

		ResultSet resultSet = preparedQuery.executeQuery();

		if (resultSet != null) {
			while (resultSet.next()) {
				ExerciseWithImage ex = new ExerciseWithImage();
				ex.setEnglish(resultSet.getString("ENGLISH"));
				ex.setHungarian(resultSet.getString("HUNGARIAN"));
				ex.setExerciseType(ExerciseType.IMAGE);
				ex.setKnowledgeLevel(level);
				ex.setImage(resultSet.getBytes("IMAGE"));

				exerciseList.add(ex);
			}
		}

		return exerciseList;
	}

}
