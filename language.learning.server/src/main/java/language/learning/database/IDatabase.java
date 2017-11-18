package language.learning.database;

import java.util.List;

import language.learning.exercise.Exercise;
import language.learning.exercise.ExerciseLevel;
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
	 * Returns all exercise with the given type.
	 * @param type
	 * @return
	 */
	public List<Exercise> getAllExercise(ExerciseType type);
	
	/**
	 * Returns all exercise with the given type and level or level below.
	 * @param type
	 * @param level
	 * @param onlyAtLevel if true, than only the same level exercise are returned, else the exercises below the level also
	 * @return
	 */
	public List<Exercise> getExercisesWithUserLevel(ExerciseType type, ExerciseLevel level, boolean onlyAtLevel);
	
	/**
	 * Method for inserting new exercise into database.
	 * @param exercise
	 * @return
	 */
	public int addExercise(Exercise exercise, ExerciseType type);

}
