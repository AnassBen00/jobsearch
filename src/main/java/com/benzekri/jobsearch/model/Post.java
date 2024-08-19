package com.benzekri.jobsearch.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "JobPost")
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Post {
    private String profile;
    private String desc;
    private int exp;
    private String[] techs;

}
