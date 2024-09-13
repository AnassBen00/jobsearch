package com.benzekri.jobsearch.controller;

import com.benzekri.jobsearch.model.Application;
import com.benzekri.jobsearch.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ApplicationController.class)
public class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    private Application application;

    @BeforeEach
    void setUp() {
        application = new Application("applicationId","job12","user1","Sample cover letter","pathResume", LocalDateTime.now(), "SENT");
    }

    @Test
    @WithMockUser(roles = {"JOBSEEKER"})
    public void applyToJobTest() throws Exception{

        Mockito.when(applicationService.applyToJob(anyString(), anyString(), any(MultipartFile.class), anyString())).thenReturn(application);

        // Create a mock MultipartFile for the resume
        MockMultipartFile mockResume = new MockMultipartFile(
                "resume",
                "resume.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "Mock PDF Content".getBytes()
        );

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/applications/apply")
                        .file(mockResume)
                        .param("jobId", "job12")
                        .param("userId", "user1")
                        .param("coverLetter", "Sample cover letter")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is("user1")))
                .andExpect(jsonPath("$.jobId", is("job12")))
                .andExpect(jsonPath("$.resumePath", is("pathResume")))
                .andExpect(jsonPath("$.coverLetter", is("Sample cover letter")));
    }

    @Test
    @WithMockUser(roles = "EMPLOYER")
    void testUpdateApplicationStatus() throws Exception {

        application.setStatus("APPROVED");

        Mockito.when(applicationService.updateApplicationStatus(anyString(), anyString()))
                .thenReturn(application);

        mockMvc.perform(put("/api/applications/{id}/status", "applicationId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"APPROVED\"")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    @WithMockUser(roles = "EMPLOYER")
    void testUpdateApplicationStatus_NotFound() throws Exception {
        Mockito.when(applicationService.updateApplicationStatus(anyString(), anyString()))
                .thenThrow(new RuntimeException("Application not found with id: applicationId"));

        mockMvc.perform(put("/api/applications/{id}/status", "applicationId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"APPROVED\"")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
