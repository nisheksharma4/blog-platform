package com.nsdev.blog.common.exception;



public class BlogNotFoundException extends RuntimeException{
	
	public BlogNotFoundException(String message) {
        super("Blog Not Found");
    }
    
    public BlogNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
