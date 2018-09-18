import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;

public class EchoServer {

    private static final int PORT = Integer.parseInt("8090");

    public static void main(String args[]) throws Exception {

        new EchoServer().start();
    }

    private void start() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .localAddress(new InetSocketAddress(PORT))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            ChannelPipeline cp = socketChannel.pipeline();
                            cp.addLast(new LineBasedFrameDecoder(1024));
                            cp.addLast(new StringDecoder());
                            cp.addLast(new EchoServerHandler());
                        }
                    });

            ChannelFuture future = b.bind().sync();
            future.channel().closeFuture().sync();
        }finally {

            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
