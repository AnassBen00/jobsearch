package com.benzekri.jobsearch;

import com.benzekri.jobsearch.controller.EmployerController;
import com.benzekri.jobsearch.model.Application;
import com.benzekri.jobsearch.model.Post;
import com.benzekri.jobsearch.repository.ApplicationRepository;
import com.benzekri.jobsearch.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmployerController.class)
public class EmployerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationRepository applicationRepository;

    @MockBean
    private PostRepository postRepository;


    @Test
    @WithMockUser(roles = {"EMPLOYER"})
    public void testGetApplicationsForJob_AsEmployer_Owner() throws Exception {
        // Mocking the postRepository to return a post with the employerId "employer1"
        String jobId = "job123";
        String employerId = "employer1";

        Post mockPost = new Post("Software Engineer", "Job Description", 2, new String[]{"Java", "Spring"});
        mockPost.setEmployerId(employerId);

        Mockito.when(postRepository.findById(jobId)).thenReturn(Optional.of(mockPost));

        // Mocking the applicationRepository to return a list of applications
        Application app1 = new Application();
        app1.setId("app1");
        app1.setJobId(jobId);
        app1.setUserId("user1");
        app1.setCoverLetter("Cover Letter 1");

        Application app2 = new Application();
        app2.setId("app2");
        app2.setJobId(jobId);
        app2.setUserId("user2");
        app2.setCoverLetter("Cover Letter 2");

        List<Application> applications = Arrays.asList(app1, app2);
        Mockito.when(applicationRepository.findByJobId(jobId)).thenReturn(applications);

        // Perform the GET request
        mockMvc.perform(get("/employer/job/" + jobId + "/applications")
                        .param("employerId", employerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("app1"))
                .andExpect(jsonPath("$[1].id").value("app2"));
    }

    @Test
    @WithMockUser(roles = {"EMPLOYER"})
    public void testGetApplicationsForJob_AsEmployer_NotOwner() throws Exception {
        // Mocking the postRepository to return a post with a different employerId "employer1"
        String jobId = "job123";
        String employerId = "employer1";

        Post mockPost = new Post("Software Engineer", "Job Description", 2, new String[]{"Java", "Spring"});
        mockPost.setEmployerId(employerId);

        Mockito.when(postRepository.findById(jobId)).thenReturn(Optional.of(mockPost));

        // Perform the GET request with a different employerId (not the owner)
        mockMvc.perform(get("/employer/job/" + jobId + "/applications")
                        .param("employerId", "employer2") // Not the owner of the job
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()); // Expecting 403 Forbidden
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testGetApplicationsForJob_AsAdmin() throws Exception {
        // Mocking the postRepository to return a post with the employerId "employer1"
        String jobId = "job123";
        String employerId = "employer1";

        Post mockPost = new Post("Software Engineer", "Job Description", 2, new String[]{"Java", "Spring"});
        mockPost.setEmployerId(employerId);

        Mockito.when(postRepository.findById(jobId)).thenReturn(Optional.of(mockPost));

        // Mocking the applicationRepository to return a list of applications
        Application app1 = new Application();
        app1.setId("app1");
        app1.setJobId(jobId);
        app1.setUserId("user1");
        app1.setCoverLetter("Cover Letter 1");

        Application app2 = new Application();
        app2.setId("app2");
        app2.setJobId(jobId);
        app2.setUserId("user2");
        app2.setCoverLetter("Cover Letter 2");

        List<Application> applications = Arrays.asList(app1, app2);
        Mockito.when(applicationRepository.findByJobId(jobId)).thenReturn(applications);

        // Perform the GET request as ADMIN
        mockMvc.perform(get("/employer/job/" + jobId + "/applications")
                        .param("employerId", employerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("app1"))
                .andExpect(jsonPath("$[1].id").value("app2"));
    }
}
