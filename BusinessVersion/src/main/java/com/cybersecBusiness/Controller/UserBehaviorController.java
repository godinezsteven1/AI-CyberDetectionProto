package com.cybersecBusiness.Controller;

import com.cybersecBusiness.Model.UserBehaviorModel;
import com.cybersecBusiness.Service.DetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * The {@code UserBehaviorController} class is meant to analyze NEW behavior.
 */
@RestController
@RequestMapping("/api/business")
public class UserBehaviorController {

  @Autowired
  private DetectionService detectionService;

  @PostMapping("/behavior/train")
  public String trainModel() {
    detectionService.trainModel();
    return "Model trained :D";
  }

  @PostMapping("/behavior/analyze")
  public boolean analyzeBehavior(@RequestBody UserBehaviorModel behavior) {
    return detectionService.isAnomalousTrained(behavior);
  }
}
