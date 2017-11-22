package language.learning.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import language.learning.exercise.Exercise;
import language.learning.exercise.ExerciseWithImage;
import language.learning.exercise.SentenceExercises;

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
	void deleteExercise(@QueryParam("username") String username, Exercise exercise);
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	SentenceExercises listExercises();
	
}
