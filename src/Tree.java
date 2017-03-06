package djikstraSchloten;


public class Tree {
	int nodeID;
	String state;
	int parentNode;
	int ackCount;
	int messageCount;
	Tree() {
		nodeID = 0;
		state = "IDLE";
		parentNode = -1;
		ackCount = 0;
		messageCount = 0;
	}
	
	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}
	public int getNodeID() {
		return this.nodeID;
	}
	public int getMsgCount() {
		return this.messageCount;
	}
	public void setMsgCount(int messageCount) {
		this.messageCount = messageCount;
	}
	
	public int getAckCount() {
		return this.ackCount;
	}
	public void setAckCount(int ackCount) {
		this.ackCount = ackCount;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getState() {
		return this.state;
	}
	
	public void setParent(int parentNode) {
		this.parentNode = parentNode;
	}
	public int getParent() {
		return this.parentNode;
	}
	
	public boolean isNodeActive() {
		if(this.getParent() != -1) {
			return true;
		}
		else {
			return false;
		}
	}
}