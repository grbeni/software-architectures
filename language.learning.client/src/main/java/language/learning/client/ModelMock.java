package language.learning.client;

import java.util.Arrays;
import java.util.List;

import javafx.scene.image.Image;
import language.learning.exercise.Exercise;
import language.learning.exercise.ExerciseWithImage;
import language.learning.exercise.Exercises;
import language.learning.exercise.FourWordsExercise;
import language.learning.exercise.KnowledgeLevel;
import language.learning.server.IExerciseManager;
import language.learning.server.ILearning;
import language.learning.server.IUserManager;
import language.learning.user.User;

public class ModelMock implements IExerciseManager, ILearning, IUserManager {

	@Override
	public Exercises getExercises(String type, int count) {
		return null;
	}

	@Override
	public Exercises getExercisesWithUserLevel(String type, String userLevel, boolean equals, int count) {
		List<Exercise> exerciseList = Arrays.asList(new Exercise[] {
				new Exercise("dog", "kutya", KnowledgeLevel.BEGINNER),
				new Exercise("cat", "macska", KnowledgeLevel.BEGINNER),
				new Exercise("carrot", "repa", KnowledgeLevel.BEGINNER),
				new Exercise("boat", "hajo", KnowledgeLevel.BEGINNER),
				new Exercise("apple", "alma", KnowledgeLevel.BEGINNER),
				new ExerciseWithImage("Sponge Bob", "Spongya Bob", new Image("spongya.jpg"), KnowledgeLevel.BEGINNER),
				new FourWordsExercise("hammer", "kalapacs", Arrays.asList(new String[] {"szög", "fűrész", "gereblye"}), KnowledgeLevel.BEGINNER),
				new FourWordsExercise("car", "autó", Arrays.asList(new String[] {"bickli", "motor", "busz"}), KnowledgeLevel.BEGINNER),
				new FourWordsExercise("cow", "szarvasmarha", Arrays.asList(new String[] {"sertes", "csirke", "lud"}), KnowledgeLevel.BEGINNER),
				new FourWordsExercise("violet", "lila", Arrays.asList(new String[] {"piros", "szürke", "fekete"}), KnowledgeLevel.BEGINNER)				
		});
		
		Exercises exercises = new Exercises(exerciseList);		
		return exercises;
	}

	@Override
	public void updateUserScore(int score, User user) {
		
	}

	@Override
	public void updateUserLevel(String userLevel, User user) {
		
	}

	@Override
	public boolean updateExerciseWithImage() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean deleteUser(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User logIn(String username) {
		// TODO Auto-generated method stub
		return new User(username, "asd", KnowledgeLevel.BEGINNER, 0, false);
	}

	@Override
	public boolean addUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addExercise(Exercise exercise, User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteExercise(Exercise exercise, User user) {
		// TODO Auto-generated method stub
		return false;
	}

}
