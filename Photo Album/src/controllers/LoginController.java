package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.*;

/** This class handles the login view represented by
 * login.fxml. Verifies user or admin authentication.
 * Implements LogoutEvent.
 *
 * @author Derek Schatel
 *
 */
public class LoginController implements LogoutEvent {

	@FXML
	private MenuItem logOut;

	@FXML
	private MenuItem quit;

	@FXML
	private TextField usernameField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private Button loginButton;

	@FXML
	private Text errorText;

	@FXML
	private Parent root;

	private AdminList list;

	/** Sets up the initial application display on the login page.
	 * The "log out" menuitem is disabled on this view.
	 *
	 * @param mainstage the main stage
	 */
	public void start (Stage mainstage) {

		errorText.setText("");

		try {
			list = AdminList.readFromFile();
		} catch (ClassNotFoundException | IOException e1) {
			User admin = new User("admin", "admin", "Admin");
			list = new AdminList();
			list.addUser(admin);
			try {
				AdminList.saveToFile(list);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		logOut.setDisable(true);
		handleButtons(mainstage);

		quit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				quit(list);
			}

		});

	}

	/** Handles the login button. Retrieves input from user/pass fields
	 * and checks against the serialized list of users. If admin, transitions
	 * to admin subsystem. Otherwise transitions to the specified user's album summary
	 * view.
	 * @param mainstage the main stage
	 */
	private void handleButtons (Stage mainstage) {

		loginButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				String user = usernameField.getText();
				String pass = passwordField.getText();
				Parent parent;

				if(list.doesUserExist(user, pass)) {
					if (user.equals("admin") && pass.equals("admin")) {
							FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin.fxml"));
							try {
								parent = (Parent) loader.load();
								AdminController adminController = loader.getController();
								Scene scene = new Scene(parent);

								adminController.start(mainstage);

								mainstage.setScene(scene);
								mainstage.show();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

					}
					else {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/userview.fxml"));
						try {
							parent = (Parent) loader.load();
							UserViewController userViewController = loader.getController();
							userViewController.setAdminList(list);
							userViewController.setUser(list.getUser(user));
							Scene scene = new Scene(parent);

							userViewController.start(mainstage);
							mainstage.setScene(scene);
							mainstage.show();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
				else
					errorText.setText("Error: Invalid User/Pass Combination");

			}

		});

	}

}
