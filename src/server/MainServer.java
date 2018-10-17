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
			//
			//File nomFichier = new File("userdatabase.db");
			//	nomFichier.createNewFile();
			 //File nomFichier2 = new File("forumdatabase.db");
			 //nomFichier2.createNewFile();
			//	userDatabase.clearData();
			//
			t1 = new Thread(new Server());
			t1.start();	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		finally{
			/*
			System.out.println(" Eteindre le serveur ? Y");
			Scanner sc = new Scanner(System.in);
			if (sc.toString().toLowerCase()=="y") t1.stop();
			 */
		}	

	}



}
