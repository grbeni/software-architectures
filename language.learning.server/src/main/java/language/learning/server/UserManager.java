package language.learning.server;

import org.apache.log4j.Logger;

import language.learning.database.Database;
import language.learning.database.IDatabase;
import language.learning.logger.LoggerWrapper;
import language.learning.user.User;
import language.learning.user.UserLevel;

public class UserManager implements IUserManager {

	private static final Logger log = (new LoggerWrapper(UserManager.class.getName())).getLog();
	
	private IDatabase db = Database.getInstance();
	
	@Override
	public User logIn(String username) {
		log.info("Log in with user name: " + username);
		
		User user = null;
		if (db.connect()) {
			log.info("Establishing connection was successful.");
			
			user = db.getUser(username);
			db.disconnect();
		}
		else {
			log.error("Establishing connection was not successful.");
		}
		
		return user;
	}

	@Override
	public boolean addUser(String username, String passwordHash, boolean isAdmin) {
		log.info("Add user: " + username + " - " + passwordHash);
		
		boolean success = false;
		User user = null;
		
		if (db.connect()) {
			log.info("Establishing connection was successful.");
			
			user = db.getUser(username);
			
			if (user == null) {
				user = new User();
				user.setUserName(username);
				user.setPasswordHash(passwordHash);
				user.setScore(0);
				user.setUserLevel(UserLevel.BEGINNER);
				user.setAdmin(isAdmin);
				
				success = db.addUser(user);
			}
			
			db.disconnect();
		}
		else {
			log.error("Establishing connection was not successful.");
			success = false;
		}
		
		return success;
	}

	@Override
	public boolean deleteUser(String username) {
		log.info("Delete user: " + username);
		
		boolean success = false;
		
		if (db.connect()) {
			log.info("Establishing connection was successful.");
			
			// Success is when 0 or 1 row is deleted.
			success = (db.deleteUser(username) >= 0);
			
			db.disconnect();
		}
		else {
			log.error("Establishing connection was not successful.");
			success = false;
		}
		
		return success;
	}

}
