package com.cybersec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;

/**
 * The {@code TrafficSimulator} class is used to simulate a network traffic environment
 * and to use my handler and detector class to see compatability.
 */
public class TrafficSimulator {

  public static void main(String[] args) throws Exception {
    TrafficHandler simHandler = new TrafficHandler();
    EmbeddedChannel simChannel = new EmbeddedChannel();

    /**
     * lets create our traffic of what we expect which include
     * - Normal
     * - anomalies (random payload data)
     */

    // normal
    simulateTraffic(simChannel, simHandler, "192.168.0.1 10.0.0.1 8080 tcp 1024");
    // normal
    simulateTraffic(simChannel, simHandler, "192.168.1.1 10.0.0.2 443 tcp 512");
    // normal
    simulateTraffic(simChannel, simHandler, "10.0.0.5 172.16.0.1 80 http 256");
    // anomaly (not trained yet but payload data pls random port
    simulateTraffic(simChannel, simHandler, "172.16.254.1 10.0.0.2 9090 udp 2048");

  }

  private static void simulateTraffic(EmbeddedChannel channel, TrafficHandler handler,
                                      String dataTraffic) throws Exception {
    ChannelHandlerContext simChannel = channel.pipeline().firstContext();

    // further the simulator visuals i guess
    System.out.println("Simulating traffic: " + dataTraffic);
    handler.channelRead(simChannel, dataTraffic); // Feed traffic into the handler
  }
}
