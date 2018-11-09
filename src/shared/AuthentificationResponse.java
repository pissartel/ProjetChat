package shared;

public class AuthentificationResponse extends Response{
	private boolean isAuthentified ;
	private User user;
	public AuthentificationResponse(User user, boolean yesOrnot) {
		this.setUser(user);
		this.setAuthentified(yesOrnot);
	}
	public boolean isAuthentified() {
		return isAuthentified;
	}
	public void setAuthentified(boolean isAuthentified) {
		this.isAuthentified = isAuthentified;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

}
