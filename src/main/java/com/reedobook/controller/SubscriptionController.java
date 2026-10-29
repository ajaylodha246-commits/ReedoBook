package com.reedobook.controller;

import com.reedobook.service.SubscriptionService;
import com.reedobook.entity.Subscription;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveSubscription(@Valid @RequestBody Subscription subscription) {
        try {
            Subscription savedSubscription = subscriptionService.saveSubscription(subscription);
            logger.info("Subscription saved successfully with id: {}", savedSubscription.getId());
            return ResponseEntity.ok(savedSubscription);
        } catch (Exception e) {
            logger.error("Error saving subscription", e);
            return ResponseEntity.status(500).body("Error saving subscription: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Subscription>> getSubscriptionsByUser(@PathVariable Long userId) {
        List<Subscription> subscriptions = subscriptionService.findByUserId(userId);
        if (subscriptions.isEmpty()) {
            logger.info("No subscriptions found for userId: {}", userId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/getActiveSubscriptionByUserId/{userId}")
    public ResponseEntity<List<Subscription>> getActiveSubscriptionByUserId(@PathVariable Long userId) {
        List<Subscription> subscriptions = subscriptionService.findActiveSubscriptionByUserId(userId);
        if (subscriptions.isEmpty()) {
            logger.info("No subscriptions found for userId: {}", userId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/getAllSubscription")
    public ResponseEntity<List<Subscription>> getAllSubscription() {
        List<Subscription> subscriptions = subscriptionService.findAllSubscriptions();
        if (subscriptions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(subscriptions);
    }

}
