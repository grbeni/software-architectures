package language.learning.exercise;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="fourWords")
public class FourWordsExercise extends Exercise {

	// List containing the wrong choices for the particular exercise
	@XmlElement
	private List<String> wrongChoices;
	
	public FourWordsExercise() {}
	
	/**
	 * Constructor with all parameter.	
	 * @param english English word
	 * @param hungarian Hungarian word
	 * @param wrongChoices wrong choices for the exercise
	 */
	public FourWordsExercise(String english, String hungarian, List<String> wrongChoices, KnowledgeLevel knowledgeLevel) {
		super(english, hungarian, knowledgeLevel);
		
		this.exerciseType = ExerciseType.WORD;
		this.wrongChoices = wrongChoices;
		
		log.trace("FourWordsExercise created");
	}
	
	
	/**
	 * Getter for wrong choices.
	 * @return wrong choices
	 */
	public List<String> getWrongChoices() {
		log.trace("Wrong choices getter");		
		
		return wrongChoices;
	}
	
	/**
	 * Setter for wrong choices.
	 * @return
	 */
	public void setWrongChoices(List<String> wrongChoices) {
		log.trace("Wrong choices setter");		
		
		this.wrongChoices = wrongChoices;
	}
	
}
