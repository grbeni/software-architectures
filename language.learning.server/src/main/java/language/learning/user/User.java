package language.learning.user;

import org.apache.log4j.Logger;

import language.learning.logger.LoggerWrapper;

public class User {
	
	private static final Logger log = (new LoggerWrapper(User.class.getName())).getLog();

	// The user name of the user.
	private String username;
	
	// The hash value of the user's password.
	private int passwordHash;
	
	// The level of the user.
	private UserLevel userLevel;
	
	
	/**
	 * Default constructor..
	 */
	public User() {
		log.trace("User created");
	}
	
	/**
	 * Constructor without level parameter.
	 * @param username
	 * @param passwordHash
	 */
	public User(String username, int passwordHash) {
		log.trace("User created with username: " + username + ", password hash: " + passwordHash);
		
		this.username = username;
		this.passwordHash = passwordHash;
		this.userLevel = UserLevel.BEGINNER;
	}
	
	public User(String username, int passwordHash, UserLevel userLevel) {
		log.trace("User created with username: " + username + ", password hash: " + passwordHash + ", user level: " + userLevel);
		
		this.username = username;
		this.passwordHash = passwordHash;
		this.userLevel = userLevel;
	}
	
	/**
	 * Getter for user name.
	 * @return user name
	 */
	public String getUsername() {
		log.trace("Get username: " + username);
		
		return username;
	}
	
	/**
	 * Getter for password hash.
	 * @return hash value of password
	 */
	public int getPasswordHash() {
		log.trace("Get password hash: " + passwordHash);
		
		return passwordHash;
	}
	
	/**
	 * Getter for user level.
	 * @return the knowledge level of the user.
	 */
	public UserLevel getUserLevel() {
		log.trace("Get password hash: " + passwordHash);
		
		return userLevel;
	}
	
}
