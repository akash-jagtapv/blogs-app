package com.blogs.app;

import com.blogs.app.entity.Post;
import com.blogs.app.service.PostService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final PostService postService;

    public DataSeeder(PostService postService) {
        this.postService = postService;
    }

    @Override
    public void run(String... args) throws Exception {
        Post post = new Post();
        post.setTitle("My First Blog Post");
        post.setBody("This is just test data to prove saving works.");

        postService.createPost(post);

        System.out.println("Seeded a post with id: " + post.getId());
    }
}
