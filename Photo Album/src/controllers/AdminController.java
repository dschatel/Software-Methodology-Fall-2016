package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * This class controls the administrator subsystem represented by
 * admin.fxml. Handles the list of all users in the subsystem.
 * Implements LogoutEvent.
 * @author Derek Schatel
 *
 *
 *
 */
public class AdminController implements LogoutEvent {

	@FXML
	private MenuItem logOut;

	@FXML
	private MenuItem quit;

	@FXML
	private Button addUser;

	@FXML
	private Button deleteUser;

	@FXML
	private Parent root;

	@FXML
	private ListView<User> userList;

	@FXML
	private Text user;

	@FXML
	private Text date;

	@FXML
	private Text albums;

	@FXML
	private Text photos;

	@FXML
	private Text password;

	private ObservableList<User> obsList;
	private List<User> users = new ArrayList<User>();
	private AdminList list;




	/**Sets up the initial admin list view.
	 * @param mainstage the main stage
	 */
	public void start (Stage mainstage) {

		mainstage.setTitle("Administrator Control Panel");

		try {
			list = AdminList.readFromFile();
			users = list.getAdminList();
		} catch (ClassNotFoundException | IOException e1) {
			User admin = new User("admin", "admin", "Admin");
			list = new AdminList();
			list.addUser(admin);
			try {
				AdminList.saveToFile(list);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		obsList = FXCollections.observableArrayList(users);

		 userList.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {

				@Override
				public ListCell<User> call(ListView<User> arg0) {
						ListCell<User> cell = new ListCell<User>(){

						@Override
						protected void updateItem(User user, boolean bool) {
							super.updateItem(user, bool);
							if(user != null) {
								setText(user.getUser());
							}
							else
								setText("");
						}

					};
					return cell;
				}
			 });

		 userList.setItems(obsList);
		 accountDetailDisplay();
		 buttonHandling();

		 userList.getSelectionModel().select(0);

		logOut.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				try {
					logout(root, list);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

		quit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				quit(list);
			}

		});

	}

	/**
	 * Handles dynamic display of selected user account information.
	 */
	private void accountDetailDisplay () {

		userList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

			if(newValue != null) {
			user.setText("User: " + newValue.getName());
			date.setText("Created On: " + newValue.getCreatedDate());
			albums.setText("Total Albums: " + newValue.getTotalAlbums());
			photos.setText("Total Photos: " + newValue.getTotalPhotos());
			password.setText("Password: " + newValue.getPassword());
			}

		});

	}

	/**
	 * Handles interaction with Add and Delete user buttons.
	 * Add user prompts the user with a dialog box to provide a username,
	 * password and full account name. Delete user prompts the user with a
	 * confirmation dialog box for deleting the selected user from the list.
	 */
	private void buttonHandling() {
		addUser.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Dialog<User> dialog = new Dialog<>();
				dialog.setTitle("Add User to System");
				dialog.setHeaderText("Add a New User:");
				dialog.setResizable(false);

				   Label username = new Label("Username: ");
				   Label password = new Label("Password: ");
				   Label nameLabel = new Label("User's Full Name: ");
				   TextField usernameInput = new TextField();
				   TextField passwordInput = new TextField();
				   TextField nameInput = new TextField();

				   GridPane grid = new GridPane();
				   grid.add(username, 1, 1);
				   grid.add(usernameInput, 2, 1);
				   grid.add(password, 1, 2);
				   grid.add(passwordInput, 2, 2);
				   grid.add(nameLabel, 1, 3);
				   grid.add(nameInput, 2, 3);

				   dialog.getDialogPane().setContent(grid);

				   ButtonType buttonTypeOk = new ButtonType("Add User", ButtonData.OK_DONE);
				   dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

				   dialog.setResultConverter(new Callback<ButtonType, User>() {

					@Override
					public User call(ButtonType arg0) {
						if (arg0 == buttonTypeOk) {
							if (usernameInput.getText().equals("") || passwordInput.getText().equals("") || nameInput.getText().equals("")) {
							    Alert disallowed = new Alert(AlertType.INFORMATION);
								disallowed.setTitle("Add User Error");
								disallowed.setHeaderText("Insufficient Fields");
								disallowed.setContentText("ERROR: Fields left blank.");
								disallowed.showAndWait();
								return null;
							}
							else return new User(usernameInput.getText(), passwordInput.getText(), nameInput.getText());
						}
						return null;
					}

				   });

				   Optional<User> result = dialog.showAndWait();

				   if(result.isPresent()) {
					   User newUser = (User) result.get();

					   if (newUser.getName().equals("admin")) {
						    Alert disallowed = new Alert(AlertType.INFORMATION);
							disallowed.setTitle("Add User Error");
							disallowed.setHeaderText("Cannot Add Admin");
							disallowed.setContentText("ERROR: Cannot add another administrator account.");
							disallowed.showAndWait();
							return;
					   }


					   for (User u: users) {
						   if (u.getUser().equals(newUser.getUser())) {
							   Alert disallowed = new Alert(AlertType.INFORMATION);
								disallowed.setTitle("Add User Error");
								disallowed.setHeaderText("Cannot Add Account");
								disallowed.setContentText("ERROR: Account with that name already exists.");
								disallowed.showAndWait();
								return;
						   }

					   }
					   obsList.add(newUser);
					   users.add(newUser);
					   try {
						AdminList.saveToFile(list);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				   }

			}

		});

		deleteUser.setOnAction(new EventHandler<ActionEvent>() {


			@Override
			public void handle(ActionEvent arg0) {
				int recordToDelete = userList.getSelectionModel().getSelectedIndex();

				Alert deletion = new Alert(AlertType.INFORMATION);
				deletion.setTitle("Delete User from System");
				deletion.setHeaderText("Confirm Deletion");

				deletion.setContentText("Do you really want to delete the account: " + obsList.get(recordToDelete).getUser() + "?");

				Optional<ButtonType> result = deletion.showAndWait();

				if (!obsList.get(recordToDelete).getUser().equals("admin") && result.isPresent()) {
					obsList.remove(recordToDelete);
					users.remove(recordToDelete);
					try {
						AdminList.saveToFile(list);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(obsList.get(recordToDelete).getUser().equals("admin")) {
					Alert disallowed = new Alert(AlertType.INFORMATION);
					disallowed.setTitle("Deletion Error");
					disallowed.setHeaderText("Cannot Remove Account");
					disallowed.setContentText("ERROR: Cannot delete administrator account.");
					disallowed.showAndWait();
				}

			}

		});
	}

}
