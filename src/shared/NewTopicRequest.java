package shared;

public class NewTopicRequest extends Request {
	private Topic newTopic;

	public NewTopicRequest(Topic newTopic) {
		this.newTopic = newTopic;
	}
	
	
	public Topic getTopic() {
		return this.newTopic;  
	}
}
