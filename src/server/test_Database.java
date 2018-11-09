package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import shared.Message;
import shared.Topic;
import shared.User;


public class test_Database {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

		ArrayList<User> list_user = new ArrayList<User>();
		
		UserDatabase userl = new UserDatabase("userdatabase.db");

		ArrayList<Topic> list_topic = new ArrayList<Topic>();

		//Topic topic1 = new Topic(user1, "Règles du chat", "Il n'y a pas de règles sur le chat.");
		//Topic topic2 = new Topic(user2, "Topic2", "Blabla2");
		
		//topic1.addMessage(new Message(user2, "coucou"));
		//System.out.println(topic1.toString());
		//list_topic.add(topic1);
		//list_topic.add(topic2);
	//	System.out.println(topic1.toString());
		System.out.println("databases écrasées");

		ChatDatabase chat = new ChatDatabase("chatdatabase.db");
	//	chat.clearTopics();

				
		try {
			chat.saveTopics(list_topic);
			userl.saveData(list_user);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 

		try {
			ArrayList<Topic> topics = chat.loadTopics() ;
			topics.forEach(x->System.out.println(x.toString()));
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
