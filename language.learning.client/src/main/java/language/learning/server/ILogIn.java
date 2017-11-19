package language.learning.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import language.learning.user.User;

@Path("login")
public interface ILogIn {
	
	/**
	 * Method for user login, returns the User in JSON.
	 * @param username
	 * @return user name and hash of the password in a JSON file
	 */
	@GET
	@Path("{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	User logIn(@PathParam("username") String username, String passwordHash);
	
}
