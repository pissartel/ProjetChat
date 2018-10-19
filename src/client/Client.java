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

	public Client(){
		try {
			this.socket= new Socket((String) null, 3000);
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public  boolean  authenticate(String login, String password) throws ResponseException{
		try {
			AuthentificationRequest req = new AuthentificationRequest(login,password);
			this.out.writeObject(req);
			this.out.flush();

			Response rep=this.readResponse();
			//System.out.println("identification");

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

		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public  boolean createAccount(String login, String password) throws ResponseException{
		try {
			AccountCreationRequest req = new AccountCreationRequest(login,password);
			this.out.writeObject(req);
			this.out.flush();
			// on attend que le serveur retourne la confirmation de la creation du compte
			Response rep=this.readResponse();
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


		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}



	public  ArrayList<Topic>  loadForum() throws ResponseException{

		LoadForumRequest req = new LoadForumRequest();

		try {

			this.out.writeObject(req);
			this.out.flush();
			Response rep=this.readResponse();

			if (rep.getClass().getSimpleName().equals("LoadForumResponse")) {
				ArrayList<Topic> listTopic=((LoadForumResponse) rep).topicList ;
				if ( !listTopic.isEmpty()) {
					System.out.println("---------------------------------- FORUM ----------------------------------");
					listTopic.forEach(x->System.out.println("\n" + x.getTitle() + "\n par :" + x.getAuthor() + "\n"+"---------------------------------- \n"));
				}
				else System.out.println("Forum vide");
				return listTopic;
			}
			else throw new ResponseException();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public  Topic  newTopic( String Title, String content) throws ResponseException{
		try {
			Topic newTopic = new Topic(this.user, Title, content);
			NewTopicRequest req = new NewTopicRequest(newTopic);
			this.out.writeObject(req);
			this.out.flush();
			Response rep=this.readResponse();
			if (rep.getClass().getSimpleName().equals("NewTopicResponse")) {
				Topic newTopicCreated=((NewTopicResponse) rep).getTopic() ;
				if ( !newTopicCreated.equals(newTopic)) System.out.println(newTopicCreated.toString());
				else System.out.println("le topic n'a pas pu être créé :(");
				return newTopicCreated;
			}
			else throw new ResponseException();

		}catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}


	public  boolean deleteTopic(String topicTitle) throws ResponseException{
		try {
			DeleteTopicRequest req = new DeleteTopicRequest(this.user, "topicTitle");
			this.out.writeObject(req);
			this.out.flush();

			Response rep=this.readResponse();
			if (rep.getClass().getSimpleName().equals("DeleteTopicResponse")) {
				if (((DeleteTopicResponse)rep).isDelete) return true; 
				else return false ; 			
			}
			else return false;

		}catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}


	public  Topic  loadTopic(int index) throws ResponseException{

		LoadTopicRequest req = new LoadTopicRequest(index);

		try {

			this.out.writeObject(req);
			this.out.flush();
			Response rep=this.readResponse();

			if (rep.getClass().getSimpleName().equals("LoadTopicResponse")) {
				this.currentTopic=((LoadTopicResponse) rep).getTopic() ;	
				System.out.println(currentTopic.toString());				
				return currentTopic;
			}	
			else System.out.println("Topic vide");
			return null;

		} catch (IOException | ClassNotFoundException e) {
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
			Response rep=this.readResponse();

			if (rep.getClass().getSimpleName().equals("NewMessageResponse")) {
				// On refresh la page 
				//this.loadTopic(currentTopic.getTitle());

				return newMessage;
			}	
			else throw new ResponseException();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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


}
