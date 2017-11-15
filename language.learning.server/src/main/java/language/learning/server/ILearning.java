package language.learning.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import language.learning.exercise.Exercises;
import language.learning.user.User;

@Path("learn")
public interface ILearning {

	/**
	 * Method for accessing all exercise.
	 * @return all exercise
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	Exercises getExercises();
	
	
	/**
	 * Method for accessing exercises for the given level of user.
	 * If equals then returns only the same level exercises, otherwise 
	 * returns the same and below level exercises.
	 * @param userLevel the knowledge level of the user
	 * @param equals 1 (true), 0 (false)
	 * @return
	 */
	@GET
	@Path("{find}")
	//@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Exercises getExercisesWithUserLevel(@QueryParam("level") String userLevel, @QueryParam("equals") int equals);
	
	@PUT
	@Path("{score}")
	@Consumes(MediaType.APPLICATION_JSON)
	void updateUserScore(@PathParam("score") int score, User user);
		
	
}
