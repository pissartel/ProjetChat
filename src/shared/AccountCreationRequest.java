package shared;

public class AccountCreationRequest extends Request{
	private User user;

	public AccountCreationRequest(String login, String password) {
		this.user=new User(login, password);
	}
	
	
	public User getUser() {
		return this.user;  
	}
}
