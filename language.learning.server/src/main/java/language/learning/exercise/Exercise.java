package language.learning.exercise;

import org.apache.log4j.Logger;

import language.learning.logger.LoggerWrapper;

public class Exercise {

	private static final Logger log = (new LoggerWrapper(Exercise.class.getName())).getLog();
	
	// Exercise type
	protected ExerciseType exerciseType;
	
	// The word in English
	private String english;
		
	// The word in Hungarian
	private String hungarian;
	
	// The level of the exercise
	private KnowledgeLevel exerciseLevel;
	

	// Constructors
	public Exercise() {
		log.trace("Exercise created");
	}
	
	
	/**
	 * Getter for exercise type.
	 * @return exercise level
	 */
	public ExerciseType getExerciseType() {
		log.info("Exercise type getter: " + exerciseType);
		
		return exerciseType;
	}
	
	public void setExerciseType(ExerciseType exerciseType) {
		log.info("Exercise type set to: " + exerciseType);
		
		this.exerciseType = exerciseType;
	}
	
	/**
	 * Getter for English.
	 * @return English
	 */
	public String getEnglish() {
		log.trace("English getter: " + english);
		
		return english;
	}
	
	/**
	 * Setter for English.
	 * @param english
	 */
	public void setEnglish(String english) {
		log.trace("English set to: " + english);
		
		this.english = english;
	}
	
	/**
	 * Getter for Hungarian.
	 * @return Hungarian
	 */
	public String getHungarian() {
		log.trace("Hungarian getter: " + hungarian);
		
		return hungarian;
	}
	
	/**
	 * Setter for Hungarian.
	 * @param hungarian
	 */
	public void setHungarian(String hungarian) {
		log.trace("Hungarian set to: " + hungarian);
		
		this.hungarian = hungarian;
	}

	/**
	 * Getter for exercise level.
	 * @return exercise level
	 */
	public KnowledgeLevel getExerciseLevel() {
		log.trace("Exercise level getter: " + exerciseLevel.toString());
		
		return exerciseLevel;
	}

	/**
	 * Setter for exercise level.
	 * @param exerciseLevel
	 */
	public void setExerciseLevel(KnowledgeLevel exerciseLevel) {
		log.trace("Exercise level set to: " + exerciseLevel.toString());
		
		this.exerciseLevel = exerciseLevel;
	}
}
