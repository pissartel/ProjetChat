package shared;

import java.util.ArrayList;

public class LoadChatResponse extends Response{
	public ArrayList<Topic> topicList ;

	public LoadChatResponse(ArrayList<Topic>  topicList) {
		this.topicList=topicList;
	}
	public ArrayList<Topic> TopicList() {
		return this.topicList;
	}

	
}