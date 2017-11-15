package language.learning.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import language.learning.exercise.Exercise;
import language.learning.exercise.Exercises;
import language.learning.logger.LoggerWrapper;
import language.learning.user.User;

public class Learning implements ILearning {

	private static final Logger log = (new LoggerWrapper(Learning.class.getName())).getLog();
	
	// TODO
	@Override
	public Exercises getExercises() {
		
		List<Exercise> exerciseList = new ArrayList<>();
		exerciseList.add(new Exercise("cat", "cica"));
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
