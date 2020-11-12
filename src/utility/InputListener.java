/**
 * InputListener
 * Establishes the relationship between an socket, its id, 
 * and the PropertyChangeListener that they correspond to.
 */
package utility;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import server.ClientHandler;

/**
 * @author 755340 - Luis Ojeda
 * @version 1.0
 * October 1, 2019
 */
public class InputListener implements Runnable{

	//Attributes
	private int id;
	private Socket socket;
	private List<PropertyChangeListener> listenerList = new ArrayList<>();
	private ObjectInputStream ois;
	
	/**
	 * @param id the id of the client
	 * @param socket the socket of the client 
	 * @param listener the property to be changed.
	 */
	public InputListener(int id, Socket socket, PropertyChangeListener listener) {
		this.id = id;
		this.socket = socket;
		listenerList.add(listener);
	}

	
	/**
	 * @param socket the socket of the client 
	 * @param listener the property to be changed.
	 */
	public InputListener(Socket socket, PropertyChangeListener listener) {
		this.socket = socket;
		listenerList.add(listener);
	}
	
	/**
	 * gets the Id of the client that is sending the request 
	 * of a property change.
	 * @return the id of the client
	 */
	public int getId() {
		return id;
	}


	/**
	 * 
	 * @param id The id of the client
	 */
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void run() {
		Object o;
		
		try {
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e){
			
		}
		
		while(true){
			try {
				o =(Object) ois.readObject();
				notifyListener(o);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException | NullPointerException e) {
				
			}
		}
		
	}

	/**
	 * notifyListeners method makes the the calling source Observable.
	 * @param obj the obj to be changed
	 */
	private void notifyListener(Object obj) {
		for( PropertyChangeListener listener : listenerList ){
			listener.propertyChange(new PropertyChangeEvent(this, null, null, obj));
		}
		
	}
}
