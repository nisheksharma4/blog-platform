package com.nsdev.blog.module.blog.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BlogResponseDto {
    
	
    private String id;
    
    private String title;
    
    private String slug;
    
    private String excerpt;
    
    private String content;
    
    private String featuredImageUrl;
    
    private List<String> tags;
    
    private LocalDateTime publishedAt;
    
    // 🤔 Question for you: Why don't we include these?
    // - content (hint: think about blog list vs detail page)
    // - status
    // - createdAt / updatedAt
}
