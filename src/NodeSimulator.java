
package terminationDetection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Termination Detection
 * NodeSimulator.java
 * Purpose: To simulate the events on the node from a file.
 */
enum Events {
	INITIATOR,
	SEND
}
public class NodeSimulator {
	Node node;
	int portNo;


	/**
	 * This method is used to simulate the events 
	 * on the node from the file. 
	 * @param file - path to the file name
	 * @param nodeId - identifier of the node
	 */
	public void execute(String file,int nodeId) throws IOException, InterruptedException {

		String event;
		/** calling the node class to set up the node propertied and start up the server and client of that node */
		node = new Node(nodeId);
		node.setup(node);
		BufferedReader buffer = new BufferedReader(new FileReader(file));
		while( (event = buffer.readLine()) != null) {
			String eventComponents [] = event.split(" ");
			if(nodeId == Integer.parseInt(eventComponents[0])) {
				
				/** Initiate Event */
				if(eventComponents[1].equals(Events.INITIATOR.name())) {
					System.out.println("Node " +nodeId+" Initiated the Termination Detection Algorithm");
					node.setParent(nodeId);
					node.setState(State.ACTIVE);
				}

				/** Send Event */
				else if (eventComponents[1].equals(Events.SEND.name())) {
					/** Checking if node active if not waits until active */
					while(true) {
						if(node.isActive()) {
							break;
						}
						else {
							Thread.sleep(5);
						}
					}
					System.out.println("_____________SEND EVENT__________________");
					int destinationNodeId = Integer.parseInt(eventComponents[2]);
					node.sendMessage(nodeId, destinationNodeId,eventComponents[1]);
				}
			}
		}
		/** Set state to inactive if all the required events are done by node */
		node.setState(State.INACTIVE);
		buffer.close();
	}

	/**
	 * The main method begins here  .
	 * @param args[0] - path to the file
	 * @param args[1] - Identifier for the node
	 * @author - deepakrtp 			
	 */
	public static void main(String args[]) throws IOException, InterruptedException {

		NodeSimulator nodeSimulator = new NodeSimulator();
		nodeSimulator.execute(args[0],Integer.parseInt(args[1]));
	}
}