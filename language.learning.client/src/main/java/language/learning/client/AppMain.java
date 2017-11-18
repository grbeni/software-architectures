/**
 * This JavaFX skeleton is provided for the Software Laboratory 5 course. Its structure
 * should provide a general guideline for the students.
 * It contains the entry point of the application.
 */

package language.learning.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Application class
// This class includes the entry point of the application
public class AppMain extends Application {
	/**
	 * Display GUI window
	 *
	 * @param Stage
	 *            The top level JavaFX container
	 *
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		try {

			// Create a loader object and load View and Controller
			final FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("View.fxml"));
			final VBox viewRoot = (VBox) loader.load();

			// Get controller object and initialize it
			final View controller = loader.getController();
			controller.initData(primaryStage);

			// Set scene (and the title of the window) and display it
			Scene scene = new Scene(viewRoot);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Language Learning");
			primaryStage.show();

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	/**
	 * Entry point for the application
	 *
	 * @param args
	 *            Command-line arguments
	 */
	public static void main(String[] args) {
		launch(args);

	}

	/**
	 * It is called before application is stopped
	 */
	@Override
	public void stop() {

		// ...

	}

}
