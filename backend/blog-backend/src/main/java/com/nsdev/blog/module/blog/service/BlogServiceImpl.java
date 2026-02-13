package com.nsdev.blog.module.blog.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nsdev.blog.module.blog.dto.BlogRequestDto;
import com.nsdev.blog.module.blog.dto.BlogResponseDto;
import com.nsdev.blog.module.blog.model.Blog;
import com.nsdev.blog.module.blog.repository.BlogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService{
	
	final BlogRepository blogRepository;

	@Override
	public BlogResponseDto createBlog(BlogRequestDto requestDto) {
		
		//1. Create Blog entity from DTO
		Blog blog = Blog.builder()
		.title(requestDto.getTitle())
		.content(requestDto.getContent())
		.excerpt(requestDto.getExcerpt())
		.featuredImageUrl(requestDto.getFeaturedImageUrl())
		.tags(requestDto.getTags())
		.status(Blog.Status.PUBLISHED)
		.createdAt(LocalDateTime.now())
		.updatedAt(LocalDateTime.now())
		.publishedAt(LocalDateTime.now())
		.build();
		
		// 2. Generate slug if not provided
	    if (requestDto.getSlug() != null && !requestDto.getSlug().trim().isEmpty()) {
	        blog.setSlug(requestDto.getSlug().toLowerCase());
	    } else {
	        blog.setSlug(generateSlug(requestDto.getTitle()));
	    }
	    
	 // 3. Save to MongoDB
	    Blog savedBlog = blogRepository.save(blog);
	    
	    // 4. Convert to Response DTO and return
	    return convertToResponseDto(savedBlog);
	}
	
	// Helper method to Generate slug from title
	private String generateSlug(String title) {
	    return title.toLowerCase()
	        .replaceAll("[^a-z0-9]+", "-")  // Replace non-alphanumeric with hyphens
	        .replaceAll("-+", "-")           // Replace multiple hyphens with single
	        .replaceAll("^-+|-+$", "");      // Remove leading/trailing hyphens
	}
	
	// Helper method to Convert Blog entity to BlogResponseDto
	private BlogResponseDto convertToResponseDto(Blog blog) {
	    BlogResponseDto dto = new BlogResponseDto();
	    dto.setId(blog.getId());
	    dto.setTitle(blog.getTitle());
	    dto.setSlug(blog.getSlug());
	    dto.setExcerpt(blog.getExcerpt());
	    dto.setContent(blog.getContent());
	    dto.setFeaturedImageUrl(blog.getFeaturedImageUrl());
	    dto.setTags(blog.getTags());
	    dto.setPublishedAt(blog.getPublishedAt());
	    return dto;
	}

	@Override
	public BlogResponseDto updateBlog(String id, BlogRequestDto requestDto) {
		if(!blogRepository.existsById(id)) {
			throw new RuntimeException("Blog does not exists.");
		}
		Blog existingBlog = blogRepository.findById(id).orElseThrow();
		
		//update fields
		existingBlog.setTitle(requestDto.getTitle());
		existingBlog.setContent(requestDto.getContent());
		existingBlog.setExcerpt(requestDto.getExcerpt());
		existingBlog.setFeaturedImageUrl(requestDto.getFeaturedImageUrl());
		existingBlog.setTags(requestDto.getTags());
		existingBlog.setUpdatedAt(LocalDateTime.now());
		
		if (requestDto.getSlug() != null && !requestDto.getSlug().trim().isEmpty()) {
	        existingBlog.setSlug(requestDto.getSlug().toLowerCase());
	    }
		
		Blog save = blogRepository.save(existingBlog);
		
		return convertToResponseDto(save);
	}

	@Override
	public void deleteBlog(String id) {
		
		if(!blogRepository.existsById(id)) {
			throw new RuntimeException("Blog does not exist By :"+id);
		}
		
		blogRepository.deleteById(id);
		
	}

	@Override
	public BlogResponseDto getBlogById(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public BlogResponseDto getBlogBySlug(String slug) {
	    Blog bySlug = blogRepository.findBySlug(slug)
	            .orElseThrow(() -> new ResponseStatusException(
	                    HttpStatus.NOT_FOUND, "Blog not found with slug " + slug));
	    return convertToResponseDto(bySlug);
	}

	@Override
	public List<BlogResponseDto> getAllPublishedBlogs(int page, int size) {
		
		Page<Blog> blogPage = blogRepository.findByStatus(Blog.Status.PUBLISHED, PageRequest.of(page, size));
		
		List<Blog> content = blogPage.getContent();
		
		 return content.stream()
        .map(this::convertToResponseDto)
        .collect(Collectors.toList());
		
	}

}
