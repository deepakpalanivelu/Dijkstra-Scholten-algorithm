package terminationDetection;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
public class Server {
	EventLoopGroup bossGroup;
	EventLoopGroup workerGroup;
	ChannelFuture future;
	Server(int port, Clock clock,Node node) throws InterruptedException {

		bossGroup = new NioEventLoopGroup(1);
		workerGroup = new NioEventLoopGroup(2);

		final Clock clock1 = clock;
		final Node nodes = node;
		ServerBootstrap server = new ServerBootstrap();
		server.group(bossGroup,workerGroup);
		server.channel(NioServerSocketChannel.class);
		server.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				// TODO Auto-generated method stub
				ch.pipeline().addLast(new ObjectEncoder(),new ObjectDecoder(ClassResolvers.weakCachingResolver(Message.class.getClassLoader())),new ServerHandler(clock1,nodes));
			}
		});
		server.option(ChannelOption.SO_BACKLOG, 128);
		server.childOption(ChannelOption.SO_KEEPALIVE, true);
		future = server.bind(port).sync();
		if(future.isSuccess()) {
			System.out.println("Server with port number " +port +" started");
		}
	}
}

