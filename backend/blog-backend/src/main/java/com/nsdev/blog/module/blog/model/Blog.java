package com.nsdev.blog.module.blog.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "blogs")
@CompoundIndexes({
    @CompoundIndex(name = "slug_unique", def = "{'slug': 1}", unique = true),
    @CompoundIndex(name = "published_status", def = "{'status': 1, 'publishedAt': -1}")
})


public class Blog {
    
    @Id
    private String id;
    
    @Field("title")
    private String title;
    
    @Field("slug")
    private String slug;
    
    @Field("excerpt")
    private String excerpt;
    
    @Field("content")
    private String content;
    
    @Field("featuredImageUrl")
    private String featuredImageUrl;
    
    @Field("tags")
    private List<String> tags;
    
    @Field("status")
    private Status status;
    
    @Field("createdAt")
    private LocalDateTime createdAt;
    
    @Field("updatedAt")
    private LocalDateTime updatedAt;
    
    @Field("publishedAt")
    private LocalDateTime publishedAt;
    
    public enum Status {
        DRAFT,
        PUBLISHED
    }
}