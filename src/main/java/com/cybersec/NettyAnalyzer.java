package com.cybersec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/*
The Netty Analyzer will serve as our Intruison Detection System, better konwn as IDS by
viewing network packages
 */
public class NettyAnalyzer {

  private final int portInput;

  public NettyAnalyzer(int portInput) {
    this.portInput = portInput;
  }

  /*
  Runs the package grabber.
   */
  public void run() throws Exception {
    // handlers for connections also called worker group
    EventLoopGroup bossGroup = new NioEventLoopGroup(); // handles our IO exceptions
    EventLoopGroup workerGroup = new NioEventLoopGroup(); // handles non-blocking IO based network
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup) // handles our incoming connection request and traffic of conn
              .channel(NioServerSocketChannel.class) // supports the non-blocking I/o
              .handler(new LoggingHandler(LogLevel.INFO)) // logger
              .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                  ChannelPipeline p = ch.pipeline();
                  // handles our traffic analysis
                  // aka basis of our IDS, very important
                  p.addLast(new LoggingHandler(LogLevel.INFO));
                }
              });
      ChannelFuture future = b.bind(portInput).sync();
      future.channel().closeFuture().sync();
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }

  /*
   Starts server on following port, will listen to our connections in that port. It is well known
   (8080), easily accessible...
   -> Captures incoming traffic
   */
  public static void main(String[] args) throws Exception {
    new NettyAnalyzer(8080).run();  // set to common port, also avoids conflict
  }
}
