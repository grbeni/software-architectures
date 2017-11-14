package language.learning.server;

import language.learning.database.Database;
import language.learning.database.IDatabase;
import language.learning.datatype.SentenceExercise;
import language.learning.datatype.WordExercise;

public class AddExercise implements IAddExercise {

	private IDatabase db = Database.getInstance();
	
	@Override
	public boolean addExerciseTypeSentence(SentenceExercise sentenceExercise) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addExerciseTypeWord(WordExercise wordExercise) {
		// TODO
		WordExercise exercise = new WordExercise();
		exercise.setWord("alma");
		
		db.addExerciseWord();
		
		return false;
	}

}
