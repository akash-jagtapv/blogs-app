package com.blogs.app.service;

import com.blogs.app.entity.Post;
import com.blogs.app.entity.User;
import com.blogs.app.exception.*;
import com.blogs.app.repository.PostRepository;
import com.blogs.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post createPost(Post post, Long requestingUserId) {
        User foundUser = userRepository.findById(requestingUserId)
                        .orElseThrow(() -> new UserNotFoundException(requestingUserId));

        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setAuthor(foundUser);

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    public Post updatePost(Long id, Post updatedPost, Long requestingUserId) {
        Post existingPost = getPostById(id);

        if(!requestingUserId.equals(existingPost.getAuthor().getId())) {
            throw new UnauthorizedActionException("User is not the authorized to update this post");
        }

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setBody(updatedPost.getBody());
        existingPost.setUpdatedAt(LocalDateTime.now());

        return postRepository.save(existingPost);
    }

    public void deletePost(Long id, Long requestingUserId) {
        Post existingPost = getPostById(id);

        if(!requestingUserId.equals(existingPost.getAuthor().getId())) {
            throw new UnauthorizedActionException("User is not authorized to delete this post");
        }
        postRepository.delete(existingPost);
    }
}
