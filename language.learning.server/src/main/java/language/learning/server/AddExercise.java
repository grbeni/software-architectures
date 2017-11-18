package language.learning.server;

import org.apache.log4j.Logger;

import language.learning.database.Database;
import language.learning.database.IDatabase;
import language.learning.exercise.Exercise;
import language.learning.exercise.ExerciseLevel;
import language.learning.exercise.ExerciseType;
import language.learning.logger.LoggerWrapper;

public class AddExercise implements IAddExercise {
	
	private static final Logger log = (new LoggerWrapper(AddExercise.class.getName())).getLog();

	private IDatabase db = Database.getInstance();
	

	@Override
	public int addExercise(String exerciseType, String exerciseLevel, Exercise exercise) {
		log.info("Exercise to be added: " + exercise.getEnglish() + " - " + exercise.getHungarian() 
			+ ", " + exerciseLevel + ", " + exerciseType);
		
		exercise.setExerciseLevel(ExerciseLevel.valueOf(exerciseLevel.toUpperCase()));
		
		int result = 0;
		
		if (db.connect()) {
			log.trace("Establishing connection was successful.");
			
			result = db.addExercise(exercise, ExerciseType.valueOf(exerciseType.toUpperCase()));
			
			db.disconnect();
		}
		else {
			log.trace("Establishing connection was not successful.");
		}
		
		return result;
	}
	
	

	// TODO
	@Override
	public int updateExerciseWithImage() {
		log.info("Exercise to be updated: ");
		
		return 0;
	}

}
