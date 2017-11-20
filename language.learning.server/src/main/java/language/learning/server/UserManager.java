package language.learning.server;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import language.learning.database.Database;
import language.learning.database.IDatabase;
import language.learning.exercise.KnowledgeLevel;
import language.learning.logger.LoggerWrapper;
import language.learning.user.User;

public class UserManager implements IUserManager {

	private static final Logger log = (new LoggerWrapper(UserManager.class.getName())).getLog();

	private IDatabase db = Database.getInstance();

	@Override
	public User logIn(String username) {
		log.info("Log in with user name: " + username);

		User user = null;

		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");

				user = db.getUser(username);
				db.disconnect();
			} else {
				log.error("Establishing connection was not successful.");
			}
		} catch (SQLException | ClassNotFoundException e) {
			log.error(e.getMessage());
		} finally {
			try {
				db.disconnect();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		return user;
	}

	@Override
	public boolean addUser(User newUser) {
		log.info("Add user: " + newUser.getUsername() + " - " + newUser.getPasswordHash());

		boolean success = false;
		User user = null;

		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");

				user = db.getUser(newUser.getUsername());

				if (user == null) {
					user = new User();
					user.setUserName(newUser.getUsername());
					user.setPasswordHash(newUser.getPasswordHash());
					user.setScore(0);
					user.setUserLevel(KnowledgeLevel.BEGINNER);
					user.setAdmin(newUser.isAdmin());

					success = db.addUser(user);
				}

				db.disconnect();
			} else {
				log.error("Establishing connection was not successful.");
				success = false;
			}
		} catch (SQLException | ClassNotFoundException e) {
			log.error(e.getMessage());
		} finally {
			try {
				db.disconnect();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		return success;
	}

	@Override
	public boolean deleteUser(String username) {
		log.info("Delete user: " + username);

		boolean success = false;

		try {
			if (db.connect()) {
				log.info("Establishing connection was successful.");

				// Success is when 0 or 1 row is deleted.
				success = (db.deleteUser(username) >= 0);

				db.disconnect();
			} else {
				log.error("Establishing connection was not successful.");
				success = false;
			}
		} catch (SQLException | ClassNotFoundException e) {
			log.error(e.getMessage());
		} finally {
			try {
				db.disconnect();
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		return success;
	}

}
