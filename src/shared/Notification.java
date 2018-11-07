package shared;

import java.io.Serializable;

public class Notification implements Serializable{
	private Message message;
	
	public Notification(Message message) {
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}


}