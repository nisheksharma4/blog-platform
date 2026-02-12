package com.nsdev.blog.module.blog.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

import org.hibernate.validator.constraints.URL;

@Data
public class BlogRequestDto {
    
    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must be less than 150 characters")
    private String title;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    @Size(max = 300, message = "Excerpt must be less than 300 characters")
    private String excerpt;
    
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", 
             message = "Slug must be lowercase letters, numbers, and hyphens only")
    private String slug;
    
    @URL(message = "Featured image must be a valid URL")
    private String featuredImageUrl;
    
    @Size(max = 5, message = "Maximum 5 tags allowed")
    private List<@Size(max = 20) String> tags;
    
    private String status; 
}
