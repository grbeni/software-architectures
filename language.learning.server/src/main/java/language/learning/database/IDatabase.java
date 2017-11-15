package language.learning.database;

import language.learning.user.User;

public interface IDatabase {
	
	public User getUser(String username);
	
	
	// TODO
	public int addExercise();

}
