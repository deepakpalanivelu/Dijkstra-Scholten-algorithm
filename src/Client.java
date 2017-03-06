package djikstraSchloten;

import java.util.HashMap;
import java.util.Map;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class Client {

	Message data;
	private Map<Integer,ChannelFuture> channelMap; 
	EventLoopGroup workerGroup;
	ChannelFuture future;
	Client() {
		data = new Message();
		channelMap = new HashMap<Integer,ChannelFuture>();
		workerGroup = new NioEventLoopGroup();
	}

	public void sendMessage (ChannelFuture future,int destination_Node,int nodeID,int clockTime,String msg) {
		System.out.println("Sending message to " + destination_Node);
		data.setSender(nodeID);
		data.setClockTime(clockTime);
		data.setMessage(msg);
		future.channel().writeAndFlush(data);
	}


	public void run(int nodeID,int destination_Node, int destination_Port,Tree tree, int clockTime,String msg)  throws InterruptedException {
		if(channelMap.containsKey(destination_Node)) {
			future = channelMap.get(destination_Node);
			sendMessage(future,destination_Node,nodeID,clockTime,msg);
		}
		else {
			System.out.println("Connecting to port" + destination_Node);
			final Tree tree1 = tree;
			Bootstrap client = new Bootstrap();
			client.group(workerGroup);
			client.channel(NioSocketChannel.class);
			client.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ObjectEncoder(),new ObjectDecoder(ClassResolvers.weakCachingResolver(Message.class.getClassLoader())),new ClientHandler(tree1));
				}
			});
			Thread.sleep(10000);
			future = client.connect("localhost", destination_Port).sync();
			if(future.isSuccess()) {
				System.out.println("Connected to node " +destination_Node + " port is "+ destination_Port);
			}
			channelMap.put(destination_Node,future);
			sendMessage(future,destination_Node,nodeID,clockTime,msg);
		}
	}
}


