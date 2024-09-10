package com.benzekri.jobsearch;

import com.benzekri.jobsearch.controller.JobPostController;
import com.benzekri.jobsearch.model.Post;
import com.benzekri.jobsearch.repository.PostRepository;
import com.benzekri.jobsearch.repository.SearchPostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobPostController.class)
public class JobPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostRepository repository;

    @MockBean
    private SearchPostRepository searchRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = {"JOBSEEKER", "EMPLOYER", "ADMIN"})
    public void testGetAllPosts() throws Exception {
        Post post1 = new Post("Software Engineer", "Job Description", 2, new String[]{"Java", "Spring"});
        Post post2 = new Post("Data Scientist", "Job Description", 3, new String[]{"Python", "Machine Learning"});
        List<Post> posts = Arrays.asList(post1, post2);

        when(repository.findAll()).thenReturn(posts);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].profile").value("Software Engineer"));
    }

    @Test
    @WithMockUser(roles = {"EMPLOYER"})
    public void testGetActiveJobs() throws Exception {
        Post activePost = new Post("Software Engineer", "Job Description", 2, new String[]{"Java", "Spring"});
        activePost.setExpirationDate(LocalDateTime.now().plusDays(10));
        List<Post> activeJobs = List.of(activePost);

        when(repository.findByExpirationDateAfter(any(LocalDateTime.class))).thenReturn(activeJobs);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].profile").value("Software Engineer"));
    }

    @Test
    @WithMockUser(roles = {"JOBSEEKER", "EMPLOYER", "ADMIN"})
    public void testSearchPosts() throws Exception {
        Post post = new Post("Software Engineer", "Job Description", 2, new String[]{"Java", "Spring"});
        List<Post> posts = List.of(post);

        when(searchRepository.findPosts(anyString())).thenReturn(posts);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].profile").value("Software Engineer"));
    }

    @Test
    @WithMockUser(roles = {"EMPLOYER"})
    public void testAddPost() throws Exception {
        Post newPost = new Post("Software Engineer", "Job Description", 2, new String[]{"Java", "Spring"});
        newPost.setExpirationDate(LocalDateTime.now().plusDays(30));

        when(repository.save(any(Post.class))).thenReturn(newPost);

        mockMvc.perform(MockMvcRequestBuilders.post("/post")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"profile\":\"Software Engineer\",\"desc\":\"Job Description\",\"exp\":2,\"techs\":[\"Java\",\"Spring\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile").value("Software Engineer"));
    }

    @Test
    @WithMockUser(roles = {"JOBSEEKER", "EMPLOYER", "ADMIN"})
    public void testSearchPostsByFilters() throws Exception {
        Post post = new Post("Software Engineer", "Job Description", 2, new String[]{"Java", "Spring"});
        List<Post> posts = List.of(post);

        when(searchRepository.filterPosts(anyString(), anyString(), any(Double.class), any(Double.class), any(List.class)))
                .thenReturn(posts);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/filter")
                        .param("category", "IT")
                        .param("location", "New York")
                        .param("minSalary", "50000")
                        .param("maxSalary", "100000")
                        .param("tags", "Java,Spring"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].profile").value("Software Engineer"));
    }
}
