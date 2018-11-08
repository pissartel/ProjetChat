package shared;

import java.util.ArrayList;
public class NewMessageResponse extends Response{
	private boolean isSent;

	public NewMessageResponse(boolean isSent) {
		this.isSent = isSent;
	}
	public boolean isSent(){
		return this.isSent;
	}


}
