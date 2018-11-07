package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/* 
 * thread qui permet de gérer l'attente de client, 
 * et lorsque celui-ci est connecté lance le thread de gestion deu client
 */	
public class Server implements Runnable {
	public static final int SERVER_PORT = 3000;
	private ServerSocket server;
	public Thread t1;

	// constructeur 
	public Server( ) throws IOException {
		// TODO Auto-generated constructor stub

		this.server = new  ServerSocket(SERVER_PORT);
		System.out.println("Serveur démarré sur le port " + SERVER_PORT);
		System.out.println("En attente de connexion...");
	}


	public void run(){

		Socket client;
		try {
			boolean running = true;
			ArrayList<ClientHandler> ClientList = new ArrayList<ClientHandler>();
			while (running) {
				System.out.println("En attente de accept...");
				client = this.server.accept();
				System.out.println("accepté");
				
				//On créer un nouveau CLientHandler
				ClientHandler ch = new ClientHandler(client);
				// On l'ajoute à la liste des ClientHandler
				ClientList.add(ch);
				// MAJ dans chaque clienthandlers
				ClientList.forEach(x->x.setlistClientHandler(ClientList));
				// On lance le thread
				t1 = new Thread(ch);
				
				System.out.println("on démarre client handler");
				t1.start();	
			}


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
}
