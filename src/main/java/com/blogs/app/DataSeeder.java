package com.blogs.app;

import com.blogs.app.entity.Post;
import com.blogs.app.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDateTime;

public class DataSeeder implements CommandLineRunner {

    @Autowired
    private final PostRepository postRepository;

    public DataSeeder(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Post post = new Post();
        post.setTitle("My First Blog Post");
        post.setBody("This is just test data to prove saving works.");
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        postRepository.save(post);

        System.out.println("Seeded a post with id: " + post.getId());
    }
}
