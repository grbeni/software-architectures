package language.learning.exercise;

import java.awt.Image;

import org.apache.log4j.Logger;

import language.learning.logger.LoggerWrapper;

public class ExerciseWithImage extends Exercise {

	private static final Logger log = (new LoggerWrapper(ExerciseWithImage.class.getName())).getLog();
	
	// Image depicting a word
	private Image image;
	
	
	/**
	 * Constructor with all parameter.	
	 * @param english English word
	 * @param hungarian Hungarian word
	 * @param image image
	 */
//	public ExerciseWithImage(String english, String hungarian, Image image) {
//		super(english, hungarian);
//		
//		this.image = image;
//		
//		log.trace("ExerciseWithImage created");
//	}
	
	
	/**
	 * Getter for image.
	 * @return image
	 */
	public Image getImage() {
		log.trace("Image getter");		
		
		return image;
	}
}
