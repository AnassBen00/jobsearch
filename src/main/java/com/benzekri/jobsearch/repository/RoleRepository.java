package com.benzekri.jobsearch.repository;

import com.benzekri.jobsearch.model.ERole;
import com.benzekri.jobsearch.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role,String> {
    Optional<Role> findByName(ERole name);
}
