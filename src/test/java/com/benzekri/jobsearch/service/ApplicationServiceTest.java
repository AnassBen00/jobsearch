package com.benzekri.jobsearch.service;

import com.benzekri.jobsearch.model.Application;
import com.benzekri.jobsearch.model.User;
import com.benzekri.jobsearch.repository.ApplicationRepository;
import com.benzekri.jobsearch.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MultipartFile mockResume;

    @InjectMocks
    private ApplicationService applicationService;

    private Application mockApplication;

    private User mockUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up mock application
        mockApplication = new Application();
        mockApplication.setId("app123");
        mockApplication.setJobId("job123");
        mockApplication.setUserId("user123");
        mockApplication.setApplicationDate(LocalDateTime.now());
        mockApplication.setResumePath("resume.pdf");
        mockApplication.setCoverLetter("Cover letter content");

        // Set up mock user
        mockUser = new User();
        mockUser.setId("user123");
        mockUser.setEmail("user@example.com");
    }

    @Test
    public void testApplyToJob_Success() throws IOException {
        // Mock MultipartFile behavior
        when(mockResume.isEmpty()).thenReturn(false);
        when(mockResume.getOriginalFilename()).thenReturn("resume.pdf");
        when(mockResume.getInputStream()).thenReturn(mock(InputStream.class));

        // Mock repository behavior
        when(applicationRepository.save(any(Application.class))).thenReturn(mockApplication);

        // Execute the method
        Application savedApplication = applicationService.applyToJob("job123", "user123", mockResume, "Cover letter content");

        // Verify repository interaction
        verify(applicationRepository, times(1)).save(any(Application.class));

        // Verify file saving logic
        Path uploadPath = Paths.get("uploads/resumes/");
        assertTrue(Files.exists(uploadPath));

        // Assertions
        assertNotNull(savedApplication);
        assertEquals("job123", savedApplication.getJobId());
        assertEquals("user123", savedApplication.getUserId());
        assertEquals("resume.pdf", savedApplication.getResumePath());
        assertEquals("Cover letter content", savedApplication.getCoverLetter());
    }

    @Test
    public void testApplyToJob_EmptyResume() throws IOException {
        // Mock MultipartFile behavior with empty resume
        when(mockResume.isEmpty()).thenReturn(true);

        // Expect an IOException
        assertThrows(IOException.class, () -> {
            applicationService.applyToJob("job123", "user123", mockResume, "Cover letter content");
        });

        // Verify that the application is not saved if an exception is thrown
        verify(applicationRepository, never()).save(any(Application.class));
    }

    @Test
    public void testUpdateApplicationStatus_Success() {
        // Mock repository behavior
        when(applicationRepository.findById("app123")).thenReturn(Optional.of(mockApplication));
        when(applicationRepository.save(any(Application.class))).thenReturn(mockApplication);
        when(userRepository.findById("user123")).thenReturn(Optional.of(mockUser));

        // Execute the method
        Application updatedApplication = applicationService.updateApplicationStatus("app123", "ACCEPTED");

        // Verify repository interaction
        verify(applicationRepository, times(1)).findById("app123");
        verify(applicationRepository, times(1)).save(any(Application.class));

        // Verify email sending
        verify(emailService, times(1)).sendEmail(eq("user@example.com"), anyString(), anyString());

        // Assertions
        assertNotNull(updatedApplication);
        assertEquals("ACCEPTED", updatedApplication.getStatus());
    }

    @Test
    public void testUpdateApplicationStatus_ApplicationNotFound() {
        // Mock repository behavior for not finding application
        when(applicationRepository.findById("app123")).thenReturn(Optional.empty());

        // Expect RuntimeException
        assertThrows(RuntimeException.class, () -> {
            applicationService.updateApplicationStatus("app123", "ACCEPTED");
        });

        // Verify repository interaction
        verify(applicationRepository, times(1)).findById("app123");
        verify(applicationRepository, never()).save(any(Application.class));
    }

    @Test
    public void testUpdateApplicationStatus_UserNotFound() {
        // Mock repository behavior for application found but user not found
        when(applicationRepository.findById("app123")).thenReturn(Optional.of(mockApplication));
        when(userRepository.findById("user123")).thenReturn(Optional.empty());

        // Expect RuntimeException
        assertThrows(RuntimeException.class, () -> {
            applicationService.updateApplicationStatus("app123", "ACCEPTED");
        });

        // Verify repository interaction
        verify(applicationRepository, times(1)).findById("app123");
        verify(userRepository, times(1)).findById("user123");
        verify(applicationRepository, never()).save(any(Application.class));
    }
}