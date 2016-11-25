package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/** This class is a data structure representing an
 * individual Album.
 * @author Derek Schatel
 *
 */
public class Album implements Serializable {

	private String albumName;
	private List<Photo> photos;
	private Calendar dateCreated;
	private Photo oldestPhoto;
	private Photo newestPhoto;

	/** Constructor. Creates an Album object with the specified name.
	 * @param name the name of the album
	 */
	public Album (String name) {
		this.dateCreated = Calendar.getInstance();
		this.dateCreated.set(Calendar.MILLISECOND, 0);
		this.albumName = name;
		this.photos = new ArrayList<Photo>();
		this.oldestPhoto = null;
		this.newestPhoto = null;

	}

	/** Retrieves the date of the oldest photo
	 * @return the date of the oldest photo
	 */
	public String getOldestPhoto() {
		if (oldestPhoto == null)
			return "";
		else
			return oldestPhoto.getDate();
	}

	/** Retrieves a string showing the date range for the album
	 * @return the date range
	 */
	public String getDateRange() {
		if (oldestPhoto == null)
			return "";
		else
			return oldestPhoto.getShortDate() + " - " + newestPhoto.getShortDate();
	}

	/**
	 * Finds the oldest photo in the album and sets the class
	 * field to that photo.
	 */
	private void findOldestPhoto() {

		if (photos.size() == 0)
			return;

		Photo tempPhoto = photos.get(0);

		for (Photo p: photos)
			if(p.getCalendar().compareTo(tempPhoto.getCalendar()) < 0)
				tempPhoto = p;

		oldestPhoto = tempPhoto;


	}

	/**
	 * Searches for the newest photo in the album by date
	 * and sets the class field to that photo.
	 */
	private void findNewestPhoto() {

		if (photos.size() == 0)
			return;

		Photo tempPhoto = photos.get(0);

		for (Photo p: photos)
			if(p.getCalendar().compareTo(tempPhoto.getCalendar()) > 0)
				tempPhoto = p;

		newestPhoto = tempPhoto;

	}

	/** Retrieves the name of the album
	 * @return string of the album name
	 */
	public String getAlbumName() {
		return albumName;
	}

	/** Sets the album name to the specified parameter
	 * @param name the new album name
	 */
	public void setAlbumName(String name) {
		this.albumName = name;
	}

	/** Creates a readable version of the Album's calendar object
	 * @return a string representing date album was created
	 */
	public String getCreatedDate() {
		String[] str = dateCreated.getTime().toString().split("\\s+");
		return str[0] + " " + str[1] + " " + str[2] + ", " + str[5] + " " + str[3] + " " + str[4];
	}

	/** Retrieves the number of photos in the album
	 * @return the number of photos
	 */
	public int getNumPhotos () {
		return photos.size();
	}

	/** Adds the specified photo to the album list, then
	 * re-checks the oldest and newest photo values.
	 * @param photo the photo to be added
	 */
	public void addPhoto(Photo photo) {
		photos.add(photo);
		findOldestPhoto();
		findNewestPhoto();
	}

	/** Removes the specified photo from the album list, then
	 * re-checks the oldest and newest photo values.
	 * @param index index of the photo to be removed
	 */
	public void removePhoto(int index) {
		photos.remove(index);
		findOldestPhoto();
		findNewestPhoto();
	}

	/** Retrieves the specified photo from the album list.
	 * @param index index of the photo
	 * @return Photo at specified index
	 */
	public Photo getPhoto (int index) {
		return photos.get(index);
	}

	/** Retrieves the full list of photos in the album
	 * @return the list of photos in the album
	 */
	public List<Photo> getPhotoList() {
		return photos;
	}

	/** Retrieves the index number of the photo being sought.
	 * @param photo The photo requested
	 * @return Index number of that photo in the photo list
	 */
	public int getPhotoIndex(Photo photo) {
		for (int i = 0; i < photos.size(); i++)
			if (photos.get(i).equals(photo))
				return i;
		return -1;
	}



}
