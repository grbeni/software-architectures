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
import language.learning.exercise.ExerciseWithImage;
import language.learning.exercise.SentenceExercises;
import language.learning.exercise.FourWordsExercise;
import language.learning.exercise.FourWordsExercises;
import language.learning.exercise.ImageExercises;
import language.learning.exercise.KnowledgeLevel;
import language.learning.logger.LoggerWrapper;
import language.learning.user.User;

public class Learning implements ILearning {

	private static final Logger log = (new LoggerWrapper(Learning.class.getName())).getLog();

	private IDatabase db = Database.getInstance();

	@Override
	public SentenceExercises getExercises(String type, int count) {
		log.info("Get all exercise with type: " + type);

		List<Exercise> exerciseList = new ArrayList<>();

		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");

				exerciseList = db.getAllExercise(ExerciseType.valueOf(type.toUpperCase()));

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

		SentenceExercises ex = getRandomNCountExercise(exerciseList, count);
				
		return ex;
	}
	
	@Override
	public SentenceExercises getSentenceExercises(String userLevel, boolean equals, int count) {
		log.info("Get sentence exercises: " + userLevel + ", " + equals);

		List<Exercise> exerciseList = new ArrayList<>();

		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");
				
				exerciseList = db.getExercisesWithUserLevel(ExerciseType.SENTENCE,
						KnowledgeLevel.valueOf(userLevel.toUpperCase()), equals);

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

		SentenceExercises ex = getRandomNCountExercise(exerciseList, count);		

		return ex;
	}

	@Override
	public FourWordsExercises getWordExercises(String userLevel, boolean equals, int count) {
		log.info("Get word exercises: " + userLevel + ", " + equals);

		List<Exercise> exerciseList = new ArrayList<>();

		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");
				exerciseList = db.getExercisesWithUserLevel(ExerciseType.WORD,
						KnowledgeLevel.valueOf(userLevel.toUpperCase()), equals);

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

		FourWordsExercises ex = getRandomNCountExerciseFourWords(exerciseList, count);

		log.error("4w list: " + ex);
		
		return ex;
	}

	@Override
	public ImageExercises getImageExercises(String userLevel, boolean equals, int count) {
		log.info("Get image exercises: " + userLevel + ", " + equals);

		List<ExerciseWithImage> exerciseList = new ArrayList<>();

		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");
				exerciseList = db.getExerciseWithImage(KnowledgeLevel.valueOf(userLevel.toUpperCase()), 
														equals);

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

		ImageExercises ex = getRandomNCountImageExercise(exerciseList, count);
		
		return ex;
	}
	
	/**
	 * Chooses N exercise from ex randomly. 
	 * @param ex
	 * @param count N
	 * @return
	 */
	private ImageExercises getRandomNCountImageExercise(List<ExerciseWithImage> ex, int count) {
		int range = ex.size();
		
		if (range <= count) {
			return new ImageExercises(ex);
		}
		
		Set<Integer> indexSet = new HashSet<>();
		
		while (indexSet.size() < count) {
			Random rand = new Random();
			indexSet.add(rand.nextInt(range));			
		}
		
		List<ExerciseWithImage> resultList = new ArrayList<>();
		
		for (Integer index : indexSet) {
			resultList.add(ex.get(index));
		}
		
		return new ImageExercises(resultList);
	}
	
	/**
	 * Chooses N exercise from ex randomly. 
	 * @param ex
	 * @param count N
	 * @return
	 */
	private SentenceExercises getRandomNCountExercise(List<Exercise> ex, int count) {
		int range = ex.size();
		
		if (range <= count) {
			return new SentenceExercises(ex);
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
		
		return new SentenceExercises(resultList);
	}

	private FourWordsExercises getRandomNCountExerciseFourWords(List<Exercise> ex, int count) {
		int range = ex.size();
		
		if (range <= count) {
			return new FourWordsExercises(Collections.emptyList());
		}
		
		Set<Integer> indexSet = new HashSet<>();
		
		while (indexSet.size() < count) {
			Random rand = new Random();
			indexSet.add(rand.nextInt(range));			
		}
		
		List<FourWordsExercise> resultList = new ArrayList<>();
		
		for (Integer index : indexSet) {
			List<String> wrongChoices = getThreeStringFromListRandomly(ex, range, index);			
						
			FourWordsExercise fwEx = new FourWordsExercise(ex.get(index).getEnglish(),
															ex.get(index).getHungarian(), 
															wrongChoices, 
															ex.get(index).getKnowledgeLevel());
			resultList.add(fwEx);
		}
				
		return new FourWordsExercises(resultList);
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
