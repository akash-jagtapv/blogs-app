package com.blogs.app.mapper;

import com.blogs.app.dto.*;
import com.blogs.app.entity.Post;

public class PostMapper {

    public static PostResponse toResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setBody(post.getBody());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        response.setAuthorId(post.getAuthor().getId());
        response.setAuthorUsername(post.getAuthor().getUsername());

        return response;
    }

    public static Post toEntity(CreatePostRequest request) {
        Post post = new Post();

        post.setTitle(request.getTitle());
        post.setBody(request.getBody());

        return post;
    }

    public static void updateEntity(Post existingPost, UpdatePostRequest request) {
        existingPost.setTitle(request.getTitle());
        existingPost.setBody(request.getBody());
    }
}
