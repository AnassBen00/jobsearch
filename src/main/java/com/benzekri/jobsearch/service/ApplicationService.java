package com.benzekri.jobsearch.service;

import com.benzekri.jobsearch.model.Application;
import com.benzekri.jobsearch.model.User;
import com.benzekri.jobsearch.repository.ApplicationRepository;
import com.benzekri.jobsearch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ApplicationService {

    private final String UPLOAD_DIR = "uploads/resumes/";

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    public Application applyToJob(String jobId, String userId, MultipartFile resume, String coverLetter) throws IOException {
        // Save the resume to the server
        String resumeFileName = saveResume(resume);

        // Create an Application object
        Application application = new Application();
        application.setJobId(jobId);
        application.setUserId(userId);
        application.setResumePath(resumeFileName);
        application.setCoverLetter(coverLetter);
        application.setApplicationDate(LocalDateTime.now());

        // Save the application to the database
        return applicationRepository.save(application);
    }

    private String saveResume(MultipartFile resume) throws IOException {
        if (resume.isEmpty()) {
            throw new IOException("Empty file.");
        }

        // Create the uploads directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save the file locally
        String fileName = resume.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(resume.getInputStream(), filePath);

        return fileName;
    }

    public Application updateApplicationStatus(String applicationId, String status) {
        Optional<Application> applicationOptional = applicationRepository.findById(applicationId);

        if (!applicationOptional.isPresent()) {
            throw new RuntimeException("Application not found with id: " + applicationId);
        }
        Application application = applicationOptional.get();
        application.setStatus(status); // Update the status
        Application upadatedApplcation = applicationRepository.save(application); // Save the updated application

        // Send email notification to the applicant
        String subject = "Application Status Update for Job: " + application.getJobId();
        String body = "Your application status has been updated to: " + status;

        // finding User email
        Optional<User> user = userRepository.findById(application.getUserId());
        if (user.isPresent()){
            emailService.sendEmail(user.get().getEmail(), subject, body);

        }
        else {
            throw new RuntimeException("User not found with id: " + applicationId);
        }
        return upadatedApplcation;
    }
}