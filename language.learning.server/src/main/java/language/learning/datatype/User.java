package language.learning.datatype;

import org.apache.log4j.Logger;

import language.learning.logger.LoggerWrapper;

public class User {
	
	private static final Logger log = (new LoggerWrapper(User.class.getName())).getLog();	

	// The user name of the user.
	private String username;
	
	// The hash value of the user's password.
	private int passwordHash;
	
	
	// Constructors
	public User() {
		log.trace("User created");
	}
	
	public User(String username, int passwordHash) {
		log.trace("User created with username: " + username + ", password hash: " + passwordHash);
		
		this.username = username;
		this.passwordHash = passwordHash;
	}
	
	
	// Setters
	public void setUsername(String username) {
		log.trace("Set username to " + username);
		
		this.username = username;
	}
	
	public void setPasswordHash(int passwordHash) {
		log.trace("Set password hash to " + passwordHash);
		
		this.passwordHash = passwordHash;
	}
	
	// Getters
	public String getUsername() {
		log.trace("Get username: " + username);
		
		return username;
	}
	
	public int getPasswordHash() {
		log.trace("Get password hash: " + passwordHash);
		
		return passwordHash;
	}
	
}
