package com.benzekri.jobsearch.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Notification")
@Getter
@Setter
public class Notification {
    @Id
    private String id;

    private String userId;
    private String jobId;
    private String message;
    private boolean isRead;
    private LocalDateTime createdTime;

}
