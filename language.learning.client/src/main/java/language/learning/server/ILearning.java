package language.learning.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import language.learning.exercise.Exercises;
import language.learning.user.User;

@Path("learn")
public interface ILearning {

	/**
	 * Method for accessing all exercise in a given type.
	 * @param type exercise type, can be sentence or word
	 * @param count number of exercises required
	 * @return all exercise with a given type
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	Exercises getExercises(@QueryParam("type") String type, @QueryParam("count") int count);
	
	
	/**
	 * Method for accessing exercises for the given level of user.
	 * If equals then returns only the same level exercises, otherwise 
	 * returns the same and below level exercises.
	 * @param type word or sentence type exercise
	 * @param userLevel userLevel the knowledge level of the user
	 * @param count number of exercises required
	 * @param equals equals true, false
	 * @return
	 */
	@GET
	@Path("{find}")
	@Produces(MediaType.APPLICATION_JSON)
	Exercises getExercisesWithUserLevel(@QueryParam("type") String type, 
										@QueryParam("level") String userLevel,
										@QueryParam("count") int count,
										@QueryParam("equals") boolean equals);
	
	
	/**
	 * Updates the score of the given user to the given score.
	 * @param score
	 * @param user
	 */
	@PUT
	@Path("score")
	@Consumes(MediaType.APPLICATION_JSON)
	void updateUserScore(@QueryParam("score") int score, User user);
	
	
	/**
	 * Updates the given user's level to the given level. 
	 * @param userLevel
	 * @param user
	 */
	@PUT
	@Path("level")
	@Consumes(MediaType.APPLICATION_JSON)
	void updateUserLevel(@QueryParam("level") String userLevel, User user);
		
	
}
