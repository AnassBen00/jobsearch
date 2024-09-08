package com.benzekri.jobsearch.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document(collection = "Bookmarks")
public class Bookmark {

    @Id
    private String id;

    private String jobId;

    private String userId;

    public Bookmark(String jobId, String userId) {
        this.jobId = jobId;
        this.userId = userId;
    }
}
