package shared;

public class AccountCreationResponse extends Response {
	private boolean isCreated ;
	
	public AccountCreationResponse(boolean yesOrnot) {
		this.setCreated(yesOrnot);
	}
	public boolean isCreated() {
		return isCreated;
	}
	public void setCreated(boolean isAuthentified) {
		this.isCreated = isAuthentified;
	}

}
