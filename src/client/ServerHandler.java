package client;

import java.awt.Point;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import server.ForumDatabase;
import server.UserDatabase;
import shared.*;

public class  ServerHandler implements Runnable  {
	private Socket socket;
	private ObjectInputStream in;
	private Topic topic;
	private IHM ihm;
	private Response rep;
	private Client client; 

	//public ArrayList<Topic> forum;

	public  ServerHandler(Client client) {
		this.client= client;
		this.in = client.getOIS();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Response rep = new Response();
		Notification notif= new Notification(topic, topic);
		System.out.println("on a run");
		StringBuilder builder = new StringBuilder();
		try {		
			while (!Thread.currentThread().isInterrupted()) {
				//rep = client.readResponse();
				System.out.println("attente de reponse ou notification");

				Object ios = in.readObject();
				//this.setResponse((Response) in.readObject()) ;
				System.out.println(ios.getClass());

				if (ios.getClass().getSimpleName().equals("Notification")){
					notif = (Notification) ios;
					System.out.println(notif.getClass());

					// Test dans le cas ou on est pas dans un topic
					if (!(this.getTopic()==null)) {
						if (notif.getTopic().getTitle().equals(this.topic.getTitle())) {
							System.out.println("notif reçue");

							// Test si on est dans le topic correspondant au msg reçu
							if (notif.getTopic().getAuthor().equals(this.topic.getAuthor())){
								System.out.println("on refresh");
								builder = new StringBuilder();
								builder.append(ihm.textPaneMessage.getText());
								builder.append(notif.getMessage().toString());
								ihm.textPaneMessage.setText(builder.toString());	
								//ihm.messagesPane.setViewportView(ihm.textPaneMessage);
								ihm.mainFrame.remove(ihm.messagesPane);
								JScrollPane messagesPane = new JScrollPane(ihm.textPaneMessage);
								messagesPane.setVerticalScrollBarPolicy(
								javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
								messagesPane.setBounds(50,170,750, 205);
								ihm.messagesPane = messagesPane;
								ihm.mainFrame.add(messagesPane);
							}
						}
					}

				}
				else {
					this.setResponse((Response) ios);
					synchronized(this.in){
						System.out.println("thread attend wait");
						in.notify();
						System.out.println("thread notify");
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}




	public Topic getTopic() {
		return topic;
	}



	public void setTopic(Topic topic) {
		this.topic = topic;
	}




	public IHM getIhm() {
		return ihm;
	}


	public void setIhm(IHM ihm) {
		this.ihm = ihm;
	}




	public Response getResponse() {
		return rep;
	}




	public void setResponse(Response rep) {
		this.rep = rep;
	}



}
