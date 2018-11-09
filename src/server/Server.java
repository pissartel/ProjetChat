package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import shared.NewMessageRequest;
import shared.Notification;

/* 
 * thread qui permet de gérer l'attente de client, 
 * et lorsque celui-ci est connecté lance le thread de gestion deu client
 */	
public class Server implements Runnable {
	public static final int SERVER_PORT = 3000;
	private ServerSocket server;
	private Thread t1;
	private ArrayList<ClientHandler> clientList;

	// constructeur 
	public Server( ) throws IOException {
		// TODO Auto-generated constructor stub
		this.clientList = new ArrayList<ClientHandler>();
		this.server = new  ServerSocket(SERVER_PORT);
		System.out.println("Serveur démarré sur le port " + SERVER_PORT);
		System.out.println("En attente de connexion...");
	}

	public void run(){

		Socket client;
		try {
			boolean running = true;
			while (running) {
				System.out.println("En attente de accept...");
				client = this.server.accept();
				System.out.println("accepté");

				//On créer un nouveau CLientHandler
				ClientHandler ch = new ClientHandler(client, this);
				// On l'ajoute à la liste des ClientHandler
				clientList.add(ch);
				t1 = new Thread(ch);

				System.out.println("on démarre client handler");
				t1.start();	

				System.out.println(clientList);
			}


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	public void remove(ClientHandler clientHandler){
		this.clientList.remove(clientHandler);

	}

	public void sendNotification(NewMessageRequest nmreq) {
		//Envoie d'une notificaiton du nouveau message à tout les clients connectés
		System.out.println(this.clientList);

		this.clientList.forEach(x->{
			try {				
				x.getOut().writeObject(new Notification(nmreq.getTopic(), nmreq.getMessage()) );
				x.getOut().flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
}
