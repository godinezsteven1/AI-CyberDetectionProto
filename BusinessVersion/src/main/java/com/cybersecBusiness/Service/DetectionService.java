package com.cybersecBusiness.Service;

import com.cybersecBusiness.Model.UserBehaviorModel;
import com.cybersecBusiness.Repo.UserBehaviorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smile.clustering.KMeans;
import smile.math.distance.EuclideanDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Enhanced {@code DetectionService} uses SMILE(machine learning) to enhance behavioral
 * analysis.
 */
@Service
public class DetectionService {

  //logger instance
  private static final Logger logger = LoggerFactory.getLogger(DetectionService.class);

  @Autowired
  private UserBehaviorRepo userBehaviorRepo;

  private KMeans model;
  private double[] clusterSize;
  private static final double ANOMALY_THRESHOLD = 2.0;
  private static final int MIN_TRAINING = 10; // min required to train.. sample size.
  private static final int DEFAULT_K = 3; // default for smile clust

  /**
   * Trains model using behavior data.
   * Updated for Smile 2.6.0
   * @return boolean wether training was successful.
   */
  public boolean trainModel() {
    // gets all data from repo
    List<UserBehaviorModel> behaviors = userBehaviorRepo.findAll();

    if (behaviors.size() < MIN_TRAINING) {
      logger.warn("not enough data to train. Need >>>: {}", MIN_TRAINING);
      return false;
    }

    try {
      // data -> array
      double[][] data = behaviors.stream()
              .map(this::extractNormalizedFeatures)
              .toArray(double[][]::new);

      // Determine optimal k
      int k = OptimalK(data);

      // Initialize KMeans with builder pattern (Smile 2.6.0 style)
      model = KMeans.fit(data, k, 100, // iterations
              new EuclideanDistance()); // hmm

      // Store cluster information
      updateClusterStat(data);
      logger.info("SMIL Trained successfully with >>>>: {}", k, " clusters") ;
      return true;

    } catch (Exception e) {
      logger.error("Failure to train,: {}", e.getMessage());
      return false;
    }
  }

  /**
   * Analyzes behavior using trained model.
   */
  public boolean isAnomalousTrained(UserBehaviorModel currentBehavior) {
    if (model == null) {
      throw new IllegalStateException("Model has not been trained yet :(");
    }

    // extract satellite data we need.
    double[] features = extractNormalizedFeatures(currentBehavior);

    // what cluster does feature belong in...     ** important **
    int cluster = model.predict(features);

    // how far from respective centroid (foundation of anomalies)
    double distanceToCentroid = calcEuclideanDist(features, model.centroids()[cluster]);

    // how far from work, what time and data packet sizes
    boolean isAnomalous = isDistAnomalous(distanceToCentroid, cluster) ||
            isTimeAnomalous(currentBehavior) ||
            isDataAnomalous(currentBehavior);

    logAnomalyDetection(currentBehavior, isAnomalous, distanceToCentroid, cluster);

    return isAnomalous;
  }

  /**
   * data -> normalized feature
   * @param behavior user behavior
   * @return double[] of user usable data.
   */
  private double[] extractNormalizedFeatures(UserBehaviorModel behavior) {
    double hour = behavior.getLoginTime().getHour() / 24.0; // convert time
    double rate = normalizeData(behavior.getAverageDataTransferRate());

    return new double[]{hour, rate};
  }

  //updates cluster stats based on data.
  private void updateClusterStat(double[][] data) {
    clusterSize = new double[model.k()];
    int[] labels = model.predict(data);

    for (int label : labels) {
      clusterSize[label]++;
    }
  }

  private boolean isDistAnomalous(double distance, int cluster) {
    return distance > ANOMALY_THRESHOLD * calcClusterDist(cluster);
  }

  private boolean isTimeAnomalous(UserBehaviorModel behavior) {
    int hour = behavior.getLoginTime().getHour();
    return (hour < 6 || hour > 20); // if before 6am or after 8pm flag it.
  }

  private boolean isDataAnomalous(UserBehaviorModel behavior) {
    double rate = behavior.getAverageDataTransferRate();
    return rate > calcDataThreshold();
  }

  private double normalizeData(double rate) {
    double maxRate = 1000.0;// ehh
    return Math.min(rate / maxRate, 1.0); //should not be less then 1
  }

  private double calcDataThreshold() {
    return 0.0;
    // STUB HOLDER, NEED TO TRAIN DATA TO DERMINE IF POINST IS FAR FORM CLUSTER CENTER. NEED TO
    // MAKE DYNAMIC BASED ON DATA SET TRAINED WITH
  }

  private double calcEuclideanDist(double[] point1, double[] point2) {
    double sum = 0.0;
    for (int i = 0; i < point1.length; i++) {
      sum += Math.pow(point1[i] - point2[i], 2);
    }
    return Math.sqrt(sum);
  }

  private int OptimalK(double[][] data) {
    return Math.min(DEFAULT_K, data.length / 3); // is this a good threshold?
  }

  private double calcClusterDist(int cluster) {
    return 0.5; // STUB HOLDER
    //  NEED TO TRAIN DATA TO DETERMINE IF POINT IS FAR FROM CLUSTER CENTER... NEED TO MAKE
    // DYNAMIC BASED ON REAL DATA
  }

  private void logAnomalyDetection(UserBehaviorModel behavior, boolean isAnomalous,
                                   double distance, int cluster) {
    if (isAnomalous) {
      logger.warn("Anomalies detected: User={}, Distance={}, Cluster={}",
              behavior.getUserID(), distance, cluster);
    } else {
      logger.debug("Normal behavior: User={} , Distance={} , Cluster={}",
              behavior.getUserID(), distance, cluster);
    }
  }
}