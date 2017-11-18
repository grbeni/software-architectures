package language.learning.logger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LoggerWrapper {

	// Logger
	private Logger log;
	
	// Default level of the logging
	private final Level logLevel = Level.TRACE;
	
	
	/**
	 * Constructor with String parameter.
	 * @param className the name of the class
	 */
	public LoggerWrapper(String className) {
		
		log = Logger.getLogger(className);
		log.setLevel(logLevel);
	}
	
	/**
	 * Constructor with String and Level parameters.
	 * @param className the name of the class
	 * @param level the level of the logging
	 */
	public LoggerWrapper(String className, Level level) {
		
		log = Logger.getLogger(className);
		log.setLevel(level);
	}
	
	/**
	 * Getter for log
	 * @return log
	 */
	public Logger getLog() {
		
		return log;
	}
	
}
