package language.learning.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import language.learning.exercise.Exercise;

@Path("add")
public interface IAddExercise {
	
	@Path("exercise")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	int addExercise(@QueryParam("type") String exerciseType, @QueryParam("level") String exerciseLevel, Exercise exercise);
	
	
	// TODO
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	int updateExerciseWithImage();
	
}
