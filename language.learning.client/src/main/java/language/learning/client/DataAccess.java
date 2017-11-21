package language.learning.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import language.learning.exercise.Exercise;
import language.learning.exercise.FourWordsExercises;
import language.learning.exercise.ImageExercises;
import language.learning.exercise.SentenceExercises;
import language.learning.server.IExerciseManager;
import language.learning.server.ILearning;
import language.learning.server.IUserManager;
import language.learning.user.User;

public class DataAccess implements IExerciseManager, ILearning, IUserManager {

	private IExerciseManager exerciseManager;
	private ILearning learning;
	private IUserManager userManager;

	public DataAccess() {

	}

	@Override
	public User logIn(String username) {
		userManager = createClient(IUserManager.class);
		return userManager.logIn(username);
	}

	@Override
	public boolean addUser(User user) {
		userManager = createClient(IUserManager.class);
		return userManager.addUser(user);
	}

	@Override
	public boolean deleteUser(String username) {
		userManager = createClient(IUserManager.class);
		return userManager.deleteUser(username);
	}

	@Override
	public SentenceExercises getExercises(String type, int count) {
		learning = createClient(ILearning.class);
		return learning.getExercises(type, count);
	}

	@Override
	public SentenceExercises getSentenceExercises(String userLevel, boolean equals, int count) {
		learning = createClient(ILearning.class);
		return learning.getSentenceExercises(userLevel, equals, count);
	}

	@Override
	public FourWordsExercises getWordExercises(String userLevel, boolean equals, int count) {
		learning = createClient(ILearning.class);
		return learning.getWordExercises(userLevel, equals, count);
	}

	@Override
	public ImageExercises getImageExercises(String userLevel, boolean equals, int count) {
		learning = createClient(ILearning.class);
		return learning.getImageExercises(userLevel, equals, count);
	}

	@Override
	public void updateUserScore(int score, User user) {
		learning = createClient(ILearning.class);
		learning.updateUserScore(score, user);
	}

	@Override
	public void updateUserLevel(String userLevel, User user) {
		learning = createClient(ILearning.class);
		learning.updateUserLevel(userLevel, user);
	}

	@Override
	public boolean addExercise(Exercise exercise, User user) {
		exerciseManager = createClient(IExerciseManager.class);
		return exerciseManager.addExercise(exercise, user);
	}

	@Override
	public boolean deleteExercise(Exercise exercise, User user) {
		exerciseManager = createClient(IExerciseManager.class);
		return exerciseManager.deleteExercise(exercise, user);
	}

	@Override
	public boolean updateExerciseWithImage() {
		exerciseManager = createClient(IExerciseManager.class);
		return exerciseManager.updateExerciseWithImage();
	}

	private <T> T createClient(Class<T> clazz) {
		// Creating a new RESTeasy client through the JAX-RS API:
		Client client = ClientBuilder.newClient();
		// The base URL of the service:
		WebTarget target = client.target("http://localhost:8080/language.learning.server/api");
		// Cast it to ResteasyWebTarget:
		ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
		// Get a typed interface:
		return rtarget.proxy(clazz);
	}

}
