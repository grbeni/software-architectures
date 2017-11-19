package language.learning.exercise;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import language.learning.logger.LoggerWrapper;

public class Exercise {

	protected static final Logger log = (new LoggerWrapper(Exercise.class.getName(), Level.ERROR)).getLog();
	
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
		log.trace("Exercise created");
	}
	
	
	public Exercise(String englishWord, String hungarianWord) {
		log.trace(exerciseType + " exercise created with word: " + english + " - " + hungarian);
		
		this.exerciseType = ExerciseType.SENTENCE;
		this.english = englishWord;
		this.hungarian = hungarianWord;
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
	 * Getter for knowledge level.
	 * @return exercise level
	 */
	public KnowledgeLevel getKnowledgeLevel() {
		log.trace("Knowledge level getter: " + knowledgeLevel.toString());
		
		return knowledgeLevel;
	}

	/**
	 * Setter for knowledge level.
	 * @param knowledgeLevel
	 */
	public void setKnowledgeLevel(KnowledgeLevel knowledgeLevel) {
		log.trace("Knowledge level set to: " + knowledgeLevel.toString());
		
		this.knowledgeLevel = knowledgeLevel;
	}
}
