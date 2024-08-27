package com.benzekri.jobsearch.controller;

import com.benzekri.jobsearch.model.Application;
import com.benzekri.jobsearch.repository.ApplicationRepository;
import com.benzekri.jobsearch.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/apply")
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
}
