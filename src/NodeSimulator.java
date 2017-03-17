package terminationDetection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class NodeSimulator {
	Node node;
	private int nodeId;
	int portNo;

	public void run() throws IOException, InterruptedException {


		String fromFile , split[];
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		System.out.println(" Enter the node id :");
		nodeId = Integer.parseInt(inFromUser.readLine());


		// calling the node to start up the server and client 
		node = new Node(nodeId);
		node.setup(node);

		// to read from file 
		BufferedReader buffer = new BufferedReader(new FileReader("/Users/deepakrtp/Documents/Consistency/Terminationdetection/src/main/resources/Simulate.txt"));
		while( (fromFile = buffer.readLine()) != null) {
			split = fromFile.split(" ");
			if(nodeId == Integer.parseInt(split[0])) {

				// Initiate event
				if(split[1].equals("INITIATOR")) {
					System.out.println("Node " +nodeId+" Initiated the Termination Detection Algorithm");
					node.setParent(nodeId);
					node.setState("Active");
				}
				while(true) {
					if(node.isActive()) {
						break;
					}
					else {
						Thread.sleep(5);
					}
				}

					// Send event
				if(split[1].equals("SEND")) {
					System.out.println("_____________SEND EVENT__________________");
					int destinationNodeId = Integer.parseInt(split[2]);
					node.sendMessage(nodeId, destinationNodeId,split[1]);
				}
			}
		}
		node.setState("Inactive");
		buffer.close();
	}
	public static void main(String args[]) throws IOException, InterruptedException {
		NodeSimulator nodeSimulator = new NodeSimulator();
		nodeSimulator.run();
	}
}