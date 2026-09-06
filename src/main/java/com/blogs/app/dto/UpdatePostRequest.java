package com.blogs.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UpdatePostRequest {
    @NotNull(message = "Post title cannot be null")
    private String title;

    @NotNull(message = "Post Body cannot be null")
    @Length(max = 5000, message = "Post Body should contain maximum 5000 characters")
    private String body;
}
