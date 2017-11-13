package language.learning.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class Application {
	public static void main(String[] args) {
		try {
			// Create a new RESTeasy client through the JAX-RS API:
			Client client = ClientBuilder.newClient();
			// The base URL of the service:
			WebTarget target = client.target("http://localhost:8080/language.learning.server/api");
			// Cast it to ResteasyWebTarget:
			ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
			// Get a typed interface:
			IHello hello = rtarget.proxy(IHello.class);
			// Call the service as a normal Java object:
			String result = hello.sayHello("me");
			// Print the result:
			System.out.println(result);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
