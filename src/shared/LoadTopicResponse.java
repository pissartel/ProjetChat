package shared;

import java.util.ArrayList;

public class LoadTopicResponse extends Response{
	private Topic topic ;

	public LoadTopicResponse(Topic topic) {
		this.topic=topic;
	}
	public Topic getTopic() {
		return this.topic;
	}{

	}
}