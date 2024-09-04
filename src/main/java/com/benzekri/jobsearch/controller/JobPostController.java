package com.benzekri.jobsearch.controller;

import com.benzekri.jobsearch.model.Post;
import com.benzekri.jobsearch.repository.PostRepository;
import com.benzekri.jobsearch.repository.SearchPostRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class JobPostController {
    @Autowired
    PostRepository repository;

    @Autowired
    SearchPostRepository searchRepository;

    @RequestMapping(value = "/")
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui/index.html");
    }

    @PreAuthorize("hasRole('JOBSEEKER') or hasRole('EMPLOYER') or hasRole('ADMIN')")
    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return repository.findAll();
    }

    @GetMapping("/posts/active")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<List<Post>> getActiveJobs() {
        List<Post> activeJobs = repository.findByExpirationDateAfter(LocalDateTime.now());
        return ResponseEntity.ok(activeJobs);
    }

    @PreAuthorize("hasRole('JOBSEEKER') or hasRole('EMPLOYER') or hasRole('ADMIN')")
    @GetMapping("/posts/{text}")
    public List<Post> searchPosts(@PathVariable String text){
        return searchRepository.findPosts(text);
    }

    @PostMapping("/post")
    @PreAuthorize("hasRole('EMPLOYER')")
    public Post addPost(@RequestBody Post newPost){
        if (newPost.getExpirationDate() == null){
            //set expiration date to 30 days if not already set
            newPost.setExpirationDate(LocalDateTime.now().plusDays(30));
        }
        return repository.save(newPost);
    }

    // New method for filtering with multiple criteria
    @PreAuthorize("hasRole('JOBSEEKER') or hasRole('EMPLOYER') or hasRole('ADMIN')")
    @GetMapping("/posts/filter")
    public ResponseEntity<List<Post>> searchPostsByFilters(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "minSalary", required = false) Double minSalary,
            @RequestParam(value = "maxSalary", required = false) Double maxSalary,
            @RequestParam(value = "tags", required = false) List<String> tags) {

        List<Post> jobs = searchRepository.filterPosts(category, location, minSalary, maxSalary, tags);
        return ResponseEntity.ok(jobs);
    }

}
