package language.learning.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import language.learning.datatype.User;

@Path("management")
public interface IManagement {
	
	@GET
	@Path("{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	User logIn(@PathParam("username") String username);
	
}
