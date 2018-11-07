package shared;

public class NewPrivateMessageRequest extends Request{
	private Message message;
	private String recipient;

	public NewPrivateMessageRequest(Message message, String recipient) {
		this.setMessage(message);
		this.setRecipient(recipient);
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

}
