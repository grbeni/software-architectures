/**
 * This JavaFX skeleton is provided for the Software Laboratory 5 course. Its structure
 * should provide a general guideline for the students.
 * As suggested by the JavaFX model, we'll have a GUI (view),
 * a controller class and a model (this one).
 */

package language.learning.client;

import java.sql.*;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

// Model class
public class Model {

	// Database driver and URL
	protected static final String driverName = "oracle.jdbc.driver.OracleDriver";
	protected static final String databaseUrl = "jdbc:oracle:thin:@rapid.eik.bme.hu:1521:szglab";

	// Product name and product version of the database
	protected String databaseProductName = null;
	protected String databaseProductVersion = null;

	// Connection object
	protected Connection connection = null;
	
	// Default transaction values for task 4.1
	private final int AMOUNT = 1;
	private final int PRICE = 1000;

	// Enum structure for Exercise #2
	protected enum ModifyResult {
		InsertOccured, UpdateOccured, Error
	}

	// String containing last error message
	protected String lastError = "";

	/**
	 * Model constructor
	 */
	public Model() {
	}

	/**
	 * Gives product name of the database
	 *
	 * @return Product name of the database
	 */
	public String getDatabaseProductName() {

		return databaseProductName;

	}

	/**
	 * Gives product version of the database
	 *
	 * @return Product version of the database
	 */
	public String getDatabaseProductVersion() {

		return databaseProductVersion;

	}

	/**
	 * Gives database URL
	 *
	 * @return Database URL
	 */
	public String getDatabaseUrl() {

		return databaseUrl;

	}

	/**
	 * Gives the message of last error
	 *
	 * @return Message of last error
	 */
	public String getLastError() {

		return lastError;

	}

	/**
	 * Tries to connect to the database
	 *
	 * @param userName
	 *            User who has access to the database
	 * @param password
	 *            User's password
	 * @return True on success, false on fail
	 */
	public boolean connect(String userName, String password, List<String> log) {	
		
		return true;
	}

	/**
	 * Method for Exercise #1
	 * @param Search keyword
	 * @return Result of the query
	 * @throws SQLException 
	 */
	public ResultSet search(String keyword) {
		// Deleting the last error message
		lastError = "";
		if (connection == null) {
			lastError = "The connection is not established.";
			return null;
		}
		// We are going to use prepared statement
		ResultSet result = null;
		try {
			if (keyword.equals("")) {
				// If the text field is empty, we list all the transactions
				PreparedStatement searchStatement = connection.prepareStatement("select b.NEV, r.CEGNEV, t.MENNYISEG, t.EGYSEGAR from BEFEKTETO b "
														+ "inner join TRANZAKCIO t on b.BEFEKTETOID = t.BEFEKTETOID "
														+ "inner join RESZVENYTIPUS r on r.RTID = t.RTID");		
				result = searchStatement.executeQuery();
			}
			else {
				// Else we list the transactions of the given year
				PreparedStatement searchStatement = connection.prepareStatement("select b.NEV, r.CEGNEV, t.MENNYISEG, t.EGYSEGAR from BEFEKTETO b "
																+ "inner join TRANZAKCIO t on b.BEFEKTETOID = t.BEFEKTETOID "
																+ "inner join RESZVENYTIPUS r on r.RTID = t.RTID "
																+ "where extract(year from t.DATUM) = ?");
				
				// Parsing the year into an integer
				searchStatement.setInt(1, Integer.parseInt(keyword));			
				result = searchStatement.executeQuery();
			}
			return result;
			// The statement is going to be closed by the Controller
		} catch (SQLException e) {
			// In case of error we modify the lastError string, so the Controller can log it
			lastError = "error " + e.getMessage(); 
			return null;
		}
	}

	/**
	 * Method for Exercise #2-#3
	 *
	 * @param data
	 *            New or modified data
	 * @param AutoCommit set the connection type (use default true, and 4.1 use false
	 * @return Type of action has been performed
	 */
	public ModifyResult modifyData(Map data, boolean AutoCommit) {
		// Deleting the last error message
		lastError = "";
		ModifyResult result = ModifyResult.Error;
		if (connection == null) {
			lastError = "The connection is not established.";
			return result;
		}
		PreparedStatement countStatement = null;
		Statement statement = null;
		try {
			// We check whether the given rtid is in the table
			int rtid = (Integer) data.get("rtid");
			countStatement = connection.prepareStatement("select count(*) from RESZVENYTIPUS where rtid = ?");
			countStatement.setInt(1, rtid);
			ResultSet resultSet = countStatement.executeQuery();
			resultSet.next();
			// Creating the a statement, that the insert or the update method will fill up
			statement = connection.createStatement();
			// If the result is 0 then we need an insert
			if (resultSet.getInt(1) == 0) {
				// Setting the auto commit
				connection.setAutoCommit(AutoCommit);
				insert(data, statement);
				result = ModifyResult.InsertOccured;
			}
			// If the result is not 0 (then it must be 1), we need an update
			else {
				update(data, statement);
				result = ModifyResult.UpdateOccured;
			}

			return result;
			
		} catch (SQLException e) {
			lastError = e.getMessage();
			try {
				if (!connection.getAutoCommit()) {
					lastError += "rollback occured";
					rollback();
				}
			} catch (SQLException e1) {
				lastError = e.getMessage();
			}
			return result;
		} catch (Exception e) {
			lastError = e.getMessage();
			return result;
		} finally {
			// We close all the created statements
			try {
				countStatement.close();	
				statement.close();
			} catch (SQLException e) {
				lastError = e.getMessage();
			}
		}

	}
	
	/**
	 * Creates an insert statement from the given Map and executes it.
	 * @param data Validated data 
	 * @param statement The Statement created in the modifyData method
	 * @throws SQLException
	 */
	private void insert(Map data, Statement statement) throws SQLException {
		StringBuilder columns = new StringBuilder("(");
		StringBuilder values = new StringBuilder("values(");
		// We need to check what columns the user wants to insert so we create the statement accordingly
		for (Object entry : data.keySet()) {
			if (!entry.equals("befektetoid")) {
				columns.append(entry.toString() + ",");
				values.append(setFormat(data.get(entry)) + ",");
			}
		} 
		// If the enumeration ended, the strings end with an ',' that has to be changed to ')' 
		if (columns.toString().endsWith(",") && values.toString().endsWith(",")) {
			columns.replace(columns.length() - 1, columns.length(), ") ");
			values.replace(values.length() - 1, values.length(), ")");
		}
		// Executing the command
		String command = "insert into RESZVENYTIPUS " + columns + " " + values + " ";
		statement.executeUpdate(command);
		
		// If the befektetoID is given, we create a transaction entry
		if (data.containsKey("befektetoid")) {
			createTransaction((Integer) data.get("befektetoid"), (Integer) data.get("rtid"), AMOUNT, PRICE);
		}
	}
	
	/**
	 * Creates an entry in the TRANZAKCIO table with the given parameters.
	 * @param befektetoid 
	 * @param rtid
	 * @param amount
	 * @param price
	 * @throws SQLException
	 */
	private void createTransaction(Integer befektetoid, Integer rtid, Integer amount, Integer price) throws SQLException {
		PreparedStatement preparedStatement = null;
		Statement maxTRID = null;
		try {
			// Preparing an insert statement
			preparedStatement = connection.prepareStatement("insert into TRANZAKCIO " 
																				+ "(TRID,BEFEKTETOID,RTID,MENNYISEG,EGYSEGAR) " 
																				+ "values(?,?,?,?,?)");
			maxTRID = connection.createStatement();
			// Getting the next available TRID
			ResultSet resultSet = maxTRID.executeQuery("select max(TRID) from TRANZAKCIO");
			resultSet.next();
			int trid = resultSet.getInt(1) + 1;
			// Giving the parameters
			preparedStatement.setInt(1, trid);
			preparedStatement.setInt(2, befektetoid);
			preparedStatement.setInt(3, rtid);
			preparedStatement.setInt(4, amount);
			preparedStatement.setInt(5, price);
			preparedStatement.executeUpdate();
		} finally {				
			// We close the statements
			preparedStatement.close();				
			maxTRID.close();
		}
	}

	/**
	 * Creates an update statement from the given Map and executes it.
	 * @param data Validated data 
	 * @param statement The Statement created in the modifyData method
	 * @throws SQLException
	 */
	private void update(Map data, Statement statement) throws SQLException {
		StringBuilder values = new StringBuilder();
		// We need to check what columns the user wants to update so we create the statement accordingly
		for (Object entry : data.keySet()) {
			// The rtid field cannot be changed, we only update the other fields
			if (!entry.equals("rtid") && !entry.equals("befektetoid")) {
				values.append(entry.toString() + "=" + setFormat(data.get(entry)) + ",");
			}
		}
		// If the enumeration ended, the strings end with an ',' that has to be changed to ')' 
		if (values.toString().endsWith(",")) {
			values.replace(values.length() - 1, values.length(), "");
		}
		// If the user granted data apart from the rtid, we execute the statement
		if (values.length() != 0) {
			String command = "update RESZVENYTIPUS set " + values + " where RTID = " + setFormat(data.get("rtid"));
			statement.executeUpdate(command);
		}
	}
	
	/**
	 * Creates the proper SQL value format according to the real type of the object.
	 * @param value The value that we want to format according to SQL syntax
	 * @return The formatted string
	 */
	private String setFormat(Object value) {
		if (value instanceof Date) {
			return "TO_DATE('" + value.toString() + "', 'yyyy-mm-dd')";
		}
		if (value instanceof String) {
			return "'" + value.toString() + "'";
		}
		else {
			return value.toString();
		}
	}
	

	/**
	 * Method for Exercise #4
	 *
	 * @return True on success, false on fail
	 */
	public boolean commit() {
		try {
			if (connection == null || connection.getAutoCommit()) {
				return false;
			}
			connection.commit();
			// If the commit is successful we set auto commit back to true
			connection.setAutoCommit(true);
			return true;
		} catch (SQLException e) {
			lastError = e.getMessage();
			return false;
		}
	}

	/**
	 * Method for Exercise #4
	 */
	public void rollback(){
		try {
			connection.rollback();
			// If the rollback is successful  we set auto commit back to true
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			lastError = e.getMessage();
		}
	}

	/**
	 * Method for Exercise #5
	 *
	 * @return Result of the query
	 */
	public ResultSet getStatistics() {
		// Deleting the last error message
		lastError = "";
		if (connection == null) {
			lastError = "The connection is not established.";
			return null;
		}
		// Complex select command 
		String command = "(select r.CEGNEV, count(*)/decode(months_between(sysdate, min(t.DATUM)), 0, 1/60, months_between(sysdate, min(t.DATUM))) as ATLAG "
							+ "from reszvenytipus r "
							+ "inner join tranzakcio t on r.rtid = t.rtid "
							+ "inner join befekteto b on b.BEFEKTETOID = t.BEFEKTETOID "
							+ "where b.KESZPENZ > 1000000 "
							+ "group by r.cegnev) "
							+ "union "
							+ "(select r.cegnev, 0 "
							+ "from reszvenytipus r "
							+ "where r.CEGNEV not in (select r.CEGNEV "
							+ "from reszvenytipus r "
							+ "inner join tranzakcio t on r.rtid = t.rtid "
							+ "inner join befekteto b on b.BEFEKTETOID = t.BEFEKTETOID "
							+ "where b.KESZPENZ > 1000000)) "
							+ "order by ATLAG";
		try {
			// Creating the statement and returning it
			PreparedStatement statisticStatement = connection.prepareStatement(command);
			ResultSet resultSet = statisticStatement.executeQuery();
			return resultSet;
		} catch (SQLException e) {
			lastError = e.getMessage();
		}
		return null;

	}

}
