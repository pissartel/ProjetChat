package client;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
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
		//f.add(risiLabel);
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
		
		int page = 1;
		ArrayList<Topic> topicList = client.loadForum();

		JLabel labelHead =new JLabel("Liste des topics"); 
		labelHead.setForeground(Color.BLUE);
		labelHead.setBounds(50,10,250, 50);  
		labelHead.setFont(new Font("Serif", Font.PLAIN, 30));

		//Creation de la liste
		DefaultListModel<String> listModel = new DefaultListModel<>();
		topicList.forEach(x-> listModel.addElement( x.getTitle() + "\t\t\t\t de : \t " + x.getAuthor()));
		JList topicJList = new JList<>(listModel);
		topicJList.setBounds(50,55,750, 500);  
		topicJList.setFont(new Font("Arial", Font.PLAIN, 15));

		// scrollbar
		JScrollPane scrollPane = new JScrollPane();
		//scrollPane.setLayoutOrientation(JList.VERTICAL);
		//scrollPane.setBounds(50,55,750, 500);
		//scrollPane.setViewportView(topicJList);
		//scrollPane.add(topicJList);
		
		//Créer compte bouton
		JButton bnew=new JButton("Nouveau topic");    
		bnew.setBounds(50,555,140, 40);

		//add to frame
		f.add(labelHead);
		f.add(topicJList);
		//f.add(scrollPane);
		f.add(bnew);
		//labelList.forEach(x->f.add(x));
		f.setLayout(null);    

		// Fonction exécutée pour création de topic
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

		// Fonction exécutée pour l'ouverture d'un topic
		topicJList.addMouseListener( new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				JList theList = (JList) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 2) {
					int index = theList.locationToIndex(mouseEvent.getPoint());
					if (index >= 0) {
						//Object o = theList.getModel().getElementAt(index);
						try {
							topicFrame( f, client, client.loadTopic(theList.getSelectedIndex()));
						} catch (ResponseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
	}

	public static void newTopic(JFrame topicListFrame, Client client) throws ResponseException {
		JFrame newTopicFrame = new JFrame("Nouveau Topic");
		newTopicFrame.setSize(500,300); // on set la size du frame
		newTopicFrame.setLocationRelativeTo(null) ; // on centre le frame
		//submit button
		JButton b=new JButton("Ok");    
		b.setBounds(240,220,140, 40);    

		//enter title 
		JLabel labelTitle = new JLabel();		
		labelTitle.setText("Titre du Topic :");
		labelTitle.setFont(new Font("Arial", Font.PLAIN, 15));
		labelTitle.setBounds(10, 10, 300, 30);
		//textfield to enter login
		JTextField textfieldTitle= new JTextField();
		textfieldTitle.setBounds(125, 10, 350, 30);

		//enter message 
		JLabel labelMsg = new JLabel();		
		labelMsg.setText("Message :");
		labelMsg.setFont(new Font("Arial", Font.PLAIN, 15));
		labelMsg.setBounds(10, 50, 300, 30);
		JTextArea textAreadMsg= new JTextArea();
		textAreadMsg.setBounds(125, 50, 350, 150);

		// scroll bar
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(textAreadMsg);
		scrollPane.setBounds(125, 50, 350, 150);
		//textAreadMsg.add(zoneScrolable,BorderLayout.CENTER);
		//      setVisible(true);



		//add to frame
		newTopicFrame.add(labelTitle);
		newTopicFrame.add(textfieldTitle);

		newTopicFrame.add(labelMsg);
		//newTopicFrame.add(textAreadMsg);
		newTopicFrame.add(scrollPane);
		newTopicFrame.add(b);    
		newTopicFrame.setLayout(null);    
		newTopicFrame.setVisible(true);    
		//newTopicFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   

		//action listener
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Topic newTopic = client.newTopic(textfieldTitle.getText(), textAreadMsg.getText());
				} catch (ResponseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				newTopicFrame.dispose();
				try {
					topicMenu(topicListFrame,  client);
				} catch (ResponseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}          
		});
	}         

	public static void topicFrame(JFrame f, Client client, Topic topic) throws ResponseException {
		// reset du frame
		f.getContentPane().removeAll();
		f.repaint();

		JLabel labelHead =new JLabel(topic.getTitle()); 
		labelHead.setForeground(Color.BLUE);
		labelHead.setBounds(50,10,250, 50);  
		labelHead.setFont(new Font("Serif", Font.PLAIN, 30));

		// affichage du content
		int largeur=750;
		
		JTextPane textPaneContent = new JTextPane();
		textPaneContent.setEditable(false);
		textPaneContent.setText(topic.getContent()); 

		JScrollPane contentPane = new JScrollPane(textPaneContent);
		contentPane.setVerticalScrollBarPolicy(
                javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		contentPane.setBounds(50,55,largeur, 100); 
		
		// affichage des messages 
		//JLabel labelMessages =new JLabel(topic.toStringMessages()); 
		JTextPane textPaneMessage = new JTextPane();
		textPaneMessage.setEditable(false);
		textPaneMessage.setText(topic.toStringMessages());

		JScrollPane messagesPane = new JScrollPane(textPaneMessage);
		messagesPane.setVerticalScrollBarPolicy(
                javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		messagesPane.setBounds(50,170,largeur, 205);

		// zone de texte pour message
		JTextArea textAreadMsg= new JTextArea();
		textAreadMsg.setBounds(50, 350, largeur, 150);

		// scroll bar
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(textAreadMsg);
		scrollPane.setBounds(50, 425, largeur, 150);

		// bouton envoyer message
		JLabel labelMsg = new JLabel();		
		labelMsg.setText("Message :");
		labelMsg.setFont(new Font("Arial", Font.PLAIN, 15));
		labelMsg.setBounds(50, 395, 300, 30);
		
		JButton bok=new JButton("Envoyer");    
		bok.setBounds(655,580,140, 40);
		JButton bre=new JButton("Retour");    
		bre.setBounds(655,10,140, 40);

		f.add(labelHead);
		//f.add(labelContentHead);
		f.add(contentPane);
		f.add(messagesPane);
		f.add(scrollPane);
		f.add(bok);
		f.add(bre);
		f.add(labelMsg);

		f.setLayout(null);    
		f.setVisible(true);  

		//action listener
				bok.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							topic.getMessages().add(client.newMessage(textAreadMsg.getText()));
							textAreadMsg.setText("");
							topicFrame( f, client, topic);
							
						} catch (ResponseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}          
				});
				
				bre.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						try { topicMenu( f, client) ;
							
						} catch (ResponseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}				
					}          
				});	
	}
}


