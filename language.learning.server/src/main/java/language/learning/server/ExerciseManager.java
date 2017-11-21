package language.learning.server;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import language.learning.database.Database;
import language.learning.database.IDatabase;
import language.learning.exercise.Exercise;
import language.learning.exercise.ExerciseWithImage;
import language.learning.exercise.KnowledgeLevel;
import language.learning.logger.LoggerWrapper;

public class ExerciseManager implements IExerciseManager {

	private static final Logger log = (new LoggerWrapper(ExerciseManager.class.getName())).getLog();

	private IDatabase db = Database.getInstance();

	@Override
	public void addExercise(String username, Exercise exercise) {
		log.info("Exercise to be added: " + exercise.getEnglish() + " - " + exercise.getHungarian() + ", "
				+ exercise.getKnowledgeLevel() + ", " + exercise.getExerciseType());

		exercise.setKnowledgeLevel(KnowledgeLevel.valueOf(exercise.getKnowledgeLevel().toString().toUpperCase()));

		int result = 0;

		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");

				result = db.addExercise(exercise, username);

				db.disconnect();
			} else {
				log.info("Establishing connection was not successful.");
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
		
		int addCount = 0;
		
		try {
			if (db.connect()) {
				
				addCount = db.addImageExercise(exercise, username);
				
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

		boolean success = false;

		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");

				success = db.deleteExercise(exercise, username);

				db.disconnect();
			} else {
				log.info("Establishing connection was not successful.");
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
