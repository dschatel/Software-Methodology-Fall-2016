package model;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/** This class is a data structure meant to encapsulate
 * information about an image file. Since Images cannot by
 * default be serialized, this class converts an Image into
 * serializable data.
 *
 * @author Derek Schatel
 *
 */
public class SerialImage implements Serializable {

	private int height;
	private int width;
	private int[][] image;

    /**
     * Default constructor that does nothing.
     */
    public SerialImage() {}

    /** Serialized an image by converting it into int values
     * for its height and width, as well as reading RGB values
     * of individual pixels and creating a 2D array of those values.
     * @param image the image to be serialized
     */
    public void setImage(Image image) {
        width = ((int) image.getWidth());
        height = ((int) image.getHeight());
        this.image = new int[width][height];

        PixelReader r = image.getPixelReader();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.image[i][j] = r.getArgb(i, j);
            }
        }

    }

    /** Turns the Serialized image back into an Image object
     * @return the Image object represented in the Serialized data
     */
    public Image getImage() {
        WritableImage img = new WritableImage(width, height);

        PixelWriter w = img.getPixelWriter();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                w.setArgb(i, j, image[i][j]);
            }
        }

        return img;
    }

	/** Gets the width of the Serialized image
	 * @return width value
	 */
	public int getWidth() {
		return width;
	}

	/** Gets the height of the Serialized image
	 * @return height value
	 */
	public int getHeight() {
		return height;
	}

	/** Gets the 2D array of RGB values
	 * @return 2D array of pixels
	 */
	public int[][] getPixels() {
		return image;
	}

    /** Equals method that compares two Serialized images. Used to avoid
     * image duplication in the Serialized data upon adding a new image.
     * @param serialImage the image to be compared
     * @return whether the two images are the same image
     */
    public boolean equals(SerialImage serialImage) {
		if (width != serialImage.getWidth())
			return false;
		if (height != serialImage.getHeight())
			return false;
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				if (image[i][j] != serialImage.getPixels()[i][j])
					return false;
		return true;
    }



}
