package shared;

public class NotificationPrivateMessage extends Notification{
	private Message message;
	private String recipient;

	public NotificationPrivateMessage(Message message, String recipient) {
		super(message);
		this.setRecipient(recipient);
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	

}
