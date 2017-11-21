package language.learning.exercise;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="image")
public class ExerciseWithImage extends Exercise {
	
	// Image depicting a word
	@XmlElement
	private byte[] image;	
	
	public ExerciseWithImage() {}
	
	/**
	 * Constructor with all parameter.	
	 * @param english English word
	 * @param hungarian Hungarian word
	 * @param image image
	 */
	public ExerciseWithImage(String english, String hungarian, byte[] image, KnowledgeLevel knowledgeLevel) {
		super(english, hungarian, knowledgeLevel);
		this.exerciseType = ExerciseType.IMAGE;
		this.image = image;
		
		log.trace("ExerciseWithImage created");
	}
	
	
	/**
	 * Getter for image.
	 * @return image
	 */
	public byte[] getImage() {
		log.trace("Image getter");		
		
		return image;
	}
	
	/**
	 * Setter for image.
	 * @return image
	 */
	public void setImage(byte[] image) {
		log.trace("Image setter");		
		
		this.image = image;
	}
}
