package com.benzekri.jobsearch.controller;

import com.benzekri.jobsearch.model.Post;
import com.benzekri.jobsearch.repository.PostRepository;
import com.benzekri.jobsearch.repository.SearchPostRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
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

    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return repository.findAll();
    }

    @GetMapping("/posts/{text}")
    public List<Post> searchPosts(@PathVariable String text){
        return searchRepository.findPosts(text);
    }

    @PostMapping("/post")
    public Post addPost(@RequestBody Post newPost){
        return repository.save(newPost);
    }
}
