package terminationDetection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
		state = "IDLE";
		parentNode = -1;
		this.nodeId = nodeId;
		input = getClass().getClassLoader().getResourceAsStream("config.properties");
		clock = new Clock();
	}
	public void setup(Node node) throws InterruptedException, IOException {

		this.node = node;
		prop.load(input);
		portNo = Integer.parseInt(prop.getProperty(Integer.toString(nodeId)));
		server = new Server(portNo,clock,node);
		clientManager = new ClientManager(node);
	}
	
	public void sendMessage(int nodeId ,int destinationNode,String msg) throws InterruptedException {

		System.out.println("Clock before the event " +clock.getClockTime());
		int destinationPort = Integer.parseInt(prop.getProperty(Integer.toString(destinationNode)));
		clock.tick(clock.getClockTime());
		clientManager.run(nodeId,destinationNode,destinationPort,clock.getClockTime(),msg);
		System.out.println("Clock After the event " +clock.getClockTime());
		
	}

	public void checkIdle () throws InterruptedException {
		if(node.getAckCount() == node.getMsgCount() && node.getState().equals("Inactive")) {
			if(node.getNodeId() != node.getParent()) {
				node.sendMessage(nodeId,getParent(),"Ack");
				node.setState("IDLE");
				node.setParent(-1);
				System.out.println("node " + node.getNodeId() + " is idle");

			}
			else {
				System.out.println(" Termination Detection Finished ");
			}
		}
	}

	public int getNodeId() {
		return this.nodeId;
	}
	
	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}

	public void setAckCount(int ackCount) {
		this.ackCount = ackCount;
	}

	public int getMsgCount() {
		return this.msgCount;
	}

	public int getAckCount() {
		return this.ackCount;
	}

	public synchronized void setState(String state) {
		this.state = state;
	}

	public synchronized String getState() {
		return this.state;
	}

	public boolean isActive() {
		if(parentNode == -1) {
			return false;
		}
		return true;
	}
	public  synchronized void setParent( int parentNode) {
		if(this.parentNode == -1) {
			this.parentNode = parentNode;
		}
	}
	public int getParent() {
		return this.parentNode;
	}
}

