package client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import server.User;
import shared.*;

public class Client {
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private User user;
	private Topic currentTopic;
	private  ServerHandler  serverHandler ;

	public Client(){
		try {
			this.socket= new Socket((String) null, 3000);
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
			this.serverHandler = new  ServerHandler(this);
			Thread threadRH = new Thread(this.serverHandler );
			threadRH.start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public  boolean  authenticate(String login, String password) throws ResponseException{
		try {

			Response rep = this.readResponse(new AuthentificationRequest(login,password));

			// si l'authentification est bonne on retourne true sinon false
			if (rep.getClass().getSimpleName().equals("AuthentificationResponse")) {
				if (((AuthentificationResponse)rep).isAuthentified()) {
					System.out.println("Vous êtes connecté :)");

					this.user=((AuthentificationResponse)rep).getUser();
					System.out.println(this.user.toString());

					return true;
				}
				else {
					System.out.println("Mauvais identifiant et/ou mot de passe :(");
					return false ;
				}
			}
			else throw new ResponseException();

		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public  boolean createAccount(String login, String password) throws ResponseException{
		try {
			Response rep = this.readResponse(new AccountCreationRequest(login,password));

			if (rep.getClass().getSimpleName().equals("AccountCreationResponse")) {
				if (((AccountCreationResponse)rep).isCreated()) {
					System.out.println("Compte créé. Vous pouvez vous connecter maintenant :)");
					return true; 
				}
				else {
					System.out.println("Ce login est déjà utilisé :(");
					return false ; 		
				}

			}
			else throw new ResponseException();


		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}



	public  ArrayList<Topic>  loadForum() throws ResponseException{

		try {

			Response rep=this.readResponse(new LoadForumRequest());

			if (rep.getClass().getSimpleName().equals("LoadForumResponse")) {
				ArrayList<Topic> listTopic=((LoadForumResponse) rep).topicList ;
				if ( !listTopic.isEmpty()) {
					System.out.println("---------------------------------- FORUM ----------------------------------");
					listTopic.forEach(x->System.out.println("\n" + x.getTitle() + "\n par :" + x.getAuthor() + "\n"+"---------------------------------- \n"));
				}
				else System.out.println("Forum vide");
				return listTopic;
			}
			else {
				System.out.println(rep.getClass().getSimpleName());
				throw new ResponseException();
			}
		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public  Topic  newTopic( String Title, String content) throws ResponseException{
		try {
			Topic newTopic = new Topic(this.user, Title, content);
			Response rep = this.readResponse( new NewTopicRequest(newTopic));

			if (rep.getClass().getSimpleName().equals("NewTopicResponse")) {
				Topic newTopicCreated=((NewTopicResponse) rep).getTopic() ;
				if ( !newTopicCreated.equals(newTopic)) System.out.println(newTopicCreated.toString());
				else System.out.println("le topic n'a pas pu être créé :(");
				return newTopicCreated;
			}
			else if(rep.getClass().getSimpleName().equals("NewTopicResponseFailure")) {
				System.out.println("Vous avez dejà créé ce topic ! ");
				return null;
			}
			else throw new ResponseException();

		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}


	public  boolean deleteTopic(String topicTitle) throws ResponseException{
		try {
			Response rep = this.readResponse(new DeleteTopicRequest(this.user, "topicTitle"));

			if (rep.getClass().getSimpleName().equals("DeleteTopicResponse")) {
				if (((DeleteTopicResponse)rep).isDelete) return true; 
				else return false ; 			
			}
			else return false;

		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}


	public  Topic  loadTopic(int index) throws ResponseException{


		try {
			Response rep = this.readResponse(new LoadTopicRequest(index));

			if (rep.getClass().getSimpleName().equals("LoadTopicResponse")) {
				this.currentTopic=((LoadTopicResponse) rep).getTopic() ;	
				System.out.println(currentTopic.toString());				
				return currentTopic;
			}	
			else System.out.println("Topic vide");
			return null;

		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Message newMessage(String Content ) throws ResponseException{
		Message newMessage = new Message(this.user, Content);
		NewMessageRequest req = new NewMessageRequest(newMessage, currentTopic);
		try {
			this.out.writeObject(req);
			this.out.flush();
			return newMessage;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public Response readResponse(Request req) throws ClassNotFoundException, IOException, InterruptedException {
		synchronized(this.in) {
			//send the req immediatelly
			this.out.writeObject(req);    
			this.out.flush();  
			//wait for response
			in.wait(); 
			System.out.println("on fini d'attendre");
		}      
		return this.serverHandler.getResponse();
	}

	public ObjectInputStream getOIS() {
		return this.in;
	}


	public Socket getSocket() {
		return this.socket;
	}

	public  ServerHandler getServerHandler() {
		return  serverHandler;
	}

	



}