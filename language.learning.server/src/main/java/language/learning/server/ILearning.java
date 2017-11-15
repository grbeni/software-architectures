package language.learning.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import language.learning.exercise.Exercises;

@Path("learn")
public interface ILearning {

	/**
	 * Method for accessing all exercise.
	 * @return all exercise
	 */
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Exercises getExercises();
	
	/**
	 * Method for accessing exercises for the given level of user.
	 * @param userLevel the knowledge level of the user
	 * @return exercises for users at the given level
	 */
	@GET
	@Path("{find}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Exercises getExercisesWithUserLevel(@QueryParam("level") String userLevel, @QueryParam("equals") int equals);
	
	
	//Exercises getExercisesBelowUserLevel(UserLevel userLevel);
	
}
