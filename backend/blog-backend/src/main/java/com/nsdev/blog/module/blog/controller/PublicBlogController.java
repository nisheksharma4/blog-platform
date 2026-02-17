package com.nsdev.blog.module.blog.controller;

import com.nsdev.blog.common.utils.ResponseStructure;
import com.nsdev.blog.module.blog.dto.BlogResponseDto;
import com.nsdev.blog.module.blog.service.BlogService;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:5500", "http://localhost:5500", "null"}, 
             allowCredentials = "true")
@RequiredArgsConstructor
public class PublicBlogController {
    
    private final BlogService blogService;
    
    @GetMapping
	public ResponseEntity<ResponseStructure<List<BlogResponseDto>>> getAllPublishedBlogs(
	        @RequestParam(defaultValue = "0") @Min(0) int page,
	        @RequestParam(defaultValue = "10") @Min(1) int size) {
		
	    List<BlogResponseDto> blogs = blogService.getAllPublishedBlogs(page, size);
	    return ResponseStructure.create(HttpStatus.OK, "Fetched All the Published Blogs.", blogs);
	}
    
    @GetMapping("/slug/{slug}")
	public ResponseEntity<ResponseStructure<BlogResponseDto>> getBlogBySlug(@PathVariable String slug) {
		BlogResponseDto blogBySlug = blogService.getBlogBySlug(slug);
		
		return ResponseStructure.create(HttpStatus.OK, "Blogs with Slugs.", blogBySlug);
	}
}