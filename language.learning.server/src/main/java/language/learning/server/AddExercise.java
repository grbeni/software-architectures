package language.learning.server;

import org.apache.log4j.Logger;

import language.learning.database.Database;
import language.learning.database.IDatabase;
import language.learning.exercise.Exercise;
import language.learning.logger.LoggerWrapper;

public class AddExercise implements IAddExercise {
	
	private static final Logger log = (new LoggerWrapper(AddExercise.class.getName())).getLog();

	private IDatabase db = Database.getInstance();
	

	// TODO
	@Override
	public int addExercise(Exercise exercise) {
		log.info("Exercise to be added: " + exercise.getEnglishWord() + " - " + exercise.getHungarianWord());
		
		return db.addExercise();
	}


	// TODO
	@Override
	public int updateExerciseWithImage() {
		log.info("Exercise to be updated: ");
		
		return 0;
	}

}
