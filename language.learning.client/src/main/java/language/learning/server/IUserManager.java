package language.learning.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import language.learning.user.User;

@Path("user")
public interface IUserManager {
	
	/**
	 * Method for user login, returns the User in JSON.
	 * @param username
	 * @return user name and hash of the password in a JSON file
	 */
	@GET
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	User logIn(@QueryParam("username") String username);
	
	/**
	 * Method for user registration.
	 * @param username
	 */
	@POST
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	boolean addUser(User user);
	
	/**
	 * Method for user registration.
	 * @param username
	 */
	@DELETE
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	boolean deleteUser(@QueryParam("username") String username);
}
