package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

import java.io.IOException;
import java.util.Optional;

/** This class controls the user's album summary page
 * as represented in userview.fxml. Shows a list of all
 * albums in the user's account. Implements LogoutEvent.
 * @author Derek Schatel
 *
 */
public class UserViewController implements LogoutEvent {

	@FXML
	private Parent root;

	@FXML
	private MenuItem logOut;

	@FXML
	private MenuItem quit;

	@FXML
	private Button createAlbum;

	@FXML
	private Button renameAlbum;

	@FXML
	private Button deleteAlbum;

	@FXML
	private Button searchAlbums;

	@FXML
	private Button openAlbum;

	@FXML
	private TableView<Album> albumData;

	@FXML
	private TableColumn<Album, String> albumName;

	@FXML
	private TableColumn<Album, String> dateRange;

	@FXML
	private TableColumn<Album, String> oldestPhoto;

	@FXML
	private TableColumn<Album, String> numPhotos;

	private ObservableList<Album> obsList;
	//private List<User> users = new ArrayList<User>();
	private AdminList list;
	private User user;

	/** Sets up the initial view for the UserView.
	 * @param mainstage the main stage
	 */
	public void start (Stage mainstage) {

		mainstage.setTitle("Welcome, " + user.getName());

		obsList = FXCollections.observableArrayList(user.getAlbums());

		albumName.setCellValueFactory(new PropertyValueFactory<Album, String>("albumName"));
		dateRange.setCellValueFactory(new PropertyValueFactory<Album, String>("dateRange"));
		oldestPhoto.setCellValueFactory(new PropertyValueFactory<Album, String>("oldestPhoto"));
		numPhotos.setCellValueFactory(new PropertyValueFactory<Album, String>("numPhotos"));

		albumData.setItems(obsList);

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

		handleAddButton();
		handleRenameButton();
		handleDeleteButton();
		handleOpenButton(mainstage);
		handleSearchButton(mainstage);



	}

	/**
	 * Allows the user to rename the currently
	 * selected album. Prompts with a dialog
	 * box to enable input.
	 */
	private void handleRenameButton () {

		renameAlbum.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				int albumToRename = albumData.getSelectionModel().getSelectedIndex();

				if (albumToRename < 0) {}
				else {
					Dialog<String> dialog = new Dialog<>();
					dialog.setTitle("Rename Album");
					dialog.setHeaderText("Rename the Selected Album:");
					dialog.setResizable(false);

					Label renameAlbum = new Label("Album Name: ");
					TextField renameAlbumInput = new TextField();
					renameAlbumInput.setText(albumData.getSelectionModel().getSelectedItem().getAlbumName());

					GridPane grid = new GridPane();
					grid.add(renameAlbum, 1, 1);
					grid.add(renameAlbumInput, 2, 1);

					dialog.getDialogPane().setContent(grid);

					ButtonType buttonTypeOk = new ButtonType("Rename Album", ButtonData.OK_DONE);
					dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

					dialog.setResultConverter(new Callback<ButtonType, String>() {

						@Override
						public String call(ButtonType arg0) {
							if (arg0 == buttonTypeOk) {

								if(renameAlbumInput.getText().equals("")) {
								    Alert disallowed = new Alert(AlertType.INFORMATION);
									disallowed.setTitle("Rename Album Error");
									disallowed.setHeaderText("Blank Album Name");
									disallowed.setContentText("ERROR: Cannot have album with no name.");
									disallowed.showAndWait();
									return null;
								}

								return renameAlbumInput.getText();
							}
							return null;
						}

					   });

				   Optional<String> result = dialog.showAndWait();

				   if(result.isPresent()) {
					   	user.getAlbums().get(albumToRename).setAlbumName(result.get());
					   	albumData.refresh();

						try {
							AdminList.saveToFile(list);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				   }
				}
			}

		});


	}

	/**
	 * Allows the user to delete the currently selected album.
	 * Prompts with an alert box.
	 */
	private void handleDeleteButton() {


		deleteAlbum.setOnAction(new EventHandler<ActionEvent>() {


			@Override
			public void handle(ActionEvent arg0) {
				int recordToDelete = albumData.getSelectionModel().getSelectedIndex();

				if (recordToDelete < 0) {}
				else {
					Alert deletion = new Alert(AlertType.INFORMATION);
					deletion.setTitle("Delete Album");
					deletion.setHeaderText("Confirm Deletion");

					deletion.setContentText("Do you really want to delete the Album: " + obsList.get(recordToDelete).getAlbumName() + "?");

					Optional<ButtonType> result = deletion.showAndWait();

					if (result.isPresent()) {
						obsList.remove(recordToDelete);
						user.getAlbums().remove(recordToDelete);
						try {
							AdminList.saveToFile(list);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}

		});

	}

	/** Transitions the scene to the currently selected
	 * album in the list.
	 * @param mainstage the main stage
	 */
	private void handleOpenButton (Stage mainstage) {


		openAlbum.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				Parent parent;


				if(albumData.getSelectionModel().getSelectedItem() != null) {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/albumview.fxml"));
					try {
						parent = (Parent) loader.load();
						AlbumViewController albumViewController = loader.getController();
						albumViewController.setAdminList(list);
						albumViewController.setUser(user);
						albumViewController.setAlbum(albumData.getSelectionModel().getSelectedItem());
						Scene scene = new Scene(parent);

						albumViewController.start(mainstage);
						mainstage.setScene(scene);
						mainstage.show();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {}
			}
		});
	}

	/** Transitions the scene to the search menu.
	 * @param mainstage the main stage
	 */
	private void handleSearchButton (Stage mainstage) {

		searchAlbums.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				Parent parent;


				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/search.fxml"));
					try {
						parent = (Parent) loader.load();
						SearchController searchController = loader.getController();
						searchController.setAdminList(list);
						searchController.setUser(user);
						Scene scene = new Scene(parent);

						searchController.start(mainstage);
						mainstage.setScene(scene);
						mainstage.show();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

	}

	/**
	 * Prompts the user for an album name, then
	 * creates a new album in the user's list based on that
	 * name.
	 */
	private void handleAddButton() {

		createAlbum.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Dialog<Album> dialog = new Dialog<>();
				dialog.setTitle("New Album");
				dialog.setHeaderText("Add a New Album:");
				dialog.setResizable(false);

				 Label newAlbumName = new Label("Album Name: ");
				 TextField newAlbumInput = new TextField();

				   GridPane grid = new GridPane();
				   grid.add(newAlbumName, 1, 1);
				   grid.add(newAlbumInput, 2, 1);

				   dialog.getDialogPane().setContent(grid);

				   ButtonType buttonTypeOk = new ButtonType("Add Album", ButtonData.OK_DONE);
				   dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

				   dialog.setResultConverter(new Callback<ButtonType, Album>() {

					@Override
					public Album call(ButtonType arg0) {
						if (arg0 == buttonTypeOk) {
							if (newAlbumInput.getText().equals("")) {
							    Alert disallowed = new Alert(AlertType.INFORMATION);
								disallowed.setTitle("Add Album Error");
								disallowed.setHeaderText("No Album Name");
								disallowed.setContentText("ERROR: Cannot add an album with no name.");
								disallowed.showAndWait();
								return null;
							}
							else if (user.doesAlbumExist(newAlbumInput.getText())) {
							    Alert disallowed = new Alert(AlertType.INFORMATION);
								disallowed.setTitle("Add Album Error");
								disallowed.setHeaderText("Duplicate Album Name");
								disallowed.setContentText("ERROR: An Album with that name already exists.");
								disallowed.showAndWait();
								return null;
							}
							return new Album(newAlbumInput.getText());
						}
						return null;
					}

				   });

				   Optional<Album> result = dialog.showAndWait();

				   if(result.isPresent()) {
					   Album newAlbum = (Album) result.get();
						obsList.add(newAlbum);
						user.addAlbum(newAlbum);
						try {
							AdminList.saveToFile(list);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				   }
			}

		});

	}

	/** Allows a calling class to set the adminlist
	 * @param adminList the serialized adminlist
	 */
	public void setAdminList(AdminList adminList) {
		this.list = adminList;
	}

	/** Allows a callign class to set the current user
	 * @param user the current user
	 */
	public void setUser(User user) {
		this.user = user;
	}

}
