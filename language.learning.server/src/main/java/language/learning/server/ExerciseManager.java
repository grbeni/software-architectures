package language.learning.server;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import language.learning.database.Database;
import language.learning.database.IDatabase;
import language.learning.exercise.Exercise;
import language.learning.exercise.KnowledgeLevel;
import language.learning.logger.LoggerWrapper;
import language.learning.user.User;

public class ExerciseManager implements IExerciseManager {

	private static final Logger log = (new LoggerWrapper(ExerciseManager.class.getName())).getLog();

	private IDatabase db = Database.getInstance();

	@Override
	public boolean addExercise(Exercise exercise, User user) {
		log.info("Exercise to be added: " + exercise.getEnglish() + " - " + exercise.getHungarian() + ", "
				+ exercise.getExerciseLevel() + ", " + exercise.getExerciseType());

		exercise.setExerciseLevel(KnowledgeLevel.valueOf(exercise.getExerciseLevel().toString().toUpperCase()));

		int result = 0;

		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");

				result = db.addExercise(exercise, user);

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

		// result > 0 means the insertion was successful
		return (result > 0);
	}

	@Override
	public boolean deleteExercise(Exercise exercise, User user) {
		log.info("Exercise to be deleted: " + exercise.getEnglish() + " - " + exercise.getHungarian());

		boolean success = false;

		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");

				success = db.deleteExercise(exercise);

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

		return success;
	}

	@Override
	public boolean updateExerciseWithImage() {
		// TODO Auto-generated method stub
		return false;
	}

}
