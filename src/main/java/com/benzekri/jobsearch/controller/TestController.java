package com.benzekri.jobsearch.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/jobseeker")
    @PreAuthorize("hasRole('JOBSEEKER') or hasRole('EMPLOYER') or hasRole('ADMIN')")
    public String userAccess() {
        return "Jobseeker Content.";
    }

    @GetMapping("/employer")
    @PreAuthorize("hasRole('EMPLOYER')")
    public String moderatorAccess() {
        return "Employer Content.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}
