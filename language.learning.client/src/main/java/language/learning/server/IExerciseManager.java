package language.learning.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import language.learning.exercise.Exercise;
import language.learning.exercise.ExerciseWithImage;
import language.learning.user.User;

@Path("exercise")
public interface IExerciseManager {
	
	@POST
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)
	boolean addExercise(Exercise exercise, User user);
	
	@POST
	@Path("image")
	@Consumes(MediaType.APPLICATION_JSON)
	boolean addExercise(ExerciseWithImage exercise, User user);
	
	@DELETE
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	boolean deleteExercise(Exercise exercise, User user);
	
	// TODO
	@PUT
	@Path("update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	boolean updateExerciseWithImage();
	
}
