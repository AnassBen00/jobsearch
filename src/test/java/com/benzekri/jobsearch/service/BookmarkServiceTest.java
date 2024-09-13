package com.benzekri.jobsearch.service;

import com.benzekri.jobsearch.model.Bookmark;
import com.benzekri.jobsearch.repository.BookmarkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookmarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @InjectMocks
    private BookmarkService bookmarkService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveBookmark_Success() {
        String userId = "user123";
        String jobId = "job123";

        when(bookmarkRepository.existsByUserIdAndJobId(userId, jobId)).thenReturn(false);

        Bookmark bookmark = new Bookmark(jobId, userId);
        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(bookmark);

        Bookmark result = bookmarkService.saveBookmark(userId, jobId);
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(jobId, result.getJobId());

        verify(bookmarkRepository, times(1)).existsByUserIdAndJobId(userId, jobId);
        verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
    }

    @Test
    public void testSaveBookmark_AlreadyExists() {
        String userId = "user123";
        String jobId = "job123";

        when(bookmarkRepository.existsByUserIdAndJobId(userId, jobId)).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookmarkService.saveBookmark(userId, jobId);
        });

        assertEquals("Job post already bookmarked !", exception.getMessage());

        verify(bookmarkRepository, never()).save(any(Bookmark.class));
    }

    @Test
    public void testGetBookmarksByUserId_Success() {
        String userId = "user123";

        List<Bookmark> bookmarks = Arrays.asList(
                new Bookmark("job1", userId),
                new Bookmark("job2", userId)
        );

        when(bookmarkRepository.findByUserId(userId)).thenReturn(bookmarks);

        List<Bookmark> result = bookmarkService.getBookmarksByUserId(userId);
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(bookmarkRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testDeleteBookmark_Success() {
        String userId = "user123";
        String jobId = "job123";

        when(bookmarkRepository.existsByUserIdAndJobId(userId, jobId)).thenReturn(true);

        bookmarkService.deleteBookmark(userId, jobId);

        verify(bookmarkRepository, times(1)).deleteByUserIdAndJobId(userId, jobId);
    }

    @Test
    public void testDeleteBookmark_NotFound() {
        String userId = "user123";
        String jobId = "job123";

        when(bookmarkRepository.existsByUserIdAndJobId(userId, jobId)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookmarkService.deleteBookmark(userId, jobId);
        });

        assertEquals("bookmark not found !", exception.getMessage());

        verify(bookmarkRepository, never()).deleteByUserIdAndJobId(userId, jobId);
    }
}
