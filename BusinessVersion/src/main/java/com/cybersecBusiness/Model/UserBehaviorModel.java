package com.cybersecBusiness.Model;

import java.time.LocalDateTime;

/**
 * The {@code userBehaviorModel} class is meant to monitor user behavior such as
 * log in time, data transfers, and user behavior patterns. This works class by comparing user
 * behavior to typical behavior patterns. If unusual behavior detected, admin or other sec team
 * notified.
 */
public class UserBehaviorModel {

  private String userID; // assuming IDs are like 'SO12345'  not limited to int only
  private LocalDateTime loginTime;
  private String lastLoginLocation;
  private double averageDataTransferRate;

  public UserBehaviorModel(String userID, LocalDateTime loginTime, String lastLoginLocation,
                           double averageDataTransferRate) {
    this.userID = userID;
    this.loginTime = loginTime;
    this.lastLoginLocation = lastLoginLocation;
    this.averageDataTransferRate = averageDataTransferRate;
  }

  /*
  Setters and Getters
   */

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public LocalDateTime getLoginTime() {
    return loginTime;
  }

  public void setLoginTime(LocalDateTime loginTime) {
    this.loginTime = loginTime;
  }

  public String getLastLoginLocation() {
    return lastLoginLocation;
  }

  public void setLastLoginLocation(String lastLoginLocation) {
    this.lastLoginLocation = lastLoginLocation;
  }

  public double getAverageDataTransferRate() {
    return averageDataTransferRate;
  }

  public void setAverageDataTransferRate(double averageDataTransferRate) {
    this.averageDataTransferRate = averageDataTransferRate;
  }

}