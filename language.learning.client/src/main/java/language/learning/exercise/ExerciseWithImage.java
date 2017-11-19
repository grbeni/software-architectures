package language.learning.exercise;

import javafx.scene.image.Image;

public class ExerciseWithImage extends Exercise {
	
	// Image depicting a word
	private Image image;	
	
	/**
	 * Constructor with all parameter.	
	 * @param english English word
	 * @param hungarian Hungarian word
	 * @param image image
	 */
	public ExerciseWithImage(String english, String hungarian, Image image) {
		super(english, hungarian);
		this.exerciseType = ExerciseType.IMAGE;
		this.image = image;
		
		log.trace("ExerciseWithImage created");
	}
	
	
	/**
	 * Getter for image.
	 * @return image
	 */
	public Image getImage() {
		log.trace("Image getter");		
		
		return image;
	}
}
