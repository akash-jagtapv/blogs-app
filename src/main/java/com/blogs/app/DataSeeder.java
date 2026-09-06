package com.blogs.app;

import com.blogs.app.entity.Post;
import com.blogs.app.entity.User;
import com.blogs.app.repository.UserRepository;
import com.blogs.app.service.PostService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final PostService postService;

    private final UserRepository userRepository;

    public DataSeeder(PostService postService, UserRepository userRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        User author =  userRepository.findByUsername("akash")
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername("akash");
                    newUser.setCreatedAt(LocalDateTime.now());
                    return userRepository.save(newUser);
                });

        Post post = new Post();
        post.setTitle("My First Blog Post");
        post.setBody("This is just test data to prove saving works.");
        post.setAuthor(author);

        postService.createPost(post);

        System.out.println("Seeded a post with id: " + post.getId());
    }
}
