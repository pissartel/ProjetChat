package shared;

public class DeleteTopicResponse extends Response {
	public boolean isDelete ;
	
	public DeleteTopicResponse(boolean yesOrnot) {
		this.isDelete=yesOrnot;
	}
}
