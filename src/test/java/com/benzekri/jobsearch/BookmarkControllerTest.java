package com.benzekri.jobsearch;

import com.benzekri.jobsearch.controller.BookmarkController;
import com.benzekri.jobsearch.model.Bookmark;
import com.benzekri.jobsearch.service.BookmarkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookmarkController.class)
public class BookmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookmarkService bookmarkService;

    @Test
    @WithMockUser(roles = {"JOBSEEKER"})
    public void testSaveBookmark() throws Exception {
        // Mocking the service method to return a Bookmark object
        Bookmark mockBookmark = new Bookmark("job123", "user1");

        Mockito.when(bookmarkService.saveBookmark(anyString(), anyString())).thenReturn(mockBookmark);

        // Perform the POST request
        mockMvc.perform(post("/api/bookmarks/save")
                        .with(csrf())
                        .param("userId", "user1")
                        .param("jobId", "job123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is("user1")))
                .andExpect(jsonPath("$.jobId", is("job123")));
    }

    @Test
    @WithMockUser(roles = {"JOBSEEKER"})
    public void testGetUserBookmarks() throws Exception {
        // Mocking the service method to return a list of bookmarks
        Bookmark bookmark1 = new Bookmark("job123", "user1");

        Bookmark bookmark2 = new Bookmark("job456", "user1");

        List<Bookmark> bookmarks = Arrays.asList(bookmark1, bookmark2);

        Mockito.when(bookmarkService.getBookmarksByUserId("user1")).thenReturn(bookmarks);

        // Perform the GET request
        mockMvc.perform(get("/api/bookmarks/user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].jobId", is("job123")))
                .andExpect(jsonPath("$[1].jobId", is("job456")));
    }

    @Test
    @WithMockUser(roles = {"JOBSEEKER"})
    public void testRemoveBookmark() throws Exception {
        // Mocking the service method to do nothing (void method)
        Mockito.doNothing().when(bookmarkService).deleteBookmark("user1", "job123");

        // Perform the DELETE request
        mockMvc.perform(delete("/api/bookmarks/remove")
                        .with(csrf())
                        .param("userId", "user1")
                        .param("jobId", "job123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
