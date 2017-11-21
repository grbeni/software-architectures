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
public class ImageExercises {

	private static final Logger log = (new LoggerWrapper(SentenceExercises.class.getName())).getLog();

	// List containing exercises
	@XmlElement
	private List<ExerciseWithImage> exercises;

	public ImageExercises() {}	

	/**
	 * Constructor with parameter.
	 * 
	 * @param exercises list of exercises
	 */
	public ImageExercises(List<ExerciseWithImage> exercises) {
		log.trace("Exercises created with exercises.");

		this.exercises = exercises;
	}
	
	@Override
	public String toString() {
		return "Exercises [exercises=" + exercises + "]";
	}

	/**
	 * Getter for exercises.
	 * 
	 * @return list of exercises
	 */
	public List<ExerciseWithImage> getExercises() {
		log.trace("Get exercises");

		return exercises;
	}

	/**
	 * Getter for exercises.
	 * 
	 * @return list of exercises
	 */
	public void setExercises(List<ExerciseWithImage> exercises) {
		log.trace("Get exercises");

		this.exercises = exercises;
	}

}
