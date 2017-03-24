package terminationDetection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
enum State {
	ACTIVE,
	INACTIVE,
	IDLE
}
public class Node {
	private int nodeId;
	private String state;
	private int parentNode;
	private int portNo;
	private int msgCount;
	private int ackCount;
	Server server;
	ClientManager clientManager;
	Node node;
	Clock clock;
	Properties prop = new Properties();
	InputStream input = null;
	Node(int nodeId) {
		msgCount = 0;
		ackCount = 0;
		state = State.IDLE.name();
		parentNode = -1;
		this.nodeId = nodeId;
		input = getClass().getClassLoader().getResourceAsStream("config.properties");
		clock = new Clock();
	}
	
	/**
	* This method is used to set up server and client.
	*/
	public void setup(Node node) throws InterruptedException, IOException {

		this.node = node;
		prop.load(input);
		portNo = Integer.parseInt(prop.getProperty(Integer.toString(nodeId)));
		server = new Server(portNo,clock,node);
		clientManager = new ClientManager(node);
	}
	
	/**
	* This method asks the client to    
	* send a message to the mentioned node.
	* @param nodeId - Identifier of the node
	* @param destinationNode - Recipient of the message
	* @param msg - Contents of the message to be sent
	*/
	public void sendMessage(int nodeId ,int destinationNode,String msg) throws InterruptedException {

		System.out.println("Clock before the event " +clock.getClockTime());
		int destinationPort = Integer.parseInt(prop.getProperty(Integer.toString(destinationNode)));
		clock.tick(clock.getClockTime());
		clientManager.execute(nodeId,destinationNode,destinationPort,clock.getClockTime(),msg);
		System.out.println("Clock After the event " +clock.getClockTime());
		
	}
	
	/**
	* This method checks if the node is idle or not.
	* If the node that initiated the process is idle then termination is detected.
	* @throws InteruptedException
	*/
	public void checkIdle () throws InterruptedException {
		if(node.getAckCount() == node.getMsgCount() && node.getState().equals(State.INACTIVE.name())) {
			if(node.getNodeId() != node.getParent()) {
				node.sendMessage(nodeId,getParent(),"Ack");
				node.setState(State.IDLE);
				node.setParent(-1);
				System.out.println("node " + node.getNodeId() + " is idle");

			}
			else {
				System.out.println(" Termination Detection Finished ");
			}
		}
	}
	
	/**
	* Method to get the identifier of the node
	* @return nodeId - identifier of the node
	*/
	public int getNodeId() {
		return this.nodeId;
	}
	
	/**
	* Method to set the number of messages sent from this node.
	* @param msgCount - Message sent count
	*/
	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}
	
	/**
	* Method to set the number of Acknowledge received
	* @param ackCount - Acknowledge count 
	*/
	public void setAckCount(int ackCount) {
		this.ackCount = ackCount;
	}
	
	/**
	* Method to get the number of messages sent from this node.
	* @return msgCount - Message sent count
	*/
	public int getMsgCount() {
		return this.msgCount;
	}
	
	/**
	* Method to get the number of Acknowledge received
	* @return ackCount - Acknowledge count 
	*/
	public int getAckCount() {
		return this.ackCount;
	}
	
	/**
	* Method to set the state of node
	* @param State - denotes the state of the node.
	*/
	public  void setState(State state) {
		this.state = state.name();
	}
	
	/**
	* Method to get the state of the node
	* 	* @return state - state of the node 
	*/
	public  String getState() {
		return this.state;
	}
	
	/**
	* Method to check if the node is active
	* @return true - if active
	* @return false - if not active 
	*/
	public boolean isActive() {
		if(parentNode == -1) {
			return false;
		}
		return true;
	}
	
	/**
	* Method to set the parent of the node
	* @param parentNode - identifier of the parentNode
	*/
	public   void setParent( int parentNode) {
		if(this.parentNode == -1) {
			this.parentNode = parentNode;
		}
	}
	
	/**
	* Method to get the parent of the node
	* @return parentNode - identifier of the parentNode 
	*/
	public int getParent() {
		return this.parentNode;
	}
}

