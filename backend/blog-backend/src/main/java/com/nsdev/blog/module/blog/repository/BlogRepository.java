package com.nsdev.blog.module.blog.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.nsdev.blog.module.blog.model.Blog;
import java.util.Optional;


@Repository
public interface BlogRepository extends MongoRepository<Blog, String>{
	
	Optional<Blog> findBySlug(String slug);
}
