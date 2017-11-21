package language.learning.exercise;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.log4j.Logger;

import language.learning.logger.LoggerWrapper;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class Exercises {
	
	private static final Logger log = (new LoggerWrapper(Exercises.class.getName())).getLog();
	
	// List containing exercises
	@XmlElement
	private List<Exercise> exercises;	

	public Exercises() {}
	
	@Override
	public String toString() {
		return "Exercises [exercises=" + exercises + "]";
	}
	
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
	
	/**
	 * Getter for exercises.
	 * @return list of exercises
	 */
	public void setExercises(List<Exercise> exercises) {
		log.trace("Get exercises");
		
		this.exercises = exercises;
	}

}
