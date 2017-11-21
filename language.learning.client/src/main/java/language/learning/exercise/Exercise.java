package language.learning.exercise;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import language.learning.logger.LoggerWrapper;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ExerciseWithImage.class, FourWordsExercise.class})
public class Exercise {

	protected static final Logger log = (new LoggerWrapper(Exercise.class.getName(), Level.INFO)).getLog();
	
	// Exercise type
	@XmlElement
	protected ExerciseType exerciseType;
	
	// The word in English
	@XmlElement
	private String english;
		
	// The word in Hungarian
	@XmlElement
	private String hungarian;
	
	// The level of the exercise
	@XmlElement
	private KnowledgeLevel knowledgeLevel;
	
	// Constructors
	public Exercise() {
		log.info("Exercise created");
	}
	
	
	public Exercise(String englishWord, String hungarianWord, KnowledgeLevel knowledgeLevel) {
		log.info(exerciseType + " exercise created with word: " + english + " - " + hungarian);
		
		this.exerciseType = ExerciseType.SENTENCE;
		this.english = englishWord;
		this.hungarian = hungarianWord;
		this.knowledgeLevel = knowledgeLevel;
	}
	
	@Override
	public String toString() {
		return "Exercise [exerciseType=" + exerciseType + ", english=" + english + ", hungarian=" + hungarian
				+ ", knowledgeLevel=" + knowledgeLevel + "]";
	}

	/**
	 * Setter for exercise type.
	 */
	public void setExerciseType(ExerciseType exerciseType) {
		log.info("Exercise type setter: " + exerciseType.toString());
		
		this.exerciseType = exerciseType;
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
