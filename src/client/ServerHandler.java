package client;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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
		System.out.println("on a run");
		StringBuilder builder = new StringBuilder();
		try {		
			while (!Thread.currentThread().isInterrupted()) {
				//rep = client.readResponse();
				System.out.println("attente de reponse ou notification");

				Object ios = in.readObject();
				//this.setResponse((Response) in.readObject()) ;
				System.out.println(ios.getClass());

				if (ios.getClass().getSuperclass().getSimpleName().equals("Notification")){
					//notif = (Notification) ios;

					// si message publique
					if (ios.getClass().getSimpleName().equals("NotificationPublicMessage")){	
						NotificationPublicMessage notif = (NotificationPublicMessage) ios;
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
					// Si msg privé
					else if (ios.getClass().getSimpleName().equals("NotificationPrivateMessage")){	
						NotificationPrivateMessage notif = (NotificationPrivateMessage) ios;
						if (notif.getRecipient().equals(this.client.getUser().getLogin()))
						{	


							// si fenetre ouverte on affiche le message
							// on maj messagerie
							System.out.println(ihm.isMessagerieFrame);

							if (ihm.isMessagerieFrame) {

								if(!(ihm.messagerie.get(notif.getMessage().getAuthor())==null)) {
									builder.append(ihm.messagerie.get(notif.getMessage().getAuthor()));
								}
								builder.append(notif.getMessage().toString());
								ihm.messagerie.put(notif.getMessage().getAuthor(), builder.toString());


								// on affiche si on est sur le bon utilisateur
								builder=new StringBuilder();
								System.out.println(ihm.memberJList.getSelectedValue());
								if(ihm.memberJList.getSelectedValue().equals(notif.getRecipient())) {
									if (!ihm.textPanePrivateMessage.getText().isEmpty()) builder.append(ihm.textPanePrivateMessage);
									builder.append(notif.getMessage().toString());
									ihm.textPanePrivateMessage.setText(builder.toString());	
									ihm.privateMessageFrame.remove(ihm.privatemessagesPane);
									JScrollPane messagesPane = new JScrollPane(ihm.textPaneMessage);
									messagesPane.setVerticalScrollBarPolicy(
											javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
									messagesPane.setBounds(125,10,460, 180);
									ihm.privateMessageFrame.add(messagesPane);
								}
								

							}
							else {
								// sinon On averti en changeant l'icone
								// on récupère le mesg
								ArrayList<Message> msgList = client.getPrivateMessages();
								msgList.add(notif.getMessage());
								client.setPrivateMessages(msgList);
								System.out.println(ihm.msgIcon==null);

								ihm.msgIcon="new_message.gif";
								ihm.mainFrame.remove(ihm.bMessagerie);
								JButton bmsg = new JButton(new ImageIcon(ihm.msgIcon));    
								bmsg.setBounds(750,10,45, 40);
								ihm.bMessagerie= bmsg;
								ihm.mainFrame.add(bmsg);

								bmsg.addActionListener(new ActionListener() {

									@Override
									public void actionPerformed(ActionEvent arg0) {
										try {
											IHM.PrivateMessageFrame(ihm, client);
										} catch (ResponseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
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
