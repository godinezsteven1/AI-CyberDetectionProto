package com.cybersecurity;

import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.layers.AutoEncoder;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * class {@code AnamalyDetector} detects Anomalies from Multi layer network and those protocol
 * decoding and encoding of traffic data
 */
class AnomalyDetector {

  // cool new deep learn model :D
  private MultiLayerNetwork network;

  /**
   * Constructor detects anomalies bu comparing reconstruction
   */
  public AnomalyDetector() {
    MultiLayerConfiguration config =new NeuralNetConfiguration.Builder()
            .seed(123)
            .list()
            .layer(0, new DenseLayer.Builder().nIn(100).nOut(64).build()) // Input layer
            .layer(1, new AutoEncoder.Builder().nIn(64).nOut(32).build()) // Encoder layer
            .layer(2, new DenseLayer.Builder().nIn(32).nOut(64).build())  // Decoder layer
            .layer(3, new DenseLayer.Builder().nIn(64).nOut(100).build()) // Output layer
            .build();
    network = new MultiLayerNetwork(config);
    network.init();
  }

  /**
   * Anomaly checker.
   * @param dataTraffic network traffic dta.
   * @return if calculated error overwhelms our error
   */
  public boolean checkForAnomalies(String dataTraffic) {
    INDArray in = convertIND(dataTraffic);
    INDArray reconst = network.output(in);

    double calculatedError = in.distance2(reconst);
    return calculatedError > 0.5;
    // how sensitive should we want...
    // tweak after training model
  }

  /**
   * Network traffic to INDArray which is just compatible with neural network to work
   * as an input.
   * @param data network traffic data as string "IP destination Port Protocol size".
   * @return an INDArray with netowrk traffic info.
   */
  private static INDArray convertIND(String data) {
    String[] parts = data.split(" "); // add spaces to data

    double sourceIP = encodeIP(parts[0]);
    double destinationIP = encodeIP(parts[1]);
    double portNumber = Double.parseDouble(parts[2]);
    double protoc = encodeProtocol(parts[3]);
    double size = Double.parseDouble(parts[4]);

    // how we are going to track the data
    double[] newData = new double[] {sourceIP, destinationIP, portNumber, protoc, size};
    return Nd4j.create(newData);
  }

  /**
   * IP adress encoder to num.
   * @param ip address in typical format.
   * @return number IP address.
   */
  private static double encodeIP(String ip) {
    String[] octets = ip.split("\\.");
    long result = 0;
    for (int v = 0; v < 4; v++) {
      result |= (Long.parseLong(octets[v]) << (24 - (8 * v)));
    }
    return (double) result;
  }

  /**
   * encodes protocol as a double.
   * @param protocol current network name like tcp.
   * @return numerical representation of our protocol.
   */
  private static double encodeProtocol(String protocol) {
    switch (protocol.toLowerCase()) {
      case "tcp": return 1.0;
      case "udp": return 2.0;
      case "icmp": return 3.0;
      default: return 0.0; // basically if its it unknown
    }
  }

}