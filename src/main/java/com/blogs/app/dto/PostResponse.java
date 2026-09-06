package com.blogs.app.dto;

import java.time.LocalDateTime;

public class PostResponse {
    private Long id;

    private String title;

    private String body;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long authorId;

    private String authorUsername;
}
