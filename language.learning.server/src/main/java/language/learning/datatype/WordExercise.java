package language.learning.datatype;

import org.apache.log4j.Logger;

import language.learning.logger.LoggerWrapper;

public class WordExercise {
	
	private static final Logger log = (new LoggerWrapper(WordExercise.class.getName())).getLog();
	
	// A word.
	private String word;
		
	
	// Constructors
	public WordExercise() {
		log.trace("WordExercise created");
	}
	
	public WordExercise(String word) {
		log.trace("WordExercise created with word: " + word);
		
		this.word = word;
	}
	
	
	// Setters
	public void setWord(String word) {
		
		this.word = word;
	}

	// Getters
	public String getWord() {
		
		return word;
	}
		
}
