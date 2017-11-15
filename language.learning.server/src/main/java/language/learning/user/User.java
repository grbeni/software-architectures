package language.learning.user;

import org.apache.log4j.Logger;

import language.learning.logger.LoggerWrapper;

public class User {
	
	private static final Logger log = (new LoggerWrapper(User.class.getName())).getLog();

	// The user name of the user.
	private String userName;
	
	// The hash value of the user's password.
	private int passwordHash;
	
	// The level of the user.
	private UserLevel userLevel;
	
	// The score of the user.
	private int score;	
	
	
	/**
	 * Getter for user name.
	 * @return user name
	 */
	public String getUsername() {
		log.trace("Get username: " + userName);
		
		return userName;
	}
	
	/**
	 * Setter for user name.
	 * @param userName
	 */
	public void setUserName(String userName) {
		log.trace("Set username to: " + userName);
		
		this.userName = userName;
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
	 * Setter for password hash.
	 * @param passwordHash
	 */
	public void setPasswordHash(int passwordHash) {
		log.trace("Set password hash to: " + passwordHash);
		
		this.passwordHash = passwordHash;
	}
		
	/**
	 * Getter for user level.
	 * @return the knowledge level of the user
	 */
	public UserLevel getUserLevel() {
		log.trace("Get user level: " + userLevel);
		
		return userLevel;
	}
	
	/**
	 * Setter for user level.
	 * @param userLevel
	 */
	public void setUserLevel(UserLevel userLevel) {
		log.trace("Set user level to: " + userLevel);
		
		this.userLevel = userLevel;
	}	

	/**
	 * Getter for user score.
	 * @return the score of the user
	 */
	public int getScore() {
		log.trace("Get score: " + score);
		
		return score;
	}
	
	/**
	 * Setter for user score.
	 * @param score
	 */
	public void setScore(int score) {
		log.trace("Set score: " + score);
		
		this.score = score;
	}
	
}
