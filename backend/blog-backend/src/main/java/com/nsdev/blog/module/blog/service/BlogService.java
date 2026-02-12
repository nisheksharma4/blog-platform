package com.nsdev.blog.module.blog.service;

import java.util.List;

import com.nsdev.blog.module.blog.dto.BlogRequestDto;
import com.nsdev.blog.module.blog.dto.BlogResponseDto;

public interface BlogService {
	
	BlogResponseDto createBlog(BlogRequestDto requestDto);
	
	BlogResponseDto updateBlog(String id, BlogRequestDto requestDto);
	
	void deleteBlog(String id);
	
	BlogResponseDto getBlogById(String id);
	
	BlogResponseDto getBlogBySlug(String slug);
	
	List<BlogResponseDto> getAllPublishedBlogs(int page, int size);
}
