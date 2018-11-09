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
	private Thread threadRH;

	public Client(){
		try {
			this.socket= new Socket((String) null, 3000);
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
			this.serverHandler = new  ServerHandler(this);
			this.threadRH = new Thread(this.serverHandler );
			this.threadRH.start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean authenticate(String login, String password) throws ResponseException{
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

	public  synchronized ArrayList<Topic>  loadChat() throws ResponseException{

		try {

			Response rep=this.readResponse(new LoadChatRequest());

			if (rep.getClass().getSimpleName().equals("LoadChatResponse")) {
				ArrayList<Topic> listTopic=((LoadChatResponse) rep).topicList ;
				if ( !listTopic.isEmpty()) {
					System.out.println("---------------------------------- Chat ----------------------------------");
					listTopic.forEach(x->System.out.println("\n" + x.getTitle() + "\n par :" + x.getAuthor() + "\n"+"---------------------------------- \n"));
				}
				else System.out.println("Chat vide");
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

	public   Topic  newTopic( String Title, String content) throws ResponseException{
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


	public synchronized Topic  loadTopic(int index) throws ResponseException{


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
		try {
			Response rep = readResponse(new NewMessageRequest(newMessage, currentTopic));
			if (rep.getClass().getSimpleName().equals("NewMessageResponse")) {
				if (((NewMessageResponse) rep).isSent()){
					return newMessage;
				}
				else return null;

			}
			else return null;

		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public synchronized boolean closeClient() {
		try {
			System.out.println("demande de quitte");
			readResponse(new CloseRequest());   
			this.out.close();
			this.in.close();
			socket.close();
			this.threadRH.interrupt();
			return true;

		} catch (IOException | ClassNotFoundException | InterruptedException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}    
	}


	public Response readResponse(Request req) throws ClassNotFoundException, IOException, InterruptedException {
		synchronized(this.in) {
			//send the req immediatelly
			this.out.writeObject(req);    
			this.out.flush();  
			//wait for response
			in.wait(); 
		}      
		return this.serverHandler.getResponse();
	}


	public ObjectInputStream getOIS() {
		return this.in;
	}
	public ObjectOutputStream getOOS() {
		return this.out;
	}


	public Socket getSocket() {
		return this.socket;
	}

	public  ServerHandler getServerHandler() {
		return  serverHandler;
	}


}