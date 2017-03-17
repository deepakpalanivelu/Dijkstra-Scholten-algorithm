package terminationDetection;


import io.netty.channel.ChannelFuture;

public class Client {

	Message message;
	ChannelFuture future;
	Node node;
	Client(Node node) {
		message = new Message();
		this.node = node;
	}

	public void sendMessage (ChannelFuture future,int destination_Node,int nodeID,int clockTime,String msg) {
		System.out.println("Sending message to node " + destination_Node);
		message.setSender(nodeID);
		message.setClockTime(clockTime);
		message.setMessage(msg);
		node.setMsgCount(node.getMsgCount() + 1);
		future.channel().writeAndFlush(message);
		System.out.println("Message sent");
	}
}


