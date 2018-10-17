package shared;

public class NewTopicResponse extends Response {
	private Topic newTopicCreated;
	public NewTopicResponse(Topic newTopicCreated) {
		this.newTopicCreated= newTopicCreated;
	}

	public Topic getTopic() {
		return newTopicCreated;
	}
}
