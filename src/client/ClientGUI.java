/**
 * ClientGUI
 * It creates a frame with a game board of TicTacToe and a chat where the clients can
 * communicate with the opponents they are facing.
 */
package client;



import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
import javax.swing.Box;
import javax.swing.DefaultListModel;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import utility.InputListener;
import utility.Message;

/**
 * @author 755340 - Luis Ojeda
 * @version 1.0
 * October 1, 2019
 */
public class ClientGUI implements PropertyChangeListener{
	//Attributes
	private JFrame frame;
	private Container content;
	private JMenuBar menuBar;
	private JMenu mnOption;
	private JMenuItem mniHelp;
	private JButton firstPositionbtn;
	private JButton secondPositionbtn;
	private JButton thirdPositionbtn;
	private JButton fourthPositionbtn;
	private JButton fifthPositionbtn;
	private JButton sixthPositionbtn;
	private JButton seventhPositionbtn;
	private JButton eighthPositionbtn;
	private JButton ninthPositionbtn;
	private JTextField tfMessageToSend;
	private JLabel lblMessageToSend;
	private JButton btnSend;
	private DefaultListModel<String> listModel;
	private JList<String> lstMessageBoard;
	private JButton btnConnect;
	private JButton btnDisconnect;
	private Message obj;
	private ObjectOutputStream oos;
	private Socket socket;
	private String user;
	private String ip;

	/*
	 * Default Constructor
	 */
	public ClientGUI() {
		initialize();
	}
	
	/**
	 * initialize  creates the new JFrame for the client and calls the createMenu method
	 */
	private void initialize() {
		frame = new JFrame("TicTacToe The Super Game");
		frame.setSize(740, 620);
		frame.setLocationRelativeTo(null); 
		
		content = frame.getContentPane();
		content.add(getMainPanel());
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createMenu();
		frame.setVisible(true);
	}
	
	/**
	 * createMenu creates a bar in the top of the frame where users have the button option.
	 * From there, they can click help to get information about how to play the game.
	 */
	private void createMenu() {
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		mnOption = new JMenu("Options");
		mniHelp = new JMenuItem("Help");
		mniHelp.addActionListener(btnListener());
		mnOption.add(mniHelp);	
		menuBar.add(mnOption);
	}
	
	/**
	 * CheckwinCondition is called every time a position button (game button) is clicked.
	 * It checks if three or more buttons in a rows are pressed.
	 * If they are, tells the user with 3 X that they have won and the user with 3 O that they have lost.
	 * If all panels have been pressed and no player won, it shows them a message that they draw.
	 */
	private void checkWinCondition() {
		if((firstPositionbtn.getText().equals("X") && secondPositionbtn.getText().equals("X") && thirdPositionbtn.getText().equals("X")) ||
			(secondPositionbtn.getText().equals("X") && fifthPositionbtn.getText().equals("X") && eighthPositionbtn.getText().equals("X")) ||
			(firstPositionbtn.getText().equals("X") && fourthPositionbtn.getText().equals("X") && seventhPositionbtn.getText().equals("X")) ||
			(firstPositionbtn.getText().equals("X") && fifthPositionbtn.getText().equals("X") && ninthPositionbtn.getText().equals("X")) || 
			(thirdPositionbtn.getText().equals("X") && fifthPositionbtn.getText().equals("X") && seventhPositionbtn.getText().equals("X")) || 
			(thirdPositionbtn.getText().equals("X") && sixthPositionbtn.getText().equals("X") && ninthPositionbtn.getText().equals("X")) || 
			(fourthPositionbtn.getText().equals("X") && fifthPositionbtn.getText().equals("X") && sixthPositionbtn.getText().equals("X")) || 
			(seventhPositionbtn.getText().equals("X") && eighthPositionbtn.getText().equals("X") && ninthPositionbtn.getText().equals("X"))) {  
				String wonMessage = "You have won";
				JOptionPane.showMessageDialog(frame, wonMessage, "WINNER :D", JOptionPane.INFORMATION_MESSAGE);	
				Object[] options = {"Yes, let's go!", "No, maybe later."};
				int nextGame = JOptionPane.showOptionDialog(frame, "Do you want to play again?", "Continue?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (nextGame == 0){
					cleanBoard();
				}
				else{
					disconnectClient();
				}
		}
		
		else if((firstPositionbtn.getText().equals("O") && secondPositionbtn.getText().equals("O") && thirdPositionbtn.getText().equals("O")) ||
			(secondPositionbtn.getText().equals("O") && fifthPositionbtn.getText().equals("O") && eighthPositionbtn.getText().equals("O")) ||
			(firstPositionbtn.getText().equals("O") && fourthPositionbtn.getText().equals("O") && seventhPositionbtn.getText().equals("O")) ||
			(firstPositionbtn.getText().equals("O") && fifthPositionbtn.getText().equals("O") && ninthPositionbtn.getText().equals("O")) || 
			(thirdPositionbtn.getText().equals("O") && fifthPositionbtn.getText().equals("O") && seventhPositionbtn.getText().equals("O")) || 
			(thirdPositionbtn.getText().equals("O") && sixthPositionbtn.getText().equals("O") && ninthPositionbtn.getText().equals("O")) || 
			(fourthPositionbtn.getText().equals("O") && fifthPositionbtn.getText().equals("O") && sixthPositionbtn.getText().equals("O")) || 
			(seventhPositionbtn.getText().equals("O") && eighthPositionbtn.getText().equals("O") && ninthPositionbtn.getText().equals("O"))) {  
				String lostMessage = "You have lost";
				Object[] options = {"Yes, let's go!", "No, maybe later."};
				JOptionPane.showMessageDialog(frame, lostMessage, "NOT WINNER :(", JOptionPane.INFORMATION_MESSAGE);
				int nextGame = JOptionPane.showOptionDialog(frame, "Do you want to play again?", "Continue?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (nextGame == 0){
					cleanBoard();
				}
				else if(nextGame == 1){
					disconnectClient();
				}
				else {
					JOptionPane.showMessageDialog(frame, "Disconnection canceled. Click disconnect to continue and open the game again");
				}
		}
		
		else if(!firstPositionbtn.getText().equals("") && !secondPositionbtn.getText().equals("") && !thirdPositionbtn.getText().equals("") &&
				!fourthPositionbtn.getText().equals("") && !fifthPositionbtn.getText().equals("") && !sixthPositionbtn.getText().equals("") && 
				!seventhPositionbtn.getText().equals("") && !eighthPositionbtn.getText().equals("") && !ninthPositionbtn.getText().equals("")) {
			String lostMessage = "It's a draw! Both of you rules!";
			Object[] options = {"Yes, let's go!", "No, maybe later."};
			JOptionPane.showMessageDialog(frame, lostMessage, "DRAW", JOptionPane.INFORMATION_MESSAGE);
			int nextGame = JOptionPane.showOptionDialog(frame, "Do you want to play again?", "Continue?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			
			if (nextGame == 0){
				cleanBoard();
			}
			else{
				disconnectClient();
			}
			
		}
		
	}
	
	private void cleanBoard() {
		firstPositionbtn.setText("");
		firstPositionbtn.setEnabled(true);
		secondPositionbtn.setText("");
		secondPositionbtn.setEnabled(true);
		thirdPositionbtn.setText("");
		thirdPositionbtn.setEnabled(true);
		fourthPositionbtn.setText("");
		fourthPositionbtn.setEnabled(true);
		fifthPositionbtn.setText("");
		fifthPositionbtn.setEnabled(true);
		sixthPositionbtn.setText("");
		sixthPositionbtn.setEnabled(true);
		seventhPositionbtn.setText("");
		seventhPositionbtn.setEnabled(true);
		eighthPositionbtn.setText("");
		eighthPositionbtn.setEnabled(true);
		ninthPositionbtn.setText("");
		ninthPositionbtn.setEnabled(true);
		
		try {
			oos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		runSocket();
	}
	
	/**
	 * nextTurn method 
	 * @param myTurn a boolean with the decision of enabling disabling the game board for a player.
	 */
	private void nextTurn(boolean myTurn) {
		firstPositionbtn.setEnabled(myTurn);
		if(!firstPositionbtn.getText().equals("")) {
			firstPositionbtn.setEnabled(false);
		}
		secondPositionbtn.setEnabled(myTurn);
		if(!secondPositionbtn.getText().equals("")) {
			secondPositionbtn.setEnabled(false);
		}
		thirdPositionbtn.setEnabled(myTurn);
		if(!thirdPositionbtn.getText().equals("")) {
			thirdPositionbtn.setEnabled(false);
		}
		fourthPositionbtn.setEnabled(myTurn);
		if(!fourthPositionbtn.getText().equals("")) {
			fourthPositionbtn.setEnabled(false);
		}
		fifthPositionbtn.setEnabled(myTurn);
		if(!fifthPositionbtn.getText().equals("")) {
			fifthPositionbtn.setEnabled(false);
		}
		sixthPositionbtn.setEnabled(myTurn);
		if(!sixthPositionbtn.getText().equals("")) {
			sixthPositionbtn.setEnabled(false);
		}
		seventhPositionbtn.setEnabled(myTurn);
		if(!seventhPositionbtn.getText().equals("")) {
			seventhPositionbtn.setEnabled(false);
		}
		eighthPositionbtn.setEnabled(myTurn);
		if(!eighthPositionbtn.getText().equals("")) {
			eighthPositionbtn.setEnabled(false);
		}
		ninthPositionbtn.setEnabled(myTurn);	
		if(!ninthPositionbtn.getText().equals("")) {
			ninthPositionbtn.setEnabled(false);
		}
	}

	/**
	 * registerUser method is called when the connect button is pressed by a client.
	 * It enables the disconnect and send buttons.
	 * It ask the client for a user name and the IP if the server they want to connect.
	 * It runs the runSocket method.
	 */
	private void registerUser() {
		btnDisconnect.setEnabled(true);
		btnConnect.setEnabled(false);
		btnSend.setEnabled(true);
		user = "";
		user = JOptionPane.showInputDialog("Enter User Name");
		ip = JOptionPane.showInputDialog("Enter Server IP");
		
		runSocket();
		
	}
	
	/**
	 * runSocket method runs the client socket with the IP and port number of the server socket.
	 * Creates a new ObjectOputStream and InputListener methods.
	 * Creates a new thread with the created inputListener and starts said thread.
	 */
	private void runSocket() {
		oos = null;
		
		try {
			//Open a connection to my server listening on port 3333
			socket = new Socket(ip, 3333);
			
			//Create an object output stream to send the message to server.
			OutputStream os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);
			
			//Create an inputlistener object
			InputListener intListener = new InputListener(socket, this);
			Thread t = new Thread(intListener);
			t.start();	
			
		} catch(UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * disconnectClient method is called when a client socket is dropped and disconnected from the server. 
	 * It shows a message telling the user they have been disconnected and it closes the GUI window.
	 */
	private void disconnectClient() {
		//close all sockets and streams
		try {
			oos.close();
			socket.close();
			String DisconnectMessage = "You have been disconnected from the server.Thank you for playing";
			JOptionPane.showMessageDialog(frame, DisconnectMessage);
			frame.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/*****************************************************************************************************************************************
 	* Construction of the GUI panels.
 	* 
 	******************************************************************************************************************************************/
	
	/**
	 * getMainPanel method creates a JPanel that covers the whole frame and it's divided in two parts.
	 * @return panel a JPanel that covers the whole frame
	 */
	private JPanel getMainPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(getNorthPanel(), BorderLayout.NORTH);
		panel.add(getCenterPanel(), BorderLayout.CENTER);
		return panel;
	}
	
	/**
	 * getNorthPanel method creates a JPanel that holds a JLabel as a title for the game.
	 * It's located at the top of the frame
	 * @return panel a JPanel with the north part of the frame
	 */
	private JPanel getNorthPanel() {
		JPanel panel = new JPanel();
		Border edge = BorderFactory.createEtchedBorder();
		Box vBox = Box.createVerticalBox();
		
		JLabel label = new JLabel("Tic Tac Toe the game!");
		label.setForeground(Color.BLUE);
		label.setFont(new Font("Times New Roman", Font.BOLD, 25));
		
		vBox.add(Box.createVerticalStrut(10));
		vBox.add(label);
		vBox.add(Box.createVerticalStrut(15));
		
		panel.add(vBox);
		panel.setBorder(edge);
		return panel;
	}
	
	/**
	 * getCenterPanel method creates a JPanel in the center of the frame.
	 * It is divided in two parts.
	 * @return panel a JPanel with the center part of the frame
	 */
	private JPanel getCenterPanel() {
		JPanel panel = new JPanel(new GridLayout(1,2, 0, 10));
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.add(getLeftPanel());
		panel.add(getRightPanel());
		return panel;
	}
	
	/**
	 * getLeftPanel method creates 9 buttons that are used as the game board.
	 * It's located at the left side of the frame.
	 * @return panel a JPanel with the game board.
	 */
	private JPanel getLeftPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setLayout(new GridLayout(3,3,10,10));
		Border buttonEdge = BorderFactory.createRaisedBevelBorder();
		
		firstPositionbtn = new JButton("");
		firstPositionbtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 35));
		firstPositionbtn.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, Color.BLACK));
		firstPositionbtn.addActionListener(positionListener());
		firstPositionbtn.setOpaque(false);
		firstPositionbtn.setEnabled(false);
		firstPositionbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		secondPositionbtn = new JButton("");
		secondPositionbtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 35));
		secondPositionbtn.setBorder(BorderFactory.createMatteBorder(0, 5, 5, 5, Color.BLACK));
		secondPositionbtn.addActionListener(positionListener());
		secondPositionbtn.setOpaque(false);
		secondPositionbtn.setEnabled(false);
		secondPositionbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		thirdPositionbtn = new JButton("");
		thirdPositionbtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 35));
		thirdPositionbtn.setBorder(BorderFactory.createMatteBorder(0, 5, 5, 0, Color.BLACK));
		thirdPositionbtn.addActionListener(positionListener());
		thirdPositionbtn.setOpaque(false);
		thirdPositionbtn.setEnabled(false);
		thirdPositionbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
	
		fourthPositionbtn = new JButton("");
		fourthPositionbtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 35));
		fourthPositionbtn.setBorder(BorderFactory.createMatteBorder(5, 0, 5, 5, Color.BLACK));
		fourthPositionbtn.addActionListener(positionListener());
		fourthPositionbtn.setOpaque(false);
		fourthPositionbtn.setEnabled(false);
		fourthPositionbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		fifthPositionbtn = new JButton("");
		fifthPositionbtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 35));
		fifthPositionbtn.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
		fifthPositionbtn.addActionListener(positionListener());
		fifthPositionbtn.setOpaque(false);
		fifthPositionbtn.setEnabled(false);
		fifthPositionbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		sixthPositionbtn = new JButton("");
		sixthPositionbtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 35));
		sixthPositionbtn.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 0, Color.BLACK));
		sixthPositionbtn.addActionListener(positionListener());
		sixthPositionbtn.setOpaque(false);
		sixthPositionbtn.setEnabled(false);
		sixthPositionbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		seventhPositionbtn = new JButton("");
		seventhPositionbtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 35));
		seventhPositionbtn.setBorder(BorderFactory.createMatteBorder(5, 0, 0, 5, Color.BLACK));
		seventhPositionbtn.addActionListener(positionListener());
		seventhPositionbtn.setOpaque(false);
		seventhPositionbtn.setEnabled(false);
		seventhPositionbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		eighthPositionbtn = new JButton("");
		eighthPositionbtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 35));
		eighthPositionbtn.setBorder(BorderFactory.createMatteBorder(5, 5, 0, 5, Color.BLACK));
		eighthPositionbtn.addActionListener(positionListener());
		eighthPositionbtn.setOpaque(false);
		eighthPositionbtn.setEnabled(false);
		eighthPositionbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		ninthPositionbtn = new JButton("");
		ninthPositionbtn.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 35));
		ninthPositionbtn.setBorder(BorderFactory.createMatteBorder(5, 5, 0, 0, Color.BLACK));
		ninthPositionbtn.addActionListener(positionListener());
		ninthPositionbtn.setOpaque(false);
		ninthPositionbtn.setEnabled(false);
		ninthPositionbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		panel.add(firstPositionbtn);
		panel.add(secondPositionbtn);
		panel.add(thirdPositionbtn);
		panel.add(fourthPositionbtn);
		panel.add(fifthPositionbtn);
		panel.add(sixthPositionbtn);
		panel.add(seventhPositionbtn);
		panel.add(eighthPositionbtn);
		panel.add(ninthPositionbtn);
		
		return panel;
	}
	
	/**
	 * getRightPanel method creates a JPanel that covers the right side of the frame.
	 * It's divided in two parts.
	 * @return panel a JPanel with the right part of the frame.
	 */
	private JPanel getRightPanel() {
		JPanel panel = new JPanel (new BorderLayout());
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.add(getNorthRightPanel(), BorderLayout.NORTH);
		panel.add(getCenterRightPanel(), BorderLayout.CENTER);
		 
		return panel;
	}

	/**
	 * getNorthRightPanel method creates a JPanel that covers the upper right side of the frame.
	 * It creates a JTextField where the client can enter a message.
	 * It creates a JButton that sends the message in the JTextfild to whichever other client is paired up with this instance of the clientGUI. 
	 * @return panel a JPanel with a text field that sends messages across clients.
	 */
	private JPanel getNorthRightPanel() {
		JPanel panel = new JPanel();
		panel.setLayout((LayoutManager) new FlowLayout(FlowLayout.LEFT));
		Border edge = BorderFactory.createEtchedBorder();
		Box vBox = Box.createVerticalBox();
		tfMessageToSend = new JTextField(28);
		vBox.add(tfMessageToSend);
		
		Component verticalStrut = Box.createVerticalStrut(5);
		vBox.add(verticalStrut);
		btnSend = new JButton("Send");
		vBox.add(btnSend);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Message msg = new Message(user, tfMessageToSend.getText(), new Date());
				try {
					oos.writeObject(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnSend.setFont(new Font("Arial", Font.BOLD, 15));
		btnSend.setBorder(edge);
		btnSend.setEnabled(false);
		Box hBox = Box.createHorizontalBox();
		vBox.add(hBox);
		JLabel label =  new JLabel("                  ");
		label.setAlignmentX(Component.RIGHT_ALIGNMENT);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		hBox.add(label);
		
		
		lblMessageToSend = new JLabel ("Message to Send:");
		panel.add(lblMessageToSend);
		lblMessageToSend.setAlignmentY(Component.TOP_ALIGNMENT);
		lblMessageToSend.setHorizontalAlignment(SwingConstants.LEFT);
		lblMessageToSend.setForeground(Color.BLACK);
		lblMessageToSend.setFont(new Font("Times New Roman", Font.BOLD, 18));
		
		
		vBox.add(Box.createVerticalStrut(10));
	
		panel.add(vBox);
		return panel;
	}
	
	/**
	 * getCenterRightPanel creates a JPanel that covers the center and southern part to the right of the frame.
	 * It's divided in two sections.
	 * @return 
	 */
	private JPanel getCenterRightPanel() {

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(getCenterRightPanelNorth(), BorderLayout.CENTER);
		panel.add(getCenterRightPanelSouth(), BorderLayout.SOUTH);
		return panel;
	}
	
	/**
	 * getCenterRightPanelNorth creates a JPanel that holds the messages that are being received by the client
	 * that the user is currently paired up with.
	 * @return a JPanel with a list model where the Client receives messages from other clients.
	 */
	private JPanel getCenterRightPanelNorth() {
		JPanel panel = new JPanel();
		
		listModel = new DefaultListModel<String>();
		lstMessageBoard = new JList<String>(listModel);
		
		JScrollPane scrollPane = new JScrollPane(lstMessageBoard);
		
		lstMessageBoard.setFixedCellWidth(350);
		lstMessageBoard.setVisibleRowCount(20);
		
		panel.add(scrollPane);
		return panel;
	}
	
	/**
	 * getCenterRightPanelSouth method creates a JPanel at the southern right side of the frame.
	 * It creates one button that the user uses to connect to the server and with another client.
	 * The connect JButton enables the rest of the functionality in the program.
	 * It creates a JButton that disconnects the user from the server and closes that client's socket.
	 * @return a JPanel with two buttons on it.
	 */
	private JPanel getCenterRightPanelSouth() {
		JPanel panel = new JPanel( new FlowLayout(FlowLayout.CENTER, 70, 15));
		    
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(btnListener());;
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(btnListener());
		btnDisconnect.setEnabled(false);
		
		panel.add(btnConnect);
		panel.add(btnDisconnect);
		return panel;
	}
	
	/*****************************************************************************************************************************************
 	* Listeners and property changers.
 	* 
 	******************************************************************************************************************************************/
	
	/**
	 * btnListener method 
	 * 
	 * Depending on the calling source, it performs different actions
	 * @return an ActionListener for the clicked button
	 */
	private ActionListener btnListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
	
				if(e.getSource() == btnDisconnect) {
					//Reconnect the opponent with another user
					Message reconnect = new Message(user, "OpponentWasDisconnectFromServer-NeedReconnect", new Date());
					try {
						oos.writeObject(reconnect);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					disconnectClient();
				}
				
				else if(e.getSource() == mniHelp) {
					String helpMessage = "You are assigned  X and opponent is assigned O. Each player can put their symbol once per turn in one of the avaiable spaces.\n"
							+ "Once a player have 3 symbols in a row, that player is the winner. The players can chat with each other while they are connected.\n "
							+ "When the game is done, the users will have the option to play again with the next player or to disconnect";
					JOptionPane.showMessageDialog(frame, helpMessage, "How to play", JOptionPane.QUESTION_MESSAGE);
				}
				
				else if(e.getSource() == btnConnect) {
					registerUser();
					nextTurn(true);
				}
			}
		};
	}
	
	/**
	 * postionListener method listens for the buttons in the game board 
	 * and performs and actions base on which button was pressed.
	 */
	private  ActionListener positionListener()  {
			return new ActionListener() {
			

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == firstPositionbtn) {
					firstPositionbtn.setText("X");
					Message move = new Message (user, "firstPositionButtonCliked", new Date());
					try {
						oos.writeObject(move);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					nextTurn(false);
//					CheckwinCondition();
				}
				
				else if(e.getSource() == secondPositionbtn) {
					secondPositionbtn.setText("X");
					Message move = new Message (user, "secondPositionButtonCliked", new Date());
					try {
						oos.writeObject(move);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					nextTurn(false);
//					CheckwinCondition();
				}
				
				else if (e.getSource() == thirdPositionbtn) {
					thirdPositionbtn.setText("X");
					Message move = new Message (user, "thirdPositionButtonCliked", new Date());
					try {
						oos.writeObject(move);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					nextTurn(false);
//					CheckwinCondition();
				}
				
				else if (e.getSource() == fourthPositionbtn) {
					fourthPositionbtn.setText("X");
					Message move = new Message (user, "fourthPositionButtonCliked", new Date());
					try {
						oos.writeObject(move);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					nextTurn(false);
//					CheckwinCondition();
				}
				
				else if (e.getSource() == fifthPositionbtn) {
					fifthPositionbtn.setText("X");
					Message move = new Message (user, "fifthPositionButtonCliked", new Date());
					try {
						oos.writeObject(move);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					nextTurn(false);
//					CheckwinCondition();
				}
				
				else if (e.getSource() == sixthPositionbtn) {
					sixthPositionbtn.setText("X");
					Message move = new Message (user, "sixthPositionButtonCliked", new Date());
					try {
						oos.writeObject(move);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					nextTurn(false);
//					CheckwinCondition();
				}
				
				else if (e.getSource() == seventhPositionbtn) {
					seventhPositionbtn.setText("X");
					Message move = new Message (user, "seventhPositionButtonCliked", new Date());
					try {
						oos.writeObject(move);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					nextTurn(false);
//					CheckwinCondition();
				}
				
				else if (e.getSource() == eighthPositionbtn) {
					eighthPositionbtn.setText("X");
					Message move = new Message (user, "eighthPositionButtonCliked", new Date());
					try {
						oos.writeObject(move);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					nextTurn(false);
//					CheckwinCondition();
				}
				
				else if (e.getSource() == ninthPositionbtn) {
					ninthPositionbtn.setText("X");
					Message move = new Message (user, "ninthPositionButtonCliked", new Date());
					try {
						oos.writeObject(move);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					nextTurn(false);
//					CheckwinCondition();
				}
				nextTurn(false);
				checkWinCondition();
			}
		};		
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		obj = (Message)e.getNewValue();
		switch(obj.getMsg()) {
		case "firstPositionButtonCliked": 
			firstPositionbtn.setText("O");
			firstPositionbtn.setEnabled(false);
			nextTurn(true);
			checkWinCondition();
			break;
			
		case "secondPositionButtonCliked": 
			secondPositionbtn.setText("O");
			secondPositionbtn.setEnabled(false);
			nextTurn(true);
			checkWinCondition();
			break;
			
		case "thirdPositionButtonCliked": 
			thirdPositionbtn.setText("O");
			thirdPositionbtn.setEnabled(false);
			nextTurn(true);
			checkWinCondition();
			break;
			
		case "fourthPositionButtonCliked": 
			fourthPositionbtn.setText("O");
			fourthPositionbtn.setEnabled(false);
			nextTurn(true);
			checkWinCondition();
			break;
			
		case "fifthPositionButtonCliked": 
			fifthPositionbtn.setText("O");
			fifthPositionbtn.setEnabled(false);
			checkWinCondition();
			nextTurn(true);
			break;
			
		case "sixthPositionButtonCliked": 
			sixthPositionbtn.setText("O");
			sixthPositionbtn.setEnabled(false);
			checkWinCondition();
			nextTurn(true);
			break;
			
		case "seventhPositionButtonCliked": 
			seventhPositionbtn.setText("O");
			seventhPositionbtn.setEnabled(false);
			checkWinCondition();
			nextTurn(true);
			break;
			
		case "eighthPositionButtonCliked": 
			eighthPositionbtn.setText("O");
			eighthPositionbtn.setEnabled(false);
			checkWinCondition();
			nextTurn(true);
			break;
			
		case "ninthPositionButtonCliked": 
			ninthPositionbtn.setText("O");
			ninthPositionbtn.setEnabled(false);
			checkWinCondition();
			nextTurn(true);
			break;
			
		case "OpponentWasDisconnectFromServer-NeedReconnect":
			JOptionPane.showMessageDialog(frame, "Your opponent disconnected.\n You will be paired up with another client", "Oops. The Opponent ran away!", JOptionPane.WARNING_MESSAGE);
			cleanBoard();
			break;
			
		default:
			listModel.addElement("User: " + obj.getUser() + ". Date: " + obj.getTimeStamp());
			listModel.addElement("Message: " + obj.getMsg());
		}
	}
}
