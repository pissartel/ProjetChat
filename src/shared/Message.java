package shared;

import java.io.Serializable;
import java.util.Date;

import server.User;

public class Message implements Serializable{
	private String author;
	private String content;
	public Date date;


	
	public Message(User author, String content) {
		this.author= author.getLogin();
		this.setContent(content);
		this.date=new Date();
	}
	
	public String getAuthor() {
		return this.author;  
	}
	
	public String getContent() {
		return this.content;  
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String toString() {
		return this.getAuthor() + " : \n"  + this.getContent()+ "\n" 
	+ "----------------------------------"+ "\n" ;
	}
	


}
