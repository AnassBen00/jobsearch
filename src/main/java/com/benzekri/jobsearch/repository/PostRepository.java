package com.benzekri.jobsearch.repository;

import com.benzekri.jobsearch.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends MongoRepository<Post,String> {

    List<Post> findByCategory(String category);
    List<Post> findByTagsIn(List<String> tags);
    List<Post> findByCategoryAndTagsIn(String category, List<String> tags);
    List<Post> findByExpirationDateAfter(LocalDateTime now);

}
