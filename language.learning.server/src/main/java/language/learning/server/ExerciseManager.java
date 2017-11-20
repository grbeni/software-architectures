package language.learning.server;

import org.apache.log4j.Logger;

import language.learning.database.Database;
import language.learning.database.IDatabase;
import language.learning.exercise.Exercise;
import language.learning.exercise.ExerciseLevel;
import language.learning.exercise.ExerciseType;
import language.learning.logger.LoggerWrapper;

public class ExerciseManager implements IExerciseManager {
	
	private static final Logger log = (new LoggerWrapper(ExerciseManager.class.getName())).getLog();

	private IDatabase db = Database.getInstance();
	

	@Override
	public boolean addExercise(Exercise exercise) {
		log.info("Exercise to be added: " + exercise.getEnglish() + " - " + exercise.getHungarian() 
			+ ", " + exercise.getExerciseLevel() + ", " + exercise.getExerciseType());
		
		exercise.setExerciseLevel(ExerciseLevel.valueOf(exercise.getExerciseLevel().toString().toUpperCase()));
		
		int result = 0;
		
		if (db.connect()) {
			log.info("Establishing connection was successful.");
			
			result = db.addExercise(exercise, ExerciseType.valueOf(exercise.getExerciseType().toString().toUpperCase()));
			
			db.disconnect();
		}
		else {
			log.info("Establishing connection was not successful.");
		}
		
		// result > 0 means the insertion was successful
		return (result > 0);
	}	


	@Override
	public boolean deleteExercise(Exercise exercise) {
		log.info("Exercise to be deleted: " + exercise.getEnglish() + " - " + exercise.getHungarian());
		
		boolean success = false;
		
		if (db.connect()) {
			log.info("Establishing connection was successful.");
			
			success = db.deleteExercise(exercise);
			
			db.disconnect();
		}
		else {
			log.info("Establishing connection was not successful.");
		}
		
		return success;
	}


	@Override
	public boolean updateExerciseWithImage() {
		// TODO Auto-generated method stub
		return false;
	}

}
