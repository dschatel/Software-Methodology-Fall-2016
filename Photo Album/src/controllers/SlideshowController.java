package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.*;

/** This class controls the slideshow view
 * for a specified album, as represented in
 * slideshow.fxml. Implements LogoutEvent.
 * @author Derek Schatel
 *
 */
public class SlideshowController implements LogoutEvent {

	@FXML
	private Parent root;

	@FXML
	private MenuItem logOut;

	@FXML
	private MenuItem quit;

	@FXML
	private Button slideShowBack;

	@FXML
	private Button slideShowForward;

	@FXML
	private Button returnToAlbum;

	@FXML
	private ImageView imageContainer;

	private AdminList list;
	private User user;
	private Album album;
	private List<Photo> photoList = new ArrayList<Photo>();
	private int index;

	/** Sets up the initial slideshow view. Disables
	 * forward and back buttons depending on the length of the photo
	 * list.
	 * @param mainstage the main stage
	 */
	public void start (Stage mainstage) {

		mainstage.setTitle("Viewing SlideShow for " + album.getAlbumName());
		photoList = album.getPhotoList();
		index = 0;


		imageContainer.setImage(photoList.get(index).getImage());

		slideShowBack.setDisable(true);
		if (photoList.size() == 1)
			slideShowForward.setDisable(true);

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


		handleForwardButton();
		handleBackButton();
		handleReturnButton(mainstage);


	}

	/**
	 * Progresses the slideshow forward one image, replacing the image
	 * in the ImageView with the next photo in the list.
	 */
	private void handleForwardButton () {

		slideShowForward.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				index++;
				imageContainer.setImage(photoList.get(index).getImage());

				if(index == photoList.size() -1)
					slideShowForward.setDisable(true);

				if (index > 0)
					slideShowBack.setDisable(false);



			}
		});


	}

	/**
	 * Sends the slideshow back one image, replacing the image in the
	 * ImageView with the previous photo in the list.
	 */
	private void handleBackButton () {

		slideShowBack.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				index--;
				imageContainer.setImage(photoList.get(index).getImage());

				if(index == 0)
					slideShowBack.setDisable(true);

				if (index < photoList.size())
					slideShowForward.setDisable(false);



			}
		});

	}

	/** Transitions the scene back to the album view for that album.
	 * @param mainstage the main stage
	 */
	private void handleReturnButton(Stage mainstage) {
		returnToAlbum.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				Parent parent;

				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/albumview.fxml"));
				try {
					parent = (Parent) loader.load();
					AlbumViewController albumViewController = loader.getController();
					albumViewController.setAdminList(list);
					albumViewController.setUser(user);
					albumViewController.setAlbum(album);
					Scene scene = new Scene(parent);

					albumViewController.start(mainstage);
					mainstage.setScene(scene);
					mainstage.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

	/** Allows a calling class to set the current user
	 * @param user the current user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/** Allows a calling class to set the current album.
	 * @param album the current album
	 */
	public void setAlbum(Album album) {
		this.album = album;
	}

}
