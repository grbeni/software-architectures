package language.learning.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.log4j.Logger;

import language.learning.exercise.KnowledgeLevel;
import language.learning.logger.LoggerWrapper;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class User {
	
	private static final Logger log = (new LoggerWrapper(User.class.getName())).getLog();

	// The user name of the user.
	@XmlElement
	private String username;
	// The hash value of the user's password.
	@XmlElement
	private String passwordHash;
	// The level of the user.
	@XmlElement
	private KnowledgeLevel userLevel;
	// The score of the user.
	@XmlElement
	private int score;
	// A user can be admin or not.
	@XmlElement
	private boolean admin;	
	
	public User() {}
	
	public User(String userName, String passwordHash, KnowledgeLevel userLevel, int score, boolean admin) {
		this.username = userName;
		this.passwordHash = passwordHash;
		this.userLevel = userLevel;
		this.score = score;
		this.admin = admin;
	}

	/**
	 * Getter for user name.
	 * @return user name
	 */
	public String getUsername() {
		log.info("Get username: " + username);
		
		return username;
	}
	
	/**
	 * Setter for user name.
	 * @param userName
	 */
	public void setUsername(String userName) {
		log.info("Set username to: " + userName);
		
		this.username = userName;
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
