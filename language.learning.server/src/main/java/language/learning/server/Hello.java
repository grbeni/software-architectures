package language.learning.server;

public class Hello implements IHello {
	@Override
	public String sayHello(String name) {		
				
		return "Hello-bello: " + name;
	}
}