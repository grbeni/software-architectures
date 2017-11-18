package language.learning.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import language.learning.database.Database;
import language.learning.database.IDatabase;
import language.learning.exercise.Exercise;
import language.learning.exercise.ExerciseLevel;
import language.learning.exercise.ExerciseType;
import language.learning.exercise.Exercises;
import language.learning.logger.LoggerWrapper;
import language.learning.user.User;
import language.learning.user.UserLevel;

public class Learning implements ILearning {

	private static final Logger log = (new LoggerWrapper(Learning.class.getName())).getLog();
	
	private IDatabase db = Database.getInstance();
	
	
	@Override
	public Exercises getExercises(String type) {
		log.info("Get all exercise with type: " + type);
		
		List<Exercise> exerciseList = new ArrayList<>();
		
		if (db.connect()) {
			log.info("Establishing connection was successful.");
			
			exerciseList = db.getAllExercise(ExerciseType.valueOf(type.toUpperCase()));
			
			db.disconnect();
		}
		else {
			log.warn("Establishing connection was not successful.");
		}
		
		Exercises ex = new Exercises(exerciseList);
		
		return ex;
	}


	@Override
	public Exercises getExercisesWithUserLevel(String type, String userLevel, int equals) {
		log.info("Get " + type + " exercises: " + userLevel + ", " + equals);
		
		boolean onlyAtLevel;
		List<Exercise> exerciseList = new ArrayList<>();
		
		if (equals == 1) {
			onlyAtLevel = true;
		}
		else {
			onlyAtLevel = false;
		}
		
		if (db.connect()) {
			log.info("Establishing connection was successful.");
			exerciseList = db.getExercisesWithUserLevel(ExerciseType.valueOf(type.toUpperCase()),
														ExerciseLevel.valueOf(userLevel.toUpperCase()),
														onlyAtLevel);
			
			db.disconnect();
		}
		else {
			log.warn("Establishing connection was not successful.");
		}
		
		Exercises ex = new Exercises(exerciseList);
		
		return ex;
	}


	@Override
	public void updateUserScore(int score, User user) {
		log.info("Update " + user.getUsername() + "'s score from: " + user.getScore() + " to: " + score);
		
		if (db.connect()) {
			log.info("Establishing connection was successful.");
			
			db.updateUserScore(user, score);
			
			db.disconnect();
		}
		else {
			log.warn("Establishing connection was not successful.");
		}
	}


	@Override
	public void updateUserLevel(String userLevel, User user) {
		log.info("Update " + user.getUsername() + "'s level from: " + user.getUserLevel() + " to: " + userLevel);
		
		if (db.connect()) {
			log.info("Establishing connection was successful.");
			
			db.updateUserLevel(user, UserLevel.valueOf(userLevel.toUpperCase()));
			
			db.disconnect();
		}
		else {
			log.warn("Establishing connection was not successful.");
		}
	}

}
