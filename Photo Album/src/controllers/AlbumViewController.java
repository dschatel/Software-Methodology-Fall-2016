package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;

/**
 * This class controls the album view represented by
 * albumview.fxml. Displays information in a tableview
 * representing all photos in the specified album.
 * Implements LogoutEvent.
 * @author Derek Schatel
 *
 *
 */
public class AlbumViewController implements LogoutEvent {

	@FXML
	private Parent root;

	@FXML
	private MenuItem logOut;

	@FXML
	private MenuItem quit;

	@FXML
	private Button addPhoto;

	@FXML
	private Button deletePhoto;

	@FXML
	private Button viewPhoto;

	@FXML
	private Button slideShow;

	@FXML
	private Button returnToAlbum;

	@FXML
	private TableView<Photo> photoList;

	@FXML
	private TableColumn<Photo, Image> photoPreview;

	@FXML
	private TableColumn<Photo, String> caption;

	@FXML
	private TableColumn<Photo, String> dateAdded;

	private ObservableList<Photo> obsList;
	private AdminList list;
	private User user;
	private Album album;


	/** Sets up the initial album view.
	 * @param mainstage the main stage
	 */
	public void start (Stage mainstage) {

		mainstage.setTitle("Viewing " + album.getAlbumName());

		obsList = FXCollections.observableArrayList(album.getPhotoList());

		photoPreview.setCellValueFactory(new PropertyValueFactory<Photo, Image>("thumbNail"));
		dateAdded.setCellValueFactory(new PropertyValueFactory<Photo, String>("date"));
		caption.setCellValueFactory(new PropertyValueFactory<Photo, String>("caption"));

		photoList.setItems(obsList);


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
		handleDeleteButton();
		handleReturnButton(mainstage);
		handleOpenButton(mainstage);
		handleSlideshowButton(mainstage);

	}

	/** Allows a calling class to pass the global administrator list to this view
	 * @param adminList the serialized list of all users
	 */
	public void setAdminList(AdminList adminList) {
		this.list = adminList;
	}

	/** Allows a calling class to pass the current user to this view
	 * @param user the current user being viewed
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/** Allows a calling class to pass the current album to this view
	 * @param album the current album being viewed
	 */
	public void setAlbum(Album album) {
		this.album = album;
	}

	/** Handles opening the selected photo from the list into a Photo View.
	 * @param mainstage the main stage
	 */
	private void handleOpenButton(Stage mainstage) {

		viewPhoto.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				if(photoList.getSelectionModel().getSelectedItem() != null) {

					Parent parent;

					FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/photoview.fxml"));
					try {
						parent = (Parent) loader.load();
						PhotoViewController photoViewController = loader.getController();
						photoViewController.setAdminList(list);
						photoViewController.setUser(user);
						photoViewController.setAlbum(album);
						photoViewController.setPhoto(photoList.getSelectionModel().getSelectedItem());

						Scene scene = new Scene(parent);

						photoViewController.start(mainstage);
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

	/**
	 * Adds a new photo to this album. Pops up a FileChooser dialog that allows the user
	 * to select either a JPG or PNG file. The method then checks the photo globally
	 * against all other photos in all user albums to avoid unnecessary duplication.
	 */
	private void handleAddButton() {

		addPhoto.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				FileChooser fileChooser = new FileChooser();

				FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
				FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
				fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

				fileChooser.setTitle("Upload Photo");
				Stage app_stage = (Stage) ((Node) arg0.getSource()).getScene().getWindow();
				File file = fileChooser.showOpenDialog(app_stage);

				if (file == null)
					return;

				 BufferedImage bufferedImage;
				try {
					bufferedImage = ImageIO.read(file);
				    Image image = SwingFXUtils.toFXImage(bufferedImage, null);

				    SerialImage serialImage = new SerialImage();
				    serialImage.setImage(image);
				    for (Photo p : album.getPhotoList()) {
				    	if(serialImage.equals(p.getSerialImage())) {
				    		Alert disallowed = new Alert(AlertType.INFORMATION);
							disallowed.setTitle("Add Photo Error");
							disallowed.setHeaderText("Photo Already Exists");
							disallowed.setContentText("ERROR: Cannot add a photo already in album.");
							disallowed.showAndWait();
				    		return;
				    	}
				    }

				    Photo photo = null;
				    boolean foundPhoto = false;

				    for (Album album: user.getAlbums()) {
				    	for (Photo p: album.getPhotoList()) {
				    		if(serialImage.equals(p.getSerialImage())) {
				    			photo = p;
				    			foundPhoto = true;
				    			break;
				    		}
				    		if (foundPhoto)
				    			break;
				    	}
				    }

				    if(!foundPhoto) {
				    	photo = new Photo(image);
				    }

				    obsList.add(photo);
				    album.addPhoto(photo);


				    AdminList.saveToFile(list);


				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Prompts the user with a dialog box to confirm deletion of the selected
	 * photo. If confirmed, deleted the selected photo from the album.
	 */
	private void handleDeleteButton() {

		deletePhoto.setOnAction(new EventHandler<ActionEvent>() {


			@Override
			public void handle(ActionEvent arg0) {
				int recordToDelete = photoList.getSelectionModel().getSelectedIndex();

				if (recordToDelete < 0) {}
				else {
					Alert deletion = new Alert(AlertType.INFORMATION);
					deletion.setTitle("Delete Photo from Album");
					deletion.setHeaderText("Confirm Deletion");

					deletion.setContentText("Do you really want to delete this photo?");

					Optional<ButtonType> result = deletion.showAndWait();

					if (result.isPresent()) {
						obsList.remove(recordToDelete);
						album.removePhoto(recordToDelete);
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

	/** Transitions the scene to a slideshow view of the album.
	 * Produces error alert if the album is currently empty.
	 * @param mainstage the main stage
	 */
	private void handleSlideshowButton(Stage mainstage) {

			slideShow.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {

					if (album.getPhotoList().size() == 0) {
						Alert disallowed = new Alert(AlertType.INFORMATION);
						disallowed.setTitle("No Photos");
						disallowed.setHeaderText("No Photos in Album");
						disallowed.setContentText("ERROR: Cannot play slideshow for empty album!");
						disallowed.showAndWait();

					}
					else {
						Parent parent;

						FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/slideshow.fxml"));
						try {
							parent = (Parent) loader.load();
							SlideshowController slideshowController = loader.getController();
							slideshowController.setAdminList(list);
							slideshowController.setUser(user);
							slideshowController.setAlbum(album);
							Scene scene = new Scene(parent);

							slideshowController.start(mainstage);
							mainstage.setScene(scene);
							mainstage.show();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		}

	/** Transitions the scene back to the album summary list for the specified user.
	 * @param mainstage the main stage
	 */
	private void handleReturnButton(Stage mainstage) {
		returnToAlbum.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				Parent parent;

				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/userview.fxml"));
				try {
					parent = (Parent) loader.load();
					UserViewController userViewController = loader.getController();
					userViewController.setAdminList(list);
					userViewController.setUser(user);
					Scene scene = new Scene(parent);

					userViewController.start(mainstage);
					mainstage.setScene(scene);
					mainstage.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}


}
