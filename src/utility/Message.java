/**
 * Message
 * Creates an object that it's used to transport users messages 
 * between clients on a server. 
 */
package utility;

import java.io.*;
import java.util.*;

/**
 * Original by: 
 * @author dwatson
 * @version 1.0
 * Sep 8, 2008
 * 
 * Provided as common-use code by:
 * Maryam Moussavi - Advanced Object-Oriented Programming (CPRG-311) instructor
 * September, 2019
 * 
 * Used by:
 * 000755340 - Luis Ojeda
 * October 1, 2019.
 */
public class Message implements Serializable {
	
	//Constants
	static final long serialVersionUID = 3216948875178844229L;
	
	//Attributes
	private String 	user;
	private String	msg;
	private Date	timeStamp;
	
	//Constructors
	public Message() { }
	
	public Message(String user, String msg, Date timeStamp) {
		this.user = user;
		this.msg = msg;
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the timeStamp
	 */
	public Date getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	//Operational Methods
	public String toString() {
		return "User: "+ user + "  "+"\nDate: "+ timeStamp +
				"\n Message: " + msg;
	}
}
