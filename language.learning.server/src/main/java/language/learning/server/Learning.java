package language.learning.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import language.learning.database.Database;
import language.learning.database.IDatabase;
import language.learning.exercise.Exercise;
import language.learning.exercise.ExerciseType;
import language.learning.exercise.Exercises;
import language.learning.logger.LoggerWrapper;
import language.learning.user.User;

public class Learning implements ILearning {

	private static final Logger log = (new LoggerWrapper(Learning.class.getName())).getLog();
	
	private IDatabase db = Database.getInstance();
	
	
	@Override
	public Exercises getExercises(String type) {
		log.trace("Get all exercise with type: " + type);
		
		List<Exercise> exerciseList = new ArrayList<>();
		
		if (db.connect()) {
			log.trace("Establishing connection was successful.");
			
			exerciseList = db.getAllExercise(ExerciseType.valueOf(type.toUpperCase()));
		}
		else {
			log.trace("Establishing connection was not successful.");
		}
		
		Exercises ex = new Exercises(exerciseList);
		
		return ex;
	}

	// TODO
	@Override
	public Exercises getExercisesWithUserLevel(String userLevel, int equals) {
		log.error("equals: " + equals);
//		try {
//			UserLevel level = UserLevel.valueOf(userLevel.toUpperCase());
//		} catch (IllegalArgumentException e) {
//			log.error("No such user level: " + userLevel);
//		}
		List<Exercise> exerciseList = new ArrayList<>();
		exerciseList.add(new Exercise("dog", "kutya"));
		Exercises ex = new Exercises(exerciseList);
		
		return ex;
	}

	// TODO
	@Override
	public void updateUserScore(int score, User user) {
		log.error("new score: " + score);
		log.error("UPDATE: name, pw, level, score " + user.getUsername() + " " 
				+ user.getPasswordHash() + " " + user.getUserLevel() + " " + user.getScore());
		
	}

}
