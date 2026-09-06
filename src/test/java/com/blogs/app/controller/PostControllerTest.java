package com.blogs.app.controller;

import com.blogs.app.entity.Post;
import com.blogs.app.entity.User;
import com.blogs.app.exception.PostNotFoundException;
import com.blogs.app.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Test
    void getPostById_whenPostExists_returns200AndPost() throws Exception {
        User author = new User();
        author.setId(1L);
        author.setUsername("akash");

        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Post");
        post.setBody("Test Body");
        post.setAuthor(author);

        when(postService.getPostById(1L)).thenReturn(post);

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Post"));
    }

    @Test
    void getPostById_whenPostDoesNotExist_returns404() throws Exception {
        when(postService.getPostById(999L)).thenThrow(new PostNotFoundException(999L));

        mockMvc.perform(get("/api/posts/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Post not found with the id: 999"));
    }
}
