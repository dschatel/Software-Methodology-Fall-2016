package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/** This class is a data structure designed to hold the full list
 * of all users in the program. Implements Serializable.
 * @author Derek Schatel
 *
 */
public class AdminList implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = -7878161794921060077L;
	private List<User> users;

	/**
	 * Constructor. Creates an arraylist of User objects.
	 */
	public AdminList() {
		this.users = new ArrayList<User>();
	}

	/** Retrieves the list of users
	 * @return the List of User objects
	 */
	public List<User> getAdminList() {
		return users;
	}

	/** Adds a user object to the list
	 * @param user the user object to be added
	 */
	public void addUser(User user) {
		users.add(user);
	}

	/** Deletes a user object from the list
	 * @param user the user to be deleted
	 */
	public void deleteUser(User user) {
		users.remove(user);
	}

	/** Checks to see if a user already exists in the system.
	 * @param user the username to be searched
	 * @param pass the password to be searched
	 * @return if the user already exists in the system
	 */
	public boolean doesUserExist(String user, String pass) {
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).getUser().equals(user) && users.get(i).getPassword().equals(pass))
				return true;
		}
		return false;
	}

	/** Retrieves a specified username
	 * @param user the username
	 * @return the user Object going by that name
	 */
	public User getUser (String user) {
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).getUser().equals(user))
				return users.get(i);
		}
		return null;
	}

	/** Saves the serialized userlist to a .ser file contained in the data directory
	 * @param list The AdminList to be saved
	 * @throws IOException
	 */
	public static void saveToFile (AdminList list) throws IOException {
		FileOutputStream fileOut = new FileOutputStream("data/users.ser");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(list);
		out.close();
		fileOut.close();
	}

	/** Reads a .ser file and creates an AdminList from the serialized data
	 * @return the serialized AdminList
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static AdminList readFromFile() throws IOException, ClassNotFoundException {
			FileInputStream fileIn = new FileInputStream("data/users.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			AdminList userList = (AdminList) in.readObject();
			in.close();
			fileIn.close();
			return userList;
	}

}
