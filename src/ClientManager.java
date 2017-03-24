package terminationDetection;


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
import java.util.HashMap;
import java.util.Map;

public class ClientManager {

	Message message;
	private Map<Integer,ChannelFuture> channelMap;
	EventLoopGroup workerGroup;
	ChannelFuture future;
	Node node;
	Client client;
	ClientManager(Node node) {
		this.node = node;
		workerGroup =  new NioEventLoopGroup();
		channelMap = new HashMap<Integer,ChannelFuture>(); 
		client = new Client(node);

	}
	/**
	 * This method is used to connect to the the other node and send the message 
	 * if the connections already exist gets the channel and then sends
	 * @param destinationNode - recipient node
	 * @param destinationPort - Port number on which the destination Node listens
	 * @param clockTime - The Lamport clock value is piggybacked with the message
	 * @param msg - The message to be sent.
	 */
	public void execute(int nodeID,int destinationNode, int destinationPort, int clockTime,String msg)  throws InterruptedException {
		if(channelMap.containsKey(destinationNode)) {
			future = channelMap.get(destinationNode);
			client.sendMessage(future,destinationNode,nodeID,clockTime,msg);
		}
		else { 

			final Node nodes = node;
			Bootstrap clientbootstrap = new Bootstrap();
			clientbootstrap.group(workerGroup);
			clientbootstrap.channel(NioSocketChannel.class);
			clientbootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ObjectEncoder(),new ObjectDecoder(ClassResolvers.weakCachingResolver(Message.class.getClassLoader())),new ClientHandler(nodes));
				}
			});
			Thread.sleep(10000);
			future = clientbootstrap.connect("localhost", destinationPort).sync();
			if(future.isSuccess()) {
				System.out.println("Connected to node " +destinationNode + " port is "+ destinationPort);
			}
			channelMap.put(destinationNode,future);
			client.sendMessage(future,destinationNode,nodeID,clockTime,msg);
		}
	}
}
