package com.benzekri.jobsearch.service;

import com.benzekri.jobsearch.model.Notification;
import com.benzekri.jobsearch.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    // Method to create a new notification
    public Notification createNotification(String userId, String jobId, String message){
        Notification notification = new Notification();

        notification.setUserId(userId);
        notification.setJobId(jobId);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setCreatedTime(LocalDateTime.now());

        return notification;
    }

    // Retrieve all notifications for a user
    public List<Notification> getNotificationsForUser(String userId){
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // Mark notification as read
    public void markNotificationAsRead(String notificationId){
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                ()-> new RuntimeException("Nptification not found")
        );
        notification.setRead(true);
        notificationRepository.save(notification);

    }
}
