package language.learning.client;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import language.learning.exercise.Exercise;
import language.learning.exercise.ExerciseWithImage;
import language.learning.exercise.Exercises;
import language.learning.exercise.FourWordsExercise;
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
	@FXML
	private VBox learningTabBox;
	@FXML
	private HBox exercisesInfoBox;
	
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
	private Label correctAnswerLabel;
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
	
	@FXML
	private ImageView imageView;
	@FXML
	private ImageView correctAnswerView;
	
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
	
	private Exercise actualExercise;
	private Button correctAnswer; // In case of four words exercise
	private Button chosenAnswer; // In case of four words exercise

	private final Image tickImage;
	private final Image crossImage;
	
	public View() {
		model = new ModelMock();
		tickImage = new Image("tick.png", 100, 100, false, false);
		crossImage = new Image("cross.png", 100, 100, false, false);
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
		if (!startLearningButton.isVisible()) {
			alert("You cannot disconnect during a lesson!");
		}
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
		if (loggedInUser == null) {
			alert("You are not logged in!");
			return;
		}		
		// Get coaching tasks
		Exercises retrievedExercises = model.getExercisesWithUserLevel(null, loggedInUser.getKnowledgeLevel().toString(), this.TASK_COUNT, false);
		exercises = new ArrayList<Exercise>(retrievedExercises.getExercises());
		coachingExercises = new ArrayList<Exercise>(exercises);
		
		// Change the window
		startLearningButton.setVisible(false);
		nextCoachingEventHandler(event); // So the default values are not shown 
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
			displayAnswers();
			return;
		}
		// New random index
		int nextIndex = (coachingExercises.size() % 3 - 1);
		nextIndex = (nextIndex < 0) ? 0 : nextIndex;
		
		Exercise coachingExercise = coachingExercises.remove(nextIndex);
		
		englishWordLabel.setText(coachingExercise.getEnglish());
		hungarianWordLabel.setText(coachingExercise.getHungarian());	
		
		// Write log to the GUI
		logList(log);
	}
	
	private void testChoiceAnswer() {
		++answerCount;
		
		switch (actualExercise.getExerciseType()) {
			case SENTENCE:
				checkAnswer(translationField.getText(), actualExercise.getHungarian());
			break;
			case WORD:
				checkAnswer(chosenAnswer.getText(), correctAnswer.getText());
			break;
			case IMAGE:
				checkAnswer(imageDescriptionField.getText(), actualExercise.getHungarian());
			break;
		}
		exerciseCountLabel.setText("Finished exercises: " + answerCount + "/" + TASK_COUNT);		
		
		if (answerCount >= 10) {
			finishExercises();
			return;
		}
		// Delete image so it does not bother the following layouts
		imageView.setImage(null);
		// New exercise
		displayAnswers();

	}

	private void checkAnswer(String givenAnswer, String correctAnswer) {
		if (givenAnswer.equals(correctAnswer)) {
			++correctAnswerCount;
			correctAnswerLabel.setText("Correct answer!");
			correctAnswerView.setImage(tickImage);
		}
		else {
			correctAnswerLabel.setText("The correct asnwer would have been " + correctAnswer + ".");
			correctAnswerView.setImage(crossImage);
		}
	}
	
	private void finishExercises() {
		fourWordsBox.setVisible(false);
		translationBox.setVisible(false);
		imageRecognitionBox.setVisible(false);
		summaryBox.setVisible(true);
			
		correctAnswersLabel.setText("Correct answers: " + correctAnswerCount);
		experienceGainedLabel.setText("Experience gained: " + correctAnswerCount * EXPERIENCE_RATIO);
		
		answerCount = 0;
		correctAnswerCount = 0;		
	}
	
	private void displayAnswers() {
		if (exercises.size() <= 0) {
			return;
		}
		emptyTextFields();
		actualExercise = exercises.remove(0);
		switch (actualExercise.getExerciseType()) {
			case SENTENCE:
				setLearningBoxesInvisible();
				translatablePhraseLabel.setText(actualExercise.getEnglish());
				translationBox.setVisible(true);
			break;
			case WORD:
				setLearningBoxesInvisible();
				translatableWordLabel.setText(actualExercise.getEnglish());
				setButtonAnswers((FourWordsExercise) actualExercise);
				fourWordsBox.setVisible(true);
			break;
			case IMAGE:
				setLearningBoxesInvisible();
				ExerciseWithImage exerciseWithImage = (ExerciseWithImage) actualExercise;
				imageView.setImage(exerciseWithImage.getImage());
				imageRecognitionBox.setVisible(true);
			break;
		}
	}
	
	private void setLearningBoxesInvisible() {
		coachingBox.setVisible(false);
		fourWordsBox.setVisible(false);
		translationBox.setVisible(false);
		imageRecognitionBox.setVisible(false);
		summaryBox.setVisible(false);
	}
	
	private void emptyTextFields() {		
		translationField.setText("");
		imageDescriptionField.setText("");
	}
	
	private void setButtonAnswers(FourWordsExercise exercise) {
		int correctButtonNumber = (int) ((Math.random() * 100) % 4 + 1);
		List<Button> wrongButtons = Stream.of(choiceOneButton, choiceTwoButton,
				choiceThreeButton, choiceFourButton).collect(Collectors.toList());
		switch (correctButtonNumber) {
			case 1:
				wrongButtons.remove(choiceOneButton);
				choiceOneButton.setText(exercise.getHungarian());
				correctAnswer = choiceOneButton;
			break;
			case 2:
				wrongButtons.remove(choiceTwoButton);
				choiceTwoButton.setText(exercise.getHungarian());
				correctAnswer = choiceTwoButton;
			break;
			case 3:
				wrongButtons.remove(choiceThreeButton);
				choiceThreeButton.setText(exercise.getHungarian());
				correctAnswer = choiceThreeButton;
			break;
			case 4:
				wrongButtons.remove(choiceFourButton);
				choiceFourButton.setText(exercise.getHungarian());
				correctAnswer = choiceFourButton;
			break;
		}
		for (int i = 0; i < wrongButtons.size(); ++i) {
			wrongButtons.get(i).setText(exercise.getWrongChoices().get(i));
		}
	}
	
	/**
	 * Called whenever the choice one button is pressed.
	 */
	@FXML
	private void choiceOneEventHandler(ActionEvent event) {
		chosenAnswer = choiceOneButton;
		testChoiceAnswer();
	}
	
	/**
	 * Called whenever the choice two button is pressed.
	 */
	@FXML
	private void choiceTwoEventHandler(ActionEvent event) {
		chosenAnswer = choiceTwoButton;
		testChoiceAnswer();
	}
	
	/**
	 * Called whenever the choice three button is pressed.
	 */
	@FXML
	private void choiceThreeEventHandler(ActionEvent event) {
		chosenAnswer = choiceThreeButton;
		testChoiceAnswer();
	}
	
	/**
	 * Called whenever the choice four button is pressed.
	 */
	@FXML
	private void choiceFourEventHandler(ActionEvent event) {
		chosenAnswer = choiceFourButton;
		testChoiceAnswer();
	}
	
	/**
	 * Called whenever the send translation button is pressed.
	 */
	@FXML
	private void sendTranslationEventHandler(ActionEvent event) {
		testChoiceAnswer();
	}
	
	/**
	 * Called whenever the image description button is pressed.
	 */
	@FXML
	private void sendImageDescriptionEventHandler(ActionEvent event) {
		testChoiceAnswer();
	}
	
	/**
	 * Called whenever the reset learning button is pressed.
	 */
	@FXML
	private void resetLearningEventHandler(ActionEvent event) {
		summaryBox.setVisible(false);
		exercisesInfoBox.setVisible(false);
		startLearningButton.setVisible(true);
	}

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
