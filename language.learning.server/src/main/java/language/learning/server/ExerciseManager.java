package language.learning.server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import language.learning.database.Database;
import language.learning.database.IDatabase;
import language.learning.exercise.Exercise;
import language.learning.exercise.ExerciseWithImage;
import language.learning.exercise.KnowledgeLevel;
import language.learning.exercise.SentenceExercises;
import language.learning.logger.LoggerWrapper;

public class ExerciseManager implements IExerciseManager {

	private static final Logger log = (new LoggerWrapper(ExerciseManager.class.getName())).getLog();

	private IDatabase db = Database.getInstance();

	@Override
	public SentenceExercises listExercises() {
		log.info("Get all exercise.");

		List<Exercise> exList = new ArrayList<>();
		
		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");
				
				exList = db.getExercises();
				
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

		SentenceExercises se = new SentenceExercises(exList);
		log.info("sentence exercises created " + se.getExercises().get(0).getEnglish());
		
		return se;
	}

	@Override
	public void addExercise(String username, Exercise exercise) {
		log.info("Exercise to be added: " + exercise.getEnglish() + " - " + exercise.getHungarian() + ", "
				+ exercise.getKnowledgeLevel() + ", " + exercise.getExerciseType());

		exercise.setKnowledgeLevel(KnowledgeLevel.valueOf(exercise.getKnowledgeLevel().toString().toUpperCase()));

		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");

				int resultCount = db.addExercise(exercise, username);

				log.info("Added " + resultCount + " exercise(s).");
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
	public void addExercise(String username, ExerciseWithImage exercise) {
		log.info("Add exercise with image: " + exercise.getEnglish());

		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");

				int addCount = db.addImageExercise(exercise, username);

				log.info("Added " + addCount + " exercise(s).");
			} else {
				log.warn("Establishing connection was not successful.");
			}
		} catch (ClassNotFoundException | SQLException e) {
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
	public void deleteExercise(String username, Exercise exercise) {
		log.info("Exercise to be deleted: " + exercise.getEnglish() + " - " + exercise.getHungarian());

		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");

				db.deleteExercise(exercise, username);

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
