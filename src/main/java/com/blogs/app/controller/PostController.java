package com.blogs.app.controller;

import com.blogs.app.entity.Post;
import com.blogs.app.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post createdPost = postService.createPost(post);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);

        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post post) {
        Post updatedpost = postService.updatePost(id, post);

        return ResponseEntity.ok(updatedpost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable Long id) {
        postService.deletePost(id);

        return ResponseEntity.noContent().build();
    }
}
