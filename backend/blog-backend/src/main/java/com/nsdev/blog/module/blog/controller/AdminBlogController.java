package com.nsdev.blog.module.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nsdev.blog.module.blog.dto.BlogRequestDto;
import com.nsdev.blog.module.blog.dto.BlogResponseDto;
import com.nsdev.blog.module.blog.service.BlogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/blogs")
@RequiredArgsConstructor
public class AdminBlogController {
	
	private final BlogService blogService;
	
	@PostMapping
	public ResponseEntity<BlogResponseDto> createBlog(@Valid @RequestBody BlogRequestDto blogRequestDto) {
		BlogResponseDto responseDto = blogService.createBlog(blogRequestDto);
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}
	
	@GetMapping("/slug/{slug}")
	public ResponseEntity<BlogResponseDto> getBlogBySlug(@PathVariable String slug) {
		BlogResponseDto blogBySlug = blogService.getBlogBySlug(slug);
		return ResponseEntity.ok(blogBySlug);
	}
}
