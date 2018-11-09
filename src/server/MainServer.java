package server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
//import server.Database;
import java.util.*;

/*
 	Classe contenant le thread principale qui lance le serveur
 */
public class MainServer {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("On démarre le serveur");
		Thread t1 = null;
		try {
			t1 = new Thread(new Server());
			t1.start();	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}


	}



}
