/**
 * ServerGUI
 * It creates a frame that holds a server that monitors the game traffic and client activity 
 * from those who connect to this server.
 */
package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

/**
 * @author 755340 - Luis Ojeda
 * @version 1.0
 * October 1, 2019
 */
public class ServerGUI {
	//Attributes
	private JFrame serverFrame;
	private Container content;
	private DefaultListModel<String> listModel;
	private JList<String> lstGameTraffic;
	
	/**
	 * Default Constructor
	 */
	
	public ServerGUI() {
		initialize();
	}
	
	
	/**
	 * initialize  creates the new JFrame for the server and calls the startServer method
	 */
	private void initialize() {
		serverFrame = new JFrame("TicTacToe server");
		serverFrame.setSize(610, 530);
		serverFrame.setLocationRelativeTo(null);  
		
		content = serverFrame.getContentPane();
		
		content.add(getMainPanel());
		serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		serverFrame.setVisible(true);
		startServer();
	}
	
	/**
	 * startServer creates a server sockets and initializes with port 3333. 
	 * It waits for two clients to connect and puts them together, clearing up the socketList for the next two clients.
	 * It monitors the traffic of clients connecting and being paired up.
	 */
	private void startServer() {
		ServerSocket server = null;
		Socket socket = null;
		List<Socket> socketList = new ArrayList<Socket>();
		
		try {
			server = new ServerSocket(3333);
			listModel.addElement(new Date() + ":  Server is up and running!");
			listModel.addElement(new Date() + ":  Waiting for players to connect.");
			listModel.addElement("----------------------------------------------------------------------------------------------------------------------------");
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		while(true){
			try {
				socket = server.accept();
				socketList.add(socket);
				
				if(socketList.size()==1) {
					listModel.addElement(new Date() + ":  Accepted first player connection.");
				}
				
				if(socketList.size()==2) {
					listModel.addElement(new Date() + ":  Accepted second player connection.");
					listModel.addElement(new Date() + ":  Two players were paired up! Starting a TicTacToe game.");
					listModel.addElement(" ");
					ClientHandler ch = new ClientHandler(socketList.get(0), socketList.get(1));
					Thread t = new Thread(ch);
					t.start();
					socketList.clear();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}

	/*****************************************************************************************************************************************
 	* Construction of the GUI panels.
 	* 
 	******************************************************************************************************************************************/
	
	/**
	 * getMainPanel gets the whole panel that it's used in this frame .
	 * It is divided in two sections.
	 * @return a JPanel with that calls other JPanels.
	 */
	private JPanel getMainPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(getNorthPanel(), BorderLayout.NORTH);
		panel.add(getCenterPanel(), BorderLayout.CENTER);
		return panel;
	}

	/**
	 * getNorthPanel method creates a JPanel that holds a JLabel as a title for the monitoring of the server.
	 * It's located at the top of the frame
	 * @return panel a JPanel with the north part of the frame
	 */
	private JPanel getNorthPanel() {
		JPanel panel = new JPanel();
		Border edge = BorderFactory.createEtchedBorder();
		Box vBox = Box.createVerticalBox();
		
		JLabel label = new JLabel ("User's traffic");
		label.setForeground(Color.BLUE);
		label.setFont(new Font("Times New Roman", Font.BOLD,25));
		
		vBox.add(Box.createVerticalStrut(10));
		vBox.add(label);
		vBox.add(Box.createVerticalStrut(15));
		
		panel.add(vBox);
		panel.setBorder(edge);
		return panel;
	}

	/**
	 * getCenterPanel method creates a JPanels that creates a List model that 
	 * monitors the game traffic with time include of every action that it is
	 * happening within the clients and the server.
	 * @return panel a JPanel with the list model that holds the game traffic
	 */
	private JPanel getCenterPanel() {
		JPanel panel = new JPanel();
		
		listModel = new DefaultListModel<String>();
		lstGameTraffic = new JList<String>(listModel);
		
		JScrollPane scrollPane = new JScrollPane(lstGameTraffic);
		
		lstGameTraffic.setFixedCellWidth(500);
		lstGameTraffic.setVisibleRowCount(25);
		
		panel.add(scrollPane);
		return panel;
	}
}
