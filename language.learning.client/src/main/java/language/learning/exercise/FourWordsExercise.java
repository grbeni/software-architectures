package language.learning.exercise;

import java.util.List;

public class FourWordsExercise extends Exercise {

	// List containing the wrong choices for the particular exercise
	private List<String> wrongChoices;
	
	/**
	 * Constructor with all parameter.	
	 * @param english English word
	 * @param hungarian Hungarian word
	 * @param wrongChoices wrong choices for the exercise
	 */
	public FourWordsExercise(String english, String hungarian, List<String> wrongChoices) {
		super(english, hungarian);
		
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
	
}
