package shared;

import java.util.ArrayList;

public class Topic extends Message{
	private String author;
	private ArrayList<Message> messages;
	private String content;
	private String title;
	
	public Topic(shared.User user, String title, String content) {
		super(user, content);
		// TODO Auto-generated constructor stub
		this.setTitle(title);
		this.setMessages(new ArrayList<Message>());

	}


	public int getNumberOfMessages() {
		return this.messages.size();  
	}


	public ArrayList<Message> getMessages() {
		return messages;
	}


	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public String toString() {
		/*if ((this.getMessages()==null)) {
			return "---------------------------------- " + this.getTitle() + "---------------------------------- \n"+ "De : " + this.getAuthor() + "\n" + this.getContent()+ "\n" + "----------------------------------"+ "\n" ;

		}
		else*/ 
		StringBuilder builder = new StringBuilder();
		builder.append("---------------------------------- " + this.getTitle() + " ---------------------------------- \n" + "De : " + this.getAuthor() + "\n" + this.getContent()+ "\n" + "----------------------------------"+ "\n");

		if (!(this.getMessages().isEmpty())) this.getMessages().forEach(x->builder.append(x.toString()));
		return builder.toString();
	}

	public String toStringMessages() {
		StringBuilder builder = new StringBuilder();
		this.getMessages().forEach(x->builder.append(x.toString()));
		return builder.toString();
	}


	public Topic addMessage(Message message) {

		if (!(this.getMessages().isEmpty())) this.getMessages().add(message);
		else {
			this.getMessages().add(message);
			//this.setMessages(messageList);
		}
		return this;
	}









}
