package terminationDetection;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class ClientHandler extends SimpleChannelInboundHandler<Message> {

	Node node;
	ClientHandler(Node node) {
		this.node = node;
	}
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		System.out.println("_________________________");
		System.out.println("ACK RECIEVED FROM" + msg.getSender());
		node.setAckCount(node.getAckCount() + 1);
		node.checkIdle();	
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}

