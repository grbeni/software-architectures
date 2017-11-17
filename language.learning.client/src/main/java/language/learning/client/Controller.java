package language.learning.client;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
	//model instance, controller communicate just with the model
	//Don't use javaFX imports classes, etc.
	private Model model;

	public Controller(){
		model = new Model();
	}

	/**
	 * Connect to DB with model
	 * @param userName Your DB username
	 * @param password Your DB password
	 * @param log Log container
	 * @return true if connect success else false
	 */
	public boolean connect(String userName, String password, List<String> log){
		if (model.connect(userName, password)) {
			// Test the connection
			String results = model.testConnection();
			if (results != null) {
				log.add("Connection seems to be working.");
				log.add("Connected to: '" + model.getDatabaseUrl() + "'");
				log.add(String.format("DBMS: %s, version: %s", model.getDatabaseProductName(),
						model.getDatabaseProductVersion()));
				log.add(results);
				return true;
			}
		}
		//always log
		log.add(model.getLastError());
		return false;
	}

	/**	 * Task 1: Search with keyword
	 * USE: model.search
	 * Don't forget close the statement!
	 * @param keyword the search keyword
	 * @param log Log container
	 * @return every row in a String[],and the whole table in List<String[]>
	 */
	public List<String[]> search(String keyword, List<String> log){
		List<String[]> result = null;
		ResultSet resultSet = null;
		try {
			// We except the year to be between 1000 and 9999 or empty string
			if (!keyword.matches("[1-9][0-9][0-9][0-9]") && !keyword.equals("")) {
				throw new IllegalArgumentException("Not suitable year format. A year between 1000 and 9999 is expected.");
			}
			result = new ArrayList<>();
			// Getting the result set
			resultSet = model.search(keyword); 
			// If exception happened in the model, it is in the lastError string
			if (model.getLastError() != "") {
				log.add("error " + model.getLastError());
			}
			if (resultSet != null) {
				// If the result set is empty, we throw an exception
				if (!resultSet.isBeforeFirst()) {
					throw new Exception("There is no transaction in " + keyword + ".");
				}
				// Instantiating a ResultSetMetaData, so we may know the column count
				ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
				// Filling the result list
				while (resultSet.next()) {
					String[] row = new String[resultSetMetaData.getColumnCount()];
					for (int i = 1; i<= resultSetMetaData.getColumnCount(); ++i) {			
						row[i - 1] = resultSet.getString(i);
					}
					result.add(row);
				}
			}
			return result;		
		} catch (SQLException e) {
			// Error handling
			log.add("error " + e.getMessage());
			return null;
		} catch (IllegalArgumentException e) {
			log.add("Date fieldname syntax error: " + e.getMessage());
			return null;
		} catch (Exception e) {
			log.add("error " + e.getMessage());
			return null;
		}
		finally {
			// If the result set is not null, we have to close the statement
			try {	
				if (resultSet != null && resultSet.getStatement() != null) {				
					resultSet.getStatement().close();
				}
			} catch (SQLException e) {
				log.add("error " + e.getMessage());
			}
		}
	}

	/**
	 * Task 2 and 3: Modify data (task 2) and (before) verify(task 3) it, and disable autocommit (task 4.1)
	 * USE: model.modifyData and Model.ModifyResult
	 * @param data Modify data
	 * @param AutoCommit autocommit parameter
	 * @param log Log container
	 * @return true if verify ok else false
	 */
	public boolean modifyData(Map data, boolean AutoCommit, List<String> log){
		Model.ModifyResult result = Model.ModifyResult.Error;
		try {
			// First we validate
			boolean validationResult = verifyData(data, log);
			// If the validation failed, we return immediately
			if (!validationResult) {
				return false;
			}
			// If the validation succeeded, we modify the data
			result = model.modifyData(data, AutoCommit);			
			// We return with the result of the modification
			switch (result) {
				case Error:
					log.add("error " + model.getLastError());
				break;
				case UpdateOccured:
					log.add("update occured");
				break;
				case InsertOccured:
					log.add("insert occured");
				break;
			}		
			return true;
			
		}  catch (IllegalArgumentException e) {
			log.add(e.getMessage());
			return false;
		} catch (Exception e) {
			log.add(e.getMessage());
			return false;
		} 
		

	}

	/**
	 * Task 5: get statistics
	 * USE: model.getStatistics
	 * Don't forget close the statement!
	 * @param log Log container
	 * @return every row in a String[],and the whole table in List<String[]>
	 */
	public List<String[]> getStatistics(List<String> log){
		List<String[]> result = new ArrayList<>();
		ResultSet resultSet = null;
		try {
			result = new ArrayList<>();
			// Getting the result set
			resultSet = model.getStatistics(); 
			// If exception happened in the model, it is in the lastError string
			if (model.getLastError() != "") {
				log.add("error " + model.getLastError());
			}
			if (resultSet != null) {
				// Instantiating a ResultSetMetaData, so we may know the column count
				ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
				// Filling the result list
				while (resultSet.next()) {
					String[] row = new String[resultSetMetaData.getColumnCount()];		
					row[0] = resultSet.getString(1);
					row[1] = (Double.valueOf(resultSet.getDouble(2)).toString());
					result.add(row);
				}
			}
			return result;		
		} catch (SQLException e) {
			// Error handling
			log.add("error " + e.getMessage());
			return null;
		} catch (Exception e) {
			log.add("error " + e.getMessage());
			return null;
		}
		finally {
			// If the result set is not null, we have to close the statement
			try {	
				if (resultSet != null && resultSet.getStatement() != null) {				
					resultSet.getStatement().close();
				}
			} catch (SQLException e) {
				log.add("error " + e.getMessage());
			}
		}
	}

	/**
	 * Commit all uncommitted changes
	 * USE: model.commit
	 * @param log Log container
	 * @return true if model.commit true else false
	 */
	public boolean commit(List<String> log){
		if (model.commit()) {
			log.add("commit ok");
			return true;
		}
		// If we get to this point, the commit failed
		log.add(model.lastError);
		log.add("commit failed");
		return false;
	}

	/**
	 * Verify all fields value
	 * USE it to modifyData function
	 * USE regular expressions, try..catch
	 * @param data Modify data
	 * @param log Log container
	 * @return true if all fields in Map is correct else false
	 */
	private boolean verifyData(Map data, List<String> log) {
		try {
			// If the user did not give an rtid we return immediately
			if (!data.containsKey("rtid")) {
				throw new IllegalArgumentException("RTID fieldname syntax error: rtid must be positive integer.");				
			}			
			// Else we validate all fields given in the map
			for (Object key : data.keySet()) {
				String value = null;
				// The rtid must be an integer value
				if (key.equals("rtid")) {
					value = data.get(key).toString();
					if (!value.toString().matches("(0)||([^0&&1-9]([0-9])*)")) {
						throw new IllegalArgumentException("RTID fieldname syntax error: rtid must be positive integer.");
					}
					// If it is indeed an integer value, we replace the String value with an Integer
					data.replace(key, Integer.parseInt(value.toString()));
				}
				else if (key.equals("cegnev")) {
					// The users can basically give whatever name they want if it contains at least one word character
					value = data.get(key).toString();
					if (!value.matches(".*[A-Za-z].*")) {
						throw new IllegalArgumentException("Cegnev fieldname syntax error: value must contain at least one word character.");						
					}
				}
				// The kibocsatas must be a date value with the yyyy-mm-dd format
				else if (key.equals("kibocsatas")) {
					value = data.get(key).toString();
					if (!value.matches("[1-9]([0-9]){3}-[0-3][0-9]-[0-3][0-9]")) {
						throw new IllegalArgumentException("Kibocsatas fieldname syntax error: dates must be added in the following format: YYYY-MM-DD.");
					}
					// If it is indeed a date value with the given format, we replace the String value with a Date
					data.replace(key, Date.valueOf(value.toString()));
				}
				// The nevertek must be an integer value				
				else if (key.equals("nevertek")) {
					value = data.get(key).toString();
					if (!value.matches("[^0&&1-9]([0-9])*")) {
						throw new IllegalArgumentException("Nevertek fieldname syntax error: value must be positive integer, cannot start with 0.");
					}
					// If it is indeed an integer value, we replace the String value with an Integer
					data.replace(key, Integer.parseInt(value.toString()));
				}	
				// The arfolyam must be an integer value				
				else if (key.equals("arfolyam")) {
					value = data.get(key).toString();
					if (!value.matches("[^0&&1-9]([0-9])*")) {
						throw new IllegalArgumentException("Arfolyam fieldname syntax error: value must be positive integer, cannot start with 0.");
					}
					// If it is indeed an integer value, we replace the String value with an Integer
					data.replace(key, Integer.parseInt(value.toString()));
				}
				else if (key.equals("megjegyzes")) {
					// The users can basically give whatever comment they want if it contains at least one word character
					value = data.get(key).toString();
					if (!value.matches(".*[A-Za-z].*")) {
						throw new IllegalArgumentException("Megjegyzes fieldname syntax error: value must contain at least one word character.");
						
					}
				}
				// The befektetoid must be an integer value
				else if (key.equals("befektetoid")) {
					value = data.get(key).toString();
					if (!value.matches("(0)||([^0&&1-9]([0-9])*)")) {
						throw new IllegalArgumentException("BefektetoID fieldname syntax error: value must be positive integer.");
					}
					// If it is indeed an integer value, we replace the String value with an Integer
					data.replace(key, Integer.parseInt(value.toString()));
				}
				// If there is another field in the map, something is very wrong
				else {
					throw new IllegalArgumentException("Not expected column name: " + key.toString());
				}
			}
			return true;
			
		} catch (NumberFormatException e) {
			log.add(e.getMessage());
			return false;
		} catch (IllegalArgumentException e) {
			log.add(e.getMessage());
			return false;
		} catch (Exception e) {
			log.add(e.getMessage());
			return false;
		}
	}

}
