package com.cybersecBusiness.Service;

import com.cybersecBusiness.Model.UserBehaviorModel;
import java.time.LocalDateTime;

public class AnamolyBehaviorService {

  // detect anomalies in user behavior
  public boolean isAnomalous(UserBehaviorModel currentBehavior, UserBehaviorModel normalBehavior) {
    /**
     * Potential Criteria for simple anomalies
     * - last log in hours should not be __  amount of expected last log in hours
     *    con - working from home, re adjust times
     * - where is person last logging in from, person has to log in company premise.
     *    con - working from home, adjust locations to user home or grant special permissions
     * - normal data transfer should not be more than __ different
     *    con - big projects
     *    log in || log in loco || data transfer
     */
    return true; // stub
  }
}
