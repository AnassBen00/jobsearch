package com.benzekri.jobsearch.controller;

import com.benzekri.jobsearch.model.Notification;
import com.benzekri.jobsearch.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    // Get all notifications for a user
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('JOBSEEKER') or hasRole('EMPLOYER')")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable String userId) {
        List<Notification> notifications = notificationService.getNotificationsForUser(userId);
        return ResponseEntity.ok(notifications);
    }

    // Mark a specific notification as read
    @PutMapping("/{notificationId}")
    @PreAuthorize("hasRole('JOBSEEKER') or hasRole('EMPLOYER')")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable String notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.noContent().build();
    }
}
