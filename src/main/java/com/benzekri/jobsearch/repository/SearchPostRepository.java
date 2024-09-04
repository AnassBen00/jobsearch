package com.benzekri.jobsearch.repository;

import com.benzekri.jobsearch.model.Post;

import java.util.List;

public interface SearchPostRepository {
    List<Post> findPosts(String text);
    List<Post> filterPosts(String category, String location, Double minSalary, Double maxSalary, List<String> tags);
}
