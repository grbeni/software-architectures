package language.learning.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("hello")
public interface IHello {
	@GET
	@Path("sayHello")
	public String sayHello(@QueryParam("name") String name);
}