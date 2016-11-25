package controllers;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.AdminList;

/**
 * This interface contains default methods for handling
 * logout and quit events that exist from the MenuBar
 * interaction on every scene.
 * @author Derek Schatel
 *
 */
public interface LogoutEvent {

	/** Handles logout selection from MenuBar. Saves the current serialized
	 * userlist and returns to the login screen.
	 *
	 * @param root The root item of the fxml view
	 * @param list the serialized userlist
	 * @throws ClassNotFoundException
	 */
	default void logout (Parent root, AdminList list) throws ClassNotFoundException {

		Parent parent;

		try {

			AdminList.saveToFile(list);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
			parent = (Parent) loader.load();

			Scene scene = new Scene(parent);

			Stage stage = (Stage) root.getScene().getWindow();
			stage.setScene(scene);

			LoginController loginController = loader.getController();
			loginController.start(stage);
			stage.setTitle("Photo Album");
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Saves the userlist and then exits the application.
	 * @param list the serialized userlist
	 */
	default void quit (AdminList list) {

		   try {
			AdminList.saveToFile(list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Platform.exit();
	}

}
