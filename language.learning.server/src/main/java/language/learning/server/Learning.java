package language.learning.server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import language.learning.database.Database;
import language.learning.database.IDatabase;
import language.learning.exercise.Exercise;
import language.learning.exercise.ExerciseType;
import language.learning.exercise.Exercises;
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

		Exercises ex = new Exercises(exerciseList);

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

		Exercises ex = new Exercises(exerciseList);

		return ex;
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
