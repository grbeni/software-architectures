/**
 * This JavaFX skeleton is provided for the Software Laboratory 5 course. Its structure
 * should provide a general guideline for the students.
 * As suggested by the JavaFX model, we'll have a GUI (view),
 * a controller class (this one) and a model.
 */

package language.learning.client;

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

/**
 * The class responsible for the controlling of the view defined in the View.fxml file. 
 * @author Bence Graics
 */
public class View {

	private Controller controller;

	// Layouts
	@FXML
	private VBox rootLayout;
	@FXML
	private HBox connectionLayout;

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

	// Tabs
	@FXML
	private Tab editTab;
	@FXML
	private Tab statisticsTab;
	@FXML
	private Tab logTab;
	@FXML
	private Tab searchTab;
	
	// Alert window
	Alert alertWindow = new Alert(AlertType.ERROR);

	// Titles and map keys of table columns search
	String searchColumnTitles[] = new String[] { "Befekteto neve", "Reszveny neve", "Tranzakcios mennyiseg", "Tranzakcios egysegar" };
	String searchColumnKeys[] = new String[] { "col1", "col2", "col3", "col4" };

	// Titles and map keys of table columns statistics
	String statisticsColumnTitles[] = new String[] { "Reszveny neve", "Atlagos havi tranzakcioszam"};
	String statisticsColumnKeys[] = new String[] { "cegnev", "atltrszam" };


	/**
	 * View constructor
	 */
	public View() {
		controller = new Controller();
	}

	/**
	 * View initialization, it is called after the view is prepared.
	 */
	@FXML
	public void initialize() {
		// Clear username and password textfields and display status
		connectionStateLabel.setTextFill(Color.web("#ee0000"));
		logMsg("LOLGEX");
	}

	/**
	 * Initialize controller with data from AppMain (now only sets stage)
	 *
	 * @param stage
	 *            The top level JavaFX container
	 */
	public void initData(Stage stage) {
		// Set 'onClose' event handler (of the container)
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent winEvent) {
				List<String> log = new ArrayList<>();
				// We try to commit
				// If the autocommit is true, then the commit will be unsuccessful, but that should not bother us
				controller.commit(log);
				for (String string : log) logMsg(string);
			}
		});
	}

	/**
	 * This is called whenever the connect button is pressed.
	 */
	@FXML
	private void connectEventHandler(ActionEvent event) {
		//Log container
		List<String> log = new ArrayList<>();

		// Controller connect method will do everything for us, just call
		// it
		if (controller.connect(userNameField.getText(), passwordField.getText(), log)) {
			connectionStateLabel.setText("Connection created");
			connectionStateLabel.setTextFill(Color.web("#009900"));
		}

		//Write log to gui
		for (String string : log) { 
			logMsg(string); 
			if (string.startsWith("error ")) {
				alertWindow.setContentText(string.replaceFirst("^error ", ""));
				alertWindow.show();
			}
		}
	}


	/**
	 * This is called whenever the edit button is pressed
	 * Task 2,3,4
	 * USE controller modify method (verify data in controller !!!)
	 * @param event
	 *            Contains details about the JavaFX event
	 */
	@FXML
	private void editEventHandler(ActionEvent event) {
		List<String> log = new ArrayList<>();
		
		logRecord(log);
	}


	/**
	 * This is called whenever the commit button is pressed
	 * Task 4
	 * USE controller commit method
	 * Don't forget SET the commit button disable state
	 * LOG:
	 * 	commit ok: if commit return true
	 *  commit failed: if commit return false
	 * @param event
	 *            Contains details about the JavaFX event
	 */
	@FXML
	private void commitEventHandler(ActionEvent event) {
		List<String> log = new ArrayList<>();
		controller.commit(log);
		logRecord(log);
	}



	/**
	 * This is called whenever the statistics button is pressed
	 * Task 5
	 * USE controller getStatistics method
	 * @param event
	 *            Contains details about the JavaFX event
	 */
	@FXML
	private void statisticsEventHandler(ActionEvent event) {
		List<String> log = new ArrayList<>();
		
		logRecord(log);	
			
	}

	/**
	 * Appends the message (with a line break added) to the log.
	 *
	 * @param message The message to be logged
	 */
	protected void logMsg(String message) {
		logTextArea1.appendText(message + "\n");
		logTextArea2.appendText(message + "\n");
	}
	
	private void logRecord(List<String> log) {
		for (String string : log) {
			logMsg(string);	
			if (!string.equals("commit ok") && !string.equals("insert occured") && !string.equals("update occured")) {
				alertWindow.setContentText(string.replaceFirst("^error ", ""));
				alertWindow.show();
			}
		}
	}
	
	

}
