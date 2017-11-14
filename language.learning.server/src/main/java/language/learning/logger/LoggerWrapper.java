package language.learning.logger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LoggerWrapper {

	// Logger
	private Logger log;
	
	// Default level of the logging
	private final Level logLevel = Level.ERROR;
	
	
	// Constructors
	public LoggerWrapper(String className) {
		
		log = Logger.getLogger(className);
		log.setLevel(logLevel);
	}
	
	public LoggerWrapper(String className, Level level) {
		
		log = Logger.getLogger(className);
		log.setLevel(level);
	}
	
	// Getter for log
	public Logger getLog() {
		
		return log;
	}
	
}
