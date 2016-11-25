package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/** This class is a data structure representing a single
 * Photo in an album.
 * @author Derek Schatel
 *
 */
public class Photo implements Serializable {

	private String caption;
	private List<Tags> tags;
	private SerialImage image;
	private Calendar dateUploaded;

	/**
	 * Private constructor. Creates a new photo object without
	 * adding the actual image to the photo.
	 */
	private Photo() {

		this.caption = "";
		this.tags = new ArrayList<Tags>();
		this.dateUploaded = Calendar.getInstance();
		this.dateUploaded.set(Calendar.MILLISECOND, 0);
		this.image = new SerialImage();


	}

	/** Public constructor. Creates an image object with
	 * the specified image object.
	 * @param image Image contained in the Photo
	 */
	public Photo (Image image) {
		this();
		this.image.setImage(image);

	}

	/** Retrieves the image from the photo
	 * @return the visual image object
	 */
	public Image getImage() {
		return image.getImage();
	}

	/** Retrieves a thumbnail version of the photo's image
	 * @return A smaller version of the image, encapsulated
	 * in an ImageView
	 */
	public ImageView getThumbNail() {
		final ImageView imageView = new ImageView(image.getImage());
		imageView.setFitHeight(50);
		imageView.setFitWidth(50);
		imageView.setPreserveRatio(true);

		return imageView;
	}

	/** Retrieves a tag at a specified index
	 * @param index index of the tag
	 * @return Tag object at that index
	 */
	public Tags getTag(int index) {
		return tags.get(index);
	}

	/** Adds a new tag to the tag list
	 * @param type tag type
	 * @param value tag value
	 */
	public void addTag(String type, String value) {
		tags.add(new Tags(type, value));
	}

	/** Removes a tag from the tag list of specified index
	 * @param index index of tag to be removed
	 */
	public void removeTag (int index) {
		tags.remove(index);
	}

	/** Edits a selected tag.
	 * @param index Index of tag to be edited
	 * @param type new tag type
	 * @param value new tag value
	 */
	public void editTag (int index, String type, String value) {
		tags.get(index).setType(type);
		tags.get(index).setValue(value);
	}

	/** Retrieves photo's caption
	 * @return caption
	 */
	public String getCaption () {
		return caption;
	}

	/** Sets photo's caption
	 * @param caption the caption to be set
	 */
	public void setCaption (String caption) {
		this.caption = caption;
	}

	/** Retrieves a readable version of the photo's upload date
	 * @return the photo's full upload date, including HH:MM:SS
	 */
	public String getDate() {
		String[] str = dateUploaded.getTime().toString().split("\\s+");
		return str[0] + " " + str[1] + " " + str[2] + ", " + str[5] + " " + str[3] + " " + str[4];
	}

	/** Retrieves a readable shortened version of the photo's upload date
	 * @return the photo's shortened upload date, only month/day/year
	 */
	public String getShortDate() {
		String[] str = dateUploaded.getTime().toString().split("\\s+");
		return str[1] + " " + str[2] + ", " + str[5];

	}

	/** Retrieves the photo's calendar object
	 * @return the Calendar object representing date uploaded
	 */
	public Calendar getCalendar() {
		return dateUploaded;
	}


	/** Retrieves the list of photo tags
	 * @return the list of photo tags
	 */
	public List<Tags> getTags () {
		return tags;
	}

	/** Retrieves the Serializable version of the photo's image
	 * @return a SerialImage version of the image
	 */
	public SerialImage getSerialImage() {
		return image;
	}

}
