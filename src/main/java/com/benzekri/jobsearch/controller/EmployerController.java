package com.benzekri.jobsearch.controller;

import com.benzekri.jobsearch.model.Application;
import com.benzekri.jobsearch.repository.ApplicationRepository;
import com.benzekri.jobsearch.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class EmployerController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private PostRepository postRepository;

    @PreAuthorize("hasRole('EMPLOYER') or hasRole('ADMIN')") // Seuls les employeurs et les administrateurs peuvent accéder
    @GetMapping("/employer/job/{jobId}/applications")
    public ResponseEntity<List<Application>> getApplicationsForJob(@PathVariable String jobId, @RequestParam String employerId) {
        // Vérifier que l'employeur est le propriétaire de l'offre d'emploi
        boolean isOwner = postRepository.findById(jobId)
                .map(post -> post.getEmployerId().equals(employerId))
                .orElse(false);

        if (!isOwner) {
            return ResponseEntity.status(403).build(); // Retourner une erreur 403 si l'employeur n'est pas le propriétaire de l'offre
        }

        // Récupérer les candidatures associées à l'offre d'emploi
        List<Application> applications = applicationRepository.findByJobId(jobId);
        return ResponseEntity.ok(applications);
    }
}
