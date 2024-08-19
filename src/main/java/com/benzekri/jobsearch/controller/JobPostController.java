package com.benzekri.jobsearch.controller;

import com.benzekri.jobsearch.model.Post;
import com.benzekri.jobsearch.repository.PostRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

@RestController
public class JobPostController {
    @Autowired
    PostRepository repository;

    @RequestMapping(value = "/")
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui/index.html");
    }

    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return repository.findAll();
    }

    @PostMapping("/post")
    public Post addPost(@RequestBody Post newPost){
        return repository.save(newPost);
    }
}
