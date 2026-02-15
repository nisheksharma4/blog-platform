package com.nsdev.blog.common.utils;


public class SlugGenerator {
	
	public String generateSlug(String title) {
		if(title == null || title.trim().isEmpty()) {
			throw new IllegalArgumentException("Title cannot be null or empty.");
		}
		
		return title.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("-+", "-").replaceAll("^-+|-+$", "");
	}
	
	public String generateUniqueSlug(String title) {
		String baseSlug = generateSlug(title);
		long timeStamp = System.currentTimeMillis();
		return baseSlug + "-"+timeStamp;
	}
}
