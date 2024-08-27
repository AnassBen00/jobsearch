package com.benzekri.jobsearch.repository;

import com.benzekri.jobsearch.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByJobId(String jobId);
    List<Application> findByUserId(String userId);
}
