package controllers;

import java.io.IOException;
import java.util.Optional;

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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

/**
 * This class handles the view for individual photos
 * as represented by photoview.fxml. Displays a
 * larger view of the selected photo. Implements
 * LogoutEvent.
 *
 * @author Derek Schatel
 *
 */
public class PhotoViewController implements LogoutEvent {

	@FXML
	private Parent root;

	@FXML
	private MenuItem logOut;

	@FXML
	private MenuItem quit;

	@FXML
	private Button editCaption;

	@FXML
	private Button movePhoto;

	@FXML
	private Button copyPhoto;

	@FXML
	private Button addTag;

	@FXML
	private Button deleteTag;

	@FXML
	private Button returnToAlbum;

	@FXML
	private ImageView imageContainer;

	@FXML
	private ListView<Tags> tagList;

	@FXML
	private Text captionField;

	@FXML
	private Text dateAdded;

	private ObservableList<Tags> obsList;
	private AdminList list;
	private User user;
	private Album album;
	private Photo photo;

	/** Sets up initial view for the photo.
	 * Sets the photo in the imageView and
	 * updates specific details about the image.
	 * @param mainstage the main stage
	 */
	public void start (Stage mainstage) {

		mainstage.setTitle("Viewing Photo");

		imageContainer.setImage(photo.getImage());
		captionField.setText("Caption: " + photo.getCaption());
		dateAdded.setText("Date Added: " + photo.getDate());


		obsList = FXCollections.observableArrayList(photo.getTags());

		 tagList.setCellFactory(new Callback<ListView<Tags>, ListCell<Tags>>() {

				@Override
				public ListCell<Tags> call(ListView<Tags> arg0) {
						ListCell<Tags> cell = new ListCell<Tags>(){

						@Override
						protected void updateItem(Tags tags, boolean bool) {
							super.updateItem(tags, bool);
							if(tags != null) {
								setText(tags.toString());
							}
							else
								setText("");
						}

					};
					return cell;
				}
			 });

		tagList.setItems(obsList);


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

		handleReturnButton(mainstage);
		handleCaptionButton();
		handleAddTag();
		handleDeleteTag();
		handleMovePhoto();
		handleCopyPhoto();


	}

	/** Allows a calling class to set the serialized adminlist.
	 * @param adminList the adminlist
	 */
	public void setAdminList(AdminList adminList) {
		this.list = adminList;
	}

	/** Allows a calling class to set the current user
	 * @param user the current user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/** Allows a calling class to set the current album
	 * @param album the current album
	 */
	public void setAlbum(Album album) {
		this.album = album;
	}

	/** Allows a calling class to set the current photo
	 * @param photo the current photo
	 */
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	/**
	 * Handles adding tags to the photo. Prompts
	 * the user with a dialog box for tag type and value,
	 * then adds the tag to the tag list.
	 */
	private void handleAddTag() {

		addTag.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Dialog<Tags> dialog = new Dialog<>();
				dialog.setTitle("Add Tag");
				dialog.setHeaderText("Add a Tag to the Photo:");
				dialog.setResizable(false);

				   Label tagType = new Label("Tag Type: ");
				   Label tagValue = new Label("Tag Value: ");
				   TextField tagTypeInput = new TextField();
				   TextField tagValueInput = new TextField();

				   GridPane grid = new GridPane();
				   grid.add(tagType, 1, 1);
				   grid.add(tagTypeInput, 2, 1);
				   grid.add(tagValue, 1, 2);
				   grid.add(tagValueInput, 2, 2);

				   dialog.getDialogPane().setContent(grid);

				   ButtonType buttonTypeOk = new ButtonType("Add Tag", ButtonData.OK_DONE);
				   dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

				   dialog.setResultConverter(new Callback<ButtonType, Tags>() {

					@Override
					public Tags call(ButtonType arg0) {
						if (arg0 == buttonTypeOk) {
							if (tagTypeInput.getText().equals("") || tagValueInput.getText().equals("")) {
							    Alert disallowed = new Alert(AlertType.INFORMATION);
								disallowed.setTitle("Add Tag Error");
								disallowed.setHeaderText("Blank type or value fields");
								disallowed.setContentText("ERROR: Cannot add a tag without type or value.");
								disallowed.showAndWait();
								return null;
							}

							return new Tags(tagTypeInput.getText(), tagValueInput.getText());
						}
						return null;
					}

				   });

				   Optional<Tags> result = dialog.showAndWait();

				   if(result.isPresent()) {
					   Tags newTag = (Tags) result.get();
					   obsList.add(newTag);
					   photo.getTags().add(newTag);
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

	/**
	 * Prompts the user to confirm tag deletion. On confirmation,
	 * deletes the selected tag from the tag list.
	 */
	private void handleDeleteTag() {
		deleteTag.setOnAction(new EventHandler<ActionEvent>() {


			@Override
			public void handle(ActionEvent arg0) {
				int recordToDelete = tagList.getSelectionModel().getSelectedIndex();

				if (recordToDelete < 0) {}
				else {
					Alert deletion = new Alert(AlertType.INFORMATION);
					deletion.setTitle("Delete Tag from Photo");
					deletion.setHeaderText("Confirm Deletion");

					deletion.setContentText("Do you really want to delete this tag?");

					Optional<ButtonType> result = deletion.showAndWait();

					if (result.isPresent()) {
						obsList.remove(recordToDelete);
						photo.removeTag(recordToDelete);
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
	 * Prompts the user with a dialog box displaying a ListView
	 * of all albums in the user's account. Selecting an album
	 * and confirming move will then remove the photo from the current
	 * album and add it to the photo list of the specified album.
	 */
	private void handleMovePhoto() {
		movePhoto.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Dialog<Album> dialog = new Dialog<>();
				dialog.setTitle("Move Photo");
				dialog.setHeaderText("Choose an Album:");
				dialog.setResizable(false);

				ListView<Album> albums = new ListView<Album>();
				ObservableList <Album> albumList = FXCollections.observableArrayList(user.getAlbums());

				 albums.setCellFactory(new Callback<ListView<Album>, ListCell<Album>>() {

						@Override
						public ListCell<Album> call(ListView<Album> arg0) {
								ListCell<Album> cell = new ListCell<Album>(){

								@Override
								protected void updateItem(Album album, boolean bool) {
									super.updateItem(album, bool);
									if(album != null) {
										setText(album.getAlbumName());
									}
									else
										setText("");
								}

							};
							return cell;
						}
					 });

				 albums.setItems(albumList);
				 albums.setMaxHeight(100);


				   GridPane grid = new GridPane();
				   grid.add(albums, 1, 1);

				   dialog.getDialogPane().setContent(grid);
				   albums.getSelectionModel().select(0);

				   ButtonType buttonTypeOk = new ButtonType("Move Photo", ButtonData.OK_DONE);
				   dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);


				   Optional<Album> result = dialog.showAndWait();

				   if(result.isPresent()) {

					   Album albumToMove = albums.getSelectionModel().getSelectedItem();
					   if(albumToMove != null) {

						   boolean searchForDupe = false;

						   for (Photo p : albumToMove.getPhotoList()) {
							   if (p.equals(photo)) {
								   	searchForDupe = true;
								   	Alert disallowed = new Alert(AlertType.INFORMATION);
									disallowed.setTitle("Move Error");
									disallowed.setHeaderText("Cannot Move Photo");
									disallowed.setContentText("ERROR: Photo already exists in target Album!");
									disallowed.showAndWait();
									break;
							   }
						   }

						   if (!searchForDupe) {
							   albumToMove.addPhoto(photo);
							   album.removePhoto(album.getPhotoIndex(photo));

							   try {
								   AdminList.saveToFile(list);
							   } catch (IOException e) {
								   // TODO Auto-generated catch block
								   e.printStackTrace();
							   }
						   }
					   }
					   else {
						   //error notification
					   }
				   }

			}

		});

	}

	/**
	 * Will prompt the user with a ListView of all albums on the user's
	 * account. On confirmation, will add the current photo to the photo
	 * list of the specified album.
	 */
	private void handleCopyPhoto() {

		copyPhoto.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Dialog<Album> dialog = new Dialog<>();
				dialog.setTitle("Copy Photo");
				dialog.setHeaderText("Choose an Album:");
				dialog.setResizable(false);

				ListView<Album> albums = new ListView<Album>();
				ObservableList <Album> albumList = FXCollections.observableArrayList(user.getAlbums());

				 albums.setCellFactory(new Callback<ListView<Album>, ListCell<Album>>() {

						@Override
						public ListCell<Album> call(ListView<Album> arg0) {
								ListCell<Album> cell = new ListCell<Album>(){

								@Override
								protected void updateItem(Album album, boolean bool) {
									super.updateItem(album, bool);
									if(album != null) {
										setText(album.getAlbumName());
									}
									else
										setText("");
								}

							};
							return cell;
						}
					 });

				 	albums.setItems(albumList);
					albums.setMaxHeight(100);


				   GridPane grid = new GridPane();
				   grid.add(albums, 1, 1);

				   dialog.getDialogPane().setContent(grid);
				   albums.getSelectionModel().select(0);


				   ButtonType buttonTypeOk = new ButtonType("Copy Photo", ButtonData.OK_DONE);
				   dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);


				   Optional<Album> result = dialog.showAndWait();

				   if(result.isPresent()) {

					   Album albumToCopy = albums.getSelectionModel().getSelectedItem();

					   if (albumToCopy != null) {

						   boolean searchForDupe = false;

						   for (Photo p : albumToCopy.getPhotoList()) {
							   if (p.equals(photo)) {
								   	searchForDupe = true;
								   	Alert disallowed = new Alert(AlertType.INFORMATION);
									disallowed.setTitle("Copy Error");
									disallowed.setHeaderText("Cannot Copy Photo");
									disallowed.setContentText("ERROR: Photo already exists in target Album!");
									disallowed.showAndWait();
									break;
							   }
						   }

						   if (!searchForDupe) {
							   albumToCopy.addPhoto(photo);
							   try {
								   AdminList.saveToFile(list);
							   } catch (IOException e) {
								   // TODO Auto-generated catch block
								   e.printStackTrace();
							   }
						   }
					   }
					   else {
						   //error notification
					   }
				   }

			}

		});

	}


	/** Transitions the scene to the album page containing
	 * the currently viewed photo or to the search page,
	 * depending on what view the user previously came from.
	 * @param mainstage the main stage
	 */
	private void handleReturnButton(Stage mainstage) {
		returnToAlbum.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				Parent parent;


				try {

					if (album == null) {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/search.fxml"));
						parent = (Parent) loader.load();
						SearchController returnController = loader.getController();
						returnController.setAdminList(list);
						returnController.setUser(user);
						Scene scene = new Scene(parent);
						returnController.start(mainstage);
						mainstage.setScene(scene);
						mainstage.show();
					}
					else {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/albumview.fxml"));
						parent = (Parent) loader.load();
						AlbumViewController albumViewController = loader.getController();
						albumViewController.setAdminList(list);
						albumViewController.setUser(user);
						albumViewController.setAlbum(album);
						Scene scene = new Scene(parent);
						albumViewController.start(mainstage);
						mainstage.setScene(scene);
						mainstage.show();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Adds a caption to the photo.
	 */
	private void handleCaptionButton() {

		editCaption.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Dialog<String> dialog = new Dialog<>();
				dialog.setTitle("Edit Caption");
				dialog.setHeaderText("Edit the Photo's Caption:");
				dialog.setResizable(false);

				 Label captionText = new Label("Caption: ");
				 TextField captionInput = new TextField();
				 captionInput.setText(photo.getCaption());

				   GridPane grid = new GridPane();
				   grid.add(captionText, 1, 1);
				   grid.add(captionInput, 2, 1);

				   dialog.getDialogPane().setContent(grid);

				   ButtonType buttonTypeOk = new ButtonType("Edit Caption", ButtonData.OK_DONE);
				   dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

				   dialog.setResultConverter(new Callback<ButtonType, String>() {

					@Override
					public String call(ButtonType arg0) {
						if (arg0 == buttonTypeOk) {
							if (captionInput.getText().equals("")) {
							    Alert disallowed = new Alert(AlertType.INFORMATION);
								disallowed.setTitle("Edit Caption Error");
								disallowed.setHeaderText("Blank caption");
								disallowed.setContentText("ERROR: cannot have a blank caption.");
								disallowed.showAndWait();
								return null;
							}

							return new String (captionInput.getText());
						}
						return null;
					}

				   });

				   Optional<String> result = dialog.showAndWait();

				   if(result.isPresent()) {
					   String caption = (String) result.get();
						photo.setCaption(caption);
						captionField.setText("Caption: " + photo.getCaption());
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


}
