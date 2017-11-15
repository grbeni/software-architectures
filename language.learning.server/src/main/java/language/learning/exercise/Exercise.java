package language.learning.exercise;

import org.apache.log4j.Logger;

import language.learning.logger.LoggerWrapper;

public class Exercise {

	private static final Logger log = (new LoggerWrapper(Exercise.class.getName())).getLog();
	
	// The word in English
	private String english;
		
	// The word in Hungarian
	private String hungarian;
	

	// Constructors
	public Exercise() {
		log.trace("Exercise created");
	}
	
	public Exercise(String englishWord, String hungarianWord) {
		log.trace("WordExercise created with word: " + english + " - " + hungarian);
		
		this.english = englishWord;
		this.hungarian = hungarianWord;
	}
	
	
	/**
	 * Getter for English word.
	 * @return English word
	 */
	public String getEnglishWord() {
		log.trace("English getter: " + english);
		
		return english;
	}
	
	/**
	 * Getter for Hungarian word.
	 * @return Hungarian word
	 */
	public String getHungarianWord() {
		log.trace("Hungarian getter: " + hungarian);
		
		return hungarian;
	}
		
}
