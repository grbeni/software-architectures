package language.learning.database;

import org.apache.log4j.Logger;

import language.learning.logger.LoggerWrapper;
import language.learning.user.User;
import language.learning.user.UserLevel;

public class Database implements IDatabase {

	// Lock object
	private static Object lockObject = new Object();
	
	private static final Logger log = (new LoggerWrapper(Database.class.getName())).getLog();
	private static Database instance = null;
	
	// TODO create connection
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
	public User getUser(String username) {
		// TODO
		User dummyUser = new User();
		dummyUser.setUserName("dummy");
		dummyUser.setPasswordHash(12345);
		dummyUser.setUserLevel(UserLevel.BEGINNER);
		dummyUser.setScore(0);
		
				
		return dummyUser;
	}

	@Override
	public int addExercise() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
