package language.learning.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import language.learning.datatype.SentenceExercise;
import language.learning.datatype.WordExercise;

@Path("add")
public interface IAddExercise {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	boolean addExerciseTypeSentence(SentenceExercise sentenceExercise);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	boolean addExerciseTypeWord(WordExercise wordExercise);
	
}
