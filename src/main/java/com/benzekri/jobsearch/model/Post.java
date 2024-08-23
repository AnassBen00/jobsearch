package com.benzekri.jobsearch.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "JobPost")
@ToString
@Getter
@Setter
public class Post {
    private String profile;
    private String desc;
    private int exp;
    private String[] techs;
    private LocalDateTime expirationDate;

    public Post(String profile, String desc, int exp, String[] techs) {
        this.profile = profile;
        this.desc = desc;
        this.exp = exp;
        this.techs = techs;
    }
}
