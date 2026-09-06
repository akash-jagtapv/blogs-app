package com.blogs.app.service;

import com.blogs.app.entity.Post;
import com.blogs.app.exception.PostNotFoundException;
import com.blogs.app.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Test
    void getPostById_whenPostExists_returnsPost() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Post");

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        PostService postService = new PostService(postRepository);
        Post result = postService.getPostById(1L);

        assertThat(result.getTitle()).isEqualTo("Test Post");
    }

    @Test
    void getPostById_whenPostDoesNotExist_throwsException() {
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        PostService postService = new PostService(postRepository);

        assertThatThrownBy(() -> postService.getPostById(999L))
                .isInstanceOf(PostNotFoundException.class);
    }
}
