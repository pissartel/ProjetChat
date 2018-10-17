package server;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * This class represents the server database.
 * In this project, it will simply provides an API for the server to interact with the file system.
 * @author strift
 *
 */

public class UserDatabase {

	/**
	 * The name of the file used to store the data
	 */
	private File file;

	/**
	 * Constructor
	 * @param fileName the name of the file used to store the data
	 */
	public UserDatabase(String fileName) {
		this.file = new File(fileName);
	}

	/**
	 * Load the list of Pokemons stored inside the file and returns it
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<User> loadData() throws IOException, ClassNotFoundException {
		ArrayList<User> data = new ArrayList<User>();

		// This checks if the file actually exists
		if(this.file.exists() && !this.file.isDirectory()) { 
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			data=(ArrayList<User>)ois.readObject(); // 
			
		} else {
			System.out.println("Le fichier de sauvegarde n'existe pas.");
			this.file.createNewFile();
		}

		System.out.println("Base de donnée chargée.");
	
	//	System.out.println(data.toString());
		return data;
	}

	/**
	 * Save the list of Pokémons received in parameters
	 * @param data the list of Pokémons
	 * @throws IOException 
	 */
	public void saveData(ArrayList<User> data) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(data);
		oos.close();
		fos.close();

		System.out.println("Sauvegarde effectuée.");
	}
	
	public void clearData() throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(null);
		oos.close();
		fos.close();

	}
}
