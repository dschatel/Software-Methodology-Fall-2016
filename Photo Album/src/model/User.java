package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/** This class is a data structure representing
 * a single user in the userlist.
 * @author Derek Schatel
 *
 */
/**
 * @author Falcon24
 *
 */
public class User implements Serializable {

	private String username;
	private String name;
	private String password;
	private Calendar createdDate;
	private List<Album> albums;


	/** Constructor. Creates a new user with the specified attributes.
	 * @param user username of the User
	 * @param pass password of the User
	 * @param name full name of the User
	 */
	public User(String user, String pass, String name) {
		this.username = user;
		this.name = name;
		this.password = pass;
		this.albums = new ArrayList<Album>();
		this.createdDate = Calendar.getInstance();
		this.createdDate.set(Calendar.MILLISECOND, 0);
	}

	/** Gets the username
	 * @return User object's username
	 */
	public String getUser() {
		return username;
	}

	/** Gets the password
	 * @return User object's password
	 */
	public String getPassword() {
		return password;
	}

	/** Sets password to a new value
	 * @param pass new password value
	 */
	public void setPassword(String pass) {
		this.password = pass;
	}

	/** Gets the list of albums
	 * @return List of albums the user owns
	 */
	public List<Album> getAlbums() {
		return albums;
	}

	/** Adds an album to the user's account
	 * @param album Album object to be added
	 */
	public void addAlbum(Album album) {
		albums.add(album);
	}

	/** Adds an album to the user's account based on specified name
	 * @param name Name of new album
	 */
	public void addAlbum(String name) {
		albums.add(new Album(name));
	}

	/** Gets a readable version of the user's account creation date
	 * @return Full readable string of creation date
	 */
	public String getCreatedDate() {
		String[] str = createdDate.getTime().toString().split("\\s+");
		return str[0] + " " + str[1] + " " + str[2] + ", " + str[5] + " " + str[3] + " " + str[4];
	}

	/** Deletes specified album object from userlist
	 * @param album album object to be deleted
	 */
	public void deleteAlbum(Album album) {
		albums.remove(album);
	}

	/** Gets number of albums in user's account
	 * @return Number of album objects
	 */
	public int getTotalAlbums() {
		return albums.size();
	}

	/** Gets total number of photos in user's account
	 * @return Number of photo objects
	 */
	public int getTotalPhotos() {
		int total = 0;
		for (int i = 0; i < albums.size(); i++) {
			total+= albums.get(i).getNumPhotos();
		}

		return total;
	}


	/** Gets full name of user
	 * @return user's full name
	 */
	public String getName() {
		return name;
	}

	/** Checks if an album by that name already exists
	 * @param albumName the album name to check
	 * @return whether album name already exists
	 */
	public boolean doesAlbumExist(String albumName) {
		for (Album a: albums) {
			if (a.getAlbumName().equals(albumName))
				return true;
		}
		return false;
	}


}
