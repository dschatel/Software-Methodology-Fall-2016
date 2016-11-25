//Derek Schatel and Monica Ho

package application;

public class SongObject {

	private String name;
	private String artist;
	private String album;
	private String year;

	public SongObject(String name, String artist, String album, String year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}

	public String getSong() {
		return name;
	}

	public void setSong(String songName) {
		this.name = songName;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artistName) {
		this.artist = artistName;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String albumName) {
		this.album = albumName;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

}
