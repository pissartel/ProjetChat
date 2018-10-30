package shared;

import java.io.Serializable;
import java.util.Date;

/*
	Classe user avec un champs login et un champ mdp
 */
public class User implements Serializable{
	private String login;
	private String password;
	private Date dateInscription;

	public User(String login, String password) {
		this.login = login;
		this.password = password;
		this.dateInscription=new Date();
	}

	public String toString() {
		return "login : " + this.login + "\n" + "password : " + this.password 
				+ "\n" + "Date d'inscription :" + this.dateInscription.toString();  
	}

	public String getLogin() {
		return this.login;  
	}

	public String getPassword() {
		return this.password;  
	}
}
