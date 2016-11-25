//Derek Schatel and Monica Ho

package song.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.StringTokenizer;

import application.SongObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class SongController {

	 @FXML
	 private ListView<SongObject> songList;

	 @FXML
	 private Button newSong;

	 @FXML
	 private Button editSong;

	 @FXML
	 private Button deleteSong;

	 @FXML
	 private TextField artistEntry;

	 @FXML
	 private TextField songEntry;

	 @FXML
	 private TextField albumEntry;

	 @FXML
	 private TextField yearEntry;

	 @FXML
	 private Text songText;

	 @FXML
	 private Text artistText;

	 @FXML
	 private Text albumText;

	 @FXML
	 private Text yearText;

	 @FXML
	 private Button confirmAction;

	 @FXML
	 private Button cancelAction;

	 @FXML
	 private Text actionLabel;

	 private String actionState;

	 private SongObject songToEdit;

	 private ObservableList<SongObject> obsList = FXCollections.observableArrayList();

	 public void start(Stage mainstage) {

		 confirmAction.setDisable(true);
		 cancelAction.setDisable(true);

		 //Logic to have ListView display Song Title instead of Object address ref
		 songList.setCellFactory(new Callback<ListView<SongObject>, ListCell<SongObject>>() {

			@Override
			public ListCell<SongObject> call(ListView<SongObject> arg0) {
					ListCell<SongObject> cell = new ListCell<SongObject>(){

					@Override
					protected void updateItem(SongObject song, boolean bool) {
						super.updateItem(song, bool);
						if(song != null) {
							setText(song.getSong());
						}
						else
							setText("");
					}

				};
				return cell;
			}
		 });

		 try {
			 loadList();
		 } catch (FileNotFoundException e) {
			 e.printStackTrace();
		 }

		 songList.setItems(obsList);

		 buttonHandling();
		 songDetailDisplay();
		 saveListListener(mainstage);

		 //Selects first entry in list
		 songList.getSelectionModel().select(0);
	 }


	 private void buttonHandling() {

		 if (obsList.size() == 0) {
			 editSong.setDisable(true);
			 deleteSong.setDisable(true);
		 }

	//Add new Song to ObservableArrayList. Show alert if song or artist are not filled out.
	 newSong.setOnAction(new EventHandler<ActionEvent>() {

		 @Override
		 public void handle(ActionEvent arg0) {

			 if (songEntry.getText().equals("") || artistEntry.getText().equals("")) {
				 Alert alert = new Alert(AlertType.INFORMATION);
				 alert.setTitle("Insufficient Data");
				 alert.setHeaderText("No Song/Artist Name");
				 alert.setContentText("You must enter both a Song Title and an Artist Title.");
				 alert.showAndWait();
				 songEntry.clear();
				 artistEntry.clear();
				 albumEntry.clear();
				 yearEntry.clear();
			 }
			 else {
				 confirmAction.setDisable(false);
				 cancelAction.setDisable(false);
				 newSong.setDisable(true);
				 editSong.setDisable(true);
				 deleteSong.setDisable(true);
				 songEntry.setDisable(true);
				 artistEntry.setDisable(true);
				 albumEntry.setDisable(true);
				 yearEntry.setDisable(true);
				 actionState = "ADD";
				 actionLabel.setText("Add:");
			 }
		 }

	 });


	 //Edit a currently selected song. Creates a new dialog box with edit fields.
	 editSong.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent arg0) {

			songToEdit = songList.getSelectionModel().getSelectedItem();

			songEntry.setText(songToEdit.getSong());
			artistEntry.setText(songToEdit.getArtist());
			albumEntry.setText(songToEdit.getAlbum());
			yearEntry.setText(songToEdit.getYear());

			 confirmAction.setDisable(false);
			 cancelAction.setDisable(false);
			 newSong.setDisable(true);
			 editSong.setDisable(true);
			 deleteSong.setDisable(true);
			 songList.setDisable(true);
			 actionState = "EDIT";
			 actionLabel.setText("Edit:");
		}

	 });


	 //Deletes the selected song from the list.
	 deleteSong.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent arg0) {

			songEntry.setDisable(true);
			 artistEntry.setDisable(true);
			 albumEntry.setDisable(true);
			 yearEntry.setDisable(true);
			 confirmAction.setDisable(false);
			 cancelAction.setDisable(false);
			 newSong.setDisable(true);
			 editSong.setDisable(true);
			 deleteSong.setDisable(true);
			 songList.setDisable(true);
			 actionState = "DELETE";
			 actionLabel.setText("Delete:");
		}

	 });


	 //Confirm button: case statement handles different logic depending on what mode application is in
	 confirmAction.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent arg0) {
			switch(actionState) {
			case "ADD":
				 String song = songEntry.getText();
				 String artist = artistEntry.getText();
				 String album = albumEntry.getText();
				 String year = yearEntry.getText();

				 if(album.equals(""))
					 album = "N/A";
				 if(year.equals(""))
					 year = "N/A";
				 SongObject songObject = new SongObject(song, artist, album, year);
				 insertSorted(songObject);
				 songList.setItems(obsList);
				 songList.getSelectionModel().select(songObject);
				 if(obsList.size() >= 1) {
					 editSong.setDisable(false);
					 deleteSong.setDisable(false);
				 }
				 newSong.setDisable(false);
				 confirmAction.setDisable(true);
				 cancelAction.setDisable(true);
				 songEntry.setDisable(false);
				 artistEntry.setDisable(false);
				 albumEntry.setDisable(false);
				 yearEntry.setDisable(false);

				 songEntry.clear();
				 artistEntry.clear();
				 albumEntry.clear();
				 yearEntry.clear();
				 actionLabel.setText("");
				 break;
			case "EDIT":
				 if (songEntry.getText().equals("") || artistEntry.getText().equals("")) {
					 Alert alert = new Alert(AlertType.INFORMATION);
					 alert.setTitle("Insufficient Data");
					 alert.setHeaderText("No Song/Artist Name");
					 alert.setContentText("You must enter both a Song Title and an Artist Title.");
					 alert.showAndWait();
					songEntry.setText(songToEdit.getSong());
					artistEntry.setText(songToEdit.getArtist());
					albumEntry.setText(songToEdit.getAlbum());
					yearEntry.setText(songToEdit.getYear());
					 return;
				 }
				 song = songEntry.getText();
				 artist = artistEntry.getText();
				 album = albumEntry.getText();
				 year = yearEntry.getText();

				 if(album.equals(""))
					 album = "N/A";
				 if(year.equals(""))
					 year = "N/A";

				 songObject = new SongObject(song, artist, album, year);

				 if (insertSorted(songObject)) {
						obsList.remove(songToEdit);
						songList.getSelectionModel().select(songObject);
				 }

				 newSong.setDisable(false);
				 editSong.setDisable(false);
				 deleteSong.setDisable(false);
				 confirmAction.setDisable(true);
				 cancelAction.setDisable(true);
				 songList.setDisable(false);
				 songEntry.setDisable(false);
				 artistEntry.setDisable(false);
				 albumEntry.setDisable(false);
				 yearEntry.setDisable(false);

				 songEntry.clear();
				 artistEntry.clear();
				 albumEntry.clear();
				 yearEntry.clear();
				 actionLabel.setText("");
				 break;
			case "DELETE":

				int songToDelete = songList.getSelectionModel().getSelectedIndex();

				if (obsList.size() > 0) {
					obsList.remove(songToDelete);
					songList.setItems(obsList);
					songList.getSelectionModel().select(songToDelete);
					 confirmAction.setDisable(true);
					 cancelAction.setDisable(true);
					 newSong.setDisable(false);
					 editSong.setDisable(false);
					 deleteSong.setDisable(false);
					 songList.setDisable(false);
						songEntry.setDisable(false);
						 artistEntry.setDisable(false);
						 albumEntry.setDisable(false);
						 yearEntry.setDisable(false);
					 actionLabel.setText("");
					if (obsList.size() == 0) {
						editSong.setDisable(true);
						deleteSong.setDisable(true);
					}
				}
				break;

			}

		}

	 });


	 //If action is cancelled, makes all UI elements available again and resets application state
	 cancelAction.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			 newSong.setDisable(false);
			 editSong.setDisable(false);
			 deleteSong.setDisable(false);
			 confirmAction.setDisable(true);
			 cancelAction.setDisable(true);
			 songList.setDisable(false);
			 songEntry.setDisable(false);
			 artistEntry.setDisable(false);
			 albumEntry.setDisable(false);
			 yearEntry.setDisable(false);

			 if (obsList.size() == 0) {
				 editSong.setDisable(true);
				 deleteSong.setDisable(true);
			 }

			 songEntry.clear();
			 artistEntry.clear();
			 albumEntry.clear();
			 yearEntry.clear();
			 actionLabel.setText("");

		}

	 });

	 }

private boolean insertSorted(SongObject song) {

	for (int i = 0; i < obsList.size(); i++) {

		//Checks if there the song to be inserted has the same song name as a pre-existing entry
		if (song.getSong().toLowerCase().compareTo(obsList.get(i).getSong().toLowerCase()) == 0) {

			//If entry to be added has the same song AND artist, considers it a duplicate and disallows entry
			if (song.getArtist().toLowerCase().compareTo(obsList.get(i).getArtist().toLowerCase()) == 0) {

				if(actionState.equals("EDIT") && obsList.get(i).equals(songToEdit)) {
					obsList.add(i, song);
					 songEntry.clear();
					 artistEntry.clear();
					 albumEntry.clear();
					 yearEntry.clear();
					 return true;
				}
				else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Duplicate Song");
				alert.setHeaderText("Song Already Exists");
				alert.setContentText("You cannot enter a song that already exists in the list.");
				alert.showAndWait();
				songEntry.clear();
				artistEntry.clear();
				albumEntry.clear();
				yearEntry.clear();
				return false;
				}
			}

		}

		//Inserts a unique entry into place in list where it belongs
		else if (song.getSong().toLowerCase().compareTo(obsList.get(i).getSong().toLowerCase()) < 0) {
			obsList.add(i, song);
			 songEntry.clear();
			 artistEntry.clear();
			 albumEntry.clear();
			 yearEntry.clear();
			 return true;
		}
	}

	//If list is exhausted, inserts new entry at end of the list
	obsList.add(song);
	 songEntry.clear();
	 artistEntry.clear();
	 albumEntry.clear();
	 yearEntry.clear();
	 return true;

}

private void songDetailDisplay () {

	songList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

		if(newValue != null) {
		songText.setText("Song Name: " + newValue.getSong());
		artistText.setText("Artist Name: " + newValue.getArtist());
		albumText.setText("Album Name: " + newValue.getAlbum());
		yearText.setText("Year: " + newValue.getYear());
		}
		else
		{
			songText.setText("Song Name: N/A");
			artistText.setText("Artist Name: N/A");
			albumText.setText("Album Name: N/A");
			yearText.setText("Year: N/A");
		}

	});

}

private void loadList() throws FileNotFoundException {

	File file = new File("songlist.txt");
	if(!file.exists())
		return;

	Scanner scanner = new Scanner(file);
	String line;
	String song;
	String artist;
	String album;
	String year;

	while(scanner.hasNextLine()) {
		line = scanner.nextLine();

		StringTokenizer tok = new StringTokenizer(line, ",");
		while(tok.hasMoreTokens()) {
			song = tok.nextToken();
			artist = tok.nextToken();
			album = tok.nextToken();
			year = tok.nextToken();
			obsList.add(new SongObject(song, artist, album, year));
		}

	}

	scanner.close();


}

private void saveListListener(Stage primaryStage) {

	primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

		@Override
		public void handle(WindowEvent arg0) {

			try {

				PrintWriter printWriter = new PrintWriter("songlist.txt");
				BufferedWriter bufferedWriter = new BufferedWriter(printWriter);

				for (int i = 0; i < obsList.size(); i++) {
					SongObject songObject = obsList.get(i);
					String toFile = songObject.getSong() + "," + songObject.getArtist() + "," + songObject.getAlbum() + "," + songObject.getYear();
					bufferedWriter.write(toFile);
					bufferedWriter.newLine();
				}

				bufferedWriter.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	});

}


}
