package language.learning.database;

import org.apache.log4j.Logger;

import language.learning.datatype.User;
import language.learning.logger.LoggerWrapper;

public class Database implements IDatabase {

	
	private static final Logger log = (new LoggerWrapper(Database.class.getName())).getLog();
	private static Database instance = null;
	
	// TODO create connection
	private Database() {
		log.trace("Database created");
	}
	
	public static Database getInstance() {
		log.trace("Database instance getter");
		
		if (instance == null) {
			instance = new Database();
		}
		
		return instance;		
	}

	@Override
	public User getUser(String username) {
		// TODO
		User dummyUser = new User();
		dummyUser.setUsername("dummy");
		dummyUser.setPasswordHash(12345);
				
		return dummyUser;
	}

	@Override
	public boolean addExerciseSentence() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addExerciseWord() {
		// TODO Auto-generated method stub
		return true;
	}
	
}
