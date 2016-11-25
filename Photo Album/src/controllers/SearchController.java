package controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

/** This class controls the search functionality
 * represented by search.fxml. Allows the user
 * to search through all photos in the user's account
 * by date or by tag. Implements LogoutEvent.
 *
 * @author Derek Schatel
 *
 */
public class SearchController implements LogoutEvent {

	@FXML
	private Parent root;

	@FXML
	private MenuItem logOut;

	@FXML
	private MenuItem quit;

	@FXML
	private Button formAlbum;

	@FXML
	private Button searchPhotos;

	@FXML
	private Button viewPhoto;

	@FXML
	private Button returnToSummary;

	@FXML
	private ListView<Tags> tagList;

	@FXML
	private TextField tagType;

	@FXML
	private TextField tagValue;

	@FXML
	private Button addTag;

	@FXML
	private Button deleteTag;

	@FXML
	private DatePicker dateStartRange;

	@FXML
	private DatePicker dateEndRange;

	@FXML
	private TableView<Photo> photoList;

	@FXML
	private TableColumn<Photo, Image> photoPreview;

	@FXML
	private TableColumn<Photo, String> caption;

	@FXML
	private TableColumn<Photo, String> dateAdded;

	private ObservableList<Photo> photoObsList;
	private ObservableList<Tags> tagObsList;
	private AdminList list;
	private User user;
	private Album tempAlbum;
	private List<Tags> tags;


	/** Sets up the initial view for the search
	 * screen.
	 * @param mainstage the main stage
	 */
	public void start (Stage mainstage) {

		tags = new ArrayList<Tags>();
		tagObsList = FXCollections.observableArrayList(tags);
		tagList.setItems(tagObsList);

		mainstage.setTitle("Search Albums");


		photoPreview.setCellValueFactory(new PropertyValueFactory<Photo, Image>("thumbNail"));
		dateAdded.setCellValueFactory(new PropertyValueFactory<Photo, String>("date"));
		caption.setCellValueFactory(new PropertyValueFactory<Photo, String>("caption"));


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

		handleSearch();
		addTags();
		deleteTags();
		handleReturnButton(mainstage);
		handleViewButton(mainstage);
		handleCreateButton();

	}

	/** Transitions the scene back to the user's
	 * album summary page.
	 * @param mainstage the main stage
	 */
	private void handleReturnButton(Stage mainstage) {

		returnToSummary.setOnAction(new EventHandler<ActionEvent>() {

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

	/**
	 * Created a new album from the list
	 * of search results and adds it to the user's
	 * account.
	 */
	private void handleCreateButton() {

		formAlbum.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				if(tempAlbum == null || tempAlbum.getPhotoList().isEmpty()) {}
				else {
					Dialog<String> dialog = new Dialog<>();
					dialog.setTitle("Form Album");
					dialog.setHeaderText("Give the Album a Name:");
					dialog.setResizable(false);

					 Label newAlbumName = new Label("Album Name: ");
					 TextField newAlbumInput = new TextField();

					   GridPane grid = new GridPane();
					   grid.add(newAlbumName, 1, 1);
					   grid.add(newAlbumInput, 2, 1);

					   dialog.getDialogPane().setContent(grid);

					   ButtonType buttonTypeOk = new ButtonType("Form Album", ButtonData.OK_DONE);
					   dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

					   dialog.setResultConverter(new Callback<ButtonType, String>() {

						@Override
						public String call(ButtonType arg0) {
							if (arg0 == buttonTypeOk) {
								if(newAlbumInput.getText().equals("")) {
								    Alert disallowed = new Alert(AlertType.INFORMATION);
									disallowed.setTitle("Create Album Error");
									disallowed.setHeaderText("Blank Album Name");
									disallowed.setContentText("ERROR: Cannot create album with blank name");
									disallowed.showAndWait();
									return null;
								}

								return newAlbumInput.getText();
							}
							return null;
						}

					   });

					   Optional<String> result = dialog.showAndWait();

					   if(result.isPresent()) {
						   	tempAlbum.setAlbumName(result.get());
							user.addAlbum(tempAlbum);
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

	/** Transitions to a Photo View of the selected photo.
	 * @param mainstage the main stage
	 */
	private void handleViewButton(Stage mainstage) {

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
	 * Starts a search given the specified parameters.
	 * Checks the start/end date ranges specified by the DatePicker and
	 * pulls all photos associated with those dates as well as any
	 * specified tags in the tag list.
	 */
	private void handleSearch() {

		searchPhotos.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				LocalDate start = null;
				LocalDate end = null;

				if(tags.isEmpty() && dateStartRange.getValue() == null && dateEndRange.getValue() == null) {
					return;
				}

				if(dateStartRange.getValue() != null && dateEndRange.getValue() != null && dateStartRange.getValue().isAfter(dateEndRange.getValue())) {
				    Alert disallowed = new Alert(AlertType.INFORMATION);
					disallowed.setTitle("Search Error");
					disallowed.setHeaderText("Incorrect Search Criteria");
					disallowed.setContentText("ERROR: Please select a range of dates that are sequential.");
					disallowed.showAndWait();
				}

				if(dateStartRange.getValue() == null)
					start = LocalDate.MIN;
				else
					start = dateStartRange.getValue();

				if(dateEndRange.getValue() == null)
					end = LocalDate.MAX;
				else
					end = dateEndRange.getValue();

				tempAlbum = new Album("tempAlbum");
				photoObsList = FXCollections.observableArrayList(tempAlbum.getPhotoList());
				photoList.setItems(photoObsList);

				for(Album a: user.getAlbums()) {
					for (Photo p: a.getPhotoList()) {
						if(tags.isEmpty()) {
							if (checkSearchDate(start, end, p)) {
								tempAlbum.addPhoto(p);
								photoObsList.add(p);
							}
						}
						else {
							if(checkTags(tags, p.getTags()) && checkSearchDate(start, end, p)) {
								tempAlbum.addPhoto(p);
								photoObsList.add(p);
							}

						}

					}
				}



			}
		});


	}

	/**
	 *  Helper function to check whether a photo is within the date ranges specified
	 *  by the DatePickers.
	 * @param start the start date
	 * @param end the end date
	 * @param p the photo being checked
	 * @return whether the photo is within the date range
	 */
	private boolean checkSearchDate(LocalDate start, LocalDate end, Photo p) {
		LocalDate photoDate = p.getCalendar().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return photoDate.isAfter(start) && photoDate.isBefore(end) || photoDate.equals(start) || photoDate.equals(end);


	}

	/** Checks whether a photo contains one of the tags specified in the tag list
	 * @param searchedTags the list of tags to be searched for
	 * @param photoTags the list of that specific photo's tags
	 * @return whether the photo's tags contains one of the tags being searched for
	 */
	private boolean checkTags (List<Tags> searchedTags, List<Tags> photoTags) {

		for (Tags pt: photoTags) {
			for (Tags st: searchedTags) {
				if (st.equals(pt))
					return true;
			}
		}
		return false;
	}

	/**
	 * Deletes the specified tag from the search tag list.
	 */
	private void deleteTags() {
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
						tagObsList.remove(recordToDelete);
						tags.remove(recordToDelete);
					}
				}
			}

		});
	}

	/**
	 * Adds a tag to the search tag list.
	 */
	private void addTags() {

		addTag.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

						if (tagType.getText().equals("") || tagValue.getText().equals("")) {
						    Alert disallowed = new Alert(AlertType.INFORMATION);
							disallowed.setTitle("Add Tag Error");
							disallowed.setHeaderText("Blank Tag fields");
							disallowed.setContentText("ERROR: Cannot add tag without type and value.");
							disallowed.showAndWait();
							return;
						}

					   Tags newTag = new Tags(tagType.getText(), tagValue.getText());

					   boolean alreadyExists = false;

					   for (Tags t: tags) {
						   if(t.equals(newTag)) {
							   alreadyExists = true;
							   break;
							}
					   }

					   if (!alreadyExists) {
					   tagObsList.add(newTag);
					   tags.add(newTag);
					   }

					   tagType.clear();
					   tagValue.clear();

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


}
