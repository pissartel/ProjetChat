package client;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import shared.Topic;

public class IHM {
	public static void main(String argv[]) {

		// Frame principal
		JFrame f = new JFrame("Chat Java");
		f.setSize(850,650); // on set la size du frame
		f.setLocationRelativeTo(null) ; // on centre le frame

		// en tête avec risitas
		JLabel labelHead =new JLabel("Chat Java"); 
		labelHead.setForeground(Color.BLUE);
		labelHead.setBounds(240,50,250, 40);  
		labelHead.setFont(new Font("Serif", Font.PLAIN, 50));
		labelHead.setHorizontalAlignment(JLabel.CENTER);

		ImageIcon icone = new ImageIcon("risi.gif");
		JLabel risiLabel =new JLabel(icone);
		risiLabel.setBounds(510, 10, 100, 100);

		//enter login 
		JLabel labelLogin = new JLabel();		
		labelLogin.setText("Login :");
		labelLogin.setFont(new Font("Arial", Font.PLAIN, 20));
		labelLogin.setBounds(280, 240, 300, 30);
		//textfield to enter login
		JTextField textfieldLog= new JTextField();
		textfieldLog.setBounds(380, 240, 200, 30);

		//enter password 
		JLabel labelPass = new JLabel();		
		labelPass.setText("Password :");
		labelPass.setFont(new Font("Arial", Font.PLAIN, 20));
		labelPass.setBounds(280, 300, 300, 30);
		//textfield to enter login
		JTextField textfieldPass= new JTextField();
		textfieldPass.setBounds(380, 300, 200, 30);


		//Créer compte bouton
		JButton bins=new JButton("S'inscrire");    
		bins.setBounds(280,400,140, 40);

		JButton bco=new JButton("Se connecter");    
		bco.setBounds(440,400,140, 40);

		//empty label which will show event after button clicked
		JLabel labelMsg = new JLabel();
		labelMsg.setBounds(100, 500, 500, 30);

		//add to frame
		f.add(labelHead);
		f.add(risiLabel);
		f.add(labelLogin);
		f.add(textfieldLog);
		f.add(labelPass);
		f.add(textfieldPass);
		f.add(bins);
		f.add(bco);
		f.add(labelMsg);
		f.setLayout(null);    
		f.setVisible(true);    
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Création du client
		Client client = new Client();


		// Fonctions exécutées lors de l'appui
		bins.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(client.createAccount(textfieldLog.getText(), textfieldPass.getText())) {
						labelMsg.setText("Compte créé. Vous pouvez vous connecter maintenant :)");
						labelMsg.setForeground(Color.BLUE);
					}
					else {
						labelMsg.setText("Ce login est déjà utilisé :(");
						labelMsg.setForeground(Color.RED);
					}

				} catch (ResponseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}          
		});
		bco.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(client.authenticate(textfieldLog.getText(), textfieldPass.getText())) {
						topicMenu( f, client) ;
					}
					else {
						labelMsg.setText("Mauvais identifiant et/ou mot de passe :(");
						labelMsg.setForeground(Color.RED);
					}

				} catch (ResponseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}          
		});

	}

	public static void topicMenu(JFrame f, Client client) throws ResponseException {
		// reset du frame
		f.getContentPane().removeAll();
		f.repaint();

		ArrayList<Topic> topicList = client.loadForum();


		JLabel labelHead =new JLabel("Liste des topics"); 
		labelHead.setForeground(Color.BLUE);
		labelHead.setBounds(50,10,250, 50);  
		labelHead.setFont(new Font("Serif", Font.PLAIN, 30));

		// Creation de la liste
		DefaultListModel<String> listModel = new DefaultListModel<>();
		topicList.forEach(x-> listModel.addElement( x.getTitle() + "\t \t \t \t \t \t \t de :" + x.getAuthor()));
		JList topicJList = new JList<>(listModel);
		topicJList.setBounds(50,50,500, 500);  

		//Créer compte bouton
		JButton bnew=new JButton("Nouveau topic");    
		bnew.setBounds(550,50,140, 40);

		JButton bco=new JButton("Se connecter");    
		bco.setBounds(550,100,140, 40);

		//add to frame
		f.add(labelHead);
		f.add(topicJList);
		f.setLayout(null);    

		// Fonctions exécutées lors de l'appui
		bnew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					newTopic(f, client);
				} catch (ResponseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}          
		});
		bco.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(client.authenticate(textfieldLog.getText(), textfieldPass.getText())) {
						topicMenu( f, client) ;
					}
					else {
						labelMsg.setText("Mauvais identifiant et/ou mot de passe :(");
						labelMsg.setForeground(Color.RED);
					}

				} catch (ResponseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}          
		});

	}

	public static void newTopic(JFrame topicListFrame, Client client) throws ResponseException {
		JFrame newTopicFrame = new JFrame("Nouveau Topic");
		newTopicFrame.setSize(300,300); // on set la size du frame
		newTopicFrame.setLocationRelativeTo(null) ; // on centre le frame
		//submit button
		JButton b=new JButton("Ok");    
		b.setBounds(250,250,140, 40);    

		//enter login 
		JLabel labelTitle = new JLabel();		
		labelTitle.setText("Titre du Topic :");
		labelTitle.setFont(new Font("Arial", Font.PLAIN, 20));
		labelTitle.setBounds(10, 100, 300, 30);
		//textfield to enter login
		JTextField textfieldTitle= new JTextField();
		textfieldTitle.setBounds(100, 100, 200, 30);

		//enter password 
		JLabel labelMsg = new JLabel();		
		labelMsg.setText("Message :");
		labelMsg.setFont(new Font("Arial", Font.PLAIN, 20));
		labelMsg.setBounds(10, 200, 300, 30);
		//textfield to enter login
		JTextField textfieldMsg= new JTextField();
		textfieldMsg.setBounds(100, 200, 200, 30);



		//add to frame
		newTopicFrame.add(labelTitle);
		newTopicFrame.add(textfieldTitle);

		newTopicFrame.add(labelMsg);
		newTopicFrame.add(textfieldMsg);
		newTopicFrame.add(b);    
		newTopicFrame.setLayout(null);    
		newTopicFrame.setVisible(true);    
		newTopicFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   

		//action listener
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Topic newTopic = client.newTopic(textfieldTitle.getText(), textfieldMsg.getText());
				} catch (ResponseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				newTopicFrame.dispose();
				topicMenu(topicListFrame,  client);
			}          
		});
	}         




}


