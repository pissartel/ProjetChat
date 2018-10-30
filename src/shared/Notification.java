package shared;

import java.io.Serializable;

public class Notification extends Response {

	private Topic topic;
	private Message message;

	public Notification(Topic topic, Message message) {
		this.setTopic(topic);
		this.setMessage(message);
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}



}
