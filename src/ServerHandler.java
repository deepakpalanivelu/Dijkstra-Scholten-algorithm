package djikstraSchloten;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<Message> {
	Clock clock;
	Tree tree;
	String state;
	ServerHandler(Clock clock,Tree tree) {
		this.clock = clock;
		this.tree = tree;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);

	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub

		super.channelInactive(ctx);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		// TODO Auto-generated method stub
		if(msg.getMessage().equals("IDLE")) {
			System.out.println("Ack recieved from child  "+ msg.getSender());
			tree.setAckCount(tree.getAckCount() + 1);
			System.out.println("in node" + tree.getNodeID() + " message sent count = " + tree.getMsgCount() + " acknowlwdge count = " + tree.getAckCount()  );

		}
		else {
			System.out.println("clock before updating" +clock.getClockTime());
			System.out.println("_____________RECIEVE EVENT______________");
			System.out.println("-----------------------------------------");
			System.out.println("message recieved from " + msg.getSender() +" at = " + msg.getClockTime());
			System.out.println("State ");
			clock.tick(msg.getClockTime());

			if(tree.getState().equals("ACTIVE")) {
				msg.setSender(tree.getNodeID());
				msg.setMessage("ACK");
				ctx.writeAndFlush(msg);
			}
			else if(tree.getState().equals("IDLE")) {
				tree.setParent(msg.getSender());
				System.out.println("from server Parent =" + tree.getParent());
				tree.setState("ACTIVE");
			}
		}
		System.out.println("clock after updating" + clock.getClockTime());
	}

	@Override 
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}


