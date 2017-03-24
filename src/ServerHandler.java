package terminationDetection;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<Message> {
	Clock clock;
	Node node;
	String state;

	ServerHandler(Clock clock, Node node) {
		this.clock = clock;
		this.node = node;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		if(msg.getMessage().equals("Ack")) {
			node.setAckCount(node.getAckCount() + 1);
			System.out.println("Ack recieved from child  "+ msg.getSender());
			/** Checking if node idle */  
			node.checkIdle();
		}

		else {
			System.out.println("Clock before the event" +clock.getClockTime());
			System.out.println("_____________RECIEVE EVENT______________");
			System.out.println("-----------------------------------------");
			System.out.println("message recieved from " + msg.getSender() +" with time stamp  " + msg.getClockTime());
			clock.tick(msg.getClockTime());
			System.out.println("Clock after the event " +clock.getClockTime());
			channelReadComplete(ctx,msg);
		}
	}
	
	public synchronized void channelReadComplete(ChannelHandlerContext ctx,Message msg) throws Exception {
		// TODO Auto-generated method stub
		if(node.getState().equals(State.IDLE.name())) {
			/* if the node was idle it won't send the ack immediately */
			node.setParent(msg.getSender());
			System.out.println(node.getNodeId() + " Parent = "  + node.getParent());
			node.setState(State.ACTIVE);
		}
		else {
			/* if the node is active it sends Ack immediately */
			msg.setSender(node.getNodeId());
			msg.setMessage("ACK");
			ctx.writeAndFlush(msg);		
			System.out.println("Ack Sent to " + node.getNodeId());
		}
	}
	
	@Override 
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}


