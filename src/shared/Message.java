package shared;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable{
	private String author;
	private String content;
	public String date;


	
	public Message(User author, String content) {
		this.author= author.getLogin();
		this.setContent(content);
		//this.date=new Date();
		SimpleDateFormat formater = new SimpleDateFormat("'le' dd/MM/yyyy 'à' hh:mm:ss");
		this.date= formater.format(new Date());
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
		return this.getAuthor() + " a écrit le " + this.date +  " : \n"  + this.getContent()+ "\n" 
	+ "----------------------------------"+ "\n" ;
	}
	


}
