package model;

import java.io.Serializable;

/** This class is a data structure representing a
 * single tag on a photo.
 * @author Derek Schatel
 *
 */
public class Tags implements Serializable {

	private String type;
	private String value;

	/** Constructor. Creates a new tag with the specified attributes.
	 * @param type tag type
	 * @param value tag value
	 */
	public Tags(String type, String value) {
		this.type = type;
		this.value = value;
	}

	/** Gets the tag type
	 * @return tag type
	 */
	public String getType(){
		return type;
	}

	/** Gets the tag value
	 * @return tag value
	 */
	public String getValue() {
		return value;
	}

	/** Sets tag type to specified parameter
	 * @param type new tag type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/** Sets tag value to specified parameter
	 * @param value new tag value
	 */
	public void setValue (String value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return type + ": " + value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(obj==null || !(obj instanceof Tags))
			   return false;

		Tags t =(Tags ) obj;

        return t.getValue().equals(value) && t.getType().equals(type);
	}



}
