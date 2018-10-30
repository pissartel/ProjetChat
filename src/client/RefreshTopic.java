package client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JScrollPane;

import server.ForumDatabase;
import server.UserDatabase;
import shared.*;

public class RefreshTopic implements Runnable  {
	private Socket socket;
//	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Topic topic;
	private Client client;
	private IHM ihm;




	//public ArrayList<Topic> forum;

	public RefreshTopic(ObjectInputStream in, Client client, IHM ihm, Topic topic) {
		this.socket = socket;
		this.client=client;
		this.ihm = ihm;
		this.topic=topic;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		Response rep = new Response();
		Notification notif= new Notification(topic, topic);
		System.out.println("on a run");
		StringBuilder builder = new StringBuilder();
		try {		

			while (true) {
				rep = client.readResponse();
				System.out.println(rep.getClass().getSimpleName());
				
				if (rep.getClass().getSimpleName().equals("Notification")){
					notif = (Notification) rep;
					if (notif.getTopic().getTitle().equals(this.topic.getTitle())) {
						System.out.println("notif reçue");

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


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
