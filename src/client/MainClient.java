package client;

import server.Server;

public class MainClient {

	public static void main(String[] args) throws ResponseException {
		Client client1 = new Client();
	//	client1.authenticate("admin5", "admin4");
	//	client1.createAccount("admin5", "admin");

		client1.authenticate("admin", "admin");

		//client1.loadForum();
client1.newTopic("Règles du forum", "1ere regle du forum : il n'y a pas de regle sur le forum.");
//	client1.deleteTopic("Règles du forum");
	client1.loadForum();
	client1.loadTopic("Règles du forum");
	client1.newMessage("coucou");
	client1.loadTopic("Règles du forum");


		
		//Client client3 = new Client();
		//client3.createAccount("roger", "a23");
	}

}