package language.learning.exercise;

import java.util.List;

import org.apache.log4j.Logger;

import language.learning.logger.LoggerWrapper;

public class Exercises {
	
	private static final Logger log = (new LoggerWrapper(Exercises.class.getName())).getLog();
	
	// List containing exercises.
	private List<Exercise> exercises;
	
	
	/**
	 * Constructor with parameter.
	 * @param exercises list of exercises
	 */
	public Exercises(List<Exercise> exercises) {
		log.trace("Exercises created with exercises.");
		
		this.exercises = exercises;
	}
	
	/**
	 * Getter for exercises.
	 * @return list of exercises
	 */
	public List<Exercise> getExercises() {
		log.trace("Get exercises");
		
		return exercises;
	}

}
