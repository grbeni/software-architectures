package language.learning.database;

import language.learning.datatype.User;

public interface IDatabase {
	
	public User getUser(String username);
	
	// TODO
	public boolean addExerciseSentence();
	
	// TODO
	public boolean addExerciseWord();

}
