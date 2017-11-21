package language.learning.exercise;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import javafx.scene.image.Image;

@XmlType(name="image")
public class ExerciseWithImage extends Exercise {
	
	// Image depicting a word
	@XmlElement
	private Image image;	
	
	public ExerciseWithImage() {}
	
	/**
	 * Constructor with all parameter.	
	 * @param english English word
	 * @param hungarian Hungarian word
	 * @param image image
	 */
	public ExerciseWithImage(String english, String hungarian, Image image, KnowledgeLevel knowledgeLevel) {
		super(english, hungarian, knowledgeLevel);
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
	
	/**
	 * Setter for image.
	 * @return image
	 */
	public void setImage(Image image) {
		log.trace("Image setter");		
		
		this.image = image;
	}
}
