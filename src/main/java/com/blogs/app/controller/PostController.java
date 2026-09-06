package com.blogs.app.controller;

import com.blogs.app.dto.*;
import com.blogs.app.entity.Post;
import com.blogs.app.mapper.PostMapper;
import com.blogs.app.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody CreatePostRequest request, @RequestHeader("X-User-Id") Long requestingUserId) {
        Post createdPost = postService.createPost(PostMapper.toEntity(request), requestingUserId);

        return ResponseEntity.status(HttpStatus.CREATED).body(PostMapper.toResponse(createdPost));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();

        return ResponseEntity.ok(posts.stream().map(PostMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);

        return ResponseEntity.ok(PostMapper.toResponse(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @Valid @RequestBody UpdatePostRequest request, @RequestHeader("X-User-Id") Long requestingUserId) {
        Post updatedPost = postService.updatePost(id, request, requestingUserId);

        return ResponseEntity.ok(PostMapper.toResponse(updatedPost));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable Long id, @RequestHeader("X-User-Id") Long requestingUserId) {
        postService.deletePost(id, requestingUserId);

        return ResponseEntity.noContent().build();
    }
}
