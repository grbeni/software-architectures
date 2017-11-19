package language.learning.client;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import language.learning.exercise.Exercise;
import language.learning.exercise.Exercises;
import language.learning.user.User;

/**
 * The class responsible for the controlling of the view defined in the View.fxml file. 
 * @author Bence Graics
 */
public class View {
	
	// Layouts
	@FXML
	private VBox rootLayout;
	@FXML
	private HBox connectionLayout;
	@FXML
	private VBox coachingBox;
	@FXML
	private VBox fourWordsBox;
	@FXML
	private VBox translationBox;
	@FXML
	private VBox imageRecognitionBox;
	@FXML
	private VBox summaryBox;
	
	// Texts
	@FXML
	private TextField userNameField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private TextField translationField;
	@FXML
	private TextField imageDescriptionField;
	@FXML
	private TextField addEnglishPhraseField;
	@FXML
	private TextField addHungarianPhraseField;
	@FXML
	private TextField deleteEnglishPhraseField;
	@FXML
	private TextField deleteHungarianPhraseField;
	@FXML
	private TextField addUserNameField;
	@FXML
	private PasswordField addPasswordField;
	@FXML
	private TextField deleteUserField;
	
	// Logs
	@FXML
	private TextArea logTextArea1;
	@FXML
	private TextArea logTextArea2;
	
	// Buttons
	@FXML
	private Button connectButton;
	@FXML
	private Button disconnectButton;
	@FXML
	private Button startLearningButton;
	@FXML
	private Button nextCoachingButton;
	@FXML
	private Button choiceOneButton;
	@FXML
	private Button choiceTwoButton;
	@FXML
	private Button choiceThreeButton;
	@FXML
	private Button choiceFourButton;
	@FXML
	private Button sendTranslationButton;
	@FXML
	private Button sendImageDescriptionButton;
	@FXML
	private Button resetLearningButton;
	@FXML
	private Button addExerciseButton;
	@FXML
	private Button deleteExerciseButton;
	@FXML
	private Button addUserButton;
	@FXML
	private Button deleteUserButton;

	// Labels
	@FXML
	private Label connectionStateLabel;
	@FXML
	private Label userInfoLabel;
	@FXML
	private Label englishWordLabel;
	@FXML
	private Label hungarianWordLabel;
	@FXML
	private Label translatableWordLabel;
	@FXML
	private Label translatablePhraseLabel;
	@FXML
	private Label exerciseCountLabel;
	@FXML
	private Label correctAnswersLabel;
	@FXML
	private Label experienceGainedLabel;

	// Tabs
	@FXML
	private Tab learningTab;
	@FXML
	private Tab manageExercisesTab;
	@FXML
	private Tab manageUsersTab;
	
	// Alert window
	Alert alertWindow = new Alert(AlertType.ERROR);
	
	// Experience ratio
	private final int EXPERIENCE_RATIO = 10;
	private final int TASK_COUNT = 10;
	
	// Inner variables
	private int correctAnswerCount = 0;
	private int answerCount = 0;
	
	private ModelMock model;
	
	private User loggedInUser; 
	
	private List<Exercise> exercises;
	private List<Exercise> coachingExercises;
	

	public View() {
		model = new ModelMock();
	}

	/**
	 * View initialization, it is called after the view is prepared.
	 */
	@FXML
	public void initialize() {
		// Set display status
		connectionStateLabel.setTextFill(Color.web("#ee0000"));
	}

	/**
	 * Initialize controller with data from AppMain.
	 *
	 * @param stage The top level JavaFX container
	 */
	public void initData(Stage stage) {
		// Set 'onClose' event handler of the container
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent winEvent) {
				System.out.println("Exiting...");				
			}
		});
	}

	/**
	 * Called whenever the connect button is pressed.
	 */
	@FXML
	private void connectEventHandler(ActionEvent event) {
		if (loggedInUser != null) {
			alert("You are logged in!");
			return;
		}
		//Log container
		List<String> log = new ArrayList<String>();
		loggedInUser = model.logIn(userNameField.getText(), passwordField.getText());
		// Controller connect method will do everything for us, just call
		if (loggedInUser != null) {
			connectionStateLabel.setText("Connection created");
			connectionStateLabel.setTextFill(Color.web("#009900"));
		}
		userInfoLabel.setText(loggedInUser.getUsername() + " Experience: " + loggedInUser.getScore() + " Level: " + loggedInUser.getKnowledgeLevel());
		
		// Write log to the GUI
		logList(log);
	}
	
	/**
	 * Called whenever the disconnect button is pressed.
	 */
	@FXML
	private void disconnectEventHandler(ActionEvent event) {
		// Log container
		List<String> log = new ArrayList<String>();
		// Disconnecting the user
		loggedInUser = null;
		
		connectionStateLabel.setText("Disconnected");
		connectionStateLabel.setTextFill(Color.web("#ee0000"));
		userInfoLabel.setText("");
		
		// Write log to the GUI
		logList(log);
	}
	
	/**
	 * Called whenever the start learning button is pressed.
	 */
	@FXML
	private void startLearningEventHandler(ActionEvent event) {
		//Log container
		List<String> log = new ArrayList<String>();
		System.out.println("Started coaching");
		
		// Get coaching tasks
		Exercises retrievedExercises = model.getExercisesWithUserLevel(null, loggedInUser.getKnowledgeLevel().toString(), this.TASK_COUNT, false);
		exercises = retrievedExercises.getExercises();
		coachingExercises = new ArrayList<Exercise>(exercises);
		
		// Change the window
		startLearningButton.setVisible(false);
		coachingBox.setVisible(true);
		
		// Write log to the GUI
		logList(log);
	}
	
	/**
	 * Called whenever the next coaching button is pressed.
	 */
	@FXML
	private void nextCoachingEventHandler(ActionEvent event) {
		//Log container
		List<String> log = new ArrayList<String>();
		if (coachingExercises.size() <= 0) {
			// At the end of coaching period
			coachingBox.setVisible(false);
			fourWordsBox.setVisible(true);
			return;
		}
		
		Exercise coachingExercise = coachingExercises.remove(0);
		
		englishWordLabel.setText(coachingExercise.getEnglish());
		hungarianWordLabel.setText(coachingExercise.getHungarian());	
		
		// Write log to the GUI
		logList(log);
	}
	
	private void testChoiceAnswer(Button choiceButton) {
		++answerCount;
		exerciseCountLabel.setText("Finished tasks: " + answerCount + "/" + TASK_COUNT);
		
		if (answerCount >= 10) {
			finishExercises();
		}
	}
	
	private void finishExercises() {
		if (answerCount >= 10) {
			fourWordsBox.setVisible(false);
			translationBox.setVisible(false);
			imageRecognitionBox.setVisible(false);
			summaryBox.setVisible(true);
			
			correctAnswersLabel.setText("Correct answers: " + correctAnswerCount);
			experienceGainedLabel.setText("Experience gained: " + correctAnswerCount * EXPERIENCE_RATIO);
		
			answerCount = 0;
			correctAnswerCount = 0;
		}
		
		
	}
	
	
	/**
	 * Called whenever the choice one button is pressed.
	 */
	@FXML
	private void choiceOneEventHandler(ActionEvent event) {
		//Log container
		List<String> log = new ArrayList<String>();
		
		testChoiceAnswer(choiceOneButton);
		
		// Get coaching tasks
		
		// Write log to the GUI
		logList(log);
	}
	
	/**
	 * Called whenever the choice two button is pressed.
	 */
	@FXML
	private void choiceTwoEventHandler(ActionEvent event) {
		//Log container
		List<String> log = new ArrayList<String>();
		
		testChoiceAnswer(choiceTwoButton);
		
		// Get coaching tasks
		
		// Write log to the GUI
		logList(log);
	}
	
	/**
	 * Called whenever the choice three button is pressed.
	 */
	@FXML
	private void choiceThreeEventHandler(ActionEvent event) {
		//Log container
		List<String> log = new ArrayList<String>();
		
		testChoiceAnswer(choiceThreeButton);
		
		// Get coaching tasks
		
		// Write log to the GUI
		logList(log);
	}
	
	/**
	 * Called whenever the choice four button is pressed.
	 */
	@FXML
	private void choiceFourEventHandler(ActionEvent event) {
		//Log container
		List<String> log = new ArrayList<String>();
		
		testChoiceAnswer(choiceFourButton);
		
		// Get coaching tasks
		
		// Write log to the GUI
		logList(log);
	}
	
	/**
	 * Called whenever the reset learning button is pressed.
	 */
	@FXML
	private void resetLearningEventHandler(ActionEvent event) {
		summaryBox.setVisible(false);
		startLearningButton.setVisible(true);
	}
	
//		@FXML
//		private Button choiceOneButton;
//		@FXML
//		private Button choiceTwoButton;
//		@FXML
//		private Button choiceThreeButton;
//		@FXML
//		private Button choiceFourButton;
//		@FXML
//		private Button sendTranslationButton;
//		@FXML
//		private Button sendImageDescriptionButton;
//		@FXML
//		private Button addExerciseButton;
//		@FXML
//		private Button deleteExerciseButton;
//		@FXML
//		private Button addUserButton;
//		@FXML
//		private Button deleteUserButton;


	/**
	 * Appends the message (with a line break added) to the logs.
	 */
	protected void logMsg(String message) {
		logTextArea1.appendText(message + "\n");
		logTextArea2.appendText(message + "\n");
	}
	
	/**
	 * Appends the list of messages (with a line break added) to the logs and
	 * pops up an alert window in case of a logged error message.
	 */
	protected void logList(List<String> log) {
		for (String string : log) {
			logMsg(string);	
			if (string.startsWith("error ")) {
				alert(string.replaceFirst("^error ", ""));
			}
		}
	}
	
	protected void alert(String message) {
		alertWindow.setContentText(message);
		alertWindow.show();
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
