package application;

import javafx.application.Application;
import controllers.*;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;



/**
 * This is the main application. By default it launches the login view.
 * @author Derek Schatel
 *
 */
public class PhotoAlbum extends Application {



	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/login.fxml"));

		AnchorPane root = (AnchorPane)loader.load();

		LoginController controller = loader.getController();
		controller.start(primaryStage);

		Scene scene = new Scene(root);
		scene.getStylesheets().add("/application/application.css");
		primaryStage.setScene(scene);
		primaryStage.setTitle("Photo Album");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
