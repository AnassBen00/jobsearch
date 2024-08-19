package com.benzekri.jobsearch.repository;

import com.benzekri.jobsearch.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post,String> {

}
