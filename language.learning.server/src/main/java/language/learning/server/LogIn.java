package language.learning.server;

import language.learning.database.Database;
import language.learning.database.IDatabase;
import language.learning.user.User;

public class LogIn implements ILogIn {

	private IDatabase db = Database.getInstance();
	
	@Override
	public User logIn(String username) {
		
		User user = null;
		if (db.connect()) {
			user = db.getUser(username);
		}
		
		
		return user;
	}

}
