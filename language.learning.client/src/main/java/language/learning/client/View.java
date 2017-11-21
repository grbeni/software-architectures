package language.learning.client;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
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
import language.learning.exercise.ExerciseType;
import language.learning.exercise.ExerciseWithImage;
import language.learning.exercise.Exercises;
import language.learning.exercise.FourWordsExercise;
import language.learning.exercise.KnowledgeLevel;
import language.learning.user.User;

/**
 * The class responsible for the controlling of the view defined in the View.fxml file. 
 * @author Bence Graics
 */
public class View {
	
	private static final String SALT = "software-architecture";
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

	@FXML
	private CheckBox isAdminCheckBox;
	
	@FXML
	private ComboBox<KnowledgeLevel> knowledgeLevelSelector;
	
	// Alert window
	private Alert alertWindow = new Alert(AlertType.ERROR);
	
	// Constants
	private final int EXPERIENCE_RATIO = 10;
	private final int EXERCISE_COUNT = 10;
	private final int NEW_LEVEL_SCORE = 1000;
	
	// Inner variables for counting
	private int correctAnswerCount = 0;
	private int answerCount = 0;	
	
	// Actual logged in user
	private User loggedInUser;
	
	// The exercises that are retrieved when the user pressed on the play button
	private List<Exercise> exercises;
	private List<Exercise> coachingExercises;
	// The exercise that is being solved by the user
	private Exercise actualExercise;
	
	// The button references in case of the four word exercise
	private Button correctAnswer; // This should be pressed by the user
	private Button chosenAnswer; // This was actually pressed by the user

	// Images for the correct and wrong answers
	private final Image tickImage;
	private final Image crossImage;
	
	// The object responsible for connecting to the server
	private DataAccess model;
	
	public View() {
		model = new DataAccess();
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
		// Fill the knowledge level selector
		knowledgeLevelSelector.getItems().setAll(KnowledgeLevel.BEGINNER,
				KnowledgeLevel.INTERMEDIATE, KnowledgeLevel.EXPERT);
		knowledgeLevelSelector.getSelectionModel().selectFirst();
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
		loggedInUser = model.logIn(userNameField.getText());
		if (loggedInUser == null) {
			alert("Wrong user name!");
			return;
		}
		// Checking the password
//		if (!loggedInUser.getPasswordHash().equals(hashPassword(passwordField.getText(), SALT))) {
//			alert("Wrong password!");
//			return;
//		}
		// Connection succeeded
		if (loggedInUser != null) {
			connectionStateLabel.setText("Connection created");
			connectionStateLabel.setTextFill(Color.web("#009900"));
		}
		printUserData();
		
		// Write log to the GUI
		logList(log);
	}
	
	private void printUserData() {
		levelUp();
		userInfoLabel.setText(loggedInUser.getUsername() + " Experience: " + loggedInUser.getScore()
			+ " Level: " + loggedInUser.getUserLevel());
	}
	
	private void levelUp() {
		if (loggedInUser.getScore() / NEW_LEVEL_SCORE > 1) {
			switch (loggedInUser.getUserLevel()) {
				case BEGINNER:
					loggedInUser.setUserLevel(KnowledgeLevel.INTERMEDIATE);
				break;
				case INTERMEDIATE:
					loggedInUser.setUserLevel(KnowledgeLevel.EXPERT);
				break;
				default:
					break;
			}
			loggedInUser.setScore(loggedInUser.getScore() % NEW_LEVEL_SCORE);
		}
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
		Exercises retrievedExercises = retrieveExercises();
		exercises = new ArrayList<Exercise>(retrievedExercises.getExercises());
		coachingExercises = new ArrayList<Exercise>(exercises);
		
		// Change the window
		startLearningButton.setVisible(false);
		nextCoachingEventHandler(event); // So the default values are not shown 
		coachingBox.setVisible(true);
		
		// Write log to the GUI
		logList(log);
	}
	
	private Exercises retrieveExercises() {
		assert 10 == this.EXERCISE_COUNT;
		
		Exercises wordExercises = model.getExercisesWithUserLevel(ExerciseType.WORD.toString(),
				loggedInUser.getUserLevel().toString(), false, 4);
		System.out.println("Word exercises: " + wordExercises);
		Exercises sentenceExercises = model.getExercisesWithUserLevel(ExerciseType.SENTENCE.toString(),
				loggedInUser.getUserLevel().toString(), false, 4);
		System.out.println("Sentence exercises: " + sentenceExercises);
		Exercises imageExercises = model.getExercisesWithUserLevel(ExerciseType.IMAGE.toString(),
				loggedInUser.getUserLevel().toString(), false, 2);
		System.out.println("Image exercises: " + imageExercises);
		
		List<Exercise> exercises = new ArrayList<>(wordExercises.getExercises());
		exercises.addAll(sentenceExercises.getExercises());
		exercises.addAll(imageExercises.getExercises());
		if (exercises.size() < EXERCISE_COUNT) {
			alert("Not enough exercises: " + exercises.size());
		}
		System.out.println(exercises);
		Collections.shuffle(exercises);		
		return new Exercises(exercises);
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
		exerciseCountLabel.setText("Finished exercises: " + answerCount + "/" + EXERCISE_COUNT);		
		
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
		int gainedExperience = correctAnswerCount * EXPERIENCE_RATIO;
		loggedInUser.setScore(loggedInUser.getScore() + gainedExperience);
		experienceGainedLabel.setText("Experience gained: " + gainedExperience);
		
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
		// Resetting the view
		summaryBox.setVisible(false);
		exercisesInfoBox.setVisible(false);
		startLearningButton.setVisible(true);
		// Refreshing the labels
		printUserData();
		// Sending the data to the server
		model.updateUserScore(loggedInUser.getScore(), loggedInUser);
		model.updateUserLevel(loggedInUser.getUserLevel().toString(), loggedInUser);
	}

	@FXML
	private void addExerciseEventHandler() {
		if (!isLoggedIn()) {
			return;
		}
		String englishPhrase = addEnglishPhraseField.getText();
		String hungarianPhrase = addHungarianPhraseField.getText();
		ExerciseType exerciseType = (!englishPhrase.contains(" ") && !hungarianPhrase.contains(" ")) ?
				ExerciseType.WORD : ExerciseType.SENTENCE;
		// Creating the exercise object
		Exercise createdExercise = new Exercise(englishPhrase, hungarianPhrase, knowledgeLevelSelector.getSelectionModel().getSelectedItem());
		createdExercise.setExerciseType(exerciseType);
		// Sending it to the server
		boolean result = model.addExercise(createdExercise, loggedInUser);
		if (result) {
			logMsg("Exercise added.");
		}
		else {
			logMsg("Error during creation.");
		}
	}
	
	@FXML
	private void deleteExerciseEventHandler() {
		if (!isLoggedIn()) {
			return;
		}
		if (addEnglishPhraseField.getText().equals("") || addHungarianPhraseField.getText().equals("")) {
			alert("Both text fields need to be filled!");
			return;
		}
		// Creating the exercise object
		Exercise createdExercise = new Exercise(deleteEnglishPhraseField.getText(), deleteHungarianPhraseField.getText(), null);
		// Sending it to the server
		boolean result = model.deleteExercise(createdExercise, loggedInUser);
		if (result) {
			logMsg("Exercise deleted.");
		}
		else {
			logMsg("Error during deletion.");
		}
	}
	
	@FXML
	private void createUserEventHandler() {
		if (!isLoggedIn()) {
			return;
		}
		if (!loggedInUser.isAdmin()) {
			alert("Only administrators can add users!");
			return;
		}
		// Creating user object 
		User createdUser = new User(addUserNameField.getText(),	hashPassword(addPasswordField.getText(), SALT),
				KnowledgeLevel.BEGINNER, 0, isAdminCheckBox.isSelected());
		boolean result = model.addUser(createdUser);
		System.out.println(isAdminCheckBox.isSelected());
		if (result) {
			logMsg("User " + deleteUserField.getText() + " deleted.");
		}
		else {
			logMsg("Error during creation.");
		}
	}
	
	@FXML
	private void deleteUserEventHandler() {
		if (!isLoggedIn()) {
			return;
		}
		if (!loggedInUser.isAdmin()) {
			alert("Only administrators can delete users!");
			return;
		}
		boolean result = model.deleteUser(deleteUserField.getText());
		if (result) {
			logMsg("User " + deleteUserField.getText() + " deleted.");
		}
		else {
			logMsg("Error during deletion.");
		}
	}
	
	private boolean isLoggedIn() {
		if (loggedInUser == null) {
			alert("You are not logged in!");
			return false;
		}
		return true;
	}

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
