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

import com.nsdev.blog.common.exception.BlogNotFoundException;
import com.nsdev.blog.module.blog.dto.BlogRequestDto;
import com.nsdev.blog.module.blog.dto.BlogResponseDto;
import com.nsdev.blog.module.blog.model.Blog;
import com.nsdev.blog.module.blog.repository.BlogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogServiceImpl implements BlogService{
	
	final BlogRepository blogRepository;

	@Override
	public BlogResponseDto createBlog(BlogRequestDto requestDto) {
		
		log.info("Creating new blog with title: {}", requestDto.getTitle());
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
	    log.info("Blog created successfully with id: {}", savedBlog.getId());
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
		log.info("Updating blog with id: {}", id);
		if(!blogRepository.existsById(id)) {
		    throw new BlogNotFoundException("Blog does not exist with id: " + id);
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
		
		log.info("Blog updated successfully with id: {}", save.getId());
		
		return convertToResponseDto(save);
	}

	@Override
	public void deleteBlog(String id) {
		
		log.info("Deleting Blog by Id : {}", id);
		
		if(!blogRepository.existsById(id)) {
		    throw new BlogNotFoundException("Blog does not exist with id: " + id);
		}
		
		blogRepository.deleteById(id);
		log.info("Blog Deleted Successfully with id: {}", id);
		
	}

	@Override
	public BlogResponseDto getBlogById(String id) {
		
		log.info("Fetching blog By id : {}", id);
		// Fetch blog or throw exception if not found
	    Blog blog = blogRepository.findById(id)
	            .orElseThrow(() -> new BlogNotFoundException("Blog does not exist with id: " + id));
	    
	    log.info("Blog Fetched Successfully with id: {}", id);
	    // Convert to response DTO
	    return convertToResponseDto(blog);
	}
	
	@Override
	public BlogResponseDto getBlogBySlug(String slug) {
		
		log.info("Fetching Blog By Slugs : ", slug);
	    Blog bySlug = blogRepository.findBySlug(slug)
	            .orElseThrow(() -> new BlogNotFoundException("Blog not found with slug " + slug));
	    
	    log.info("Blog Fetched Successfully by slug : {}",slug);
	    return convertToResponseDto(bySlug);
	}

	@Override
	public List<BlogResponseDto> getAllPublishedBlogs(int page, int size) {
		
		log.info("Fetching All published Blog - page : {}, size : {}", page, size);
		
		Page<Blog> blogPage = blogRepository.findByStatus(Blog.Status.PUBLISHED, PageRequest.of(page, size));
		
		List<Blog> content = blogPage.getContent();
		
		log.info("Fetched {} blogs",blogPage.getContent().size());
		 return content.stream()
        .map(this::convertToResponseDto)
        .collect(Collectors.toList());
		
	}

}
