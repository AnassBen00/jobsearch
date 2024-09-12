package com.benzekri.jobsearch.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Applications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Application {

    @Id
    private String id;
    private String jobId;
    private String userId;
    private String coverLetter;
    private String resumePath; // Path to the resume file stored on the server
    private LocalDateTime applicationDate;
    private String status;


}
