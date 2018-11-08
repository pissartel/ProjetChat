package server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.crypto.Data;

import shared.*;

/*
	thread qui communique avec le client
 */
public class ClientHandler implements Runnable {
	private Socket client;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private UserDatabase userDatabase;
	private ForumDatabase forumDatabase;
	private User userClient;
	private Server server;

	public static final String UDB_FILE_NAME ="userdatabase.db";	
	public static final String FDB_FILE_NAME ="forumdatabase.db";	

	public boolean isconnected = true;
	public boolean islogged = false;

	//public ArrayList<Topic> forum;
	public ClientHandler(Socket client, Server server) {
		this.client = client;
		this.userDatabase=new UserDatabase(UDB_FILE_NAME);
		this.forumDatabase=new ForumDatabase(FDB_FILE_NAME);
		this.server=server;
	}
	


	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {		

			out = new ObjectOutputStream(this.client.getOutputStream());
			in = new ObjectInputStream(this.client.getInputStream());

			while (this.isconnected) {

				ArrayList<User> userList = userDatabase.loadData();
				ArrayList<Topic> topicList = forumDatabase.loadTopics();

				Request request;
				request=(Request) in.readObject();
				
				
				// MAJ listes client et topics
				userList = userDatabase.loadData();
				topicList = forumDatabase.loadTopics();

				//System.out.println(nb_tour);

				try {	
					switch(request.getClass().getSimpleName()) {
					// Authentification du client : on récupere les identifiants et on compare avec
					// 								la database
					case "AuthentificationRequest":
						System.out.println("on rentre dans authentification");

						AuthentificationRequest areq = (AuthentificationRequest) request;
						User user = areq.getUser(); 
						// On cherche si l'utilisateur existe et si son mdp est le bon
						islogged = userList.stream().filter(x-> x.getLogin()
								.equals(user.getLogin())).map(User::getPassword)
								.collect(Collectors.toList())
								.contains(user.getPassword());

						if (islogged) {
							System.out.println(userList.stream().filter(x-> x.getLogin()
									.equals(user.getLogin()))
									.collect(Collectors.toList()).get(0) );

							// on met à jour le user avec les données du serveurs 
							userClient = userList.stream().filter(x-> x.getLogin()
									.equals(user.getLogin()))
									.collect(Collectors.toList()).get(0);
							out.writeObject(new AuthentificationResponse(userClient, true));
						}
						else out.writeObject(new AuthentificationResponse(user, false));
						this.out.flush();
						System.out.println("authentification " + islogged);
						break ;


					case "AccountCreationRequest":

						AccountCreationRequest acreq = (AccountCreationRequest) request;
						// on vérifie d'abord si le login est déjà pris ou pas
						User userDemand = acreq.getUser(); 

						List<String> loginList = userList.stream().map(x -> x.getLogin())
								.collect(Collectors.toList());
						if (!loginList.contains(userDemand.getLogin())) {
							userList.add(userDemand);
							userDatabase.saveData(userList);
							// On informe le client que son compte a été créé
							out.writeObject(new AccountCreationResponse(true));
							this.out.flush();
							System.out.println("On vient de créer un nouveau compte.");
						}
						else {	// On informe le client que le login est déjà pris
							out.writeObject(new AccountCreationResponse(false));
							this.out.flush();
						}
						System.out.println("connexion " + isconnected);
						break ;
						
					case "LoadForumRequest":
						if (islogged){
							LoadForumRequest lfreq = (LoadForumRequest) request;
							out.writeObject(new LoadForumResponse(topicList));
							this.out.flush();
						}
						break ;
					case "NewTopicRequest":
						NewTopicRequest ntreq = (NewTopicRequest) request;

						if (islogged){
							//		ArrayList<Topic> topicList = forumDatabase.loadTopics();

							// on vérifie si le topic a pas deja été fait 
							if(!topicList.stream().map(x-> x.getTitle())
									.collect(Collectors.toList()).contains(ntreq.getTopic().getTitle())) {

								out.writeObject(new NewTopicResponse(ntreq.getTopic()));
								this.out.flush();
								//MAJ du forum
								topicList.add(ntreq.getTopic());
								forumDatabase.saveTopics(topicList);
							}
							else 	out.writeObject(new NewTopicResponseFailure());

						}
						break ;

					case "DeleteTopicRequest":
						DeleteTopicRequest dtreq = (DeleteTopicRequest) request;
						if (islogged){
							if(dtreq.getUser().getLogin().equals("admin")) {

								// On supprime le topic choisi
								if (dtreq.getTopic().getTitle().equals("all")) topicList = (ArrayList<Topic>) topicList.stream()
										.filter( x -> !x.getTitle().equals(dtreq.getTopic().getTitle()))
										.collect(Collectors.toList());
								// On supprime tous les topics
								else topicList.clear();
							}

							out.writeObject(new DeleteTopicResponse(true));
							this.out.flush();
							//MAJ du forum
							forumDatabase.saveTopics(topicList);
						}
						break ;
					case "LoadTopicRequest":
						if (islogged){
							LoadTopicRequest ltreq = (LoadTopicRequest) request;
							try {
								Topic topic = topicList.get(ltreq.index);
								out.writeObject(new LoadTopicResponse(topic));
								this.out.flush();
							}
							catch(IOException e){
								System.out.println("topic inexistant");
							}
						}
						break ;
					case "NewMessageRequest":
						if (islogged){
							NewMessageRequest nmreq = (NewMessageRequest) request;	
							System.out.println("msg ");
							System.out.println(nmreq.getTopic().toStringMessages());

							
							topicList.set(topicList.stream().map(x -> x.getTitle())
									.collect(Collectors.toList()).indexOf(nmreq.getTopic().getTitle()), 
									nmreq.getTopic().addMessage(nmreq.getMessage()));
							//System.out.println(nmreq.getTopic().toStringMessages());
							
							System.out.println("msg sauvegardé");

							// MAJ du forum
							forumDatabase.saveTopics(topicList);
							System.out.println("topic maj");

							// Envoie de la notification du message
							this.server.sendNotification(nmreq); 
						}
						break ;
						
					case "CloseRequest":
						this.server.remove(this);
						this.isconnected = false;
						break ;
						//e.printStackTrace();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassCastException e) {
					// TODO Auto-generated catch block
					System.out.println("mauvaise request, on retourne dans la boucle");

				}

			}
		}catch ( IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("on est sorti de la boucle");

		} 

	}
	private synchronized  Response readResponse() throws ClassNotFoundException, IOException {
		/*
		 * TODO
		 * Here you should read the server response from the input stream, then print it.
		 * Note: the server only answers with String ;)
		 */
		return (Response) in.readObject() ;

	}


	
	public ObjectOutputStream getOut() {
		return this.out;
	}

	
}