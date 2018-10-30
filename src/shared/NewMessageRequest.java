package shared;

public class NewMessageRequest extends Request {
	private Message message;
	private Topic topic;

	public NewMessageRequest(Message message, Topic topic) {
		this.setMessage(message);
		this.setTopic(topic);
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	
	

}
