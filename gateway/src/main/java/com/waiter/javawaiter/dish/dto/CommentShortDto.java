package com.waiter.javawaiter.dish.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentShortDto {
    private Long commentId;
    @NotBlank
    private String comment;
}
