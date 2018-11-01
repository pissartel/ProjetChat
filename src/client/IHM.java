package client;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.Socket;
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
	public  JFrame mainFrame;
	public JTextPane textPaneMessage;
	public JScrollPane messagesPane ;

	public IHM(JFrame frame){
		this.mainFrame=frame;
	}

	public static void main(String argv[]) {

		// Frame principal
		IHM ihm = new IHM(new JFrame("Chat Java"));
		//	JFrame mainFrame = new JFrame("Chat Java");
		ihm.mainFrame.setSize(850,650); // on set la size du frame
		ihm.mainFrame.setLocationRelativeTo(null) ; // on centre le frame

		// en tête avec le logo
		JLabel labelHead =new JLabel("Chat Java"); 
		labelHead.setForeground(Color.BLUE);
		labelHead.setBounds(240,50,250, 40);  
		labelHead.setFont(new Font("Serif", Font.PLAIN, 50));
		labelHead.setHorizontalAlignment(JLabel.CENTER);

		ImageIcon icone = new ImageIcon("logo.gif");
		JLabel iconLabel =new JLabel(icone);
		iconLabel.setBounds(510, 10, 100, 100);

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
		ihm.mainFrame.add(labelHead);
		ihm.mainFrame.add(iconLabel);
		ihm.mainFrame.add(labelLogin);
		ihm.mainFrame.add(textfieldLog);
		ihm.mainFrame.add(labelPass);
		ihm.mainFrame.add(textfieldPass);
		ihm.mainFrame.add(bins);
		ihm.mainFrame.add(bco);
		ihm.mainFrame.add(labelMsg);
		ihm.mainFrame.setLayout(null);    
		ihm.mainFrame.setVisible(true);    
		ihm.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
						ihm.mainFrame.setTitle(textfieldLog.getText());
						topicMenu( ihm, client) ;
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

	public static void topicMenu(IHM ihm, Client client) throws ResponseException {
		// reset du frame
		ihm.mainFrame.getContentPane().removeAll();
		ihm.mainFrame.repaint();

		//int page = 1;
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

		//Créer compte bouton
		JButton bnew=new JButton("Nouveau topic");    
		bnew.setBounds(50,555,140, 40);

		//add to frame
		ihm.mainFrame.add(labelHead);
		ihm.mainFrame.add(topicJList);
		//ihm.mainFrame.add(scrollPane);
		ihm.mainFrame.add(bnew);
		//labelList.forEach(x->ihm.mainFrame.add(x));
		ihm.mainFrame.setLayout(null);    

		// Fonction exécutée pour création de topic
		bnew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					newTopic(ihm, client);
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
							client.loadForum();
							topicFrame( ihm, client, client.loadTopic(theList.getSelectedIndex()));
						} catch (ResponseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
	}

	public static void newTopic(IHM ihm , Client client) throws ResponseException {
		JFrame topicListFrame = ihm.mainFrame;
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
		//      setVisible(true);

		// Label d'erreur
		JLabel labelErr = new JLabel();
		labelErr.setBounds(50,250,140, 40); 
		labelErr.setForeground(Color.RED);



		//add to frame
		newTopicFrame.add(labelTitle);
		newTopicFrame.add(textfieldTitle);

		newTopicFrame.add(labelErr);
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

					if (!(newTopic==null)) {
						newTopicFrame.dispose();

						try {
							//topicMenu(topicListFrame,  client);
							topicMenu(ihm,  client);
						} catch (ResponseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else labelErr.setText("Vous avez dejà créé ce topic !");
				} catch (ResponseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}          
		});
	}         

	public static void topicFrame(IHM ihm, Client client, Topic topic) throws ResponseException {
		// reset du frame
		ihm.mainFrame.getContentPane().removeAll();
		ihm.mainFrame.repaint();

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

		ihm.textPaneMessage = textPaneMessage;
		ihm.messagesPane =  messagesPane;

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

		ihm.mainFrame.add(labelHead);
		//ihm.mainFrame.add(labelContentHead);
		ihm.mainFrame.add(contentPane);
		ihm.mainFrame.add(messagesPane);
		ihm.mainFrame.add(scrollPane);
		ihm.mainFrame.add(bok);
		ihm.mainFrame.add(bre);
		ihm.mainFrame.add(labelMsg);

		ihm.mainFrame.setLayout(null);    
		ihm.mainFrame.setVisible(true);  

		// On lance le thread pour refresh les nouveau messages envoyés
	//	RefreshTopic RT = new RefreshTopic( client.getOIS(), client, ihm,  topic);
	//	Thread t1 = new Thread(RT );
	//	t1.start();
		//action listener
		
		// On rajoute ihm et topic
		client.getServerHandler().setIhm(ihm);
		client.getServerHandler().setTopic(topic);
		
		bok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					topic.getMessages().add(client.newMessage(textAreadMsg.getText()));
					textAreadMsg.setText("");
					//topicFrame( ihm.mainFrame, client, topic);

				} catch (ResponseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}          
		});

		bre.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			
						try {
							client.getServerHandler().setTopic(null);
							topicMenu( ihm, client) ;
						} catch (ResponseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

			}          
		});	


	}
}