package language.learning.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import language.learning.exercise.Exercise;
import language.learning.exercise.Exercises;
import language.learning.server.IExerciseManager;
import language.learning.server.ILearning;
import language.learning.server.IUserManager;
import language.learning.user.User;

public class DataAccess implements IExerciseManager, ILearning, IUserManager {

	private IExerciseManager exerciseManager;
	private ILearning learning;
	private IUserManager userManager;
	
	public DataAccess() {
		// Creating a new RESTeasy client through the JAX-RS API:
		Client client = ClientBuilder.newClient();
		// The base URL of the service:
		WebTarget target = client.target("http://localhost:8080/language.learning.server/api");
		// Cast it to ResteasyWebTarget:
		ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
		// Get a typed interface:
		exerciseManager = rtarget.proxy(IExerciseManager.class);
		learning = rtarget.proxy(ILearning.class);
		userManager = rtarget.proxy(IUserManager.class);
	}

	@Override
	public User logIn(String username) {
		return userManager.logIn(username);
	}

	@Override
	public boolean addUser(User user) {
		return userManager.addUser(user);
	}

	@Override
	public boolean deleteUser(String username) {
		return userManager.deleteUser(username);
	}

	@Override
	public Exercises getExercises(String type, int count) {
		return learning.getExercises(type, count);
	}

	@Override
	public Exercises getExercisesWithUserLevel(String type, String userLevel, boolean equals, int count) {
		return learning.getExercisesWithUserLevel(type, userLevel, equals, count);
	}

	@Override
	public void updateUserScore(int score, User user) {
		learning.updateUserScore(score, user);
	}

	@Override
	public void updateUserLevel(String userLevel, User user) {
		learning.updateUserLevel(userLevel, user);
	}

	@Override
	public boolean addExercise(Exercise exercise, User user) {
		return exerciseManager.addExercise(exercise, user);
	}

	@Override
	public boolean deleteExercise(Exercise exercise, User user) {
		return exerciseManager.deleteExercise(exercise, user);
	}

	@Override
	public boolean updateExerciseWithImage() {
		return exerciseManager.updateExerciseWithImage();
	}

}
