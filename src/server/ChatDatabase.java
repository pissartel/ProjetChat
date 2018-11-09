package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import shared.Topic;

public class ChatDatabase {

	private File file;

	/**
	 * Constructor
	 * @param fileName the name of the file used to store the data
	 */
	public ChatDatabase(String fileName) {
		this.file = new File(fileName);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Topic> loadTopics() throws IOException, ClassNotFoundException {
		ArrayList<Topic> data = new ArrayList<Topic>();

		// This checks if the file actually exists
		if(this.file.exists() && !this.file.isDirectory()) { 
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			data=(ArrayList<Topic>)ois.readObject(); // cast en Arraylist de topics
			
		} else {
			System.out.println("Le fichier de sauvegarde des topics chat n'existe pas.");
			this.file.createNewFile();
		}
	
	//	System.out.println(data.toString());
		return data;
	}

	public void saveTopics(ArrayList<Topic> data) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(data);
		oos.close();
		fos.close();
	}
	
	public void clearTopics() throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(null);
		oos.close();
		fos.close();

	}
}


