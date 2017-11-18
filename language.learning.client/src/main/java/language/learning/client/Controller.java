package language.learning.client;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.List;
import java.util.Map;

public class Controller {
	
	// Model instance, controller communicates through the server using a model
	private Model model;

	public Controller(){
		model = new Model();
	}

	/**
	 * Connects to the server.
	 */
	public boolean connect(String userName, String password, List<String> log){
		String hashedPassword = this.hashPassword(password, "software-architecture-home-assingment");
		if (model.connect(userName, hashedPassword, log)) {
			// Test the connection
			
			return true;
		}
		return false;
	}

	/**
	 * Validates the values of the fields.
	 * @return true if all fields in the Map is correct else false
	 */
	public boolean validateData(Map<Object, Object> data, List<String> log) {
		try {
			// If the user did not give an rtid we return immediately
			if (!data.containsKey("rtid")) {
				throw new IllegalArgumentException("RTID fieldname syntax error: rtid must be positive integer.");				
			}			
			// Else we validate all fields given in the map
			for (Object key : data.keySet()) {
				String value = null;
				// The rtid must be an integer value
				if (key.equals("rtid")) {
					value = data.get(key).toString();
					if (!value.toString().matches("(0)||([^0&&1-9]([0-9])*)")) {
						throw new IllegalArgumentException("RTID fieldname syntax error: rtid must be positive integer.");
					}
					// If it is indeed an integer value, we replace the String value with an Integer
					data.replace(key, Integer.parseInt(value.toString()));
				}
				else if (key.equals("cegnev")) {
					// The users can basically give whatever name they want if it contains at least one word character
					value = data.get(key).toString();
					if (!value.matches(".*[A-Za-z].*")) {
						throw new IllegalArgumentException("Cegnev fieldname syntax error: value must contain at least one word character.");						
					}
				}
				// If there is another field in the map, something is very wrong
				else {
					throw new IllegalArgumentException("Not expected column name: " + key.toString());
				}
			}
			return true;
			
		} catch (NumberFormatException e) {
			log.add(e.getMessage());
			return false;
		} catch (IllegalArgumentException e) {
			log.add(e.getMessage());
			return false;
		} catch (Exception e) {
			log.add(e.getMessage());
			return false;
		}
	}

	protected String hashPassword(String passwordToHash, String salt) {
		String hash = null;
	    try {
	         MessageDigest md = MessageDigest.getInstance("SHA-512");
	         md.update(salt.getBytes("UTF-8"));
	         byte[] bytes = md.digest(passwordToHash.getBytes("UTF-8"));
	         StringBuilder sb = new StringBuilder();
	         for (int i = 0; i < bytes.length ; ++i){
	            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	         }
	         hash = sb.toString();
	        } 
	       catch (NoSuchAlgorithmException e){
	        e.printStackTrace();
	       } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    return hash;
	}

}
