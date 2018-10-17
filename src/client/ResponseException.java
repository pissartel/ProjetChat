package client;

public class ResponseException extends Exception {
	public ResponseException(){
	    System.out.println("Reception d'une mauvaise réponse de la part du serveur");
	  }  
}
