package language.learning.database;

import java.sql.SQLException;
import java.util.List;

import language.learning.exercise.Exercise;
import language.learning.exercise.ExerciseType;
import language.learning.exercise.ExerciseWithImage;
import language.learning.exercise.KnowledgeLevel;
import language.learning.user.User;

public interface IDatabase {
	
	/**
	 * Creates connection with the database.
	 * @return true if successful, false if not
	 */
	public boolean connect() throws SQLException, ClassNotFoundException;
	
	/**
	 * Disconnects from the database.
	 * @return true if successful, false if not
	 */
	public boolean disconnect() throws SQLException;
	
	/**
	 * Returns the user with the given user name.
	 * @param username
	 * @return
	 */
	public User getUser(String username) throws SQLException;
	
	/**
	 * Inserts a user into the database.
	 * @param user the user to be inserted
	 * @return whether the insertion was successful
	 */
	public boolean addUser(User user) throws SQLException;
	
	/**
	 * Deletes a user from the database by the given name.
	 * @param username the name of the user to be deleted
	 * @return whether the deletion was successful
	 */
	public int deleteUser(String username) throws SQLException;
	
	/**
	 * Method for updating a user's score.
	 * @param user
	 * @param score
	 */
	public void updateUserScore(User user, int score) throws SQLException;
	
	/**
	 * Method for updating a user's level.
	 * @param user
	 * @param level
	 */
	public void updateUserLevel(User user, KnowledgeLevel level) throws SQLException;
	
	/**
	 * Returns all exercise with the given type.
	 * @param type
	 * @return
	 */
	public List<Exercise> getAllExercise(ExerciseType type) throws SQLException;
	
	/**
	 * Returns all exercise with the given type and level or level below.
	 * @param type
	 * @param level
	 * @param onlyAtLevel if true, than only the same level exercise are returned, else the exercises below the level also
	 * @return
	 */
	public List<Exercise> getExercisesWithUserLevel(ExerciseType type, 
													KnowledgeLevel level, 
													boolean onlyAtLevel) throws SQLException;
	
	public List<ExerciseWithImage> getExerciseWithImage(KnowledgeLevel level, boolean onlyAtLevel) throws SQLException;
	
	/**
	 * Method for inserting new exercise into database.
	 * @param exercise
	 * @return the id of the inserted exercise
	 */
	public int addExercise(Exercise exercise, String username) throws SQLException;
	
	/**
	 * Method for inserting image exercise into database.
	 * @param exercise
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public int addImageExercise(ExerciseWithImage exercise, String username) throws SQLException;
		
	/**
	 * Deletes the given exercise
	 * @param english
	 * @param hungarian
	 * @return whether the deletion was successful
	 */
	public boolean deleteExercise(Exercise exercise, String username) throws SQLException;
	
	
}
