package language.learning.server;

import language.learning.database.Database;
import language.learning.database.IDatabase;
import language.learning.datatype.User;

public class Management implements IManagement {

	private IDatabase db = Database.getInstance();
	
	@Override
	public User logIn(String username) {
		
		User user = db.getUser(username);
		
		return user;
	}

}
