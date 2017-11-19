package language.learning.user;

import org.apache.log4j.Logger;

import language.learning.exercise.KnowledgeLevel;
import language.learning.logger.LoggerWrapper;

public class User {
	
	private static final Logger log = (new LoggerWrapper(User.class.getName())).getLog();

	private boolean isAdmin;
	
	// The user name of the user.
	private String userName;
	
	// The hash value of the user's password.
	private String passwordHash;
	
	// The level of the user.
	private KnowledgeLevel knowledgeLevel;
	
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
	public String getPasswordHash() {
		log.trace("Get password hash: " + passwordHash);
		
		return passwordHash;
	}
	
	/**
	 * Setter for password hash.
	 * @param passwordHash
	 */
	public void setPasswordHash(String passwordHash) {
		log.trace("Set password hash to: " + passwordHash);
		
		this.passwordHash = passwordHash;
	}
		
	/**
	 * Getter for user level.
	 * @return the knowledge level of the user
	 */
	public KnowledgeLevel getKnowledgeLevel() {
		log.trace("Get knowledge level: " + knowledgeLevel);
		
		return knowledgeLevel;
	}
	
	/**
	 * Setter for user level.
	 * @param userLevel
	 */
	public void setKnowledgeLevel(KnowledgeLevel knowledgeLevel) {
		log.trace("Set knowledge level to: " + knowledgeLevel);
		
		this.knowledgeLevel = knowledgeLevel;
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
