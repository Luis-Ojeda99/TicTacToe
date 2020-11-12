/**
 * ClientHandler
 * Manages all the information that ins being sent from one client to the other and
 * organizes who is receiving which actions.
 */
package server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import utility.InputListener;

/**
 * @author 755340 - Luis Ojeda
 * @version 1.0
 * October 1, 2019
 */
public class ClientHandler implements Runnable, PropertyChangeListener{

	//Attributes
	private Socket socket1;
	private Socket socket2;
	private ObjectOutputStream oos1;
	private ObjectOutputStream oos2;
	private InputListener inputListener1;
	private InputListener inputListener2;
		
	/**
	 * Constructor that holds the sockets of the two players that are being paired up
	 * @param socket1 socket of the first client to connect
	 * @param socket2 socket of the second client to connect
	 */
	public ClientHandler(Socket socket1, Socket socket2) {
		this.socket1 = socket1;
		this.socket2 = socket2;
	}
	
	@Override
	public void run() {
		inputListener1 = new InputListener(1, socket1, this);
		inputListener2 = new InputListener(2, socket2, this);
		Thread t1 = new Thread(inputListener1);
		t1.start();
		Thread t2 = new Thread(inputListener2);
		t2.start();
		
		try {
			
			oos1 = new ObjectOutputStream(socket1.getOutputStream());
			oos2 = new ObjectOutputStream(socket2.getOutputStream());
			while (socket1.isConnected() && socket2.isConnected());
			
			oos1.close();
			oos2.close();
			socket1.close();
			socket2.close();
		} catch (IOException e) {
			System.out.println("The user has disconected");
		}
	}

	@Override
	synchronized public void propertyChange(PropertyChangeEvent evt) {
		InputListener intlist = (InputListener) evt.getSource();
		Object newValue = evt.getNewValue();
		
		if(intlist.getId() == 1) {
			try {
				oos2.writeObject(newValue);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		else {
			try {
				oos1.writeObject(newValue);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
