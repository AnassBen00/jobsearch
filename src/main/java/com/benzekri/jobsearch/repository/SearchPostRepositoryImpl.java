package com.benzekri.jobsearch.repository;

import com.benzekri.jobsearch.model.Post;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SearchPostRepositoryImpl implements SearchPostRepository{

    @Autowired
    MongoClient client;

    @Autowired
    MongoConverter converter;

    @Override
    public List<Post> findPosts(String text) {
        List<Post> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("JobSearch");
        MongoCollection<Document> collection = database.getCollection("JobPost");
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search",
                        new Document("text",
                        new Document("query", text).append("path", Arrays.asList("techs", "desc", "profile")))),
                        new Document("$sort",
                        new Document("exp", 1L)),
                        new Document("$limit", 5L)));

        result.forEach(document -> posts.add(converter.read(Post.class, document)));

        return posts;
    }

    // New method for filtering with multiple criteria
    @Override
    public List<Post> filterPosts(String category, String location, Double minSalary, Double maxSalary, List<String> tags) {
        List<Post> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("JobSearch");
        MongoCollection<Document> collection = database.getCollection("JobPost");

        List<Document> pipeline = new ArrayList<>();

        // Match stage for dynamic filters
        Document match = new Document();

        if (category != null && !category.isEmpty()) {
            match.append("category", category);
        }

        if (location != null && !location.isEmpty()) {
            match.append("location", location);
        }

        if (minSalary != null && maxSalary != null) {
            match.append("minSalary", new Document("$gte", minSalary));
            match.append("maxSalary", new Document("$lte", maxSalary));
        } else if (minSalary != null) {
            match.append("minSalary", new Document("$gte", minSalary));
        } else if (maxSalary != null) {
            match.append("maxSalary", new Document("$lte", maxSalary));
        }

        if (tags != null && !tags.isEmpty()) {
            match.append("tags", new Document("$in", tags));
        }

        // Add the match stage if any filters are present
        if (!match.isEmpty()) {
            pipeline.add(new Document("$match", match));
        }

        // Sorting stage (optional, add sorting by experience or any other field as needed)
        pipeline.add(new Document("$sort", new Document("exp", 1L)));

        // Perform aggregation with pipeline
        AggregateIterable<Document> result = collection.aggregate(pipeline);

        result.forEach(document -> posts.add(converter.read(Post.class, document)));

        return posts;
    }
}
