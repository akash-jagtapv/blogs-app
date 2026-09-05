package com.blogs.app.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Long id) {
        super("Post not found with the id: " + id);
    }
}
