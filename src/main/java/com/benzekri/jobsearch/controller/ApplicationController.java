package com.benzekri.jobsearch.controller;

import com.benzekri.jobsearch.model.Application;
import com.benzekri.jobsearch.service.ApplicationService;
import com.benzekri.jobsearch.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/apply")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<Application> applyToJob(
            @RequestParam("jobId") String jobId,
            @RequestParam("userId") String userId,
            @RequestParam("resume") MultipartFile resume,
            @RequestParam("coverLetter") String coverLetter) {

        try {
            Application application = applicationService.applyToJob(jobId, userId, resume, coverLetter);
            return ResponseEntity.ok(application);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Application> updateApplicationStatus(@PathVariable("id") String applicationId, @RequestBody String status) {
        try {
            Application updatedApplication = applicationService.updateApplicationStatus(applicationId, status);
            // creating a notification
            String message = "Your application has been updated to: " + status;
            notificationService.createNotification(updatedApplication.getUserId(), updatedApplication.getJobId(), message);
            return ResponseEntity.ok(updatedApplication);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }
}
