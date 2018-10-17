package shared;

import server.User;

public class AuthentificationRequest extends Request {
	private User user;

	public AuthentificationRequest(String login, String password) {
		this.user=new User(login, password);
	}
	
	
	public User getUser() {
		return this.user;  
	}
	
	

}
