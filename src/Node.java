package djikstraSchloten;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Node {
	public void run() throws NumberFormatException, IOException, InterruptedException {
		int nodeID;
		String fromFile;
		Server server1 = new Server();
		Client client1 = new Client();
		String words[];
		Clock clock = new Clock();
		Tree tree = new Tree();
		Properties prop = new Properties();
		InputStream input = null;
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the Node ID:");
		nodeID = Integer.parseInt(inFromUser.readLine());
		tree.setNodeID(nodeID);

		try {
			input = getClass().getClassLoader().getResourceAsStream("config.properties");
			// load a properties file
			prop.load(input);
			int port = Integer.parseInt(prop.getProperty(Integer.toString(nodeID)));
			server1.run(port,clock,tree);
			BufferedReader buffer = new BufferedReader(new FileReader("Simulate.txt"));
			while( (fromFile = buffer.readLine()) != null) {
				words = fromFile.split(" ");
				if(nodeID == Integer.parseInt(words[0])) {
					if(words[1].equals("INITIATOR")) {
						System.out.println("Initiator");
						System.out.println("Node " +nodeID+" Initiated the Termination Detection Algorithm");
						tree.setParent(nodeID);
						tree.setState("Active");
						System.out.println(" State = " + tree.getState());
					}
					// waiting for the node to be active 
					while(true) {
						if(tree.isNodeActive()) {
							System.out.println(tree.isNodeActive());
							break;
						}
						else {
							Thread.sleep(1000);
						}
					}

					if(words[1].equals("SEND")) {
						System.out.println("___________SEND EVENT_________________");
						System.out.println("Before the event  clock value = " + clock.getClockTime());
						int destination_Node = Integer.parseInt(words[2]);	
						int destination_port = Integer.parseInt(prop.getProperty(Integer.toString(destination_Node)));
						clock.tick(clock.getClockTime());
						client1.run(nodeID,destination_Node,destination_port,tree, clock.getClockTime(),words[1]);
						tree.setMsgCount(tree.getMsgCount() + 1);
						System.out.println("Clock after the event" + clock.getClockTime());
					}

					else if(words[1].equals("IDLE")) {
						while(true) {
							/* checking if the child of this node is active 
							 * if active this node cannot be idle and has to wait for the child node to be inactive
							 */
							if(tree.getMsgCount() == tree.getAckCount() && tree.getMsgCount() != 0 && tree.getAckCount() != 0) {
								break;
							}
							else {
								Thread.sleep(10);
							}
						}
						System.out.println("Entering idle state");
						int parentID = tree.getParent();
						int parent_port = Integer.parseInt(prop.getProperty(Integer.toString(parentID)));
						System.out.println("The Parent for the node "+nodeID+  " is =" + parentID);

						if(nodeID != parentID) {
							System.out.println("Clock before the event" + clock.getClockTime());
							clock.tick(clock.getClockTime());
							client1.run(nodeID,parentID,parent_port,tree, clock.getClockTime(),words[1]);								
							System.out.println("Clock after the event" + clock.getClockTime());
							tree.setParent(-1);
							tree.setState("IDLE");
							System.out.println("NODE " + nodeID+ " IS IDLE");
						}

						else {
							clock.tick(clock.getClockTime());
							tree.setParent(-1);
							tree.setState("IDLE");
							System.out.println("TERMINATION DETECTION FINISHED");
						}
					}
				}
			}
			buffer.close();
			server1.future.channel().closeFuture().sync();
			client1.future.channel().closeFuture().sync();
		}
		finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			server1.workerGroup.shutdownGracefully();
			server1.bossGroup.shutdownGracefully();
			client1.workerGroup.shutdownGracefully();
		}
	}

	public static void main(String [] args) throws NumberFormatException, InterruptedException, IOException {
		Node node = new Node();
		node.run();
	}
}




