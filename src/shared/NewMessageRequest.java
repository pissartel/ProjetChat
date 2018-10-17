package shared;

public class NewMessageRequest extends Request {
	private Message message;
	public Topic topic;

	public NewMessageRequest(Message message, Topic topic) {
		this.setMessage(message);
		this.topic=topic;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	

}
