package language.learning.user;

import org.apache.log4j.Logger;

import language.learning.exercise.KnowledgeLevel;
import language.learning.logger.LoggerWrapper;

public class User {
	
	private static final Logger log = (new LoggerWrapper(User.class.getName())).getLog();

	// The user name of the user.
	private String userName;
	
	// The hash value of the user's password.
	private String passwordHash;
	
	// The level of the user.
	private KnowledgeLevel userLevel;
	
	// The score of the user.
	private int score;
	
	// A user can be admin or not.
	private boolean admin;
	
	
	/**
	 * Getter for user name.
	 * @return user name
	 */
	public String getUsername() {
		log.info("Get username: " + userName);
		
		return userName;
	}
	
	/**
	 * Setter for user name.
	 * @param userName
	 */
	public void setUserName(String userName) {
		log.info("Set username to: " + userName);
		
		this.userName = userName;
	}
	
	/**
	 * Getter for password hash.
	 * @return hash value of password
	 */
	public String getPasswordHash() {
		log.info("Get password hash: " + passwordHash);
		
		return passwordHash;
	}
	
	/**
	 * Setter for password hash.
	 * @param passwordHash
	 */
	public void setPasswordHash(String passwordHash) {
		log.info("Set password hash to: " + passwordHash);
		
		this.passwordHash = passwordHash;
	}
		
	/**
	 * Getter for user level.
	 * @return the knowledge level of the user
	 */
	public KnowledgeLevel getUserLevel() {
		log.info("Get user level: " + userLevel);
		
		return userLevel;
	}
	
	/**
	 * Setter for user level.
	 * @param userLevel
	 */
	public void setUserLevel(KnowledgeLevel userLevel) {
		log.info("Set user level to: " + userLevel);
		
		this.userLevel = userLevel;
	}	

	/**
	 * Getter for user score.
	 * @return the score of the user
	 */
	public int getScore() {
		log.info("Get score: " + score);
		
		return score;
	}
	
	/**
	 * Setter for user score.
	 * @param score
	 */
	public void setScore(int score) {
		log.info("Set score: " + score);
		
		this.score = score;
	}
	
	/**
	 * Getter for admin.
	 * @return
	 */
	public boolean isAdmin() {
		log.info("Get admin: " + admin);
		
		return admin;
	}

	/**
	 * Setter for admin.
	 * @param admin
	 */
	public void setAdmin(boolean admin) {
		log.info("Set admin to: " + admin);
		
		this.admin = admin;
	}
}
