package language.learning.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import language.learning.exercise.Exercise;
import language.learning.exercise.ExerciseWithImage;

@Path("exercise")
public interface IExerciseManager {
	
	@POST
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)
	void addExercise(@QueryParam("username") String username, Exercise exercise);
	
	@POST
	@Path("image")
	@Consumes(MediaType.APPLICATION_JSON)
	void addExercise(@QueryParam("username") String username, ExerciseWithImage exercise);
	
	@DELETE
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	boolean deleteExercise(@QueryParam("username") String username, Exercise exercise);
	
}
