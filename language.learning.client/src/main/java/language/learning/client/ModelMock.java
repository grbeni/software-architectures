package language.learning.client;

import javafx.scene.image.Image;
import java.util.Arrays;
import java.util.List;

import language.learning.exercise.Exercise;
import language.learning.exercise.ExerciseWithImage;
import language.learning.exercise.Exercises;
import language.learning.exercise.FourWordsExercise;
import language.learning.exercise.KnowledgeLevel;
import language.learning.server.IAddExercise;
import language.learning.server.ILearning;
import language.learning.server.ILogIn;
import language.learning.user.User;

public class ModelMock implements IAddExercise, ILearning, ILogIn {
	
	/**
	 * Connects to the server.
	 */
	@Override
	public User logIn(String username, String passwordHash) {
		User user = new User();
		user.setUserName(username);
		user.setPasswordHash(passwordHash);
		user.setKnowledgeLevel(KnowledgeLevel.BEGINNER);
		return user;
	}

	@Override
	public Exercises getExercises(String type, int count) {
		List<Exercise> exerciseList = Arrays.asList(new Exercise[] {
				new Exercise("dog", "kutya"),
				new Exercise("cat", "macska"),
				new Exercise("carrot", "répa"),
				new Exercise("boat", "hajó"),
				new Exercise("apple", "alma"),
				new ExerciseWithImage("Sponge Bob", "Spongya Bob", new Image("spongya.jpg", 100, 100, true, false)),
				new FourWordsExercise("hammer", "kalapacs", Arrays.asList(new String[] {"szög", "fűrész", "gereblye"})),
				new FourWordsExercise("car", "autó", Arrays.asList(new String[] {"bickli", "motor", "busz"})),
				new FourWordsExercise("cow", "szarvasmarha", Arrays.asList(new String[] {"sertes", "csirke", "lud"})),
				new FourWordsExercise("violet", "lila", Arrays.asList(new String[] {"piros", "szürke", "fekete"}))				
		});
		
		Exercises exercises = new Exercises(exerciseList);		
		return exercises;
	}

	@Override
	public Exercises getExercisesWithUserLevel(String type, String userLevel, int count, boolean equals) {
		List<Exercise> exerciseList = Arrays.asList(new Exercise[] {
				new Exercise("dog", "kutya"),
				new Exercise("cat", "macska"),
				new Exercise("carrot", "repa"),
				new Exercise("boat", "hajo"),
				new Exercise("apple", "alma"),
				new ExerciseWithImage("Sponge Bob", "Spongya Bob", new Image("spongya.jpg")),
				new FourWordsExercise("hammer", "kalapacs", Arrays.asList(new String[] {"szög", "fűrész", "gereblye"})),
				new FourWordsExercise("car", "autó", Arrays.asList(new String[] {"bickli", "motor", "busz"})),
				new FourWordsExercise("cow", "szarvasmarha", Arrays.asList(new String[] {"sertes", "csirke", "lud"})),
				new FourWordsExercise("violet", "lila", Arrays.asList(new String[] {"piros", "szürke", "fekete"}))				
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
	public int addExercise(String exerciseType, String exerciseLevel, Exercise exercise) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateExerciseWithImage() {
		// TODO Auto-generated method stub
		return 0;
	}

}
