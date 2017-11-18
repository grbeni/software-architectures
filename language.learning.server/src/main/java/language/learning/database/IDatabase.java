package language.learning.database;

import java.util.List;

import language.learning.exercise.Exercise;
import language.learning.exercise.ExerciseType;
import language.learning.user.User;

public interface IDatabase {
	
	/**
	 * Creates connection with the database.
	 * @return true if successful, false if not
	 */
	public boolean connect();
	
	/**
	 * Disconnects from the database.
	 * @return true if successful, false if not
	 */
	public boolean disconnect();
	
	/**
	 * Returns the user with the given user name.
	 * @param username
	 * @return
	 */
	public User getUser(String username);
	
	/**
	 * Returns the all exercise with the given type.
	 * @param type
	 * @return
	 */
	public List<Exercise> getAllExercise(ExerciseType type);
	
	/**
	 * Method for inserting new exercise into database.
	 * @param exercise
	 * @return
	 */
	public int addExercise(Exercise exercise, ExerciseType type);

}
