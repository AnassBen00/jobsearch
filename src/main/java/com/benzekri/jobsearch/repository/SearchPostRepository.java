package com.benzekri.jobsearch.repository;

import com.benzekri.jobsearch.model.Post;

import java.util.List;

public interface SearchPostRepository {
    List<Post> findPosts(String text);
}
