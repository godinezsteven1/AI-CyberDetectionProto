package com.cybersec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.net.InetSocketAddress;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/*
The {@code TrafficHandler} handles the traffic and serves as a feeder to the ML detection
aka our control...
 */
public class TrafficHandler {

  /**
   * Monitors our traffic and feeds it to ML and does basic actions
   * @param c represents our Channel handler context
   * @param msgs represents information either byts, etc..
   * @throws Exception if fails.
   */
  public void channelRead (ChannelHandlerContext c, Object msgs) throws Exception {
    String data = msgs.toString(); // incoming traffic data to something we can read!
    AnomalyDetector tempDetector = new AnomalyDetector();
    boolean isAnomaly = tempDetector.checkForAnomalies(data); // feedeeerrrrrrr
    InetSocketAddress rAddy = (InetSocketAddress) c.channel().remoteAddress();
    String foreignIP = rAddy.getAddress().getHostAddress();
    if (isAnomaly) {
      System.out.println(data + " Danger Detected Proceeding with blocking IP " +
              foreignIP);
      c.close();
      blockTrackIP(foreignIP);
    } else {
      System.out.println("No Harmful Traffic Detected: " + foreignIP);
    }
  }

  /**
   *
   * @param c interact with channel
   * @param cause provides what info went wrong
   */
  public void exceptionCaught(ChannelHandlerContext c, Throwable cause) {
    cause.printStackTrace();
    c.close();
  }

  // Optionally, implement a method to block or track IP addresses
  private void blockTrackIP(String ipAdress) {
    // Connect to Mongo
    try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

      // get into data base
      MongoDatabase db = mongoClient.getDatabase("AI-CyberDetectionProto");
      MongoCollection<Document> collection = db.getCollection("blockedIPs");

      Document pastForeignAnomalies = new Document("IP", ipAdress) // new doc
              .append("blockedAt", System.currentTimeMillis());
      collection.insertOne(pastForeignAnomalies); // insert

      System.out.println("Blocked IP stored in MongoDB: " + ipAdress);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

/**
 * In order to find my Database with the blocked Ips well open terminal
 * >> mongosh
 * >> show dbs
 * >> use AI-CyberDetectionProto // as written in MongoDatabase db = ...("AI-CyberDetectionProto");
 * >> db.blockedIPs.find().pretty()
 */
