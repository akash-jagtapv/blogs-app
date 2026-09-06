package com.blogs.app.controller;

import com.blogs.app.dto.CreatePostRequest;
import com.blogs.app.dto.UpdatePostRequest;
import com.blogs.app.entity.Post;
import com.blogs.app.entity.User;
import com.blogs.app.exception.PostNotFoundException;
import com.blogs.app.exception.UnauthorizedActionException;
import com.blogs.app.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    void createPost_withValidRequest_returns201() throws Exception {
        CreatePostRequest request = new CreatePostRequest();
        request.setTitle("New Post");
        request.setBody("New Body");

        User author = new User();
        author.setId(1L);
        author.setUsername("akash");

        Post savedPost = new Post();
        savedPost.setId(1L);
        savedPost.setTitle("New Post");
        savedPost.setBody("New Body");
        savedPost.setAuthor(author);

        when(postService.createPost(any(Post.class), eq(1L))).thenReturn(savedPost);

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-User-Id", 1L)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Post"))
                .andExpect(jsonPath("$.authorId").value(1))
                .andExpect(jsonPath("$.authorUsername").value("akash"));
    }

    @Test
    void createPost_withBlankTitle_returns400() throws Exception {
        CreatePostRequest request = new CreatePostRequest();
        request.setTitle(null);
        request.setBody("Some Body");

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePost_whenRequestingUserIsAuthor_returns200() throws Exception {
        UpdatePostRequest request = new UpdatePostRequest();
        request.setTitle("Updated Title");
        request.setBody("Updated Body");

        User author = new User();
        author.setId(1L);
        author.setUsername("akash");

        Post updatedPost = new Post();
        updatedPost.setId(1L);
        updatedPost.setTitle("Updated Title");
        updatedPost.setBody("Updated Body");
        updatedPost.setAuthor(author);

        when(postService.updatePost(eq(1L), any(UpdatePostRequest.class), eq(1L)))
                .thenReturn(updatedPost);

        mockMvc.perform(put("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void updatePost_whenRequestingUserIsNotAuthor_returns403() throws Exception {
        UpdatePostRequest request = new UpdatePostRequest();
        request.setTitle("Hacked Title");
        request.setBody("Hacked Body");

        when(postService.updatePost(eq(1L), any(UpdatePostRequest.class), eq(2L)))
                .thenThrow(new UnauthorizedActionException("User is not authorized to update this post"));

        mockMvc.perform(put("/api/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", 2L)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
