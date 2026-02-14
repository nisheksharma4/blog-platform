package com.nsdev.blog.module.blog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nsdev.blog.common.utils.ResponseStructure;
import com.nsdev.blog.module.blog.dto.BlogRequestDto;
import com.nsdev.blog.module.blog.dto.BlogResponseDto;
import com.nsdev.blog.module.blog.service.BlogService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/blogs")
@RequiredArgsConstructor
public class AdminBlogController {
	
	private final BlogService blogService;
	
	@PostMapping
	public ResponseEntity<ResponseStructure<BlogResponseDto>> createBlog(@Valid @RequestBody BlogRequestDto blogRequestDto) {
		BlogResponseDto responseDto = blogService.createBlog(blogRequestDto);
		
		 return ResponseStructure.create(
		            HttpStatus.CREATED,
		            "Blog Created Successfully",
		            responseDto
		    );
	}
	
	
	@GetMapping("/slug/{slug}")
	public ResponseEntity<ResponseStructure<BlogResponseDto>> getBlogBySlug(@PathVariable String slug) {
		BlogResponseDto blogBySlug = blogService.getBlogBySlug(slug);
		
		return ResponseStructure.create(HttpStatus.OK, "Blogs with Slugs.", blogBySlug);
	}
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<BlogResponseDto>>> getAllPublishedBlogs(
	        @RequestParam(defaultValue = "0") @Min(0) int page,
	        @RequestParam(defaultValue = "10") @Min(1) int size) {
		
	    List<BlogResponseDto> blogs = blogService.getAllPublishedBlogs(page, size);
	    return ResponseStructure.create(HttpStatus.OK, "Fetched All the Published Blogs.", blogs);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<BlogResponseDto>> getBlogById(@PathVariable String id) {
		BlogResponseDto blogById = blogService.getBlogById(id);
		
		return ResponseStructure.create(HttpStatus.OK, "Fetched Blog By "+id, blogById);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseStructure<BlogResponseDto>> updateBlog(@PathVariable String id, @Valid @RequestBody BlogRequestDto dto){
		BlogResponseDto updateBlog = blogService.updateBlog(id, dto);
		return ResponseStructure.create(HttpStatus.OK, "Blog with "+id+" updated Successfully.", updateBlog);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<Void>> deleteBlog(@PathVariable String id) {
		blogService.deleteBlog(id);
		return ResponseStructure.create(
	            HttpStatus.OK,
	            "Blog Deleted Successfully",
	            null
	    );
	}
	
	
}
