package shared;

public class DeleteTopicRequest extends Request {
	private User user;
	private Topic topic;

	public DeleteTopicRequest(User adminUser, Topic topic) {
		this.user=adminUser;
		this.topic=topic;
		
	}
	
	public DeleteTopicRequest(User adminUser, String str) {
		this.user=adminUser;
		if (str.toLowerCase().equals("all")) this.topic = new Topic(user, "all", "");
		else this.topic = new Topic(user, str, "");
	}
	
	public Topic getTopic() {
		return this.topic;  
	}
	
	public User getUser() {
		return this.user;  
	}
}
