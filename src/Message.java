package djikstraSchloten;

import java.io.Serializable;

public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	private int clockTime;
	private int Sender_nodeID;
	private String msg;

	public void setSender(int Sender_nodeID) {
		this.Sender_nodeID = Sender_nodeID;
	}
	
	public void setMessage(String msg) {
		this.msg = msg;
	}
	
	public String getMessage(){
		return this.msg;
	}

	public int getClockTime() {
		return this.clockTime;
	}

	public int getSender() {
		return this.Sender_nodeID;
	}

	public void setClockTime(int clockTime) {
		this.clockTime = clockTime;
	}
}

