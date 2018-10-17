package shared;

import java.util.ArrayList;

public class LoadForumResponse extends Response{
	public ArrayList<Topic> topicList ;

	public LoadForumResponse(ArrayList<Topic>  topicList) {
		this.topicList=topicList;
	}
	public ArrayList<Topic> TopicList() {
		return this.topicList;
	}

	
}