package language.learning.exercise;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import language.learning.logger.LoggerWrapper;

public class Exercise {

	protected static final Logger log = (new LoggerWrapper(Exercise.class.getName(), Level.INFO)).getLog();
	
	// Exercise type
	protected ExerciseType exerciseType;
	
	// The word in English
	private String english;
		
	// The word in Hungarian
	private String hungarian;
	
	// The level of the exercise
	private KnowledgeLevel knowledgeLevel;
	

	// Constructors
	public Exercise() {
		log.info("Exercise created");
	}
	
	
	public Exercise(String englishWord, String hungarianWord) {
		log.info(exerciseType + " exercise created with word: " + english + " - " + hungarian);
		
		this.exerciseType = ExerciseType.SENTENCE;
		this.english = englishWord;
		this.hungarian = hungarianWord;
	}
	
	/**
	 * Getter for exercise type.
	 * @return exercise level
	 */
	public ExerciseType getExerciseType() {
		log.info("Exercise type getter: " + exerciseType.toString());
		
		return exerciseType;
	}
	
	/**
	 * Getter for English.
	 * @return English
	 */
	public String getEnglish() {
		log.info("English getter: " + english);
		
		return english;
	}
	
	/**
	 * Setter for English.
	 * @param english
	 */
	public void setEnglish(String english) {
		log.info("English set to: " + english);
		
		this.english = english;
	}
	
	/**
	 * Getter for Hungarian.
	 * @return Hungarian
	 */
	public String getHungarian() {
		log.info("Hungarian getter: " + hungarian);
		
		return hungarian;
	}
	
	/**
	 * Setter for Hungarian.
	 * @param hungarian
	 */
	public void setHungarian(String hungarian) {
		log.info("Hungarian set to: " + hungarian);
		
		this.hungarian = hungarian;
	}

	/**
	 * Getter for knowledge level.
	 * @return exercise level
	 */
	public KnowledgeLevel getKnowledgeLevel() {
		log.info("Knowledge level getter: " + knowledgeLevel.toString());
		
		return knowledgeLevel;
	}

	/**
	 * Setter for knowledge level.
	 * @param knowledgeLevel
	 */
	public void setKnowledgeLevel(KnowledgeLevel knowledgeLevel) {
		log.info("Knowledge level set to: " + knowledgeLevel.toString());
		
		this.knowledgeLevel = knowledgeLevel;
	}
}
