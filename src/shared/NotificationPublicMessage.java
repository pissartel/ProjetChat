package shared;

import java.io.Serializable;

public class NotificationPublicMessage extends Notification{

	private Topic topic;
	private Message message;

	public NotificationPublicMessage(Topic topic, Message message) {
		super(message);
		this.setTopic(topic);
		
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}





}
