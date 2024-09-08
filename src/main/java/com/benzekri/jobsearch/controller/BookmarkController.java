package com.benzekri.jobsearch.controller;

import com.benzekri.jobsearch.model.Bookmark;
import com.benzekri.jobsearch.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {
    @Autowired
    private BookmarkService bookmarkService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<Bookmark> saveBookmark(@RequestParam("userId") String userId,
                                                 @RequestParam("jobId") String jobId) {
        Bookmark bookmark = bookmarkService.saveBookmark(userId, jobId);
        return ResponseEntity.ok(bookmark);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<List<Bookmark>> getUserBookmarks(@PathVariable("userId") String userId) {
        List<Bookmark> bookmarks = bookmarkService.getBookmarksByUserId(userId);
        return ResponseEntity.ok(bookmarks);
    }

    @DeleteMapping("/remove")
    @PreAuthorize("hasRole('JOBSEEKER')")
    public ResponseEntity<Void> removeBookmark(@RequestParam("userId") String userId,
                                               @RequestParam("jobId") String jobId) {
        bookmarkService.deleteBookmark(userId, jobId);
        return ResponseEntity.noContent().build();
    }
}
