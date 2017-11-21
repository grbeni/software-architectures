package language.learning.server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import language.learning.database.Database;
import language.learning.database.IDatabase;
import language.learning.exercise.Exercise;
import language.learning.exercise.ExerciseType;
import language.learning.exercise.Exercises;
import language.learning.exercise.FourWordsExercise;
import language.learning.exercise.KnowledgeLevel;
import language.learning.logger.LoggerWrapper;
import language.learning.user.User;

public class Learning implements ILearning {

	private static final Logger log = (new LoggerWrapper(Learning.class.getName())).getLog();

	private IDatabase db = Database.getInstance();

	@Override
	public Exercises getExercises(String type, int count) {
		log.info("Get all exercise with type: " + type);

		List<Exercise> exerciseList = new ArrayList<>();

		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");

				exerciseList = db.getAllExercise(ExerciseType.valueOf(type.toUpperCase()));

				db.disconnect();
			} else {
				log.warn("Establishing connection was not successful.");
			}
		} catch (SQLException | ClassNotFoundException e) {
			log.error(e.getMessage());
		} finally {
			try {
				db.disconnect();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		Exercises ex = null;
		if ("WORD".equals(type)) {
			ex = getRandomNCountExerciseFourWords(exerciseList, count);
		}
		else {
			ex = getRandomNCountExercise(exerciseList, count);
		}		

		return ex;
	}

	@Override
	public Exercises getExercisesWithUserLevel(String type, String userLevel, boolean equals, int count) {
		log.info("Get " + type + " exercises: " + userLevel + ", " + equals);

		List<Exercise> exerciseList = new ArrayList<>();

		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");
				exerciseList = db.getExercisesWithUserLevel(ExerciseType.valueOf(type.toUpperCase()),
						KnowledgeLevel.valueOf(userLevel.toUpperCase()), equals);

				db.disconnect();
			} else {
				log.warn("Establishing connection was not successful.");
			}
		} catch (SQLException | ClassNotFoundException e) {
			log.error(e.getMessage());
		} finally {
			try {
				db.disconnect();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		Exercises ex = null;
		// TODO FourWords Exercise használata
		if ("WORD".equals(type)) {
			ex = getRandomNCountExerciseFourWords(exerciseList, count);
		}
		else {
			ex = getRandomNCountExercise(exerciseList, count);
		}

		return ex;
	}
	
	/**
	 * Chooses N exercise from ex randomly. 
	 * @param ex
	 * @param count N
	 * @return
	 */
	private Exercises getRandomNCountExercise(List<Exercise> ex, int count) {
		int range = ex.size();
		
		if (range <= count) {
			return new Exercises(ex);
		}
		
		Set<Integer> indexSet = new HashSet<>();
		
		while (indexSet.size() < count) {
			Random rand = new Random();
			indexSet.add(rand.nextInt(range));			
		}
		
		List<Exercise> resultList = new ArrayList<>();
		
		for (Integer index : indexSet) {
			resultList.add(ex.get(index));
		}
		
		return new Exercises(resultList);
	}

	private Exercises getRandomNCountExerciseFourWords(List<Exercise> ex, int count) {
		int range = ex.size();
		
		if (range <= count) {
			return new Exercises(Collections.emptyList());
		}
		
		Set<Integer> indexSet = new HashSet<>();
		
		while (indexSet.size() < count) {
			Random rand = new Random();
			indexSet.add(rand.nextInt(range));			
		}
		
		List<Exercise> resultList = new ArrayList<>();
		
		for (Integer index : indexSet) {
			List<String> wrongChoices = getThreeStringFromListRandomly(ex, range, index);			
			
			FourWordsExercise fwEx = new FourWordsExercise(ex.get(index).getEnglish(),
															ex.get(index).getHungarian(), 
															wrongChoices, 
															ex.get(index).getKnowledgeLevel());
			resultList.add(fwEx);
		}
				
		return new Exercises(resultList);
	}
	
	private List<String> getThreeStringFromListRandomly(List<Exercise> ex, int range, int index) {
		Set<Integer> indexSet = new HashSet<>();
		
		while (indexSet.size() < 3) {
			Random rand = new Random();
			int randomIndex = rand.nextInt(range);
			if (randomIndex != index) {
				indexSet.add(randomIndex);
			}
		}
		
		List<String> stringList = new ArrayList<>();
		
		for (Integer integer : indexSet) {
			stringList.add(ex.get(integer).getHungarian());
		}
		
		return stringList;
	}
	
	@Override
	public void updateUserScore(int score, User user) {
		log.info("Update " + user.getUsername() + "'s score from: " + user.getScore() + " to: " + score);

		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");

				db.updateUserScore(user, score);

				db.disconnect();
			} else {
				log.warn("Establishing connection was not successful.");
			}
		} catch (SQLException | ClassNotFoundException e) {
			log.error(e.getMessage());
		} finally {
			try {
				db.disconnect();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
	}

	@Override
	public void updateUserLevel(String userLevel, User user) {
		log.info("Update " + user.getUsername() + "'s level from: " + user.getUserLevel() + " to: " + userLevel);

		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");

				db.updateUserLevel(user, KnowledgeLevel.valueOf(userLevel.toUpperCase()));

				db.disconnect();
			} else {
				log.warn("Establishing connection was not successful.");
			}
		} catch (SQLException | ClassNotFoundException e) {
			log.error(e.getMessage());
		} finally {
			try {
				db.disconnect();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
	}

}
