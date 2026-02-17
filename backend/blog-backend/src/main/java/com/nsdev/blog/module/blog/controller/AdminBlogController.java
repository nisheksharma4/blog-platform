package com.nsdev.blog.module.blog.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nsdev.blog.common.utils.ResponseStructure;
import com.nsdev.blog.module.blog.dto.BlogRequestDto;
import com.nsdev.blog.module.blog.dto.BlogResponseDto;
import com.nsdev.blog.module.blog.service.BlogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/blogs")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:5500", "file:///"})
@RequiredArgsConstructor
public class AdminBlogController {
	
	private final BlogService blogService;
	
	@PostMapping
	public ResponseEntity<ResponseStructure<BlogResponseDto>> createBlog(
			@Valid @RequestBody BlogRequestDto blogRequestDto) {
		BlogResponseDto responseDto = blogService.createBlog(blogRequestDto);
		
		 return ResponseStructure.create(
		            HttpStatus.CREATED,
		            "Blog Created Successfully",
		            responseDto
		    );
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
