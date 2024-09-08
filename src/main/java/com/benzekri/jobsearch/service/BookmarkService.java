package com.benzekri.jobsearch.service;

import com.benzekri.jobsearch.model.Bookmark;
import com.benzekri.jobsearch.repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkService {

    @Autowired
    BookmarkRepository bookmarkRepository;

    public Bookmark saveBookmark(String userId, String jobId){
        if (bookmarkRepository.existsByUserIdAndJobId(userId, jobId)){
            throw new RuntimeException("Job post already bookmarked !");
        }
        return bookmarkRepository.save(new Bookmark(userId, jobId));
    }

    public List<Bookmark> getBookmarksByUserId(String userId){
        return bookmarkRepository.findByUserId(userId);
    }

    public void deleteBookmark(String userId, String jobId){
        if(!bookmarkRepository.existsByUserIdAndJobId(userId, jobId)){
            throw new RuntimeException("bookmark not found !");
        }
        bookmarkRepository.deleteByUserIdAndJobId(userId, jobId);
    }
}
