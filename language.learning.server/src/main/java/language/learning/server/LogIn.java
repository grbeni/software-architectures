package language.learning.server;

import org.apache.log4j.Logger;

import language.learning.database.Database;
import language.learning.database.IDatabase;
import language.learning.logger.LoggerWrapper;
import language.learning.user.User;

public class LogIn implements ILogIn {

	private static final Logger log = (new LoggerWrapper(LogIn.class.getName())).getLog();
	
	private IDatabase db = Database.getInstance();
	
	@Override
	public User logIn(String username) {
		log.trace("Log in with user name: " + username);
		
		User user = null;
		if (db.connect()) {
			log.trace("Establishing connection was successful.");
			
			user = db.getUser(username);
			db.disconnect();
		}
		else {
			log.error("Establishing connection was not successful.");
		}
		
		return user;
	}

}
