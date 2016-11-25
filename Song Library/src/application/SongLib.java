//Derek Schatel and Monica Ho

package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import song.view.SongController;

public class SongLib extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/song/view/songapp.fxml"));

		AnchorPane root = (AnchorPane)loader.load();

		SongController songController = loader.getController();
		songController.start(primaryStage);

		Scene scene = new Scene(root);
		scene.getStylesheets().add("/application/application.css");
		primaryStage.setScene(scene);
		primaryStage.setTitle("Song Library");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
