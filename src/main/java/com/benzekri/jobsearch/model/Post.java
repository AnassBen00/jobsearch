package com.benzekri.jobsearch.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "JobPost")
@ToString
@Getter
@Setter
public class Post {

    @Id
    private String id;
    private String profile;
    private String desc;
    private int exp;
    private String[] techs;
    private LocalDateTime expirationDate;
    private String category;
    private List<String> tags;
    private String location;
    private Double minSalary;
    private Double maxSalary;
    private String employerId;

    public Post(String profile, String desc, int exp, String[] techs) {
        this.profile = profile;
        this.desc = desc;
        this.exp = exp;
        this.techs = techs;
    }
}
