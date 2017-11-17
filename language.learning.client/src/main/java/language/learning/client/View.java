/**
 * This JavaFX skeleton is provided for the Software Laboratory 5 course. Its structure
 * should provide a general guideline for the students.
 * As suggested by the JavaFX model, we'll have a GUI (view),
 * a controller class (this one) and a model.
 */

package language.learning.client;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.MapValueFactory;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

// Controller class
public class View {

	private Controller controller;


	@FXML
	private ComboBox<String> comboSample;

	//Layouts
	@FXML
	private VBox rootLayout;
	@FXML
	private HBox connectionLayout;

	//Texts
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;
	@FXML
	private TextField searchTextField;
	@FXML
	private TextField rtid;
	@FXML
	private TextField companyName;
	@FXML
	private TextField issue;
	@FXML
	private TextField value;
	@FXML
	private TextField rate;
	@FXML
	private TextField comment;
	@FXML
	private TextField befektetoId;
	@FXML
	private TextArea logTextArea;
	
	//Buttons
	@FXML
	private Button connectButton;
	@FXML
	private Button commitButton;
	@FXML
	private Button editButton;
	@FXML
	private Button statisticsButton;
	@FXML
	private Button searchButton;


	// Labels
	@FXML
	private Label connectionStateLabel;

	// Tabs
	@FXML
	private Tab editTab;
	@FXML
	private Tab statisticsTab;
	@FXML
	private Tab logTab;
	@FXML
	private Tab searchTab;


	// Tables
	@FXML
	private TableView searchTable;
	@FXML
	private TableView statisticsTable;
	
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
	 * View initialization, it will be called after view was prepared
	 */
	@FXML
	public void initialize() {

		// Clear username and password textfields and display status
		// 'disconnected'
		usernameField.setText("");
		passwordField.setText("");
		connectionStateLabel.setText("Connection: disconnected");
		connectionStateLabel.setTextFill(Color.web("#ee0000"));

		// Create table (search table) columns
		for (int i = 0; i < searchColumnTitles.length; i++) {
			// Create table column
			TableColumn<Map, String> column = new TableColumn<>(searchColumnTitles[i]);
			// Set map factory
			column.setCellValueFactory(new MapValueFactory(searchColumnKeys[i]));
			// Set width of table column
			column.prefWidthProperty().bind(searchTable.widthProperty().divide(4));
			// Add column to the table
			searchTable.getColumns().add(column);
		}

		// Create table (statistics table) columns
		for (int i = 0; i < statisticsColumnTitles.length; i++) {
			// Create table column
			TableColumn<Map, String> column = new TableColumn<>(statisticsColumnTitles[i]);
			// Set map factory
			column.setCellValueFactory(new MapValueFactory(statisticsColumnKeys[i]));
			// Set width of table column
			column.prefWidthProperty().bind(statisticsTable.widthProperty().divide(2));
			// Add column to the table
			statisticsTable.getColumns().add(column);
		}

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
	 * This is called whenever the connect button is pressed
	 *
	 * @param event
	 *            Contains details about the JavaFX event
	 */
	@FXML
	private void connectEventHandler(ActionEvent event) {
		//Log container
		List<String> log = new ArrayList<>();

		// Controller connect method will do everything for us, just call
		// it
		if (controller.connect(usernameField.getText(), passwordField.getText(), log))
		{
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
	 * This is called whenever the search button is pressed
	 * Task 1
	 * USE controller search method
	 * @param event
	 *            Contains details about the JavaFX event
	 */
	@FXML
	private void searchEventHandler(ActionEvent event) {
		List<String> log = null;
		try {
			//always use log
			log = new ArrayList<>();
	
			// Get a reference to the row list of search table
			ObservableList<Map> allRows = searchTable.getItems();
	
			// Delete all the rows
			allRows.clear();
			
			// Getting the keyword
			String keyword = searchTextField.getText();
			// Getting the result list
			List<String[]> searchResult = controller.search(keyword, log);
			// Iterating through the result list
			if (searchResult != null) {
				for (String[] row : searchResult) {
					// If the length of the row is different from the column count, we throw and exception
					if (row.length != searchTable.getColumns().size()) {
						throw new Exception("Row size is " + row.length + " instead of " + searchTable.getColumns().size());
					}
					// New map has to be created for all the new rows
					Map<String, String> searchDataRow = new HashMap<>();
					for (int i = 0; i < searchTable.getColumns().size(); i++) {
						searchDataRow.put(searchColumnKeys[i], row[i]);
					}
					allRows.add(searchDataRow);				
				}
			}
			// And write it to gui
			myLog(log);
		} catch (Exception e) {			
			logMsg(e.getMessage());
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
		boolean autoCommit = true;
		// Creating a string array with the column names
		String[] columnNames = {"RTID", "Cegnev", "Kibocsatas", "Nevertek", "Arfolyam", "Megjegyzes", "BefektetoID"};
		// Filling a map with the values of the user
		Map<String, Object> data = new HashMap<String, Object>();
		// We only put real values into the map (no "" values)
		if (!rtid.getText().equals(""))
			data.put(columnNames[0].toLowerCase(), rtid.getText());
		if (!companyName.getText().equals(""))
			data.put(columnNames[1].toLowerCase(), companyName.getText());
		if (!issue.getText().equals(""))
			data.put(columnNames[2].toLowerCase(), issue.getText());
		if (!value.getText().equals(""))
			data.put(columnNames[3].toLowerCase(), value.getText());
		if (!rate.getText().equals(""))
			data.put(columnNames[4].toLowerCase(), rate.getText());
		if (!comment.getText().equals(""))
			data.put(columnNames[5].toLowerCase(), comment.getText());
		if (!befektetoId.getText().equals("")) {
			data.put(columnNames[6].toLowerCase(), befektetoId.getText());
			autoCommit = false;
		}
		else {
			autoCommit = true;
		}
		
		controller.modifyData(data, autoCommit, log);
		
		myLog(log);
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
		myLog(log);
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
		try {
			// Get a reference to the row list of search table
			ObservableList<Map> allRows = statisticsTable.getItems();
			// Delete all the rows
			allRows.clear();
			
			List<String[]> statisticsResult = controller.getStatistics(log);
			// Iterating through the result list
			if (statisticsResult != null) {
				for (String[] row : statisticsResult) {
					// If the length of the row is different from the column count, we throw and exception
					if (row.length != statisticsTable.getColumns().size()) {
						throw new Exception("Row size is " + row.length + " instead of " + statisticsTable.getColumns().size());
					}
					// New map has to be created for all the new rows
					Map<String, String> searchDataRow = new HashMap<>();
					for (int i = 0; i < statisticsTable.getColumns().size(); i++) {
						searchDataRow.put(statisticsColumnKeys[i], row[i]);
					}
					allRows.add(searchDataRow);				
				}
				
			}		
			myLog(log);
		
		} catch (Exception e) {			
			logMsg(e.getMessage());
		}
	}

	/**
	 * Appends the message (with a line break added) to the log
	 *
	 * @param message
	 *            The message to be logged
	 */
	protected void logMsg(String message) {

		logTextArea.appendText(message + "\n");

	}
	
	private void myLog(List<String> log) {
		for (String string : log) {
			logMsg(string);	
			if (!string.equals("commit ok") && !string.equals("insert occured") && !string.equals("update occured")) {
				alertWindow.setContentText(string.replaceFirst("^error ", ""));
				alertWindow.show();
			}
		}
	}
	
	

}
